package com.arpitas.persiancalender.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.arpitas.persiancalender.entity.CityEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.arpitas.persiancalender.Constants;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.util.LanguageUtil;
import com.arpitas.persiancalender.util.SharedPrefManager;
import com.arpitas.persiancalender.util.Utils;
import com.arpitas.persiancalender.preferences.AthanNumericDialog;
import com.arpitas.persiancalender.preferences.AthanNumericPreference;
import com.arpitas.persiancalender.preferences.AthanVolumeDialog;
import com.arpitas.persiancalender.preferences.AthanVolumePreference;
import com.arpitas.persiancalender.preferences.LocationPreference;
import com.arpitas.persiancalender.preferences.LocationPreferenceDialog;
import com.arpitas.persiancalender.preferences.PrayerSelectDialog;
import com.arpitas.persiancalender.preferences.PrayerSelectPreference;
import com.arpitas.persiancalender.preferences.ShapedListDialog;
import com.arpitas.persiancalender.preferences.ShapedListPreference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import static com.arpitas.persiancalender.ApplicationContexts.prefs;

public class ApplicationPreferenceFragment extends PreferenceFragmentCompat {

   private Preference categoryAthan, lat, lon, location, athanAlarm, algoritm;
   private Utils utils;
   String azan_list = "";
   ArrayList<String> azan = new ArrayList<>();
   private boolean isEnglish;

   @Override
   public void onCreatePreferences(Bundle bundle, String s) {
      SharedPrefManager shared = new SharedPrefManager(getContext());
      if (!shared.getLanguage().equals("")) {
         LanguageUtil.changeLanguage(getContext(), shared.getLanguage());
      }

      /*----------------------------------------------------------------------------------------*/
      utils = Utils.getInstance(getContext());
      utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.location), "");

      categoryAthan = findPreference(Constants.PREF_KEY_ATHAN);
      lat = findPreference("Latitude");
      lon = findPreference("Longitude");
      location = findPreference("Location");
      athanAlarm = findPreference("AthanAlarm");
      algoritm = findPreference("PrayTimeMethod");
      updateAthanPreferencesState();

      instance = this;
   }

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      try {
         addPreferencesFromResource(R.xml.preferences);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
      super.setPreferenceScreen(preferenceScreen);
      if (preferenceScreen != null) {
         int count = preferenceScreen.getPreferenceCount();
         for (int i = 0; i < count; i++) {
            preferenceScreen.getPreference(i).setIconSpaceReserved(false);
         }
      }
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = super.onCreateView(inflater, container, savedInstanceState);
      isEnglish = new SharedPrefManager(getContext()).getLanguage().toLowerCase().equalsIgnoreCase(SharedPrefManager.EN);
      Objects.requireNonNull(view).setBackgroundResource(R.drawable.border1);
      setMargins(view, 20, 20, 20, 20);
      return view;
   }

   public void setMargins(View view, int left, int top, int right, int bottom) {
      if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
         ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
         p.setMargins(left, top, right, bottom);
         view.requestLayout();
      }
   }

   public void updateAthanPreferencesState() {
      try {
         algoritm.setSummary(prefs.getString("algoritm", isEnglish ? "Method7" : "انجمن ژئوفیزیک، دانشگاه تهران"));
         CityEntity cityEntity = utils.getCityFromPreference();
         String cityName;
         if (cityEntity != null) {
            cityName = isEnglish ? cityEntity.getEn() : cityEntity.getFa();
         } else {
            DialogFragment fragment = null;
            fragment = new LocationPreferenceDialog();
            Log.e("eliiii", "ddddd");
            if (fragment != null) {
               Bundle bundle = new Bundle(1);
               bundle.putString("key", "Location");
               fragment.setArguments(bundle);
               fragment.setTargetFragment(this, 0);
               fragment.show(getFragmentManager(),
                     "android.support.v7.preference.PreferenceFragment.DIALOG");
            }
            cityName = getResources().getString(R.string.pls_select_your_city);
         }
         boolean locationEmpty = utils.getCoordinate() == null;
         categoryAthan.setEnabled(!locationEmpty);
         if (locationEmpty) {
            categoryAthan.setSummary(R.string.athan_disabled_summary);
            lat.setVisible(true);
            lon.setVisible(true);
            location.setSummary(cityName);
         } else {
            lat.setVisible(false);
            lon.setVisible(false);
            categoryAthan.setSummary("");
            location.setSummary(cityName);
         }
         Gson gson1 = new Gson();
         String json1 = prefs.getString("azan", null);
         Type type1 = new TypeToken<ArrayList<String>>() {
         }.getType();
         azan = gson1.fromJson(json1, type1);
         if ((azan != null ? azan.size() : 0) == 0) {
            athanAlarm.setSummary(getResources().getString(R.string.pls_select_time_of_azan));
         } else {
            azan_list = "";
            for (int i = 0; i < azan.size(); i++) {
               if (i == azan.size() - 1) {
                  azan_list = azan_list + azan.get(i);
               } else {
                  azan_list = azan_list + azan.get(i) + " - ";
               }
            }
            athanAlarm.setSummary(azan_list);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public void onDisplayPreferenceDialog(Preference preference) {
      DialogFragment fragment = null;
      if (preference instanceof PrayerSelectPreference) {
         fragment = new PrayerSelectDialog();
      } else if (preference instanceof AthanVolumePreference) {
         fragment = new AthanVolumeDialog();
      } else if (preference instanceof LocationPreference) {
         fragment = new LocationPreferenceDialog();
      } else if (preference instanceof AthanNumericPreference) {
         fragment = new AthanNumericDialog();
      } else if (preference instanceof ShapedListPreference) {
         fragment = new ShapedListDialog();
      } else {
         super.onDisplayPreferenceDialog(preference);
      }

      if (fragment != null) {
         Bundle bundle = new Bundle(1);
         bundle.putString("key", preference.getKey());
         fragment.setArguments(bundle);
         fragment.setTargetFragment(this, 0);
         fragment.show(getFragmentManager(),
               "android.support.v7.preference.PreferenceFragment.DIALOG");
      }
   }

   private static ApplicationPreferenceFragment instance;

   public static void update() {
      // Total hack but better than using broadcast on wrong places
      if (instance != null) {
         instance.updateAthanPreferencesState();
      }
   }
}
