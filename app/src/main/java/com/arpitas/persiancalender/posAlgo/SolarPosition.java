
package com.arpitas.persiancalender.posAlgo;

public class SolarPosition {

    private static final double LTERMS[][][] = {
            {{175347046.0, 0, 0}, {3341656.0, 4.6692568, 6283.07585}, {34894.0, 4.6261, 12566.1517}, {3497.0, 2.7441, 5753.3849}, {3418.0, 2.8289, 3.5231}, {3136.0, 3.6277, 77713.7715}, {2676.0, 4.4181, 7860.4194}, {2343.0, 6.1352, 3930.2097}, {1324.0, 0.7425, 11506.7698}, {1273.0, 2.0371, 529.691}, {1199.0, 1.1096, 1577.3435}, {990, 5.233, 5884.927}, {902, 2.045, 26.298}, {857, 3.508, 398.149}, {780, 1.179, 5223.694}, {753, 2.533, 5507.553}, {505, 4.583, 18849.228}, {492, 4.205, 775.523}, {357, 2.92, 0.067}, {317, 5.849, 11790.629}, {284, 1.899, 796.298}, {271, 0.315, 10977.079}, {243, 0.345, 5486.778}, {206, 4.806, 2544.314}, {205, 1.869, 5573.143}, {202, 2.458, 6069.777}, {156, 0.833, 213.299}, {132, 3.411, 2942.463}, {126, 1.083, 20.775}, {115, 0.645, 0.98}, {103, 0.636, 4694.003}, {102, 0.976, 15720.839}, {102, 4.267, 7.114}, {99, 6.21, 2146.17}, {98, 0.68, 155.42}, {86, 5.98, 161000.69}, {85, 1.3, 6275.96}, {85, 3.67, 71430.7}, {80, 1.81, 17260.15}, {79, 3.04, 12036.46}, {75, 1.76, 5088.63}, {74, 3.5, 3154.69}, {74, 4.68, 801.82}, {70, 0.83, 9437.76}, {62, 3.98, 8827.39}, {61, 1.82, 7084.9}, {57, 2.78, 6286.6}, {56, 4.39, 14143.5}, {56, 3.47, 6279.55}, {52, 0.19, 12139.55}, {52, 1.33, 1748.02}, {51, 0.28, 5856.48}, {49, 0.49, 1194.45}, {41, 5.37, 8429.24}, {41, 2.4, 19651.05}, {39, 6.17, 10447.39}, {37, 6.04, 10213.29}, {37, 2.57, 1059.38}, {36, 1.71, 2352.87}, {36, 1.78, 6812.77}, {33, 0.59, 17789.85}, {30, 0.44, 83996.85}, {30, 2.74, 1349.87}, {25, 3.16, 4690.48}},
            {{628331966747.0, 0, 0}, {206059.0, 2.678235, 6283.07585}, {4303.0, 2.6351, 12566.1517}, {425.0, 1.59, 3.523}, {119.0, 5.796, 26.298}, {109.0, 2.966, 1577.344}, {93, 2.59, 18849.23}, {72, 1.14, 529.69}, {68, 1.87, 398.15}, {67, 4.41, 5507.55}, {59, 2.89, 5223.69}, {56, 2.17, 155.42}, {45, 0.4, 796.3}, {36, 0.47, 775.52}, {29, 2.65, 7.11}, {21, 5.34, 0.98}, {19, 1.85, 5486.78}, {19, 4.97, 213.3}, {17, 2.99, 6275.96}, {16, 0.03, 2544.31}, {16, 1.43, 2146.17}, {15, 1.21, 10977.08}, {12, 2.83, 1748.02}, {12, 3.26, 5088.63}, {12, 5.27, 1194.45}, {12, 2.08, 4694}, {11, 0.77, 553.57}, {10, 1.3, 6286.6}, {10, 4.24, 1349.87}, {9, 2.7, 242.73}, {9, 5.64, 951.72}, {8, 5.3, 2352.87}, {6, 2.65, 9437.76}, {6, 4.67, 4690.48}},
            {{52919.0, 0, 0}, {8720.0, 1.0721, 6283.0758}, {309.0, 0.867, 12566.152}, {27, 0.05, 3.52}, {16, 5.19, 26.3}, {16, 3.68, 155.42}, {10, 0.76, 18849.23}, {9, 2.06, 77713.77}, {7, 0.83, 775.52}, {5, 4.66, 1577.34}, {4, 1.03, 7.11}, {4, 3.44, 5573.14}, {3, 5.14, 796.3}, {3, 6.05, 5507.55}, {3, 1.19, 242.73}, {3, 6.12, 529.69}, {3, 0.31, 398.15}, {3, 2.28, 553.57}, {2, 4.38, 5223.69}, {2, 3.75, 0.98}},
            {{289.0, 5.844, 6283.076}, {35, 0, 0}, {17, 5.49, 12566.15}, {3, 5.2, 155.42}, {1, 4.72, 3.52}, {1, 5.3, 18849.23}, {1, 5.97, 242.73}},
            {{114.0, 3.142, 0}, {8, 4.13, 6283.08}, {1, 3.84, 12566.15}},
            {{1, 3.14, 0}}
    };
    private static final double BTERMS[][][] = {
            {{280.0, 3.199, 84334.662}, {102.0, 5.422, 5507.553}, {80, 3.88, 5223.69}, {44, 3.7, 2352.87}, {32, 4, 1577.34}},
            {{9, 3.9, 5507.55}, {6, 1.73, 5223.69}}
    };

