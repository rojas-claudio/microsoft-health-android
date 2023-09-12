package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class WorkoutPlanBrowseDetails implements Parcelable {
    public static final Parcelable.Creator<WorkoutPlanBrowseDetails> CREATOR = new Parcelable.Creator<WorkoutPlanBrowseDetails>() { // from class: com.microsoft.krestsdk.models.WorkoutPlanBrowseDetails.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutPlanBrowseDetails createFromParcel(Parcel in) {
            return new WorkoutPlanBrowseDetails(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutPlanBrowseDetails[] newArray(int size) {
            return new WorkoutPlanBrowseDetails[size];
        }
    };
    @SerializedName("dur")
    private Integer mDuration;
    @SerializedName("$id")
    private Integer mId;
    @SerializedName("isCustom")
    private boolean mIsCustom;
    @SerializedName("lvl")
    private String mLevel;
    @SerializedName(WorkoutSummary.NAME)
    private String mName;
    @SerializedName("path")
    private String mPath;
    @SerializedName("publishdate")
    private DateTime mPublishDate;

    public Integer getId() {
        return this.mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getLevel() {
        return this.mLevel;
    }

    public void setLevel(String level) {
        this.mLevel = level;
    }

    public Integer getDuration() {
        return this.mDuration;
    }

    public void setDuration(Integer duration) {
        this.mDuration = duration;
    }

    public String getPath() {
        return this.mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public boolean getIsCustom() {
        return this.mIsCustom;
    }

    public void setIsCustom(boolean isCustom) {
        this.mIsCustom = isCustom;
    }

    public DateTime getPublishDate() {
        return this.mPublishDate;
    }

    public void setPublishDate(DateTime publishDate) {
        this.mPublishDate = publishDate;
    }

    public WorkoutPlanBrowseDetails(Parcel source) {
        readFromParcel(source);
    }

    public WorkoutPlanBrowseDetails() {
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId.intValue());
        dest.writeString(this.mName);
        dest.writeString(this.mLevel);
        dest.writeInt(this.mDuration.intValue());
        dest.writeString(this.mPath);
        dest.writeByte((byte) (this.mIsCustom ? 1 : 0));
        dest.writeSerializable(this.mPublishDate);
    }

    public void readFromParcel(Parcel in) {
        this.mId = Integer.valueOf(in.readInt());
        this.mName = in.readString();
        this.mLevel = in.readString();
        this.mDuration = Integer.valueOf(in.readInt());
        this.mPath = in.readString();
        this.mIsCustom = in.readByte() != 0;
        this.mPublishDate = (DateTime) in.readSerializable();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
