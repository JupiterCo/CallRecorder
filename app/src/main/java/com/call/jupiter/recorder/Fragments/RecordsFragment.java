package com.call.jupiter.recorder.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.call.jupiter.recorder.Adapters.RecordAdapter;
import com.call.jupiter.recorder.ContextMenuClick;
import com.call.jupiter.recorder.Helper.AppUtility;
import com.call.jupiter.recorder.Helper.GlobalValues;
import com.call.jupiter.recorder.Helper.Utility;
import com.call.jupiter.recorder.Models.RecordsModel;
import com.call.jupiter.recorder.R;
import com.call.jupiter.recorder.RecordsMethods.GetRecords;
import com.call.jupiter.recorder.Services.CallServices;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by batuhan on 21.08.2018.
 */

public class RecordsFragment extends Fragment{
    View rootView;
    GetRecords getRecords;
    int recordSize;
    TextView TVRecord;
    ProgressBar progressBar;
    ListView LVRecords;
    List<RecordsModel> recordsModelList = new ArrayList<>();
    boolean isLoadFromOnCreate = false;
    ContextMenuClick listener;
    String contextPhoneNumber, contextRecordPath, contextFilename;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_records, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        try{
            getActivity().setTitle(getString(R.string.title_of_records));
        }catch (NullPointerException npe){

        }

        listener = new ContextMenuClick() {
            @Override
            public void onContextMenuClicked(String phoneNumber, String recordPath, String fileName) {
                contextPhoneNumber = phoneNumber;
                contextRecordPath = recordPath;
                contextFilename = fileName;
            }
        };

        TVRecord = rootView.findViewById(R.id.TVRecord);
        progressBar = rootView.findViewById(R.id.progressBar1);
        LVRecords = rootView.findViewById(R.id.LVRecords);
        registerForContextMenu(LVRecords);

        if(Utility.checkIfAlreadyhavePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getContext())) {
            setRecordsMethods();

            if (recordSize > 0) {
                TVRecord.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                isLoadFromOnCreate = true;
                loadRecords();
            }
        }else{
            goToPermission();
        }
    }

    private void loadRecords(){
        recordsModelList.clear();
        setRecordsMethods();

        for (int i = (recordSize - 1); i >= 0; i--) {
            recordsModelList.add(new RecordsModel(AppUtility.getContactName(getRecords.getFilePhoneNumber(i), getContext()), getRecords.getFileCreationDate(i), getRecords.getFileDuration(i), getRecords.getFilePath(i), getRecords.getFileCallFrom(i), getRecords.getFileName(i)));

            RecordAdapter recordAdapter = new RecordAdapter(getContext(), recordsModelList, listener);
            LVRecords.setAdapter(recordAdapter);

            if(i == 0){
                progressBar.setVisibility(View.GONE);
                TVRecord.setVisibility(View.GONE);
            }
        }
    }

    public void loadRecordsWithSearch(String searchQuery){
        recordsModelList.clear();
        setRecordsMethods();

        for (int i = (recordSize - 1); i >= 0; i--) {
            if(getRecords.getFilePhoneNumber(i).contains(searchQuery)){
                recordsModelList.add(new RecordsModel(AppUtility.getContactName(getRecords.getFilePhoneNumber(i), getContext()), getRecords.getFileCreationDate(i), getRecords.getFileDuration(i), getRecords.getFilePath(i), getRecords.getFileCallFrom(i), getRecords.getFileName(i)));
            }

            RecordAdapter recordAdapter = new RecordAdapter(getContext(), recordsModelList, listener);
            LVRecords.setAdapter(recordAdapter);

            if(i == 0){
                progressBar.setVisibility(View.GONE);
                TVRecord.setVisibility(View.GONE);
            }
        }
    }

    private void setRecordsMethods(){
        getRecords = new GetRecords();
        recordSize = getRecords.getSize();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.LVRecords) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.menu_call:
                if(!Utility.isMyServiceRunning(CallServices.class, getContext())){
                    showWantToEnableDialog();
                }else{
                    goToCall();
                }
                return true;
            case R.id.menu_delete:
                showDeleteDialog();
                return true;
            case R.id.menu_play:
                goToPlay();
                return true;
            case R.id.menu_share:
                goToShare();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isLoadFromOnCreate){
            if(Utility.checkIfAlreadyhavePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getContext())){
                if(!GlobalValues.isUserPlayRecord){
                    loadRecords();
                }else{
                    GlobalValues.isUserPlayRecord = false;
                }
            }else{
                goToPermission();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isLoadFromOnCreate = false;
        if(Utility.checkIfAlreadyhavePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getContext())){
            AppUtility.setRecordCount(getContext());
        }else{
            goToPermission();
        }
    }

    private void goToPermission(){
        AppUtility.goToOtherPermission(getActivity(), getContext(), false);
    }

    private void goToShare(){
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + GlobalValues.CallRecorderSaveDirectory, contextFilename);
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent .setType("vnd.android.cursor.dir/email");
        /*String to[] = {"asd@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);*/
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Call Recorder File");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }

    private void goToCall(){
        String uri = "tel:" + contextPhoneNumber.trim() ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private void goToDelete(){
        File file = new File(contextRecordPath);
        if(file.exists()){
            if(file.delete()){
                loadRecords();
            }
        }
    }

    private void goToPlay(){
        GlobalValues.isUserPlayRecord = true;

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(contextRecordPath)), "audio/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }

    private void showDeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getContext().getString(R.string.are_you_sure))
                .setCancelable(false)
                .setPositiveButton(getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        goToDelete();
                    }
                })
                .setNegativeButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showWantToEnableDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getContext().getString(R.string.not_recording))
                .setCancelable(false)
                .setNeutralButton(getContext().getString(R.string.yes_activate), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().startService(new Intent(getActivity(), CallServices.class));
                        goToCall();
                    }
                })
                .setPositiveButton(getContext().getString(R.string.just_call), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        goToCall();
                    }
                })
                .setNegativeButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
