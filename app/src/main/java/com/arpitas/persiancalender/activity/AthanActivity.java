package com.arpitas.persiancalender.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arpitas.persiancalender.Constants;
import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.entity.CityEntity;
import com.arpitas.persiancalender.service.BroadcastReceivers;
import com.arpitas.persiancalender.util.LanguageUtil;
import com.arpitas.persiancalender.util.SharedPrefManager;
import com.arpitas.persiancalender.util.Utils;
import com.arpitas.persiancalender.praytimes.Coordinate;
import com.arpitas.persiancalender.praytimes.PrayTime;

import java.util.concurrent.TimeUnit;

public class AthanActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textAlarmName;
    private ImageView athanIconView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPrefManager shared = new SharedPrefManager(this);
        if (!shared.getLanguage().equals(""))
            LanguageUtil.changeLanguage(this, shared.getLanguage());

        /*----------------------------------------------------------------------------------------*/
        super.onCreate(savedInstanceState);

        String prayerKey = getIntent().getStringExtra(Constants.KEY_EXTRA_PRAYER_KEY);
        Utils utils = Utils.getInstance(getApplicationContext());
        setContentView(R.layout.activity_athan);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textAlarmName =  findViewById(R.id.athan_name);
        TextView textCityName =  findViewById(R.id.place);
        athanIconView =  findViewById(R.id.background_image);
        athanIconView.setOnClickListener(this);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.moazen);
        mp.start();
        setPrayerView(prayerKey);

        CityEntity cityEntity = utils.getCityFromPreference();
        if (cityEntity != null) {
            String cityName = utils.getAppLanguage().equals("en") ? cityEntity.getEn() : cityEntity.getFa();
            textCityName.setText(getString(R.string.in_city_time) + " " + cityName);
        } else {
            Coordinate coordinate = utils.getCoordinate();
            textCityName.setText(getString(R.string.in_city_time) + " " + coordinate.getLatitude() + ", " + coordinate.getLongitude());
        }

        new Handler().postDelayed(this::finish, TimeUnit.SECONDS.toMillis(45));
    }

    private void setPrayerView(String key) {
        if (!TextUtils.isEmpty(key)) {
            PrayTime prayTime = PrayTime.valueOf(key);
            ((BitmapDrawable) athanIconView.getDrawable()).getBitmap().recycle();
            switch (prayTime) {
                case IMSAK:
                    textAlarmName.setText(getString(R.string.azan1));
                    athanIconView.setImageResource(R.drawable.imsak);
                    break;

                case DHUHR:
                    textAlarmName.setText(getString(R.string.azan2));
                    athanIconView.setImageResource(R.drawable.dhuhr);
                    break;

                case ASR:
                    textAlarmName.setText(getString(R.string.azan3));
                    athanIconView.setImageResource(R.drawable.asr);
                    break;

                case MAGHRIB:
                    textAlarmName.setText(getString(R.string.azan4));
                    athanIconView.setImageResource(R.drawable.maghrib);
                    break;

                case ISHA:
                    textAlarmName.setText(getString(R.string.azan5));
                    athanIconView.setImageResource(R.drawable.isha);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        sendBroadcastStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendBroadcastStop();
    }

    @Override
    public void onBackPressed() {
        sendBroadcastStop();
    }

    private void sendBroadcastStop() {
        Intent intent = new Intent(getBaseContext(), BroadcastReceivers.class);
        intent.setAction(Constants.ACTION_STOP_ALARM);
        AthanActivity.this.sendBroadcast(intent);
        finish();
    }

}
