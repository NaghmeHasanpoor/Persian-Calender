package com.arpitas.persiancalender.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.interfaces.ExitAppListener;

public class WarningDialog extends Dialog {
    private ExitAppListener listener;

    public WarningDialog(@NonNull Context context, ExitAppListener _listener) {
        super(context);
        listener =_listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_warning_dialog);
        setCancelable(false);

        findViewById(R.id.lExit_warning).setOnClickListener(view -> {
            dismiss();
            if (listener != null)
                listener.onExit();

        });
    }
}
