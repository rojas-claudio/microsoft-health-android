package com.microsoft.kapp.guidedworkout;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import com.microsoft.krestsdk.models.WorkoutPlanBrowseDetails;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class WorkoutPlanSummaryDetails implements Parcelable {
    public static final Parcelable.Creator<WorkoutPlanSummaryDetails> CREATOR = new Parcelable.Creator<WorkoutPlanSummaryDetails>() { // from class: com.microsoft.kapp.guidedworkout.WorkoutPlanSummaryDetails.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutPlanSummaryDetails createFromParcel(Parcel in) {
            return new WorkoutPlanSummaryDetails(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutPlanSummaryDetails[] newArray(int size) {
            return new WorkoutPlanSummaryDetails[size];
        }
    };
    boolean mIsFavorite;
    WorkoutPlanBrowseDetails mWorkoutPlanBrowseDetails;
    String mWorkoutPlanId;

    public WorkoutPlanSummaryDetails() {
    }

    public WorkoutPlanSummaryDetails(String workoutPlanId, WorkoutPlanBrowseDetails workoutPlanBrowseDetails) {
        this.mWorkoutPlanId = workoutPlanId;
        this.mWorkoutPlanBrowseDetails = workoutPlanBrowseDetails;
    }

    public WorkoutPlanSummaryDetails(WorkoutSummary summary) {
        if (summary != null) {
            this.mWorkoutPlanBrowseDetails = new WorkoutPlanBrowseDetails();
            this.mWorkoutPlanBrowseDetails.setId(0);
            this.mWorkoutPlanBrowseDetails.setName(summary.getName());
            this.mWorkoutPlanBrowseDetails.setLevel(summary.getLevel());
            this.mWorkoutPlanBrowseDetails.setDuration(Integer.valueOf(summary.getDurationMinutes()));
            this.mWorkoutPlanBrowseDetails.setPath(summary.getImageUrl());
            this.mWorkoutPlanBrowseDetails.setIsCustom(summary.getIsCustomWorkout());
            this.mWorkoutPlanBrowseDetails.setPublishDate(summary.getPublishDate());
            this.mWorkoutPlanId = summary.getId();
        }
    }

    public WorkoutPlanSummaryDetails(FavoriteWorkoutPlan favoriteWorkoutPlan) {
        if (favoriteWorkoutPlan != null) {
            this.mWorkoutPlanBrowseDetails = favoriteWorkoutPlan.getWorkoutPlanBrowseDetails();
            this.mWorkoutPlanId = favoriteWorkoutPlan.getWorkoutPlanId();
            this.mIsFavorite = true;
        }
    }

    public String getWorkoutPlanId() {
        return this.mWorkoutPlanId;
    }

    public void setWorkoutPlanId(String workoutPlanId) {
        this.mWorkoutPlanId = workoutPlanId;
    }

    public WorkoutPlanBrowseDetails getWorkoutPlanBrowseDetails() {
        return this.mWorkoutPlanBrowseDetails;
    }

    public void setWorkoutPlanBrowseDetails(WorkoutPlanBrowseDetails workoutPlanBrowseDetails) {
        this.mWorkoutPlanBrowseDetails = workoutPlanBrowseDetails;
    }

    public String getPath() {
        if (this.mWorkoutPlanBrowseDetails == null) {
            return null;
        }
        return this.mWorkoutPlanBrowseDetails.getPath();
    }

    public String getName() {
        if (this.mWorkoutPlanBrowseDetails == null) {
            return null;
        }
        return this.mWorkoutPlanBrowseDetails.getName();
    }

    public String getLevel() {
        if (this.mWorkoutPlanBrowseDetails == null) {
            return null;
        }
        return this.mWorkoutPlanBrowseDetails.getLevel();
    }

    public Integer getDuration() {
        if (this.mWorkoutPlanBrowseDetails == null) {
            return null;
        }
        return this.mWorkoutPlanBrowseDetails.getDuration();
    }

    public Integer getId() {
        if (this.mWorkoutPlanBrowseDetails == null) {
            return null;
        }
        return this.mWorkoutPlanBrowseDetails.getId();
    }

    public boolean getIsFavorite() {
        return this.mIsFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.mIsFavorite = isFavorite;
    }

    public boolean getIsCustom() {
        if (this.mWorkoutPlanBrowseDetails == null) {
            return false;
        }
        return this.mWorkoutPlanBrowseDetails.getIsCustom();
    }

    public DateTime getPublishDate() {
        if (this.mWorkoutPlanBrowseDetails == null) {
            return null;
        }
        return this.mWorkoutPlanBrowseDetails.getPublishDate();
    }

    public WorkoutPlanSummaryDetails(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mWorkoutPlanId);
        dest.writeInt(this.mIsFavorite ? 1 : 0);
        dest.writeParcelable(this.mWorkoutPlanBrowseDetails, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mWorkoutPlanId = in.readString();
        this.mIsFavorite = in.readInt() == 1;
        this.mWorkoutPlanBrowseDetails = (WorkoutPlanBrowseDetails) in.readParcelable(WorkoutPlanBrowseDetails.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
