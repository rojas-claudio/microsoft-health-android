package com.microsoft.krestsdk.models;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class ODataResponse<T> {
    @SerializedName("odata.metadata")
    private String mMetadata;
    @SerializedName("value")
    private T mValue;

    public String getMetadata() {
        return this.mMetadata;
    }

    public void setMetadata(String metadata) {
        this.mMetadata = metadata;
    }

    public T getValue() {
        return this.mValue;
    }

    public void setValue(T value) {
        this.mValue = value;
    }
}
