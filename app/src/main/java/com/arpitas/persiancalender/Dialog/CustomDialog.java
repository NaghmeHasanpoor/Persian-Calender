package com.arpitas.persiancalender.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.entity.Dialog_;
import com.arpitas.persiancalender.interfaces.ICustomDl;

public class CustomDialog extends Dialog {
    private Dialog_ dialog;
    private ICustomDl listener;

    public CustomDialog(@NonNull Context context, Dialog_ dialog, ICustomDl listener) {
        super(context);
        this.dialog = dialog;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_custom_dialog);
        setCancelable(!dialog.isDialog_isForce());

        TextView title = findViewById(R.id.title_custom_dialog);
        TextView desc = findViewById(R.id.desc_custom_dialog);
        TextView title_btn_cancel = findViewById(R.id.title_btn_cancel_custom_dialog);
        TextView title_btn_ok = findViewById(R.id.title_btn_ok_custom_dialog);

        findViewById(R.id.lCancel_custom_dialog).setOnClickListener(view -> {
            if (dialog.isDialog_isForce()){
                if (listener != null){
                    dismiss();
                    listener.onCancel();
                }
            }else {
                dismiss();
            }
        });
        findViewById(R.id.lOk_custom_dialog).setOnClickListener(view -> {
            if (dialog.getType().equalsIgnoreCase("1")){
                action_view();
            }else if (dialog.getType().equalsIgnoreCase("2")){
                action_send();
            }
            dismiss();
        });

        if (dialog.getDialog_title() != null && !dialog.getDialog_title().equalsIgnoreCase(""))
            title.setText(dialog.getDialog_title());

        if (dialog.getDialog_description() != null && !dialog.getDialog_description().equalsIgnoreCase(""))
            desc.setText(dialog.getDialog_description());

        if (dialog.getDialog_button_ok() != null && !dialog.getDialog_button_ok().equalsIgnoreCase(""))
            title_btn_ok.setText(dialog.getDialog_button_ok());

        if (dialog.getDialog_button_cancel() != null && !dialog.getDialog_button_cancel().equalsIgnoreCase(""))
            title_btn_cancel.setText(dialog.getDialog_button_cancel());
    }

    private void action_view() {
        try {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(dialog.getIntent_url())));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void action_send(){
        try {
            String[] params = dialog.getBody().split(",");
            Intent it = new Intent(Intent.ACTION_SENDTO,  Uri.parse(params[0]));
            it.putExtra("sms_body", params[1]);
            getContext().startActivity(it);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
