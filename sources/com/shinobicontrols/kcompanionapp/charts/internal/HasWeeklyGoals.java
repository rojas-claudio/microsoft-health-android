package com.shinobicontrols.kcompanionapp.charts.internal;

import java.util.HashMap;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public interface HasWeeklyGoals {
    public static final String WEEKLY_GOALS = "WeeklyGoals";

    WeeklyGoals getWeeklyGoals();

    void setWeeklyGoals(HashMap<DateTime, Double> hashMap);
}
