package com.shinobicontrols.kcompanionapp.charts.internal;
/* loaded from: classes.dex */
public class Rounder {
    public static int roundUp(double value, int toTheNearest) {
        if (toTheNearest == 0) {
            throw new IllegalArgumentException("Cannot round to the nearest zero.");
        }
        return (((int) (Math.ceil(value) + (toTheNearest - 1))) / toTheNearest) * toTheNearest;
    }

    public static int roundDown(double value, int toTheNearest) {
        if (toTheNearest == 0) {
            throw new IllegalArgumentException("Cannot round to the nearest zero.");
        }
        return ((int) (Math.floor(value) / toTheNearest)) * toTheNearest;
    }
}
