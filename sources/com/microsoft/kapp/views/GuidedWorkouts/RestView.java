package com.microsoft.kapp.views.GuidedWorkouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.picasso.utils.PicassoWrapper;
import com.microsoft.kapp.utils.ViewUtils;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public class RestView extends TreeNode.BaseNodeViewHolder<String> {
    private final boolean mSkipped;

    public RestView(Context context) {
        this(context, false);
    }

    public RestView(Context context, boolean skipped) {
        super(context);
        this.mSkipped = skipped;
    }

    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public View createNodeView(TreeNode node, ViewGroup container, String duration) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.treeview_guided_workout_exercise, (ViewGroup) null);
        ImageView image = (ImageView) ViewUtils.getValidView(view, R.id.workout_video_button, ImageView.class);
        TextView title = (TextView) ViewUtils.getValidView(view, R.id.workout_step_name, TextView.class);
        TextView details = (TextView) ViewUtils.getValidView(view, R.id.workout_step_reps, TextView.class);
        TextView videoButtonBackground = (TextView) ViewUtils.getValidView(view, R.id.workout_video_button_play_white_background, TextView.class);
        TextView videoButton = (TextView) ViewUtils.getValidView(view, R.id.workout_video_button_play, TextView.class);
        title.setText(this.mSkipped ? this.context.getString(R.string.workout_exercise_rest_step_skipped) : this.context.getString(R.string.workout_exercise_rest_step));
        details.setText(duration);
        PicassoWrapper.with(this.context).load(R.drawable.guided_workouts_rest_image).fit().centerInside().into(image);
        videoButton.setVisibility(8);
        videoButtonBackground.setVisibility(8);
        return view;
    }
}
