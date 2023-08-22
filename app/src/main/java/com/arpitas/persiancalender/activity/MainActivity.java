package com.arpitas.persiancalender.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.arpitas.persiancalender.Dialog.AutoUpdateDialog;
import com.arpitas.persiancalender.Dialog.CustomDialog;
import com.arpitas.persiancalender.Dialog.LanguageDialog;
import com.arpitas.persiancalender.Dialog.ProgressDialog;
import com.arpitas.persiancalender.Dialog.RatingDialog;
import com.arpitas.persiancalender.connections.NetworkUtil;
import com.arpitas.persiancalender.entity.Ad;
import com.arpitas.persiancalender.entity.AutoUpdate;
import com.arpitas.persiancalender.entity.Dialog_;
import com.google.android.material.snackbar.Snackbar;
import com.arpitas.persiancalender.Constants;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.adapter.DrawerAdapter;
import com.arpitas.persiancalender.preferences.LocationDialog;
import com.arpitas.persiancalender.service.ApplicationService;
import com.arpitas.persiancalender.util.AdMobManager;
import com.arpitas.persiancalender.util.AppBrainManager;
import com.arpitas.persiancalender.util.LanguageUtil;
import com.arpitas.persiancalender.util.SharedPrefManager;
import com.arpitas.persiancalender.util.UpdateUtils;
import com.arpitas.persiancalender.util.Utils;
import com.arpitas.persiancalender.fragment.CalendarFragment;
import com.arpitas.persiancalender.fragment.CompassFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;

public class MainActivity extends BaseActivity {

   private static final String TAG = MainActivity.class.getSimpleName();
   public static final int LANGUAGE = 1;
   public static final int CALENDAR = 2;
   public static final int COMPASS = 3;
   public static final int PREFERENCE = 4;
   public static final int SHARE = 5;
   public static final int RATE = 6;
   public static final int PRIVACY = 7;
   public static MainActivity mainActivity;
   private UpdateUtils updateUtils;
   public int menuPosition = 0;
   public Utils utils;
   private DrawerLayout drawerLayout;
   TextView title;
   private String prevLocale;
   private String prevTheme;
   ImageView adsBtn, back;
   RelativeLayout mainLayout;
   public SharedPreferences sp;
   public CalendarFragment calendarFragment;
   public static LinearLayout today;
   private Toolbar toolbar;
   TextView todayy;
   private AdView mAdView;
   private ProgressDialog progress;
   private AppUpdateManager appUpdateManager;
   private static final int RC_APP_UPDATE = 100;
   private boolean isExit = false, doubleBackToExitPressedOnce = false;

   private boolean isAdMobShowed = false;

   @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      SharedPrefManager shared = new SharedPrefManager(this);
      if (!shared.getLanguage().equals("")) {
         LanguageUtil.changeLanguage(this, shared.getLanguage());
      }

      /*----------------------------------------------------------------------------------------*/
      mainActivity = this;
      utils = Utils.getInstance(getApplicationContext());
      utils.setTheme(this);
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      super.onCreate(savedInstanceState);
      getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

      prevLocale = utils.loadLanguageFromSettings();
      prevTheme = utils.getTheme();
      updateUtils = UpdateUtils.getInstance(getApplicationContext());

      setStatusBarTranslucent(true);
      Window window = getWindow();
      WindowManager.LayoutParams winParams = window.getAttributes();
      winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
      window.setAttributes(winParams);
      window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

