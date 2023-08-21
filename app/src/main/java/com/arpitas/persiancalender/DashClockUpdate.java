package com.arpitas.persiancalender;

import com.arpitas.persiancalender.util.UpdateUtils;
import com.google.android.apps.dashclock.api.DashClockExtension;
import java.io.IOException;

public class DashClockUpdate extends DashClockExtension {

    @Override
    protected void onUpdateData(int reason) {
        setUpdateWhenScreenOn(true);
        UpdateUtils updateUtils = UpdateUtils.getInstance(getApplicationContext());
        try {
            updateUtils.update(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
