package com.shinobicontrols.kcompanionapp.properties;

import android.content.Context;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public class ChartConfig {
    private int mAge;
    private Context mContext;
    private boolean mIsMetric;
    private int mMaxHeartRate;

    public ChartConfig(Context context, int age, int maxHeartRate, boolean isMetric) {
        Validate.notNull(context, "app context", new Object[0]);
        this.mContext = context;
        this.mAge = age;
        this.mMaxHeartRate = maxHeartRate;
        this.mIsMetric = isMetric;
    }

    public Context getContext() {
        return this.mContext;
    }

    public int getAge() {
        return this.mAge;
    }

    public boolean isMetric() {
        return this.mIsMetric;
    }

    public int getMaxHeartRate() {
        return this.mMaxHeartRate;
    }
}