      if (!Utils.getInstance(this).isServiceRunning(ApplicationService.class)) {
         try {
               startService(new Intent(this, ApplicationService.class));
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      try {
         updateUtils.update(true);
      } catch (IOException e) {
         e.printStackTrace();
      }

      LinearLayout layout = new LinearLayout(this);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
      layout.setLayoutParams(params);

      setContentView(R.layout.activity_main);

      initial_views();
      check_update();
   }

   private void initial_views() {
      RelativeLayout advertiseBannerLayout = findViewById(R.id.Banner);
      if (adMobManager == null) {
         adMobManager = AdMobManager.getInstance(this);
         appBrainManager = AppBrainManager.getInstance(this);
      }
      if (ad == null || ad.isIs_google_admob()) {
         adMobManager.showFirstBanner(advertiseBannerLayout);
      } else if (ad != null && ad.isIs_google_appbrain()) {
         Utils.set_appBrain_banner(this, advertiseBannerLayout);
      }

      adsBtn = findViewById(R.id.ad);
      adsBtn.setVisibility(ad == null || ad.isIs_google_admob() || ad.isIs_google_appbrain() ? View.VISIBLE : View.INVISIBLE);
      adsBtn.setOnClickListener(view -> {
         try {
            if (ad != null) {
               if (ad.isIs_google_admob() && adMobManager != null && !isAdMobShowed) {
                  adMobManager.showInterstitialAd(this, new AdMobManager.IInterstitialListener() {
                     @Override
                     public void onAdClosed() {
                        Log.e(TAG, "Interstitial Admob onAdClosed!");
                        isAdMobShowed = true;
                     }

                     @Override
                     public void onAdNotLoaded() {
                        Log.e(TAG, "Interstitial Admob onAdNotLoaded!");
                        if (ad.isIs_google_appbrain() && appBrainManager != null) {
                           appBrainManager.showAd(new AppBrainManager.IInterstitialListener() {
                              @Override
                              public void onAdClosed() {
                                 Log.e(TAG, "Interstitial AppBrain onAdClosed!");
                              }

                              @Override
                              public void onAdNotLoaded() {
                                 Log.e(TAG, "Interstitial AppBrain onAdNotLoaded!");
                              }
                           });
                        } else {
                        }
                     }
                  });
               } else if (ad.isIs_google_appbrain() && appBrainManager != null) {
                  appBrainManager.showAd(new AppBrainManager.IInterstitialListener() {
                     @Override
                     public void onAdClosed() {
                        Log.e(TAG, "Interstitial AppBrain onAdClosed!");
                     }

                     @Override
                     public void onAdNotLoaded() {
                        Log.e(TAG, "Interstitial AppBrain onAdNotLoaded!");
                     }
                  });
               } else {
               }
            } else {
               if (adMobManager != null) {
                  adMobManager.showInterstitialAd(this, new AdMobManager.IInterstitialListener() {
                     @Override
                     public void onAdClosed() {
                        Log.e(TAG, "Interstitial Admob onAdClosed!");
                     }

                     @Override
                     public void onAdNotLoaded() {
                        Log.e(TAG, "Interstitial Admob onAdNotLoaded!");
                     }
                  });
               } else {
               }
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      });

      toolbar = findViewById(R.id.toolbar);
      calendarFragment = (CalendarFragment) this.getSupportFragmentManager().findFragmentByTag(Constants.CALENDAR_MAIN_FRAGMENT_TAG);

      today = findViewById(R.id.today);
      todayy = findViewById(R.id.todayy);

      today.setOnClickListener(view -> {
         Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT); //todo use fragment tag
         intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT, Constants.BROADCAST_TO_MONTH_FRAGMENT_RESET_DAY);
         sendBroadcast(intent);
         if (CalendarFragment.monthViewPager.getCurrentItem() != Constants.MONTHS_LIMIT / 2) {
            CalendarFragment.monthViewPager.setCurrentItem(Constants.MONTHS_LIMIT / 2);
         }
         try {
            CalendarFragment.calendarFragment.selectDay(utils.getToday());
         } catch (IOException e) {
            e.printStackTrace();
         }
      });
      title = findViewById(R.id.title);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      } else {
         toolbar.setPadding(0, 0, 0, 0);
      }

      RecyclerView navigation = findViewById(R.id.navigation_view);
      navigation.setAdapter(new DrawerAdapter(this, pos -> {
         try {
            selectItem(pos);
         } catch (IOException e) {
            e.printStackTrace();
         }
      }));
      navigation.setLayoutManager(new LinearLayoutManager(this));

      sp = getApplicationContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
      back = findViewById(R.id.back);
      mainLayout = findViewById(R.id.app_main_layout);
      setDrawerLayout();
   }

