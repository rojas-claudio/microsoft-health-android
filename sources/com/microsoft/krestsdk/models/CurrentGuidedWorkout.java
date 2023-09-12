package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class CurrentGuidedWorkout implements Parcelable {
    public static final Parcelable.Creator<CurrentGuidedWorkout> CREATOR = new Parcelable.Creator<CurrentGuidedWorkout>() { // from class: com.microsoft.krestsdk.models.CurrentGuidedWorkout.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CurrentGuidedWorkout createFromParcel(Parcel in) {
            return new CurrentGuidedWorkout(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CurrentGuidedWorkout[] newArray(int size) {
            return new CurrentGuidedWorkout[size];
        }
    };
    @SerializedName("state")
    private WorkoutState mState;
    @SerializedName("workoutInfo")
    private ScheduledWorkout mWorkoutInfo;

    public CurrentGuidedWorkout() {
    }

    public WorkoutState getState() {
        return this.mState;
    }

    public ScheduledWorkout getWorkoutInfo() {
        return this.mWorkoutInfo;
    }

    protected CurrentGuidedWorkout(Parcel in) {
        this.mState = (WorkoutState) in.readValue(GolfCourseType.class.getClassLoader());
        this.mWorkoutInfo = (ScheduledWorkout) in.readParcelable(ScheduledWorkout.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mState);
        dest.writeParcelable(this.mWorkoutInfo, flags);
    }
}
