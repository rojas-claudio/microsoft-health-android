package com.microsoft.kapp.views.GuidedWorkouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.guidedworkout.BaseSetExercise;
import com.microsoft.kapp.models.guidedworkout.GuidedWorkoutUnitType;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public class SubGroupHeaderViewHolder extends BasePostGuidedWorkoutViewHolder<BaseSetExercise> {
    final int groupTypeL2HeaderBackgroundColor;
    final int groupTypeL2HeaderTextColor;
    TextView mExpandView;

    public SubGroupHeaderViewHolder(Context context) {
        super(context);
        this.groupTypeL2HeaderBackgroundColor = context.getResources().getColor(R.color.greySuperLight);
        this.groupTypeL2HeaderTextColor = context.getResources().getColor(R.color.black);
    }

    public SubGroupHeaderViewHolder(Context context, GuidedWorkoutUnitType unitType, boolean isDistanceHeightMetric) {
        super(context, unitType, isDistanceHeightMetric);
        this.groupTypeL2HeaderBackgroundColor = context.getResources().getColor(R.color.greySuperLight);
        this.groupTypeL2HeaderTextColor = context.getResources().getColor(R.color.black);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public View createNodeView(TreeNode node, ViewGroup container, BaseSetExercise baseSetExercise) {
        this.mNode = node;
        this.mValue = baseSetExercise;
        View view = LayoutInflater.from(this.context).inflate(R.layout.treeview_guided_workout_sub_group_header, container, false);
        TextView titleView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderTitle, TextView.class);
        this.mUnitView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderUnit, TextView.class);
        this.mExpandView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderExpandIcon, TextView.class);
        String title = baseSetExercise.getName();
        view.setBackgroundColor(this.groupTypeL2HeaderBackgroundColor);
        titleView.setText(title);
        titleView.setTextColor(this.groupTypeL2HeaderTextColor);
        this.mExpandView.setTextColor(this.groupTypeL2HeaderTextColor);
        String unitText = GuidedWorkoutUtils.getUnitTextForPostGuidedWorkout(this.context, baseSetExercise, this.mUnitType, this.isDistanceHeightMetric);
        if (unitText != null) {
            this.mUnitView.setText(unitText);
            this.mUnitView.setTextColor(this.groupTypeL2HeaderTextColor);
            this.mUnitView.setVisibility(0);
        }
        node.setClickListener(new TreeNode.TreeNodeClickListener() { // from class: com.microsoft.kapp.views.GuidedWorkouts.SubGroupHeaderViewHolder.1
            @Override // com.unnamed.b.atv.model.TreeNode.TreeNodeClickListener
            public void onClick(TreeNode clickedNode, Object value) {
                SubGroupHeaderViewHolder.this.setExpandedState(clickedNode);
            }
        });
        setExpandedState(node);
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setExpandedState(TreeNode node) {
        if (this.mExpandView != null) {
            if (node.isExpanded()) {
                this.mExpandView.setText(this.context.getString(R.string.glyph_triangle_right));
            } else {
                this.mExpandView.setText(this.context.getString(R.string.glyph_triangle_down));
            }
        }
    }
}
