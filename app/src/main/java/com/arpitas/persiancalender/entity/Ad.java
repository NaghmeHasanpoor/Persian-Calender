package com.arpitas.persiancalender.entity;

import java.io.Serializable;

public class Ad implements Serializable  {
    private String id = "", package_name = "", privacy_policy_google = "";

    private String  admob_app_id = "", admob_banner_Id = "", admob_interstitial_Id = ""
            , admob_rate_id = "", admob_native_id = "", admob_reward_id = "", timeout, appOpenId;

    private boolean is_google_admob = false, is_google_appbrain = false;

    private AutoUpdate autoUpdate = new AutoUpdate();

    private Dialog_ dialog = new Dialog_();

    private String content_rating = "";

    public Ad(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getAdmob_app_id() {
        return admob_app_id;
    }

    public void setAdmob_app_id(String admob_app_id) {
        this.admob_app_id = admob_app_id;
    }

    public String getAdmob_banner_Id() {
        return admob_banner_Id;
    }

    public void setAdmob_banner_Id(String admob_banner_Id) {
        this.admob_banner_Id = admob_banner_Id;
    }

    public String getAdmob_interstitial_Id() {
        return admob_interstitial_Id;
    }

    public void setAdmob_interstitial_Id(String admob_interstitial_Id) {
        this.admob_interstitial_Id = admob_interstitial_Id;
    }

    public String getAdmob_rate_id() {
        return admob_rate_id;
    }

    public void setAdmob_rate_id(String admob_rate_id) {
        this.admob_rate_id = admob_rate_id;
    }

    public String getAdmob_native_id() {
        return admob_native_id;
    }

    public void setAdmob_native_id(String admob_native_id) {
        this.admob_native_id = admob_native_id;
    }

    public String getAdmob_reward_id() {
        return admob_reward_id;
    }

    public void setAdmob_reward_id(String admob_reward_id) {
        this.admob_reward_id = admob_reward_id;
    }

    public String getPrivacy_policy_google() {
        return privacy_policy_google;
    }

    public void setPrivacy_policy_google(String privacy_policy_google) {
        this.privacy_policy_google = privacy_policy_google;
    }

    public boolean isIs_google_admob() {
        return is_google_admob;
    }

    public void setIs_google_admob(boolean is_google_admob) {
        this.is_google_admob = is_google_admob;
    }

    public boolean isIs_google_appbrain() {
        return is_google_appbrain;
    }

    public void setIs_google_appbrain(boolean is_google_appbrain) {
        this.is_google_appbrain = is_google_appbrain;
    }

    public AutoUpdate getAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(AutoUpdate autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public Dialog_ getDialog() {
        return dialog;
    }

    public void setDialog(Dialog_ dialog) {
        this.dialog = dialog;
    }

    public String getContent_rating() {
        return content_rating;
    }

    public void setContent_rating(String content_rating) {
        this.content_rating = content_rating;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getAppOpenId() {
        return appOpenId;
    }

    public void setAppOpenId(String appOpenId) {
        this.appOpenId = appOpenId;
    }
}
