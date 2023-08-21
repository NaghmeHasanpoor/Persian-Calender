package com.arpitas.persiancalender.entity;

import java.io.Serializable;

public class AutoUpdate implements Serializable {
    private String type = "", last_version = "";

    private String dialog_title = "", dialog_description = "", botton_ok = "", button_cancel = "";

    private boolean auto_update_isForce = false;

    public AutoUpdate(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLast_version() {
        return last_version;
    }

    public void setLast_version(String last_version) {
        this.last_version = last_version;
    }

    public String getDialog_title() {
        return dialog_title;
    }

    public void setDialog_title(String dialog_title) {
        this.dialog_title = dialog_title;
    }

    public String getDialog_description() {
        return dialog_description;
    }

    public void setDialog_description(String dialog_description) {
        this.dialog_description = dialog_description;
    }

    public String getBotton_ok() {
        return botton_ok;
    }

    public void setBotton_ok(String botton_ok) {
        this.botton_ok = botton_ok;
    }

    public String getButton_cancel() {
        return button_cancel;
    }

    public void setButton_cancel(String button_cancel) {
        this.button_cancel = button_cancel;
    }

    public boolean isAuto_update_isForce() {
        return auto_update_isForce;
    }

    public void setAuto_update_isForce(boolean auto_update_isForce) {
        this.auto_update_isForce = auto_update_isForce;
    }
}
