package com.microsoft.kapp.views.GuidedWorkouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.services.healthandfitness.models.CircuitGroupType;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutCircuit;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public class GroupTypeHeader extends TreeNode.BaseNodeViewHolder<WorkoutCircuit> {
    final int mCount;
    TextView mExpandView;
    TextView mHeaderExpandView;

    public GroupTypeHeader(Context context, int count) {
        super(context);
        this.mCount = count;
    }

    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public View createNodeView(TreeNode node, ViewGroup container, WorkoutCircuit circuit) {
        this.alternativeView = getNodeView(node, circuit, true);
        return getNodeView(node, circuit, false);
    }

    private View getNodeView(TreeNode node, WorkoutCircuit circuit, boolean isHeader) {
        TextView expandView;
        View view = LayoutInflater.from(this.context).inflate(R.layout.treeview_guided_workout_group_type_header, (ViewGroup) null, false);
        TextView titleView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderTitle, TextView.class);
        TextView iconView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderIcon, TextView.class);
        TextView durationView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderDuration, TextView.class);
        if (!isHeader) {
            if (this.mExpandView == null) {
                this.mExpandView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderExpandIcon, TextView.class);
            }
            expandView = this.mExpandView;
        } else {
            if (this.mHeaderExpandView == null) {
                this.mHeaderExpandView = (TextView) ViewUtils.getValidView(view, R.id.txtGroupTypeHeaderExpandIcon, TextView.class);
            }
            expandView = this.mHeaderExpandView;
        }
        int color = GuidedWorkoutUtils.getCircuitHeaderColor(this.context, circuit.getCircuitType(), true);
        String icon = GuidedWorkoutUtils.getCircuitIcon(this.context, circuit.getGroupType());
        String title = GuidedWorkoutUtils.getCircuitGroupTypeTitle(this.context, circuit.getGroupType());
        if (circuit.getGroupType() == CircuitGroupType.Rest || circuit.getGroupType() == CircuitGroupType.FreePlay) {
            expandView.setVisibility(8);
        } else {
            if (this.mCount > 1) {
                title = String.format("%s %d", title, Integer.valueOf(circuit.getGroupTypeIndex()));
            }
            node.setClickListener(new TreeNode.TreeNodeClickListener() { // from class: com.microsoft.kapp.views.GuidedWorkouts.GroupTypeHeader.1
                @Override // com.unnamed.b.atv.model.TreeNode.TreeNodeClickListener
                public void onClick(TreeNode clickedNode, Object value) {
                    GroupTypeHeader.this.setExpandedState(GroupTypeHeader.this.mExpandView, clickedNode);
                    GroupTypeHeader.this.setExpandedState(GroupTypeHeader.this.mHeaderExpandView, clickedNode);
                }
            });
            setExpandedState(expandView, node);
        }
        String duration = GuidedWorkoutUtils.getGroupDurationString(this.context, circuit);
        view.setBackgroundColor(color);
        titleView.setText(title);
        iconView.setText(icon);
        durationView.setText(duration);
        durationView.setVisibility(0);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, (int) this.context.getResources().getDimension(R.dimen.guided_workout_circuit_header_child_height)));
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setExpandedState(TextView expandView, TreeNode node) {
        if (expandView != null) {
            if (node.isExpanded()) {
                expandView.setText(this.context.getString(R.string.glyph_triangle_right));
            } else {
                expandView.setText(this.context.getString(R.string.glyph_triangle_down));
            }
        }
    }
}
