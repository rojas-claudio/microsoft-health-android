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
public class DataItemViewHolder extends BasePostGuidedWorkoutViewHolder<BaseSetExercise> {
    final int dataItemBackgroundColor;
    private boolean isShowSeperator;

    public DataItemViewHolder(Context context) {
        super(context);
        this.dataItemBackgroundColor = context.getResources().getColor(R.color.white);
    }

    public DataItemViewHolder(Context context, GuidedWorkoutUnitType unitType, boolean isDistanceHeightMetric) {
        super(context, unitType, isDistanceHeightMetric);
        this.dataItemBackgroundColor = context.getResources().getColor(R.color.white);
    }

    public DataItemViewHolder(Context context, GuidedWorkoutUnitType unitType, boolean isDistanceHeightMetric, boolean isShowSeperator) {
        this(context, unitType, isDistanceHeightMetric);
        this.isShowSeperator = isShowSeperator;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public View createNodeView(TreeNode node, ViewGroup container, BaseSetExercise baseSetExercise) {
        this.mValue = baseSetExercise;
        View view = LayoutInflater.from(this.context).inflate(R.layout.treeview_guided_workout_post_data_item, container, false);
        View separatorView = (View) ViewUtils.getValidView(view, R.id.txtGuidedWorkoutDataItemSeparator, View.class);
        view.setBackgroundColor(this.dataItemBackgroundColor);
        if (baseSetExercise != 0) {
            TextView titleView = (TextView) ViewUtils.getValidView(view, R.id.txtDataItemTitle, TextView.class);
            titleView.setText(baseSetExercise.getName());
            this.mUnitView = (TextView) ViewUtils.getValidView(view, R.id.txtDataItemUnit, TextView.class);
            String unitText = GuidedWorkoutUtils.getUnitTextForPostGuidedWorkout(this.context, baseSetExercise, this.mUnitType, this.isDistanceHeightMetric);
            if (unitText != null) {
                this.mUnitView.setText(unitText);
                this.mUnitView.setVisibility(0);
            }
        }
        if (this.isShowSeperator) {
            separatorView.setVisibility(0);
        }
        return view;
    }
}