    private static final double RTERMS[][][] = {
            {{100013989.0, 0, 0}, {1670700.0, 3.0984635, 6283.07585}, {13956.0, 3.05525, 12566.1517}, {3084.0, 5.1985, 77713.7715}, {1628.0, 1.1739, 5753.3849}, {1576.0, 2.8469, 7860.4194}, {925.0, 5.453, 11506.77}, {542.0, 4.564, 3930.21}, {472.0, 3.661, 5884.927}, {346.0, 0.964, 5507.553}, {329.0, 5.9, 5223.694}, {307.0, 0.299, 5573.143}, {243.0, 4.273, 11790.629}, {212.0, 5.847, 1577.344}, {186.0, 5.022, 10977.079}, {175.0, 3.012, 18849.228}, {110.0, 5.055, 5486.778}, {98, 0.89, 6069.78}, {86, 5.69, 15720.84}, {86, 1.27, 161000.69}, {65, 0.27, 17260.15}, {63, 0.92, 529.69}, {57, 2.01, 83996.85}, {56, 5.24, 71430.7}, {49, 3.25, 2544.31}, {47, 2.58, 775.52}, {45, 5.54, 9437.76}, {43, 6.01, 6275.96}, {39, 5.36, 4694}, {38, 2.39, 8827.39}, {37, 0.83, 19651.05}, {37, 4.9, 12139.55}, {36, 1.67, 12036.46}, {35, 1.84, 2942.46}, {33, 0.24, 7084.9}, {32, 0.18, 5088.63}, {32, 1.78, 398.15}, {28, 1.21, 6286.6}, {28, 1.9, 6279.55}, {26, 4.59, 10447.39}},
            {{103019.0, 1.10749, 6283.07585}, {1721.0, 1.0644, 12566.1517}, {702.0, 3.142, 0}, {32, 1.02, 18849.23}, {31, 2.84, 5507.55}, {25, 1.32, 5223.69}, {18, 1.42, 1577.34}, {10, 5.91, 10977.08}, {9, 1.42, 6275.96}, {9, 0.27, 5486.78}},
            {{4359.0, 5.7846, 6283.0758}, {124.0, 5.579, 12566.152}, {12, 3.14, 0}, {9, 3.63, 77713.77}, {6, 1.87, 5573.14}, {3, 5.47, 18849.23}},
            {{145.0, 4.273, 6283.076}, {7, 3.92, 12566.15}},
            {{4, 2.56, 6283.08}}
    };

