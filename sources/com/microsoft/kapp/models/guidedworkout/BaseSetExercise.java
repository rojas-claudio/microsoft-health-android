package com.microsoft.kapp.models.guidedworkout;

import android.content.Context;
import com.microsoft.krestsdk.models.CompletionType;
/* loaded from: classes.dex */
public class BaseSetExercise extends BaseGuidedWorkoutItem {
    protected int mCompetionValue;
    protected CompletionType mCompletionType;
    protected Context mContext;
    protected boolean mDoNotCount;
    protected int mExerciseOrdinal;
    protected boolean mIsRest;
    protected String mName;

    public Context getContext() {
        return this.mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public CompletionType getCompletionType() {
        return this.mCompletionType;
    }

    public void setCompletionType(CompletionType completionType) {
        this.mCompletionType = completionType;
    }

    public int getCompetionValue() {
        return this.mCompetionValue;
    }

    public void setCompetionValue(int competionValue) {
        this.mCompetionValue = competionValue;
    }

    public int getExerciseOrdinal() {
        return this.mExerciseOrdinal;
    }

    public void setExerciseOrdinal(int mExerciseOrdinal) {
        this.mExerciseOrdinal = mExerciseOrdinal;
    }

    public boolean isIsRest() {
        return this.mIsRest;
    }

    public void setIsRest(boolean mIsRest) {
        this.mIsRest = mIsRest;
    }

    public boolean isDoNotCount() {
        return this.mDoNotCount;
    }

    public void setDoNotCount(boolean mDoNotCount) {
        this.mDoNotCount = mDoNotCount;
    }
}
