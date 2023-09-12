package com.microsoft.kapp.utils;
/* loaded from: classes.dex */
public class ConversionUtils {
    public static double CentimeterPerSecondToMilesPerHour(double cmPerSecond) {
        return 0.022369362906d * cmPerSecond;
    }

    public static double CentimeterPerSecondToKilometersPerHour(double cmPerSecond) {
        return 0.036d * cmPerSecond;
    }

    public static double MilesPerHourToCentimetersPerSecond(double milesPerHour) {
        return milesPerHour / 0.022369362906d;
    }

    public static double KilometersPerHourToCentimetersPerSecond(double kmPerHour) {
        return kmPerHour / 0.036d;
    }

    public static double CentimetersToKilometers(double cm) {
        return cm / 100000.0d;
    }

    public static double CentimetersToMiles(double cm) {
        return cm / 160934.0d;
    }

    public static double CentimetersToFeet(double cm) {
        return 0.0328084d * cm;
    }

    public static double CentimetersToMeters(double cm) {
        return 0.01d * cm;
    }

    public static double KilometersToCentimeters(double km) {
        return 100000.0d * km;
    }

    public static double MilesToCentimeters(double mi) {
        return 160934.0d * mi;
    }

    public static double MilesToKilometers(double mi) {
        return 1.60934d * mi;
    }

    public static double FeetToCentimeters(int feet) {
        return feet / 0.0328084d;
    }

    public static double MetersToCentimeters(int meters) {
        return meters / 0.01d;
    }

    public static double DistanceToCm(double distance, boolean isMetric) {
        return isMetric ? KilometersToCentimeters(distance) : MilesToCentimeters(distance);
    }

    public static double CmToDistance(double distanceCm, boolean isMetric) {
        return isMetric ? CentimetersToKilometers(distanceCm) : CentimetersToMiles(distanceCm);
    }

    public static int CentimetersToYards(double distanceCM) {
        return (int) (((int) distanceCM) * 0.0109361d);
    }

    public static int SecondsToMinutes(int seconds) {
        return seconds / 60;
    }
}
