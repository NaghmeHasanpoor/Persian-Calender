package com.arpitas.persiancalender.praytimes;

public class MinuteOrAngleDouble {
    private final boolean isMin;
    private final double value;

    public MinuteOrAngleDouble(double value, boolean isMin) {
        this.value = value;
        this.isMin = isMin;
    }

    public boolean isMin() {
        return isMin;
    }

    public double getValue() {
        return value;
    }
}
