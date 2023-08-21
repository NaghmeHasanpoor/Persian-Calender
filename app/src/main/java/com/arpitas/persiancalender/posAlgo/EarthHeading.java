
package com.arpitas.persiancalender.posAlgo;

public class EarthHeading {
    private double mHeading;
    private long mMetres;

    public EarthHeading(double heading, long metres) {
        mHeading = heading;
        mMetres = metres;
    }

    public double getHeading() {
        return mHeading;
    }

}
