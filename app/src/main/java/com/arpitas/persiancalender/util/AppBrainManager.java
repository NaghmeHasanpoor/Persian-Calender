package com.arpitas.persiancalender.util;

import android.content.Context;
import android.util.Log;

import com.appbrain.AdId;
import com.appbrain.InterstitialBuilder;
import com.appbrain.InterstitialListener;

public class AppBrainManager {
    private static String TAG = AppBrainManager.class.getSimpleName();
    private static AppBrainManager mInstance;
    private static InterstitialBuilder interstitialBuilder;
    private static IInterstitialListener mInterstitialListener;
    private Context context;
    private static boolean isLoaded;

    public static AppBrainManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppBrainManager(context);
        }
        return mInstance;
    }

    private AppBrainManager(final Context context) {
        this.context = context;
        Log.e(TAG, "request");
        interstitialBuilder = InterstitialBuilder.create()
                .setAdId(AdId.LEVEL_COMPLETE)
                .setListener(new InterstitialListener() {
                    @Override
                    public void onPresented() {
                        Log.e(TAG, "onPresented!");
                    }

                    @Override
                    public void onClick() {
                        Log.e(TAG, "onClick!");
                    }

                    @Override
                    public void onDismissed(boolean b) {
                        if (mInterstitialListener != null)
                            mInterstitialListener.onAdClosed();
                        Log.e(TAG, "onDismissed!");
                    }

                    @Override
                    public void onAdLoaded() {
                        isLoaded = true;
                        Log.e(TAG, "onAdLoaded!");
                    }

                    @Override
                    public void onAdFailedToLoad(InterstitialError interstitialError) {
                        if (mInterstitialListener != null)
                            mInterstitialListener.onAdNotLoaded();
                        isLoaded = false;
                        Log.e(TAG, "onAdFailedToLoad!");
                    }
                })
                .setOnDoneCallback(() -> {
                    // Preload again, so we can use interstitialBuilder again.
                    interstitialBuilder.preload(context);
                }).preload(context);
    }

    public void showAd(IInterstitialListener iInterstitialListener) {
        mInterstitialListener = iInterstitialListener;
        if (isLoaded) {
            boolean isShowing = interstitialBuilder.show(context);
            if (!isShowing){
                if (mInterstitialListener != null)
                    mInterstitialListener.onAdNotLoaded();
            }
            Log.d(TAG, "showAd, isShowing? " + isShowing);
        } else {
            if (mInterstitialListener != null)
                mInterstitialListener.onAdNotLoaded();
        }
    }

    public interface IInterstitialListener {
        void onAdClosed(); //If anything extra needs to do in 'OnAdCloseListener'.
        void onAdNotLoaded(); //What to do when ad has not loaded yet.
    }

    public void empty_instance(){
        try{
            mInstance = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
