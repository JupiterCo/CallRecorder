package com.call.jupiter.recorder.Fragments;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.call.jupiter.recorder.Helper.AppUtility;
import com.call.jupiter.recorder.Helper.GlobalValues;
import com.call.jupiter.recorder.Helper.Utility;
import com.call.jupiter.recorder.R;
import com.call.jupiter.recorder.Services.CallServices;

/**
 * Created by batuhan on 21.08.2018.
 */

public class HomepageFragment extends Fragment {
    View rootView;
    Button btnRegister, btnUnregister;
    TextView TVRunOrNot;
    ImageView IVRunOrNot;
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

        btnRegister = rootView.findViewById(R.id.btnRegister);
        btnUnregister = rootView.findViewById(R.id.btnUnregister);
        TVRunOrNot = rootView.findViewById(R.id.TVRunOrNot);
        IVRunOrNot = rootView.findViewById(R.id.IVRunOrNot);

        setViewsAboutRunOrNot();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startService(new Intent(getActivity(), CallServices.class));
                setViewsAboutRunOrNot();
            }
        });

        btnUnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().stopService(new Intent(getActivity(), CallServices.class));
                GlobalValues.isUserWantToStop = true;
                setViewsAboutRunOrNot();
            }
        });
    }

    private void setViewsAboutRunOrNot(){
        if(Utility.isMyServiceRunning(CallServices.class, getContext())){
            TVRunOrNot.setText("Working");
            TVRunOrNot.setTextColor(Color.parseColor("#6aaa3b"));
            IVRunOrNot.setImageResource(R.mipmap.icon_ok);

            Animation in  = AnimationUtils.loadAnimation(getContext(), R.anim.in_animation);
            IVRunOrNot.setAnimation(in);
        }else{
            TVRunOrNot.setText("Not Working");
            TVRunOrNot.setTextColor(Color.parseColor("#b53b3b"));
            IVRunOrNot.setImageResource(R.mipmap.icon_notok);

            Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.out_animation);
            IVRunOrNot.setAnimation(out);
        }
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
        setViewsAboutRunOrNot();

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
    }
}
