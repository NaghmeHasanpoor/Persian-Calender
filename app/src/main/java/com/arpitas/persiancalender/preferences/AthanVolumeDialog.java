package com.arpitas.persiancalender.preferences;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.arpitas.persiancalender.R;
import com.arpitas.persiancalender.util.Utils;

import java.io.IOException;

public class AthanVolumeDialog extends PreferenceDialogFragmentCompat {
    int volume;
    AudioManager audioManager;
    MediaPlayer mediaPlayer;

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setNegativeButton(getResources().getString(R.string.cancel_update), (dialog, which) -> {});
    }

    @Override
    protected View onCreateDialogView(Context context) {
        View view = super.onCreateDialogView(context);
        final AthanVolumePreference athanPref = (AthanVolumePreference)getPreference();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setDataSource(
                    getContext(),
                    Utils.getInstance(getContext()).getAthanUri());
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, athanPref.getVolume(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SeekBar seekBar = view.findViewById(R.id.sbVolumeSlider);
        volume = athanPref.getVolume();
        seekBar.setProgress(volume);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress;
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }
                } catch (IOException | IllegalStateException ignored) {
                }
            }
        });
        return view;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        final AthanVolumePreference athanPref = (AthanVolumePreference)getPreference();
        mediaPlayer.release();
        if (positiveResult) {
            athanPref.setVolume(volume);
        }
    }
}