    private static final byte YTERMS[][] = {
            {0, 0, 0, 0, 1}, {-2, 0, 0, 2, 2}, {0, 0, 0, 2, 2}, {0, 0, 0, 0, 2}, {0, 1, 0, 0, 0}, {0, 0, 1, 0, 0}, {-2, 1, 0, 2, 2}, {0, 0, 0, 2, 1}, {0, 0, 1, 2, 2}, {-2, -1, 0, 2, 2}, {-2, 0, 1, 0, 0}, {-2, 0, 0, 2, 1}, {0, 0, -1, 2, 2}, {2, 0, 0, 0, 0}, {0, 0, 1, 0, 1}, {2, 0, -1, 2, 2}, {0, 0, -1, 0, 1}, {0, 0, 1, 2, 1}, {-2, 0, 2, 0, 0}, {0, 0, -2, 2, 1}, {2, 0, 0, 2, 2}, {0, 0, 2, 2, 2}, {0, 0, 2, 0, 0}, {-2, 0, 1, 2, 2}, {0, 0, 0, 2, 0}, {-2, 0, 0, 2, 0}, {0, 0, -1, 2, 1}, {0, 2, 0, 0, 0}, {2, 0, -1, 0, 1}, {-2, 2, 0, 2, 2}, {0, 1, 0, 0, 1}, {-2, 0, 1, 0, 1}, {0, -1, 0, 0, 1}, {0, 0, 2, -2, 0}, {2, 0, -1, 2, 1}, {2, 0, 1, 2, 2}, {0, 1, 0, 2, 2}, {-2, 1, 1, 0, 0}, {0, -1, 0, 2, 2}, {2, 0, 0, 2, 1}, {2, 0, 1, 0, 0}, {-2, 0, 2, 2, 2}, {-2, 0, 1, 2, 1}, {2, 0, -2, 0, 1}, {2, 0, 0, 0, 1}, {0, -1, 1, 0, 0}, {-2, -1, 0, 2, 1}, {-2, 0, 0, 0, 1}, {0, 0, 2, 2, 1}, {-2, 0, 2, 0, 1}, {-2, 1, 0, 2, 1}, {0, 0, 1, -2, 0}, {-1, 0, 1, 0, 0}, {-2, 1, 0, 0, 0}, {1, 0, 0, 0, 0}, {0, 0, 1, 2, 0}, {0, 0, -2, 2, 2}, {-1, -1, 1, 0, 0}, {0, 1, 1, 0, 0}, {0, -1, 1, 2, 2}, {2, -1, -1, 2, 2}, {0, 0, 3, 2, 2}, {2, -1, 0, 2, 2},};
    private static final double PETERMS[][] = {
            {-171996, -174.2, 92025, 8.9}, {-13187, -1.6, 5736, -3.1}, {-2274, -0.2, 977, -0.5}, {2062, 0.2, -895, 0.5}, {1426, -3.4, 54, -0.1}, {712, 0.1, -7, 0}, {-517, 1.2, 224, -0.6}, {-386, -0.4, 200, 0}, {-301, 0, 129, -0.1}, {217, -0.5, -95, 0.3}, {-158, 0, 0, 0}, {129, 0.1, -70, 0}, {123, 0, -53, 0}, {63, 0, 0, 0}, {63, 0.1, -33, 0}, {-59, 0, 26, 0}, {-58, -0.1, 32, 0}, {-51, 0, 27, 0}, {48, 0, 0, 0}, {46, 0, -24, 0}, {-38, 0, 16, 0}, {-31, 0, 13, 0}, {29, 0, 0, 0}, {29, 0, -12, 0}, {26, 0, 0, 0}, {-22, 0, 0, 0}, {21, 0, -10, 0}, {17, -0.1, 0, 0}, {16, 0, -8, 0}, {-16, 0.1, 7, 0}, {-15, 0, 9, 0}, {-13, 0, 7, 0}, {-12, 0, 6, 0}, {11, 0, 0, 0}, {-10, 0, 5, 0}, {-8, 0, 3, 0}, {7, 0, -3, 0}, {-7, 0, 0, 0}, {-7, 0, 3, 0}, {-7, 0, 3, 0}, {6, 0, 0, 0}, {6, 0, -3, 0}, {6, 0, -3, 0}, {-6, 0, 3, 0}, {-6, 0, 3, 0}, {5, 0, 0, 0}, {-5, 0, 3, 0}, {-5, 0, 3, 0}, {-5, 0, 3, 0}, {4, 0, 0, 0}, {4, 0, 0, 0}, {4, 0, 0, 0}, {-4, 0, 0, 0}, {-4, 0, 0, 0}, {-4, 0, 0, 0}, {3, 0, 0, 0}, {-3, 0, 0, 0}, {-3, 0, 0, 0}, {-3, 0, 0, 0}, {-3, 0, 0, 0}, {-3, 0, 0, 0}, {-3, 0, 0, 0}, {-3, 0, 0, 0},};
    private static byte L_COUNT, B_COUNT, R_COUNT, Y_COUNT;
    private final double SUNRADIUS = 0.26667;
    private final byte FAJR = 0, SUNRISE = 1, SUNTRANSIT = 2, ASR_SHAFI = 3, ASR_HANEFI = 4, SUNSET = 5, ISHA = 6, SUN_COUNT = 7;
    private final byte FAJR_ = 0, ISRAK = 1, SUNTRANSIT_ = 2, ASRHANEFI = 3, ISFIRAR = 4, SUNSET_ = 5, KERAHAT_COUNT = 6, DUHA = 7, ISTIVA = 8;

