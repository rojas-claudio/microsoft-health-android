package com.microsoft.kapp.services.healthandfitness.models;

import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.models.CompletionType;
import com.microsoft.krestsdk.models.ExerciseTraversalType;
import java.io.Serializable;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class WorkoutCircuit implements Serializable {
    private static final long serialVersionUID = -8351018845477321993L;
    @SerializedName("droplastrest")
    private boolean mDropLastRest;
    @SerializedName("exs")
    private WorkoutExercise[] mExerciseList;
    @SerializedName("grouptype")
    private CircuitGroupType mGroupType;
    private int mGroupTypeIndex;
    @SerializedName("kcircuittype")
    private CircuitType mKCircuitType;
    @SerializedName("kcompletiontype")
    private CompletionType mKCompletionType;
    @SerializedName("kcompletionvalue")
    private int mKCompletionValue;
    @SerializedName("kexercisetraversaltype")
    private ExerciseTraversalType mKExerciseTraversalType;
    @SerializedName(WorkoutSummary.NAME)
    private String mName;

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public WorkoutExercise[] getExerciseList() {
        return this.mExerciseList;
    }

    public void setExerciseList(WorkoutExercise[] mExerciseList) {
        this.mExerciseList = mExerciseList;
    }

    public WorkoutExercise[] getExerciseArrayWithoutRests() {
        ArrayList<WorkoutExercise> list = getExerciseListWithoutRests();
        return (WorkoutExercise[]) list.toArray(new WorkoutExercise[list.size()]);
    }

    public ArrayList<WorkoutExercise> getExerciseListWithoutRests() {
        ArrayList<WorkoutExercise> list = new ArrayList<>();
        WorkoutExercise[] arr$ = this.mExerciseList;
        for (WorkoutExercise exercise : arr$) {
            if (exercise != null && exercise.getId() != null && !Constants.REST_EXERCISE_ID.equals(exercise.getId())) {
                list.add(exercise);
            }
        }
        return list;
    }

    public CompletionType getCompletionType() {
        return this.mKCompletionType;
    }

    public void setCompletionType(CompletionType completionType) {
        this.mKCompletionType = completionType;
    }

    public int getCompletionValue() {
        return this.mKCompletionValue;
    }

    public void setCompletionValue(int completionValue) {
        this.mKCompletionValue = completionValue;
    }

    public CircuitType getCircuitType() {
        return this.mKCircuitType;
    }

    public void setCircuitType(CircuitType circuitType) {
        this.mKCircuitType = circuitType;
    }

    public ExerciseTraversalType getExerciseTraversalType() {
        return this.mKExerciseTraversalType;
    }

    public void setExerciseTraversalType(ExerciseTraversalType exerciseTraversalType) {
        this.mKExerciseTraversalType = exerciseTraversalType;
    }

    public CircuitGroupType getGroupType() {
        return this.mGroupType != null ? this.mGroupType : CircuitGroupType.UNKNOWN;
    }

    public void setGroupType(CircuitGroupType type) {
        this.mGroupType = type;
    }

    public void setGroupTypeIndex(int index) {
        this.mGroupTypeIndex = index;
    }

    public int getGroupTypeIndex() {
        return this.mGroupTypeIndex;
    }

    public boolean getDropLastRest() {
        return this.mDropLastRest;
    }

    public void setDropLastRest(boolean dropLastRest) {
        this.mDropLastRest = dropLastRest;
    }
}
