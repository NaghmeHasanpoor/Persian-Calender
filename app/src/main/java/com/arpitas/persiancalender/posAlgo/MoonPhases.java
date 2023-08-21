
package com.arpitas.persiancalender.posAlgo;

public class MoonPhases {
    private SolarPosition solar;
    private LunarPosition lunar;
    private Ecliptic moonPos, solarPos;

    public MoonPhases() {
        solar = new SolarPosition();
        lunar = new LunarPosition();
    }

    public double searchPhaseEvent(double jd, double ΔT, int phase) {
        double LongDiff, tau_Sun = 8.32 / (1440.0); // 8.32 min [cy]
        moonPos = lunar.calculateMoonEclipticCoordinates(jd, ΔT);
        solarPos = solar.calculateSunEclipticCoordinatesAstronomic(
                jd - tau_Sun, ΔT);
        LongDiff = moonPos.λ - solarPos.λ;
        if (phase == 8)// Crescent Visibility at 8 degrees Angle
        {
            double elongation = Math.sqrt(LongDiff * LongDiff + moonPos.β
                    * moonPos.β);
            return phase - elongation;
        }
        return modulo((LongDiff - phase + 180), 360) - 180;

    }

    private double modulo(double x, double y) {
        return y * frac(x / y);
    }

    private double frac(double x) {
        return x - Math.floor(x);
    }
}
