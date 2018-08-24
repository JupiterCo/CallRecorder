package com.call.jupiter.recorder.Helper;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by batuhan on 23.08.2018.
 */

public class Utility {

    public static boolean checkIfAlreadyhavePermission(String whichPermission, Context context) {
        int result = ContextCompat.checkSelfPermission(context, whichPermission);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void showAlertDialogOneButton(Context context, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(Message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
