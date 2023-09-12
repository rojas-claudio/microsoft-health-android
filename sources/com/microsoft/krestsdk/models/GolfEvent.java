package com.microsoft.krestsdk.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.Comparator;
/* loaded from: classes.dex */
public class GolfEvent extends UserEvent implements Parcelable {
    public static final Parcelable.Creator<GolfEvent> CREATOR = new Parcelable.Creator<GolfEvent>() { // from class: com.microsoft.krestsdk.models.GolfEvent.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfEvent createFromParcel(Parcel in) {
            return new GolfEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfEvent[] newArray(int size) {
            return new GolfEvent[size];
        }
    };
    @SerializedName("AverageHeartRate")
    private int mAverageHeartRate;
    @SerializedName("CourseID")
    private String mCourseID;
    @SerializedName("CourseMapVersion")
    private String mCourseMapVersion;
    @SerializedName("CourseName")
    private String mCourseName;
    @SerializedName("CoursePar")
    private int mCoursePar;
    @SerializedName("GPSState")
    private int mGPSState;
    @SerializedName("LongestDriveInCm")
    private int mLongestDriveInCm;
    @SerializedName("LowestHeartRate")
    private int mLowestHeartRate;
    @SerializedName("PaceOfPlayInSeconds")
    private int mPaceOfPlayInSeconds;
    @SerializedName("ParForHolesPlayed")
    private int mParForHolesPlayed;
    @SerializedName("ParOrBetterCount")
    private int mParOrBetterCount;
    @SerializedName("PeakHeartRate")
    private int mPeakHeartRate;
    @SerializedName("Sequences")
    private GolfEventHoleSequence[] mSequences;
    private GolfEventHoleSequence[] mSortedSequences;
    @SerializedName("TeeNameSelected")
    private String mTeeNameSelected;
    @SerializedName("TotalDistanceWalkedInCm")
    private int mTotalDistanceWalkedInCm;
    @SerializedName("TotalHolesPlayed")
    private int mTotalHolesPlayed;
    @SerializedName("TotalScore")
    private int mTotalScore;
    @SerializedName("TotalStepCount")
    private int mTotalStepCount;

    public String getCourseID() {
        return this.mCourseID;
    }

    public void setCourseID(String courseID) {
        this.mCourseID = courseID;
    }

    public String getCourseMapVersion() {
        return this.mCourseMapVersion;
    }

    public void setCourseMapVersion(String courseMapVersion) {
        this.mCourseMapVersion = courseMapVersion;
    }

    public String getCourseName() {
        return this.mCourseName;
    }

    public void setCourseName(String courseName) {
        this.mCourseName = courseName;
    }

    public int getCoursePar() {
        return this.mCoursePar;
    }

    public void setCoursePar(int coursePar) {
        this.mCoursePar = coursePar;
    }

    public int getTotalHolesPlayed() {
        return this.mTotalHolesPlayed;
    }

    public void setTotalHolesPlayed(int totalHolesPlayed) {
        this.mTotalHolesPlayed = totalHolesPlayed;
    }

    public int getTotalScore() {
        return this.mTotalScore;
    }

    public void setTotalScore(int totalScore) {
        this.mTotalScore = totalScore;
    }

    public int getAverageHeartRate() {
        return this.mAverageHeartRate;
    }

    public void setAverageHeartRate(int averageHeartRate) {
        this.mAverageHeartRate = averageHeartRate;
    }

    public int getLowestHeartRate() {
        return this.mLowestHeartRate;
    }

    public void setLowestHeartRate(int lowestHeartRate) {
        this.mLowestHeartRate = lowestHeartRate;
    }

    public int getPeakHeartRate() {
        return this.mPeakHeartRate;
    }

    public void setPeakHeartRate(int peakHeartRate) {
        this.mPeakHeartRate = peakHeartRate;
    }

    public int getParOrBetterCount() {
        return this.mParOrBetterCount;
    }

    public void setParOrBetterCount(int parOrBetterCount) {
        this.mParOrBetterCount = parOrBetterCount;
    }

    public int getLongestDriveInCm() {
        return this.mLongestDriveInCm;
    }

    public void setLongestDriveInCm(int longestDriveInCm) {
        this.mLongestDriveInCm = longestDriveInCm;
    }

    public int getPaceOfPlayInSeconds() {
        return this.mPaceOfPlayInSeconds;
    }

    public void setPaceOfPlayInSeconds(int paceOfPlayInSeconds) {
        this.mPaceOfPlayInSeconds = paceOfPlayInSeconds;
    }

    public String getTeeNameSelected() {
        return this.mTeeNameSelected;
    }

