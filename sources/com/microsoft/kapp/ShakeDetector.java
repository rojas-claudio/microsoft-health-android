package com.microsoft.kapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import com.microsoft.kapp.activities.DebugActivity;
import com.microsoft.kapp.activities.FeedbackActivity;
import com.microsoft.kapp.debug.KappConfig;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.feedback.FeedbackUtilsV1;
import com.microsoft.kapp.services.SettingsProvider;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class ShakeDetector implements SensorEventListener {
    private static final int MAX_PAUSE_BETWEEN_MOVEMENTS = 200;
    private static final int MAX_TOTAL_DURATION_OF_SHAKE = 800;
    private static final int MIN_MOVEMENTS_FOR_SHAKE = 10;
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.3f;
    private static Sensor mAccelerometer;
    private static Context mContext = null;
    private static ArrayList<String> mScreenshotStrings;
    private static SensorManager mSensorManager;
    private WeakReference<Activity> mActivity;
    @Inject
    SettingsProvider mSettingsProvider;
    private boolean mShakeDialogDisplayed;
    private long mShakeTimestamp;
    private int mMovementCounter = 0;
    private long mFirstMovementTime = 0;

    /* loaded from: classes.dex */
    public interface OnShakeListener {
        void onShake();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ShakeDetector() {
    }

    public ShakeDetector(Context context, SettingsProvider settingsProvider) {
        mContext = context;
        this.mSettingsProvider = settingsProvider;
        mSensorManager = (SensorManager) mContext.getSystemService("sensor");
        mAccelerometer = mSensorManager.getDefaultSensor(1);
    }

    public void registerListener(Activity activity) {
        this.mActivity = new WeakReference<>(activity);
        mSensorManager.registerListener(this, mAccelerometer, 2);
        this.mShakeDialogDisplayed = false;
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent event) {
        if (this.mActivity != null && this.mActivity.get() != null && !this.mShakeDialogDisplayed && didDeviceShake(event.values[0], event.values[1], event.values[2]) && this.mSettingsProvider.isShakeToSendFeedbackEnabled()) {
            displayShakeDetectedDialog(this.mActivity.get());
        }
    }

    private void displayShakeDetectedDialog(final Activity activity) {
        this.mShakeDialogDisplayed = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.shake_to_send_feedback_header)).setMessage(R.string.shake_to_send_feedback_content).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.microsoft.kapp.ShakeDetector.3
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialog) {
                ShakeDetector.this.mShakeDialogDisplayed = false;
            }
        }).setPositiveButton(R.string.feedback_notification_ticker_text, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.ShakeDetector.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                ShakeDetector.this.launchScreenshotsAsyncTask(activity);
                ShakeDetector.this.logTelemetry(TelemetryConstants.Events.ShakeDialog.Dimensions.SEND_FEEDBACK);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.ShakeDetector.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ShakeDetector.this.logTelemetry(TelemetryConstants.Events.ShakeDialog.Dimensions.CANCEL);
            }
        });
        if (KappConfig.isDebbuging) {
            builder.setNeutralButton(R.string.debug_settings, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.ShakeDetector.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ShakeDetector.this.launchDebugActivity(activity);
                    ShakeDetector.this.logTelemetry(TelemetryConstants.Events.ShakeDialog.Dimensions.DEBUG);
                }
            });
        }
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logTelemetry(String type) {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Selection", type);
        Telemetry.logEvent(TelemetryConstants.Events.ShakeDialog.EVENT_NAME, properties, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchDebugActivity(Activity activity) {
        Intent intent = new Intent(activity, DebugActivity.class);
        this.mActivity.get().startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchFeedbackActivity(Activity activity, ArrayList<String> screenshotStrings) {
        Intent intent = new Intent(activity, FeedbackActivity.class);
        intent.putExtra(FeedbackActivity.KEY_SCREENSHOT, screenshotStrings);
        intent.putExtra(FeedbackActivity.KEY_SENDER, activity.getClass().getSimpleName());
        this.mActivity.get().startActivity(intent);
    }

    public boolean didDeviceShake(float x, float y, float z) {
        float gX = x / 9.80665f;
        float gY = y / 9.80665f;
        float gZ = z / 9.80665f;
        double gForce = Math.sqrt((gX * gX) + (gY * gY) + (gZ * gZ));
        if (gForce <= 2.299999952316284d) {
            return false;
        }
        long now = System.currentTimeMillis();
        if (this.mFirstMovementTime == 0) {
            this.mFirstMovementTime = now;
            this.mShakeTimestamp = now;
        }
        if (now - this.mShakeTimestamp <= 200) {
            this.mShakeTimestamp = now;
            this.mMovementCounter++;
            if (this.mMovementCounter < 10 || now - this.mFirstMovementTime > 800) {
                return false;
            }
            resetShakeParameters();
            return true;
        }
        resetShakeParameters();
        return false;
    }

    private void resetShakeParameters() {
        this.mFirstMovementTime = 0L;
        this.mMovementCounter = 0;
        this.mShakeTimestamp = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchScreenshotsAsyncTask(final Activity activity) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() { // from class: com.microsoft.kapp.ShakeDetector.5
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... params) {
                ArrayList<Uri> screenshots = FeedbackUtilsV1.captureScreenshots(activity);
                ArrayList unused = ShakeDetector.mScreenshotStrings = new ArrayList();
                Iterator i$ = screenshots.iterator();
                while (i$.hasNext()) {
                    Uri uri = i$.next();
                    ShakeDetector.mScreenshotStrings.add(uri.toString());
                }
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public final void onPostExecute(Void result) {
                ShakeDetector.this.launchFeedbackActivity(activity, ShakeDetector.mScreenshotStrings);
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }
}
