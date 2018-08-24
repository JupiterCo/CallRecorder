package com.call.jupiter.recorder.Receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.call.jupiter.recorder.Helper.GlobalValues;
import com.call.jupiter.recorder.Helper.AppUtility;
import com.call.jupiter.recorder.Helper.Utility;
import com.call.jupiter.recorder.RecordsMethods.RecordProcess;

import java.util.Random;

/**
 * Created by batuhan on 16.08.2018.
 */

public class CallReceiver extends BroadcastReceiver {
    Bundle bundle;
    String state;
    public boolean wasRinging = false;
    private boolean recordstarted = false;
    RecordProcess recordProcess;
    private String phoneNumber, inORout, fileName;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Utility.checkIfAlreadyhavePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, context)) {
            if (intent.getAction().equals(GlobalValues.ACTION_IN)) {
                if ((bundle = intent.getExtras()) != null) {
                    state = bundle.getString(TelephonyManager.EXTRA_STATE);

                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        phoneNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        wasRinging = true;
                        inORout = "IN";
                        Log.d("Kontrol", "IN: " + phoneNumber);
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        Log.d("Kontrol", "STATE OFF HOOK");

                        AppUtility.createFolder();

                        if (wasRinging) {
                            Random rnd = new Random();
                            fileName = phoneNumber + "%" + inORout + "%" + String.valueOf(rnd.nextInt(1000000));
                            recordProcess = new RecordProcess(fileName);

                            recordstarted = true;
                            recordProcess.Start();
                        }
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        wasRinging = false;
                        Log.d("Kontrol", "Buraya geldiyse reddedilme ya da sonlanmış çağrı!");

                        if (recordstarted) {
                            recordProcess.Stop(context, fileName);
                            recordstarted = false;
                        }
                    }
                }
            } else if (intent.getAction().equals(GlobalValues.ACTION_OUT)) {
                if ((bundle = intent.getExtras()) != null) {
                    phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                    inORout = "OUT";
                    wasRinging = true;
                    Log.d("Kontrol", "OUT: " + phoneNumber);
                }
            }
        }
    }
}
