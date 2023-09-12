package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class Evidence implements Parcelable {
    public static final Parcelable.Creator<Evidence> CREATOR = new Parcelable.Creator<Evidence>() { // from class: com.microsoft.krestsdk.models.Evidence.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Evidence createFromParcel(Parcel in) {
            return new Evidence(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Evidence[] newArray(int size) {
            return new Evidence[size];
        }
    };
    @SerializedName("Insight")
    private RaisedInsight mRaisedInsight;
    @SerializedName("RaisedInsightId")
    private String mRaisedInsightId;
    @SerializedName("Role")
    private String mRole;

    public String getRole() {
        return this.mRole;
    }

    public void setRole(String role) {
        this.mRole = role;
    }

    public String getRaisedInsightId() {
        return this.mRaisedInsightId;
    }

    public void setRaisedInsightId(String raisedInsightId) {
        this.mRaisedInsightId = raisedInsightId;
    }

    public RaisedInsight getRaisedInsight() {
        return this.mRaisedInsight;
    }

    public void setRaisedInsight(RaisedInsight raisedInsight) {
        this.mRaisedInsight = raisedInsight;
    }

    protected Evidence(Parcel in) {
        this.mRole = in.readString();
        this.mRaisedInsightId = in.readString();
        this.mRaisedInsight = (RaisedInsight) in.readParcelable(getClass().getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mRole);
        dest.writeString(this.mRaisedInsightId);
        dest.writeParcelable(this.mRaisedInsight, flags);
    }
}
