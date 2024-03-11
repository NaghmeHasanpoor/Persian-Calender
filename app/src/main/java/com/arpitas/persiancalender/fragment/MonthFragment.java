package com.arpitas.persiancalender.fragment;

import static android.content.Context.RECEIVER_EXPORTED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.arpitas.persiancalender.Constants;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.adapter.MonthAdapter;
import com.arpitas.persiancalender.entity.DayEntity;
import com.arpitas.persiancalender.util.LanguageUtil;
import com.arpitas.persiancalender.util.SharedPrefManager;
import com.arpitas.persiancalender.util.Utils;
import com.arpitas.persiancalender.activity.MainActivity;

import java.io.IOException;
import java.util.List;

import com.arpitas.persiancalender.calendar.PersianDate;

public class MonthFragment extends Fragment {
    public Utils utils;
    private CalendarFragment calendarFragment;
    private PersianDate persianDate;
    private int offset;
    private IntentFilter filter;
    private BroadcastReceiver receiver;
    private MonthAdapter adapter;
    private boolean isEnglish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPrefManager shared = new SharedPrefManager(getContext());
        if (!shared.getLanguage().equals(""))
            LanguageUtil.changeLanguage(getContext(), shared.getLanguage());

        /*----------------------------------------------------------------------------------------*/
        utils = Utils.getInstance(getContext());
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        offset = getArguments().getInt(Constants.OFFSET_ARGUMENT);
        List<DayEntity> days = utils.getDays(offset);

        isEnglish = shared.getLanguage().toLowerCase().equalsIgnoreCase(SharedPrefManager.EN);
        persianDate = utils.getToday();
        int month = persianDate.getMonth() - offset;
        month -= 1;
        int year = persianDate.getYear();

        year = year + (month / 12);
        month = month % 12;
        if (month < 0) {
            year -= 1;
            month += 12;
        }

        month += 1;
        persianDate.setMonth(month);
        persianDate.setYear(year);
        persianDate.setDayOfMonth(1);

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        adapter = new MonthAdapter(getContext(), this, days);
        recyclerView.setAdapter(adapter);
        updateTitle1();
        calendarFragment = (CalendarFragment) getActivity().getSupportFragmentManager().findFragmentByTag(Constants.CALENDAR_MAIN_FRAGMENT_TAG);
        if (calendarFragment != null && offset == 0 && CalendarFragment.viewPagerPosition == offset) {
            try {
                calendarFragment.selectDay(utils.getToday());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (offset == 0 && CalendarFragment.viewPagerPosition == offset) {
            updateTitle();
        }

        filter = new IntentFilter(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int value = intent.getExtras().getInt(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT);
                if (value == offset) {
                    updateTitle();
                } else if (value == Constants.BROADCAST_TO_MONTH_FRAGMENT_RESET_DAY) {
                    resetSelectDay();
                }
            }
        };
        return view;
    }

    public void onClickItem(PersianDate day) throws IOException {
        calendarFragment.selectDay(day);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().registerReceiver(receiver, filter, RECEIVER_EXPORTED);
        }else{
            requireContext().registerReceiver(receiver, filter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(receiver);
    }

    private void updateTitle() {
        try {
            utils.setActivityTitleAndSubtitle(
                    getActivity(),
                    utils.getMonthName(persianDate), isEnglish ? Utils.faToEn(String.valueOf(persianDate.getYear())) : Utils.enToFa(String.valueOf(persianDate.getYear()))/*utils.formatNumber(persianDate.getYear()*/);
            ((MainActivity)getActivity()).text().setText(utils.getMonthName(persianDate) + " " + (isEnglish ? Utils.faToEn(String.valueOf(persianDate.getYear())) : Utils.enToFa(String.valueOf(persianDate.getYear()))/*utils.formatNumber(persianDate.getYear())*/));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTitle1() {
        utils.setActivityTitleAndSubtitle(
                getActivity(),
                utils.getMonthName(persianDate), (isEnglish ? Utils.faToEn(String.valueOf(persianDate.getYear())) : Utils.enToFa(String.valueOf(persianDate.getYear()))/*utils.formatNumber(persianDate.getYear())*/));
    }

    private void resetSelectDay() {
        if (adapter.select_Day != -1) {
            adapter.select_Day = -1;
            adapter.notifyDataSetChanged();
        }
    }

}
