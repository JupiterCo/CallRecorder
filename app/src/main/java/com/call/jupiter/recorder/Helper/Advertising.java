package com.call.jupiter.recorder.Helper;

import android.content.Context;

import com.call.jupiter.recorder.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by batuhan on 2.09.2018.
 */

public class Advertising {
    private static GSharedPreferences sharedPreferences;
    private Context context;

    public Advertising(Context context){
        this.context = context;
        sharedPreferences = new GSharedPreferences(context);
    }

    public void showBanner(AdView mAdView){
        if(!sharedPreferences.GET_IS_USER_PURCHASE_REMOVE_ADS()){
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
    }

    public void showInterstitial(final InterstitialAd mInterstitialAd, boolean isNeedSetUnitID) {
        if (!sharedPreferences.GET_IS_USER_PURCHASE_REMOVE_ADS()){
            if (isNeedSetUnitID){
                mInterstitialAd.setAdUnitId(context.getString(R.string.admob_homepage_interstitial_id));
            }

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
