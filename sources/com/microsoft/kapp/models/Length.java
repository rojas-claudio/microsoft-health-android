package com.microsoft.kapp.models;

import android.content.Context;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import java.util.Locale;
/* loaded from: classes.dex */
public class Length {
    private static final int INCHES_PER_FOOT = 12;
    private static final int MM_PER_CM = 10;
    private static final double MM_PER_INCH = 25.4d;
    private static final double MM_PER_KM = 1000000.0d;
    private static final int MM_PER_METER = 1000;
    private static final double MM_PER_MILE = 1609344.0d;
    private double mMillimeters;

    private Length(double millimeters) {
        this.mMillimeters = millimeters;
    }

    public Length add(Length length) {
        Validate.notNull(length, "length");
        return new Length(getMillimeters() + length.getMillimeters());
    }

    public double getMillimeters() {
        return this.mMillimeters;
    }

    public int getFeet() {
        double totalInches = this.mMillimeters / MM_PER_INCH;
        int feet = ((int) totalInches) / 12;
        int inches = (int) Math.round(totalInches % 12.0d);
        if (inches == 12) {
            return feet + 1;
        }
        return feet;
    }

    public int getInches() {
        double totalInches = this.mMillimeters / MM_PER_INCH;
        int inches = (int) Math.round(totalInches % 12.0d);
        if (inches == 12) {
            return 0;
        }
        return inches;
    }

    public int getCentimeters() {
        return (int) Math.round(this.mMillimeters / 10.0d);
    }

    public double getTotalMeters() {
        return this.mMillimeters / 1000.0d;
    }

    public double getTotalMiles() {
        return this.mMillimeters / MM_PER_MILE;
    }

    public double getTotalKilometers() {
        return this.mMillimeters / MM_PER_KM;
    }

    public double getTotalDistanceInPreferredUnits(boolean isMetric) {
        return isMetric ? getTotalKilometers() : getTotalMiles();
    }

    public double getTotalFeet() {
        double totalInches = this.mMillimeters / MM_PER_INCH;
        return totalInches / 12.0d;
    }

    public String format(Context context, boolean isMetric) {
        return isMetric ? context.getString(R.string.centimeters_abbreviation, Integer.valueOf(getCentimeters())) : String.format(Locale.getDefault(), "%d%s %d%s", Integer.valueOf(getFeet()), "'", Integer.valueOf(getInches()), "\"");
    }

    public String formatElevation(Context context, boolean isMetric) {
        return isMetric ? context.getString(R.string.meters_abbreviation, Integer.valueOf((int) Math.round(getTotalMeters()))) : context.getString(R.string.feet_abbreviation, Integer.valueOf((int) Math.round(getTotalFeet())));
    }

    public static Length fromImperial(int feet, int inches) {
        return new Length(((feet * 12) + inches) * MM_PER_INCH);
    }

    public static Length fromMillimeters(int millimeters) {
        return new Length(millimeters);
    }

    public static Length fromCentimeters(int centimeters) {
        return new Length(centimeters * 10);
    }

    public static Length fromMeters(int meters) {
        return new Length(meters * 1000);
    }
}
