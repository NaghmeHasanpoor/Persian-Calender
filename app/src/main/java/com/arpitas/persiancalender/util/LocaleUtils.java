package com.arpitas.persiancalender.util;

import android.content.Context;
import android.util.Log;

import com.arpitas.persiancalender.calendar.LocaleData;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


import static com.arpitas.persiancalender.Constants.ASR;
import static com.arpitas.persiancalender.Constants.DAY;
import static com.arpitas.persiancalender.Constants.DHUHR;
import static com.arpitas.persiancalender.Constants.EQUALS_WITH;
import static com.arpitas.persiancalender.Constants.GEORGIAN;
import static com.arpitas.persiancalender.Constants.HIJRI_QAMARI;
import static com.arpitas.persiancalender.Constants.HIJRI_SHAMSI;
import static com.arpitas.persiancalender.Constants.IMSAK;
import static com.arpitas.persiancalender.Constants.ISHA;
import static com.arpitas.persiancalender.Constants.MAGHRIB;
import static com.arpitas.persiancalender.Constants.MIDNIGHT;
import static com.arpitas.persiancalender.Constants.MONTH;
import static com.arpitas.persiancalender.Constants.SUNRISE;
import static com.arpitas.persiancalender.Constants.SUNSET;
import static com.arpitas.persiancalender.Constants.TODAY;
import static com.arpitas.persiancalender.Constants.YEAR;

public class LocaleUtils {
    private static final String TAG = LocaleUtils.class.getName();
    private static LocaleUtils instance;
    private static final String CALENDAR_BUNDLE = "Data";
    private Context context;
    private Map<String, String> cache = new HashMap<>();

    private LocaleUtils(Context context, String localeCode) {
        this.context = context;
    }

    public static LocaleUtils getInstance(Context context, String code) {
        if (instance == null) {
            instance = new LocaleUtils(context, code);
        }
        return instance;
    }

    public void changeLocale(String localeCode) {
        if (localeCode.equals("fa-AF")) {
            localeCode = "prs";
        }
        try {
            InputStream pis = context.getAssets().open( CALENDAR_BUNDLE  + ".properties");
            ResourceBundle bundle = new PropertyResourceBundle(pis);

            // cache strings for later use
            for (LocaleData.PersianMonthNames name : LocaleData.PersianMonthNames.values()) {
                String stringName = name.toString();
                cache.put(stringName, getUTF8(bundle.getString(stringName)));
            }

            for (LocaleData.CivilMonthNames name : LocaleData.CivilMonthNames.values()) {
                String stringName = name.toString();
                cache.put(stringName, getUTF8(bundle.getString(stringName)));
            }

            for (LocaleData.IslamicMonthNames name : LocaleData.IslamicMonthNames.values()) {
                String stringName = name.toString();
                cache.put(stringName, getUTF8(bundle.getString(stringName)));
            }

            for (LocaleData.WeekDayNames name : LocaleData.WeekDayNames.values()) {
                String stringName = name.toString();
                cache.put(stringName, getUTF8(bundle.getString(stringName)));
            }

            cache.put(IMSAK, getUTF8(bundle.getString(IMSAK)));
            cache.put(SUNRISE, getUTF8(bundle.getString(SUNRISE)));
            cache.put(DHUHR, getUTF8(bundle.getString(DHUHR)));
            cache.put(ASR, getUTF8(bundle.getString(ASR)));
            cache.put(SUNSET, getUTF8(bundle.getString(SUNSET)));
            cache.put(MAGHRIB, getUTF8(bundle.getString(MAGHRIB)));
            cache.put(ISHA, getUTF8(bundle.getString(ISHA)));
            cache.put(MIDNIGHT, getUTF8(bundle.getString(MIDNIGHT)));
            cache.put(TODAY, getUTF8(bundle.getString(TODAY)));
            cache.put(EQUALS_WITH, getUTF8(bundle.getString(EQUALS_WITH)));
            cache.put(DAY, getUTF8(bundle.getString(DAY)));
            cache.put(MONTH, getUTF8(bundle.getString(MONTH)));
            cache.put(YEAR, getUTF8(bundle.getString(YEAR)));
            cache.put(HIJRI_SHAMSI, getUTF8(bundle.getString(HIJRI_SHAMSI)));
            cache.put(HIJRI_QAMARI, getUTF8(bundle.getString(HIJRI_QAMARI)));
            cache.put(GEORGIAN, getUTF8(bundle.getString(GEORGIAN)));
        } catch (IOException e) {
            Log.e(TAG, "COULDN'T LOAD PROPERTIES FILES", e);
        }
    }

    public String getString(String key) {
        return cache.get(key);
    }

    private String getUTF8(String input) {
        String output = "";
        try {
            output = new String(input.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "couldn't convert string to utf-8: " + input, e);
        }
        return output;
    }
}