    static double limitDegrees(double degrees) {
        double limited;
        degrees /= 360.0;
        limited = 360.0 * (degrees - Math.floor(degrees));
        if (limited < 0) {
            limited += 360.0;
        }
        return limited;
    }


    private static double meanElongationMoonSun(double jce) {
        return AstroLib.thirdOrderPolynomial(1.0 / 189474.0, -0.0019142, 445267.11148, 297.85036, jce);
    }


    private static double meanAnomalySun(double jce) {
        return AstroLib.thirdOrderPolynomial(-1.0 / 300000.0, -0.0001603, 35999.05034, 357.52772, jce);
    }


    private static double meanAnomalyMoon(double jce) {
        return AstroLib.thirdOrderPolynomial(1.0 / 56250.0, 0.0086972, 477198.867398, 134.96298, jce);
    }


    private static double argumentLatitudeMoon(double jce) {
        return AstroLib.thirdOrderPolynomial(1.0 / 327270.0, -0.0036825, 483202.017538, 93.27191, jce);
    }


    private static double ascendingLongitudeMoon(double jce) {
        return AstroLib.thirdOrderPolynomial(1.0 / 450000.0, 0.0020708, -1934.136261, 125.04452, jce);
    }

    private static double xyTermSummation(int i, double x[]) {
        int j;
        double sum = 0;
        int TERM_Y_COUNT = x.length;
        for (j = 0; j < TERM_Y_COUNT; j++) {
            sum += x[j] * YTERMS[i][j];
        }

        return sum;
    }


    static double nutationObliquity(double jce, double Δεi[]) {
        int i;
        double xyTermSum, sumε = 0;
        Y_COUNT = (byte) YTERMS.length;
        for (i = 0; i < Y_COUNT; i++) {
            xyTermSum = Math.toRadians(xyTermSummation(i, Δεi));
            sumε += (PETERMS[i][2] + jce * PETERMS[i][3]) * Math.cos(xyTermSum);
        }

        return sumε / 36000000.0;
    }


    static double nutationLongitude(double jce, double X[]) {
        int i;
        double xyTermSum, sumPsi = 0;
        Y_COUNT = (byte) YTERMS.length;
        for (i = 0; i < Y_COUNT; i++) {
            xyTermSum = Math.toRadians(xyTermSummation(i, X));
            sumPsi += (PETERMS[i][0] + jce * PETERMS[i][1]) * Math.sin(xyTermSum);
        }

        return sumPsi / 36000000.0;
    }


    static double eclipticTrueObliquity(double Δε, double ε0) {
        return Δε + ε0 / 3600.0;
    }

    static double eclipticMeanObliquity(double jme) {
        double u = jme / 10.0;

        return 84381.448 + u * (-4680.93 + u * (-1.55 + u * (1999.25 + u * (-51.38 + u * (-249.67
                + u * (-39.05 + u * (7.12 + u * (27.87 + u * (5.79 + u * 2.45)))))))));
    }


    static double greenwichMeanSiderealTime(double jd) {

        double jc = (jd - 2451545.0) / 36525.0;// jc the Julian  Century
        return limitDegrees(280.46061837 + 360.98564736629 * (jd - 2451545.0)
                + jc * jc * (0.000387933 - jc / 38710000.0));
    }


    static double greenwichSiderealTime(double ν0, double Δψ, double ε) {
        return ν0 + Δψ * Math.cos(Math.toRadians(ε));
    }


    static double geocentricRightAscension(double λ, double ε, double β) {
        double λRad = Math.toRadians(λ);
        double εRad = Math.toRadians(ε);
        return limitDegrees(Math.toDegrees(MATH.atan2(Math.sin(λRad) * Math.cos(εRad) - Math.tan(Math.toRadians(β) * Math.sin(εRad)), Math.cos(λRad))));

    }


