package com.arpitas.persiancalender;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.arpitas.persiancalender.fragment.CompassFragment;

public final class CompassListener implements SensorEventListener {
    static final float ALPHA = 0.15f;
    float azimuth;
    private CompassFragment compassFragment;

    public CompassListener(CompassFragment compassFragment) {
        this.compassFragment = compassFragment;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        azimuth = lowPass(event.values[0], azimuth);
        compassFragment.compassView.setBearing(azimuth);
        compassFragment.compassView.invalidate();
    }

    private float lowPass(float input, float output) {
        if (Math.abs(180 - input) > 170) {
            return input;
        }
        return output + ALPHA * (input - output);
    }
}