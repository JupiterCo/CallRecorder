package com.call.jupiter.recorder.RecordsMethods;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.call.jupiter.recorder.Helper.AppUtility;
import com.call.jupiter.recorder.Helper.GlobalValues;

import java.io.File;
import java.io.IOException;

/**
 * Created by batuhan on 16.08.2018.
 */

public class RecordProcess {
    private MediaRecorder recorder;
    private String fileName;

    public RecordProcess(String mfileName){
        fileName = mfileName;
        recorder = new MediaRecorder();
    }

    public void Start(){
        setAudioSourceForDifferentDevices();

        recorder.setAudioSamplingRate(8000);
        recorder.setAudioEncodingBitRate(12200);

        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(Environment.getExternalStorageDirectory() + GlobalValues.CallRecorderSaveDirectory + "/" + fileName + ".amr");

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recorder.start();
        Log.d("Kontrol", "Kayıt başladı!");
    }

    public void Stop(Context context, String fileName){
        recorder.stop();
        changeFileName(fileName);
        AppUtility.showNotification(context);

        Log.d("Kontrol", "Buraya geldiyse arama bitirilmiş demektir, kayıt et!");
    }

    private void setAudioSourceForDifferentDevices(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }else{
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }
    }

    private void changeFileName(String fileName){
        File sdcard = Environment.getExternalStorageDirectory();
        File from = new File(sdcard, GlobalValues.CallRecorderSaveDirectory + "/" + fileName + ".amr");
        File to = new File(sdcard,GlobalValues.CallRecorderSaveDirectory + "/" + fileName + "%" + AppUtility.getFileDurationFromFileName(fileName) + "%.amr");

        from.renameTo(to);
    }


}
