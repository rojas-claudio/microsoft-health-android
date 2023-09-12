package com.microsoft.kapp.models.guidedworkout;

import android.content.Context;
import android.text.TextUtils;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.guidedworkout.Subgroup;
import com.microsoft.kapp.services.healthandfitness.models.CircuitGroupType;
import com.microsoft.kapp.services.healthandfitness.models.CircuitType;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.krestsdk.models.CompletionType;
import com.microsoft.krestsdk.models.ExerciseTraversalType;
import com.microsoft.krestsdk.models.WorkoutExerciseSequence;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/* loaded from: classes.dex */
public class Group extends BaseGuidedWorkoutItem {
    private static final String TAG = Group.class.getSimpleName();
    private CircuitGroupType mCircuitGroupType;
    private int mCircuitGroupTypeIndex;
    private CircuitType mCircuitType;
    private Context mContext;
    private String mDescription;
    private ExerciseTraversalType mExerciseTraversalType;
    private String mName;
    private boolean mShowCircuitGroupIndex;
    private List<Subgroup> mSubgroups;
    private HashSet<String> mNoRestExerciseIdsSet = new HashSet<>();
    private boolean mIsRestCircuit = true;
    private boolean mIsExpanded = true;
    private int mLastSubgroupIndex = -1;

    public Group(Context context, String name) {
        this.mName = name;
        this.mContext = context;
    }

