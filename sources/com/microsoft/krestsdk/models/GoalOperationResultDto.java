package com.microsoft.krestsdk.models;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class GoalOperationResultDto {
    @SerializedName("ErrorMessage")
    private String mErrorMessage;
    @SerializedName("ErrorSource")
    private String mErrorSource;
    @SerializedName("IsSucceed")
    private boolean mIsSucceeded;

    public boolean isSucceeded() {
        return this.mIsSucceeded;
    }

    public String getErrorMessage() {
        return this.mErrorMessage;
    }

    public String getErrorSource() {
        return this.mErrorSource;
    }
}
