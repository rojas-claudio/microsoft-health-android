package com.microsoft.kapp.version;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.ShakeDetector;
import com.microsoft.kapp.activities.FeedbackActivity;
import com.microsoft.kapp.activities.OobeFirmwareUpdateActivity;
import com.microsoft.kapp.activities.SignInActivity;
import com.microsoft.kapp.activities.SplashActivity;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.feedback.FeedbackUtilsV1;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.KAppBroadcastReceiver;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.tasks.FirmwareUpdateCheckTask;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FirmwareUpdateUtils;
import com.microsoft.krestsdk.auth.SignInContext;
import com.microsoft.krestsdk.models.BandVersion;
import com.microsoft.krestsdk.services.RestService;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Singleton;
import org.joda.time.DateTime;
@Singleton
/* loaded from: classes.dex */
public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks, FirmwareUpdateCheckTask.FirmwareUpdateNeededCallback, OnTaskListener {
    private static final String TAG = ActivityLifecycleListener.class.getSimpleName();
    private final CargoConnection mCargoConnection;
    private WeakReference<Activity> mCurrentActivity;
    private final MultiDeviceManager mMultiDeviceManager;
    private final SettingsProvider mSettingsProvider;
    private final ShakeDetector mShakeDetector;
    private AlertDialog mUpdateDialog;
    private final BroadcastReceiver mReceiver = new ActivityLifecycleEventBroadcastReceiver();
    private boolean mIsUpdateDialogShown = false;
    private AtomicInteger mActivityCount = new AtomicInteger(0);

    public ActivityLifecycleListener(CargoConnection cargoConnection, SettingsProvider settingsProvider, MultiDeviceManager multiDeviceManager, ShakeDetector shakeDetector) {
        this.mCargoConnection = cargoConnection;
        this.mSettingsProvider = settingsProvider;
        this.mMultiDeviceManager = multiDeviceManager;
        this.mShakeDetector = shakeDetector;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
        if (!(activity instanceof SplashActivity) && !(activity instanceof SignInActivity) && !(activity instanceof OobeFirmwareUpdateActivity) && this.mUpdateDialog != null) {
            Activity owner = this.mUpdateDialog.getOwnerActivity();
            if (owner != null && !owner.isFinishing() && !owner.isDestroyed()) {
                this.mUpdateDialog.dismiss();
            }
            this.mUpdateDialog = null;
            this.mIsUpdateDialogShown = false;
        }
        if (!Compatibility.isPublicRelease()) {
            FeedbackUtilsV1.cancelFeedbackNotification(activity);
        }
        try {
            this.mShakeDetector.unregisterListener();
            activity.unregisterReceiver(this.mReceiver);
        } catch (Exception ex) {
            KLog.w(TAG, "unable to unregister activity ", ex);
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        if (!(activity instanceof SplashActivity) && !(activity instanceof SignInActivity) && !(activity instanceof OobeFirmwareUpdateActivity) && this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN && this.mMultiDeviceManager.hasBand()) {
            checkForFirmwareUpdate(activity);
        }
        boolean shakeToSendFeedbackEnabled = this.mSettingsProvider.isShakeToSendFeedbackEnabled();
        if (!(activity instanceof FeedbackActivity) && !(activity instanceof SignInActivity) && shakeToSendFeedbackEnabled) {
            this.mShakeDetector.registerListener(activity);
        }
        boolean isPublicRelease = Compatibility.isPublicRelease();
        if (!isPublicRelease) {
            FeedbackUtilsV1.buildFeedbackNotification(activity);
            IntentFilter filter = new IntentFilter("Kapp.feedback");
            activity.registerReceiver(this.mReceiver, filter);
        }
        activity.registerReceiver(this.mReceiver, new IntentFilter(RestService.SIGN_IN_REQUIRED_INTENT));
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public synchronized void onActivityStarted(Activity activity) {
        if (!(activity instanceof SplashActivity) && !(activity instanceof SignInActivity) && this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN) {
            DateTime lastSyncTime = this.mMultiDeviceManager.getLastSyncTime();
            if (lastSyncTime == null || DateTime.now().getMillis() - lastSyncTime.getMillis() > Constants.BACKGROUND_SYNC_INTERVAL_MS) {
                this.mMultiDeviceManager.startSync(false);
            }
            this.mCurrentActivity = new WeakReference<>(activity);
        }
        this.mActivityCount.incrementAndGet();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkForFirmwareUpdate(Context context) {
        if (FirmwareUpdateUtils.isFirmwareUpdateCheckRequired(this.mSettingsProvider)) {
            FirmwareUpdateCheckTask task = new FirmwareUpdateCheckTask(context, this, this.mSettingsProvider, this.mCargoConnection, false, this);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void signInRequested(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra(SignInActivity.ARG_IN_SIGN_CONTEXT, SignInContext.MANUAL);
        context.startActivity(intent);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public synchronized void onActivityStopped(Activity activity) {
        int totalActivityCount = this.mActivityCount.decrementAndGet();
        if (totalActivityCount == 0) {
            KLog.flushAndClose();
        }
    }

    @Override // com.microsoft.kapp.OnTaskListener
    public boolean isWaitingForResult() {
        return true;
    }

    @Override // com.microsoft.kapp.tasks.FirmwareUpdateCheckTask.FirmwareUpdateNeededCallback
    public synchronized void onFirmwareUpdateNeeded(CheckedFirmwareUpdateInfo firmwareUpdateInfo, final BandVersion bandVersion) {
        final Activity currentActivity = this.mCurrentActivity.get();
        if (!this.mIsUpdateDialogShown && currentActivity != null && !(currentActivity instanceof OobeFirmwareUpdateActivity)) {
            final boolean isUpdateOptional = firmwareUpdateInfo.isIsUpdateOptional();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(currentActivity);
            alertDialogBuilder.setTitle(R.string.firmware_update_activity_title).setMessage(R.string.oobe_firmware_update_required_message).setCancelable(false).setPositiveButton(R.string.firmware_update_activity_update, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.version.ActivityLifecycleListener.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    ActivityLifecycleListener.this.mIsUpdateDialogShown = false;
                    Intent intent = new Intent(currentActivity, OobeFirmwareUpdateActivity.class);
                    intent.putExtra(OobeFirmwareUpdateActivity.ARG_IN_EXTRA_IS_IN_APP_UPDATE, true);
                    intent.putExtra(OobeFirmwareUpdateActivity.ARG_IN_EXTRA_IS_OPTIONAL_APP_UPDATE, isUpdateOptional);
                    intent.putExtra(PersonalizationManagerFactory.BAND_VERSION_ID, bandVersion.ordinal());
                    currentActivity.startActivity(intent);
                }
            });
            if (isUpdateOptional) {
                this.mSettingsProvider.setCheckedFirmwareUpdateInfo(new CheckedFirmwareUpdateInfo(true, true, firmwareUpdateInfo.getFirmwareVersion(), DateTime.now()));
                alertDialogBuilder.setNegativeButton(R.string.firmware_update_activity_skip, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.version.ActivityLifecycleListener.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityLifecycleListener.this.mIsUpdateDialogShown = false;
                    }
                });
            }
            this.mUpdateDialog = alertDialogBuilder.create();
            if (!this.mIsUpdateDialogShown) {
                if (!currentActivity.isFinishing()) {
                    this.mUpdateDialog.show();
                    this.mIsUpdateDialogShown = true;
                } else {
                    this.mSettingsProvider.setCheckedFirmwareUpdateInfo(null);
                }
            }
        }
    }

    @Override // com.microsoft.kapp.tasks.FirmwareUpdateCheckTask.FirmwareUpdateNeededCallback
    public void onFirmwareUpdateNotNeeded(CheckedFirmwareUpdateInfo firmwareUpdateInfo) {
    }

    @Override // com.microsoft.kapp.tasks.FirmwareUpdateCheckTask.FirmwareUpdateNeededCallback
    public void onFirmwareUpdateCheckFailed(int failureCode) {
    }

    /* loaded from: classes.dex */
    private final class ActivityLifecycleEventBroadcastReceiver extends BroadcastReceiver {
        private ActivityLifecycleEventBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            try {
                Activity currentActivity = (Activity) ActivityLifecycleListener.this.mCurrentActivity.get();
                if (currentActivity != null) {
                    String action = intent.getAction();
                    if ("Kapp.feedback".equals(action)) {
                        Intent logCleanup = new Intent(currentActivity, KAppBroadcastReceiver.class);
                        logCleanup.setAction(Constants.INTENT_LOG_CLEANUP);
                        currentActivity.sendBroadcast(logCleanup);
                        View view = currentActivity.getWindow().getDecorView();
                        FeedbackUtilsV1.sendFeedbackAsync(view, currentActivity, ActivityLifecycleListener.this.mCargoConnection);
                    } else if (Constants.FIRMWARE_UPDATE_REQUIRED_INTENT.equals(action)) {
                        ActivityLifecycleListener.this.checkForFirmwareUpdate(currentActivity);
                    } else if (RestService.SIGN_IN_REQUIRED_INTENT.equals(action)) {
                        ActivityLifecycleListener.this.signInRequested(currentActivity);
                    }
                }
            } catch (Exception exception) {
                KLog.d(ActivityLifecycleListener.TAG, "unable to send feedback ", exception);
            }
        }
    }
}
