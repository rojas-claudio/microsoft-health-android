package com.microsoft.kapp.sensor.service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.sensor.SensorDataLogger;
import com.microsoft.kapp.sensor.models.StepSensorEvent;
import javax.inject.Inject;
import org.joda.time.DateTime;
@TargetApi(19)
/* loaded from: classes.dex */
public class SensorService extends Service implements SensorEventListener {
    private static final int BATCH_SENSOR_DELAY_MICROSECONDS = 60000000;
    private static final int NANOSECONDS_PER_MILLIS = 1000000;
    private static final String TAG = SensorService.class.getSimpleName();
    @Inject
    SensorDataLogger mSensorDataLogger;
    private SensorManager mSensorManager = null;
    private Sensor mSensor = null;
    private long mLastStepCounter = -1;
    private long mStartEventNanoseconds = -1;
    private DateTime mStartEventDateTime = DateTime.now();

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (this.mSensorManager != null) {
            this.mSensorManager.flush(this);
            return 1;
        }
        return 1;
    }

    @Override // android.app.Service
    public void onCreate() {
        KApplication application = (KApplication) getApplication();
        application.inject(this);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mSensor = this.mSensorManager.getDefaultSensor(19);
        if (this.mSensor != null) {
            boolean batchMode = this.mSensorManager.registerListener(this, this.mSensor, 3, BATCH_SENSOR_DELAY_MICROSECONDS);
            if (batchMode) {
                int hardwareBufferSize = this.mSensor.getFifoMaxEventCount();
                setScheduedAlarm(hardwareBufferSize);
            }
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this);
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent event) {
        new SensorEventLoggerTask().execute(event);
    }

    /* loaded from: classes.dex */
    private class SensorEventLoggerTask extends AsyncTask<SensorEvent, Void, Void> {
        private SensorEventLoggerTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(SensorEvent... events) {
            for (SensorEvent event : events) {
                long steps = (int) event.values[0];
                if (SensorService.this.mLastStepCounter == -1 || SensorService.this.mStartEventNanoseconds == -1) {
                    SensorService.this.mLastStepCounter = steps;
                    SensorService.this.mStartEventNanoseconds = event.timestamp;
                    SensorService.this.mStartEventDateTime = DateTime.now();
                } else {
                    long stepsThisEvent = steps - SensorService.this.mLastStepCounter;
                    SensorService.this.mLastStepCounter = steps;
                    if (stepsThisEvent >= 0) {
                        StepSensorEvent sensorEvent = new StepSensorEvent();
                        sensorEvent.setEventTime(DateTime.now());
                        sensorEvent.setValue(stepsThisEvent);
                        if (!SensorService.this.mSensorDataLogger.logStepEvent(sensorEvent)) {
                            KLog.w(SensorService.TAG, "unable to record steps for this event eventTime " + sensorEvent.getEventTime());
                        }
                    }
                }
            }
            return null;
        }
    }

    private void setScheduedAlarm(int hardwareBufferSize) {
        AlarmManager scheduler = (AlarmManager) getSystemService("alarm");
        Intent serviceIntent = new Intent(getApplicationContext(), SensorService.class);
        PendingIntent scheduledIntent = PendingIntent.getService(getApplicationContext(), 0, serviceIntent, 134217728);
        scheduler.cancel(scheduledIntent);
        scheduler.setInexactRepeating(0, System.currentTimeMillis(), 900000L, scheduledIntent);
    }
}
