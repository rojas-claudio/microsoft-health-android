package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class DefaultStock implements Parcelable {
    public static final Parcelable.Creator<DefaultStock> CREATOR = new Parcelable.Creator<DefaultStock>() { // from class: com.microsoft.krestsdk.models.DefaultStock.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DefaultStock createFromParcel(Parcel in) {
            return new DefaultStock(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DefaultStock[] newArray(int size) {
            return new DefaultStock[size];
        }
    };
    @SerializedName("id")
    private String mId;
    @SerializedName("symbol")
    private String mSymbol;

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getSymbol() {
        return this.mSymbol;
    }

    public void setSymbol(String symbol) {
        this.mSymbol = symbol;
    }

    protected DefaultStock(Parcel in) {
        this.mId = in.readString();
        this.mSymbol = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mSymbol);
    }
}
