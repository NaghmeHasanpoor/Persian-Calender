package com.arpitas.persiancalender.preferences;

import android.content.Context;

import android.util.AttributeSet;

import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

import com.arpitas.persiancalender.util.Utils;

public class ShapedPreferenceCategory extends PreferenceCategory {

    public ShapedPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ShapedPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ShapedPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShapedPreferenceCategory(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        Utils.getInstance(getContext()).setFontAndShape(holder);
    }
}