    static double geocentricDeclination(double λ, double ε, double β) {
        double βRad = Math.toRadians(β);
        double εRad = Math.toRadians(ε);

        return Math.toDegrees(MATH.asin(Math.sin(βRad) * Math.cos(εRad)
                + Math.cos(βRad) * Math.sin(εRad) * Math.sin(Math.toRadians(λ))));
    }

    static double calculateGreenwichSiderealTime(double jd, double ΔT) {
        double jce, jme, jde, Δψ, ε, ν0, Δε, ε0;
        double ν;
        double[] x = new double[5];
        jde = AstroLib.getJulianEphemerisDay(jd, ΔT);
        jce = AstroLib.getJulianEphemerisCentury(jde);
        jme = AstroLib.getJulianEphemerisMillennium(jce);
        x[0] = meanElongationMoonSun(jce);
        x[1] = meanAnomalySun(jce);
        x[2] = meanAnomalyMoon(jce);
        x[3] = argumentLatitudeMoon(jce);
        x[4] = ascendingLongitudeMoon(jce);
        ε0 = eclipticMeanObliquity(jme);//
        Δε = nutationObliquity(jce, x);//
        Δψ = nutationLongitude(jce, x);//
        ε = eclipticTrueObliquity(Δε, ε0);//
        ν0 = greenwichMeanSiderealTime(jd);
        ν = greenwichSiderealTime(ν0, Δψ, ε);
        return ν;
    }


    static double[] calculateXArray(double jd, double ΔT) {
        double jce, jde;
        double[] x = new double[5];
        jde = AstroLib.getJulianEphemerisDay(jd, ΔT);
        jce = AstroLib.getJulianEphemerisCentury(jde);
        x[0] = meanElongationMoonSun(jce);
        x[1] = meanAnomalySun(jce);
        x[2] = meanAnomalyMoon(jce);
        x[3] = argumentLatitudeMoon(jce);
        x[4] = ascendingLongitudeMoon(jce);
        return x;
    }


    double limitMinutes(double minutes) {
        double limited = minutes;
        if (limited < -20.0) {
            limited += 1440.0;
        } else if (limited > 20.0) {
            limited -= 1440.0;
        }
        return limited;
    }

    double limitDegrees180pm(double degrees) {
        double limited;

        degrees /= 360.0;
        limited = 360.0 * (degrees - Math.floor(degrees));
        if (limited < -180.0) {
            limited += 360.0;
        } else if (limited > 180.0) {
            limited -= 360.0;
        }

        return limited;
    }

    double limitDegrees180(double degrees) {
        double limited;

        degrees /= 180.0;
        limited = 180.0 * (degrees - Math.floor(degrees));
        if (limited < 0) {
            limited += 180.0;
        }

        return limited;
    }

    double limitZero2one(double value) {
        double limited;

        limited = value - Math.floor(value);
        if (limited < 0) {
            limited += 1.0;
        }

        return limited;
    }

    double dayFracToLocalHour(double dayfrac, double timezone) {
        return 24.0 * limitZero2one(dayfrac + timezone / 24.0);
    }


    private double earthHeliocentricLongitude(double jme) {
        L_COUNT = (byte) LTERMS.length;

        double[] sum = new double[L_COUNT];
        int i;

        for (i = 0; i < L_COUNT; i++) {
            sum[i] = earthPeriodicTermSummation(LTERMS[i], LTERMS[i].length, jme);
        }

        return limitDegrees(Math.toDegrees(earthValues(sum, L_COUNT, jme)));

    }


    private double earthRadiusVector(double jme) {
        R_COUNT = (byte) RTERMS.length;
        double[] sum = new double[R_COUNT];
        int i;

        for (i = 0; i < R_COUNT; i++) {
            sum[i] = earthPeriodicTermSummation(RTERMS[i], RTERMS[i].length, jme);
        }

        return earthValues(sum, R_COUNT, jme);

    }


    private double earthHeliocentricLatitude(double jme) {
        B_COUNT = (byte) BTERMS.length;

        double[] sum = new double[B_COUNT];
        int i;
        for (i = 0; i < B_COUNT; i++) {
            sum[i] = earthPeriodicTermSummation(BTERMS[i], BTERMS[i].length, jme);
        }

        return Math.toDegrees(earthValues(sum, B_COUNT, jme));

    }


    private double earthPeriodicTermSummation(double terms[][], int count, double jme) {
        int i;
        double sum = 0;
        for (i = 0; i < count; i++) {
            sum += terms[i][0] * Math.cos(terms[i][1] + terms[i][2] * jme);
        }
        return sum;
    }


