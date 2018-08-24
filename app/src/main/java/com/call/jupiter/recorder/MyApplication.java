package com.call.jupiter.recorder;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by batuhan on 23.08.2018.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        settingsForUpper24APIFileListen();
    }

    private void settingsForUpper24APIFileListen(){
        if(Build.VERSION.SDK_INT >= 24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
