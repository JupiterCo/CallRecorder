package com.call.jupiter.recorder.Helper;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by batuhan on 2.09.2018.
 */

public class Advertising {
    public static GSharedPreferences sharedPreferences;

    public Advertising(Context context){
        sharedPreferences = new GSharedPreferences(context);
    }

    public void showBanner(AdView mAdView){
        if(!sharedPreferences.GET_IS_USER_PURCHASE_REMOVE_ADS()){
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
    }

    public void showInterstitial(final InterstitialAd mInterstitialAd){
        if (!sharedPreferences.GET_IS_USER_PURCHASE_REMOVE_ADS()){
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                }
            });
        }
    }
}
