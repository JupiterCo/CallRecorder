package com.call.jupiter.recorder.Services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.call.jupiter.recorder.Helper.GlobalValues;
import com.call.jupiter.recorder.Receiver.CallReceiver;

/**
 * Created by batuhan on 17.08.2018.
 */

public class CallServices extends Service {
    CallReceiver callReceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(!GlobalValues.isUserWantToStop){
            Intent intent = new Intent("com.android.RESTART_BECAUSE_STOPPED");
            sendBroadcast(intent);
        }else{
            GlobalValues.isUserWantToStop = false;
        }

        Log.d("Kontrol", "onDestroy - CallServices");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1, new Notification());

        registerCallReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //registerCallReceiver();

        //return START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    public void registerCallReceiver(){
        Log.d("Kontrol", "register Call Receiver");
        try {
            if (callReceiver != null) {
                unregisterReceiver(callReceiver);
            }

            final IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalValues.ACTION_OUT);
            filter.addAction(GlobalValues.ACTION_IN);
            filter.setPriority(9999);

            callReceiver = new CallReceiver();

            registerReceiver(callReceiver, filter);
        } catch (Exception e) {
            Log.d("Kontrol", "broadcastReceiver is already unregistered");
            callReceiver = null;
        }
    }
}
