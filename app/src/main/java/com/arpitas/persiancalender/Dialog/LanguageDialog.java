package com.arpitas.persiancalender.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.interfaces.LanguageListener;
import com.arpitas.persiancalender.util.SharedPrefManager;

public class LanguageDialog extends Dialog {
    private SharedPrefManager shared;
    private LanguageListener listener;
    private boolean isFirst;
    private RadioButton mEnglishRadio,mPersianRadio;
    private CardView card_ok;
    private TextView txt_ok;

    public LanguageDialog(@NonNull Context context, LanguageListener listener, boolean isFirst) {
        super(context);
        this.listener = listener;
        this.isFirst = isFirst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_language_dialog);
        setCancelable(false);
        shared = new SharedPrefManager(getContext());

        card_ok = findViewById(R.id.card_save_language_dialog);
        txt_ok = findViewById(R.id.txt_save_language_dialog);
        mEnglishRadio = findViewById(R.id.rdBtn_english);
        mPersianRadio = findViewById(R.id.rdBtn_persian);

        if (!isFirst){
            String lang = !shared.getLanguage().equals("") ? shared.getLanguage() : SharedPrefManager.EN;
            if (lang.equals(SharedPrefManager.EN)) {
                mEnglishRadio.setChecked(true);
            } else {
                mPersianRadio.setChecked(true);
            }
        }else {
            mEnglishRadio.setChecked(false);
            mPersianRadio.setChecked(false);
            card_ok.setCardBackgroundColor(getContext().getResources().getColor(R.color.gray_2));
            txt_ok.setTextColor(getContext().getResources().getColor(R.color.gray_3));
        }

        mEnglishRadio.setOnClickListener(view -> event_click_english());
        findViewById(R.id.layout_english).setOnClickListener(view ->event_click_english());
        findViewById(R.id.txtLanguage_english).setOnClickListener(view -> event_click_english());
        findViewById(R.id.imgLanguage_english).setOnClickListener(view -> event_click_english());

        mPersianRadio.setOnClickListener(view ->event_click_persian());
        findViewById(R.id.layout_persian).setOnClickListener(view ->event_click_persian());
        findViewById(R.id.txtLanguage_persian).setOnClickListener(view -> event_click_persian());
        findViewById(R.id.imgLanguage_persian).setOnClickListener(view -> event_click_persian());

        findViewById(R.id.card_save_language_dialog).setOnClickListener(view -> {
            if (mEnglishRadio.isChecked()){
                if (!shared.getLanguage().equalsIgnoreCase(SharedPrefManager.EN)) {
                    shared.setStateShowDialogLang(true);
                    shared.setLanguage(SharedPrefManager.EN);
                    if(listener != null)
                        listener.onClickLang(SharedPrefManager.EN);
                    dismiss();
                } else {
                    dismiss();
                }
            }else if(mPersianRadio.isChecked()){
                if (!shared.getLanguage().equalsIgnoreCase(SharedPrefManager.FA)) {
                    shared.setStateShowDialogLang(true);
                    shared.setLanguage(SharedPrefManager.FA);
                    if(listener != null)
                        listener.onClickLang(SharedPrefManager.FA);
                    dismiss();
                } else {
                    dismiss();
                }
            }else{
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_language), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void event_click_english(){
        isFirst = true;
        mEnglishRadio.setChecked(true);
        card_ok.setCardBackgroundColor(getContext().getResources().getColor(R.color.dark_primary));
        txt_ok.setTextColor(getContext().getResources().getColor(R.color.white));
    }

    private void event_click_persian(){
        isFirst = true;
        mPersianRadio.setChecked(true);
        card_ok.setCardBackgroundColor(getContext().getResources().getColor(R.color.dark_primary));
        txt_ok.setTextColor(getContext().getResources().getColor(R.color.white));
    }
}
