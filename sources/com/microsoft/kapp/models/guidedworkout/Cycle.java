package com.microsoft.kapp.models.guidedworkout;

import android.content.Context;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.krestsdk.models.WorkoutExerciseSequence;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class Cycle extends BaseGuidedWorkoutItem {
    private static final String TAG = Cycle.class.getSimpleName();
    private List<Block> mBlocks;
    private Context mContext;
    private String mName;

    public Cycle(Context context, String name) {
        this.mName = name;
        this.mContext = context;
    }

    public String getName() {
        return this.mName;
    }

    public List<Block> getBlockList() {
        return this.mBlocks == null ? new ArrayList() : this.mBlocks;
    }

    public void addExercise(WorkoutExerciseSequence exercise) {
        if (exercise != null) {
            if (this.mBlocks == null) {
                this.mBlocks = new ArrayList();
            }
            int blockIndex = exercise.getCircuitType().value();
            if (this.mBlocks.size() <= blockIndex) {
                for (int i = this.mBlocks.size(); i <= blockIndex; i++) {
                    this.mBlocks.add(null);
                }
            }
            if (blockIndex >= 0) {
                Block block = this.mBlocks.get(blockIndex);
                if (block == null) {
                    block = new Block(this.mContext);
                    block.setBlockType(exercise.getCircuitType());
                }
                block.setSmartCountingWorkout(isSmartCountingWorkout());
                block.accumulateCalories(exercise.getCaloriesBurned());
                block.accumulateDuration(exercise.getDuration());
                block.accumulateRepetitions(exercise);
                block.accumulateDistance(exercise);
                block.addExcercise(exercise);
                this.mBlocks.set(blockIndex, block);
                return;
            }
            KLog.e(TAG, "Error: Circuit index started at 0!! Circuit index should be 1-based and start a 1!");
        }
    }
}
