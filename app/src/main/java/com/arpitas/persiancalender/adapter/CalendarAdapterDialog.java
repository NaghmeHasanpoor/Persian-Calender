package com.arpitas.persiancalender.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.arpitas.persiancalender.Constants;
import com.arpitas.persiancalender.fragment.MonthFragmentDialog;

public class CalendarAdapterDialog extends FragmentStatePagerAdapter {

    public CalendarAdapterDialog(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        MonthFragmentDialog fragment = new MonthFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.OFFSET_ARGUMENT, position - Constants.MONTHS_LIMIT / 2);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return Constants.MONTHS_LIMIT;
    }
}
