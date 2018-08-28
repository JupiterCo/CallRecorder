package com.call.jupiter.recorder.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.call.jupiter.recorder.Helper.Utility;
import com.call.jupiter.recorder.Services.CallServices;

/**
 * Created by batuhan on 15.08.2018.
 */

public class RestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Kontrol", "Buraya düştü, onReceive");
        if (!Utility.isMyServiceRunning(CallServices.class, context)) {
            Intent serviceIntent = new Intent(context ,CallServices.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                context.startForegroundService(serviceIntent);
            }else{
                context.startService(serviceIntent);
            }
        }
    }
}
