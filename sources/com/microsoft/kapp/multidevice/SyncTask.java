package com.microsoft.kapp.multidevice;

import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.multidevice.KSyncResult;
import com.microsoft.kapp.multidevice.MultiDeviceConstants;
import com.microsoft.kapp.sensor.SensorDataProvider;
import com.microsoft.kapp.sensor.models.KSensorEvent;
import com.microsoft.kapp.sensor.models.KSensorEventType;
import com.microsoft.kapp.sensor.upload.SensorDataUploader;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class SyncTask implements Runnable {
    public static final int CHUNK_DURATION_MINUTES = 1440;
    private static final String TAG = SyncTask.class.getSimpleName();
    private MultiDeviceConstants.DeviceType mDeviceType;
    private SyncProgressCallback mProgressCallback;
    private SensorDataProvider mProvider;
    private SensorDataUploader mUploader;

    public SyncTask(SyncProgressCallback progressCallback, MultiDeviceConstants.DeviceType deviceType, SensorDataProvider provider, SensorDataUploader uploader) {
        this.mProgressCallback = progressCallback;
        this.mDeviceType = deviceType;
        this.mProvider = provider;
        this.mUploader = uploader;
    }

    @Override // java.lang.Runnable
    public void run() {
        KSensorEventType[] supportedEventTypes = this.mProvider.getSupportedEventTypes();
        this.mProgressCallback.registerForCallbacks(this.mDeviceType);
        boolean success = syncToCloud(supportedEventTypes);
        KSyncResult result = new KSyncResult();
        if (success) {
            result.setStatus(KSyncResult.SyncResultCode.SUCCESS);
        } else {
            result.setStatus(KSyncResult.SyncResultCode.CLOUD_ERROR);
        }
        this.mProgressCallback.onSyncProgress(this.mDeviceType, 100);
        this.mProgressCallback.onSyncComplete(this.mDeviceType, result);
    }

    private boolean syncToCloud(KSensorEventType[] sensorTypes) {
        DateTime startSyncTime = this.mProvider.getOldestEventTime();
        boolean success = true;
        if (startSyncTime == null) {
            return true;
        }
        DateTime startWindow = roundTime(startSyncTime, 5);
        DateTime.now();
        this.mProgressCallback.onSyncProgress(this.mDeviceType, 50);
        boolean terminate = false;
        while (!terminate) {
            DateTime endWindow = startWindow.plusMinutes(1440);
            if (endWindow.isAfterNow()) {
                endWindow = DateTime.now();
                terminate = true;
            }
            Map<KSensorEventType, KSensorEvent[]> eventMap = new HashMap<>();
            for (KSensorEventType sensorType : sensorTypes) {
                KSensorEvent[] events = this.mProvider.getEventsBetween(sensorType, startWindow, endWindow);
                eventMap.put(sensorType, events);
            }
            boolean uploadSuccess = this.mUploader.upload(this.mProvider.getDeviceId(), startWindow, eventMap);
            success &= uploadSuccess;
            if (uploadSuccess) {
                DateTime modifiedEndWindow = roundTime(endWindow, 5);
                this.mProvider.clearEventsBetween(startWindow, modifiedEndWindow);
            }
            KLog.v(TAG, String.format("Uploaded events for duration %s to %s ", startWindow.toString(), endWindow.toString()));
            startWindow = endWindow;
        }
        this.mProgressCallback.onSyncProgress(this.mDeviceType, 100);
        return success;
    }

    private DateTime roundTime(DateTime dateTime, int toNearest) {
        double minuteOfHour = dateTime.getMinuteOfHour();
        double nearest = minuteOfHour / toNearest;
        long round = (long) Math.floor(nearest);
        int i = (int) (toNearest * round);
        return dateTime.withMinuteOfHour(i).withSecondOfMinute(0).withMillisOfSecond(0);
    }
}
