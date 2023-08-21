package com.arpitas.persiancalender.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arpitas.persiancalender.entity.Ad;
import com.arpitas.persiancalender.util.AdMobManager;
import com.arpitas.persiancalender.util.AppBrainManager;
import com.arpitas.persiancalender.util.SharedPrefManager;
import com.arpitas.persiancalender.util.Utils;

public class BaseActivity extends AppCompatActivity {

   private static final String TAG = MainActivity.class.getSimpleName();
   public SharedPrefManager shared;
   public static AdMobManager adMobManager;
   public static AppBrainManager appBrainManager;
   public static Ad ad;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      shared = new SharedPrefManager(this);

      Utils.setDaviceHeight(this);
      Utils.setDeviceWidth(this);
   }

   /*Initial Instance Of AdmobManager------------------------------------------------------------*/
   public void check_admob_instance() {
      if (adMobManager == null) {
         adMobManager = AdMobManager.getInstance(this);
      }
   }

   /*Initial Instance Of AppBrainManager---------------------------------------------------------*/
   public void check_appbrain_instance() {
      if (appBrainManager == null) {
         appBrainManager = AppBrainManager.getInstance(this);
      }
   }

   /*Initial Ad Object---------------------------------------------------------------------------*/
   public void initial_ad_object() {
      if (ad == null) {
         ad = shared.get_ad_object();
      }
   }

   public void empty_objects() {
      try {
         ad = null;
         if (adMobManager != null) {
            adMobManager.empty_instance();
            adMobManager = null;
         }

         if (appBrainManager != null) {
            appBrainManager.empty_instance();
            appBrainManager = null;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   /*--------------------------------------------------------------------------------------------*/
   public void show_interstitial_ad(String key) {
      try {
         if (ad != null) {
            if (ad.isIs_google_admob() && adMobManager != null) {
               adMobManager.showInterstitialAd(this, new AdMobManager.IInterstitialListener() {
                  @Override
                  public void onAdClosed() {
                     onShowInterstitialResult(key, true);
                     Log.e(TAG, "Interstitial Admob onAdClosed!");
                  }

                  @Override
                  public void onAdNotLoaded() {
                     Log.e(TAG, "Interstitial Admob onAdNotLoaded!");
                     if (ad.isIs_google_appbrain() && appBrainManager != null) {
                        appBrainManager.showAd(new AppBrainManager.IInterstitialListener() {
                           @Override
                           public void onAdClosed() {
                              onShowInterstitialResult(key, true);
                              Log.e(TAG, "Interstitial AppBrain onAdClosed!");
                           }

                           @Override
                           public void onAdNotLoaded() {
                              onShowInterstitialResult(key, false);
                              Log.e(TAG, "Interstitial AppBrain onAdNotLoaded!");
                           }
                        });
                     } else {
                        onShowInterstitialResult(key, false);
                     }
                  }
               });
            } else if (ad.isIs_google_appbrain() && appBrainManager != null) {
               appBrainManager.showAd(new AppBrainManager.IInterstitialListener() {
                  @Override
                  public void onAdClosed() {
                     onShowInterstitialResult(key, true);
                     Log.e(TAG, "Interstitial AppBrain onAdClosed!");
                  }

                  @Override
                  public void onAdNotLoaded() {
                     onShowInterstitialResult(key, false);
                     Log.e(TAG, "Interstitial AppBrain onAdNotLoaded!");
                  }
               });
            } else {
               onShowInterstitialResult(key, false);
            }
         } else {
            if (adMobManager != null) {
               adMobManager.showInterstitialAd(this, new AdMobManager.IInterstitialListener() {
                  @Override
                  public void onAdClosed() {
                     onShowInterstitialResult(key, true);
                     Log.e(TAG, "Interstitial Admob onAdClosed!");
                  }

                  @Override
                  public void onAdNotLoaded() {
                     onShowInterstitialResult(key, false);
                     Log.e(TAG, "Interstitial Admob onAdNotLoaded!");
                  }
               });
            } else {
               onShowInterstitialResult(key, false);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         onShowInterstitialResult(key, false);
      }
   }

   public void show_interstitial_ad_splash(String key) {
      try {
         if (ad == null || ad.isIs_google_admob()) {
            if (adMobManager != null) {
               adMobManager.showInterstitialAd(this, new AdMobManager.IInterstitialListener() {
                  @Override
                  public void onAdClosed() {
                     onShowInterstitialResult(key, true);
                     Log.e(TAG, "Interstitial Admob onAdClosed!");
                  }

                  @Override
                  public void onAdNotLoaded() {
                     onShowInterstitialResult(key, false);
                     Log.e(TAG, "Interstitial Admob onAdNotLoaded!");
                  }
               });
            } else {
               onShowInterstitialResult(key, false);
            }
         } else {
            onShowInterstitialResult(key, false);
         }
      } catch (Exception e) {
         e.printStackTrace();
         onShowInterstitialResult(key, false);
      }
   }

   public void onShowInterstitialResult(String key, boolean result) {
   }
}
