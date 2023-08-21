
package com.arpitas.persiancalender.posAlgo;

public class Ecliptic {
    public double λ; //λ the ecliptic longitude
    public double β; //β the ecliptic latitude
    public double Δ; //distance  in km

    Ecliptic(double longitude, double latitude) {
        this.λ = longitude;
        this.β = latitude;
    }

    Ecliptic(double longitude, double latitude, double radius) {
        this.λ = longitude;
        this.β = latitude;
        this.Δ = radius;
    }
}


