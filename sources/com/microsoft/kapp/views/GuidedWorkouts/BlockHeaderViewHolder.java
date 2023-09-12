package com.microsoft.kapp.views.GuidedWorkouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.guidedworkout.Block;
import com.microsoft.kapp.models.guidedworkout.GuidedWorkoutUnitType;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public class BlockHeaderViewHolder extends BasePostGuidedWorkoutViewHolder<Block> {
    private TextView mExpandView;

    public BlockHeaderViewHolder(Context context) {
        super(context);
    }

    public BlockHeaderViewHolder(Context context, GuidedWorkoutUnitType unitType, boolean isDistanceHeightMetric) {
        super(context, unitType, isDistanceHeightMetric);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public View createNodeView(TreeNode node, ViewGroup container, Block circuit) {
        this.mValue = circuit;
        View view = LayoutInflater.from(this.context).inflate(R.layout.treeview_guided_workout_circuit_type_header, (ViewGroup) null, false);
        TextView textView = (TextView) ViewUtils.getValidView(view, R.id.txtCircuitHeaderTitle, TextView.class);
        this.mUnitView = (TextView) ViewUtils.getValidView(view, R.id.txtCircuitHeaderUnit, TextView.class);
        this.mExpandView = (TextView) ViewUtils.getValidView(view, R.id.txtCircuitHeaderExpandIcon, TextView.class);
        String title = GuidedWorkoutUtils.getCircuitTypeString(this.context, circuit.getBlockType());
        int color = GuidedWorkoutUtils.getCircuitHeaderColor(this.context, circuit.getBlockType(), false);
        view.setBackgroundColor(color);
        textView.setText(title);
        node.setClickListener(new TreeNode.TreeNodeClickListener() { // from class: com.microsoft.kapp.views.GuidedWorkouts.BlockHeaderViewHolder.1
            @Override // com.unnamed.b.atv.model.TreeNode.TreeNodeClickListener
            public void onClick(TreeNode clickedNode, Object value) {
                BlockHeaderViewHolder.this.setExpandedState(clickedNode);
            }
        });
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, (int) this.context.getResources().getDimension(R.dimen.guided_workout_circuit_header_height)));
        setExpandedState(node);
        String unitText = GuidedWorkoutUtils.getUnitTextForPostGuidedWorkout(this.context, circuit, this.mUnitType, this.isDistanceHeightMetric);
        if (unitText != null) {
            this.mUnitView.setText(unitText);
            this.mUnitView.setTextColor(-1);
            this.mUnitView.setVisibility(0);
        }
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
