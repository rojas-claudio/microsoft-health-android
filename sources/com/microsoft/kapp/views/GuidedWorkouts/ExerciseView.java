package com.microsoft.kapp.views.GuidedWorkouts;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.picasso.utils.PicassoWrapper;
import com.microsoft.kapp.services.bedrock.BedrockImageServiceUtils;
import com.microsoft.kapp.services.healthandfitness.models.CircuitGroupType;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutExercise;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.DialogManagerImpl;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.unnamed.b.atv.model.TreeNode;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class ExerciseView extends TreeNode.BaseNodeViewHolder<WorkoutExercise> {
    private CircuitGroupType mGroupType;

    public ExerciseView(Context context, CircuitGroupType groupType) {
        super(context);
        this.mGroupType = groupType;
    }

    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public View createNodeView(TreeNode node, ViewGroup container, WorkoutExercise exercise) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.treeview_guided_workout_exercise, (ViewGroup) null);
        ImageView image = (ImageView) ViewUtils.getValidView(view, R.id.workout_video_button, ImageView.class);
        TextView title = (TextView) ViewUtils.getValidView(view, R.id.workout_step_name, TextView.class);
        TextView details = (TextView) ViewUtils.getValidView(view, R.id.workout_step_reps, TextView.class);
        TextView videoButtonBackground = (TextView) ViewUtils.getValidView(view, R.id.workout_video_button_play_white_background, TextView.class);
        TextView videoButton = (TextView) ViewUtils.getValidView(view, R.id.workout_video_button_play, TextView.class);
        int thumbnailImageWidth = this.context.getResources().getDimensionPixelSize(R.dimen.guided_workout_thumbnail_width);
        int thumbnailImageHight = this.context.getResources().getDimensionPixelSize(R.dimen.guided_workout_thumbnail_height);
        title.setText(exercise.getName());
        String detailsText = GuidedWorkoutUtils.getRepsDescriptionFromExercise(this.context, exercise, this.mGroupType);
        details.setText(detailsText);
        PicassoWrapper.with(this.context).load(BedrockImageServiceUtils.createSizedImageUrl(exercise.getThumbnail(), thumbnailImageWidth, thumbnailImageHight)).fit().centerInside().placeholder(R.drawable.workout_thumbnail).into(image);
        if (exercise.getVideoId() != null && !exercise.getVideoId().isEmpty()) {
            image.setOnClickListener(new AnonymousClass1(exercise));
        } else {
            videoButton.setVisibility(8);
            videoButtonBackground.setVisibility(8);
        }
        return view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.microsoft.kapp.views.GuidedWorkouts.ExerciseView$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AtomicBoolean loading = new AtomicBoolean(false);
        String path;
        final /* synthetic */ WorkoutExercise val$exercise;

        AnonymousClass1(WorkoutExercise workoutExercise) {
            this.val$exercise = workoutExercise;
            this.path = String.format(Constants.MSN_VIDEO_URL_PATTERN, this.val$exercise.getVideoId());
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (!CommonUtils.isNetworkAvailable(ExerciseView.this.context)) {
                DialogManagerImpl.getDialogManager(ExerciseView.this.context).showNetworkErrorDialog(ExerciseView.this.context);
            } else if (this.loading.compareAndSet(false, true)) {
                WindowManager wm = (WindowManager) ExerciseView.this.context.getSystemService("window");
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                GuidedWorkoutUtils.playVideo(this.path, width, height, ExerciseView.this.context, new Callback<Void>() { // from class: com.microsoft.kapp.views.GuidedWorkouts.ExerciseView.1.1
                    @Override // com.microsoft.kapp.Callback
                    public void onError(Exception ex) {
                        AnonymousClass1.this.loading.set(false);
                    }

                    @Override // com.microsoft.kapp.Callback
                    public void callback(Void result) {
                        AnonymousClass1.this.loading.set(false);
                    }
                });
                HashMap<String, String> telemetryProperties = new HashMap<>();
                telemetryProperties.put(TelemetryConstants.Events.GuidedWorkoutWatchVideo.Dimensions.EXERCISE_ID, this.val$exercise.getId());
                telemetryProperties.put(TelemetryConstants.Events.GuidedWorkoutWatchVideo.Dimensions.EXERCISE_NUMBER, this.val$exercise.getCode());
                telemetryProperties.put(TelemetryConstants.Events.GuidedWorkoutWatchVideo.Dimensions.VIDEO_ID, this.val$exercise.getVideoId());
                Telemetry.logEvent(TelemetryConstants.Events.GuidedWorkoutWatchVideo.EVENT_NAME, telemetryProperties, null);
            }
        }
    }
}
