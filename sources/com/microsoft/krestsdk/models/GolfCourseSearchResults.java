package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class GolfCourseSearchResults implements Parcelable {
    public static final Parcelable.Creator<GolfCourseSearchResults> CREATOR = new Parcelable.Creator<GolfCourseSearchResults>() { // from class: com.microsoft.krestsdk.models.GolfCourseSearchResults.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfCourseSearchResults createFromParcel(Parcel in) {
            return new GolfCourseSearchResults(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfCourseSearchResults[] newArray(int size) {
            return new GolfCourseSearchResults[size];
        }
    };
    @SerializedName("facilities")
    private List<GolfCourse> mFacilities;

    public List<GolfCourse> getFacilities() {
        return this.mFacilities;
    }

    public void setFacilities(List<GolfCourse> mFacilities) {
        this.mFacilities = mFacilities;
    }

    public GolfCourseSearchResults() {
    }

    protected GolfCourseSearchResults(Parcel in) {
        if (in.readByte() == 1) {
            this.mFacilities = new ArrayList();
            in.readList(this.mFacilities, GolfCourse.class.getClassLoader());
            return;
        }
        this.mFacilities = null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        if (this.mFacilities == null) {
            dest.writeByte((byte) 0);
            return;
        }
        dest.writeByte((byte) 1);
        dest.writeList(this.mFacilities);
    }
}
