package com.arpitas.persiancalender.preferences;

import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.arpitas.persiancalender.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.arpitas.persiancalender.ApplicationContexts;

import com.arpitas.persiancalender.fragment.ApplicationPreferenceFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class PrayerSelectDialog extends PreferenceDialogFragmentCompat {
    Set<String> prayers;
    ArrayList<String> azan = new ArrayList<>();

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        PrayerSelectPreference prayerspref = (PrayerSelectPreference) getPreference();

        final CharSequence[] entries = getResources().getStringArray(R.array.prayerTimeNames);
        final CharSequence[] entriesKeys = getResources().getStringArray(R.array.prayerTimeKeys);

        prayers = prayerspref.getPrayers();
        builder.setNegativeButton(getResources().getString(R.string.cancel_update), (dialog, which) -> Objects.requireNonNull(getDialog()).dismiss());
        builder.setPositiveButton(getResources().getString(R.string.ok), (dialog, which) -> {
            SharedPreferences.Editor editor = ApplicationContexts.prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(azan);
            editor.putString("azan", json);
            editor.apply();
            ((PrayerSelectPreference) getPreference()).setPrayers(prayers);
            ApplicationPreferenceFragment.update();
        });

        boolean[] checked = new boolean[entriesKeys.length];
        for (int i = 0; i < entriesKeys.length; ++i) {
            checked[i] = prayers.contains(entriesKeys[i]);
        }

        Gson gson1 = new Gson();
        String json1 = ApplicationContexts.prefs.getString("azan", null);
        Type type1 = new TypeToken<ArrayList<String>>() {
        }.getType();
        if (gson1.fromJson(json1, type1) != null) {
            azan = gson1.fromJson(json1, type1);
        }

        builder.setMultiChoiceItems(entries, checked, (dialog, which, isChecked) -> {
            if (isChecked) {
                prayers.add(entriesKeys[which].toString());
                azan.add(entries[which].toString());
            } else {
                prayers.remove(entriesKeys[which].toString());
                azan.remove(entries[which].toString());
            }
        });
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {}
}
