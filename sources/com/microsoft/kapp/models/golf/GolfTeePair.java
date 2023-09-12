package com.microsoft.kapp.models.golf;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;
/* loaded from: classes.dex */
public class GolfTeePair extends Pair<String, String> implements Parcelable {
    public static final Parcelable.Creator<GolfTeePair> CREATOR = new Parcelable.Creator<GolfTeePair>() { // from class: com.microsoft.kapp.models.golf.GolfTeePair.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfTeePair createFromParcel(Parcel in) {
            return new GolfTeePair(in.readString(), in.readString());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfTeePair[] newArray(int size) {
            return new GolfTeePair[size];
        }
    };

    public GolfTeePair(String teeName, String teeId) {
        super(teeName, teeId);
    }

    public String getTeeName() {
        return (String) this.first;
    }

    public String getTeeId() {
        return (String) this.second;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString((String) this.first);
        dest.writeString((String) this.second);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
