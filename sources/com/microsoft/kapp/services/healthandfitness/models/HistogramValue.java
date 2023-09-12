package com.microsoft.kapp.services.healthandfitness.models;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class HistogramValue {
    @SerializedName("count")
    private String mCount;
    @SerializedName("id")
    private String mId;
    @SerializedName(WorkoutSummary.NAME)
    private String mName;

    public String getCount() {
        return this.mCount;
    }

    public void setCount(String mCount) {
        this.mCount = mCount;
    }

    public String getId() {
        return this.mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}
