package com.arpitas.persiancalender.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceViewHolder;

import com.appbrain.AppBrainBanner;
import com.appbrain.BannerListener;
import com.arpitas.persiancalender.entity.AdItem;
import com.google.android.gms.ads.AdSize;
import com.arpitas.persiancalender.calendar.AbstractDate;
import com.arpitas.persiancalender.calendar.CivilDate;
import com.arpitas.persiancalender.calendar.DateConverter;
import com.arpitas.persiancalender.calendar.DayOutOfRangeException;
import com.arpitas.persiancalender.calendar.IslamicDate;
import com.arpitas.persiancalender.calendar.LocaleData;
import com.arpitas.persiancalender.calendar.PersianDate;
import com.arpitas.persiancalender.entity.Ad;
import com.arpitas.persiancalender.entity.AutoUpdate;
import com.arpitas.persiancalender.entity.CityEntity;
import com.arpitas.persiancalender.entity.DayEntity;
import com.arpitas.persiancalender.entity.Dialog_;
import com.arpitas.persiancalender.entity.EventEntity;
import com.arpitas.persiancalender.ApplicationContexts;
import com.arpitas.persiancalender.Constants;

import com.arpitas.persiancalender.enums.SeasonEnum;
import com.arpitas.persiancalender.service.BroadcastReceivers;
import com.arpitas.persiancalender.praytimes.CalculationMethod;
import com.arpitas.persiancalender.praytimes.Clock;
import com.arpitas.persiancalender.praytimes.Coordinate;
import com.arpitas.persiancalender.praytimes.PrayTime;
import com.arpitas.persiancalender.praytimes.PrayTimesCalculator;
import com.arpitas.persiancalender.TypefaceSpan;
import com.arpitas.persiancalender.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

    private static final String TAG = Utils.class.getName();
    private static Utils myInstance;
    private com.arpitas.persiancalender.util.LocaleUtils localeUtils;
    private Context context;
    private Typeface typeface;
    private SharedPreferences prefs;
    private List<EventEntity> events = new ArrayList<>();
    private List<EventEntity> hijriEvents = new ArrayList<>();
    private List<EventEntity> gregorianEvents = new ArrayList<>();
    private String cachedCityKey = "";
    private CityEntity cachedCity;

    private Utils(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        updateStoredPreference(context);
        try {
            readEventsFromJSON(context.getAssets().open("events.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Utils getInstance(Context context) {
        if (myInstance == null) {
            myInstance = new Utils(context.getApplicationContext());
        }
        return myInstance;
    }

    public String shape(String text) {
        return text;
    }

    public String getString(String key) {
        return localeUtils == null
                ? ""
                : localeUtils.getString(key);
    }

    public String programVersion() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Name not found on PersianCalendarUtils.programVersion");
            return "";
        }
    }

    public void setFont(TextView textView) {
        textView.setTypeface(ApplicationContexts.typeface1);
    }

    public void setFontAndShape(TextView textView) {
        setFont(textView);
        textView.setText(shape(textView.getText().toString()));
    }

    public void setFontAndShape(PreferenceViewHolder holder) {
        // See android.support.v7.preference.Preference#onBindViewHolder
        TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        if (titleView != null) {
            titleView.setTypeface(ApplicationContexts.typeface2);
            //setFontAndShape(titleView);
            titleView.setTextColor(context.getResources().getColor(R.color.white));
        }
        TextView summaryView = (TextView) holder.findViewById(android.R.id.summary);
        if (summaryView != null) {
            // setFontAndShape(summaryView);
            summaryView.setTypeface(ApplicationContexts.typeface1);
            summaryView.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    public void setActivityTitleAndSubtitle(Activity activity, String title, String subtitle) {
        if (title == null || subtitle == null) {
            return;
        }
        ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (supportActionBar == null) {
            return;
        }
        SpannableString titleSpan = new SpannableString(shape(title));
        titleSpan.setSpan(new TypefaceSpan(typeface), 0, titleSpan.length(), 0);
        titleSpan.setSpan(new RelativeSizeSpan(0.8f), 0, titleSpan.length(), 0);
        supportActionBar.setTitle(titleSpan);

        SpannableString subtitleSpan = new SpannableString(shape(subtitle));
        subtitleSpan.setSpan(new TypefaceSpan(typeface), 0, subtitleSpan.length(), 0);
        subtitleSpan.setSpan(new RelativeSizeSpan(0.8f), 0, subtitleSpan.length(), 0);
        supportActionBar.setSubtitle(subtitleSpan);
    }

    public CalculationMethod getCalculationMethod() {
        // It seems Iran is using Jafari method
        return CalculationMethod.valueOf(prefs.getString(
                Constants.PREF_PRAY_TIME_METHOD,
                Constants.DEFAULT_PRAY_TIME_METHOD));
    }

    public int getIslamicOffset() {
        return Integer.parseInt(prefs.getString(
                Constants.PREF_ISLAMIC_OFFSET,
                Constants.DEFAULT_ISLAMIC_OFFSET).replace("+", ""));
    }

    public Coordinate getCoordinate() {
        CityEntity cityEntity = getCityFromPreference();
        if (cityEntity != null) {
            return cityEntity.getCoordinate();
        }
        try {
            Coordinate coord = new Coordinate(
                    Double.parseDouble(prefs.getString(
                            Constants.PREF_LATITUDE,
                            Constants.DEFAULT_LATITUDE)),

                    Double.parseDouble(prefs.getString(
                            Constants.PREF_LONGITUDE,
                            Constants.DEFAULT_LONGITUDE)),

                    Double.parseDouble(prefs.getString(
                            Constants.PREF_ALTITUDE,
                            Constants.DEFAULT_ALTITUDE))
            );

            // If latitude or longitude is zero probably preference is not set yet
            if (coord.getLatitude() == 0 && coord.getLongitude() == 0) {
                return null;
            }

            return coord;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private char[] preferredDigits;
    private boolean clockIn24;
    public boolean iranTime;

    public void updateStoredPreference(Context context) {
        preferredDigits = new SharedPrefManager(context).getLanguage().toLowerCase().equalsIgnoreCase(SharedPrefManager.EN)
                ? Constants.ARABIC_DIGITS
                : Constants.PERSIAN_DIGITS;
        clockIn24 = prefs.getBoolean(Constants.PREF_WIDGET_IN_24, Constants.DEFAULT_WIDGET_IN_24);
        iranTime = prefs.getBoolean(Constants.PREF_IRAN_TIME, Constants.DEFAULT_IRAN_TIME);
    }

    public boolean isPersianDigitSelected() {
        return prefs.getBoolean(Constants.PREF_PERSIAN_DIGITS, Constants.DEFAULT_PERSIAN_DIGITS);
    }

    public void setTheme(Context context) {
        String key = prefs.getString(Constants.PREF_THEME, "");
        int theme = R.style.DarkTheme; // default theme
        if (key.equals(Constants.LIGHT_THEME)) {
            theme = R.style.DarkTheme;
        } else if (key.equals(Constants.DARK_THEME)) {
            theme = R.style.DarkTheme;
        }
        context.setTheme(theme);
    }

    public boolean isWidgetClock() {
        return prefs.getBoolean(Constants.PREF_WIDGET_CLOCK, Constants.DEFAULT_WIDGET_CLOCK);
    }

    public boolean isNotifyDate() {
        return prefs.getBoolean(Constants.PREF_NOTIFY_DATE, Constants.DEFAULT_NOTIFY_DATE);
    }

    public int getAthanVolume() {
        return prefs.getInt(Constants.PREF_ATHAN_VOLUME, Constants.DEFAULT_ATHAN_VOLUME);
    }

    public String getAppLanguage() {
        String language = prefs.getString(
                Constants.PREF_APP_LANGUAGE,
                Constants.DEFAULT_APP_LANGUAGE);

        return TextUtils.isEmpty(language) ? Constants.DEFAULT_APP_LANGUAGE : language;
    }

    public String getTheme() {
        return prefs.getString(Constants.PREF_THEME, Constants.LIGHT_THEME);
    }

    public String getSelectedWidgetTextColor() {
        return prefs.getString(
                Constants.PREF_SELECTED_WIDGET_TEXT_COLOR,
                Constants.DEFAULT_SELECTED_WIDGET_TEXT_COLOR);
    }

    public PersianDate getToday() {
        CivilDate civilDate = new CivilDate();
        return DateConverter.civilToPersian(civilDate);
    }

    public Calendar makeCalendarFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (iranTime) {
            calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        }
        calendar.setTime(date);
        return calendar;
    }

    public String clockToString(int hour, int minute) {
        return formatNumber(String.format(Locale.ENGLISH, "%d:%02d", hour, minute));
    }

    public String getPersianFormattedClock(Clock clock, boolean isEnglish) {
        String timeText = null;
        int hour = clock.getHour();
        if (!clockIn24) {
            if (hour >= 12) {
                timeText = isEnglish ? Constants.PM_IN_ENGLISH : Constants.PM_IN_PERSIAN;
                hour -= 12;
            } else {
                timeText = isEnglish ? Constants.AM_IN_ENGLISH : Constants.AM_IN_PERSIAN;
            }
        }
        String result = clockToString(hour, clock.getMinute());
        if (!clockIn24) {
            result = result + " " + timeText;
        }
        return isEnglish ? faToEn(result) : enToFa(result);
    }

    public String formatNumber(int number) {
        return formatNumber(Integer.toString(number));
    }

    public String formatNumber(String number) {
        if (preferredDigits == Constants.ARABIC_DIGITS) {
            return number;
        }

        StringBuilder sb = new StringBuilder();
        for (char i : number.toCharArray()) {
            if (Character.isDigit(i)) {
                sb.append(preferredDigits[Integer.parseInt(i + "")]);
            } else {
                sb.append(i);
            }
        }
        return sb.toString();
    }

    public String dateToString(AbstractDate date) {
        boolean isEnglish = new SharedPrefManager(context).getLanguage().toLowerCase().equalsIgnoreCase(SharedPrefManager.EN);
        return /*formatNumber(date.getDayOfMonth())*/(isEnglish ? faToEn(String.valueOf(date.getDayOfMonth())) : enToFa(String.valueOf(date.getDayOfMonth()))) + ' '
                + getMonthName(date) + ' '
                + (isEnglish ? faToEn(String.valueOf(date.getYear())) : enToFa(String.valueOf(date.getYear())))/*formatNumber(date.getYear())*/;
    }

    public String dayTitleSummary(PersianDate persianDate) {
        return getWeekDayName(persianDate) + Constants.PERSIAN_COMMA + " "
                + dateToString(persianDate);
    }

    public String getMonthName(AbstractDate date) {
        boolean isEnglish = new SharedPrefManager(context).getLanguage().equalsIgnoreCase(SharedPrefManager.EN);
        String monthName = "";
        // zero based
        int month = date.getMonth() - 1;

        if (date.getClass().equals(PersianDate.class)) {
            LocaleData.PersianMonthNames monthNameCode = LocaleData.PersianMonthNames.values()[month];
            monthName = isEnglish ? String.valueOf(monthNameCode) : getString(String.valueOf(monthNameCode));
        } else if (date.getClass().equals(CivilDate.class)) {
            LocaleData.CivilMonthNames monthNameCode = LocaleData.CivilMonthNames.values()[month];
            monthName = isEnglish ? String.valueOf(monthNameCode) : getString(String.valueOf(monthNameCode));
        } else if (date.getClass().equals(IslamicDate.class)) {
            LocaleData.IslamicMonthNames monthNameCode = LocaleData.IslamicMonthNames.values()[month];
            monthName = isEnglish ? String.valueOf(monthNameCode) : getString(String.valueOf(monthNameCode));
        }
        return monthName;
    }

    public String getWeekDayName(AbstractDate date) {
        boolean isEnglish = new SharedPrefManager(context).getLanguage().equalsIgnoreCase(SharedPrefManager.EN);
        CivilDate civilDate;
        if (date.getClass().equals(PersianDate.class)) {
            civilDate = DateConverter.persianToCivil((PersianDate) date);
        } else if (date.getClass().equals(IslamicDate.class)) {
            civilDate = DateConverter.islamicToCivil((IslamicDate) date);
        } else {
            civilDate = (CivilDate) date;
        }

        // zero based
        int dayOfWeek = civilDate.getDayOfWeek() - 1;
        LocaleData.WeekDayNames weekDayNameCode = LocaleData.WeekDayNames.values()[dayOfWeek];

        return isEnglish ? weekDayNameCode.toString() : getString(weekDayNameCode.toString());
    }

    public void quickToast(String message) {
        Toast.makeText(context, shape(message), Toast.LENGTH_SHORT).show();
    }

    private String convertStreamToString(InputStream is) {
        // http://stackoverflow.com/a/5445161
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String persianStringToArabic(String text) {
        return text
                .replaceAll("ی", "ي")
                .replaceAll("ک", "ك")
                .replaceAll("گ", "كی")
                .replaceAll("ژ", "زی")
                .replaceAll("چ", "جی")
                .replaceAll("پ", "بی");
    }

    public static String enToFa(String s) {
        return s.replace("0", "۰")
                .replace("1", "۱")
                .replace("2", "۲")
                .replace("3", "۳")
                .replace("4", "۴")
                .replace("5", "۵")
                .replace("6", "۶")
                .replace("7", "۷")
                .replace("8", "۸")
                .replace("9", "۹");
    }

    public static String faToEn(String s) {
        return s.replace("۰", "0")
                .replace("۱", "1")
                .replace("۲", "2")
                .replace("۳", "3")
                .replace("۴", "4")
                .replace("۵", "5")
                .replace("۶", "6")
                .replace("۷", "7")
                .replace("۸", "8")
                .replace("۹", "9");
    }

    private <T> Iterable<T> iteratorToIterable(final Iterator<T> iterator) {
        return () -> iterator;
    }

    public List<CityEntity> getAllCities(boolean needsSort) {
        boolean isEnglish = new SharedPrefManager(context).getLanguage().equalsIgnoreCase(SharedPrefManager.EN);
        List<CityEntity> result = new ArrayList<>();
        try {
            JSONObject countries = new JSONObject(convertStreamToString(context.getAssets().open("cities.json")));

            for (String countryCode : iteratorToIterable(countries.keys())) {
                JSONObject country = countries.getJSONObject(countryCode);
                String countryEn = country.getString("en");
                String countryFa = country.getString("fa");
                JSONObject cities = country.getJSONObject("cities");
                for (String key : iteratorToIterable(cities.keys())) {
                    JSONObject city = cities.getJSONObject(key);
                    String en = city.getString("en");
                    String fa = city.getString("fa");
                    Coordinate coordinate = new Coordinate(city.getDouble("latitude"), city.getDouble("longitude"),
                            0 // city.getDouble("elevation")
                    );
                    result.add(new CityEntity(key, en, fa, countryCode, countryEn, countryFa, coordinate));
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        if (!needsSort) {
            return result;
        }

        CityEntity[] cities = result.toArray(new CityEntity[result.size()]);
        // Sort first by country code then city
        Arrays.sort(cities, (l, r) -> {
            if (l.getKey().equals("CUSTOM")) {
                return -1;
            }
            if (r.getKey().equals("CUSTOM")) {
                return 1;
            }
            int compare = r.getCountryCode().compareTo(l.getCountryCode());
            if (compare != 0) {
                return compare;
            }
            if (isEnglish) {
                return l.getEn().compareTo(r.getEn());
            } else {
                return persianStringToArabic(l.getFa()).compareTo(persianStringToArabic(r.getFa()));
            }
        });

        return Arrays.asList(cities);
    }

    public CityEntity getCityFromPreference() {
        String key = prefs.getString("Location", "");
        if (TextUtils.isEmpty(key) || key.equals("CUSTOM") || key.equals("")) {
            return null;
        }
        if (key.equals(cachedCityKey)) {
            return cachedCity;
        }
        cachedCityKey = key;
        for (CityEntity cityEntity : getAllCities(false)) {
            if (cityEntity.getKey().equals(key)) {
                cachedCity = cityEntity;
                return cityEntity;
            }
        }

        cachedCity = null;
        return null;
    }

    private void readEventsFromJSON(InputStream is) {
        try {
            String json = String.valueOf(new JSONObject(convertStreamToString(is)));
            parseJsonPersian(json);
            parseJsonHijri(json);
            parseJsonGregorian(json);

//         int length = json.length();
//         for (int i = 0; i < length; ++i) {
//            JSONObject event = json.getJSONObject(i);
//
//            int year = event.getInt("year");
//            int month = event.getInt("month");
//            int day = event.getInt("day");
//            String title = event.getString("title");
//            boolean holiday = event.getBoolean("holiday");
//
//            result.add(new EventEntity(new PersianDate(year, month, day), title, holiday));
//         }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void parseJsonPersian(String json) {
        try {
            JSONArray questionsJsonArray = new JSONObject(json).getJSONArray("Persian Calendar");
            JSONObject questionJsonObject;
            for (int i = 0; i < questionsJsonArray.length(); i++) {
                questionJsonObject = questionsJsonArray.getJSONObject(i);
                PersianDate persianDate = new PersianDate();
                EventEntity HEvent = new EventEntity();
                persianDate.setDayOfMonth(questionJsonObject.getInt("day"));
                persianDate.setMonth(questionJsonObject.getInt("month"));
                persianDate.setYear(1402);
                HEvent.setDate(persianDate);
                HEvent.setTitle(questionJsonObject.getString("title"));
                HEvent.setHoliday(questionJsonObject.getBoolean("holiday") && questionJsonObject.getString("type").equals("Iran"));
                events.add(HEvent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonHijri(String json) {
        try {
            JSONArray questionsJsonArray = new JSONObject(json).getJSONArray("Hijri Calendar");
            JSONObject questionJsonObject;
            for (int i = 0; i < questionsJsonArray.length(); i++) {
                questionJsonObject = questionsJsonArray.getJSONObject(i);
                PersianDate persianDate = new PersianDate();
                EventEntity HEvent = new EventEntity();
                persianDate.setDayOfMonth(questionJsonObject.getInt("day"));
                persianDate.setMonth(questionJsonObject.getInt("month"));
                persianDate.setYear(1402);
                HEvent.setDate(persianDate);
                HEvent.setTitle(questionJsonObject.getString("title"));
                HEvent.setHoliday(questionJsonObject.getBoolean("holiday") && questionJsonObject.getString("type").equals("Iran"));
                hijriEvents.add(HEvent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonGregorian(String json) {
        try {
            JSONArray questionsJsonArray = new JSONObject(json).getJSONArray("Gregorian Calendar");
            JSONObject questionJsonObject;
            for (int i = 0; i < questionsJsonArray.length(); i++) {
                questionJsonObject = questionsJsonArray.getJSONObject(i);
                PersianDate persianDate = new PersianDate();
                EventEntity HEvent = new EventEntity();
                persianDate.setDayOfMonth(questionJsonObject.getInt("day"));
                persianDate.setMonth(questionJsonObject.getInt("month"));
                persianDate.setYear(1402);
                HEvent.setDate(persianDate);
                HEvent.setTitle(questionJsonObject.getString("title"));
                HEvent.setHoliday(questionJsonObject.getBoolean("holiday") && questionJsonObject.getString("type").equals("Iran"));
                gregorianEvents.add(HEvent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<EventEntity> getEvents(PersianDate day) throws IOException {
        IslamicDate islamicDate = DateConverter.persianToIslamic(day);
        CivilDate civilDate = DateConverter.persianToCivil(day);

        List<EventEntity> result = new ArrayList<>();
        for (EventEntity eventEntity : events) {
            if (eventEntity.getDate().equals(day)) {
                result.add(eventEntity);
            }
        }

        for (EventEntity eventEntity : gregorianEvents) {
            if (eventEntity.getDate().getDayOfMonth() == civilDate.getDayOfMonth() && eventEntity.getDate().getMonth() == civilDate.getMonth()) {
                result.add(eventEntity);
            }
        }

        for (EventEntity eventEntity : hijriEvents) {
            if (eventEntity.getDate().getDayOfMonth() == islamicDate.getDayOfMonth() && eventEntity.getDate().getMonth() == islamicDate.getMonth()) {
                result.add(eventEntity);
            }
        }
        return result;
    }

    public String getEventsTitle(PersianDate day, boolean holiday) throws IOException {
        String titles = "";
        boolean first = true;
        List<EventEntity> dayEvents = getEvents(day);

        for (EventEntity event : dayEvents) {
            if (event.isHoliday() == holiday) {
                if (first) {
                    first = false;

                } else {
                    titles = titles + "\n";
                }
                titles = titles + event.getTitle();
            }
        }
        return titles;
    }

    public void loadApp() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 1);
        Intent intent = new Intent(context, BroadcastReceivers.class);
        intent.setAction(Constants.BROADCAST_RESTART_APP);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        else
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            | PendingIntent.FLAG_MUTABLE);
        alarmManager.set(AlarmManager.RTC, startTime.getTimeInMillis(), pendingIntent);
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void loadAlarms() {
        Log.d(TAG, "reading and loading all alarms from prefs");
        String prefString = prefs.getString(Constants.PREF_ATHAN_ALARM, "");
        CalculationMethod calculationMethod = getCalculationMethod();
        Coordinate coordinate = getCoordinate();

        if (calculationMethod != null && coordinate != null && !TextUtils.isEmpty(prefString)) {
            PrayTimesCalculator calculator = new PrayTimesCalculator(calculationMethod);
            Map<PrayTime, Clock> prayTimes = calculator.calculate(new Date(), coordinate);

            String[] alarmTimesNames = TextUtils.split(prefString, ",");
            for (int i = 0; i < alarmTimesNames.length; i++) {

                Clock alarmTime = prayTimes.get(PrayTime.valueOf(alarmTimesNames[i]));

                if (alarmTime != null) {
                    setAlarm(PrayTime.valueOf(alarmTimesNames[i]), alarmTime, i);
                }
            }
        }
    }

    public void setAlarm(PrayTime prayTime, Clock clock, int id) {
        Calendar triggerTime = Calendar.getInstance();
        triggerTime.set(Calendar.HOUR_OF_DAY, clock.getHour());
        triggerTime.set(Calendar.MINUTE, clock.getMinute());
        setAlarm(prayTime, triggerTime.getTimeInMillis(), id);
    }

    public void setAlarm(PrayTime prayTime, long timeInMillis, int id) {
        String valAthanGap = prefs.getString(Constants.PREF_ATHAN_GAP, "0");
        long athanGap;
        try {
            athanGap = (long) (Double.parseDouble(valAthanGap) * 60);
        } catch (NumberFormatException e) {
            athanGap = 0;
        }

        Calendar triggerTime = Calendar.getInstance();
        triggerTime.setTimeInMillis(timeInMillis - TimeUnit.SECONDS.toMillis(athanGap));
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (!triggerTime.before(Calendar.getInstance())) {
            Log.d(TAG, "setting alarm for: " + triggerTime.getTime());

            Intent intent = new Intent(context, BroadcastReceivers.class);
            intent.setAction(Constants.BROADCAST_ALARM);
            intent.putExtra(Constants.KEY_EXTRA_PRAYER_KEY, prayTime.name());
            PendingIntent pendingIntent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                SetExactAlarm.setExactAlarm(alarmManager,
                        AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), pendingIntent);
            }
        }
    }

    private static class SetExactAlarm {

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public static void setExactAlarm(AlarmManager alarmManager, int type, long triggerAtMillis, PendingIntent pendingIntent) {
            alarmManager.setExact(type, triggerAtMillis, pendingIntent);
        }
    }

    public Uri getAthanUri() {
        String defaultSoundUri = "file://android_asset/athan.ogg";
        return Uri.parse(defaultSoundUri);
    }

    public void changeAppLanguage(String localeCode) {
        Locale locale = TextUtils.isEmpty(localeCode) ? Locale.getDefault() : new Locale(localeCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = context.getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void changeCalendarLanguage(String localeCode) {
        if (localeUtils == null) {
            localeUtils = LocaleUtils.getInstance(context, localeCode);
        }
        localeUtils.changeLocale(localeCode);
    }

    public String loadLanguageFromSettings() {
        // set app language
        String locale = getAppLanguage();
        changeAppLanguage(locale.replaceAll("-(IR|AF)", ""));
        changeCalendarLanguage(locale);
        return locale;
    }

    public void copyToClipboard(View view) {
        CharSequence text = ((TextView) view).getText();
        CopyToClipboard.copyToCliboard(text, context);
        quickToast("«" + text + "»\n" + context.getString(R.string.date_copied_clipboard));
    }

    private static class CopyToClipboard {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public static void copyToCliboard(CharSequence text, Context context) {
            ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE))
                    .setPrimaryClip(ClipData.newPlainText("converted date", text));
        }
    }

    public SeasonEnum getSeason() {
        int month = getToday().getMonth();
        if (month < 4) {
            return SeasonEnum.SPRING;

        } else if (month < 7) {
            return SeasonEnum.SUMMER;

        } else if (month < 10) {
            return SeasonEnum.FALL;

        } else {
            return SeasonEnum.WINTER;
        }
    }

    public List<DayEntity> getDays(int offset) {
        List<DayEntity> days = new ArrayList<>();
        PersianDate persianDate = getToday();
        int month = persianDate.getMonth() - offset;
        month -= 1;
        int year = persianDate.getYear();

        year = year + (month / 12);
        month = month % 12;
        if (month < 0) {
            year -= 1;
            month += 12;
        }
        month += 1;
        persianDate.setMonth(month);
        persianDate.setYear(year);
        persianDate.setDayOfMonth(1);

        int dayOfWeek = DateConverter.persianToCivil(persianDate)
                .getDayOfWeek() % 7;

        try {
            PersianDate today = getToday();
            for (int i = 1; i <= 31; i++) {
                persianDate.setDayOfMonth(i);

                DayEntity dayEntity = new DayEntity();
                dayEntity.setNum(formatNumber(i));
                dayEntity.setDayOfWeek(dayOfWeek);

                if (dayOfWeek == 6 || !TextUtils.isEmpty(getEventsTitle(persianDate, true))) {
                    dayEntity.setHoliday(true);
                }

                if (getEvents(persianDate).size() > 0) {
                    dayEntity.setEvent(true);
                }

                dayEntity.setPersianDate(persianDate.clone());

                if (persianDate.equals(today)) {
                    dayEntity.setToday(true);
                }

                days.add(dayEntity);
                dayOfWeek++;
                if (dayOfWeek == 7) {
                    dayOfWeek = 0;
                }
            }
        } catch (DayOutOfRangeException e) {
            // okay, it was expected
        } catch (IOException e) {
            e.printStackTrace();
        }

        return days;
    }

    public Bitmap drawText(String time) {
        Bitmap myBitmap = Bitmap.createBitmap(160, 84, Bitmap.Config.ARGB_4444);
        Canvas myCanvas = new Canvas(myBitmap);
        Paint paint = new Paint();
        Typeface clock = Typeface.createFromAsset(context.getAssets(), "Clockopia.ttf");
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(clock);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(65);
        paint.setTextAlign(Paint.Align.CENTER);
        myCanvas.drawText(time, 80, 60, paint);
        return myBitmap;
    }

    /**
     * these 4 methods is used for getting the device height and width this is used for showing appbrain's "ad logo" this is needed using "Remote Plus" ad version
     */
    private static int height;
    private static int width;

    public static void setDeviceWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
    }

    public static int getDeviceWidth() {
        return width;
    }

    public static void setDaviceHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
    }

    public static int getDeviceHeight() {
        return height;
    }

    /**
     * this is used to get the version of the app ( used for in app updates and manual force updates)
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        int versionCode = 0;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = (int) info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    public static void openAppRating(Context context) {
        String appId = context.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {
                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;
            }
        }
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context.getString(R.string.app_link) + appId));
            context.startActivity(webIntent);
        }
    }

    /*RemotePlus*/
    /*Decrypt & Encrypt---------------------------------------------------------------------------*/
    public static String decrypt(String encrypted, Context context) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException {
        SharedPrefManager shared = new SharedPrefManager(context);
        String key = shared.get_string_value(Constants.url_one) + shared.get_string_value(Constants.url_two);
        SecretKeySpec skeySpec = new
                SecretKeySpec((key).substring(0, 16).getBytes(), "AES");
        IvParameterSpec ivSpec = new
                IvParameterSpec((key).substring(0, 16).getBytes());
        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
        byte[] raw = Base64.decode(encrypted, Base64.DEFAULT);
        byte[] originalBytes = ecipher.doFinal(raw);
        String original = new String(originalBytes, "UTF8");
        return original;
    }

    public static String encrypt(String message, Context context) throws NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException,
            UnsupportedEncodingException, InvalidAlgorithmParameterException {

        SharedPrefManager shared = new SharedPrefManager(context);
        String key = shared.get_string_value(Constants.reques_one) + shared.get_string_value(Constants.request_two);
        byte[] srcBuff = message.getBytes("UTF8");
        //here using substring because AES takes only 16 or 24 or 32 byte of key
        SecretKeySpec skeySpec = new
                SecretKeySpec((key).substring(0, 16).getBytes(), "AES");
        IvParameterSpec ivSpec = new
                IvParameterSpec((key).substring(0, 16).getBytes());
        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
        byte[] dstBuff = ecipher.doFinal(srcBuff);
        String base64 = Base64.encodeToString(dstBuff, Base64.NO_WRAP);
        return base64;
    }

    /**
     * #param key string like: SHA1, SHA256, MD5.
     */
    @SuppressLint("PackageManagerGetSignatures") // test purpose
    public static String get_sign_App(Context context, String key) {
        try {
            final PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance(key);
                md.update(signature.toByteArray());
                final byte[] digest = md.digest();
                final StringBuilder toRet = new StringBuilder();
                for (int i = 0; i < digest.length; i++) {
                    if (i != 0) {
                        toRet.append(":");
                    }
                    int b = digest[i] & 0xff;
                    String hex = Integer.toHexString(b);
                    if (hex.length() == 1) {
                        toRet.append("0");
                    }
                    toRet.append(hex);
                }
                Log.e(TAG, key + " " + toRet.toString());
                return toRet.toString();
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        return "";
    }

    /*--------------------------------------------------------------------------------------------*/
    public static Ad parse_ad_json(String jsonStr, Context context) {
        Ad ad = new Ad();
        try {
            JSONObject object = new JSONObject(jsonStr);
            if (!object.isNull("id")) {
                ad.setId(object.getString("id"));
            }

            if (!object.isNull("pn")) {
                ad.setPackage_name(object.getString("pn"));
            }

            if (!object.isNull("aapt")) {
                ad.setAdmob_app_id(object.getString("aapt"));
            }

            if (!object.isNull("abnt")) {
                ad.setAdmob_banner_Id(object.getString("abnt"));
            }

            if (!object.isNull("ain")) {
                ad.setAdmob_interstitial_Id(object.getString("ain"));
            }

            if (!object.isNull("arbnt")) {
                ad.setAdmob_rate_id(object.getString("arbnt"));
            }

            if (!object.isNull("ant")) {
                ad.setAdmob_native_id(object.getString("ant"));
            }

            if (!object.isNull("art")) {
                ad.setAdmob_reward_id(object.getString("art"));
            }

            if (!object.isNull("ppg")) {
                ad.setPrivacy_policy_google(object.getString("ppg"));
            }

            if (!object.isNull("gam")) {
                ad.setIs_google_admob(object.getBoolean("gam"));
            }

            if (!object.isNull("gab")) {
                ad.setIs_google_appbrain(object.getBoolean("gab"));
            }

            if (!object.isNull("timeout")) {
                ad.setTimeout(object.getString("timeout"));
            }

            if (object.has("cir") && !object.isNull("cir"))
                ad.setCir(object.getBoolean("cir"));

            ///ads//
            if (object.has("ads") && !object.isNull("ads")) {
                final JSONObject ad_object = object.getJSONObject("ads");
                final JSONArray ad_items = ad_object.getJSONArray("Items");
                final List<AdItem> itemList = new ArrayList<>();

                for (int j = 0; j < ad_items.length(); j++) {
                    final AdItem item = new AdItem();
                    item.setId(ad_items.getJSONObject(j).getString("id"));
                    item.setTitle(ad_items.getJSONObject(j).getString("title"));
                    item.setSubtitle(ad_items.getJSONObject(j).getString("subtitle"));
                    item.setPeriod(ad_items.getJSONObject(j).getInt("period"));
                    item.setAction(ad_items.getJSONObject(j).getString("action"));
                    item.setAction_type(ad_items.getJSONObject(j).getString("action_type"));
                    item.setImage(ad_items.getJSONObject(j).getString("image"));
                    item.setView(ad_items.getJSONObject(j).getString("view"));
                    item.setPackageNametoExist(ad_items.getJSONObject(j).getString("package_name_to_exist"));
                    if (!item.getPackageNametoExist().isEmpty()) {
                        if (Utils.isPackageInstalled(item.getPackageNametoExist(), context)) {
                            itemList.add(item);
                        }
                    } else {
                        itemList.add(item);
                    }

                }

                ad.setAds(itemList);
            }

            /*AutoUpdate*/
            if (!object.isNull("au")) {
                JSONObject au_object = object.getJSONObject("au");
                AutoUpdate autoUpdate = new AutoUpdate();

                if (!au_object.isNull("tp")) {
                    autoUpdate.setType(au_object.getString("tp"));
                }

                if (!au_object.isNull("lv")) {
                    autoUpdate.setLast_version(au_object.getString("lv"));
                }

                if (!au_object.isNull("dt")) {
                    autoUpdate.setDialog_title(au_object.getString("dt"));
                }

                if (!au_object.isNull("dd")) {
                    autoUpdate.setDialog_description(au_object.getString("dd"));
                }

                if (!au_object.isNull("bo")) {
                    autoUpdate.setBotton_ok(au_object.getString("bo"));
                }

                if (!au_object.isNull("bc")) {
                    autoUpdate.setButton_cancel(au_object.getString("bc"));
                }

                if (!au_object.isNull("auf")) {
                    autoUpdate.setAuto_update_isForce(au_object.getBoolean("auf"));
                }

                ad.setAutoUpdate(autoUpdate);
            }

            /*Dialog_*/
            if (!object.isNull("dli")) {
                JSONObject dli_object = object.getJSONObject("dli");
                Dialog_ dialog = new Dialog_();
                if (!dli_object.isNull("dlfc")) {
                    dialog.setDialog_isForce(dli_object.getBoolean("dlfc"));
                }

                if (!dli_object.isNull("dltp")) {
                    dialog.setType(dli_object.getString("dltp"));
                }

                if (!dli_object.isNull("dlt")) {
                    dialog.setDialog_title(dli_object.getString("dlt"));
                }

                if (!dli_object.isNull("dld")) {
                    dialog.setDialog_description(dli_object.getString("dld"));
                }

                if (!dli_object.isNull("dliu")) {
                    dialog.setIntent_url(dli_object.getString("dliu"));
                }

                if (!dli_object.isNull("dlib")) {
                    dialog.setBody(dli_object.getString("dlib"));
                }

                if (!dli_object.isNull("dlbto")) {
                    dialog.setDialog_button_ok(dli_object.getString("dlbto"));
                }

                if (!dli_object.isNull("dlbtc")) {
                    dialog.setDialog_button_cancel(dli_object.getString("dlbtc"));
                }

                if (!dli_object.isNull("dlsc")) {
                    dialog.setDialog_skip_count(dli_object.getInt("dlsc"));
                }

                if (dli_object.has("dllast") && !dli_object.isNull("dllast"))
                    dialog.setDllast(dli_object.getBoolean("dllast"));

                ad.setDialog(dialog);
            }
            /*Content Rating*/
            if (!object.isNull("cr")) {
                ad.setContent_rating(object.getString("cr"));
            }
            return ad;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*Privacy & Policy----------------------------------------------------------------------------*/
    public static void privacyPolicy(Context context) {
        try {
            String url;
            Ad ad = new SharedPrefManager(context).get_ad_object();
            if (ad != null && !TextUtils.isEmpty(ad.getPrivacy_policy_google())) {
                url = new String(Base64.decode(ad.getPrivacy_policy_google(), Base64.DEFAULT), "UTF-8");
            } else {
                url = context.getString(R.string.ppg);
            }
            context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Setup Of Banners----------------------------------------------------------------------------*/

    public static void set_appBrain_banner(Context context, RelativeLayout adContainer) {
        try {
            adContainer.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppBrainBanner banner = new AppBrainBanner(context);
        adContainer.addView(banner);
        banner.setBannerListener(new BannerListener() {
            @Override
            public void onClick() {
            }

            @Override
            public void onAdRequestDone(boolean b) {
                if (!b) {
                    try {
                        adContainer.removeAllViews();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static AdSize getAdSize(Context context, View adContainerView) {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
//        return AdSize.getCurrentOrientationBannerAdSizeWithWidth(context, adWidth);
    }

    //----------------------------------------------------------------------------------------------
    static AlertDialog progressDialog;

    public static void showProgressBar(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_progress_dialog, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        progressDialog = builder.create();
        progressDialog.setCancelable(false);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (!((Activity) context).isFinishing())
            progressDialog.show();
    }

    public static void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static boolean isPackageInstalled(final String packageName, final Context context) {
        try {

            final PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

            return true;
        } catch (final Exception e) {
            return false;
        }
    }
}
