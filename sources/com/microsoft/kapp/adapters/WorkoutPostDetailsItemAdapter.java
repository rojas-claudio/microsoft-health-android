package com.microsoft.kapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.TimeSpan;
import com.microsoft.kapp.services.healthandfitness.models.CircuitType;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.krestsdk.models.CompletionType;
import com.microsoft.krestsdk.models.ExerciseTraversalType;
import com.microsoft.krestsdk.models.WorkoutExerciseSequence;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
/* loaded from: classes.dex */
public class WorkoutPostDetailsItemAdapter extends BaseExpandableListAdapter {
    private static String HOUR_STRING_HR;
    private static String MINUTE_STRING_MIN;
    private static String SECOND_STRING_SEC;
    private static final String TAG = WorkoutPostDetailsItemAdapter.class.getSimpleName();
    private Context mContext;
    private final List<Cycle> mCyclesList = new ArrayList();
    private boolean mIsSingleCycle;
    private boolean mIsSmartCountingWorkout;

    /* loaded from: classes.dex */
    private static class CycleHeaderBannerViewHolder {
        private TextView groupIcon;
        public TextView name;

        private CycleHeaderBannerViewHolder() {
        }
    }

    public WorkoutPostDetailsItemAdapter(Context context, List<WorkoutExerciseSequence> exerciseList, boolean isSmartCounting) {
        this.mContext = context;
        this.mIsSmartCountingWorkout = isSmartCounting;
        SECOND_STRING_SEC = getString(R.string.secs_singular);
        MINUTE_STRING_MIN = getString(R.string.mins_singular);
        HOUR_STRING_HR = getString(R.string.hrs_singular);
        restructureData(this.mCyclesList, exerciseList);
        this.mIsSingleCycle = this.mCyclesList.size() == 1;
    }

