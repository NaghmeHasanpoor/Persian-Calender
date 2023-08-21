package com.arpitas.persiancalender.preferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arpitas.persiancalender.R;

public class LocationPreferenceDialog extends PreferenceDialogFragmentCompat {

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.preference_location, (ViewGroup) getView(), false);
        RecyclerView recyclerView =  view.findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new LocationAdapter(this));
        builder.setPositiveButton("", null);
        builder.setNegativeButton("", null);
        builder.setView(view);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {}

    public void selectItem(String city) {
        ((LocationPreference)getPreference()).setSelected(city);
        dismiss();
    }
}
