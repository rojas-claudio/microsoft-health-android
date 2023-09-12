package com.microsoft.kapp.models.guidedworkout;

import android.content.Context;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.GuidedWorkoutPostGroupList;
import com.microsoft.kapp.services.healthandfitness.models.CircuitGroupType;
import com.microsoft.kapp.services.healthandfitness.models.CircuitType;
import com.microsoft.kapp.views.GuidedWorkouts.BasePostGuidedWorkoutViewHolder;
import com.microsoft.kapp.views.GuidedWorkouts.BlockHeaderViewHolder;
import com.microsoft.kapp.views.GuidedWorkouts.DataItemViewHolder;
import com.microsoft.kapp.views.GuidedWorkouts.GroupTypePostGWHeaderViewHolder;
import com.microsoft.kapp.views.GuidedWorkouts.SubGroupHeaderViewHolder;
import com.microsoft.kapp.views.GuidedWorkouts.TotalPostGWViewHolder;
import com.microsoft.krestsdk.models.WorkoutExerciseSequence;
import com.unnamed.b.atv.model.TreeNode;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class GuidedWorkoutPostModel {
    private static final String TAG = GuidedWorkoutPostModel.class.getSimpleName();
    final int groupTypeL2HeaderBackgroundColor;
    final int groupTypeL2HeaderTextColor;
    private boolean isDistanceHeigtMetric;
    private Context mContext;
    private boolean mIsSmartCountingWorkout;
    private TreeNode mRootTree;
    private GuidedWorkoutUnitType mGuidedWorkoutUnitType = GuidedWorkoutUnitType.Time;
    private final List<Cycle> mCyclesList = new ArrayList();

    public GuidedWorkoutPostModel(Context context, List<WorkoutExerciseSequence> exerciseList, boolean isSmartCounting, boolean isDistanceHeigtMetric) {
        this.mContext = context;
        this.mIsSmartCountingWorkout = isSmartCounting;
        this.isDistanceHeigtMetric = isDistanceHeigtMetric;
        this.groupTypeL2HeaderBackgroundColor = this.mContext.getResources().getColor(R.color.greySuperLight);
        this.groupTypeL2HeaderTextColor = this.mContext.getResources().getColor(R.color.black);
        try {
            restructureData(this.mCyclesList, exerciseList);
        } catch (Exception e) {
            KLog.e(TAG, "Fail to restructure data for Guided Workout Post Detail: " + e.toString());
        }
    }

    public TreeNode generateTree() {
        TreeNode root = TreeNode.root();
        if (this.mCyclesList != null) {
            for (int i = 0; i < this.mCyclesList.size(); i++) {
                Cycle cycle = this.mCyclesList.get(i);
                TreeNode warmupNode = null;
                TreeNode workoutNode = null;
                TreeNode cooldownNode = null;
                for (int j = 0; j < cycle.getBlockList().size(); j++) {
                    Block block = cycle.getBlockList().get(j);
                    if (block != null) {
                        if (block.getBlockType() == CircuitType.WarmUp) {
                            if (warmupNode == null) {
                                warmupNode = new TreeNode(block).setViewHolder(new BlockHeaderViewHolder(this.mContext, this.mGuidedWorkoutUnitType, this.isDistanceHeigtMetric));
                            }
                            warmupNode = addDataToBlock(warmupNode, block);
                        } else if (block.getBlockType() == CircuitType.CoolDown) {
                            if (cooldownNode == null) {
                                cooldownNode = new TreeNode(block).setViewHolder(new BlockHeaderViewHolder(this.mContext, this.mGuidedWorkoutUnitType, this.isDistanceHeigtMetric));
                            }
                            cooldownNode = addDataToBlock(cooldownNode, block);
                        } else {
                            if (workoutNode == null) {
                                workoutNode = new TreeNode(block).setViewHolder(new BlockHeaderViewHolder(this.mContext, this.mGuidedWorkoutUnitType, this.isDistanceHeigtMetric));
                            }
                            workoutNode = addDataToBlock(workoutNode, block);
                        }
                    }
                }
                if (warmupNode != null && warmupNode.getChildren() != null && warmupNode.getChildren().size() > 0) {
                    root.addChild(warmupNode);
                }
                if (workoutNode != null && workoutNode.getChildren() != null && workoutNode.getChildren().size() > 0) {
                    root.addChild(workoutNode);
                }
                if (cooldownNode != null && cooldownNode.getChildren() != null && cooldownNode.getChildren().size() > 0) {
                    root.addChild(cooldownNode);
                }
                if (root.getChildren() != null && root.getChildren().size() > 0) {
                    TreeNode node = new TreeNode(cycle).setViewHolder(new TotalPostGWViewHolder(this.mContext, this.mGuidedWorkoutUnitType, this.isDistanceHeigtMetric));
                    root.addChild(node);
                }
            }
        }
        this.mRootTree = root;
        return root;
    }

    public TreeNode generateTree(GuidedWorkoutUnitType guidedWorkoutUnitType) {
        this.mGuidedWorkoutUnitType = guidedWorkoutUnitType;
        return generateTree();
    }

    private TreeNode addDataToBlock(TreeNode blockNode, Block block) {
        GuidedWorkoutPostGroupList groups = block.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            if (group != null) {
                CircuitGroupType key = group.getCircuitGroupType();
                if (group.getIsRestCircuit()) {
                    key = CircuitGroupType.Rest;
                }
                TreeNode groupNode = new TreeNode(group).setViewHolder(new GroupTypePostGWHeaderViewHolder(this.mContext, block.getGroups().getGroupTypeCount(key), this.mGuidedWorkoutUnitType, this.isDistanceHeigtMetric));
                if (key == CircuitGroupType.Rest || key == CircuitGroupType.FreePlay) {
                    groupNode.getViewHolder().setExpandAble(false);
                } else {
                    groupNode = addDataToGroup(groupNode, group);
                }
                if (groupNode.getChildren() != null) {
                    blockNode.addChild(groupNode);
                }
            }
        }
        return blockNode;
    }

    private TreeNode addDataToGroup(TreeNode groupNode, Group group) {
        TreeNode subgroupNode;
        if (group != null && group.getSubgroupsList() != null) {
            for (int i = 0; i < group.getSubgroupsList().size(); i++) {
                Subgroup subgroup = group.getSubgroupsList().get(i);
                if (subgroup != null) {
                    TreeNode restSubgroupNode = null;
                    List<ExerciseItem> items = subgroup.getExercieItemList();
                    if (group.getCircuitGroupType() == CircuitGroupType.List && items != null && items.size() > 0 && items.get(items.size() - 1).isIsRest()) {
                        ExerciseItem restItem = subgroup.removeItem(items.size() - 1);
                        restSubgroupNode = new TreeNode(restItem).setViewHolder(new DataItemViewHolder(this.mContext, this.mGuidedWorkoutUnitType, this.isDistanceHeigtMetric, true));
                    }
                    if (group.getCircuitGroupType() == CircuitGroupType.List && subgroup.hasSingleExercise()) {
                        subgroupNode = new TreeNode(subgroup).setViewHolder(new DataItemViewHolder(this.mContext, this.mGuidedWorkoutUnitType, this.isDistanceHeigtMetric));
                    } else {
                        subgroupNode = addDataToSubgroup(subgroup);
                    }
                    groupNode.addChild(subgroupNode);
                    if (restSubgroupNode != null) {
                        groupNode.addChild(restSubgroupNode);
                    }
                }
            }
        }
        return groupNode;
    }

    public void updateTreeWithUnitType(GuidedWorkoutUnitType guidedWorkoutUnitType) {
        this.mGuidedWorkoutUnitType = guidedWorkoutUnitType;
        updateTreeWithUnitType(this.mRootTree, guidedWorkoutUnitType);
    }

    private void updateTreeWithUnitType(TreeNode node, GuidedWorkoutUnitType guidedWorkoutUnitType) {
        if (node != null) {
            if (node.getViewHolder() instanceof BasePostGuidedWorkoutViewHolder) {
                BasePostGuidedWorkoutViewHolder viewHolder = (BasePostGuidedWorkoutViewHolder) node.getViewHolder();
                viewHolder.updateUnit(guidedWorkoutUnitType);
            }
            List<TreeNode> children = node.getChildren();
            if (children != null) {
                for (int i = 0; i < children.size(); i++) {
                    updateTreeWithUnitType(children.get(i), guidedWorkoutUnitType);
                }
            }
        }
    }

    private TreeNode addDataToSubgroup(Subgroup subgroup) {
        if (subgroup.getExercieItemList() != null) {
            TreeNode subgroupNode = new TreeNode(subgroup).setViewHolder(new SubGroupHeaderViewHolder(this.mContext, this.mGuidedWorkoutUnitType, this.isDistanceHeigtMetric));
            for (int j = 0; j < subgroup.getExercieItemList().size(); j++) {
                ExerciseItem item = subgroup.getExercieItemList().get(j);
                if (item != null) {
                    TreeNode itemNode = new TreeNode(item).setViewHolder(new DataItemViewHolder(this.mContext, this.mGuidedWorkoutUnitType, this.isDistanceHeigtMetric));
                    subgroupNode.addChild(itemNode);
                }
            }
            return subgroupNode;
        }
        return new TreeNode(subgroup).setViewHolder(new DataItemViewHolder(this.mContext, this.mGuidedWorkoutUnitType, this.isDistanceHeigtMetric));
    }

    private List<Cycle> restructureData(List<Cycle> cyclesList, List<WorkoutExerciseSequence> exerciseList) {
        if (cyclesList == null) {
            KLog.e(TAG, "Cycles list cannot be null in post guidedWorkout!");
            return null;
        } else if (exerciseList == null) {
            KLog.e(TAG, "WorkoutExerciseSequence list cannot be null in post guidedWorkout!");
            return null;
        } else {
            for (WorkoutExerciseSequence exercise : exerciseList) {
                if (exercise.getExerciseOrdinal() != 0) {
                    int cycleIndex = exercise.getCycleOrdinal() - 1;
                    if (cyclesList.size() <= cycleIndex) {
                        for (int i = cyclesList.size(); i <= cycleIndex; i++) {
                            cyclesList.add(new Cycle(this.mContext, String.format(getString(R.string.guided_workout_details_cycle_name), Integer.valueOf(i + 1))));
                        }
                    }
                    if (cycleIndex >= 0) {
                        Cycle cycle = cyclesList.get(cycleIndex);
                        cycle.setSmartCountingWorkout(this.mIsSmartCountingWorkout);
                        cycle.accumulateCalories(exercise.getCaloriesBurned());
                        cycle.accumulateDuration(exercise.getDuration());
                        cycle.accumulateRepetitions(exercise);
                        cycle.accumulateDistance(exercise);
                        cycle.addExercise(exercise);
                    } else {
                        KLog.d(TAG, "Error: Cycle index started at 0!! Cycle index should be 1-based and start a 1!");
                    }
                }
            }
            return cyclesList;
        }
    }

    private String getString(int resId) {
        return this.mContext.getString(resId);
    }
}