    public String getName() {
        return this.mName;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public List<Subgroup> getSubgroupsList() {
        return this.mSubgroups;
    }

    public void setSubgroupsList(List<Subgroup> subgroupList) {
        this.mSubgroups = subgroupList;
    }

    public ExerciseTraversalType getExerciseTraversalType() {
        return this.mExerciseTraversalType;
    }

    public void setExerciseTraversalType(ExerciseTraversalType exerciseTraversalType) {
        this.mExerciseTraversalType = exerciseTraversalType;
    }

    public CircuitType getCircuitType() {
        return this.mCircuitType;
    }

    public void setCircuitType(CircuitType circuitType) {
        this.mCircuitType = circuitType;
    }

    public boolean getIsRestCircuit() {
        return this.mIsRestCircuit;
    }

    public void setIsRestCircuit(boolean isRestCircuit) {
        this.mIsRestCircuit = isRestCircuit;
    }

    public CircuitGroupType getCircuitGroupType() {
        return this.mCircuitGroupType;
    }

    public void setCircuitGroupType(CircuitGroupType circuitGroupType) {
        this.mCircuitGroupType = circuitGroupType;
    }

    public int getCircuitGroupIndex() {
        return this.mCircuitGroupTypeIndex;
    }

    public void setCircuitGroupTypeIndex(int mCircuitGroupIndex) {
        this.mCircuitGroupTypeIndex = mCircuitGroupIndex;
    }

    public boolean isShowCircuitGroupTypeIndex() {
        return this.mShowCircuitGroupIndex;
    }

    public void setShowCircuitGroupIndex(boolean mShowCircuitGroupIndex) {
        this.mShowCircuitGroupIndex = mShowCircuitGroupIndex;
    }

    public void addExercise(WorkoutExerciseSequence exercise) {
        if (exercise != null) {
            if (this.mIsRestCircuit && !exercise.getIsRest() && !Constants.REST_EXERCISE_ID.equalsIgnoreCase(exercise.getExerciseStringId())) {
                this.mIsRestCircuit = false;
            }
            if (this.mSubgroups == null) {
                this.mSubgroups = new ArrayList();
            }
            int subgroupIndex = getSubgroupIndex(exercise);
            if (exercise.getIsRest() || Constants.REST_EXERCISE_ID.equalsIgnoreCase(exercise.getExerciseStringId())) {
                subgroupIndex = this.mLastSubgroupIndex;
            }
            if (this.mSubgroups.size() <= subgroupIndex) {
                for (int i = this.mSubgroups.size(); i <= subgroupIndex; i++) {
                    this.mSubgroups.add(null);
                }
            }
            if (subgroupIndex >= 0) {
                this.mLastSubgroupIndex = subgroupIndex;
                Subgroup subgroup = this.mSubgroups.get(subgroupIndex);
                if (subgroup == null) {
                    subgroup = new Subgroup(this.mContext);
                    this.mSubgroups.set(subgroupIndex, subgroup);
                }
                subgroup.setSmartCountingWorkout(isSmartCountingWorkout());
                subgroup.setSubGroupType(getSubgroupType(exercise));
                if (subgroup.getName() == null) {
                    subgroup.setName(getSubgroupName(exercise, subgroupIndex));
                }
                subgroup.accumulateCalories(exercise.getCaloriesBurned());
                subgroup.accumulateDuration(exercise.getDuration());
                subgroup.accumulateRepetitions(exercise);
                subgroup.accumulateDistance(exercise);
                subgroup.addExercise(exercise);
                if (subgroupIndex == 0) {
                    String exerciseName = exercise.getName();
                    String completionValue = exercise.getCompletionValue();
                    if (exerciseName != null && !this.mNoRestExerciseIdsSet.contains(exerciseName) && !exercise.getIsRest() && !Constants.REST_EXERCISE_ID.equalsIgnoreCase(exercise.getExerciseStringId())) {
                        this.mNoRestExerciseIdsSet.add(exerciseName);
                        if (completionValue != null) {
                            CompletionType completionType = exercise.getCompletionType();
                            if (completionType == CompletionType.SECONDS) {
                                try {
                                    int completionValueInt = Integer.parseInt(completionValue);
                                    String formattedTime = Formatter.formatPostGuidedWorkoutDetailCompletionValueTime(completionValueInt, this.mContext);
                                    this.mDescription = !TextUtils.isEmpty(this.mDescription) ? this.mContext.getString(R.string.guided_workout_details_circuit_append_description, this.mDescription, exerciseName, formattedTime) : this.mContext.getString(R.string.guided_workout_details_circuit_description, exerciseName, formattedTime);
                                    return;
                                } catch (NumberFormatException e) {
                                    KLog.e(TAG, "completionValue should be a number if the completionType is seconds!");
                                    this.mDescription = formatDescription(exerciseName, completionValue, completionType);
                                    return;
                                }
                            }
                            this.mDescription = formatDescription(exerciseName, completionValue, completionType);
                            return;
                        }
                        this.mDescription = exerciseName;
                        return;
                    }
                    return;
                }
                return;
            }
            KLog.e(TAG, "Error: Round index started at 0!! Round index should be 1-based and start a 1!");
        }
    }

    private int getSubgroupIndex(WorkoutExerciseSequence exercise) {
        if (exercise == null || this.mCircuitGroupType == null) {
            return -1;
        }
        if (this.mCircuitGroupType.equals(CircuitGroupType.CircuitTime)) {
            int index = exercise.getRoundOrdinal() - 1;
            return index;
        } else if (this.mCircuitGroupType.equals(CircuitGroupType.CircuitTask)) {
            int index2 = exercise.getRoundOrdinal() - 1;
            return index2;
        } else if (this.mCircuitGroupType.equals(CircuitGroupType.Interval)) {
            int index3 = exercise.getSetOrdinal() - 1;
            return index3;
        } else {
            int index4 = exercise.getExercisePosition();
            return index4;
        }
    }

    private String getSubgroupName(WorkoutExerciseSequence exercise, int index) {
        if (exercise == null) {
            return "";
        }
        String name = exercise.getName();
        if (this.mCircuitGroupType != null) {
            if (this.mCircuitGroupType.equals(CircuitGroupType.CircuitTime)) {
                String name2 = String.format(this.mContext.getString(R.string.guided_workout_details_round_name), Integer.valueOf(index + 1));
                return name2;
            } else if (this.mCircuitGroupType.equals(CircuitGroupType.CircuitTask)) {
                String name3 = String.format(this.mContext.getString(R.string.guided_workout_details_round_name), Integer.valueOf(index + 1));
                return name3;
            } else if (this.mCircuitGroupType.equals(CircuitGroupType.Interval)) {
                String name4 = String.format(this.mContext.getString(R.string.guided_workout_details_set_name), Integer.valueOf(index + 1));
                return name4;
            } else {
                return name;
            }
        }
        return name;
    }

    private Subgroup.SubgroupType getSubgroupType(WorkoutExerciseSequence exercise) {
        Subgroup.SubgroupType type = Subgroup.SubgroupType.Exercise;
        if (exercise != null && this.mCircuitGroupType != null) {
            if (this.mCircuitGroupType.equals(CircuitGroupType.CircuitTime)) {
                return Subgroup.SubgroupType.Round;
            }
            if (this.mCircuitGroupType.equals(CircuitGroupType.CircuitTask)) {
                return Subgroup.SubgroupType.Round;
            }
            if (this.mCircuitGroupType.equals(CircuitGroupType.Interval)) {
                return Subgroup.SubgroupType.Set;
            }
            return type;
        }
        return type;
    }

    private boolean getIsExpanded() {
        return this.mIsExpanded;
    }

    private void setIsExpanded(boolean isExpanded) {
        this.mIsExpanded = isExpanded;
    }

    private String formatDescription(String exerciseName, String completionValue, CompletionType completionType) {
        if (completionType == null) {
            return "";
        }
        switch (completionType) {
            case SECONDS:
                return !TextUtils.isEmpty(this.mDescription) ? this.mContext.getString(R.string.guided_workout_details_circuit_append_description_seconds, this.mDescription, exerciseName, completionValue) : this.mContext.getString(R.string.guided_workout_details_circuit_description_seconds, exerciseName, completionValue);
            case METERS:
                return !TextUtils.isEmpty(this.mDescription) ? this.mContext.getString(R.string.guided_workout_details_circuit_append_description_meters, this.mDescription, exerciseName, completionValue) : this.mContext.getString(R.string.guided_workout_details_circuit_description_meters, exerciseName, completionValue);
            case REPETITIONS:
                return !TextUtils.isEmpty(this.mDescription) ? this.mContext.getString(R.string.guided_workout_details_circuit_append_description_reps, this.mDescription, exerciseName, completionValue) : this.mContext.getString(R.string.guided_workout_details_circuit_description_reps, exerciseName, completionValue);
            case JOULES:
                return !TextUtils.isEmpty(this.mDescription) ? this.mContext.getString(R.string.guided_workout_details_circuit_append_description_joules, this.mDescription, exerciseName, completionValue) : this.mContext.getString(R.string.guided_workout_details_circuit_description_joules, exerciseName, completionValue);
            case CALORIES:
                return !TextUtils.isEmpty(this.mDescription) ? this.mContext.getString(R.string.guided_workout_details_circuit_append_description_calories, this.mDescription, exerciseName, completionValue) : this.mContext.getString(R.string.guided_workout_details_circuit_description_calories, exerciseName, completionValue);
            case HEART_RATE:
                return !TextUtils.isEmpty(this.mDescription) ? this.mContext.getString(R.string.guided_workout_details_circuit_append_description_HR, this.mDescription, exerciseName, completionValue) : this.mContext.getString(R.string.guided_workout_details_circuit_description_HR, exerciseName, completionValue);
            default:
                return "";
        }
    }
}
