package com.engineandroid;

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
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
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
    InterstitialAd mInterstitialAd = null;
    boolean loadingReward = false, loadingInterstitial =  false;

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

    public void createInterstitialAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        loadingInterstitial = true;
        InterstitialAd.load(activity,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    private static final String TAG = "Pueba";

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        loadingInterstitial = false;
                        Log.i(TAG, "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d(TAG, "Ad was clicked.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad dismissed fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d(TAG, "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad showed fullscreen content.");
                            }
                        });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                        loadingInterstitial = false;
                    }
                });
    }

    private void createRewardAd(){
        loadingReward = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(activity, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mRewardedAd = null;
                        loadingReward = false;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        loadingReward = false;
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

    private void showRewardAdAux(Message message){
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

    private void showInterstitialAdAux(Message message){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
        } else {
            Log.d("InterstitialAd", "The insterstitial ad wasn't ready yet.");
        }
    }

    public void buildAndShowRewardAd(Message message){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buildRewardedAd();
                showRewardAdAux(message);
            }
        });
    }

    public void showRewardedAd(Message message){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isRewardBuilt())
                    createRewardAd();
                showRewardAdAux(message);
                mRewardedAd = null;
            }
        });
    }

    public void showInterstitialAd(Message message){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isInterstitialBuilt())
                    createInterstitialAd();
                showInterstitialAdAux(message);
                mRewardedAd = null;
            }
        });
    }

    public void buildInterestingAd(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createInterstitialAd();
            }
        });
    }

    public void buildRewardedAd(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createRewardAd();
            }
        });
    }

    public boolean isRewardBuilt(){
        return mRewardedAd != null && !loadingReward;
    }

    public boolean isInterstitialBuilt(){
        return mInterstitialAd != null && !loadingInterstitial;
    }

    public void setEngine(Engine engine){
        this.engine = engine;
    }
}
