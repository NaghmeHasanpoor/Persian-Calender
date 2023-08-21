package com.arpitas.persiancalender.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.appbrain.AppBrainBanner;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.arpitas.persiancalender.Constants;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.util.AppOpenManager.AppOpenListener;

public class AdMobManager {

   private static String TAG = AdMobManager.class.getSimpleName();
   private static AdMobManager mInstance;
   private InterstitialAd mInterstitialAd;
   private IInterstitialListener mInterstitialListener;
   private SharedPrefManager shared;

   private String interstitialId = "";
   private AdView firstBanner = null;
   private AdView rateBanner = null;
   private boolean isLoading = false;
   private boolean isBannerLoaded = false;
   private boolean isBannerLoading = false;

   private AppOpenManager appOpenManager;

   public static AdMobManager getInstance(Context context) {
      if (mInstance == null) {
         mInstance = new AdMobManager(context);
      }
      return mInstance;
   }

   private AdMobManager(Context context) {
      appOpenManager = new AppOpenManager();
      shared = new SharedPrefManager(context);
      if (!TextUtils.isEmpty(shared.get_string_value(Constants.admob_interstitial_id_key_shared))) {
         interstitialId = shared.get_string_value(Constants.admob_interstitial_id_key_shared);
      } else {
         interstitialId = context.getResources().getString(R.string.ain);
      }
      loadInterstitialAd(context);
   }

   public void checkInterstitial(Context context) {
      if (mInterstitialAd == null && !isLoading) {
         loadInterstitialAd(context);
      }
   }

   private void loadInterstitialAd(Context context) {
      if (isLoading) {
         return;
      }
      isLoading = true;
      AdRequest adRequest = new AdRequest.Builder().build();
      InterstitialAd.load(context, interstitialId, adRequest, new InterstitialAdLoadCallback() {
         @Override
         public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            mInterstitialAd = interstitialAd;
            isLoading = false;
         }

         @Override
         public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            mInterstitialAd = null;
            isLoading = false;
         }
      });
   }

   public void initAppOpen(Activity activity, boolean isAdModAllowed, String appOpenId) {
      appOpenManager.initAppOpen(activity, isAdModAllowed, appOpenId);
   }

   public void showAppOpen(Activity activity, AppOpenListener appOpenListener) {
      appOpenManager.showAdIfAvailable(activity, appOpenListener);
   }

   public void showInterstitialAd(Activity activity, IInterstitialListener iInterstitialListener) {
      this.mInterstitialListener = iInterstitialListener;
      if (mInterstitialAd != null) {
         mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
               mInterstitialListener.onAdClosed();
            }

            @Override
            public void onAdShowedFullScreenContent() {
               mInterstitialAd = null;
            }

            @Override
            public void onAdDismissedFullScreenContent() {
               loadInterstitialAd(activity);
               if (mInterstitialListener != null) {
                  mInterstitialListener.onAdClosed();
               }
            }
         });
         mInterstitialAd.show(activity);
      } else {
         loadInterstitialAd(activity);
         if (mInterstitialListener != null) {
            mInterstitialListener.onAdNotLoaded();
         }
      }
   }

   private IInterstitialListener bannerListener = null;

   public void loadFirstBanner(Activity activity, String bannerId) {
      if (isBannerLoading) {
         return;
      }
      isBannerLoading = true;
      firstBanner = new AdView(activity);
      firstBanner.setAdUnitId(bannerId);
      firstBanner.setAdSize(getAdSize(activity));
      AdRequest request = new AdRequest.Builder().build();
      firstBanner.loadAd(request);
      firstBanner.setAdListener(new AdListener() {
         @Override
         public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            if (bannerListener != null)
               bannerListener.onAdNotLoaded();
            isBannerLoading = false;
         }

         @Override
         public void onAdLoaded() {
            if (bannerListener != null)
               bannerListener.onAdClosed();
            isBannerLoading = false;
            isBannerLoaded = true;
         }
      });
   }

   public void showFirstBanner(View adView) {
      RelativeLayout adContainerView = (RelativeLayout) adView;
      AppBrainBanner banner = new AppBrainBanner(adView.getContext());
      adContainerView.removeAllViews();
      adContainerView.addView(banner);
      if (isBannerLoaded) {
         adContainerView.removeAllViews();
         if (firstBanner.getParent() instanceof RelativeLayout) {
            RelativeLayout parent = (RelativeLayout) firstBanner.getParent();
            parent.removeAllViews();
         }
         adContainerView.addView(firstBanner);
      } else if (isBannerLoading) {
         bannerListener = new IInterstitialListener() {
            @Override
            public void onAdClosed() {
               adContainerView.removeAllViews();
               if (firstBanner.getParent() instanceof RelativeLayout) {
                  RelativeLayout parent = (RelativeLayout) firstBanner.getParent();
                  parent.removeAllViews();
               }
               adContainerView.addView(firstBanner);
            }

            @Override
            public void onAdNotLoaded() {

            }
         };
      }
   }

   public void loadRateBanner(Context context, String rateId) {
      rateBanner = new AdView(context);
      rateBanner.setAdUnitId(rateId);
      rateBanner.setAdSize(AdSize.MEDIUM_RECTANGLE);
      AdRequest request = new AdRequest.Builder().build();
      rateBanner.loadAd(request);
      rateBanner.setAdListener(new AdListener() {
         @Override
         public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            rateBanner = null;
         }

         @Override
         public void onAdLoaded() {
         }
      });
   }

   public AdView showRateBanner() {
      return rateBanner;
   }

   private AdSize getAdSize(Activity activity) {
      Display display = activity.getWindowManager().getDefaultDisplay();
      DisplayMetrics outMetrics = new DisplayMetrics();
      display.getMetrics(outMetrics);

      float widthPixels = outMetrics.widthPixels;
      float density = outMetrics.density;
      int adWidth = (int) (widthPixels / density);

      return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
   }

   public interface IInterstitialListener {

      void onAdClosed(); //If anything extra needs to do in 'OnAdCloseListener'.

      void onAdNotLoaded(); //What to do when ad has not loaded yet.
   }

   public void empty_instance() {
      try {
         mInstance = null;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
