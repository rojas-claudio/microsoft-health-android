package com.microsoft.kapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import com.microsoft.band.FinalizeOOBE;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.band.device.WorkoutActivity;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.device.ProfileRegionSettings;
import com.microsoft.kapp.device.ProfileUpdateCallback;
import com.microsoft.kapp.device.ProfileUpdateTask;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.strapp.DefaultStrappManager;
import com.microsoft.kapp.models.strapp.StrappStateCollection;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.sensor.service.SensorService;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.tasks.ConnectPhoneTask;
import com.microsoft.kapp.tasks.GoalsPushDeviceTask;
import com.microsoft.kapp.tasks.SettingsProfileSaveTask;
import com.microsoft.kapp.tasks.StrappsPersonalizationSaveTask;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.GoalsUtils;
import com.microsoft.kapp.utils.GsonUtils;
import com.microsoft.kapp.utils.LocaleProvider;
import com.microsoft.kapp.utils.StrappUtils;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.services.RestService;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class LastOobeActivity extends OobeBaseActivity implements ConnectPhoneTask.IConnectPhoneTaskListener {
    private static final String CALORIES_DEFAULT_GOAL = "caloriesDefaultGoal";
    private static final String OOBE_COMPLETED_SERIAL_OPERATION_PROGRESS = "mOobeCompletedSerialOperationProgress";
    private static final String STEPS_DEFAULT_GOAL = "stepsDefaultGoal";
    private static final String TAG = LastOobeActivity.class.getSimpleName();
    public static final int TASK_CALL_GET_CLOUD_DEFAULT_GOALS = 7;
    public static final int TASK_CONNECT_PHONE = 10;
    public static final int TASK_DEFAULT_STRAPPS = 5;
    public static final int TASK_FINALIZE_DEVICE_OOBE = 9;
    public static final int TASK_OOBE_COMPLETE_FLAG = 0;
    public static final int TASK_SAVE_USER_PROFILE = 2;
    public static final int TASK_SET_DEFAULT_TIME = 4;
    public static final int TASK_SET_DEVICE_DEFAULT_GOALS = 8;
    public static final int TASK_SET_OOBE_COMPLETE_FLAG_ON_CLOUD = 6;
    public static final int TASK_SET_TIME_ZONE_AND_EPHEMERIS = 1;
    private Map<GoalType, Long> mAllGoalsValue;
    @Inject
    MsaAuth mAuthService;
    @Inject
    DefaultStrappManager mDefaultStrappManager;
    private boolean mHasFinalizeFailed;
    @Inject
    MultiDeviceManager mMultiDeviceManager;
    @Inject
    PersonalizationManagerFactory mPersonalizationManagerFactory;
    private Map<Integer, ScopedAsyncTask> mRemainingTasks;
    @Inject
    RestService mRestService;
    private CargoUserProfile mUserProfile;
    @Inject
    UserProfileFetcher mUserProfileFetcher;
    private int mOobeCompletedSerialOperationsProgress = 6;
    private boolean mCompleted = false;
    private boolean mSomeTaskFailed = false;

    public abstract void taskFailed(int i, Exception exc, boolean z, boolean z2);

    public abstract void taskSucceed();

    public void completeOOBE() {
        if (this.mRemainingTasks == null) {
            initTasks();
        } else if (this.mSomeTaskFailed) {
            restartTasks();
        }
    }

    @SuppressLint({"UseSparseArrays"})
    private void initTasks() {
        this.mRemainingTasks = new ConcurrentHashMap();
        this.mUserProfile = (CargoUserProfile) GsonUtils.getCustomDeserializer().fromJson(this.mSettingsProvider.getOobeUserProfile(), (Class<Object>) CargoUserProfile.class);
        if (this.mSettingsProvider.shouldOobeConnectBand()) {
            startDefaultStrappsTask();
            startDeviceTimeTask();
            startEphemerisAndTimeZoneTask();
        }
        if (this.mSettingsProvider.shouldOobeConnectPhone()) {
            startConnectPhoneTask();
        }
        startSaveUserProfileTask();
    }

    private void restartTasks() {
        this.mHasFinalizeFailed = false;
        for (Integer taskId : this.mRemainingTasks.keySet()) {
            switch (taskId.intValue()) {
                case 0:
                    startOobeCompleteTask();
                    break;
                case 1:
                    startEphemerisAndTimeZoneTask();
                    break;
                case 2:
                    startSaveUserProfileTask();
                    break;
                case 4:
                    startDeviceTimeTask();
                    break;
                case 5:
                    startDefaultStrappsTask();
                    break;
                case 10:
                    startConnectPhoneTask();
                    break;
            }
        }
    }

    private void startDeviceTimeTask() {
        DeviceTimeTask deviceTimeTask = new DeviceTimeTask(this.mCargoConnection, this);
        this.mRemainingTasks.put(4, deviceTimeTask);
        deviceTimeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    private void startConnectPhoneTask() {
        ConnectPhoneTask deviceTimeTask = new ConnectPhoneTask(this.mMultiDeviceManager, this, this.mSettingsProvider, this.mUserProfileFetcher, this.mSensorUtils);
        this.mRemainingTasks.put(10, deviceTimeTask);
        deviceTimeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSaveUserProfileTask() {
        if (this.mUserProfile != null) {
            startSaveTaskWithValidProfile();
            return;
        }
        ProfileUpdateTask fetcher = new ProfileUpdateTask(this.mCargoConnection, this.mSettingsProvider, this.mUserProfileFetcher, this.mAuthService, new ProfileUpdateCallback() { // from class: com.microsoft.kapp.activities.LastOobeActivity.1
            @Override // com.microsoft.kapp.device.ProfileUpdateCallback
            public void onProfileRetrieved(CargoUserProfile profile) {
                if (profile != null) {
                    LastOobeActivity.this.mUserProfile = profile;
                    LastOobeActivity.this.startSaveTaskWithValidProfile();
                    return;
                }
                LastOobeActivity.this.onFailed(2, null, true, false);
            }
        }, this);
        fetcher.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSaveTaskWithValidProfile() {
        setLocaleRegion();
        UserProfileSaveTask userProfileSaveTask = new UserProfileSaveTask(this.mCargoConnection, this.mSettingsProvider, this.mUserProfile, this, this.mUserProfileFetcher, this.mSensorUtils);
        this.mRemainingTasks.put(2, userProfileSaveTask);
        userProfileSaveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    private void setLocaleRegion() {
        ProfileRegionSettings locale = LocaleProvider.getLocaleSettings(this);
        if (TextUtils.isEmpty(this.mUserProfile.getPreferredLocale())) {
            this.mUserProfile.setPreferredLocale(locale.getLocaleName());
        }
        if (TextUtils.isEmpty(this.mUserProfile.getPreferredRegion())) {
            this.mUserProfile.setPreferredRegion(locale.getCargoLocation().toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDefaultStrappsTask() {
        StrappStateCollection defaultStrapps = StrappUtils.createStrappStateCollection(this.mDefaultStrappManager.getOobeStrappDefinitions(), true);
        DefaultStrappsTask defaultStrappsTask = new DefaultStrappsTask(this.mCargoConnection, defaultStrapps, this.mSettingsProvider, this.mPersonalizationManagerFactory, getApplicationContext(), this);
        this.mRemainingTasks.put(5, defaultStrappsTask);
        defaultStrappsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startEphemerisAndTimeZoneTask() {
        UpdateEphemerisAndTimeZone ephemerisTmeZoneTask = new UpdateEphemerisAndTimeZone(this.mCargoConnection, this);
        this.mRemainingTasks.put(1, ephemerisTmeZoneTask);
        ephemerisTmeZoneTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startGetCloudDefaultGoals() {
        this.mOobeCompletedSerialOperationsProgress = 7;
        this.mRestService.getGoals(new ActivityScopedCallback(this, new Callback<List<GoalDto>>() { // from class: com.microsoft.kapp.activities.LastOobeActivity.2
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GoalDto> goals) {
                if (!Validate.isNotNullNotEmpty(goals)) {
                    LastOobeActivity.this.onFailed(7, null);
                    return;
                }
                LastOobeActivity.this.mAllGoalsValue = new HashMap();
                for (GoalDto goal : goals) {
                    if (goal != null) {
                        LastOobeActivity.this.mAllGoalsValue.put(goal.getType(), Long.valueOf(GoalsUtils.getGoalValue(goal)));
                    }
                }
                LastOobeActivity.this.startSetDeviceDefaultGoalsTask();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(LastOobeActivity.TAG, "getGoals() failed.", ex);
                LastOobeActivity.this.onFailed(7, ex);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSetDeviceDefaultGoalsTask() {
        if (this.mAllGoalsValue == null) {
            this.mOobeCompletedSerialOperationsProgress = 7;
            onFailed(7, null);
            return;
        }
        this.mOobeCompletedSerialOperationsProgress = 8;
        SetDeviceDefaultGoalsTask deviceDefaultGoalsTask = new SetDeviceDefaultGoalsTask(this.mCargoConnection, this);
        deviceDefaultGoalsTask.setAllGoalsValue(this.mAllGoalsValue);
        deviceDefaultGoalsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    private void startOobeCompleteTask() {
        if (this.mOobeCompletedSerialOperationsProgress == 6) {
            OobeCompletedTask completedTask = new OobeCompletedTask(this.mCargoConnection, this, this.mUserProfileFetcher, this.mSettingsProvider);
            this.mRemainingTasks.put(0, completedTask);
            completedTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } else if (this.mOobeCompletedSerialOperationsProgress == 7) {
            startGetCloudDefaultGoals();
        } else if (this.mOobeCompletedSerialOperationsProgress == 8) {
            startSetDeviceDefaultGoalsTask();
        } else if (this.mOobeCompletedSerialOperationsProgress == 9) {
            startFinalizeDeviceTask();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFinalizeDeviceTask() {
        this.mOobeCompletedSerialOperationsProgress = 9;
        FinishDeviceOobeTask finishDeviceOobeTask = new FinishDeviceOobeTask(this.mCargoConnection, this.mSettingsProvider, this);
        finishDeviceOobeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    private void markAsCompleted() {
        this.mCompleted = true;
    }

    @Override // com.microsoft.kapp.tasks.ConnectPhoneTask.IConnectPhoneTaskListener
    public void onConnectPhoneSucceeded() {
        Intent sensorServiceIntent = new Intent(this, SensorService.class);
        startService(sensorServiceIntent);
        onSucceed(10);
    }

    @Override // com.microsoft.kapp.tasks.ConnectPhoneTask.IConnectPhoneTaskListener
    public void onConnectPhoneFailed(Exception ex, boolean isCloudError, boolean isBandError) {
        onFailed(10, ex);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSucceed(int taskId) {
        this.mRemainingTasks.remove(Integer.valueOf(taskId));
        if (this.mRemainingTasks.isEmpty()) {
            if (this.mCompleted) {
                taskSucceed();
            } else {
                startOobeCompleteTask();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFailed(int taskId, Exception exception) {
        onFailed(taskId, exception, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFailed(int taskId, Exception exception, boolean optionalIsCloudError, boolean optionalIsDeviceError) {
        this.mSomeTaskFailed = true;
        for (ScopedAsyncTask task : this.mRemainingTasks.values()) {
            task.cancel(true);
        }
        taskFailed(taskId, exception, optionalIsCloudError, optionalIsDeviceError);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DefaultStrappsTask extends StrappsPersonalizationSaveTask {
        private WeakReference<LastOobeActivity> mWeakActivity;

        public DefaultStrappsTask(CargoConnection cargoConnection, StrappStateCollection strapps, SettingsProvider settingsProvider, PersonalizationManagerFactory personalizationManagerFactory, Context applicationContext, LastOobeActivity onTaskListener) {
            super(cargoConnection, strapps, settingsProvider, personalizationManagerFactory, applicationContext, onTaskListener);
            this.mWeakActivity = new WeakReference<>(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.tasks.StrappsTask
        public void onExecuteSucceeded(Void result) {
            LastOobeActivity lastOobeActivity = this.mWeakActivity.get();
            if (lastOobeActivity != null) {
                lastOobeActivity.onSucceed(5);
            }
        }

        @Override // com.microsoft.kapp.tasks.StrappsTask
        protected void onExecuteFailed(Exception exception) {
            LastOobeActivity lastOobeActivity = this.mWeakActivity.get();
            if (lastOobeActivity != null) {
                KLog.e(this.TAG, "DefaultStrappsTask failed!", exception);
                lastOobeActivity.onFailed(5, exception);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class OobeCompletedTask extends ScopedAsyncTask<Void, Void, Void> {
        private CargoConnection mCargoConnection;
        private SettingsProvider mSettingsProvider;
        private UserProfileFetcher mUserProfileFetcher;
        private WeakReference<LastOobeActivity> mWeakActivity;

        public OobeCompletedTask(CargoConnection cargoConnection, LastOobeActivity onTaskListener, UserProfileFetcher userProfileFetcher, SettingsProvider settingsProvider) {
            super(onTaskListener);
            this.mCargoConnection = cargoConnection;
            this.mWeakActivity = new WeakReference<>(onTaskListener);
            this.mUserProfileFetcher = userProfileFetcher;
            this.mSettingsProvider = settingsProvider;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Void doInBackground(Void... params) {
            try {
                this.mCargoConnection.setOOBEComplete();
                this.mUserProfileFetcher.updateLocallyStoredValues();
                return null;
            } catch (Exception exception) {
                setException(exception);
                KLog.d(this.TAG, "Failed to mark oobe as complete on the cloud", exception);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Void aVoid) {
            LastOobeActivity lastOobeActivity = this.mWeakActivity.get();
            if (lastOobeActivity != null) {
                if (getException() != null) {
                    lastOobeActivity.onFailed(0, getException());
                } else if (this.mSettingsProvider.shouldOobeConnectBand()) {
                    lastOobeActivity.startGetCloudDefaultGoals();
                } else {
                    lastOobeActivity.onOobeComplete();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logOobeCompleteEvent(WeakReference<LastOobeActivity> mWeakActivity, CargoUserProfile userProfile, SensorUtils sensorUtils, SettingsProvider settingsProvider) {
        String motionTrackingValue;
        if (userProfile != null && !userProfile.isOobeComplete()) {
            HashMap<String, String> properties = new HashMap<>();
            int age = userProfile.getAge();
            properties.put(TelemetryConstants.Events.OobeComplete.Dimensions.GENDER, userProfile.getGender() == UserProfileInfo.Gender.female ? TelemetryConstants.Events.OobeComplete.Dimensions.GENDER_FEMALE : TelemetryConstants.Events.OobeComplete.Dimensions.GENDER_MALE);
            properties.put(TelemetryConstants.Events.OobeComplete.Dimensions.AGE_GROUP, CommonUtils.getAgeGroup(age));
            if (mWeakActivity != null) {
                Activity activity = mWeakActivity.get();
                if (activity != null) {
                    if (sensorUtils.isKitkatWithStepSensor()) {
                        motionTrackingValue = settingsProvider.shouldOobeConnectPhone() ? "Enabled" : "Disabled";
                    } else {
                        motionTrackingValue = TelemetryConstants.Events.OobeComplete.Dimensions.MOTION_TRACKING_UNSUPPORTED;
                    }
                    properties.put(TelemetryConstants.Events.OobeComplete.Dimensions.MOTION_TRACKING, motionTrackingValue);
                    properties.put(TelemetryConstants.Events.OobeComplete.Dimensions.PAIRED_BAND, settingsProvider.shouldOobeConnectBand() ? "Yes" : "No");
                }
            }
            Telemetry.logEvent(TelemetryConstants.Events.OobeComplete.EVENT_NAME, properties, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class UserProfileSaveTask extends SettingsProfileSaveTask {
        private WeakReference<LastOobeActivity> mWeakActivity;

        public UserProfileSaveTask(CargoConnection cargoConnection, SettingsProvider settingsProvider, CargoUserProfile userProfile, LastOobeActivity onTaskListener, UserProfileFetcher userProfileFetcher, SensorUtils sensorUtils) {
            super(cargoConnection, settingsProvider, userProfile, userProfileFetcher, onTaskListener, true, Boolean.valueOf(settingsProvider.shouldOobeConnectBand()), onTaskListener);
            this.mWeakActivity = new WeakReference<>(onTaskListener);
            LastOobeActivity.logOobeCompleteEvent(this.mWeakActivity, userProfile, sensorUtils, settingsProvider);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Integer result) {
            LastOobeActivity lastOobeActivity = this.mWeakActivity.get();
            if (lastOobeActivity != null) {
                if ((getException() == null && result.intValue() == 9003) || (result.intValue() == 9001 && !lastOobeActivity.mSettingsProvider.shouldOobeConnectBand())) {
                    lastOobeActivity.onSucceed(2);
                    return;
                }
                KLog.w(this.TAG, "UserProfileSaveTask failed!", getException());
                lastOobeActivity.onFailed(2, getException(), result.intValue() == 9000, result.intValue() == 9001);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class UpdateEphemerisAndTimeZone extends ScopedAsyncTask<Void, Void, Boolean> {
        private CargoConnection mCargoConnection;
        private WeakReference<LastOobeActivity> mWeakActivity;

        public UpdateEphemerisAndTimeZone(CargoConnection cargoConnection, LastOobeActivity onTaskListener) {
            super(onTaskListener);
            this.mCargoConnection = cargoConnection;
            this.mWeakActivity = new WeakReference<>(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Boolean doInBackground(Void... params) {
            try {
                return Boolean.valueOf(this.mCargoConnection.downloadEphemerisAndTimeZone());
            } catch (Exception ex) {
                setException(ex);
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Boolean resultSuccess) {
            LastOobeActivity lastOobeActivity = this.mWeakActivity.get();
            if (lastOobeActivity != null) {
                if (getException() == null && resultSuccess.booleanValue()) {
                    lastOobeActivity.onSucceed(1);
                    return;
                }
                KLog.e(this.TAG, "UpdateEphemerisAndTimeZone failed!", getException());
                lastOobeActivity.onFailed(1, getException());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DeviceTimeTask extends ScopedAsyncTask<Void, Void, Boolean> {
        private CargoConnection mCargoConnection;
        private WeakReference<LastOobeActivity> mWeakActivity;

        public DeviceTimeTask(CargoConnection cargoConnection, LastOobeActivity onTaskListener) {
            super(onTaskListener);
            this.mCargoConnection = cargoConnection;
            this.mWeakActivity = new WeakReference<>(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Boolean doInBackground(Void... params) {
            try {
                this.mCargoConnection.setDeviceTime();
                return null;
            } catch (Exception ex) {
                setException(ex);
                KLog.d(this.TAG, "Failed to send OOBE command to the device.", ex);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Boolean result) {
            LastOobeActivity lastOobeActivity = this.mWeakActivity.get();
            if (lastOobeActivity != null) {
                lastOobeActivity.onSucceed(4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SetDeviceDefaultGoalsTask extends GoalsPushDeviceTask {
        private WeakReference<LastOobeActivity> mActivityWeakRef;

        public SetDeviceDefaultGoalsTask(CargoConnection cargoConnection, LastOobeActivity onTaskListener) {
            super(cargoConnection, onTaskListener);
            this.mActivityWeakRef = new WeakReference<>(onTaskListener);
        }

        @Override // com.microsoft.kapp.tasks.GoalsPushDeviceTask
        protected void onExecuteSucceeded(Boolean result) {
            LastOobeActivity lastOobeActivity = this.mActivityWeakRef.get();
            if (lastOobeActivity != null) {
                lastOobeActivity.startFinalizeDeviceTask();
            }
        }

        @Override // com.microsoft.kapp.tasks.GoalsPushDeviceTask
        protected void onExecuteFailed(Exception exception) {
            KLog.e(this.TAG, "Pushing default goals to the K device failed: %s", exception);
            LastOobeActivity lastOobeActivity = this.mActivityWeakRef.get();
            if (lastOobeActivity != null) {
                lastOobeActivity.onFailed(8, exception);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mOobeCompletedSerialOperationsProgress = savedInstanceState.getInt(OOBE_COMPLETED_SERIAL_OPERATION_PROGRESS, 6);
            if (this.mOobeCompletedSerialOperationsProgress > 7) {
                long calories = savedInstanceState.getLong(CALORIES_DEFAULT_GOAL, -1L);
                long steps = savedInstanceState.getLong(STEPS_DEFAULT_GOAL, -1L);
                if (calories != -1 && steps != -1) {
                    this.mAllGoalsValue = new HashMap();
                    this.mAllGoalsValue.put(GoalType.CALORIE, Long.valueOf(calories));
                    this.mAllGoalsValue.put(GoalType.STEP, Long.valueOf(steps));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(OOBE_COMPLETED_SERIAL_OPERATION_PROGRESS, this.mOobeCompletedSerialOperationsProgress);
        if (this.mOobeCompletedSerialOperationsProgress > 7 && this.mAllGoalsValue != null) {
            outState.putLong(CALORIES_DEFAULT_GOAL, this.mAllGoalsValue.get(GoalType.CALORIE).longValue());
            outState.putLong(STEPS_DEFAULT_GOAL, this.mAllGoalsValue.get(GoalType.STEP).longValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class FinishDeviceOobeTask extends ScopedAsyncTask<Void, Void, Boolean> {
        private LastOobeActivity mActivity;

        public FinishDeviceOobeTask(CargoConnection cargoConnection, SettingsProvider settingsProvider, LastOobeActivity onTaskListener) {
            super(onTaskListener);
            LastOobeActivity.this.mCargoConnection = cargoConnection;
            LastOobeActivity.this.mSettingsProvider = settingsProvider;
            this.mActivity = onTaskListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Boolean doInBackground(Void... params) {
            try {
                String[] callResponses = LastOobeActivity.this.mSettingsProvider.getCallsAutoReplies();
                String[] textResponses = LastOobeActivity.this.mSettingsProvider.getMessagingAutoReplies();
                List<Pair<Integer, UUID>> exercisePairOptions = StrappSettingsManager.getExercisePickerOptions();
                LastOobeActivity.this.mCargoConnection.savePhoneCallResponses(callResponses[0], callResponses[1], callResponses[2], callResponses[3]);
                LastOobeActivity.this.mCargoConnection.saveSmsResponses(textResponses[0], textResponses[1], textResponses[2], textResponses[3]);
                List<WorkoutActivity> exerciseOptions = new LinkedList<>();
                for (Pair<Integer, UUID> pair : exercisePairOptions) {
                    exerciseOptions.add(new WorkoutActivity(this.mActivity.getResources().getString(((Integer) pair.first).intValue()), (UUID) pair.second));
                }
                LastOobeActivity.this.mCargoConnection.saveExerciseOptions(exerciseOptions);
                int result = LastOobeActivity.this.mCargoConnection.finishOobe();
                if (FinalizeOOBE.isError(result)) {
                    if (LastOobeActivity.this.mHasFinalizeFailed) {
                        throw new Exception("Finalize oobe has failed repeatedly!");
                    }
                    respondToError(result);
                }
                LastOobeActivity.this.mSettingsProvider.setResyncLastWorkoutNextSync(true);
                return true;
            } catch (Exception ex) {
                setException(ex);
                KLog.d(this.TAG, "Failed to move device to next screen Or to update SettingProvider data.", ex);
                return false;
            }
        }

        private void respondToError(int result) throws Exception {
            if (result == FinalizeOOBE.APP_LIST_MISSING) {
                LastOobeActivity.this.startDefaultStrappsTask();
            } else if (result == FinalizeOOBE.EPHEMERIS_FILE_MISSING || result == FinalizeOOBE.TIME_ZONE_FILE_MISSING || result == FinalizeOOBE.TIME_ZONE_INVALID) {
                LastOobeActivity.this.startEphemerisAndTimeZoneTask();
            } else if (result == FinalizeOOBE.PROFILE_FILE_MISSING || result == FinalizeOOBE.PROFILE_LANGUAGE_INVALID || result == FinalizeOOBE.PROFILE_LOCALE_INVALID || result == FinalizeOOBE.PROFILE_UNIT_INVALID) {
                LastOobeActivity.this.startSaveUserProfileTask();
            } else if (result == FinalizeOOBE.SMS_STRINGS_FILE_MISSING) {
                LastOobeActivity.this.startFinalizeDeviceTask();
            } else {
                throw new Exception("oobe finalization failed!");
            }
            LastOobeActivity.this.mHasFinalizeFailed = true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Boolean result) {
            if (getException() != null || !result.booleanValue()) {
                this.mActivity.onFailed(9, getException());
            } else {
                this.mActivity.onOobeComplete();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onOobeComplete() {
        markAsCompleted();
        onSucceed(0);
    }
}
