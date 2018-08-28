package com.call.jupiter.recorder.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.call.jupiter.recorder.Helper.AppUtility;
import com.call.jupiter.recorder.Helper.GSharedPreferences;
import com.call.jupiter.recorder.Helper.Utility;
import com.call.jupiter.recorder.R;

/**
 * Created by batuhan on 28.08.2018.
 */

public class ContactPermissionActivity extends AppCompatActivity {
    TextView TVTitle, TVDescription, TVSkip;
    Button btnPermission;
    GSharedPreferences sharedPreferences;
    private static String PERMISSON_NAME = Manifest.permission.READ_CONTACTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_contacts);
        defineObjects();
    }

    private void defineObjects(){
        sharedPreferences = new GSharedPreferences(this);

        TVTitle = findViewById(R.id.TVTitle);
        TVDescription = findViewById(R.id.TVDescription);
        btnPermission = findViewById(R.id.btnPermission);
        TVSkip = findViewById(R.id.TVSkip);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Avenir-Heavy.ttf");
        TVTitle.setTypeface(typeface);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/Avenir-Light.otf");
        TVDescription.setTypeface(typeface);
        TVSkip.setTypeface(typeface);

        TVSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.SET_IS_CONTACT_PERMISSION_SKIP(true);
                goToOtherPermission();
            }
        });

        if(Utility.checkIfAlreadyhavePermission(PERMISSON_NAME, this)){
            goToOtherPermission();
        }

        btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ContactPermissionActivity.this, new String[]{PERMISSON_NAME}, 1);
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
                    Utility.showAlertDialogOneButton(ContactPermissionActivity.this, getString(R.string.contact_description));
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
