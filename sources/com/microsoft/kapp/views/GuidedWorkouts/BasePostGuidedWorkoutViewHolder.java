package com.microsoft.kapp.views.GuidedWorkouts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.models.guidedworkout.BaseGuidedWorkoutItem;
import com.microsoft.kapp.models.guidedworkout.BaseSetExercise;
import com.microsoft.kapp.models.guidedworkout.GuidedWorkoutUnitType;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public abstract class BasePostGuidedWorkoutViewHolder<E> extends TreeNode.BaseNodeViewHolder<E> {
    protected boolean isDistanceHeightMetric;
    protected TextView mHeaderExpandeGlyph;
    protected TextView mHeaderUnitView;
    protected GuidedWorkoutUnitType mUnitType;
    protected TextView mUnitView;

    public BasePostGuidedWorkoutViewHolder(Context context) {
        super(context);
        this.mUnitType = GuidedWorkoutUnitType.Hide;
    }

    public BasePostGuidedWorkoutViewHolder(Context context, GuidedWorkoutUnitType unitType, boolean isDistanceHeightMetric) {
        super(context);
        this.mUnitType = GuidedWorkoutUnitType.Hide;
        this.mUnitType = unitType;
        this.isDistanceHeightMetric = isDistanceHeightMetric;
    }

    public boolean updateUnit(GuidedWorkoutUnitType unitType) {
        this.mUnitType = unitType;
        if (this.mValue == null || !(this.mValue instanceof BaseGuidedWorkoutItem)) {
            return false;
        }
        BaseGuidedWorkoutItem exercise = (BaseGuidedWorkoutItem) this.mValue;
        String unitText = GuidedWorkoutUtils.getUnitTextForPostGuidedWorkout(this.context, exercise, unitType, this.isDistanceHeightMetric);
        if (unitText != null) {
            this.mUnitView.setText(unitText);
            this.mUnitView.setVisibility(0);
            return true;
        }
        return false;
    }

    public View createNodeView(TreeNode node, ViewGroup container, BaseSetExercise baseSetExercise) {
        return null;
    }
}
