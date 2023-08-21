package com.arpitas.persiancalender.preferences;

import android.content.Context;

import android.util.AttributeSet;

import androidx.preference.DialogPreference;
import androidx.preference.PreferenceViewHolder;

import com.arpitas.persiancalender.util.Utils;
import com.arpitas.persiancalender.R;

public class AthanVolumePreference extends DialogPreference {
    private Utils utils;

    public AthanVolumePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        utils = Utils.getInstance(context);
        setDialogLayoutResource(R.layout.preference_volume);
        setDialogIcon(null);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        utils.setFontAndShape(holder);
    }

    public void setVolume(int volume) {
        final boolean wasBlocking = shouldDisableDependents();
        persistInt(volume);
        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) notifyDependencyChange(isBlocking);
    }

    public int getVolume() {
        return getPersistedInt(1);
    }
}
