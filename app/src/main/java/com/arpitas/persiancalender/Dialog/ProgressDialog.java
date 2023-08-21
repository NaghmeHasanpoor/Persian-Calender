package com.arpitas.persiancalender.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.arpitas.persiancalender.R;

public class ProgressDialog extends Dialog {

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_progress_dialog);
        setCancelable(false);
    }
}
