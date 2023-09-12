package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class Partners implements Parcelable {
    public static final Parcelable.Creator<Partners> CREATOR = new Parcelable.Creator<Partners>() { // from class: com.microsoft.krestsdk.models.Partners.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Partners createFromParcel(Parcel in) {
            return new Partners(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Partners[] newArray(int size) {
            return new Partners[size];
        }
    };
    @SerializedName("Partners")
    private List<Partner> mPartners;

    public List<Partner> getPartners() {
        return this.mPartners;
    }

    public void setName(List<Partner> partners) {
        this.mPartners = partners;
    }

    protected Partners(Parcel in) {
        if (in.readByte() == 1) {
            this.mPartners = new ArrayList();
            in.readList(this.mPartners, Partner.class.getClassLoader());
            return;
        }
        this.mPartners = null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        if (this.mPartners == null) {
            dest.writeByte((byte) 0);
            return;
        }
        dest.writeByte((byte) 1);
        dest.writeList(this.mPartners);
    }
}
