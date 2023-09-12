package com.microsoft.kapp.models.guidedworkout;

import android.content.Context;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.GuidedWorkoutPostGroupList;
import com.microsoft.kapp.services.healthandfitness.models.CircuitType;
import com.microsoft.krestsdk.models.WorkoutExerciseSequence;
/* loaded from: classes.dex */
public class Block extends BaseGuidedWorkoutItem {
    private static final String TAG = Block.class.getSimpleName();
    private CircuitType mBlockType;
    private Context mContext;
    private GuidedWorkoutPostGroupList mGroups;

    public Block(Context context) {
        this.mContext = context;
    }

    public CircuitType getBlockType() {
        return this.mBlockType;
    }

    public void setBlockType(CircuitType mBlockType) {
        this.mBlockType = mBlockType;
    }

    public GuidedWorkoutPostGroupList getGroups() {
        return this.mGroups;
    }

    public void setGroups(GuidedWorkoutPostGroupList groups) {
        this.mGroups = groups;
    }

    public void addExcercise(WorkoutExerciseSequence exercise) {
        if (exercise != null) {
            if (this.mGroups == null) {
                this.mGroups = new GuidedWorkoutPostGroupList();
            }
            int groupIndex = exercise.getCircuitOrdinal() - 1;
            if (this.mGroups.size() <= groupIndex) {
                for (int i = this.mGroups.size(); i <= groupIndex; i++) {
                    this.mGroups.add((Group) null);
                }
            }
            if (groupIndex >= 0) {
                String groupName = exercise.getCircuitGroupType().name();
                Group group = this.mGroups.get(groupIndex);
                if (group == null) {
                    group = new Group(this.mContext, groupName);
                }
                group.setSmartCountingWorkout(isSmartCountingWorkout());
                group.accumulateCalories(exercise.getCaloriesBurned());
                group.accumulateDuration(exercise.getDuration());
                group.accumulateRepetitions(exercise);
                group.accumulateDistance(exercise);
                group.setCircuitType(exercise.getCircuitType());
                group.setExerciseTraversalType(exercise.getExerciseTraversalType());
                group.setCircuitGroupType(exercise.getCircuitGroupType());
                group.addExercise(exercise);
                this.mGroups.set(groupIndex, group);
                return;
            }
            KLog.e(TAG, "Error: Circuit index started at 0!! Circuit index should be 1-based and start a 1!");
        }
    }
}
