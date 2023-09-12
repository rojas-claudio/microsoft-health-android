package com.shinobicontrols.kcompanionapp.charts.internal;
/* loaded from: classes.dex */
public interface HasProfileData {
    public static final String IS_DISTANCE_HEIGHT_METRIC = "IsDistanceHeightMetric";
    public static final String USER_AGE = "UserAge";

    int getUserAge();

    boolean isDistanceHeightMetric();

    void setDistanceHeightMetric(boolean z);

    void setUserAge(int i);
}
