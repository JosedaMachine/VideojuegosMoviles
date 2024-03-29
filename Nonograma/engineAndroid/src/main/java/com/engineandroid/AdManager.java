package com.engineandroid;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Objects;

public class AdManager {
    private static AdManager instance_;
    AdView ad;
    Engine engine;
    AppCompatActivity activity;
    RewardedAd mRewardedAd = null;
    boolean loading = false;

    public AdManager(AppCompatActivity activity){
        this.activity = activity;
    }

    public static AdManager instance(){
        return instance_;
    }

    public static boolean init(AppCompatActivity activity){
        instance_ = new AdManager(activity);
        return true;
    }

    public void initializeAds(){
        MobileAds.initialize(activity, new OnInitializationCompleteListener(){
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
    }

    public void buildBannerAd(AdView adView){
        ad = adView;
        if(ad != null){
            AdRequest adR = new AdRequest.Builder().build();
            ad.loadAd(adR);
        }
    }

    public Pair<Integer, Integer> getBannerSize(){
        return new Pair<Integer, Integer>(Objects.requireNonNull(ad.getAdSize()).getWidthInPixels(activity), Objects.requireNonNull(ad.getAdSize()).getHeightInPixels(activity));
    }

    public void buildRewardAd(){
        loading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(activity, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mRewardedAd = null;
                        loading = false;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        loading = false;
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                //Log.d(TAG, "Ad was clicked.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                //Log.d(TAG, "Ad dismissed fullscreen content.");
                                mRewardedAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                //Log.e(TAG, "Ad failed to show fullscreen content.");
                                mRewardedAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                //Log.d(TAG, "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                //Log.d(TAG, "Ad showed fullscreen content.");
                            }
                        });
                    }
                });
    }

    private void showRewardAd(Message message){
        if (mRewardedAd != null) {
            mRewardedAd.show(activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d("Reward", "The user earned the reward.");
                    engine.sendMessage(message);
                }
            });
        } else {
            Log.d("Reward", "The rewarded ad wasn't ready yet.");
        }
    }

    public void buildAndShowRewardAd(Message message){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buildRewardedAd();
                showRewardAd(message);
            }
        });
    }

    public void showRewardedAd(Message message){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isRewardBuilt())
                    buildRewardAd();
                showRewardAd(message);
                mRewardedAd = null;
            }
        });
    }

    public void buildRewardedAd(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buildRewardAd();
            }
        });
    }

    public boolean isRewardBuilt(){
        return mRewardedAd != null && !loading;
    }

    public void setEngine(Engine engine){
        this.engine = engine;
    }
}
