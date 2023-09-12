package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Array;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class ExerciseEvent extends ExerciseEventBase {
    public static final Parcelable.Creator<ExerciseEvent> CREATOR = new Parcelable.Creator<ExerciseEvent>() { // from class: com.microsoft.krestsdk.models.ExerciseEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExerciseEvent createFromParcel(Parcel in) {
            return new ExerciseEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExerciseEvent[] newArray(int size) {
            return new ExerciseEvent[size];
        }
    };
    @SerializedName("Sequences")
    private ExerciseEventSequence[] mSequences;

    public ExerciseEvent() {
    }

    public ExerciseEventSequence[] getSequences() {
        return this.mSequences;
    }

    public void setSequences(ExerciseEventSequence[] sequences) {
        this.mSequences = sequences;
    }

    protected ExerciseEvent(Parcel in) {
        super(in);
        this.mSequences = (ExerciseEventSequence[]) in.createTypedArray(ExerciseEventSequence.CREATOR);
    }

    @Override // com.microsoft.krestsdk.models.ExerciseEventBase, com.microsoft.krestsdk.models.UserEvent, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedArray(this.mSequences, flags);
    }

    @Override // com.microsoft.krestsdk.models.ExerciseEventBase
    public UserActivity[] getUpdatedInfoSequenceWithPauseTime() {
        UserActivity[] activities = super.getUpdatedInfoSequenceWithPauseTime();
        if (this.mSequences != null && this.mSequences.length >= 2) {
            DateTime[][] pauseRanges = getPauseRanges();
            int i = 0;
            while (i < activities.length) {
                int start = 0;
                int end = pauseRanges.length;
                UserActivity activity = activities[i];
                UserActivity nextActivity = i < activities.length + (-1) ? activities[i + 1] : null;
                while (true) {
                    if (start < end) {
                        int mid = (start + end) / 2;
                        if (withinRange(activity, nextActivity, pauseRanges, mid)) {
                            activity.setPaused(true);
                            break;
                        } else if (activity.getTimeOfDay().isBefore(pauseRanges[mid][0])) {
                            end = mid;
                        } else {
                            start = mid + 1;
                        }
                    }
                }
                i++;
            }
        }
        return activities;
    }

    public static boolean withinRange(UserActivity activity, UserActivity nextActivity, DateTime[][] pauseRanges, int index) {
        if (activity.getTimeOfDay().isAfter(pauseRanges[index][0]) && activity.getTimeOfDay().isBefore(pauseRanges[index][1])) {
            return true;
        }
        return nextActivity != null && pauseRanges[index][0].isAfter(activity.getTimeOfDay()) && pauseRanges[index][1].isBefore(nextActivity.getTimeOfDay());
    }

    private DateTime[][] getPauseRanges() {
        DateTime[][] pauseRanges = (DateTime[][]) Array.newInstance(DateTime.class, this.mSequences.length - 1, 2);
        for (int j = 0; j < this.mSequences.length - 1; j++) {
            pauseRanges[j][0] = this.mSequences[j].getStartTime().plusSeconds(this.mSequences[j].getDuration());
            pauseRanges[j][1] = this.mSequences[j + 1].getStartTime();
        }
        return pauseRanges;
    }
}
