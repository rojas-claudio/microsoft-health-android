package com.microsoft.kapp.models;

import android.content.Context;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class Weight {
    private static final int GRAMS_PER_KILO = 1000;
    private static final double GRAMS_PER_POUND = 453.59d;
    private double mGrams;

    private Weight(double grams) {
        if (grams < Constants.SPLITS_ACCURACY) {
            throw new IllegalArgumentException("grams cannot be negative");
        }
        this.mGrams = grams;
    }

    public String format(Context context, boolean isMetric) {
        return isMetric ? context.getString(R.string.kilograms_abbreviation, Integer.valueOf(getKilograms())) : context.getString(R.string.pounds_abbreviation, Integer.valueOf(getPounds()));
    }

    public double getGrams() {
        return this.mGrams;
    }

    public int getPounds() {
        return (int) Math.round(this.mGrams / GRAMS_PER_POUND);
    }

    public int getKilograms() {
        return (int) Math.round(this.mGrams / 1000.0d);
    }

    public static Weight fromGrams(int grams) {
        return new Weight(grams);
    }

    public static Weight fromPounds(int pounds) {
        return new Weight(pounds * GRAMS_PER_POUND);
    }

    public static Weight fromKilograms(int kilograms) {
        return new Weight(kilograms * 1000);
    }
}
