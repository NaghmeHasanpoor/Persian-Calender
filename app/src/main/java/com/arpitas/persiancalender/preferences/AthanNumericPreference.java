package com.arpitas.persiancalender.preferences;

import android.content.Context;

import android.util.AttributeSet;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceViewHolder;

import com.arpitas.persiancalender.util.Utils;
import com.arpitas.persiancalender.fragment.ApplicationPreferenceFragment;

public class AthanNumericPreference extends EditTextPreference {

    public AthanNumericPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AthanNumericPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AthanNumericPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AthanNumericPreference(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        Utils.getInstance(getContext()).setFontAndShape(holder);
    }

    private Double mDouble;

    @Override
    public void setText(String text) {
        final boolean wasBlocking = shouldDisableDependents();
        mDouble = parseDouble(text);
        persistString(mDouble != null ? mDouble.toString() : null);
        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) notifyDependencyChange(isBlocking);
        ApplicationPreferenceFragment.update();
    }

    @Override
    public String getText() {
        return mDouble != null ? mDouble.toString() : null;
    }

    private Double parseDouble(String text) {
        try { return Double.parseDouble(text); }
        catch (NumberFormatException | NullPointerException e) { return null; }
    }
}
