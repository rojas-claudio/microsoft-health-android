package com.microsoft.kapp.models.guidedworkout;

import com.microsoft.krestsdk.models.CompletionType;
import com.microsoft.krestsdk.models.WorkoutExerciseSequence;
/* loaded from: classes.dex */
public class BaseGuidedWorkoutItem {
    private boolean isRepsEstimated;
    private boolean isSmartCountingWorkout;
    private int mCalories;
    private int mDistance;
    private int mDuration;
    private int mRepetitions;

    public int getDuration() {
        return this.mDuration;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public void accumulateDuration(int duration) {
        this.mDuration += duration;
    }

    public int getCalories() {
        return this.mCalories;
    }

    public void setCalories(int calories) {
        this.mCalories = calories;
    }

    public void accumulateCalories(int calories) {
        this.mCalories += calories;
    }

    public int getRepetitions() {
        return this.mRepetitions;
    }

    public void setRepetitions(int repetitions) {
        this.mRepetitions = repetitions;
    }

    public void accumulateRepetitions(WorkoutExerciseSequence exercise) {
        if (this.isSmartCountingWorkout && exercise.getCompletionType() == CompletionType.SECONDS && exercise.getComputedCompletionValue() > 0) {
            this.isRepsEstimated = true;
            this.mRepetitions = (int) (this.mRepetitions + convertRepsEdgeValue(exercise.getComputedCompletionValue()));
        } else if (exercise.getCompletionType() == CompletionType.REPETITIONS) {
            this.mRepetitions += convertRepsEdgeValue(exercise.getCompletionValue());
        }
    }

    public int getDistance() {
        return this.mDistance;
    }

    public void setDistance(int distance) {
        this.mDistance = distance;
    }

    public void accumulateDistance(WorkoutExerciseSequence exercise) {
        if (exercise.getCompletionType() == CompletionType.METERS) {
            this.mDistance += Integer.parseInt(exercise.getCompletionValue());
        }
    }

    public boolean isRepsEstimated() {
        return this.isRepsEstimated;
    }

    public void setRepsEstimated(boolean isRepsEstimated) {
        this.isRepsEstimated = isRepsEstimated;
    }

    public boolean isSmartCountingWorkout() {
        return this.isSmartCountingWorkout;
    }

    public void setSmartCountingWorkout(boolean isSmartCountingWorkout) {
        this.isSmartCountingWorkout = isSmartCountingWorkout;
    }

    private long convertRepsEdgeValue(int reps) {
        if (reps >= Integer.MAX_VALUE || reps <= Integer.MIN_VALUE) {
            return 0L;
        }
        return reps;
    }

    private int convertRepsEdgeValue(String reps) {
        if (reps.equals(Integer.toString(Integer.MAX_VALUE))) {
            return 0;
        }
        return Integer.parseInt(reps);
    }
}
