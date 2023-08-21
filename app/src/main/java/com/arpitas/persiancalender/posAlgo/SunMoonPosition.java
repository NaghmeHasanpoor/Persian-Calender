
package com.arpitas.persiancalender.posAlgo;

public class SunMoonPosition {

    private Horizontal sunPosition, moonPosition;
    private double moonPhase;
    private EarthHeading qiblaInfo;
    private SolarPosition solar;
    private LunarPosition lunar;
    private Ecliptic moonPosEc, solarPosEc;
    private Equatorial moonPosEq, solarPosEq;

    public SunMoonPosition(double jd, double latitude, double longitude, double altitude, double ΔT) {
        solar = new SolarPosition();
        lunar = new LunarPosition();
        EarthPosition earth = new EarthPosition(latitude, longitude);
        double tau_Sun = 8.32 / (1440.0);    // 8.32 min  [cy]
        moonPosEc = lunar.calculateMoonEclipticCoordinates(jd, ΔT);
        solarPosEc = solar.calculateSunEclipticCoordinatesAstronomic(jd - tau_Sun, ΔT);


        double E = Math.toRadians(solarPosEc.λ - moonPosEc.λ);

        moonPosEq = lunar.calculateMoonEqutarialCoordinates(moonPosEc, jd, ΔT);
        solarPosEq = solar.calculateSunEquatorialCoordinates(solarPosEc, jd, ΔT);
        moonPosition = moonPosEq.Equ2Topocentric(longitude, latitude, altitude, jd, ΔT);//az=183.5858
        sunPosition = solarPosEq.Equ2Topocentric(longitude, latitude, altitude, jd, ΔT);

        EarthPosition qibla = new EarthPosition(21.416666667, 39.816666);
        qiblaInfo = earth.toEarthHeading(qibla);
        moonPhase = (1 + Math.cos(Math.PI - E)) / 2;//48694254279852139 e-17
    }

    public Horizontal getSunPosition() {
        return sunPosition;
    }

    public Horizontal getMoonPosition() {
        return moonPosition;
    }

    public double getMoonPhase() {
        return moonPhase;
    }

    public EarthHeading getDestinationHeading() {
        return qiblaInfo;
    }



}
