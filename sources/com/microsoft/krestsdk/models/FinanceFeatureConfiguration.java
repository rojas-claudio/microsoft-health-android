package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class FinanceFeatureConfiguration extends FeatureConfiguration implements Parcelable {
    public static final Parcelable.Creator<FinanceFeatureConfiguration> CREATOR = new Parcelable.Creator<FinanceFeatureConfiguration>() { // from class: com.microsoft.krestsdk.models.FinanceFeatureConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FinanceFeatureConfiguration createFromParcel(Parcel in) {
            return new FinanceFeatureConfiguration(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FinanceFeatureConfiguration[] newArray(int size) {
            return new FinanceFeatureConfiguration[size];
        }
    };
    @SerializedName("defaultStockList")
    private DefaultStock[] mDefaultStockList;
    @SerializedName("serviceUrl")
    private String mServiceUrl;

    @Override // com.microsoft.krestsdk.models.FeatureConfiguration, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getServiceUrl() {
        return this.mServiceUrl;
    }

    public void setServiceUrl(String mServiceUrl) {
        this.mServiceUrl = mServiceUrl;
    }

    public DefaultStock[] getDefaultStockList() {
        return this.mDefaultStockList;
    }

    public void setDefaultStockList(DefaultStock[] mDefaultStockList) {
        this.mDefaultStockList = mDefaultStockList;
    }

    @Override // com.microsoft.krestsdk.models.FeatureConfiguration, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getServiceUrl());
        dest.writeTypedArray(getDefaultStockList(), flags);
    }

    protected FinanceFeatureConfiguration(Parcel in) {
        super(in);
        setServiceUrl(in.readString());
        setDefaultStockList((DefaultStock[]) in.createTypedArray(DefaultStock.CREATOR));
    }
}
