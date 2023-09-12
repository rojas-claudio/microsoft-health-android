package com.microsoft.kapp.models;

import android.content.Context;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class Velocity {
    private Length mDistance;
    private TimeSpan mTime;

    public Velocity(TimeSpan time, Length distance) {
        Validate.notNull(time, "time");
        Validate.notNull(distance, "distance");
        this.mTime = time;
        this.mDistance = distance;
    }

    public static Velocity fromMillisecondsPerMeter(double millisecondsPerMeter) {
        return new Velocity(TimeSpan.fromMilliseconds(millisecondsPerMeter), Length.fromMeters(1));
    }

    public TimeSpan getTime() {
        return this.mTime;
    }

    public Length getDistance() {
        return this.mDistance;
    }

    public String format(Context context, boolean isMetric) {
        String regionalUnit = getRegionalUnit(context, isMetric);
        TimeSpan timePerUnit = getTimePerRegionalUnit(isMetric);
        return String.format("%s/%s", timePerUnit.formatTime(), regionalUnit);
    }

    public static String getRegionalUnit(Context context, boolean isMetric) {
        if (isMetric) {
            String regionalUnit = context.getResources().getString(R.string.kilometers_abbreviation);
            return regionalUnit;
        }
        String regionalUnit2 = context.getResources().getString(R.string.miles_abbreviation);
        return regionalUnit2;
    }

    public TimeSpan getTimePerRegionalUnit(boolean isMetric) {
        double distanceInRegionalUnit;
        double millisecondsPerUnit = Constants.SPLITS_ACCURACY;
        if (isMetric) {
            distanceInRegionalUnit = this.mDistance.getTotalKilometers();
        } else {
            distanceInRegionalUnit = this.mDistance.getTotalMiles();
        }
        double milliseconds = this.mTime.getTotalMilliseconds();
        if (distanceInRegionalUnit > Constants.SPLITS_ACCURACY) {
            millisecondsPerUnit = milliseconds / distanceInRegionalUnit;
        }
        return TimeSpan.fromMilliseconds(millisecondsPerUnit);
    }
}
