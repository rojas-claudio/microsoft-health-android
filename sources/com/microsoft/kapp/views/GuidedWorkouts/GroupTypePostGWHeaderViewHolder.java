package com.microsoft.kapp.views.GuidedWorkouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.guidedworkout.Group;
import com.microsoft.kapp.models.guidedworkout.GuidedWorkoutUnitType;
import com.microsoft.kapp.services.healthandfitness.models.CircuitGroupType;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public class GroupTypePostGWHeaderViewHolder extends BasePostGuidedWorkoutViewHolder<Group> {
    final int mCount;
    TextView mExpandView;

    public GroupTypePostGWHeaderViewHolder(Context context, int count) {
        super(context);
        this.mCount = count;
    }

    public GroupTypePostGWHeaderViewHolder(Context context, int count, GuidedWorkoutUnitType unitType, boolean isDistanceHeightMetric) {
        super(context, unitType, isDistanceHeightMetric);
        this.mCount = count;
    }

    public GuidedWorkoutUnitType getUnitType() {
        return this.mUnitType;
    }

    public void setUnitType(GuidedWorkoutUnitType unitType) {
        this.mUnitType = unitType;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public View createNodeView(TreeNode node, ViewGroup container, Group group) {
        this.mNode = node;
        this.mValue = group;
        View view = getNodeView(node, group);
        updateAlternativeView(false);
        return view;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private View getNodeView(TreeNode node, Group group) {
        String title;
        this.mValue = group;
        View view = LayoutInflater.from(this.context).inflate(R.layout.treeview_guided_workout_group_type_header, (ViewGroup) null, false);
        TextView titleView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderTitle, TextView.class);
        TextView iconView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderIcon, TextView.class);
        this.mUnitView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderUnit, TextView.class);
        this.mExpandView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderExpandIcon, TextView.class);
        if (group.getIsRestCircuit()) {
            title = GuidedWorkoutUtils.getCircuitGroupTypeTitle(this.context, CircuitGroupType.Rest);
        } else {
            title = GuidedWorkoutUtils.getCircuitGroupTypeTitle(this.context, group.getCircuitGroupType());
        }
        if (this.mCount > 1) {
            title = String.format("%s %d", title, Integer.valueOf(group.getCircuitGroupIndex()));
        }
        int color = GuidedWorkoutUtils.getCircuitHeaderColor(this.context, group.getCircuitType(), true);
        String icon = GuidedWorkoutUtils.getCircuitIcon(this.context, group.getCircuitGroupType());
        view.setBackgroundColor(color);
        titleView.setText(title);
        iconView.setText(icon);
        if (!this.isExpandAble) {
            this.mExpandView.setVisibility(4);
        }
        node.setClickListener(new TreeNode.TreeNodeClickListener() { // from class: com.microsoft.kapp.views.GuidedWorkouts.GroupTypePostGWHeaderViewHolder.1
            @Override // com.unnamed.b.atv.model.TreeNode.TreeNodeClickListener
            public void onClick(TreeNode clickedNode, Object value) {
                if (GroupTypePostGWHeaderViewHolder.this.setExpandedState(clickedNode)) {
                    GroupTypePostGWHeaderViewHolder.this.updateAlternativeView(false);
                }
            }
        });
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, (int) this.context.getResources().getDimension(R.dimen.guided_workout_circuit_header_child_height)));
        String unitText = GuidedWorkoutUtils.getUnitTextForPostGuidedWorkout(this.context, group, this.mUnitType, this.isDistanceHeightMetric);
        if (unitText != null) {
            this.mUnitView.setText(unitText);
            this.mUnitView.setTextColor(-1);
            this.mUnitView.setVisibility(0);
        }
        setExpandedState(node);
        return view;
    }

    private View getHeaderNodeView(TreeNode node, Group group, boolean isFromUnitChange) {
        String title;
        View view = LayoutInflater.from(this.context).inflate(R.layout.treeview_guided_workout_group_type_header, (ViewGroup) null, false);
        TextView titleView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderTitle, TextView.class);
        TextView iconView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderIcon, TextView.class);
        TextView unitView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderUnit, TextView.class);
        TextView expandView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderExpandIcon, TextView.class);
        if (group.getIsRestCircuit()) {
            title = GuidedWorkoutUtils.getCircuitGroupTypeTitle(this.context, CircuitGroupType.Rest);
        } else {
            title = GuidedWorkoutUtils.getCircuitGroupTypeTitle(this.context, group.getCircuitGroupType());
        }
        if (this.mCount > 1) {
            title = String.format("%s %d", title, Integer.valueOf(group.getCircuitGroupIndex()));
        }
        int color = GuidedWorkoutUtils.getCircuitHeaderColor(this.context, group.getCircuitType(), true);
        String icon = GuidedWorkoutUtils.getCircuitIcon(this.context, group.getCircuitGroupType());
        view.setBackgroundColor(color);
        titleView.setText(title);
        iconView.setText(icon);
        if (!this.isExpandAble) {
            expandView.setVisibility(4);
        }
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, (int) this.context.getResources().getDimension(R.dimen.guided_workout_circuit_header_child_height)));
        String unitText = GuidedWorkoutUtils.getUnitTextForPostGuidedWorkout(this.context, group, this.mUnitType, this.isDistanceHeightMetric);
        if (unitText != null) {
            unitView.setText(unitText);
            unitView.setTextColor(-1);
            unitView.setVisibility(0);
        }
        if (expandView != null && this.isExpandAble) {
            boolean isExpanded = node.isExpanded();
            if (isFromUnitChange) {
                isExpanded = !isExpanded;
            }
            if (isExpanded) {
                expandView.setText(this.context.getString(R.string.glyph_triangle_right));
            } else {
                expandView.setText(this.context.getString(R.string.glyph_triangle_down));
            }
        }
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setExpandedState(TreeNode node) {
        if (this.mExpandView != null && this.isExpandAble) {
            if (node.isExpanded()) {
                this.mExpandView.setText(this.context.getString(R.string.glyph_triangle_right));
            } else {
                this.mExpandView.setText(this.context.getString(R.string.glyph_triangle_down));
            }
            return true;
        }
        return false;
    }

    public void updateAlternativeView(boolean isFromUnitChange) {
        if (this.mNode != null && this.mValue != 0) {
            this.alternativeView = getHeaderNodeView(this.mNode, (Group) this.mValue, isFromUnitChange);
            this.mNode.invalidate();
        }
    }

    @Override // com.microsoft.kapp.views.GuidedWorkouts.BasePostGuidedWorkoutViewHolder
    public boolean updateUnit(GuidedWorkoutUnitType unitType) {
        boolean result = super.updateUnit(unitType);
        updateAlternativeView(true);
        return result;
    }
}
