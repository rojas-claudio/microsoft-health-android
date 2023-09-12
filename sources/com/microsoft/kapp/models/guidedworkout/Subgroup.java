package com.microsoft.kapp.models.guidedworkout;

import android.content.Context;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.models.WorkoutExerciseSequence;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class Subgroup extends BaseSetExercise {
    private static final String TAG = Subgroup.class.getSimpleName();
    private List<ExerciseItem> mItems;
    private Long mLastExerciseId;
    private String mLastExerciseName;
    private int mLastItemIndex;
    private boolean mHasSingleExercise = true;
    private SubgroupType mSubgroupType = SubgroupType.Exercise;

    /* loaded from: classes.dex */
    public enum SubgroupType {
        Exercise,
        Round,
        Set
    }

    public Subgroup(Context context) {
        this.mContext = context;
    }

    public Subgroup(Context context, String name) {
        this.mName = name;
        this.mContext = context;
    }

    public List<ExerciseItem> getExercieItemList() {
        return this.mItems;
    }

    public SubgroupType getSubGroupType() {
        return this.mSubgroupType;
    }

    public void setSubGroupType(SubgroupType subGroupType) {
        this.mSubgroupType = subGroupType;
    }

    public boolean hasSingleExercise() {
        return this.mHasSingleExercise;
    }

    public void setHasSingleExercise(boolean hasSingleExercise) {
        this.mHasSingleExercise = hasSingleExercise;
    }

    public void addExercise(WorkoutExerciseSequence workoutExerciseSequence) {
        if (workoutExerciseSequence != null) {
            if (this.mItems == null) {
                this.mItems = new ArrayList();
            }
            int exerciseIndex = getItemIndex(workoutExerciseSequence);
            ExerciseItem item = new ExerciseItem(this.mContext);
            String itemName = getItemName(workoutExerciseSequence, exerciseIndex);
            if (itemName == null) {
                itemName = String.format(this.mContext.getString(R.string.guided_workout_details_exercise_name), Integer.valueOf(exerciseIndex + 1));
            }
            item.setName(itemName);
            item.setSmartCountingWorkout(isSmartCountingWorkout());
            item.setDuration(workoutExerciseSequence.getDuration());
            item.setCalories(workoutExerciseSequence.getCaloriesBurned());
            item.accumulateRepetitions(workoutExerciseSequence);
            item.setExerciseOrdinal(workoutExerciseSequence.getExerciseOrdinal());
            item.setDoNotCount(workoutExerciseSequence.getDoNotCount());
            item.setIsRest(workoutExerciseSequence.getIsRest() || Constants.REST_EXERCISE_ID.equalsIgnoreCase(workoutExerciseSequence.getExerciseStringId()));
            this.mItems.add(item);
            if (this.mItems.size() > 1) {
                this.mHasSingleExercise = false;
            }
        }
    }

    public ExerciseItem removeItem(int index) {
        if (this.mItems != null && index < this.mItems.size()) {
            ExerciseItem item = this.mItems.get(index);
            if (getDuration() > 0) {
                setDuration(getDuration() - item.getDuration());
            }
            if (getCalories() > 0) {
                setCalories(getCalories() - item.getCalories());
            }
            if (getRepetitions() > 0) {
                setRepetitions(getRepetitions() - item.getRepetitions());
            }
            if (getDistance() > 0) {
                setDistance(getDistance() - item.getDistance());
            }
            this.mItems.remove(this.mItems.size() - 1);
            this.mHasSingleExercise = this.mItems.size() == 1;
            return item;
        }
        return null;
    }

    private int getItemIndex(WorkoutExerciseSequence exercise) {
        if (exercise == null || this.mSubgroupType == null) {
            return -1;
        }
        if (this.mSubgroupType.equals(SubgroupType.Exercise)) {
            int index = exercise.getSetOrdinal() - 1;
            return index;
        }
        int index2 = exercise.getExercisePosition();
        return index2;
    }

    private String getItemName(WorkoutExerciseSequence exercise, int index) {
        if (exercise == null) {
            return null;
        }
        String name = exercise.getName();
        if (exercise.getIsRest() || Constants.REST_EXERCISE_ID.equalsIgnoreCase(exercise.getExerciseStringId())) {
            String name2 = this.mContext.getString(R.string.guided_workout_details_rest_name);
            return name2;
        } else if (this.mSubgroupType != null && this.mSubgroupType.equals(SubgroupType.Exercise)) {
            String name3 = String.format(this.mContext.getString(R.string.guided_workout_details_set_name), Integer.valueOf(index + 1));
            return name3;
        } else {
            return name;
        }
    }
}
