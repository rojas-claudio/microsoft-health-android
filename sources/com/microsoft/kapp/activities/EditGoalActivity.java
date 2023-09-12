package com.microsoft.kapp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.goal.GoalProcessor;
import com.microsoft.kapp.models.goal.GoalProcessorManager;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.tasks.GoalUpdateTask;
import com.microsoft.kapp.tasks.GoalsPushDeviceTask;
import com.microsoft.kapp.tasks.OnTaskStateChangedListener;
import com.microsoft.kapp.tasks.PingDeviceTask;
import com.microsoft.kapp.tasks.StateListenerTask;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CircularSeekBar;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.widgets.ConfirmationBar;
import com.microsoft.krestsdk.models.GoalOperationResultDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.services.RestService;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class EditGoalActivity extends BaseFragmentActivity implements CircularSeekBar.OnValueChangeListener, GoalUpdateTask.OnGoalUpdateTaskListener, OnTaskStateChangedListener {
    private final String TAG = getClass().getSimpleName();
    private Map<GoalType, Long> mAllGoalsValue;
    private CircularSeekBar mCircularSeekBar;
    private ConfirmationBar mConfirmationBar;
    private String mCurrentGoalId;
    private String mCurrentGoalName;
    private int mCurrentGoalValue;
    private View mEstimationTracker;
    private CustomFontTextView mEstimationTrackerText;
    private GoalProcessor mGoalProcessor;
    @Inject
    GoalProcessorManager mGoalProcessorManager;
    private CustomFontTextView mGoalText;
    private GoalUpdateTask mGoalUpdateTask;
    private GoalUpdateProgress mGoalUpdatedOnCloud;
    private CustomFontTextView mInsight;
    private LinearLayout mLinearlayoutEstimation;
    @Inject
    MultiDeviceManager mMultiDeviceManager;
    @Inject
    RestService mRestService;
    protected TextView mTooEasyMessage;
    private ProgressBar mUpdateProgressBar;
    private int mUpdatedGoalValue;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum GoalUpdateProgress {
        START,
        DEVICE_CONNECTED,
        DEVICE_NOT_CONNECTED,
        PUSHED_ON_DEVICE,
        PUSHED_ON_CLOUD,
        ERROR_ON_DEVICE,
        ERROR_ON_CLOUD,
        DONE
    }

    public abstract void CircularSeekBarValueChanged(int i);

    protected abstract String getGoalStatusText();

    protected abstract GoalType getGoalType();

    protected abstract int getIncrementValue();

    protected abstract String getInsightText();

    protected abstract int getMaxValue();

    protected abstract int getMinStartValue();

    protected abstract boolean shouldShowEstimationTracker();

    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        ViewGroup container = (ViewGroup) inflater.inflate(R.layout.edit_goal_fragment, (ViewGroup) null);
        setContentView(container);
        Bundle data = getIntent().getExtras();
        this.mCurrentGoalId = data.getString(Constants.GOAL_ID);
        this.mCurrentGoalName = data.getString(Constants.GOAL_NAME);
        this.mCurrentGoalValue = data.getInt(Constants.GOAL_VALUE);
        this.mUpdatedGoalValue = -1;
        this.mAllGoalsValue = new HashMap();
        this.mAllGoalsValue.put(GoalType.CALORIE, Long.valueOf(data.getInt(Constants.CALORIES_GOAL_VALUE)));
        this.mAllGoalsValue.put(GoalType.STEP, Long.valueOf(data.getInt(Constants.STEPS_GOAL_VALUE)));
        this.mEstimationTracker = inflater.inflate(R.layout.goal_estimation_tracker, container, false);
        this.mEstimationTrackerText = (CustomFontTextView) ViewUtils.getValidView(this.mEstimationTracker, R.id.value, CustomFontTextView.class);
        this.mLinearlayoutEstimation = (LinearLayout) ViewUtils.getValidView(container, R.id.estimation_tracker, LinearLayout.class);
        this.mCircularSeekBar = (CircularSeekBar) ViewUtils.getValidView(container, R.id.goal_circular_seek_bar, CircularSeekBar.class);
        this.mUpdateProgressBar = (ProgressBar) ViewUtils.getValidView(container, R.id.goal_update_progess, ProgressBar.class);
        this.mInsight = (CustomFontTextView) ViewUtils.getValidView(container, R.id.goal_insight, CustomFontTextView.class);
        this.mTooEasyMessage = (TextView) ViewUtils.getValidView(container, R.id.goal_too_easy_for_you_message, TextView.class);
        this.mGoalText = (CustomFontTextView) ViewUtils.getValidView(container, R.id.goal_edit_text, CustomFontTextView.class);
        initializeEstimationTrackers();
        if (shouldShowEstimationTracker()) {
            this.mLinearlayoutEstimation.addView(this.mEstimationTracker);
        }
        this.mCircularSeekBar.setMinStartValue(getMinStartValue());
        this.mCircularSeekBar.setIncrementValue(getIncrementValue());
        this.mCircularSeekBar.setMaxValue(getMaxValue());
        this.mCircularSeekBar.setOnValueChangedListener(this);
        this.mCircularSeekBar.setInitialValue(this.mCurrentGoalValue);
        this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(container, R.id.confirmation_bar, ConfirmationBar.class);
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.EditGoalActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                EditGoalActivity.this.updateAndExit();
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.EditGoalActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                EditGoalActivity.this.exit();
            }
        });
        this.mGoalProcessor = this.mGoalProcessorManager.getGoalProcessor(getGoalType());
        this.mInsight.setText(getInsightText());
        this.mGoalText.setText(getGoalStatusText());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setEstimationTrackerText(String text) {
        this.mEstimationTrackerText.setText(text);
    }

    private void setEstimationTrackersInfo(View estimationTrackerView, int titleResId) {
        CustomFontTextView title = (CustomFontTextView) ViewUtils.getValidView(estimationTrackerView, R.id.title, CustomFontTextView.class);
        if (titleResId != -1) {
            title.setText(titleResId);
        }
    }

    protected void initializeEstimationTrackers() {
        setEstimationTrackersInfo(this.mEstimationTracker, R.string.tracker_header_steps_distance);
    }

    @Override // com.microsoft.kapp.tasks.OnTaskStateChangedListener
    public void onTaskFailed(StateListenerTask task, Exception ex) {
        progressGoalUpdate(GoalUpdateProgress.ERROR_ON_CLOUD);
    }

    @Override // com.microsoft.kapp.tasks.GoalUpdateTask.OnGoalUpdateTaskListener
    public void onGoalUpdated(GoalOperationResultDto result) {
        this.mGoalUpdatedOnCloud = GoalUpdateProgress.PUSHED_ON_CLOUD;
        progressGoalUpdate(GoalUpdateProgress.PUSHED_ON_CLOUD);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void progressGoalUpdate(GoalUpdateProgress progress) {
        switch (progress) {
            case START:
                this.mCircularSeekBar.setEditMode(false);
                this.mUpdateProgressBar.setVisibility(0);
                isDeviceConnected();
                return;
            case DEVICE_CONNECTED:
                pushGoaltoCloud();
                return;
            case DEVICE_NOT_CONNECTED:
                if (this.mMultiDeviceManager.hasBand()) {
                    getDialogManager().showDialog(this, Integer.valueOf((int) R.string.connection_with_device_error_title), Integer.valueOf((int) R.string.edit_goal_update_error_device_not_connected), DialogPriority.LOW);
                    this.mCircularSeekBar.setEditMode(true);
                    this.mUpdateProgressBar.setVisibility(8);
                    this.mCircularSeekBar.setCurrentValue(this.mCurrentGoalValue);
                    this.mConfirmationBar.setVisibility(0);
                    return;
                }
                pushGoaltoCloud();
                return;
            case PUSHED_ON_CLOUD:
                pushGoaltoDevice();
                return;
            case ERROR_ON_CLOUD:
                getDialogManager().showDialog(this, Integer.valueOf((int) R.string.connection_with_cloud_error_title), Integer.valueOf((int) R.string.network_error_loading_data), DialogPriority.LOW);
                this.mCircularSeekBar.setEditMode(true);
                this.mUpdateProgressBar.setVisibility(8);
                this.mCircularSeekBar.setCurrentValue(this.mCurrentGoalValue);
                this.mConfirmationBar.setVisibility(0);
                return;
            case PUSHED_ON_DEVICE:
                exit();
                return;
            case ERROR_ON_DEVICE:
                if (this.mMultiDeviceManager.hasBand()) {
                    getDialogManager().showDialog(this, Integer.valueOf((int) R.string.connection_with_device_error_title), Integer.valueOf((int) R.string.profile_band_saving_error), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.activities.EditGoalActivity.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int id) {
                            EditGoalActivity.this.exit();
                        }
                    }, DialogPriority.LOW);
                    return;
                } else {
                    exit();
                    return;
                }
            case DONE:
                exit();
                return;
            default:
                return;
        }
    }

    private void pushGoaltoCloud() {
        this.mUpdatedGoalValue = this.mCircularSeekBar.getCurrentValue();
        this.mGoalUpdateTask = new GoalUpdateTask.Builder().forParentActivity(this).usingRestService(this.mRestService).withListener(this).withGoalData(this.mCurrentGoalId, this.mCurrentGoalName).withGoalValue(this.mUpdatedGoalValue).build();
        this.mGoalUpdateTask.execute();
    }

    private void pushGoaltoDevice() {
        GoalsPushDeviceTask goalsPushDeviceTask = new GoalsPushDeviceTask(this.mCargoConnection, this) { // from class: com.microsoft.kapp.activities.EditGoalActivity.4
            @Override // com.microsoft.kapp.tasks.GoalsPushDeviceTask
            protected void onExecuteSucceeded(Boolean result) {
                if (result.booleanValue()) {
                    EditGoalActivity.this.progressGoalUpdate(GoalUpdateProgress.PUSHED_ON_DEVICE);
                }
            }

            @Override // com.microsoft.kapp.tasks.GoalsPushDeviceTask
            protected void onExecuteFailed(Exception exception) {
                EditGoalActivity.this.progressGoalUpdate(GoalUpdateProgress.ERROR_ON_DEVICE);
                KLog.w(this.TAG, "Pushing Goal to the device failed with the following exception:", exception);
            }
        };
        this.mAllGoalsValue.put(getGoalType(), Long.valueOf(this.mCircularSeekBar.getCurrentValue()));
        goalsPushDeviceTask.setAllGoalsValue(this.mAllGoalsValue).execute(new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAndExit() {
        this.mConfirmationBar.setVisibility(8);
        progressGoalUpdate(GoalUpdateProgress.START);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exit() {
        if (this.mGoalUpdatedOnCloud == GoalUpdateProgress.PUSHED_ON_CLOUD) {
            exitWithSuccess();
        } else {
            exitWithError();
        }
    }

    private void exitWithSuccess() {
        HomeData.updateObservedData(getGoalType());
        finish();
    }

    private void exitWithError() {
        finish();
    }

    public String getDistanceText(int goalValue) {
        if (this.mGoalProcessor == null) {
            return null;
        }
        return Formatter.formatDistance(this, this.mGoalProcessor.calculateDistanceRequired(goalValue), this.mSettingsProvider.isDistanceHeightMetric()).toString();
    }

    public String getWalkingTimeText(int goalValue) {
        if (this.mGoalProcessor == null) {
            return null;
        }
        return Formatter.formatDurationSecondsToHrMin(this, this.mGoalProcessor.calculateNumberOfSecondsRequired(goalValue));
    }

    private void isDeviceConnected() {
        PingDeviceTask pingDeviceTask = new PingDeviceTask(this.mCargoConnection, this) { // from class: com.microsoft.kapp.activities.EditGoalActivity.5
            @Override // com.microsoft.kapp.tasks.PingDeviceTask
            protected void onExecuteSucceeded(Boolean isDeviceConnected) {
                if (isDeviceConnected.booleanValue()) {
                    EditGoalActivity.this.progressGoalUpdate(GoalUpdateProgress.DEVICE_CONNECTED);
                } else {
                    EditGoalActivity.this.progressGoalUpdate(GoalUpdateProgress.DEVICE_NOT_CONNECTED);
                }
            }

            @Override // com.microsoft.kapp.tasks.PingDeviceTask
            protected void onExecuteFailed(Exception exception) {
                EditGoalActivity.this.progressGoalUpdate(GoalUpdateProgress.DEVICE_NOT_CONNECTED);
                KLog.w(EditGoalActivity.this.TAG, "Updating Goals: pinging the device failed with the following exception:", exception);
            }
        };
        pingDeviceTask.execute(new Void[0]);
    }

    public boolean isGoalTooEasy(int goalValue) {
        if (this.mGoalProcessor != null) {
            return this.mGoalProcessor.isTooEasyForUser(goalValue);
        }
        return false;
    }
}
