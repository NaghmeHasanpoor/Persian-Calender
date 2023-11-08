package com.arpitas.persiancalender.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.arpitas.persiancalender.Constants;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.adapter.CalendarAdapterDialog;
import com.arpitas.persiancalender.calendar.PersianDate;
import com.arpitas.persiancalender.fragment.CalendarFragment;
import com.arpitas.persiancalender.util.LanguageUtil;
import com.arpitas.persiancalender.util.SharedPrefManager;
import com.arpitas.persiancalender.util.Utils;

import java.io.IOException;
import java.util.List;

public class DialogActivity extends BaseActivity {
    public static ViewPager pager;
    EditText usernameInput;
    ImageView pre, next;
    public TextView mah;
    public static TextView date;
    public static String datee;
    public static Utils utils;
    TextView title;
    Button ok;
    public static int viewPagerPosition;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPrefManager shared = new SharedPrefManager(this);
        if (!shared.getLanguage().equals(""))
            LanguageUtil.changeLanguage(this, shared.getLanguage());

        /*----------------------------------------------------------------------------------------*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        utils = Utils.getInstance(this);
        viewPagerPosition = 0;
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_dialog);

        getWindow().setLayout((getDisplaySize(1) * 2) / 3, (getDisplaySize(0) * 8) / 9);


        pager = findViewById(R.id.calendar_pager);
        pager.setAdapter(new CalendarAdapterDialog(this.getSupportFragmentManager()));
        pager.setCurrentItem(CalendarFragment.monthViewPager.getCurrentItem());
        next = findViewById(R.id.next);
        pre = findViewById(R.id.prev);
        mah = findViewById(R.id.mah);

        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        ok = findViewById(R.id.ok);
        usernameInput = findViewById(R.id.txtedit);
        date.setText(CalendarFragment.weekDayName.getText().toString() + " " + CalendarFragment.shamsiDate.getText().toString());
        datee = CalendarFragment.shamsiDate.getText().toString();
        pre.setOnClickListener(view -> pager.setCurrentItem(pager.getCurrentItem() - 1));
        next.setOnClickListener(view -> pager.setCurrentItem(pager.getCurrentItem() + 1));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPagerPosition = position - Constants.MONTHS_LIMIT / 2;
                Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);//todo use fragment tag
                intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT, position - Constants.MONTHS_LIMIT / 2);
                DialogActivity.this.sendBroadcast(intent);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int getDisplaySize(int parametr) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        if (parametr == 0)
            return height;
        else
            return width;
    }

    public TextView text() {
        return mah;
    }

    @SuppressLint("SetTextI18n")
    public static void selectDay1(PersianDate persianDate) throws IOException {
        date.setText(utils.shape(utils.getWeekDayName(persianDate)) + " " + utils.shape(utils.dateToString(persianDate)));
        datee = utils.shape(utils.dateToString(persianDate));
    }

    public void ok(View view) {
        if (usernameInput.getText().toString().trim().length() != 0) {
            shared.set_boolean_value(SharedPrefManager.SHOW_APP_BRAIN_OR_PANDORA_2, true);
            show_interstitial_ad(findViewById(R.id.app_main_layout), Constants.key_ad_event_click_submit_event);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pls_insert_event_name), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onShowInterstitialResult(String key, boolean result) {
        super.onShowInterstitialResult(key, result);
        if (key.equalsIgnoreCase(Constants.key_ad_event_click_submit_event)) {
            event_click_submit_event();
            Log.e("interstitial_result", result + "");
        }
    }

    private void event_click_submit_event() {
        try {
            String s = datee;
            MyEventsSqlite db;
            db = new MyEventsSqlite(this);
            db.open();
            if (usernameInput.getText().toString().trim().length() != 0)
                db.insert(s, "  " + usernameInput.getText().toString());
            List values = db.getEvent(CalendarFragment.shamsiDate.getText().toString());
            CalendarFragment.adapter = new ArrayAdapter(this, R.layout.simple_list_item_1, values);
            CalendarFragment.list.setAdapter(CalendarFragment.adapter);
            CalendarFragment.calendarFragment.check_visibility_myEvent();
            CalendarFragment.setListViewHeightBasedOnChildren(CalendarFragment.list);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.event_has_submitted), Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
