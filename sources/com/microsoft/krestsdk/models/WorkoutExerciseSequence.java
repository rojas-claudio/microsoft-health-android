package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.services.healthandfitness.models.CircuitGroupType;
import com.microsoft.kapp.services.healthandfitness.models.CircuitType;
/* loaded from: classes.dex */
public class WorkoutExerciseSequence extends ExerciseEventSequence {
    public static final Parcelable.Creator<WorkoutExerciseSequence> CREATOR = new Parcelable.Creator<WorkoutExerciseSequence>() { // from class: com.microsoft.krestsdk.models.WorkoutExerciseSequence.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutExerciseSequence createFromParcel(Parcel in) {
            return new WorkoutExerciseSequence(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutExerciseSequence[] newArray(int size) {
            return new WorkoutExerciseSequence[size];
        }
    };
    @SerializedName("CircuitGroupType")
    private CircuitGroupType mCircuitGroupType;
    @SerializedName("CircuitID")
    private String mCircuitId;
    @SerializedName("CircuitOrdinal")
    private int mCircuitOrdinal;
    @SerializedName("CircuitType")
    private int mCircuitType;
    @SerializedName(GuidedWorkoutEvent.CompletionTypeJSONMemberName)
    private int mCompletionType;
    @SerializedName("CompletionValue")
    private String mCompletionValue;
    @SerializedName("ComputedCompletionValue")
    private int mComputedCompletionValue;
    @SerializedName("CycleOrdinal")
    private int mCycleOrdinal;
    @SerializedName("DoNotCount")
    private boolean mDoNotCount;
    @SerializedName("ExerciseCategory")
    private String mExerciseCategory;
    @SerializedName("ExerciseFinishStatus")
    private String mExerciseFinishStatus;
    @SerializedName("ExerciseID")
    private long mExerciseId;
    @SerializedName("ExerciseOrdinal")
    private int mExerciseOrdinal;
    @SerializedName("ExercisePosition")
    private int mExercisePosition;
    @SerializedName("ExerciseStringID")
    private String mExerciseStringId;
    @SerializedName("KExerciseTraversalType")
    private int mExerciseTraversalType;
    @SerializedName("ExerciseType")
    private String mExerciseType;
    @SerializedName("IsRest")
    private boolean mIsRest;
    @SerializedName(TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME)
    private String mName;
    @SerializedName("RoundOrdinal")
    private int mRoundOrdinal;
    @SerializedName("SetOrdinal")
    private int mSetOrdinal;

    public WorkoutExerciseSequence() {
    }

    public int getCycleOrdinal() {
        return this.mCycleOrdinal;
    }

    public void setCycleOrdinal(int cycleOrdinal) {
        this.mCycleOrdinal = cycleOrdinal;
    }

    public String getCircuitId() {
        return this.mCircuitId;
    }

    public void setCircuitId(String circuitId) {
        this.mCircuitId = circuitId;
    }

    public int getCircuitOrdinal() {
        return this.mCircuitOrdinal;
    }

    public void setCircuitOrdinal(int circuitOrdinal) {
        this.mCircuitOrdinal = circuitOrdinal;
    }

    public CircuitType getCircuitType() {
        return CircuitType.valueOf(this.mCircuitType);
    }

    public void setCircuitType(CircuitType circuitType) {
        this.mCircuitType = circuitType.ordinal();
    }

    public int getRoundOrdinal() {
        return this.mRoundOrdinal;
    }

    public void setRoundOrdinal(int roundOrdinal) {
        this.mRoundOrdinal = roundOrdinal;
    }

    public String getExerciseCategory() {
        return this.mExerciseCategory;
    }

    public void setExerciseCategory(String exerciseCategory) {
        this.mExerciseCategory = exerciseCategory;
    }

    public String getExerciseFinishStatus() {
        return this.mExerciseFinishStatus;
    }

    public void setExerciseFinishStatus(String exerciseFinishStatus) {
        this.mExerciseFinishStatus = exerciseFinishStatus;
    }

    public long getExerciseId() {
        return this.mExerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.mExerciseId = exerciseId;
    }

    public int getExerciseOrdinal() {
        return this.mExerciseOrdinal;
    }

    public void setExerciseOrdinal(int exerciseOrdinal) {
        this.mExerciseOrdinal = exerciseOrdinal;
    }

    public String getExerciseStringId() {
        return this.mExerciseStringId;
    }

    public void setExerciseStringId(String exerciseStringId) {
        this.mExerciseStringId = exerciseStringId;
    }

    public String getExerciseType() {
        return this.mExerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.mExerciseType = exerciseType;
    }

    public CompletionType getCompletionType() {
        return CompletionType.valueOf(this.mCompletionType);
    }

    public void setCompletionType(CompletionType completionType) {
        this.mCompletionType = completionType.ordinal();
    }

    public String getCompletionValue() {
        return this.mCompletionValue;
    }

    public void setCompletionValue(String completionValue) {
        this.mCompletionValue = completionValue;
    }

    public int getComputedCompletionValue() {
        return this.mComputedCompletionValue;
    }

    public void setComputedCompletionValue(int computedCompletionValue) {
        this.mComputedCompletionValue = computedCompletionValue;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public boolean getIsRest() {
        return this.mIsRest;
    }

    public void setIsRest(boolean isRest) {
        this.mIsRest = isRest;
    }

    public ExerciseTraversalType getExerciseTraversalType() {
        return ExerciseTraversalType.valueOf(this.mExerciseTraversalType);
    }

    public void setExerciseTraversalType(ExerciseTraversalType exerciseTraversalType) {
        this.mExerciseTraversalType = exerciseTraversalType.ordinal();
    }

    public boolean getDoNotCount() {
        return this.mDoNotCount;
    }

    public void setDoNotCount(boolean doNotCount) {
        this.mDoNotCount = doNotCount;
    }

    public CircuitGroupType getCircuitGroupType() {
        return this.mCircuitGroupType == null ? CircuitGroupType.UNKNOWN : this.mCircuitGroupType;
    }

    public void setCircuitGroupType(CircuitGroupType circuitGroupType) {
        this.mCircuitGroupType = circuitGroupType;
    }

    public int getSetOrdinal() {
        return this.mSetOrdinal;
    }

    public void setSetOrdinal(int setOrdinal) {
        this.mSetOrdinal = setOrdinal;
    }

    public int getExercisePosition() {
        return this.mExercisePosition;
    }

    public void setExercisePosition(int mExercisePosition) {
        this.mExercisePosition = mExercisePosition;
    }

    protected WorkoutExerciseSequence(Parcel in) {
        super(in);
        this.mName = in.readString();
        this.mCycleOrdinal = in.readInt();
        this.mCircuitId = in.readString();
        this.mCircuitOrdinal = in.readInt();
        this.mCircuitType = in.readInt();
        this.mRoundOrdinal = in.readInt();
        this.mExerciseCategory = in.readString();
        this.mExerciseFinishStatus = in.readString();
        this.mExerciseId = in.readLong();
        this.mExerciseOrdinal = in.readInt();
        this.mExerciseStringId = in.readString();
        this.mExerciseType = in.readString();
        this.mCompletionType = in.readInt();
        this.mCompletionValue = in.readString();
        this.mComputedCompletionValue = in.readInt();
        this.mIsRest = in.readInt() == 1;
        this.mExerciseTraversalType = in.readInt();
        this.mDoNotCount = in.readInt() == 1;
        this.mCircuitGroupType = CircuitGroupType.valueOf(in.readString());
        this.mSetOrdinal = in.readInt();
        this.mExercisePosition = in.readInt();
    }

    @Override // com.microsoft.krestsdk.models.ExerciseEventSequence, com.microsoft.krestsdk.models.UserEventSequence, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mName);
        dest.writeInt(this.mCycleOrdinal);
        dest.writeString(this.mCircuitId);
        dest.writeInt(this.mCircuitOrdinal);
        dest.writeInt(this.mCircuitType);
        dest.writeInt(this.mRoundOrdinal);
        dest.writeString(this.mExerciseCategory);
        dest.writeString(this.mExerciseFinishStatus);
        dest.writeLong(this.mExerciseId);
        dest.writeInt(this.mExerciseOrdinal);
        dest.writeString(this.mExerciseStringId);
        dest.writeString(this.mExerciseType);
        dest.writeInt(this.mCompletionType);
        dest.writeString(this.mCompletionValue);
        dest.writeInt(this.mComputedCompletionValue);
        dest.writeInt(this.mIsRest ? 1 : 0);
        dest.writeInt(this.mExerciseTraversalType);
        dest.writeInt(this.mDoNotCount ? 1 : 0);
        dest.writeString(this.mCircuitGroupType.name());
        dest.writeInt(this.mSetOrdinal);
        dest.writeInt(this.mExercisePosition);
    }
}
