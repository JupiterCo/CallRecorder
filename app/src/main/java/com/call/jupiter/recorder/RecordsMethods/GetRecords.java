package com.call.jupiter.recorder.RecordsMethods;

import android.os.Environment;
import com.call.jupiter.recorder.Helper.GlobalValues;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by batuhan on 22.08.2018.
 */

public class GetRecords {
    private File[] files;
    private String path;

    public GetRecords(){
        path = Environment.getExternalStorageDirectory().toString() + GlobalValues.CallRecorderSaveDirectory;
        File directory = new File(path);
        files = directory.listFiles();
    }

    public int getSize(){
        return files.length;
    }

    public String getFileName(int whichFile){
        return files[whichFile].getName();
    }

    public String getFileCreationDate(int whichFile){
        return new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US).format(files[whichFile].lastModified());
    }

    public String getFileDuration(int whichFile){
        String[] seperated = getFileName(whichFile).split("%");

        if(seperated.length > 4){
            return seperated[3] + ":" + seperated[4];
        }else{
            return "00:00";
        }
    }

    public String getFilePhoneNumber(int whichFile){
        String[] seperated = getFileName(whichFile).split("%");

        return seperated[0];
    }

    public String getFilePath(int whichFile){
        return Environment.getExternalStorageDirectory() + GlobalValues.CallRecorderSaveDirectory + "/" + getFileName(whichFile);
    }

}
