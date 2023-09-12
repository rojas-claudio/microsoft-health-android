package com.microsoft.kapp.views.GuidedWorkouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.guidedworkout.Cycle;
import com.microsoft.kapp.models.guidedworkout.GuidedWorkoutUnitType;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public class TotalPostGWViewHolder extends BasePostGuidedWorkoutViewHolder<Cycle> {
    private TextView mEstimatedRepsView;

    public TotalPostGWViewHolder(Context context) {
        super(context);
    }

    public TotalPostGWViewHolder(Context context, GuidedWorkoutUnitType unitType, boolean isDistanceHeightMetric) {
        super(context, unitType, isDistanceHeightMetric);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public View createNodeView(TreeNode node, ViewGroup container, Cycle cycle) {
        this.mValue = cycle;
        View view = LayoutInflater.from(this.context).inflate(R.layout.treeview_guided_workout_post_total, (ViewGroup) null, false);
        this.mUnitView = (TextView) ViewUtils.getValidView(view, R.id.txtPostGuidedWorkoutTotalUnit, TextView.class);
        this.mEstimatedRepsView = (TextView) ViewUtils.getValidView(view, R.id.txtPostGuidedWorkoutTotalEstimatedReps, TextView.class);
        String unitText = GuidedWorkoutUtils.getUnitTextForPostGuidedWorkout(this.context, cycle, this.mUnitType, this.isDistanceHeightMetric);
        if (unitText != null) {
            this.mUnitView.setText(unitText);
            this.mUnitView.setVisibility(0);
        }
        if (this.mUnitType == GuidedWorkoutUnitType.Reps && cycle.isRepsEstimated()) {
            this.mEstimatedRepsView.setVisibility(0);
        }
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        return view;
    }

    @Override // com.microsoft.kapp.views.GuidedWorkouts.BasePostGuidedWorkoutViewHolder
    public boolean updateUnit(GuidedWorkoutUnitType unitType) {
        boolean result = super.updateUnit(unitType);
        if (this.mEstimatedRepsView != null) {
            if (this.mValue != 0 && unitType == GuidedWorkoutUnitType.Reps && ((Cycle) this.mValue).isRepsEstimated()) {
                this.mEstimatedRepsView.setVisibility(0);
            } else {
                this.mEstimatedRepsView.setVisibility(8);
            }
        }
        return result;
    }
}
