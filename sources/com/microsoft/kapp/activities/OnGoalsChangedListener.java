package com.microsoft.kapp.activities;

import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.krestsdk.models.GoalType;
/* loaded from: classes.dex */
public interface OnGoalsChangedListener {
    boolean isValid();

    void onGoalsUpdated(GoalType goalType, HomeData homeData);
}
