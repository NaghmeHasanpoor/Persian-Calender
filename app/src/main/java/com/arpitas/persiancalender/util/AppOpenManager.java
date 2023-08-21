package com.arpitas.persiancalender.util;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class AppOpenManager {

   private AppOpenAd appOpenAd = null;

   private AppOpenAd.AppOpenAdLoadCallback loadCallback;

   private long loadTime = 0;

   private boolean isAppOpenLoading = false;

   private String appOpenId = "";
   private boolean isAdMobAllowed;

   private boolean isAppOpenReady() {
      return isAdMobAllowed && !isAppOpenLoading && appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
   }

   /**
    * Constructor
    */
   public void initAppOpen(Activity activity, boolean isAdMobAllowed, String appOpenId) {
      this.appOpenId = appOpenId;
      this.isAdMobAllowed = isAdMobAllowed;
      if (!isAdMobAllowed) {
         return;
      }
      fetchAd(activity);
   }

   private void fetchAd(Activity activity) {
      if (!isAdMobAllowed) return;
      isAppOpenLoading = true;
      loadCallback =
            new AppOpenAd.AppOpenAdLoadCallback() {
               @Override
               public void onAdLoaded(@NotNull AppOpenAd ad) {
                  isAppOpenLoading = false;
                  AppOpenManager.this.appOpenAd = ad;
                  AppOpenManager.this.loadTime = (new Date()).getTime();
               }

               @Override
               public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                  isAppOpenLoading = false;
                  // Handle the error.
               }
            };
      AdRequest request = getAdRequest();
      AppOpenAd.load(activity, appOpenId, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
   }

   public void showAdIfAvailable(Activity activity, AppOpenListener appOpenListener) {
      // Only show ad if there is not already an app open ad currently showing
      // and an ad is available.
      if (isAppOpenReady()) {
         FullScreenContentCallback fullScreenContentCallback =
               new FullScreenContentCallback() {
                  @Override
                  public void onAdDismissedFullScreenContent() {
                     // Set the reference to null so isAdAvailable() returns false.
                     appOpenListener.onAdDismissed();
                     AppOpenManager.this.appOpenAd = null;
                     fetchAd(activity);
                  }

                  @Override
                  public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                     appOpenListener.onAdDismissed();
                  }

                  @Override
                  public void onAdShowedFullScreenContent() {
                  }
               };

         appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
         appOpenAd.show(activity);
      } else {
         fetchAd(activity);
         appOpenListener.onAdDismissed();
      }
   }

   /**
    * Creates and returns ad request.
    */
   private AdRequest getAdRequest() {
      return new AdRequest.Builder().build();
   }

   private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
      long dateDifference = (new Date()).getTime() - this.loadTime;
      long numMilliSecondsPerHour = 3600000;
      return (dateDifference < (numMilliSecondsPerHour * numHours));
   }

   public interface AppOpenListener {

      void onAdDismissed();
   }
}