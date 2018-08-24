package com.call.jupiter.recorder.Fragments;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.call.jupiter.recorder.Adapters.RecordAdapter;
import com.call.jupiter.recorder.Helper.AppUtility;
import com.call.jupiter.recorder.Helper.Utility;
import com.call.jupiter.recorder.Models.RecordsModel;
import com.call.jupiter.recorder.R;
import com.call.jupiter.recorder.RecordsMethods.GetRecords;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by batuhan on 21.08.2018.
 */

public class RecordsFragment extends Fragment {
    View rootView;
    GetRecords getRecords;
    int recordSize;
    TextView TVRecord;
    ProgressBar progressBar;
    ListView LVRecords;
    List<RecordsModel> recordsModelList = new ArrayList<>();
    boolean isLoadFromOnCreate = false;
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

        TVRecord = rootView.findViewById(R.id.TVRecord);
        progressBar = rootView.findViewById(R.id.progressBar1);
        LVRecords = rootView.findViewById(R.id.LVRecords);

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
            recordsModelList.add(new RecordsModel(AppUtility.getContactName(getRecords.getFilePhoneNumber(i), getContext()), getRecords.getFileCreationDate(i), getRecords.getFileDuration(i), getRecords.getFilePath(i)));

            RecordAdapter recordAdapter = new RecordAdapter(getContext(), recordsModelList);
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
                recordsModelList.add(new RecordsModel(AppUtility.getContactName(getRecords.getFilePhoneNumber(i), getContext()), getRecords.getFileCreationDate(i), getRecords.getFileDuration(i), getRecords.getFilePath(i)));
            }

            RecordAdapter recordAdapter = new RecordAdapter(getContext(), recordsModelList);
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
    public void onResume() {
        super.onResume();
        if(!isLoadFromOnCreate){
            if(Utility.checkIfAlreadyhavePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getContext())){
                loadRecords();
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
}
