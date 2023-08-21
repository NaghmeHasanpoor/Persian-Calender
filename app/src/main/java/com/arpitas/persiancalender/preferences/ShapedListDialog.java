package com.arpitas.persiancalender.preferences;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.arpitas.persiancalender.ApplicationContexts;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.adapter.ShapedArrayAdapter;
import com.arpitas.persiancalender.fragment.ApplicationPreferenceFragment;

import java.util.Arrays;
import java.util.Objects;

public class ShapedListDialog extends PreferenceDialogFragmentCompat {
    CharSequence selected;
    String algoritm;

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        final ShapedListPreference listPref = (ShapedListPreference)getPreference();

        final CharSequence[] entriesValues = listPref.getEntryValues();
        final CharSequence[] entriesValues1 = listPref.getEntries();
        ShapedArrayAdapter entriesAdapter = new ShapedArrayAdapter(getContext(),
                R.layout.select_dialog_singlechoice_material,
                Arrays.asList(listPref.getEntries()));

        selected = listPref.getSelected();
        int index = Arrays.asList(entriesValues).indexOf(selected);

        builder.setNegativeButton(getResources().getString(R.string.cancel_update), (dialog, which) -> {});
        builder.setSingleChoiceItems(entriesAdapter, index, (dialog, which) -> {
            listPref.setSelected(entriesValues[which].toString());
            algoritm=entriesValues1[which].toString();
            ApplicationContexts.prefs.edit().putString("algoritm",algoritm).apply();
            Objects.requireNonNull(getDialog()).dismiss();
            ApplicationPreferenceFragment.update();
        });
        builder.setPositiveButton("", null);
    }

    @Override
    public void onDialogClosed(boolean b) {}
}
