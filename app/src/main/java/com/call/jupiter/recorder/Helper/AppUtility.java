package com.call.jupiter.recorder.Helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;

import com.call.jupiter.recorder.Activities.MainActivity;
import com.call.jupiter.recorder.Activities.MicrophonePermissionActivity;
import com.call.jupiter.recorder.Activities.PhonePermissionActivity;
import com.call.jupiter.recorder.Activities.WriteExternalStoragePermissionActivity;
import com.call.jupiter.recorder.R;
import com.call.jupiter.recorder.Fragments.RecordsFragment;
import com.call.jupiter.recorder.RecordsMethods.GetRecords;

import java.io.File;

/**
 * Created by batuhan on 16.08.2018.
 */

public class AppUtility {

    public static File createFolder(){
        File sampleDirectory = new File(Environment.getExternalStorageDirectory(), GlobalValues.CallRecorderSaveDirectory);

        if (!sampleDirectory.exists()) {
            sampleDirectory.mkdirs();
        }

        return new File(Environment.getExternalStorageDirectory(), GlobalValues.CallRecorderSaveDirectory);
    }

    public static String getFileDurationFromFileName(String fileName){
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(Environment.getExternalStorageDirectory().toString() + GlobalValues.CallRecorderSaveDirectory + "/" + fileName + ".amr");

        String duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long dur = Long.parseLong(duration);

        String seconds = String.valueOf((dur % 60000) / 1000);
        String minutes = String.valueOf(dur / 60000);

        metaRetriever.release();

        String realDuration;

        if(minutes.length() == 1){
            realDuration = "0" + minutes;
        }else{
            realDuration = minutes;
        }

        if(seconds.length() == 1){
            realDuration += "%0" + seconds;
        }else{
            realDuration += "%" + seconds;
        }

        return realDuration;
    }

    public static void setRecordCount(Context context){
        GetRecords getRecords = new GetRecords();
        GSharedPreferences sharedPreferences = new GSharedPreferences(context);

        sharedPreferences.SET_LAST_RECORD_COUNT(getRecords.getSize());
    }

    public static int getRecordCountDifference(Context context){
        GetRecords getRecords = new GetRecords();
        GSharedPreferences sharedPreferences = new GSharedPreferences(context);

        return getRecords.getSize() - sharedPreferences.GET_LAST_RECORD_COUNT();
    }

    public static void showNewRecordDetectedDialog(Context context, final Activity activity){
        GetRecords getRecords = new GetRecords();
        setRecordCount(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.new_record_detected))
                .setMessage(context.getString(R.string.phone_number) + ": " + getRecords.getFilePhoneNumber(getRecords.getSize() - 1) + "\n" + context.getString(R.string.duration) + ": " + getRecords.getFileDuration(getRecords.getSize() - 1) + "\n" + context.getString(R.string.date)+ ": "+ getRecords.getFileCreationDate(getRecords.getSize() - 1))
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(context.getString(R.string.go_to_records), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ((MainActivity) activity).goToFragment(new RecordsFragment(), MainActivity.RECORDFRAGMENTTAG);
                    }
                }).setNegativeButton(context.getString(R.string.okay), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showNotification(Context context){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "notify_001");
        Intent ii = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(context.getString(R.string.new_record_detected));
        bigText.setBigContentTitle(context.getString(R.string.app_name));
        bigText.setSummaryText(context.getString(R.string.click_here_to_listen));

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.app_icon_notification);
        mBuilder.setContentTitle(context.getString(R.string.app_name));
        mBuilder.setContentText(context.getString(R.string.new_record_detected));
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001", context.getString(R.string.new_record_detected), NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }

    public static String getContactName(final String phoneNumber, Context context) {
        if(Utility.checkIfAlreadyhavePermission(Manifest.permission.READ_CONTACTS, context)){
            Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

            String contactName="";
            Cursor cursor= context.getContentResolver().query(uri,projection,null,null,null);

            if (cursor != null) {
                if(cursor.moveToFirst()) {
                    contactName=cursor.getString(0);
                }
                cursor.close();
            }

            if(contactName.equals("") || contactName.isEmpty()){
                return phoneNumber;
            }else{
                return contactName;
            }
        }else{
            return phoneNumber;
        }
    }

    public static void goToOtherPermission(Activity activity, Context context, boolean isMainActivity){
        if(!Utility.checkIfAlreadyhavePermission(Manifest.permission.READ_PHONE_STATE, activity)){
            context.startActivity(new Intent(context, PhonePermissionActivity.class));
            activity.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
        }else if(!Utility.checkIfAlreadyhavePermission(Manifest.permission.RECORD_AUDIO, activity)){
            context.startActivity(new Intent(context, MicrophonePermissionActivity.class));
            activity.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
        }else if(!Utility.checkIfAlreadyhavePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, activity)){
            context.startActivity(new Intent(context, WriteExternalStoragePermissionActivity.class));
            activity.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
        }else{
            if(!isMainActivity){
                context.startActivity(new Intent(context, MainActivity.class));
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }

        if(!isMainActivity){ activity.finish(); }
    }
}
