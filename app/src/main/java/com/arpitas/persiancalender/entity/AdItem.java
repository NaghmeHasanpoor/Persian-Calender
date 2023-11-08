package com.arpitas.persiancalender.entity;

import java.io.Serializable;

public class AdItem implements Serializable  {
    private String id;
    private String title;
    private int period;
    private String subtitle;
    private String image;
    private String action;
    private String action_type;
    private String view;
    private String package_name_to_exist;

    public AdItem(final String id, final String title, final int period, final String subtitle, final String image,
                  final String action, final String action_type, final String view, final String package_name_to_exist){

        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.period = period;
        this.image = image;
        this.action = action;
        this.action_type = action_type;
        this.view = view;
        this.package_name_to_exist = package_name_to_exist;
    }

    public AdItem() {

    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
    }

    public int getPeriod() {
        return this.period;
    }

    public void setPeriod(final int period) {
        this.period = period;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public String getAction_type() {
        return this.action_type;
    }

    public void setAction_type(final String action_type) {
        this.action_type = action_type;
    }

    public String getView() {
        return this.view;
    }

    public void setView(final String view) {
        this.view = view;
    }

    public String getPackageNametoExist() {
        return this.package_name_to_exist;
    }

    public void setPackageNametoExist(final String package_name_to_exist) {
        this.package_name_to_exist = package_name_to_exist;
    }
}