   private void setDrawerLayout() {
      drawerLayout = findViewById(R.id.drawer);
      final View appMainView = findViewById(R.id.app_main_layout);
      ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
         int slidingDirection = +1;

         {
            if (isRTL()) {
               slidingDirection = -1;
            }
         }

         @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
         private boolean isRTL() {
            return getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
         }

         @Override
         public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
         }

         @Override
         public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
         }

         @Override
         public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            slidingAnimation(drawerView, slideOffset);
         }

         private void slidingAnimation(View drawerView, float slideOffset) {
            appMainView.setTranslationX(slideOffset * drawerView.getWidth() * slidingDirection);
            drawerLayout.bringChildToFront(drawerView);
            drawerLayout.requestLayout();
         }
      };

      drawerLayout.addDrawerListener(drawerToggle);
      drawerToggle.syncState();

      toolbar.setNavigationIcon(R.drawable.menu);
      getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_holder,
                  new CalendarFragment(),
                  Constants.CALENDAR_MAIN_FRAGMENT_TAG)
            .commit();
      menuPosition = CALENDAR;
   }

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_MENU) {
         if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
         } else {
            drawerLayout.openDrawer(GravityCompat.START);
         }
         return true;
      } else {
         return super.onKeyDown(keyCode, event);
      }

   }

   private void menuChange() throws IOException {
      if (menuPosition != PREFERENCE) {
         return;
      }

      utils.updateStoredPreference(this);
      updateUtils.update(true);
      boolean needsActivityRestart = false;

      String locale = utils.getAppLanguage();
      if (!locale.equals(prevLocale)) {
         prevLocale = locale;
         utils.loadLanguageFromSettings();
         needsActivityRestart = true;
      }

      if (!prevTheme.equals(utils.getTheme())) {
         needsActivityRestart = true;
      }

      if (needsActivityRestart) {
         Intent intent = getIntent();
         finish();
         startActivity(intent);
      }
   }

   public TextView text() {
      return CalendarFragment.mah;
   }

   public void selectItem(int position) throws IOException {
      menuChange();
      switch (position) {
         case LANGUAGE:
            showLanguageDialog();
            break;
         case CALENDAR:
            if (menuPosition != CALENDAR) {
               getSupportFragmentManager()
                     .beginTransaction()
                     .replace(R.id.fragment_holder,
                           new CalendarFragment(),
                           Constants.CALENDAR_MAIN_FRAGMENT_TAG)
                     .commit();
               menuPosition = CALENDAR;
               adsBtn.setVisibility(View.VISIBLE);
               today.setVisibility(View.VISIBLE);
               back.setVisibility(View.GONE);
               title.setVisibility(View.GONE);
            }
            break;
         case COMPASS:
            if (menuPosition != COMPASS) {
               getSupportFragmentManager()
                     .beginTransaction()
                     .replace(R.id.fragment_holder, new CompassFragment())
                     .commit();

               menuPosition = COMPASS;
               adsBtn.setVisibility(View.VISIBLE);
               today.setVisibility(View.GONE);
               back.setVisibility(View.GONE);
               title.setVisibility(View.GONE);
            }
            break;
         case PREFERENCE:
            LocationDialog locationDialog = new LocationDialog();
            locationDialog.setLocationSelectListener(() -> {
               try {
                  CalendarFragment.calendarFragment.selectDay(utils.getToday());
               } catch (IOException e) {
                  e.printStackTrace();
               }
            });
            locationDialog.show(
                  getSupportFragmentManager(), LocationDialog.TAG);
//            if (menuPosition != PREFERENCE) {
//               getSupportFragmentManager()
//                     .beginTransaction()
//                     .replace(R.id.fragment_holder, new ApplicationPreferenceFragment())
//                     .commit();
//
//               menuPosition = PREFERENCE;
//            }
//            adsBtn.setVisibility(View.GONE);
//            today.setVisibility(View.GONE);
//            back.setVisibility(View.VISIBLE);
//            title.setVisibility(View.VISIBLE);
            break;
         case SHARE:
            share();
            break;
         case RATE:
            Utils.openAppRating(this);
            break;
         case PRIVACY:
            Utils.privacyPolicy(this);
            break;

      }
      drawerLayout.closeDrawers();
   }

   private void showLanguageDialog() {
      LanguageDialog languageDialog = new LanguageDialog(this, lang -> {
         recreate();
      }, false);
      Objects.requireNonNull(languageDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      languageDialog.show();
   }

   public void share() {
      Intent fileShareIntent = new Intent(Intent.ACTION_SEND);
      fileShareIntent.setType("*/*");
      fileShareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.application) + " " + getString(R.string.app_name) + " " + "\n" + getResources().getString(R.string.get_from) + "\n" +
            getString(R.string.app_link) + getPackageName());
      fileShareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      startActivity(Intent.createChooser(fileShareIntent, getResources().getString(R.string.share_with)));
   }

   public void back(View view) {
      onBackPressed();
   }

   protected void setStatusBarTranslucent(boolean makeTranslucent) {
      if (makeTranslucent) {
         getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      } else {
         getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      }
   }

   @Override
   protected void onRestart() {
      super.onRestart();
   }

   @Override
   public void onBackPressed() {
      if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
         drawerLayout.closeDrawer(GravityCompat.START);
      } else {
         if (menuPosition != CALENDAR) {
            getSupportFragmentManager()
                  .beginTransaction()
                  .replace(R.id.fragment_holder,
                        new CalendarFragment(),
                        Constants.CALENDAR_MAIN_FRAGMENT_TAG)
                  .commit();

            menuPosition = CALENDAR;
            adsBtn.setVisibility(View.VISIBLE);
            today.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
         } else {
            if (shared.isRatingBarClick()) {
               show_snack_to_exit();
            } else {
               if (isExit) {
                  show_snack_to_exit();
               } else {
                  int count = shared.getCounterShowRatinfBar();
                  count++;
                  if (count >= 2) {
                     shared.setCounterShowRatingBar(0);
                     RatingDialog rateDialog = new RatingDialog(MainActivity.this, this::finish);
                     Objects.requireNonNull(rateDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                     rateDialog.setCancelable(true);
                     rateDialog.show();
                  } else {
                     shared.setCounterShowRatingBar(count);
                     isExit = true;
                     show_snack_to_exit();
                  }
               }
            }
         }
      }

   }

   private void show_snack_to_exit() {
      if (doubleBackToExitPressedOnce) {
         super.onBackPressed();
      } else {
         this.doubleBackToExitPressedOnce = true;
         Toast.makeText(MainActivity.this, getResources().getString(R.string.txtTwoStepForExitApp), Toast.LENGTH_SHORT).show();
         new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 3000);
      }
   }

   /*--------------------------------------------------------------------------------------------*/
   private void check_update() {
      Log.e("language", "check_update...");
      progress = new ProgressDialog(MainActivity.this);
      progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      progress.show();

      /*----------------------------------------*/
      if (NetworkUtil.getInstance(this).haveNetworkConnection()) {
         Ad ad = shared.get_ad_object();
         if (ad != null && ad.getAutoUpdate() != null) {
            AutoUpdate autoUpdate = ad.getAutoUpdate();
            String type = autoUpdate.getType();
            try {
               boolean condition = autoUpdate.getLast_version() != null && !TextUtils.isEmpty(autoUpdate.getLast_version());
               if (condition && Integer.parseInt(autoUpdate.getLast_version()) > getVersionCode()) {
                  if (type.equalsIgnoreCase("1") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                     checkInAppUpdateAvailability();
                     prepare_custom_dialog();
                  } else if ((type.equalsIgnoreCase("1") && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) || type.equalsIgnoreCase("2")) {
                     show_auto_update_dialog(ad);
                  }
               } else {
                  prepare_custom_dialog();
               }
            } catch (PackageManager.NameNotFoundException e) {
               e.printStackTrace();
            }
         } else {
            try {
               new FetchAppVersionFromGooglePlayStore().execute();
            } catch (Exception e) {
               e.printStackTrace();
               prepare_custom_dialog();
            }
         }
      } else {
         progress.dismiss();
      }
   }

   private int getVersionCode() throws PackageManager.NameNotFoundException {
      return getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
   }

   private class FetchAppVersionFromGooglePlayStore extends AsyncTask<String, Void, String> {

      protected String doInBackground(String... urls) {
         String newVersion = null;
         try {
            Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en")
                  .timeout(5000)
                  .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                  .referrer("http://www.google.com")
                  .get();
            if (document != null) {
               Elements element = document.getElementsContainingOwnText("Current Version");
               for (Element ele : element) {
                  if (ele.siblingElements() != null) {
                     Elements sibElemets = ele.siblingElements();
                     for (Element sibElemet : sibElemets) {
                        newVersion = sibElemet.text();
                     }
                  }
               }
            }
         } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
         }
         return newVersion;
      }

      protected void onPostExecute(String string) {
         try {
            if (!TextUtils.isEmpty(string) && getVersionCode() < Integer.parseInt(string)) {
               Log.e(TAG, "new Version: " + string);
               show_auto_update_dialog(null);
            } else {
               Log.e(TAG, "incorrect result for new version");
               prepare_custom_dialog();
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   private void show_auto_update_dialog(Ad ad) {
      try {
         AutoUpdateDialog autoUpdateDialog = new AutoUpdateDialog(MainActivity.this, ad);
         autoUpdateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         autoUpdateDialog.setOnCancelListener(dialogInterface -> prepare_custom_dialog());
         autoUpdateDialog.show();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /*--------------------------------------------------------------------------------------------*/
   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == RC_APP_UPDATE) {
         if (resultCode != RESULT_OK) {
            Log.e(TAG, "onActivityResult: app download failed");
         }
      }
   }

   /*New Code And Functions----------------------------------------------------------------------*/
   @Override
   protected void onDestroy() {
      super.onDestroy();
      unregisterInstallStateUpdListener();
      empty_objects();
      if (mAdView != null) {
         mAdView.destroy();
      }
   }

   @Override
   protected void onPause() {
      super.onPause();
      if (mAdView != null) {
         mAdView.pause();
      }
   }

   @Override
   protected void onResume() {
      super.onResume();
      if (mAdView != null) {
         mAdView.resume();
      }
      try {
         checkNewAppVersionState();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   InstallStateUpdatedListener installStateUpdatedListener = state -> {
      if (state.installStatus() == InstallStatus.DOWNLOADED) {
         popupSnackbarForCompleteUpdateAndUnregister();
      } else if (state.installStatus() == InstallStatus.INSTALLED) {
         unregisterInstallStateUpdListener();
      } else {
         Log.e(TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
      }
   };

   private void unregisterInstallStateUpdListener() {
      if (appUpdateManager != null && installStateUpdatedListener != null) {
         appUpdateManager.unregisterListener(installStateUpdatedListener);
      }
   }

   private void checkInAppUpdateAvailability() {
      // Creates instance of the manager.
      appUpdateManager = AppUpdateManagerFactory.create(this);
      // Returns an intent object that you use to check for an update.
      Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
      // Checks that the platform will allow the specified type of update.
      appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
         if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            // Request the update.
            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
               // Before starting an update, register a listener for updates.
               appUpdateManager.registerListener(installStateUpdatedListener);
               // Start an update.
               startAppUpdateFlexible(appUpdateInfo);
            } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
               // Start an update.
               startAppUpdateImmediate(appUpdateInfo);
            }
         }
      });
      appUpdateInfoTask.addOnFailureListener(command -> {
         Log.e(TAG, "checkInAppUpdateAvailability: " + command.getMessage());
      });
   }

   private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
      try {
         appUpdateManager.startUpdateFlowForResult(
               appUpdateInfo,
               AppUpdateType.IMMEDIATE,
               // The current activity making the update request.
               this,
               // Include a request code to later monitor this update request.
               RC_APP_UPDATE);
      } catch (IntentSender.SendIntentException e) {
         e.printStackTrace();
      }
   }

   private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
      try {
         appUpdateManager.startUpdateFlowForResult(
               appUpdateInfo,
               AppUpdateType.FLEXIBLE,
               // The current activity making the update request.
               this,
               // Include a request code to later monitor this update request.
               RC_APP_UPDATE);
      } catch (IntentSender.SendIntentException e) {
         e.printStackTrace();
         unregisterInstallStateUpdListener();
      }
   }

   private void popupSnackbarForCompleteUpdateAndUnregister() {
      Snackbar snackbar = Snackbar.make(drawerLayout, getString(R.string.update_done), Snackbar.LENGTH_INDEFINITE);
      snackbar.setAction(getString(R.string.install), view -> appUpdateManager.completeUpdate());
      snackbar.setActionTextColor(getResources().getColor(R.color.dark_primary_dark));
      snackbar.getView().setBackgroundColor(getResources().getColor(R.color.dark_accent));
      snackbar.show();
      unregisterInstallStateUpdListener();
   }

   private void checkNewAppVersionState() {
      if (appUpdateManager != null) {
         appUpdateManager
               .getAppUpdateInfo()
               .addOnSuccessListener(
                     appUpdateInfo -> {
                        //FLEXIBLE:
                        // If the update is downloaded but not installed,
                        // notify the user to complete the update.
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                           popupSnackbarForCompleteUpdateAndUnregister();
                        }
                        //IMMEDIATE:
                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                           // If an in-app update is already running, resume the update.
                           startAppUpdateImmediate(appUpdateInfo);
                        }
                     });
      }
   }

   /*--------------------------------------------------------------------------------------------*/
   private void prepare_custom_dialog() {
      try {
         Ad ad = shared.get_ad_object();
         if (ad != null && ad.getDialog() != null) {
            Dialog_ dialog = ad.getDialog();
            String type = dialog.getType();
            if (!type.equalsIgnoreCase("") && !type.equalsIgnoreCase("0")) {
               if (dialog.getDialog_skip_count() == 0) {
                  show_custom_dialog(dialog);
               } else {
                  int counter = shared.get_int_value(Constants.key_counter_login_user);
                  counter++;
                  if (counter >= dialog.getDialog_skip_count()) {
                     shared.set_int_value(Constants.key_counter_login_user, 0);
                     show_custom_dialog(dialog);
                  } else {
                     shared.set_int_value(Constants.key_counter_login_user, counter);
                     progress.dismiss();
                  }
               }
            } else {
               progress.dismiss();
            }
         } else {
            progress.dismiss();
         }
      } catch (Exception e) {
         e.printStackTrace();
         progress.dismiss();
      }
   }

   private void show_custom_dialog(Dialog_ dialog) {
      try {
         CustomDialog customDialog = new CustomDialog(MainActivity.this, dialog, () -> finish());
         customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         customDialog.setOnDismissListener(dialogInterface -> progress.dismiss());
         customDialog.show();
      } catch (Exception e) {
         e.printStackTrace();
         progress.dismiss();
      }
   }

}
