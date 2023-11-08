package com.arpitas.persiancalender.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.arpitas.persiancalender.Constants;
import com.arpitas.persiancalender.entity.Ad;

/**
 * Created by admin on 5/18/2018.
 */

public class SharedPrefManager {
    public SharedPreferences pref;
    public Editor editor;
    public Context mContext;
    public int PRIVATE_MODE = 0;
    private static String Pref_Name = "SharedPref";
    private static final String key_state_click_ratingBar = "StatClickRatingBar";
    private static final String key_counter_show_ratingBar = "counterShowRatingBar";
    public static final String SHOW_APP_BRAIN_OR_PANDORA_2 = "ShowAppBrainOrPandora2";
    public static final String index_for_display_inter = "indexForDisplayWhichInterstitialAd";
    private static final String KEY_SHOW_INTER_BEFORE = "showInterBefore";
    private static final String KEY_LANGUAGE = "language";
    private static final String key_show_dialog_lang = "showDialogLang";
    public static final String FA = "fa";
    public static final String EN = "en";

    public SharedPrefManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(Pref_Name, PRIVATE_MODE);
        editor = pref.edit();
    }

    /*--------------------------------------------------------------------------------------------*/
    public String get_string_value(String key){
        return pref.getString(key, "");
    }

    public void set_string_value(String key, String value){
        editor.putString(key, value);
        editor.apply();
    }

    public void set_int_value(String key, int value){
        editor.putInt(key, value);
        editor.apply();
    }

    public int get_int_value(String key){
        return pref.getInt(key, 0);
    }
    /*--------------------------------------------------------------------------------------------*/

    public void set_boolean_value(String key, boolean value){
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean get_boolean_value(String key){
        return pref.getBoolean(key, false);
    }

    /*--------------------------------------------------------------------------------------------*/
    public void set_ad_object(Ad ad){
        editor.putString(Constants.key_ad_object, new Gson().toJson(ad));
        editor.apply();
    }

    public Ad get_ad_object(){
        return new Gson().fromJson(pref.getString(Constants.key_ad_object, ""), Ad.class);
    }

    /*--------------------------------------------------------------------------------------------*/
    public void setStatClickRatingBar(boolean state) {
        editor.putBoolean(key_state_click_ratingBar, state);
        editor.apply();
    }

    public boolean isRatingBarClick() {
        return pref.getBoolean(key_state_click_ratingBar, false);
    }

    /*--------------------------------------------------------------------------------------------*/
    public void setCounterShowRatingBar(int count) {
        editor.putInt(key_counter_show_ratingBar, count);
        editor.apply();
    }

    public int getCounterShowRatinfBar() {
        return pref.getInt(key_counter_show_ratingBar, 2);
    }

    /*--------------------------------------------------------------------------------------------*/
    public boolean isShowDialogLang() {
        return pref.getBoolean(key_show_dialog_lang, false);
    }

    public void setStateShowDialogLang(boolean state){
        editor.putBoolean(key_show_dialog_lang, state);
        editor.apply();
    }

    /*--------------------------------------------------------------------------------------------*/
    public String getLanguage(){
        return pref.getString(KEY_LANGUAGE,"");
    }

    public void setLanguage(String language){
        editor.putString(KEY_LANGUAGE,language).apply();
    }

    //----------------------------------------------------------------------------------------------
    public void setStateShowInterBefore(boolean state){
        editor.putBoolean(KEY_SHOW_INTER_BEFORE, state).apply();
    }
    public boolean isShowInterBefore(){
        return pref.getBoolean(KEY_SHOW_INTER_BEFORE, false);
    }

    ///---------------------------------------------------------------------------------------------
    public void setInterIndex(int index) {
        final int listSize = this.get_ad_object().getAds().size();
        if (index == listSize)
            index = 0;
        this.editor.putInt(index_for_display_inter, index).apply();
    }

    public int getInterIndex() {
        return this.pref.getInt(index_for_display_inter, 0);
    }



}
