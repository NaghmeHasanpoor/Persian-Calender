package com.arpitas.persiancalender.preferences;

import android.content.Context;

import android.util.AttributeSet;

import androidx.preference.DialogPreference;
import androidx.preference.PreferenceViewHolder;

import com.arpitas.persiancalender.util.Utils;
import com.arpitas.persiancalender.fragment.ApplicationPreferenceFragment;

public class LocationPreference extends DialogPreference {

    public LocationPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        Utils.getInstance(getContext()).setFontAndShape(holder);
    }

    public void setSelected(String selected) {
        final boolean wasBlocking = shouldDisableDependents();
        persistString(selected);
        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) notifyDependencyChange(isBlocking);
        ApplicationPreferenceFragment.update();
    }
}