    public void setTeeNameSelected(String teeNameSelected) {
        this.mTeeNameSelected = teeNameSelected;
    }

    public int getTotalStepCount() {
        return this.mTotalStepCount;
    }

    public void setTotalStepCount(int totalStepCount) {
        this.mTotalStepCount = totalStepCount;
    }

    public int getTotalDistanceWalkedInCm() {
        return this.mTotalDistanceWalkedInCm;
    }

    public void setTotalDistanceWalkedInCm(int totalDistanceWalkedInCm) {
        this.mTotalDistanceWalkedInCm = totalDistanceWalkedInCm;
    }

    public int getGPSState() {
        return this.mGPSState;
    }

    public void setGPSState(int GPSState) {
        this.mGPSState = GPSState;
    }

    public GolfEventHoleSequence[] getSequences() {
        return this.mSequences;
    }

    public void setSequences(GolfEventHoleSequence[] sequences) {
        this.mSequences = sequences;
    }

    public int getParForHolesPlayed() {
        return this.mParForHolesPlayed;
    }

    public void setParForHolesPlayed(int parForHolesPlayed) {
        this.mParForHolesPlayed = parForHolesPlayed;
    }

    @Override // com.microsoft.krestsdk.models.UserEvent
    public Spannable getMainMetric(Context context, boolean isMetric) {
        return SpannableString.valueOf(String.valueOf(getTotalScore()));
    }

    public GolfEventHoleSequence[] getSortedSequences() {
        if (this.mSortedSequences != null || this.mSequences == null || this.mSequences.length <= 0) {
            return this.mSortedSequences;
        }
        this.mSortedSequences = new GolfEventHoleSequence[this.mSequences.length];
        int i = 0;
        GolfEventHoleSequence[] arr$ = this.mSequences;
        for (GolfEventHoleSequence sequence : arr$) {
            this.mSortedSequences[i] = sequence;
            i++;
        }
        Arrays.sort(this.mSortedSequences, new Comparator<GolfEventHoleSequence>() { // from class: com.microsoft.krestsdk.models.GolfEvent.1
            @Override // java.util.Comparator
            public int compare(GolfEventHoleSequence lhs, GolfEventHoleSequence rhs) {
                if (lhs == null && rhs == null) {
                    return 0;
                }
                if (lhs == null) {
                    return 1;
                }
                if (rhs == null) {
                    return -1;
                }
                return lhs.getHoleNumber() - rhs.getHoleNumber();
            }
        });
        return this.mSortedSequences;
    }

    public String getDisplayName() {
        return TextUtils.isEmpty(getName()) ? getCourseName() : getName();
    }

    protected GolfEvent(Parcel in) {
        super(in);
        this.mCourseID = in.readString();
        this.mCourseMapVersion = in.readString();
        this.mCourseName = in.readString();
        this.mCoursePar = in.readInt();
        this.mParForHolesPlayed = in.readInt();
        this.mTotalHolesPlayed = in.readInt();
        this.mTotalScore = in.readInt();
        this.mParOrBetterCount = in.readInt();
        this.mLongestDriveInCm = in.readInt();
        this.mPaceOfPlayInSeconds = in.readInt();
        this.mTeeNameSelected = in.readString();
        this.mTotalStepCount = in.readInt();
        this.mTotalDistanceWalkedInCm = in.readInt();
        this.mGPSState = in.readInt();
        this.mSequences = (GolfEventHoleSequence[]) in.createTypedArray(GolfEventHoleSequence.CREATOR);
        this.mLowestHeartRate = in.readInt();
        this.mAverageHeartRate = in.readInt();
        this.mPeakHeartRate = in.readInt();
    }

    @Override // com.microsoft.krestsdk.models.UserEvent, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mCourseID);
        dest.writeString(this.mCourseMapVersion);
        dest.writeString(this.mCourseName);
        dest.writeInt(this.mCoursePar);
        dest.writeInt(this.mParForHolesPlayed);
        dest.writeInt(this.mTotalHolesPlayed);
        dest.writeInt(this.mTotalScore);
        dest.writeInt(this.mParOrBetterCount);
        dest.writeInt(this.mLongestDriveInCm);
        dest.writeInt(this.mPaceOfPlayInSeconds);
        dest.writeString(this.mTeeNameSelected);
        dest.writeInt(this.mTotalStepCount);
        dest.writeInt(this.mTotalDistanceWalkedInCm);
        dest.writeInt(this.mGPSState);
        dest.writeTypedArray(this.mSequences, flags);
        dest.writeInt(this.mLowestHeartRate);
        dest.writeInt(this.mAverageHeartRate);
        dest.writeInt(this.mPeakHeartRate);
    }
}
