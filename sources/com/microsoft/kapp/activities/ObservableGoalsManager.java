package com.microsoft.kapp.activities;

import com.microsoft.krestsdk.models.GoalType;
/* loaded from: classes.dex */
public interface ObservableGoalsManager {
    void addOnGoalsChangedListenerWeafRef(GoalType goalType, OnGoalsChangedListener onGoalsChangedListener);

    void deleteOnGoalsChangedListenerWeafRef(GoalType goalType, OnGoalsChangedListener onGoalsChangedListener);

    void notifyDataChange(GoalType goalType);
}
