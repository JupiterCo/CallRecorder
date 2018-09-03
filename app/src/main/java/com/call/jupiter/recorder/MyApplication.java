package com.call.jupiter.recorder;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.MobileAds;

import io.fabric.sdk.android.Fabric;
import java.lang.reflect.Method;

/**
 * Created by batuhan on 23.08.2018.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        new FlurryAgent.Builder().withLogEnabled(true).build(this, "HKB6BWXFFKXJSHWVV2DD");
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
