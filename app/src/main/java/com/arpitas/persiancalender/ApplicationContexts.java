package com.arpitas.persiancalender;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

public class ApplicationContexts extends Application {
    public static ApplicationContexts GlobalContext = null;
    public static Typeface typeface1, typeface2,typeface3, typeface_enNumber;
    public static SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalContext = this;
        typeface1 = Typeface.createFromAsset(getAssets(), "Sahel.ttf");
        typeface2 = Typeface.createFromAsset(getAssets(), "Sahel-Bold.ttf");
        typeface3 = Typeface.createFromAsset(getAssets(), "Sahel-Black.ttf");
        typeface_enNumber = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

}
