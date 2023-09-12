package com.microsoft.kapp.adapters;

import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.picasso.utils.PicassoWrapper;
import com.microsoft.kapp.services.bedrock.BedrockImageServiceUtils;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutCircuit;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutExercise;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.DialogManagerImpl;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.LockedStringUtils;
import com.microsoft.kapp.utils.StringUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.krestsdk.models.CompletionType;
import com.microsoft.krestsdk.models.ExerciseTraversalType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class WorkoutStepListAdapterV1 extends BaseExpandableListAdapter {
    private static String HOURS_STRING;
    private static String HOUR_STRING;
    private static String MINUTES_STRING;
    private static String MINUTE_STRING;
    private static String SECONDS_STRING;
    private static String SECOND_STRING;
    private static final String TAG = WorkoutStepListAdapterV1.class.getSimpleName();
    private static int mThumbnailImageHight;
    private static int mThumbnailImageWidth;
    private final WorkoutCircuit[] mCircuitList;
    private final Context mContext;
    private final boolean mIsClassWorkout;

    /* loaded from: classes.dex */
    public static class ChildViewHolder {
        public CustomFontTextView repsCountText;
        public CustomFontTextView repsHeaderText;
        public ImageView videoButton;
        public TextView videoButtonPlay;
        public TextView videoButtonPlayBackground;
        public FrameLayout videoPlaceHolder;
    }

    /* loaded from: classes.dex */
    public static class GroupViewHolder {
        public CustomFontTextView circuitDescription;
        public CustomFontTextView circuitHeaderText;
    }

    public WorkoutStepListAdapterV1(Context context, WorkoutCircuit[] circuitList) {
        Validate.notNull(circuitList, "circuitList cannot be null");
        this.mContext = context;
        this.mCircuitList = circuitList;
        this.mIsClassWorkout = isClassWorkout(circuitList);
        mThumbnailImageWidth = context.getResources().getDimensionPixelSize(R.dimen.guided_workout_thumbnail_width);
        mThumbnailImageHight = context.getResources().getDimensionPixelSize(R.dimen.guided_workout_thumbnail_height);
        SECOND_STRING = context.getString(R.string.seconds_singular);
        MINUTE_STRING = context.getString(R.string.minutes_singular);
        HOUR_STRING = context.getString(R.string.hours_singular);
        SECONDS_STRING = context.getString(R.string.seconds_plural);
        MINUTES_STRING = context.getString(R.string.minutes_plural);
        HOURS_STRING = context.getString(R.string.hours_plural);
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        if (this.mIsClassWorkout) {
            return 1;
        }
        if (this.mCircuitList != null) {
            return this.mCircuitList.length;
        }
        return 0;
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int groupPosition) {
        WorkoutCircuit circuit;
        WorkoutExercise[] exerciseList;
        if (this.mIsClassWorkout) {
            return 1;
        }
        if (this.mCircuitList == null || (circuit = this.mCircuitList[groupPosition]) == null || GuidedWorkoutUtils.isRestCircuit(circuit) || (exerciseList = circuit.getExerciseArrayWithoutRests()) == null) {
            return 0;
        }
        return exerciseList.length;
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int groupPosition) {
        return this.mCircuitList[groupPosition];
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int groupPosition, int childPosition) {
        WorkoutExercise[] exerciseList = this.mCircuitList[groupPosition].getExerciseArrayWithoutRests();
        if (exerciseList == null || exerciseList.length < childPosition) {
            return null;
        }
        return exerciseList[childPosition];
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        return false;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder;
        String circuitName;
        String description;
        View view = convertView;
        ExpandableListView expandableListView = (ExpandableListView) parent;
        if (this.mIsClassWorkout) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            View view2 = inflater.inflate(R.layout.adapter_workout_step_header_attend_class, parent, false);
            expandableListView.expandGroup(groupPosition);
            return view2;
        }
        if (view == null) {
            LayoutInflater inflater2 = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            view = inflater2.inflate(R.layout.adapter_workout_step_header_v1, parent, false);
            viewHolder = new GroupViewHolder();
            viewHolder.circuitHeaderText = (CustomFontTextView) ViewUtils.getValidView(view, R.id.circuit_header, CustomFontTextView.class);
            viewHolder.circuitDescription = (CustomFontTextView) ViewUtils.getValidView(view, R.id.circuit_description, CustomFontTextView.class);
            view.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) view.getTag();
        }
        WorkoutCircuit circuit = this.mCircuitList[groupPosition];
        if (circuit != null) {
            if (GuidedWorkoutUtils.isRestCircuit(circuit)) {
                int restTime = circuit.getExerciseList()[0].getCompletionValue();
                circuitName = this.mContext.getString(R.string.workout_type_rest);
                description = this.mContext.getString(R.string.workout_circuit_description_rest_circuit, Formatter.formatDurationSecondsToHrMin(restTime, HOUR_STRING, HOURS_STRING, MINUTE_STRING, MINUTES_STRING, SECOND_STRING, SECONDS_STRING, 60));
            } else {
                circuitName = circuit.getName();
                if (TextUtils.isEmpty(circuitName)) {
                    circuitName = this.mContext.getString(R.string.workout_type_circuit);
                }
                description = constructDescriptionString(circuit);
            }
            viewHolder.circuitHeaderText.setText(circuitName.toUpperCase());
            if (description != null) {
                viewHolder.circuitDescription.setVisibility(0);
                viewHolder.circuitDescription.setText(description);
            } else {
                viewHolder.circuitDescription.setVisibility(8);
            }
            expandableListView.expandGroup(groupPosition);
        }
        return view;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            view = inflater.inflate(R.layout.adapter_workout_step_list_v1, (ViewGroup) null);
            viewHolder = new ChildViewHolder();
            viewHolder.videoButton = (ImageView) ViewUtils.getValidView(view, R.id.workout_video_button, ImageView.class);
            viewHolder.videoPlaceHolder = (FrameLayout) ViewUtils.getValidView(view, R.id.video_thumbnail_borders, FrameLayout.class);
            viewHolder.repsHeaderText = (CustomFontTextView) ViewUtils.getValidView(view, R.id.workout_step_name, CustomFontTextView.class);
            viewHolder.repsCountText = (CustomFontTextView) ViewUtils.getValidView(view, R.id.workout_step_reps, CustomFontTextView.class);
            viewHolder.videoButtonPlay = (TextView) ViewUtils.getValidView(view, R.id.workout_video_button_play, TextView.class);
            viewHolder.videoButtonPlayBackground = (TextView) ViewUtils.getValidView(view, R.id.workout_video_button_play_white_background, TextView.class);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) view.getTag();
            viewHolder.videoPlaceHolder.setVisibility(0);
            viewHolder.videoButtonPlay.setVisibility(0);
            viewHolder.videoButtonPlayBackground.setVisibility(0);
            viewHolder.videoPlaceHolder.setOnClickListener(null);
        }
        WorkoutExercise exercise = this.mCircuitList[groupPosition].getExerciseArrayWithoutRests()[childPosition];
        if (exercise == null) {
            viewHolder.repsHeaderText.setText("");
            viewHolder.repsCountText.setText("");
            viewHolder.videoPlaceHolder.setVisibility(8);
        } else {
            String name = exercise.getName();
            viewHolder.repsHeaderText.setText(name);
            viewHolder.repsCountText.setText(GuidedWorkoutUtils.getRepsDescriptionFromExercise(parent.getContext(), exercise, null));
            String videoThumbnail = exercise.getThumbnail();
            PicassoWrapper.with(this.mContext).load(BedrockImageServiceUtils.createSizedImageUrl(videoThumbnail, mThumbnailImageWidth, mThumbnailImageHight)).fit().centerInside().placeholder(R.drawable.workout_thumbnail).into(viewHolder.videoButton);
            String videoId = exercise.getVideoId();
            if (videoId != null) {
                viewHolder.videoPlaceHolder.setOnClickListener(new AnonymousClass1(videoId, exercise));
            } else {
                viewHolder.videoButtonPlay.setVisibility(8);
                viewHolder.videoButtonPlayBackground.setVisibility(8);
            }
        }
        return view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.microsoft.kapp.adapters.WorkoutStepListAdapterV1$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AtomicBoolean loading = new AtomicBoolean(false);
        String path;
        final /* synthetic */ WorkoutExercise val$exercise;
        final /* synthetic */ String val$videoId;

        AnonymousClass1(String str, WorkoutExercise workoutExercise) {
            this.val$videoId = str;
            this.val$exercise = workoutExercise;
            this.path = String.format(Constants.MSN_VIDEO_URL_PATTERN, this.val$videoId);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (!CommonUtils.isNetworkAvailable(WorkoutStepListAdapterV1.this.mContext)) {
                DialogManagerImpl.getDialogManager(WorkoutStepListAdapterV1.this.mContext).showNetworkErrorDialog(WorkoutStepListAdapterV1.this.mContext);
            } else if (this.loading.compareAndSet(false, true)) {
                WindowManager wm = (WindowManager) WorkoutStepListAdapterV1.this.mContext.getSystemService("window");
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                GuidedWorkoutUtils.playVideo(this.path, width, height, WorkoutStepListAdapterV1.this.mContext, new Callback<Void>() { // from class: com.microsoft.kapp.adapters.WorkoutStepListAdapterV1.1.1
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
                telemetryProperties.put(TelemetryConstants.Events.GuidedWorkoutWatchVideo.Dimensions.VIDEO_ID, this.val$videoId);
                Telemetry.logEvent(TelemetryConstants.Events.GuidedWorkoutWatchVideo.EVENT_NAME, telemetryProperties, null);
            }
        }
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }

    private String constructDescriptionString(WorkoutCircuit circuit) {
        if (circuit == null) {
            KLog.e(TAG, "WorkoutCircuit cannot be null!");
            return "";
        }
        return String.format("%s %s", getSetsDescriptionFromCircuit(circuit), GuidedWorkoutUtils.getRestDescriptionFromCircuit(this.mContext, circuit));
    }

    private String getSetsDescriptionFromCircuit(WorkoutCircuit circuit) {
        if (circuit == null) {
            KLog.w(TAG, "Circuit should not be null!");
            return "";
        }
        WorkoutExercise[] exercisesList = circuit.getExerciseList();
        if (exercisesList == null) {
            KLog.w(TAG, "WorkoutExercise list should not be null!");
            return "";
        }
        CompletionType circuitCompletionType = circuit.getCompletionType();
        int circuitCompletionValue = circuit.getCompletionValue();
        if (circuitCompletionType == CompletionType.REPETITIONS && circuitCompletionValue > 1) {
            return LockedStringUtils.unitReps(circuitCompletionValue, this.mContext.getResources());
        }
        if (circuitCompletionType == CompletionType.SECONDS) {
            return this.mContext.getString(R.string.workout_circuit_description_time_120s, Formatter.formatDurationSecondsToHrMin(circuitCompletionValue, HOUR_STRING, HOURS_STRING, MINUTE_STRING, MINUTES_STRING, SECOND_STRING, SECONDS_STRING, Constants.GUIDED_WORKOUT_SECONDS_120S_THRESHOLD));
        }
        int maxSets = -1;
        int minSets = Integer.MAX_VALUE;
        for (WorkoutExercise exercise : exercisesList) {
            if (exercise == null) {
                KLog.w(TAG, "exercise should not be null!");
            } else {
                String exerciseSets = exercise.getSets();
                if (exerciseSets == null) {
                    KLog.w(TAG, "Exercise Set should not be null!");
                } else if (exerciseSets.contains("*")) {
                    return this.mContext.getString(R.string.workout_circuit_description_as_many_as_possible);
                } else {
                    int numberSets = GuidedWorkoutUtils.getMaxIntegerfromString(exerciseSets);
                    if (numberSets > maxSets) {
                        maxSets = numberSets;
                    }
                    if (numberSets < minSets) {
                        minSets = numberSets;
                    }
                }
            }
        }
        if (circuit.getExerciseTraversalType() == ExerciseTraversalType.SetOrder) {
            return minSets == maxSets ? this.mContext.getString(R.string.workout_circuit_description_set_order, StringUtils.unitCycles(minSets, this.mContext.getResources())) : this.mContext.getString(R.string.workout_circuit_description_set_order_range, Integer.valueOf(minSets), Integer.valueOf(maxSets));
        } else if (maxSets == 1) {
            return this.mContext.getString(R.string.workout_circuit_description_single_round);
        } else {
            if (maxSets > 1) {
                return this.mContext.getString(R.string.workout_circuit_description_plural_rounds, Integer.valueOf(maxSets));
            }
            KLog.w(TAG, "Error getting the description of the Circuit!");
            return "";
        }
    }

    private boolean isClassWorkout(WorkoutCircuit[] circuitList) {
        if (circuitList == null) {
            return false;
        }
        boolean isEmpty = true;
        for (WorkoutCircuit circuit : circuitList) {
            if (circuit != null) {
                Iterator i$ = circuit.getExerciseListWithoutRests().iterator();
                while (i$.hasNext()) {
                    WorkoutExercise exercise = i$.next();
                    if (exercise != null) {
                        isEmpty = false;
                        if (!Constants.CLASS_EXERCISE_ID.equalsIgnoreCase(exercise.getId())) {
                            return false;
                        }
                    }
                }
                continue;
            }
        }
        return !isEmpty;
    }
}
