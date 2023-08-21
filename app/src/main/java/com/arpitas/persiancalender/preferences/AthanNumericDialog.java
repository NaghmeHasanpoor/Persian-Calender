package com.arpitas.persiancalender.preferences;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.EditTextPreferenceDialogFragmentCompat;

import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.util.Utils;

public class AthanNumericDialog extends EditTextPreferenceDialogFragmentCompat {

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setNegativeButton(getResources().getString(R.string.cancel_update), (dialog, which) -> {});
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        View dialogMessageView = view.findViewById(android.R.id.message);
        if (dialogMessageView instanceof TextView) {
            Utils.getInstance(getContext()).setFontAndShape((TextView) dialogMessageView);
        }
        EditText editText =  view.findViewById(android.R.id.edit);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setTextDirection(View.TEXT_DIRECTION_LTR);
        editText.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

    }
}