    private double earthValues(double termSum[], int count, double jme) {
        int i;
        double sum = 0;

        for (i = 0; i < count; i++) {
            sum += termSum[i] * MATH.pow(jme, i);
        }

        sum /= 1.0e8;

        return sum;
    }


    double getGeocentricLatitude(double b) {
        return -b;
    }


    double geocentricLongitude(double L) {
        double theta = L + 180.0;

        if (theta >= 360.0) {
            theta -= 360.0;
        }

        return theta;
    }

    private double apparentSunLongitude(double Θ, double Δψ, double Δτ) {
        return Θ + Δψ + Δτ;
    }


    private double aberrationCorrection(double r) {
        return -20.4898 / (3600.0 * r);
    }


    double approxSunTransitTime(double αo, double longitude, double ν) {
        return (αo - longitude - ν) / 360.0;
    }

    double getHourAngleAtRiseSet(double latitude, double δo, double h0Prime) {
        double h0 = -99999;
        double latitudeRad = Math.toRadians(latitude);
        double δoRad = Math.toRadians(δo);
        double argument = (Math.sin(Math.toRadians(h0Prime)) - Math.sin(latitudeRad) * Math.sin(δoRad))
                / (Math.cos(latitudeRad) * Math.cos(δoRad));

        if (Math.abs(argument) <= 1) {
            h0 = limitDegrees180(Math.toDegrees(MATH.acos(argument)));
        }

        return h0;
    }

    void approxSunRiseAndSet(double[] mRts, double h0) {
        double h0Dfrac = h0 / 360.0;

        mRts[1] = limitZero2one(mRts[0] - h0Dfrac);
        mRts[2] = limitZero2one(mRts[0] + h0Dfrac);
        mRts[0] = limitZero2one(mRts[0]);

    }

    void approxSalatTimes(double[] mRts, double[] h0) {
        mRts[SUNRISE] = limitZero2one(mRts[SUNTRANSIT] - h0[SUNRISE] / 360.0);
        mRts[SUNSET] = limitZero2one(mRts[SUNTRANSIT] + h0[SUNSET] / 360.0);
        mRts[ASR_SHAFI] = limitZero2one(mRts[SUNTRANSIT] + h0[ASR_SHAFI] / 360.0);
        mRts[ASR_HANEFI] = limitZero2one(mRts[SUNTRANSIT] + h0[ASR_HANEFI] / 360.0);
        mRts[FAJR] = limitZero2one(mRts[SUNTRANSIT] - h0[FAJR] / 360.0);
        mRts[ISHA] = limitZero2one(mRts[SUNTRANSIT] + h0[ISHA] / 360.0);
        mRts[SUNTRANSIT] = limitZero2one(mRts[SUNTRANSIT]);

    }

    void approxKerahatTimes(double[] mRts, double[] h0) {
        mRts[FAJR_] = limitZero2one(mRts[SUNTRANSIT_] - h0[FAJR_] / 360.0);
        mRts[ISRAK] = limitZero2one(mRts[SUNTRANSIT_] - h0[ISRAK] / 360.0);
        mRts[ASRHANEFI] = limitZero2one(mRts[SUNTRANSIT_] + h0[ASRHANEFI] / 360.0);
        mRts[ISFIRAR] = limitZero2one(mRts[SUNTRANSIT_] + h0[ISFIRAR] / 360.0);
        mRts[SUNSET] = limitZero2one(mRts[SUNTRANSIT_] + h0[SUNSET] / 360.0);
        mRts[SUNTRANSIT_] = limitZero2one(mRts[SUNTRANSIT_]);

    }

    double rtsAlphaDeltaPrime(double[] ad, double n) {
        double a = ad[1] - ad[0];
        double b = ad[2] - ad[1];

        if (Math.abs(a) >= 2.0) {
            a = limitZero2one(a);
        }
        if (Math.abs(b) >= 2.0) {
            b = limitZero2one(b);
        }

        return ad[1] + n * (a + b + (b - a) * n) / 2.0;
    }

    double Interpolate(double n, double[] Y) {
        double a = Y[1] - Y[0];
        double b = Y[2] - Y[1];
        double c = Y[0] + Y[2] - 2 * Y[1];

        return Y[1] + n / 2 * (a + b + n * c);
    }

