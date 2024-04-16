package com.arpitas.persiancalender.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.toolbox.Volley;
import com.arpitas.persiancalender.util.ImageAdsManager;
import com.google.android.gms.ads.MobileAds;
import com.arpitas.persiancalender.BuildConfig;
import com.arpitas.persiancalender.Constants;
import com.arpitas.persiancalender.Dialog.LanguageDialog;
import com.arpitas.persiancalender.Dialog.WarningDialog;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.connections.APIClient;
import com.arpitas.persiancalender.connections.APIInterface;
import com.arpitas.persiancalender.connections.IpChecker;
import com.arpitas.persiancalender.connections.NetworkUtil;
import com.arpitas.persiancalender.entity.Ad;
import com.arpitas.persiancalender.util.AdMobManager;
import com.arpitas.persiancalender.util.AppOpenManager.AppOpenListener;
import com.arpitas.persiancalender.util.LanguageUtil;
import com.arpitas.persiancalender.util.SharedPrefManager;
import com.arpitas.persiancalender.util.Utils;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstActivity extends BaseActivity {

   private static final String TAG = FirstActivity.class.getSimpleName();
   private SharedPrefManager shared;

   private Long timeout;
   private ImageView mSplash_image;

   private ConsentInformation consentInformation;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      /**
       * *Detect Language
       * */
      shared = new SharedPrefManager(this);

      /*----------------------------------------------------------------------------------------*/
      super.onCreate(savedInstanceState);
      setContentView(R.layout.first);
      timeout = Long.valueOf(getString(R.string.timeout));
      runUpdates();

      mSplash_image = findViewById(R.id.mSplash_image);

      shared.set_string_value(Constants.reques_one, "%G(/!0o&");
      shared.set_string_value(Constants.url_two, "X7EyIg@g");
      shared.set_string_value(Constants.url_one, "3J/5%/%&");
      shared.set_string_value(Constants.request_two, "0*g&%cGq");
      Ad ad = shared.get_ad_object();
      if (ad != null) {
         ad.setAds(new ArrayList<>());
         shared.set_ad_object(ad);
      }

      setupLanguage();
   }

   private void setupLanguage() {
      String lang = shared.getLanguage();
      String currentLang = Locale.getDefault().getLanguage();
      if (!lang.isEmpty()) {
         LanguageUtil.changeLanguage(this, lang);
         if (lang.equals(SharedPrefManager.FA)) {
            mSplash_image.setImageResource(R.drawable.splash);
         } else {
            mSplash_image.setImageResource(R.drawable.en_splash);
         }
         LanguageUtil.changeLanguage(this, shared.getLanguage());
         prepare_request();
      } else if (currentLang.equals(SharedPrefManager.FA)) {
         lang = SharedPrefManager.FA;
         LanguageUtil.changeLanguage(this, lang);
         shared.setLanguage(lang);
         mSplash_image.setImageResource(R.drawable.splash);
         LanguageUtil.changeLanguage(this, shared.getLanguage());
         prepare_request();
      } else {
         Volley.newRequestQueue(this).add(
               new IpChecker(ipStatus -> {
                  String language;
                  switch (ipStatus) {
                     case iran:
                        language = SharedPrefManager.FA;
                        LanguageUtil.changeLanguage(this, language);
                        shared.setLanguage(language);
                        mSplash_image.setImageResource(R.drawable.splash);
                        LanguageUtil.changeLanguage(this, shared.getLanguage());
                        prepare_request();
                        break;
                     case outside:
                     case failed:
                        showLanguageDialog();
                        break;
                  }
               }));
      }
   }

   private void showLanguageDialog() {
      LanguageDialog languageDialog = new LanguageDialog(this, lang -> {
         LanguageUtil.changeLanguage(this, lang);
         findViewById(R.id.layout_splash).setBackgroundResource(lang.equals(SharedPrefManager.FA) ?
                 R.drawable.splash : R.drawable.en_splash);
      }, true);
      languageDialog.setOnDismissListener(dialogInterface -> {
         prepare_request();
      });
      Objects.requireNonNull(languageDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      languageDialog.show();
   }

   private void prepare_request() {
      if (NetworkUtil.getInstance(this).haveNetworkConnection()) {
         request_get_remote_plus();
      } else {
         initial_ad();
      }
   }

   private void runUpdates() {
      int versionCode = 0;
      try {
         versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
      } catch (PackageManager.NameNotFoundException e) {
         e.printStackTrace();
      }
      if (shared.get_int_value(Constants.key_version_code) == 0) {
         shared.set_int_value(Constants.key_version_code, versionCode);
      } else {
         if (shared.get_int_value(Constants.key_version_code) < versionCode) {
            shared.setStatClickRatingBar(false);
            shared.set_int_value(Constants.key_version_code, versionCode);
         }
      }
   }

   /*--------------------------------------------------------------------------------------------*/
   @SuppressWarnings("unchecked")
   private void request_get_remote_plus() {
      String decrypt_url = "";
      try {
         decrypt_url = Utils.decrypt(getResources().getString(R.string.my_url), this);
      } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
         e.printStackTrace();
      }

      /*-------------------------------------------------------*/
      String SHA1 = Utils.get_sign_App(this, "SHA1");

      /*-------------------------------------------------------*/
      APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
      Call<ResponseBody> call = null;

      /*-------------------------------------------------------*/
      if (!TextUtils.isEmpty(decrypt_url) && !TextUtils.isEmpty(SHA1)) {
//            decrypt_url += "?package=" + getPackageName();
         decrypt_url += "?package=" + getPackageName();
         decrypt_url = decrypt_url.replace("/v3/", "/v4/");
         try {
            String encrypt = Utils.encrypt(getPackageName() + "_" + SHA1, this);
            HashMap<String, String> headers = new HashMap<>();
            headers.put(Constants.key_pc1, SHA1);
            headers.put("s", "c");
            headers.put("avc", String.valueOf(BuildConfig.VERSION_CODE));
            headers.put("avn", BuildConfig.VERSION_NAME);
            headers.put(Constants.key_tn, encrypt.replace("\n", ""));

            call = apiInterface.getAdInfo(decrypt_url, headers, getPackageName());
            call.enqueue(new Callback<ResponseBody>() {
               @Override
               public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                  if (response.isSuccessful()) {
                     try {
                        Ad ad = Utils.parse_ad_json(response.body().string(), FirstActivity.this);
                        if (ad != null) {
                           shared.set_ad_object(ad);
                        }
                     } catch (IOException e) {
                        e.printStackTrace();
                     }
                     initial_ad();
                  } else {
                     Log.e(TAG, "onResponse: must stay at splash");
                     show_warning_ad_dialog();
                  }
               }

               @Override
               public void onFailure(Call<ResponseBody> call, Throwable t) {
                  Log.e(TAG, "onFailure!");
                  initial_ad();
               }
            });

         } catch (Exception e) {
            e.printStackTrace();
            initial_ad();
         }
      } else {
         initial_ad();
      }
   }

   private void show_warning_ad_dialog() {
      try {
         WarningDialog warningDialog = new WarningDialog(FirstActivity.this, () -> finish());
         warningDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         warningDialog.show();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void initial_ad() {
      ad = new SharedPrefManager(this).get_ad_object();
      initial_ad_object();
      if (ad != null && ad.isCir() && !ad.getAds().isEmpty()) {
         Constants.is_valid_ads = true;

         if (ad != null && ad.isIs_google_appbrain()) {
            Constants.isGoogleAppBrain = true;
            check_appbrain_instance();
         }

         showImageInterstitial();
      } else {
         Constants.is_valid_ads = false;
         if (ad != null && ad.isIs_google_appbrain()) {
            Constants.isGoogleAppBrain = true;
            check_appbrain_instance();
         }

         if (ad == null || ad.isIs_google_admob()) {
            Constants.isGoogleAdMob = true;
//                manageAdMobIds();
            requestAdMobUMP();
         }
      }
   }

   private void showImageInterstitial() {
      ImageAdsManager.getInstance(this).displayImageAd(findViewById(R.id.layout_splash),
              state -> start_next_activity());
   }


   private void requestAdMobUMP() {
//        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this)
//                .addTestDeviceHashedId("33BE2250B43518CCDA7DE426D04EE231")
//                .build();
      ConsentRequestParameters params = new ConsentRequestParameters
              .Builder()
              .setTagForUnderAgeOfConsent(false)
              .build();

      consentInformation = UserMessagingPlatform.getConsentInformation(this);
      if (consentInformation.isConsentFormAvailable()) {
         initial_admob_ads();
      } else {
         consentInformation.requestConsentInfoUpdate(
                 this,
                 params,
                 () -> {
                    //request success
                    Log.e(TAG, "requestAdMobUMP: " + "hello");
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            this,
                            loadAndShowError -> {
                               if (loadAndShowError != null) {
                                  // Consent gathering failed.
                                  Log.w("TAGG", String.format("%s: %s",
                                          loadAndShowError.getErrorCode(),
                                          loadAndShowError.getMessage()));
                               }

                               // Consent has been gathered.
                               initial_admob_ads();
                               Log.e("TAGG", "requestAdMobUMP: gathered admob");
                            }
                    );
                 },
                 requestConsentError -> {
                    // Consent gathering failed.
                    Log.w("TAGG", String.format("%so: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));

                    initial_admob_ads();
                 });
      }
   }

//   private void manageAdMobIds() {
//      MobileAds.initialize(getApplicationContext());
//      initialAppOpenAd();
//      check_adMob_instance();
//      initialMainActivityBanner();
//
//
//      showAppOpenAd();
//   }

   private void initial_admob_ads() {
      ad = shared.get_ad_object();
      try {
         String admob_app_id = new String(Base64.decode(ad.getAdmob_app_id(), Base64.DEFAULT), "UTF-8");
         String banner_id = new String(Base64.decode(ad.getAdmob_banner_Id(), Base64.DEFAULT), "UTF-8");
         String admob_interstitial_id = new String(Base64.decode(ad.getAdmob_interstitial_Id(), Base64.DEFAULT), "UTF-8");
         String admob_rate_id = new String(Base64.decode(ad.getAdmob_rate_id(), Base64.DEFAULT), "UTF-8");
         String admob_native_id = new String(Base64.decode(ad.getAdmob_native_id(), Base64.DEFAULT), "UTF-8");
         String admob_reward_id = new String(Base64.decode(ad.getAdmob_reward_id(), Base64.DEFAULT), "UTF-8");
         String appOpenId = new String(Base64.decode(ad.getAppOpenId(), Base64.DEFAULT), "UTF-8");

         if (!TextUtils.isEmpty(admob_app_id)) {
            shared.set_string_value(Constants.admob_appId_key_shared, admob_app_id);
         }

         if (!TextUtils.isEmpty(banner_id)) {
            shared.set_string_value(Constants.admob_banner_key_shared, banner_id);
         }

         if (!TextUtils.isEmpty(admob_interstitial_id)) {
            shared.set_string_value(Constants.admob_interstitial_id_key_shared, admob_interstitial_id);
         }

         if (!TextUtils.isEmpty(admob_rate_id)) {
            shared.set_string_value(Constants.admob_rate_id_key_shared, admob_rate_id);
         }

         if (!TextUtils.isEmpty(admob_native_id)) {
            shared.set_string_value(Constants.admob_native_id_key_shared, admob_native_id);
         }

         if (!TextUtils.isEmpty(admob_reward_id)) {
            shared.set_string_value(Constants.admob_reward_id_key_shared, admob_reward_id);
         }

         if (!TextUtils.isEmpty(appOpenId)) {
            shared.set_string_value(Constants.admob_appOpen_id_key_shared, appOpenId);
         }

         if (!TextUtils.isEmpty(ad.getContent_rating())) {
            shared.set_string_value(Constants.contentRating, ad.getContent_rating());
         }

         if (!TextUtils.isEmpty(ad.getTimeout())) {
            shared.set_string_value(Constants.timeout, ad.getTimeout());
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      try {
         manageAdmobIds();
      } catch (PackageManager.NameNotFoundException e) {
         e.printStackTrace();
      }
   }

   private void manageAdmobIds() throws PackageManager.NameNotFoundException {
      String contentRating, bannerId, rateId, appOpenid;
      boolean isAdMobAllowed = true;
      if (TextUtils.isEmpty(shared.get_string_value(Constants.admob_appId_key_shared))) {
         contentRating = getString(R.string.cr);
         bannerId = getString(R.string.abnt);
         rateId = getString(R.string.arbnt);
         timeout = Long.valueOf(getString(R.string.timeout));
         appOpenid = getString(R.string.aoa);
      } else {
         contentRating = shared.get_string_value(Constants.contentRating);
         bannerId = shared.get_string_value(Constants.admob_banner_key_shared);
         rateId = shared.get_string_value(Constants.admob_rate_id_key_shared);
         try {
            timeout = Long.valueOf(shared.get_string_value(Constants.timeout));
         } catch (NumberFormatException e) {
            e.printStackTrace();
         }
         appOpenid = shared.get_string_value(Constants.admob_appOpen_id_key_shared);
         try {
            isAdMobAllowed = shared.get_ad_object().isIs_google_admob();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      MobileAds.initialize(this, initializationStatus -> {
         });
         adMobManager = AdMobManager.getInstance(this);
      adMobManager.initAppOpen(this, isAdMobAllowed, appOpenid);
      adMobManager.loadFirstBanner(this, bannerId);
      int count = shared.getCounterShowRatinfBar();
      count++;
      if (count >= 2) {
         adMobManager.loadRateBanner(this, rateId);
      }


      show_app_open();
   }

   private void show_app_open() {
      new Handler(Looper.getMainLooper()).postDelayed(() -> {
         if (ad != null) {
            if (ad.isIs_google_admob()) {
               adMobManager = AdMobManager.getInstance(this);
               adMobManager.showAppOpen(this, new AppOpenListener() {
                  @Override
                  public void onAdDismissed() {
                     if (isDestroyed()) {
                        return;
                     }
                     start_next_activity();
                  }
               });
            } else {
               if (isDestroyed()) {
                  return;
               }
               start_next_activity();
            }
         } else {
            if (isDestroyed()) {
               return;
            }
            start_next_activity();
         }
      }, timeout);
   }

   private void start_next_activity() {
      startActivity(new Intent(FirstActivity.this, MainActivity.class));
      new Handler(Looper.getMainLooper()).postDelayed(FirstActivity.this::finish, 100);
   }

   @Override
   public void onBackPressed() {
   }
}
