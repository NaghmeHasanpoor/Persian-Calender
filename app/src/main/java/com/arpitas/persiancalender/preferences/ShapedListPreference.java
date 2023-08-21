package com.arpitas.persiancalender.preferences;

import android.content.Context;

import android.util.AttributeSet;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceViewHolder;

import com.arpitas.persiancalender.util.Utils;

public class ShapedListPreference extends ListPreference {

   public ShapedListPreference(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   public ShapedListPreference(Context context) {
      super(context);
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
      if (isBlocking != wasBlocking) {
         notifyDependencyChange(isBlocking);
      }
   }

   String defaultValue = "";

   @Override
   protected void onSetInitialValue(Object defaultValue) {
      super.onSetInitialValue(defaultValue);
      this.defaultValue = (String) defaultValue;
   }

   public String getSelected() {
      return getPersistedString(defaultValue);
   }
}