    private void restructureData(List<Cycle> cyclesList, List<WorkoutExerciseSequence> exerciseList) {
        if (cyclesList == null) {
            KLog.e(TAG, "Cycles list cannot be null in post guidedWorkout!");
        } else if (exerciseList == null) {
            KLog.e(TAG, "WorkoutExerciseSequence list cannot be null in post guidedWorkout!");
        } else {
            for (WorkoutExerciseSequence exercise : exerciseList) {
                if (exercise.getExerciseOrdinal() != 0) {
                    int cycleIndex = exercise.getCycleOrdinal() - 1;
                    if (cyclesList.size() <= cycleIndex) {
                        for (int i = cyclesList.size(); i <= cycleIndex; i++) {
                            Cycle cycle = new Cycle(String.format(getString(R.string.guided_workout_details_cycle_name), Integer.valueOf(i + 1)));
                            cyclesList.add(cycle);
                        }
                    }
                    if (cycleIndex >= 0) {
                        Cycle cycle2 = cyclesList.get(cycleIndex);
                        cycle2.addExercise(exercise);
                    } else {
                        KLog.d(TAG, "Error: Cycle index started at 0!! Cycle index should be 1-based and start a 1!");
                    }
                }
            }
        }
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        return this.mCyclesList.size();
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int groupPosition) {
        List<Circuit> circuits;
        Cycle cycle = this.mCyclesList.get(groupPosition);
        if (cycle == null || (circuits = cycle.getCircuitList()) == null) {
            return 0;
        }
        return circuits.size();
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int groupPosition) {
        return this.mCyclesList.get(groupPosition);
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int groupPosition, int childPosition) {
        List<Circuit> circuits;
        Cycle cycle = this.mCyclesList.get(groupPosition);
        if (cycle == null || (circuits = cycle.getCircuitList()) == null) {
            return null;
        }
        return circuits.get(childPosition);
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int groupPosition, int childPosition) {
        return 0L;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        return true;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        CycleHeaderBannerViewHolder holder;
        if (this.mIsSingleCycle) {
            return new View(this.mContext);
        }
        if (convertView == null) {
            holder = new CycleHeaderBannerViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            convertView = inflater.inflate(R.layout.adapter_post_workout_details_list_item_cycle_header, parent, false);
            holder.name = (TextView) ViewUtils.getValidView(convertView, R.id.cycle_name, TextView.class);
            holder.groupIcon = (TextView) ViewUtils.getValidView(convertView, R.id.group_icon, TextView.class);
            convertView.setTag(holder);
        } else {
            holder = (CycleHeaderBannerViewHolder) convertView.getTag();
        }
        Cycle cycle = this.mCyclesList.get(groupPosition);
        holder.name.setText(cycle.getName().toUpperCase(Locale.getDefault()));
        if (isExpanded) {
            holder.groupIcon.setText(R.string.glyph_triangle_down);
        } else {
            holder.groupIcon.setText(R.string.glyph_triangle_right);
        }
        return convertView;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        List<Circuit> circuits;
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        View view = null;
        Cycle cycle = this.mCyclesList.get(groupPosition);
        if (cycle != null && (circuits = cycle.getCircuitList()) != null) {
            Circuit circuit = circuits.get(childPosition);
            view = getCircuitView(inflater, parent, circuit, true);
        }
        if (view == null) {
            return new View(this.mContext);
        }
        return view;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /* loaded from: classes.dex */
    public class Cycle {
        private List<Circuit> mCircuits;
        private String mName;

        public Cycle(String name) {
            this.mName = name;
        }

        public String getName() {
            return this.mName;
        }

        public List<Circuit> getCircuitList() {
            return this.mCircuits == null ? new ArrayList() : this.mCircuits;
        }

        public void addExercise(WorkoutExerciseSequence exercise) {
            String circuitName;
            if (exercise != null) {
                if (this.mCircuits == null) {
                    this.mCircuits = new ArrayList();
                }
                int circuitIndex = exercise.getCircuitOrdinal() - 1;
                if (this.mCircuits.size() <= circuitIndex) {
                    for (int i = this.mCircuits.size(); i <= circuitIndex; i++) {
                        if (exercise.getCircuitType() == CircuitType.CoolDown) {
                            circuitName = WorkoutPostDetailsItemAdapter.this.getString(R.string.guided_workout_details_cooldown_header_title);
                        } else {
                            circuitName = exercise.getCircuitType() == CircuitType.WarmUp ? WorkoutPostDetailsItemAdapter.this.getString(R.string.guided_workout_details_warmup_header_title) : WorkoutPostDetailsItemAdapter.this.getString(R.string.guided_workout_details_circuit_name);
                        }
                        this.mCircuits.add(new Circuit(circuitName));
                    }
                }
                if (circuitIndex < 0) {
                    KLog.e(WorkoutPostDetailsItemAdapter.TAG, "Error: Circuit index started at 0!! Circuit index should be 1-based and start a 1!");
                    return;
                }
                Circuit circuit = this.mCircuits.get(circuitIndex);
                circuit.addExercise(exercise);
                circuit.accumulateCalories(exercise.getCaloriesBurned());
                circuit.accumulateDuration(exercise.getDuration());
                circuit.setCircuitType(exercise.getCircuitType());
                circuit.setExerciseTraversalType(exercise.getExerciseTraversalType());
            }
        }
    }

    /* loaded from: classes.dex */
    public class Circuit {
        private int mCalories;
        private CircuitType mCircuitType;
        private String mDescription;
        private int mDuration;
        private ExerciseTraversalType mExerciseTraversalType;
        private String mName;
        private List<Round> mRounds;
        private Set<String> mNoRestExerciseIdsSet = new HashSet();
        private boolean mIsRestCircuit = true;
        private boolean mIsExpanded = true;

        public Circuit(String name) {
            this.mName = name;
        }

        public String getName() {
            return this.mName;
        }

        public String getDescription() {
            return this.mDescription;
        }

        public void setDescription(String description) {
            this.mDescription = description;
        }

        public int getDuration() {
            return this.mDuration;
        }

        public void accumulateDuration(int duration) {
            this.mDuration += duration;
        }

        public int getCalories() {
            return this.mCalories;
        }

        public void accumulateCalories(int calories) {
            this.mCalories += calories;
        }

        public List<Round> getRoundsList() {
            return this.mRounds;
        }

        public void setRoundsList(List<Round> roundsList) {
            this.mRounds = roundsList;
        }

        public ExerciseTraversalType getExerciseTraversalType() {
            return this.mExerciseTraversalType;
        }

        public void setExerciseTraversalType(ExerciseTraversalType exerciseTraversalType) {
            this.mExerciseTraversalType = exerciseTraversalType;
        }

        public CircuitType getCircuitType() {
            return this.mCircuitType;
        }

        public void setCircuitType(CircuitType circuitType) {
            this.mCircuitType = circuitType;
        }

        public boolean getIsRestCircuit() {
            return this.mIsRestCircuit;
        }

        public void setIsRestCircuit(boolean isRestCircuit) {
            this.mIsRestCircuit = isRestCircuit;
        }

        public void addExercise(WorkoutExerciseSequence exercise) {
            if (exercise != null) {
                if (this.mIsRestCircuit && !exercise.getIsRest() && !Constants.REST_EXERCISE_ID.equalsIgnoreCase(exercise.getExerciseStringId())) {
                    this.mIsRestCircuit = false;
                }
                if (this.mRounds == null) {
                    this.mRounds = new ArrayList();
                }
                int roundIndex = exercise.getRoundOrdinal() - 1;
                if (this.mRounds.size() <= roundIndex) {
                    for (int i = this.mRounds.size(); i <= roundIndex; i++) {
                        String roundName = String.format(WorkoutPostDetailsItemAdapter.this.getString(R.string.guided_workout_details_round_name), Integer.valueOf(i + 1));
                        this.mRounds.add(new Round(roundName));
                    }
                }
                if (roundIndex < 0) {
                    KLog.e(WorkoutPostDetailsItemAdapter.TAG, "Error: Round index started at 0!! Round index should be 1-based and start a 1!");
                    return;
                }
                Round round = this.mRounds.get(roundIndex);
                round.addExercise(exercise);
                round.accumulateCalories(exercise.getCaloriesBurned());
                round.accumulateDuration(exercise.getDuration());
                if (roundIndex == 0) {
                    String exerciseName = exercise.getName();
                    String completionValue = exercise.getCompletionValue();
                    if (exerciseName != null && !this.mNoRestExerciseIdsSet.contains(exerciseName) && !exercise.getIsRest() && !Constants.REST_EXERCISE_ID.equalsIgnoreCase(exercise.getExerciseStringId())) {
                        this.mNoRestExerciseIdsSet.add(exerciseName);
                        if (completionValue != null) {
                            CompletionType completionType = exercise.getCompletionType();
                            if (completionType == CompletionType.SECONDS) {
                                try {
                                    int completionValueInt = Integer.parseInt(completionValue);
                                    String formattedTime = WorkoutPostDetailsItemAdapter.this.formatCompletionValueTime(completionValueInt);
                                    this.mDescription = !TextUtils.isEmpty(this.mDescription) ? WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_append_description, this.mDescription, exerciseName, formattedTime) : WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_description, exerciseName, formattedTime);
                                    return;
                                } catch (NumberFormatException e) {
                                    KLog.e(WorkoutPostDetailsItemAdapter.TAG, "completionValue should be a number if the completionType is seconds!");
                                    this.mDescription = formatDescription(exerciseName, completionValue, completionType);
                                    return;
                                }
                            }
                            this.mDescription = formatDescription(exerciseName, completionValue, completionType);
                            return;
                        }
                        this.mDescription = exerciseName;
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean getIsExpanded() {
            return this.mIsExpanded;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setIsExpanded(boolean isExpanded) {
            this.mIsExpanded = isExpanded;
        }

        private String formatDescription(String exerciseName, String completionValue, CompletionType completionType) {
            if (completionType == null) {
                return "";
            }
            switch (completionType) {
                case SECONDS:
                    return !TextUtils.isEmpty(this.mDescription) ? WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_append_description_seconds, this.mDescription, exerciseName, completionValue) : WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_description_seconds, exerciseName, completionValue);
                case METERS:
                    return !TextUtils.isEmpty(this.mDescription) ? WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_append_description_meters, this.mDescription, exerciseName, completionValue) : WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_description_meters, exerciseName, completionValue);
                case REPETITIONS:
                    return !TextUtils.isEmpty(this.mDescription) ? WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_append_description_reps, this.mDescription, exerciseName, completionValue) : WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_description_reps, exerciseName, completionValue);
                case JOULES:
                    return !TextUtils.isEmpty(this.mDescription) ? WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_append_description_joules, this.mDescription, exerciseName, completionValue) : WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_description_joules, exerciseName, completionValue);
                case CALORIES:
                    return !TextUtils.isEmpty(this.mDescription) ? WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_append_description_calories, this.mDescription, exerciseName, completionValue) : WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_description_calories, exerciseName, completionValue);
                case HEART_RATE:
                    return !TextUtils.isEmpty(this.mDescription) ? WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_append_description_HR, this.mDescription, exerciseName, completionValue) : WorkoutPostDetailsItemAdapter.this.mContext.getString(R.string.guided_workout_details_circuit_description_HR, exerciseName, completionValue);
                default:
                    return "";
            }
        }
    }

    /* loaded from: classes.dex */
    public class Round {
        private int mCalories;
        private int mDuration;
        private List<Exercise> mExercisesList;
        private boolean mHasSingleExercise = true;
        private Long mLastExerciseId;
        private String mLastExerciseName;
        private String mName;

        public Round(String name) {
            this.mName = name;
        }

        public String getName() {
            return this.mName;
        }

        public void addExercise(WorkoutExerciseSequence workoutExerciseSequence) {
            boolean z = true;
            if (workoutExerciseSequence != null) {
                if (this.mExercisesList == null) {
                    this.mExercisesList = new ArrayList();
                }
                int exerciseIndex = workoutExerciseSequence.getExerciseOrdinal() - 1;
                if (this.mExercisesList.size() <= exerciseIndex) {
                    for (int i = this.mExercisesList.size(); i <= exerciseIndex; i++) {
                        this.mExercisesList.add(new Exercise());
                    }
                }
                if (exerciseIndex < 0) {
                    KLog.e(WorkoutPostDetailsItemAdapter.TAG, "Error: Exercise index started at 0!! Valid Exercise index should start at 1!");
                    return;
                }
                Exercise exercise = this.mExercisesList.get(exerciseIndex);
                String exerciseName = workoutExerciseSequence.getName();
                if (exerciseName == null) {
                    exerciseName = String.format(WorkoutPostDetailsItemAdapter.this.getString(R.string.guided_workout_details_exercise_name), Integer.valueOf(exerciseIndex + 1));
                }
                exercise.setName(exerciseName);
                exercise.setDuration(workoutExerciseSequence.getDuration());
                exercise.setRepetitions(workoutExerciseSequence.getComputedCompletionValue());
                exercise.setCalories(workoutExerciseSequence.getCaloriesBurned());
                exercise.setExerciseOrdinal(workoutExerciseSequence.getExerciseOrdinal());
                exercise.setDoNotCount(workoutExerciseSequence.getDoNotCount());
                exercise.setIsRest(workoutExerciseSequence.getIsRest() || Constants.REST_EXERCISE_ID.equalsIgnoreCase(workoutExerciseSequence.getExerciseStringId()));
                if (this.mHasSingleExercise && !exercise.getIsRest()) {
                    if (this.mLastExerciseId != null && this.mLastExerciseName != null) {
                        this.mHasSingleExercise = (this.mLastExerciseId.longValue() == workoutExerciseSequence.getExerciseId() && this.mLastExerciseName.equals(workoutExerciseSequence.getName())) ? false : false;
                        return;
                    }
                    this.mLastExerciseId = Long.valueOf(workoutExerciseSequence.getExerciseId());
                    this.mLastExerciseName = workoutExerciseSequence.getName();
                }
            }
        }

        public int getDuration() {
            return this.mDuration;
        }

        public void accumulateDuration(int duration) {
            this.mDuration += duration;
        }

        public int getCalories() {
            return this.mCalories;
        }

        public void accumulateCalories(int calories) {
            this.mCalories += calories;
        }

        public List<Exercise> getExerciseList() {
            return this.mExercisesList == null ? new ArrayList() : this.mExercisesList;
        }

        public boolean getHasUniqueExercise() {
            return this.mHasSingleExercise;
        }
    }

    /* loaded from: classes.dex */
    public class Exercise {
        private int mCalories;
        private int mCompetionValue;
        private CompletionType mCompletionType;
        private boolean mDoNotCount;
        private int mDuration;
        private int mExerciseOrdinal;
        private boolean mIsRest;
        private String mName;
        private int mRepetitions;

        public Exercise() {
        }

        public boolean getIsRest() {
            return this.mIsRest;
        }

        public void setIsRest(boolean isRest) {
            this.mIsRest = isRest;
        }

        public int getDuration() {
            return this.mDuration;
        }

        public String getName() {
            return this.mName;
        }

        public void setName(String name) {
            this.mName = name;
        }

        public void setDuration(int duration) {
            this.mDuration = duration;
        }

        public int getCalories() {
            return this.mCalories;
        }

        public void setCalories(int calories) {
            this.mCalories = calories;
        }

        public int getRepetitions() {
            return this.mRepetitions;
        }

        public void setRepetitions(int repetitions) {
            this.mRepetitions = repetitions;
        }

        public CompletionType getCompletionType() {
            return this.mCompletionType;
        }

        public void setCompletionType(CompletionType completionType) {
            this.mCompletionType = completionType;
        }

        public int getCompletionValue() {
            return this.mCompetionValue;
        }

        public void setCompletionValue(int completionValue) {
            this.mCompetionValue = completionValue;
        }

        public int getExerciseOrdinal() {
            return this.mExerciseOrdinal;
        }

        public void setExerciseOrdinal(int ordinal) {
            this.mExerciseOrdinal = ordinal;
        }

        public boolean getDoNotCount() {
            return this.mDoNotCount;
        }

        public void setDoNotCount(boolean doNotCount) {
            this.mDoNotCount = doNotCount;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String formatCompletionValueTime(int completionValueInt) {
        return Formatter.formatDurationSecondsToHrMin(completionValueInt, HOUR_STRING_HR, HOUR_STRING_HR, MINUTE_STRING_MIN, MINUTE_STRING_MIN, SECOND_STRING_SEC, SECOND_STRING_SEC, Constants.GUIDED_WORKOUT_SECONDS_120S_THRESHOLD, false, MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getString(int resId) {
        return this.mContext.getString(resId);
    }

    private View getCircuitView(LayoutInflater inflater, ViewGroup parent, Circuit circuit, boolean showCaloriesAndTimeLabels) {
        if (circuit == null || circuit.getIsRestCircuit()) {
            return null;
        }
        if (circuit.getCircuitType() == CircuitType.CoolDown || circuit.getCircuitType() == CircuitType.WarmUp) {
            return getWarmUpCoolDownStyleCircuitView(inflater, parent, circuit, showCaloriesAndTimeLabels);
        }
        if (isTabataRoundStyle(circuit)) {
            return getTabataRoundStyleCircuitView(inflater, parent, circuit, showCaloriesAndTimeLabels);
        }
        if (isTabataSetStyle(circuit)) {
            return getTabataSetStyleCircuitView(inflater, parent, circuit, showCaloriesAndTimeLabels);
        }
        if (isTaskTimeStyle(circuit) && !this.mIsSmartCountingWorkout) {
            return getTaskTimeStyleCircuitView(inflater, parent, circuit, showCaloriesAndTimeLabels);
        }
        return getGenericStyleCircuitView(inflater, parent, circuit, showCaloriesAndTimeLabels);
    }

    private View getGenericStyleCircuitView(LayoutInflater inflater, ViewGroup parent, Circuit circuit, boolean showCaloriesAndTimeLabels) {
        List<Round> roundsList;
        View exerciseView;
        if (circuit == null || (roundsList = circuit.getRoundsList()) == null) {
            return null;
        }
        if (roundsList.size() == 1) {
            Round singleRound = roundsList.get(0);
            if (singleRound != null) {
                View view = inflater.inflate(R.layout.adapter_post_workout_details_list_item_circuit_header, parent, false);
                ViewGroup listView = (ViewGroup) ViewUtils.getValidView(view, R.id.list_view, ViewGroup.class);
                ViewGroup headerBanner = (ViewGroup) ViewUtils.getValidView(view, R.id.header_banner, ViewGroup.class);
                TextView circuitName = (TextView) ViewUtils.getValidView(view, R.id.header_name, CustomFontTextView.class);
                if (!showCaloriesAndTimeLabels) {
                    TextView circuitTime = (TextView) ViewUtils.getValidView(view, R.id.time, CustomFontTextView.class);
                    TextView circuitCalories = (TextView) ViewUtils.getValidView(view, R.id.calories, TextView.class);
                    circuitTime.setVisibility(4);
                    circuitCalories.setVisibility(4);
                }
                circuitName.setText(circuit.getName().toUpperCase(Locale.getDefault()));
                List<Exercise> exercisesList = singleRound.getExerciseList();
                for (Exercise exercise : exercisesList) {
                    if (exercise != null && !exercise.getIsRest() && (exerciseView = getExerciseView(inflater, listView, exercise)) != null) {
                        listView.addView(exerciseView);
                    }
                }
                setCircuitOnClickLogic(headerBanner, listView, circuit);
                return view;
            }
            return null;
        }
        return getTaskTimeStyleCircuitView(inflater, parent, circuit, showCaloriesAndTimeLabels);
    }

    private View getTabataRoundStyleCircuitView(LayoutInflater inflater, ViewGroup parent, Circuit circuit, boolean showCaloriesAndTimeLabels) {
        if (circuit == null) {
            return null;
        }
        List<Round> roundsList = circuit.getRoundsList();
        View view = inflater.inflate(R.layout.adapter_post_workout_details_list_item_circuit_header, parent, false);
        ViewGroup listView = (ViewGroup) ViewUtils.getValidView(view, R.id.list_view, ViewGroup.class);
        ViewGroup headerBanner = (ViewGroup) ViewUtils.getValidView(view, R.id.header_banner, ViewGroup.class);
        TextView circuitName = (TextView) ViewUtils.getValidView(headerBanner, R.id.header_name, CustomFontTextView.class);
        if (!showCaloriesAndTimeLabels) {
            TextView circuitTime = (TextView) ViewUtils.getValidView(view, R.id.time, CustomFontTextView.class);
            TextView circuitCalories = (TextView) ViewUtils.getValidView(view, R.id.calories, TextView.class);
            circuitTime.setVisibility(4);
            circuitCalories.setVisibility(4);
        }
        View descriptionView = inflater.inflate(R.layout.adapter_post_workout_details_list_item_body_timetask_style, listView, false);
        TextView description = (TextView) ViewUtils.getValidView(descriptionView, R.id.description, TextView.class);
        circuitName.setText(circuit.getName().toUpperCase(Locale.getDefault()));
        String circuitDescription = circuit.getDescription();
        if (circuitDescription != null) {
            description.setText(circuitDescription);
        } else {
            description.setVisibility(8);
        }
        listView.addView(descriptionView);
        for (Round round : roundsList) {
            if (round != null) {
                View roundView = inflater.inflate(R.layout.post_workout_details_list_round_exercise_item, listView, false);
                TextView header = (TextView) ViewUtils.getValidView(roundView, R.id.header, TextView.class);
                TextView time = (TextView) ViewUtils.getValidView(roundView, R.id.time, TextView.class);
                TextView calories = (TextView) ViewUtils.getValidView(roundView, R.id.calories, TextView.class);
                TextView reps = (TextView) ViewUtils.getValidView(roundView, R.id.reps, TextView.class);
                try {
                    header.setText(round.getName());
                    time.setText(getFormattedTime(round.getDuration()));
                    calories.setText(Integer.toString(round.getCalories()));
                    if (this.mIsSmartCountingWorkout) {
                        Exercise round_exercise = round.getExerciseList().get(0);
                        reps.setText(String.format(getString(R.string.guided_workout_details_estimated_reps), Integer.toString(round_exercise.getRepetitions())));
                    } else {
                        reps.setVisibility(8);
                    }
                    listView.addView(roundView);
                } catch (Exception ex) {
                    KLog.e(TAG, "Error: %s", ex);
                }
            }
        }
        setCircuitOnClickLogic(headerBanner, listView, circuit);
        return view;
    }

    private void setCircuitOnClickLogic(ViewGroup circuitHeaderBannerView, final ViewGroup listView, final Circuit circuit) {
        final TextView groupIcon = (TextView) ViewUtils.getValidView(circuitHeaderBannerView, R.id.group_icon, TextView.class);
        listView.setVisibility(circuit.getIsExpanded() ? 0 : 8);
        groupIcon.setText(circuit.getIsExpanded() ? R.string.glyph_triangle_down : R.string.glyph_triangle_right);
        circuitHeaderBannerView.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.adapters.WorkoutPostDetailsItemAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (circuit.getIsExpanded()) {
                    listView.setVisibility(8);
                    circuit.setIsExpanded(false);
                    groupIcon.setText(R.string.glyph_triangle_right);
                    return;
                }
                listView.setVisibility(0);
                circuit.setIsExpanded(true);
                groupIcon.setText(R.string.glyph_triangle_down);
            }
        });
    }

    private View getTabataSetStyleCircuitView(LayoutInflater inflater, ViewGroup parent, Circuit circuit, boolean showCaloriesAndTimeLabels) {
        String string;
        Object[] objArr;
        int setOrdinal;
        if (circuit == null) {
            return null;
        }
        List<Round> roundsList = circuit.getRoundsList();
        if (roundsList != null && roundsList.get(0) != null) {
            List<Exercise> exercisesList = roundsList.get(0).getExerciseList();
            if (exercisesList == null) {
                KLog.e(TAG, "exercisesList cannot be null in TabataSet style circuit!");
                return null;
            }
            View view = inflater.inflate(R.layout.adapter_post_workout_details_list_item_circuit_header, parent, false);
            ViewGroup listView = (ViewGroup) ViewUtils.getValidView(view, R.id.list_view, ViewGroup.class);
            ViewGroup headerBanner = (ViewGroup) ViewUtils.getValidView(view, R.id.header_banner, ViewGroup.class);
            TextView circuitName = (TextView) ViewUtils.getValidView(headerBanner, R.id.header_name, CustomFontTextView.class);
            if (!showCaloriesAndTimeLabels) {
                TextView circuitTime = (TextView) ViewUtils.getValidView(view, R.id.time, CustomFontTextView.class);
                TextView circuitCalories = (TextView) ViewUtils.getValidView(view, R.id.calories, TextView.class);
                circuitTime.setVisibility(4);
                circuitCalories.setVisibility(4);
            }
            View descriptionView = inflater.inflate(R.layout.adapter_post_workout_details_list_item_body_timetask_style, listView, false);
            TextView description = (TextView) ViewUtils.getValidView(descriptionView, R.id.description, TextView.class);
            circuitName.setText(circuit.getName().toUpperCase(Locale.getDefault()));
            String circuitDescription = circuit.getDescription();
            if (circuitDescription != null) {
                description.setText(circuitDescription);
            } else {
                description.setVisibility(8);
            }
            listView.addView(descriptionView);
            int setOrdinal2 = 1;
            for (Exercise currentExercise : exercisesList) {
                if (currentExercise != null && !currentExercise.getIsRest()) {
                    View setView = inflater.inflate(R.layout.post_workout_details_list_round_exercise_item, listView, false);
                    TextView header = (TextView) ViewUtils.getValidView(setView, R.id.header, TextView.class);
                    TextView time = (TextView) ViewUtils.getValidView(setView, R.id.time, TextView.class);
                    TextView calories = (TextView) ViewUtils.getValidView(setView, R.id.calories, TextView.class);
                    TextView reps = (TextView) ViewUtils.getValidView(setView, R.id.reps, TextView.class);
                    try {
                        string = getString(R.string.guided_workout_details_set_name);
                        objArr = new Object[1];
                        setOrdinal = setOrdinal2 + 1;
                    } catch (Exception e) {
                        ex = e;
                    }
                    try {
                        objArr[0] = Integer.valueOf(setOrdinal2);
                        header.setText(String.format(string, objArr));
                        time.setText(getFormattedTime(currentExercise.getDuration()));
                        calories.setText(Integer.toString(currentExercise.getCalories()));
                        if (this.mIsSmartCountingWorkout) {
                            reps.setText(String.format(getString(R.string.guided_workout_details_estimated_reps), Integer.toString(currentExercise.getRepetitions())));
                        } else {
                            reps.setVisibility(8);
                        }
                        listView.addView(setView);
                        setOrdinal2 = setOrdinal;
                    } catch (Exception e2) {
                        ex = e2;
                        setOrdinal2 = setOrdinal;
                        KLog.e(TAG, "Error: %s", ex);
                    }
                }
            }
            setCircuitOnClickLogic(headerBanner, listView, circuit);
            return view;
        }
        KLog.e(TAG, "Unexpected parsing error in TabataSet style circuit!");
        return null;
    }

    private View getTaskTimeStyleCircuitView(LayoutInflater inflater, ViewGroup parent, Circuit circuit, boolean showCaloriesAndTimeLabels) {
        if (circuit == null) {
            return null;
        }
        List<Round> roundsList = circuit.getRoundsList();
        if (roundsList == null) {
            KLog.e(TAG, "Rounds List cannot be null!");
            return null;
        }
        View circuitView = inflater.inflate(R.layout.adapter_post_workout_details_list_item_circuit_header, parent, false);
        ViewGroup listView = (ViewGroup) ViewUtils.getValidView(circuitView, R.id.list_view, ViewGroup.class);
        ViewGroup headerBanner = (ViewGroup) ViewUtils.getValidView(circuitView, R.id.header_banner, ViewGroup.class);
        TextView circuitName = (TextView) ViewUtils.getValidView(headerBanner, R.id.header_name, TextView.class);
        if (!showCaloriesAndTimeLabels) {
            TextView circuitTime = (TextView) ViewUtils.getValidView(circuitView, R.id.time, CustomFontTextView.class);
            TextView circuitCalories = (TextView) ViewUtils.getValidView(circuitView, R.id.calories, TextView.class);
            circuitTime.setVisibility(4);
            circuitCalories.setVisibility(4);
        }
        View descriptionView = inflater.inflate(R.layout.adapter_post_workout_details_list_item_body_timetask_style, (ViewGroup) circuitView, false);
        TextView description = (TextView) ViewUtils.getValidView(descriptionView, R.id.description, TextView.class);
        circuitName.setText(circuit.getName().toUpperCase(Locale.getDefault()));
        String circuitDescription = circuit.getDescription();
        if (circuitDescription != null) {
            description.setText(circuitDescription);
        } else {
            description.setVisibility(8);
        }
        listView.addView(descriptionView);
        for (Round round : roundsList) {
            if (round != null) {
                View roundView = inflater.inflate(R.layout.post_workout_details_list_round_exercise_item, listView, false);
                TextView header = (TextView) ViewUtils.getValidView(roundView, R.id.header, TextView.class);
                TextView time = (TextView) ViewUtils.getValidView(roundView, R.id.time, TextView.class);
                TextView calories = (TextView) ViewUtils.getValidView(roundView, R.id.calories, TextView.class);
                TextView reps = (TextView) ViewUtils.getValidView(roundView, R.id.reps, TextView.class);
                try {
                    header.setText(round.getName());
                    time.setText(getFormattedTime(round.getDuration()));
                    calories.setText(Integer.toString(round.getCalories()));
                    reps.setVisibility(8);
                    listView.addView(roundView);
                } catch (Exception ex) {
                    KLog.e(TAG, "Error: %s", ex);
                }
            }
        }
        setCircuitOnClickLogic(headerBanner, listView, circuit);
        return circuitView;
    }

    private View getWarmUpCoolDownStyleCircuitView(LayoutInflater inflater, ViewGroup parent, Circuit circuit, boolean showCaloriesAndTimeLabels) {
        if (circuit == null) {
            return null;
        }
        View circuitView = inflater.inflate(R.layout.adapter_post_workout_details_list_item_circuit_header, parent, false);
        ViewGroup listView = (ViewGroup) ViewUtils.getValidView(circuitView, R.id.list_view, ViewGroup.class);
        ViewGroup headerBanner = (ViewGroup) ViewUtils.getValidView(circuitView, R.id.header_banner, ViewGroup.class);
        TextView circuitName = (TextView) ViewUtils.getValidView(headerBanner, R.id.header_name, TextView.class);
        circuitName.setText(circuit.getName().toUpperCase(Locale.getDefault()));
        if (!showCaloriesAndTimeLabels) {
            TextView circuitTime = (TextView) ViewUtils.getValidView(circuitView, R.id.time, CustomFontTextView.class);
            TextView circuitCalories = (TextView) ViewUtils.getValidView(circuitView, R.id.calories, TextView.class);
            circuitTime.setVisibility(4);
            circuitCalories.setVisibility(4);
        }
        View view = inflater.inflate(R.layout.adapter_post_workout_details_list_item_body_warmup_cooldown_style, listView, false);
        TextView description = (TextView) ViewUtils.getValidView(view, R.id.description, TextView.class);
        TextView header = (TextView) ViewUtils.getValidView(view, R.id.header, TextView.class);
        TextView time = (TextView) ViewUtils.getValidView(view, R.id.time, TextView.class);
        TextView calories = (TextView) ViewUtils.getValidView(view, R.id.calories, TextView.class);
        try {
            String circuitDescription = circuit.getDescription();
            if (circuitDescription != null) {
                description.setText(circuitDescription);
            } else {
                description.setVisibility(8);
            }
            header.setText(circuit.getName());
            time.setText(getFormattedTime(circuit.getDuration()));
            calories.setText(Integer.toString(circuit.getCalories()));
            listView.addView(view);
            setCircuitOnClickLogic(headerBanner, listView, circuit);
            return circuitView;
        } catch (Exception ex) {
            KLog.e(TAG, "Error: %s", ex);
            return null;
        }
    }

    private boolean isTabataRoundStyle(Circuit circuit) {
        if (circuit == null) {
            return false;
        }
        List<Round> roundsList = circuit.getRoundsList();
        return (roundsList == null || roundsList.size() <= 1 || roundsList.get(0) == null || roundsList.get(0).getExerciseList() == null || roundsList.get(0).getExerciseList().size() != 1) ? false : true;
    }

    private boolean isTabataSetStyle(Circuit circuit) {
        List<Round> roundsList;
        boolean z = true;
        if (circuit == null || (roundsList = circuit.getRoundsList()) == null || roundsList.size() != 1) {
            return false;
        }
        Round round = roundsList.get(0);
        if (round == null || round.getExerciseList() == null || round.getExerciseList().size() <= 1 || !round.getHasUniqueExercise()) {
            z = false;
        }
        return z;
    }

    private boolean isTaskTimeStyle(Circuit circuit) {
        boolean z = true;
        if (circuit == null) {
            return false;
        }
        if (circuit.getExerciseTraversalType() != ExerciseTraversalType.ExerciseOrder || circuit.getRoundsList() == null || circuit.getRoundsList().size() <= 1) {
            z = false;
        }
        return z;
    }

    private View getExerciseView(LayoutInflater inflater, ViewGroup parent, Exercise exercise) {
        if (exercise == null) {
            return null;
        }
        View exercise_item_view = inflater.inflate(R.layout.post_workout_details_list_round_exercise_item, parent, false);
        TextView header = (TextView) ViewUtils.getValidView(exercise_item_view, R.id.header, TextView.class);
        TextView time = (TextView) ViewUtils.getValidView(exercise_item_view, R.id.time, TextView.class);
        TextView calories = (TextView) ViewUtils.getValidView(exercise_item_view, R.id.calories, TextView.class);
        TextView reps = (TextView) ViewUtils.getValidView(exercise_item_view, R.id.reps, TextView.class);
        try {
            header.setText(exercise.getName());
            time.setText(getFormattedTime(exercise.getDuration()));
            calories.setText(Integer.toString(exercise.getCalories()));
            if (!this.mIsSmartCountingWorkout || exercise.getDoNotCount()) {
                reps.setVisibility(8);
            } else {
                reps.setText(String.format(getString(R.string.guided_workout_details_estimated_reps), Integer.toString(exercise.getRepetitions())));
            }
            return exercise_item_view;
        } catch (Exception ex) {
            KLog.e(TAG, "Error: %s", ex);
            return null;
        }
    }

    private String getFormattedTime(long seconds) {
        return TimeSpan.fromSeconds(seconds).formatTime();
    }
}
