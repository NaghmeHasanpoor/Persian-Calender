package com.arpitas.persiancalender.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.entity.Ad;
import com.arpitas.persiancalender.interfaces.RatingApp;
import com.arpitas.persiancalender.Constants;
import com.arpitas.persiancalender.util.AdMobManager;
import com.arpitas.persiancalender.util.SharedPrefManager;
import com.arpitas.persiancalender.util.Utils;


public class RatingDialog extends Dialog implements View.OnTouchListener {

   private static String TAG = RatingDialog.class.getSimpleName();
   private RatingBar ratingBar;
   private RelativeLayout adContainer;
   private ProgressBar exitDialogBannerProgress;
   private RatingApp listener;
   private SharedPrefManager shared;
   private Ad ad;

   public RatingDialog(@NonNull Context context, RatingApp listener) {
      super(context);
      this.listener = listener;
   }

   @SuppressLint("ClickableViewAccessibility")
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.layout_rating_dialog);
      initialViews();
      shared = new SharedPrefManager(getContext());
      ad = shared.get_ad_object();
      if (ad != null) {
         if (ad.isIs_google_admob()) {
            setupBannerAd();
         }
      } else {
         setupBannerAd();
      }
      ratingBar.setOnTouchListener(this);
   }

   private void initialViews() {
      ratingBar = findViewById(R.id.ratingBar);
      adContainer = findViewById(R.id.adContainer);
      exitDialogBannerProgress = findViewById(R.id.exitDialogBannerProgress);
   }

   private void setupBannerAd() {
      AdView rateBanner = AdMobManager.getInstance(getContext()).showRateBanner();

      if (rateBanner != null) {
         adContainer.removeAllViews();
         try {
            ((RelativeLayout) rateBanner.getParent()).removeAllViews();
         } catch (Exception ignore) {
         }
         adContainer.addView(rateBanner);
         exitDialogBannerProgress.setVisibility(View.INVISIBLE);
      } else {
         String admob_banner = "";
         if (!TextUtils.isEmpty(shared.get_string_value(Constants.admob_rate_id_key_shared))) {
            admob_banner = shared.get_string_value(Constants.admob_rate_id_key_shared);
         } else {
            admob_banner = getContext().getResources().getString(R.string.arbnt);
         }

         rateBanner = new AdView(getContext());
         rateBanner.setAdUnitId(admob_banner);
         rateBanner.setAdSize(AdSize.MEDIUM_RECTANGLE);
         AdRequest request = new AdRequest.Builder().build();
         rateBanner.loadAd(request);
         AdView finalRateBanner = rateBanner;
         rateBanner.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            }

            @Override
            public void onAdLoaded() {
               adContainer.removeAllViews();
               try {
                  ((RelativeLayout) finalRateBanner.getParent()).removeAllViews();
               } catch (Exception ignore) {
               }
               adContainer.addView(finalRateBanner);
               exitDialogBannerProgress.setVisibility(View.INVISIBLE);
            }
         });
      }
   }

   @Override
   public boolean onTouch(View v, MotionEvent event) {
      if (v.getId() == R.id.ratingBar) {
         int stars = 0;
         float touchPositionX, width, starsf;
         if (event.getAction() == MotionEvent.ACTION_UP) {
            touchPositionX = event.getX();
            width = ratingBar.getWidth();
            starsf = (touchPositionX / width) * 5.0f;
            stars = (int) starsf + 1;
            ratingBar.setRating(stars);
            Log.e("stars", stars + " ");

            final int finalStars = stars;
            new Handler().postDelayed(() -> {
               if (finalStars <= 4) {
                  if (listener != null) {
                     listener.OnClickRating();
                  }
               } else {
                  Utils.openAppRating(RatingDialog.this.getContext());
               }
               shared.setStatClickRatingBar(true);
               dismiss();
            }, 500);
            v.setPressed(false);
         }
         if (event.getAction() == MotionEvent.ACTION_DOWN) {
            v.setPressed(true);
         }
         if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            v.setPressed(false);
         }
         return true;
      }
      return false;
   }

}
