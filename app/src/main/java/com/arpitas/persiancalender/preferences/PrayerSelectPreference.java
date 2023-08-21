package com.arpitas.persiancalender.preferences;

import android.content.Context;

import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.preference.DialogPreference;
import androidx.preference.PreferenceViewHolder;

import com.arpitas.persiancalender.util.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PrayerSelectPreference extends DialogPreference {
    public PrayerSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPrayers(Set<String> prayers) {
        final boolean wasBlocking = shouldDisableDependents();
        persistString(TextUtils.join(",", prayers));
        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) notifyDependencyChange(isBlocking);
    }

    public Set<String> getPrayers() {
        Set<String> result = new HashSet<>();
        result.addAll(Arrays.asList(TextUtils.split(getPersistedString(""), ",")));
        return result;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        Utils.getInstance(getContext()).setFontAndShape(holder);
    }
}
