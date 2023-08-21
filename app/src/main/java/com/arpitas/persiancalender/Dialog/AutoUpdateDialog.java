package com.arpitas.persiancalender.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.entity.Ad;
import com.arpitas.persiancalender.entity.AutoUpdate;
import com.arpitas.persiancalender.util.Utils;

/**
 * Created by admin on 7/24/2019.
 */

public class AutoUpdateDialog extends Dialog {
    LinearLayout lCancel_au, lUpdate_au;
    private AutoUpdate autoUpdate = new AutoUpdate();

    public AutoUpdateDialog(@NonNull Context context, Ad ad) {
        super(context);
        if (ad != null)
            this.autoUpdate = ad.getAutoUpdate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_force_update_dialog);
        this.setCancelable(autoUpdate.isAuto_update_isForce());

        lCancel_au = findViewById(R.id.lCancel_force_update);
        lUpdate_au = findViewById(R.id.lUpdate_force);

        lCancel_au.setOnClickListener(v -> cancel());

        lUpdate_au.setOnClickListener(v -> {
            Utils.openAppRating(getContext());
            cancel();
        });

        TextView title_au = findViewById(R.id.title_auto_update);
        TextView desc_au = findViewById(R.id.desc_auto_update);
        TextView title_btn_cancel_au = findViewById(R.id.title_btn_cancel_auto_update);
        TextView title_btn_ok_au = findViewById(R.id.title_btn_ok_auto_update);

        if (autoUpdate.getDialog_title() != null && !autoUpdate.getDialog_title().equalsIgnoreCase(""))
            title_au.setText(autoUpdate.getDialog_title());

        if (autoUpdate.getDialog_description() != null && !autoUpdate.getDialog_description().equalsIgnoreCase(""))
            desc_au.setText(autoUpdate.getDialog_description());

        if (autoUpdate.getBotton_ok() != null && !autoUpdate.getBotton_ok().equalsIgnoreCase(""))
            title_btn_ok_au.setText(autoUpdate.getBotton_ok());

        if (autoUpdate.getButton_cancel() != null && !autoUpdate.getButton_cancel().equalsIgnoreCase(""))
            title_btn_cancel_au.setText(autoUpdate.getButton_cancel());
    }

}
