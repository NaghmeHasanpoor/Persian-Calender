package com.arpitas.persiancalender.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class LanguageUtil {

    public static Context changeLanguage(Context context,String language){
        return updateResourcesLegacy(context,language);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Configuration configuration = context.getResources().getConfiguration();
        LocaleList localeList = new LocaleList(locale);
        localeList.setDefault(localeList);
        configuration.setLocales(localeList);
        context.getApplicationContext().getResources().updateConfiguration(configuration, context.getApplicationContext().getResources().getDisplayMetrics());
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
}
