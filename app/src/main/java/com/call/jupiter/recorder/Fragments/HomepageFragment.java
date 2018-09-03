package com.call.jupiter.recorder.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.call.jupiter.recorder.Helper.Advertising;
import com.call.jupiter.recorder.Helper.AppUtility;
import com.call.jupiter.recorder.Helper.GlobalValues;
import com.call.jupiter.recorder.Helper.Utility;
import com.call.jupiter.recorder.R;
import com.call.jupiter.recorder.Receiver.CallReceiver;
import com.call.jupiter.recorder.Services.CallServices;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import static android.content.ContentValues.TAG;

/**
 * Created by batuhan on 21.08.2018.
 */

public class HomepageFragment extends Fragment {
    View rootView;
    Button btnActivate;
    TextView TVRunOrNot, TVRunOrNotDescription;
    ImageView IVRunOrNot;
    private boolean isMustRunAnimation = false;
    private static String TAG = "Kontrol";
    AdView mAdView;
    InterstitialAd mInterstitialAd;
    Advertising ad;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        try{
            getActivity().setTitle(getString(R.string.title_of_homepage));
        }catch (NullPointerException npe){

        }

        btnActivate = rootView.findViewById(R.id.btnActivate);
        TVRunOrNot = rootView.findViewById(R.id.TVRunOrNot);
        TVRunOrNotDescription = rootView.findViewById(R.id.TVRunOrNotDescription);
        IVRunOrNot = rootView.findViewById(R.id.IVRunOrNot);
        mAdView = rootView.findViewById(R.id.adView);
        mInterstitialAd = new InterstitialAd(getContext());

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Avenir-Heavy.ttf");
        TVRunOrNot.setTypeface(typeface);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Avenir-Light.otf");
        TVRunOrNotDescription.setTypeface(typeface);

        setViewsAboutEnableOrDisable();

        btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableProcess();
            }
        });

        if(Utility.checkIfAlreadyhavePermission(Manifest.permission.READ_PHONE_STATE, getContext())){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                requestPermissions(new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 2);
            }
        }

        ad = new Advertising(getContext());
        ad.showBanner(mAdView);
        ad.showInterstitial(mInterstitialAd, true);
    }

    private void enableDisableProcess(){
        isMustRunAnimation = true;
        if(!Utility.isMyServiceRunning(CallServices.class, getContext())) {
            getActivity().startService(new Intent(getActivity(), CallServices.class));

            setViewsAboutEnableOrDisable();

            AppUtility.sendEventLogToFlurry("Button", "Activate", "Clicked");
        }else{
            showWantToDisableItDialog();
        }
    }

    private void setViewsAboutEnableOrDisable(){
        if(Utility.isMyServiceRunning(CallServices.class, getContext())){
            TVRunOrNot.setText(getString(R.string.recording_activated));
            TVRunOrNotDescription.setText(getString(R.string.recording_activated_description));
            IVRunOrNot.setImageResource(R.drawable.icon_active);
            btnActivate.setText(getString(R.string.disable));
            btnActivate.setBackgroundResource(R.drawable.permission_button);
            btnActivate.setTextColor(Color.parseColor("#ff4848"));

            if(isMustRunAnimation){
                TVRunOrNot.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.in_animation));
            }
        }else{
            TVRunOrNot.setText(getString(R.string.recording_disabled));
            TVRunOrNotDescription.setText(getString(R.string.recording_disabled_description));
            IVRunOrNot.setImageResource(R.drawable.icon_deactive);
            btnActivate.setText(getString(R.string.activate));
            btnActivate.setBackgroundResource(R.drawable.enable_button);
            btnActivate.setTextColor(Color.parseColor("#5fbc6f"));

            if(isMustRunAnimation){
                TVRunOrNot.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.out_animation));
            }
        }
    }

    private void showWantToDisableItDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getContext().getString(R.string.want_to_disable))
                .setCancelable(false)
                .setPositiveButton(getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().stopService(new Intent(getActivity(), CallServices.class));
                        GlobalValues.isUserWantToStop = true;
                        setViewsAboutEnableOrDisable();

                        AppUtility.sendEventLogToFlurry("Button", "Disable", "Clicked");
                    }
                })
                .setNegativeButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onPause() {
        super.onPause();

        if(Utility.checkIfAlreadyhavePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getContext())){
            AppUtility.setRecordCount(getContext());
        }else{
            goToPermission();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isMustRunAnimation = false;
        setViewsAboutEnableOrDisable();

        if(Utility.checkIfAlreadyhavePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getContext())){
            if(AppUtility.getRecordCountDifference(getContext()) > 0){
                AppUtility.showNewRecordDetectedDialog(getContext(), getActivity());
            }
        }else{
            goToPermission();
        }
    }

    private void goToPermission(){
        AppUtility.goToOtherPermission(getActivity(), getContext(), false);

        AppUtility.createFolder();
    }
}
