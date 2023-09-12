package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class GuidedWorkoutEvent extends ExerciseEventBase {
    public static final Parcelable.Creator<GuidedWorkoutEvent> CREATOR = new Parcelable.Creator<GuidedWorkoutEvent>() { // from class: com.microsoft.krestsdk.models.GuidedWorkoutEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GuidedWorkoutEvent createFromParcel(Parcel in) {
            return new GuidedWorkoutEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GuidedWorkoutEvent[] newArray(int size) {
            return new GuidedWorkoutEvent[size];
        }
    };
    public static final String CompletionTypeJSONMemberName = "CompletionType";
    @SerializedName(CompletionTypeJSONMemberName)
    private CompletionType mCompletionType;
    @SerializedName("CompletionValue")
    private int mCompletionValue;
    @SerializedName("CyclesPerformed")
    private int mCyclesPerformed;
    @SerializedName("KDisplaySubType")
    private DisplaySubType mKDisplaySubType;
    @SerializedName("KIsSupportedCounting")
    private boolean mKIsSupportedCounting;
    @SerializedName("RepetitionsPerformed")
    private int mRepetitionsPerformed;
    @SerializedName("RoundsPerformed")
    private int mRoundsPerformed;
    @SerializedName("WorkoutDayId")
    private int mWorkoutDayID;
    @SerializedName("Sequences")
    private List<WorkoutExerciseSequence> mWorkoutExerciseSequences;
    @SerializedName("WorkoutIndex")
    private int mWorkoutIndex;
    @SerializedName("WorkoutPartnerId")
    private int mWorkoutPartnerID;
    @SerializedName(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID)
    private String mWorkoutPlanID;
    @SerializedName("WorkoutPlanInstanceId")
    private int mWorkoutPlanInstanceID;
    @SerializedName("WorkoutWeekId")
    private int mWorkoutWeekID;

    public GuidedWorkoutEvent() {
    }

    protected GuidedWorkoutEvent(Parcel in) {
        super(in);
        this.mCyclesPerformed = in.readInt();
        this.mRoundsPerformed = in.readInt();
        this.mRepetitionsPerformed = in.readInt();
        this.mWorkoutPlanID = in.readString();
        this.mWorkoutIndex = in.readInt();
        this.mWorkoutWeekID = in.readInt();
        this.mWorkoutDayID = in.readInt();
        this.mWorkoutPlanInstanceID = in.readInt();
        this.mWorkoutPartnerID = in.readInt();
        int completionTypeOrdinal = in.readInt();
        this.mCompletionType = completionTypeOrdinal != -1 ? CompletionType.values()[completionTypeOrdinal] : null;
        this.mCompletionValue = in.readInt();
        this.mWorkoutExerciseSequences = new ArrayList();
        in.readTypedList(this.mWorkoutExerciseSequences, WorkoutExerciseSequence.CREATOR);
    }

    @Override // com.microsoft.krestsdk.models.ExerciseEventBase, com.microsoft.krestsdk.models.UserEvent, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mCyclesPerformed);
        dest.writeInt(this.mRoundsPerformed);
        dest.writeInt(this.mRepetitionsPerformed);
        dest.writeString(this.mWorkoutPlanID);
        dest.writeInt(this.mWorkoutIndex);
        dest.writeInt(this.mWorkoutWeekID);
        dest.writeInt(this.mWorkoutDayID);
        dest.writeInt(this.mWorkoutPlanInstanceID);
        dest.writeInt(this.mWorkoutPartnerID);
        dest.writeInt(this.mCompletionType != null ? this.mCompletionType.ordinal() : -1);
        dest.writeInt(this.mCompletionValue);
        dest.writeTypedList(this.mWorkoutExerciseSequences);
    }

    public int getCyclesPerformed() {
        return this.mCyclesPerformed;
    }

    public void setCyclesPerformed(int cyclesPerformed) {
        this.mCyclesPerformed = cyclesPerformed;
    }

    public int getRoundsPerformed() {
        return this.mRoundsPerformed;
    }

    public void setRoundsPerformed(int roundsPerformed) {
        this.mRoundsPerformed = roundsPerformed;
    }

    public int getRepetitionsPerformed() {
        return this.mRepetitionsPerformed;
    }

    public void setRepetitionsPerformed(int repetitionsPerformed) {
        this.mRepetitionsPerformed = repetitionsPerformed;
    }

    public String getWorkoutPlanID() {
        return this.mWorkoutPlanID;
    }

    public void setWorkoutPlanID(String workoutPlanID) {
        this.mWorkoutPlanID = workoutPlanID;
    }

    public int getWorkoutIndex() {
        return this.mWorkoutIndex;
    }

    public void setworkoutIndex(int workoutIndex) {
        this.mWorkoutIndex = workoutIndex;
    }

    public int getWorkoutWeekID() {
        return this.mWorkoutWeekID;
    }

    public void setworkoutWeekID(int workoutWeekID) {
        this.mWorkoutWeekID = workoutWeekID;
    }

    public int getWorkoutDayID() {
        return this.mWorkoutDayID;
    }

    public void setWorkoutDayID(int workoutDayID) {
        this.mWorkoutDayID = workoutDayID;
    }

    public int getWorkoutPlanInstanceID() {
        return this.mWorkoutPlanInstanceID;
    }

    public void setWorkoutPlanInstanceID(int workoutPlanInstanceID) {
        this.mWorkoutPlanInstanceID = workoutPlanInstanceID;
    }

    public int getWorkoutPartnerID() {
        return this.mWorkoutPartnerID;
    }

    public void setWorkoutPartnerID(int workoutPartnerID) {
        this.mWorkoutPartnerID = workoutPartnerID;
    }

    public CompletionType getCompletionType() {
        return this.mCompletionType;
    }

    public void setCompletionType(CompletionType completionType) {
        this.mCompletionType = completionType;
    }

    public int getCompletionValue() {
        return this.mCompletionValue;
    }

    public void setCompletionValue(int completionValue) {
        this.mCompletionValue = completionValue;
    }

    public DisplaySubType getKDisplaySubType() {
        return this.mKDisplaySubType;
    }

    public void setKDisplaySubType(DisplaySubType displaySubType) {
        this.mKDisplaySubType = displaySubType;
    }

    public List<WorkoutExerciseSequence> getWorkoutExerciseSequences() {
        return this.mWorkoutExerciseSequences;
    }

    public void setWorkoutExerciseSequences(List<WorkoutExerciseSequence> list) {
        this.mWorkoutExerciseSequences = list;
    }

    public boolean getKIsSupportedCounting() {
        return this.mKIsSupportedCounting;
    }

    public void setKIsSupportedCounting(boolean kIsSupportedCounting) {
        this.mKIsSupportedCounting = kIsSupportedCounting;
    }
}
