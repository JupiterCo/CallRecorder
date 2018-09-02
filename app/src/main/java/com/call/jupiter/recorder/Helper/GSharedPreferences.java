package com.call.jupiter.recorder.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by batuhan on 22.08.2018.
 */

public class GSharedPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /** Defines **/
    private static final String PREF_NAME = "CallRecorderJupiter";

    public GSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    /** User Datas**/
    private static final String LAST_RECORD_COUNT = "LastRecordCount";
    private static final String CONTACT_PERMISSION = "ContactPermission";
    private static final String IS_USER_PURCHASE_REMOVE_ADS = "IsUserPurchaseRemoveAds";


    public int GET_LAST_RECORD_COUNT(){ return sharedPreferences.getInt(LAST_RECORD_COUNT, 0); }

    public void SET_LAST_RECORD_COUNT(int RecordCount){
        editor.putInt(LAST_RECORD_COUNT, RecordCount);
        editor.commit();
    }

    public boolean GET_IS_CONTACT_PERMISSION_SKIP(){
        return sharedPreferences.getBoolean(CONTACT_PERMISSION, false);
    }

    public void SET_IS_CONTACT_PERMISSION_SKIP(boolean contactPermission){
        editor.putBoolean(CONTACT_PERMISSION, contactPermission);
        editor.commit();
    }

    public boolean GET_IS_USER_PURCHASE_REMOVE_ADS(){
        return sharedPreferences.getBoolean(IS_USER_PURCHASE_REMOVE_ADS, false);
    }

    public void SET_IS_USER_PURCHASE_REMOVE_ADS(boolean isUserRemoveAds){
        editor.putBoolean(IS_USER_PURCHASE_REMOVE_ADS, isUserRemoveAds);
        editor.commit();
    }
}