    double rtsSunAltitude(double latitude, double δPrime, double hPrime) {
        double latitudeRad = Math.toRadians(latitude);
        double δPrimeRad = Math.toRadians(δPrime);

        return Math.toDegrees(MATH.asin(Math.sin(latitudeRad) * Math.sin(δPrimeRad)
                + Math.cos(latitudeRad) * Math.cos(δPrimeRad) * Math.cos(Math.toRadians(hPrime))));
    }

    double sunRiseAndSet(double[] mRts, double[] hRts, double[] δPrime, double latitude,
                         double[] hPrime, double h0Prime, int sun) {
        return mRts[sun] + (hRts[sun] - h0Prime)
                / (360.0 * Math.cos(Math.toRadians(δPrime[sun])) * Math.cos(Math.toRadians(latitude)) * Math.sin(Math.toRadians(hPrime[sun])));
    }

    public Equatorial calculateSunEquatorialCoordinates(double jd, double ΔT) {
        double jce, jme, jde, Δψ, ε, r, l, β, theta, Δτ, λ, b, Δε, ε0;
        double α, δ;
        double[] x = new double[5];
        //jc=getJulianCentury(jd);
        jde = AstroLib.getJulianEphemerisDay(jd, ΔT);
        jce = AstroLib.getJulianEphemerisCentury(jde);
        jme = AstroLib.getJulianEphemerisMillennium(jce);
        // jde=getJulianEphemerisDay(jd,ΔT);
        x[0] = meanElongationMoonSun(jce);
        x[1] = meanAnomalySun(jce);
        x[2] = meanAnomalyMoon(jce);
        x[3] = argumentLatitudeMoon(jce);
        x[4] = ascendingLongitudeMoon(jce);
        ε0 = eclipticMeanObliquity(jme);
        Δε = nutationObliquity(jce, x);
        Δψ = nutationLongitude(jce, x);
        ε = eclipticTrueObliquity(Δε, ε0);
        r = earthRadiusVector(jme);
        l = earthHeliocentricLongitude(jme);
        theta = geocentricLongitude(l);
        Δτ = aberrationCorrection(r);
        λ = apparentSunLongitude(theta, Δψ, Δτ);
        // ν0= greenwichMeanSiderealTime (jd,jc);
        //ν= greenwichSiderealTime (ν0,Δψ,ε);
        b = earthHeliocentricLatitude(jme);
        β = getGeocentricLatitude(b);
        α = geocentricRightAscension(λ, ε, β);
        δ = geocentricDeclination(λ, ε, β);
        double Δ = 149597887.5;
        return new Equatorial(α, δ, Δ);

    }

    Equatorial calculateSunEquatorialCoordinates(Ecliptic sunPosEc, double jd, double ΔT) {
        double jce, jme, jde, ε, Δε, ε0;
        double α, δ;
        double[] x = new double[5];

        jde = AstroLib.getJulianEphemerisDay(jd, ΔT);
        jce = AstroLib.getJulianEphemerisCentury(jde);
        jme = AstroLib.getJulianEphemerisMillennium(jce);

        x[0] = meanElongationMoonSun(jce);
        x[1] = meanAnomalySun(jce);
        x[2] = meanAnomalyMoon(jce);
        x[3] = argumentLatitudeMoon(jce);
        x[4] = ascendingLongitudeMoon(jce);
        ε0 = eclipticMeanObliquity(jme);
        Δε = nutationObliquity(jce, x);
        ε = eclipticTrueObliquity(Δε, ε0);

        α = geocentricRightAscension(sunPosEc.λ, ε, sunPosEc.β);
        δ = geocentricDeclination(sunPosEc.λ, ε, sunPosEc.β);
        double Δ = 149597887.5;
        return new Equatorial(α, δ, Δ);


    }

    public Ecliptic calculateSunEclipticCoordinatesAstronomic(double jd, double ΔT) {
        double jce, jme, jde, l, β, theta, b;
        jde = AstroLib.getJulianEphemerisDay(jd, ΔT);
        jce = AstroLib.getJulianEphemerisCentury(jde);
        jme = AstroLib.getJulianEphemerisMillennium(jce);
        l = earthHeliocentricLongitude(jme);
        theta = geocentricLongitude(l);
        b = earthHeliocentricLatitude(jme);
        β = getGeocentricLatitude(b);
        return new Ecliptic(theta, β);

    }
}


