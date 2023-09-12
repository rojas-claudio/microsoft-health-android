package com.microsoft.kapp.services.healthandfitness.models;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class WorkoutsResponseEnvelope {
    @SerializedName("histogram")
    private HistogramEntry[] mHistogramEntries;
    @SerializedName("fitness")
    WorkoutsResponse mResponse;

    public WorkoutsResponse getResponse() {
        return this.mResponse;
    }

    public void setResponse(WorkoutsResponse response) {
        this.mResponse = response;
    }

    public HistogramEntry[] getHistogramEntries() {
        return this.mHistogramEntries;
    }

    public void setHistogramEntries(HistogramEntry[] mHistogramEntries) {
        this.mHistogramEntries = mHistogramEntries;
    }
}
