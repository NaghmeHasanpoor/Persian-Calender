package com.arpitas.persiancalender.fragment;

import android.app.AlertDialog;

import android.app.Dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.content.Intent;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.arpitas.persiancalender.ApplicationContexts;
import com.arpitas.persiancalender.Constants;

import com.arpitas.persiancalender.R;


import com.arpitas.persiancalender.activity.DialogActivity;
import com.arpitas.persiancalender.activity.MainActivity;
import com.arpitas.persiancalender.activity.MyEventsSqlite;
import com.arpitas.persiancalender.adapter.CalendarAdapter;
import com.arpitas.persiancalender.entity.CityEntity;
import com.arpitas.persiancalender.praytimes.Clock;
import com.arpitas.persiancalender.praytimes.Coordinate;
import com.arpitas.persiancalender.praytimes.PrayTime;
import com.arpitas.persiancalender.praytimes.PrayTimesCalculator;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.arpitas.persiancalender.calendar.CivilDate;
import com.arpitas.persiancalender.calendar.DateConverter;
import com.arpitas.persiancalender.calendar.PersianDate;
import com.arpitas.persiancalender.util.LanguageUtil;
import com.arpitas.persiancalender.util.SharedPrefManager;
import com.arpitas.persiancalender.util.Utils;


public class CalendarFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    public static int viewPagerPosition;
    public static ViewPager monthViewPager;
    private Utils utils;
    private Calendar calendar = Calendar.getInstance();
    private Coordinate coordinate;
    public static ListView list;
    private PrayTimesCalculator prayTimesCalculator;
    private TextView imsakTextView;
    private TextView dhuhrTextView;
    private TextView asrTextView;
    private TextView maghribTextView;
    private TextView ishaTextView;
    private TextView sunriseTextView;
    private TextView sunsetTextView;
    private TextView midnightTextView;
    public static TextView weekDayName;
    private TextView georgianDate;
    private TextView islamicDate;
    public static TextView shamsiDate;
    private TextView eventTitle;
    private TextView holidayTitle;
    private ImageView addbtn, dot, dot1;
    private int onCreate = 0;
    public static ArrayAdapter adapter;
    ApplicationPreferenceFragment d;
    ImageView place;
    private RelativeLayout owghat;
    private RelativeLayout event;
    private RelativeLayout Myevent;
    private TextView btn;
    public static TextView mah;
    ImageView pre, next;
    private PersianDate persianDate;
    private int offset;
    private IntentFilter filter;
    private BroadcastReceiver receiver;
    public SharedPreferences sp;
    public static CalendarFragment calendarFragment;
    public static SharedPreferences.Editor e;
    boolean isFirstStart;
    public static SharedPreferences getPrefs;
    private boolean isEnglish;

    public CalendarFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPrefManager shared = new SharedPrefManager(getContext());
        if (!shared.getLanguage().equals(""))
            LanguageUtil.changeLanguage(getContext(), shared.getLanguage());

        /*----------------------------------------------------------------------------------------*/
        isEnglish = new SharedPrefManager(getContext()).getLanguage().equalsIgnoreCase(SharedPrefManager.EN);
        sp = getContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
        calendarFragment = this;
        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        btn = view.findViewById(R.id.txt);
        btn.setTypeface(ApplicationContexts.typeface2);
        mah = view.findViewById(R.id.mah);
        mah.setTypeface(ApplicationContexts.typeface3);
        next =  view.findViewById(R.id.next);
        pre = view.findViewById(R.id.prev);
        utils = Utils.getInstance(getContext());
        viewPagerPosition = 0;
        addbtn = view.findViewById(R.id.adding);

        place = view.findViewById(R.id.place);
        place.setOnClickListener(v -> {
            try {
                MainActivity.mainActivity.selectItem(MainActivity.PREFERENCE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        list = view.findViewById(R.id.list);
        list.setClickable(true);

        list.setOnItemClickListener((parent, v, position, id) -> {
            PopupMenu popup = new PopupMenu(getActivity(), v);
            popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                MyEventsSqlite.Mahsul stud = null;
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getTitle().equals(getResources().getString(R.string.delete))) {
                        if (list.getAdapter().getCount() > 0) {
                            stud = (MyEventsSqlite.Mahsul) list.getAdapter().getItem(position);
                            final MyEventsSqlite db;
                            db = new MyEventsSqlite(getContext());
                            db.open();
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage(" '" + getResources().getString(R.string.are_you_sure_del) + list.getItemAtPosition(position).toString() + getResources().getString(R.string.done_delete)+ "' ");
                            alert.setPositiveButton(getResources().getString(R.string.ok), (dialog, which) -> {
                                db.deleteMAhsul(list.getItemAtPosition(position).toString());
                                adapter.remove(stud);
                                if (list.getCount() == 0)
                                    Myevent.setVisibility(View.GONE);

                            });
                            alert.setNegativeButton(getResources().getString(R.string.cancel_update), (dialog, which) -> {});
                            alert.show();
                            adapter.notifyDataSetChanged();
                            if (list.getAdapter().getCount() == 0)
                                Myevent.setVisibility(View.GONE);
                            int count = list.getCount();
                            setListViewHeightBasedOnChildren(list);
                        } else {
                            Myevent.setVisibility(view.GONE);
                        }
                    }
                    return true;
                }
            });

            popup.show();

        });
        getActivity().openContextMenu(list);

        georgianDate =  view.findViewById(R.id.georgian_date);
        utils.setFont(georgianDate);
        islamicDate = view.findViewById(R.id.islamic_date);
        utils.setFont(islamicDate);
        shamsiDate = view.findViewById(R.id.shamsi_date);
        utils.setFont(shamsiDate);
        weekDayName =  view.findViewById(R.id.week_day_name);
        TextView event_text =  view.findViewById(R.id.event_text);
        event_text.setTypeface(ApplicationContexts.typeface2);
        imsakTextView = view.findViewById(R.id.imsak);
        utils.setFont(imsakTextView);
        utils.setFontAndShape(view.findViewById(R.id.imsakText));
        dhuhrTextView = view.findViewById(R.id.dhuhr);
        utils.setFont(dhuhrTextView);
        utils.setFontAndShape(view.findViewById(R.id.dhuhrText));
        asrTextView = view.findViewById(R.id.asr);
        utils.setFont(asrTextView);
        utils.setFontAndShape(view.findViewById(R.id.asrText));
        maghribTextView = view.findViewById(R.id.maghrib);
        utils.setFont(maghribTextView);
        utils.setFontAndShape(view.findViewById(R.id.maghribText));

        ishaTextView = view.findViewById(R.id.isgha);
        utils.setFont(ishaTextView);
        utils.setFontAndShape(view.findViewById(R.id.ishaText));

        sunriseTextView = view.findViewById(R.id.sunrise);
        utils.setFont(sunriseTextView);
        utils.setFontAndShape(view.findViewById(R.id.sunriseText));

        sunsetTextView = view.findViewById(R.id.sunset);
        utils.setFont(sunsetTextView);
        utils.setFontAndShape(view.findViewById(R.id.sunsetText));

        midnightTextView = view.findViewById(R.id.midnight);
        utils.setFont(midnightTextView);
        utils.setFontAndShape(view.findViewById(R.id.midnightText));
        btn = view.findViewById(R.id.txt);
        btn.setTypeface(ApplicationContexts.typeface2);
        btn.setText(" " + getString(R.string.in_city_time));

        eventTitle = view.findViewById(R.id.event_title);
        utils.setFont(eventTitle);
        holidayTitle =  view.findViewById(R.id.holiday_title);
        dot = view.findViewById(R.id.dot);
        dot1 = view.findViewById(R.id.dot1);
        utils.setFont(holidayTitle);
        owghat =  view.findViewById(R.id.owghat);
        event =  view.findViewById(R.id.cardEvent);
        Myevent =  view.findViewById(R.id.myEvent);
        monthViewPager = view.findViewById(R.id.calendar_pager);

        coordinate = utils.getCoordinate();
        prayTimesCalculator = new PrayTimesCalculator(utils.getCalculationMethod());

        monthViewPager.setAdapter(new CalendarAdapter(getActivity().getSupportFragmentManager()));
        monthViewPager.setCurrentItem(Constants.MONTHS_LIMIT / 2);

        monthViewPager.addOnPageChangeListener(this);

        owghat.setOnClickListener(this);
        georgianDate.setOnClickListener(this);
        islamicDate.setOnClickListener(this);
        shamsiDate.setOnClickListener(this);

        ((TextView) view.findViewById(R.id.event_card_title)).setTypeface(ApplicationContexts.typeface2);
        ((TextView) view.findViewById(R.id.owghat_text)).setTypeface(ApplicationContexts.typeface2);

        PersianDate today = utils.getToday();
        utils.setActivityTitleAndSubtitle(getActivity(), utils.getMonthName(today),
                utils.formatNumber(today.getYear()));

        String date = utils.shape(utils.dateToString(today));
        String year = date.substring(date.length() - 4, date.length());

        persianDate = utils.getToday();
        int month = persianDate.getMonth();
        month -= 1;
        // int year = persianDate.getYear();

        // year = year + (month / 12);
        month = month % 12;
        if (month < 0) {
            //  year -= 1;
            month += 12;
        }

        month += 1;
        persianDate.setMonth(month);
        persianDate.setDayOfMonth(1);
        calendarFragment = (CalendarFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(Constants.CALENDAR_MAIN_FRAGMENT_TAG);
        if (calendarFragment != null && offset == 0 && CalendarFragment.viewPagerPosition == offset) {
            try {
                calendarFragment.selectDay(utils.getToday());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (offset == 0 && CalendarFragment.viewPagerPosition == offset) {}

        filter = new IntentFilter(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int value = intent.getExtras().getInt(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT);
                if (value == offset) {
                } else if (value == Constants.BROADCAST_TO_MONTH_FRAGMENT_RESET_DAY) {
                }
            }
        };

        pre.setOnClickListener(view1 -> monthViewPager.setCurrentItem(monthViewPager.getCurrentItem() - 1));
        next.setOnClickListener(view12 -> monthViewPager.setCurrentItem(monthViewPager.getCurrentItem() + 1));

        monthViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                sp.edit().putInt("counter", sp.getInt("counter", 0) + 1).apply();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addbtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), DialogActivity.class)));

        return view;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        try {
            ListAdapter listAdapter = listView.getAdapter();
            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalHeight += listItem.getMinimumHeight();
                Log.d("Value", String.valueOf(listItem.getMeasuredHeight()));
            }
            Log.e("samii", "total height: " + totalHeight);
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showEvent(PersianDate persianDate) throws IOException {
        String holidays = utils.getEventsTitle(persianDate, true);
        String events = utils.getEventsTitle(persianDate, false);

        event.setVisibility(View.GONE);
        holidayTitle.setVisibility(View.GONE);
        dot.setVisibility(View.GONE);
        dot1.setVisibility(View.GONE);
        eventTitle.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(holidays)) {
            holidayTitle.setText(utils.shape(holidays));
            holidayTitle.setVisibility(View.VISIBLE);
            event.setVisibility(View.VISIBLE);
            dot.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(events)) {
            eventTitle.setText(utils.shape(events));
            eventTitle.setVisibility(View.VISIBLE);
            dot1.setVisibility(View.VISIBLE);
            event.setVisibility(View.VISIBLE);
        }
    }

    public void setOwghat(CivilDate civilDate) {
        if (coordinate == null) {
            getPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            //  Create a new boolean and preference and set it to true
            isFirstStart = getPrefs.getBoolean("firstStart", true);
            //  If the activity has never started before...
            if (isFirstStart) {
                e = getPrefs.edit();
                e.putBoolean("firstStart", false);
                e.apply();
                dialogAbout();
            }
            return;
        }
        btn.setText((getString(R.string.in_city_time)));
        CityEntity cityEntity = utils.getCityFromPreference();

        if (cityEntity != null) {
            String cityName = isEnglish ? cityEntity.getEn() : cityEntity.getFa();
            btn.setText(" " + getString(R.string.in_city_time) + " " + cityName);
        }

        Coordinate coordinate = utils.getCoordinate();

        calendar.set(civilDate.getYear(), civilDate.getMonth() - 1, civilDate.getDayOfMonth());
        Date date = calendar.getTime();

        Map<PrayTime, Clock> prayTimes = prayTimesCalculator.calculate(date, coordinate);

        imsakTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.FAJR), isEnglish));
        sunriseTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.SUNRISE), isEnglish));
        dhuhrTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.DHUHR), isEnglish));
        asrTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.ASR), isEnglish));
        sunsetTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.SUNSET), isEnglish));
        maghribTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.MAGHRIB), isEnglish));
        ishaTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.ISHA), isEnglish));
        midnightTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.MIDNIGHT), isEnglish));

        owghat.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (coordinate == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.desc_war_show_owghat), Toast.LENGTH_LONG).show();
            return;
        }
        switch (v.getId()) {
            case R.id.islamic_date:
            case R.id.shamsi_date:
            case R.id.georgian_date:
                utils.copyToClipboard(v);
                break;
        }
    }


    private boolean isToday(CivilDate civilDate) {
        CivilDate today = new CivilDate();
        return today.getYear() == civilDate.getYear()
                && today.getMonth() == civilDate.getMonth()
                && today.getDayOfMonth() == civilDate.getDayOfMonth();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        viewPagerPosition = position - Constants.MONTHS_LIMIT / 2;
        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);//todo use fragment tag
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT, position - Constants.MONTHS_LIMIT / 2);
        getContext().sendBroadcast(intent);
        MainActivity.today.setVisibility(View.VISIBLE);
    }

    public void selectDay(PersianDate persianDate) throws IOException {
        try {
            CivilDate civilDate = DateConverter.persianToCivil(persianDate);
            weekDayName.setText(utils.shape(utils.getWeekDayName(persianDate)));
            shamsiDate.setText(utils.shape(utils.dateToString(persianDate)));
            onCreate = 1;
            MyEventsSqlite db;
            db = new MyEventsSqlite(getContext());
            db.open();
            List values = db.getEvent(shamsiDate.getText().toString());

            if (values.toString().length() != 2) {
                Log.d("first enter", "hello");
                adapter = new ArrayAdapter(getActivity(), R.layout.simple_list_item_1, values);
                list.setAdapter(adapter);
                setListViewHeightBasedOnChildren(list);
                Myevent.setVisibility(View.VISIBLE);

            } else {
                Myevent.setVisibility(View.GONE);
            }
            list.setClickable(true);

            georgianDate.setText(utils.shape(utils.dateToString(civilDate)));
            islamicDate.setText(utils.shape(utils.dateToString(
                    DateConverter.civilToIslamic(civilDate, utils.getIslamicOffset()))));

            if (isToday(civilDate)) {
                MainActivity.today.setVisibility(View.GONE);
            } else {
                MainActivity.today.setVisibility(View.VISIBLE);
            }

            setOwghat(civilDate);
            showEvent(persianDate);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void check_visibility_myEvent(){
        if (Myevent.getVisibility() != View.VISIBLE)
            Myevent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    protected void dialogAbout() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about);
        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        final Button button_ok = dialog.findViewById(R.id.button_ok);

        button_ok.setOnClickListener(v -> {
            try {
                MainActivity.mainActivity.selectItem(MainActivity.PREFERENCE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}