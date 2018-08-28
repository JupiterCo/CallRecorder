package com.call.jupiter.recorder.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.call.jupiter.recorder.ContextMenuClick;
import com.call.jupiter.recorder.Helper.AppUtility;
import com.call.jupiter.recorder.Helper.GlobalValues;
import com.call.jupiter.recorder.Models.RecordsModel;
import com.call.jupiter.recorder.R;

import java.io.File;
import java.util.List;

/**
 * Created by batuhan on 22.08.2018.
 */

public class RecordAdapter extends BaseAdapter {
    private List<RecordsModel> allRecordList;
    private Context context;
    private TextView TVContactName, TVRecordDate, TVDuration;
    private LinearLayout LLMore, LLImage, LLMain1, LLMain2;
    private ImageView IVInOrOut;
    String recordPath;
    private ContextMenuClick contextMenuClick;

    public RecordAdapter(Context mContext, List<RecordsModel> mRecordList, ContextMenuClick mContextMenuClick){
        allRecordList = mRecordList;
        context = mContext;
        contextMenuClick = mContextMenuClick;
    }

    @Override
    public int getCount() {
        return allRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return allRecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static void blink(final View v) {
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(200);
        animation.setStartOffset(20);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(3);
        v.setAnimation(animation);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View satirView;
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();

        satirView = layoutInflater.inflate(R.layout.item_for_record, null);

        TVContactName = satirView.findViewById(R.id.TVContactName);
        TVRecordDate = satirView.findViewById(R.id.TVRecordDate);
        TVDuration = satirView.findViewById(R.id.TVDuration);
        LLMore = satirView.findViewById(R.id.LLMore);
        LLImage = satirView.findViewById(R.id.LLImage);
        LLMain1 = satirView.findViewById(R.id.LLMain1);
        LLMain2 = satirView.findViewById(R.id.LLMain2);
        IVInOrOut = satirView.findViewById(R.id.IVInOrOut);

        if(position == 0 && AppUtility.getRecordCountDifference(context) > 0){
            blink(satirView);
        }

        final RecordsModel recordsModel = allRecordList.get(position);

        recordPath = recordsModel.getRecordPath();

        setContents(recordsModel.getPhoneNumber(), recordsModel.getRecordDate(), recordsModel.getRecordDuration(), recordsModel.getRecordFrom());

        LLMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contextMenuClick.onContextMenuClicked(recordsModel.getPhoneNumber(), recordsModel.getRecordPath(), recordsModel.getRecordFileName());

                ((Activity) context).openContextMenu(satirView);

            }
        });

        LLImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecord(recordsModel.getRecordPath());
            }
        });

        LLMain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecord(recordsModel.getRecordPath());
            }
        });

        LLMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecord(recordsModel.getRecordPath());
            }
        });

        return satirView;
    }

    private void playRecord(String Path){
        GlobalValues.isUserPlayRecord = true;

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Path)), "audio/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(intent);
    }

    private void setContents(String ContactName, String RecordDate, String Duration, String InOrOut){
        TVContactName.setText(ContactName);
        TVRecordDate.setText(RecordDate);
        TVDuration.setText(Duration);

        if(InOrOut.equals("OUT")){
            IVInOrOut.setImageResource(R.drawable.icon_outgoing_call);
        }
    }
}
