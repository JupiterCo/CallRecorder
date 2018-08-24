package com.call.jupiter.recorder.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.call.jupiter.recorder.Helper.AppUtility;
import com.call.jupiter.recorder.Helper.Utility;
import com.call.jupiter.recorder.R;

/**
 * Created by batuhan on 24.08.2018.
 */

public class WriteExternalStoragePermissionActivity extends AppCompatActivity {
    TextView TVTitle, TVDescription;
    Button btnPermission;
    private static String PERMISSON_NAME = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_writeexternalstorage);
        defineObjects();
    }

    private void defineObjects(){
        //Büyük iconu yükle xmlden.

        TVTitle = findViewById(R.id.TVTitle);
        TVDescription = findViewById(R.id.TVDescription);
        btnPermission = findViewById(R.id.btnPermission);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Avenir-Heavy.ttf");
        TVTitle.setTypeface(typeface);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/Avenir-Light.otf");
        TVDescription.setTypeface(typeface);

        if(Utility.checkIfAlreadyhavePermission(PERMISSON_NAME, this)){
            goToOtherPermission();
        }

        btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(WriteExternalStoragePermissionActivity.this, new String[]{PERMISSON_NAME}, 1);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToOtherPermission();
                } else {
                    Utility.showAlertDialogOneButton(WriteExternalStoragePermissionActivity.this, getString(R.string.save_your_records_description));
                }
            }
        }
    }

    private void goToOtherPermission(){
        AppUtility.goToOtherPermission(this, this, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}