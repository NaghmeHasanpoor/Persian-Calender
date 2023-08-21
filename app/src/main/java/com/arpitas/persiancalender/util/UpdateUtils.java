package com.arpitas.persiancalender.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.arpitas.persiancalender.activity.MainActivity;
import com.arpitas.persiancalender.praytimes.Clock;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.arpitas.persiancalender.calendar.CivilDate;
import com.arpitas.persiancalender.calendar.PersianDate;

public class UpdateUtils {
    private static UpdateUtils myInstance;
    private Context context;

    private UpdateUtils(Context context) {
        this.context = context;
    }

    public static UpdateUtils getInstance(Context context) {
        if (myInstance == null) {
            myInstance = new UpdateUtils(context);
        }
        return myInstance;
    }
    boolean firstTime = true;
    private PersianDate pastDate;

    public void update(boolean updateDate) throws IOException {
        Log.d("UpdateUtils", "update");
        Utils utils = Utils.getInstance(context);
        if (firstTime) {
            utils.loadLanguageFromSettings();
            firstTime = false;
        }
        Calendar calendar = utils.makeCalendarFromDate(new Date());
        CivilDate civil = new CivilDate(calendar);
        PersianDate persian = utils.getToday();

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent launchAppPendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            launchAppPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        else
            launchAppPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
                    | PendingIntent.FLAG_MUTABLE);

        String colorInt = utils.getSelectedWidgetTextColor();
        int color = Color.parseColor(colorInt);
        Clock currentClock = new Clock(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        String owghat;

        if (pastDate == null || !pastDate.equals(persian) || updateDate) {
            Log.d("UpdateUtils", "change date");
            pastDate = persian;
            utils.loadAlarms();
//            owghat = utils.getNextOghatTime(currentClock, true);

            String holidays = utils.getEventsTitle(persian, true);
            String events = utils.getEventsTitle(persian, false);
            //
            // Permanent Notification Bar and DashClock Data Extension Update
            //
            //
            String status = utils.getMonthName(persian);
            String title = utils.getWeekDayName(civil) + " " + utils.dateToString(persian);
        }
    }
}
