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

    private Context context;
    public GSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        this.context = context;
        editor = sharedPreferences.edit();
        editor.apply();
    }

    /** User Datas**/
    private static final String LAST_RECORD_COUNT = "LastRecordCount";


    public int GET_LAST_RECORD_COUNT(){ return sharedPreferences.getInt(LAST_RECORD_COUNT, 0); }

    public void SET_LAST_RECORD_COUNT(int RecordCount){
        editor.putInt(LAST_RECORD_COUNT, RecordCount);
        editor.commit();
    }
}
