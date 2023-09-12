package com.microsoft.kapp.views.GuidedWorkouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutCircuit;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public class GroupDescriptionHeader extends TreeNode.BaseNodeViewHolder<WorkoutCircuit> {
    public GroupDescriptionHeader(Context context) {
        super(context);
    }

    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public View createNodeView(TreeNode node, ViewGroup container, WorkoutCircuit circuit) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.treeview_guided_workout_group_description_header, (ViewGroup) null);
        TextView descriptionText = (TextView) ViewUtils.getValidView(view, R.id.txtGroupDescription, TextView.class);
        String description = GuidedWorkoutUtils.getCircuitGroupDescription(this.context, circuit);
        descriptionText.setText(description);
        return view;
    }
}
