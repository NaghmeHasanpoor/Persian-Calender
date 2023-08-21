package com.arpitas.persiancalender.entity;

import java.io.Serializable;

public class Dialog_ implements Serializable {
    private boolean dialog_isForce = false;

    private String type = "";

    private String dialog_title = "", dialog_description = "", intent_url = "", body = ""
            , dialog_button_ok = "", dialog_button_cancel = "";

    private int dialog_skip_count = 0;

    public Dialog_(){}

    public boolean isDialog_isForce() {
        return dialog_isForce;
    }

    public void setDialog_isForce(boolean dialog_isForce) {
        this.dialog_isForce = dialog_isForce;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getIntent_url() {
        return intent_url;
    }

    public void setIntent_url(String intent_url) {
        this.intent_url = intent_url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDialog_button_ok() {
        return dialog_button_ok;
    }

    public void setDialog_button_ok(String dialog_button_ok) {
        this.dialog_button_ok = dialog_button_ok;
    }

    public String getDialog_button_cancel() {
        return dialog_button_cancel;
    }

    public void setDialog_button_cancel(String dialog_button_cancel) {
        this.dialog_button_cancel = dialog_button_cancel;
    }

    public int getDialog_skip_count() {
        return dialog_skip_count;
    }

    public void setDialog_skip_count(int dialog_skip_count) {
        this.dialog_skip_count = dialog_skip_count;
    }
}
