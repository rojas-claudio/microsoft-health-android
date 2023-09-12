package com.microsoft.kapp.services.healthandfitness.models;

import com.facebook.internal.ServerProtocol;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class HistogramEntry {
    @SerializedName(ServerProtocol.DIALOG_PARAM_DISPLAY)
    private String mDisplay;
    @SerializedName("filter")
    private String mFilter;
    @SerializedName("values")
    private HistogramValue[] mValues;

    public String getDisplay() {
        return this.mDisplay;
    }

    public void setDisplay(String mDisplay) {
        this.mDisplay = mDisplay;
    }

    public String getFilter() {
        return this.mFilter;
    }

    public void setFilter(String mFilter) {
        this.mFilter = mFilter;
    }

    public HistogramValue[] getValues() {
        return this.mValues;
    }

    public void setValues(HistogramValue[] mValues) {
        this.mValues = mValues;
    }
}
