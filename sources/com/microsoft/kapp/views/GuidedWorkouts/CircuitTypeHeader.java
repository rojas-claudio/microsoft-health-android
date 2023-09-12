package com.microsoft.kapp.views.GuidedWorkouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.services.healthandfitness.models.CircuitType;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public class CircuitTypeHeader extends TreeNode.BaseNodeViewHolder<CircuitType> {
    TextView mExpandView;

    public CircuitTypeHeader(Context context) {
        super(context);
    }

    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public View createNodeView(TreeNode node, ViewGroup container, CircuitType circuitType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.treeview_guided_workout_circuit_type_header, (ViewGroup) null, false);
        TextView textView = (TextView) ViewUtils.getValidView(view, R.id.txtCircuitHeaderTitle, TextView.class);
        this.mExpandView = (TextView) ViewUtils.getValidView(view, R.id.txtCircuitHeaderExpandIcon, TextView.class);
        String title = GuidedWorkoutUtils.getCircuitTypeString(this.context, circuitType);
        int color = GuidedWorkoutUtils.getCircuitHeaderColor(this.context, circuitType, false);
        view.setBackgroundColor(color);
        textView.setText(title);
        node.setClickListener(new TreeNode.TreeNodeClickListener() { // from class: com.microsoft.kapp.views.GuidedWorkouts.CircuitTypeHeader.1
            @Override // com.unnamed.b.atv.model.TreeNode.TreeNodeClickListener
            public void onClick(TreeNode clickedNode, Object value) {
                CircuitTypeHeader.this.setExpandedState(clickedNode);
            }
        });
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, (int) this.context.getResources().getDimension(R.dimen.guided_workout_circuit_header_height)));
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
