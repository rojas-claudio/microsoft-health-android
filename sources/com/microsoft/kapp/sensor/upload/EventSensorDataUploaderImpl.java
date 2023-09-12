package com.microsoft.kapp.sensor.upload;

import com.facebook.internal.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.multidevice.KSyncResult;
import com.microsoft.kapp.multidevice.LegacySyncProgressCallback;
import com.microsoft.kapp.multidevice.MultiDeviceConstants;
import com.microsoft.kapp.sensor.models.KSensorEvent;
import com.microsoft.kapp.sensor.models.KSensorEventType;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.models.sensor.SensorData;
import com.microsoft.krestsdk.models.sensor.StepsSensor;
import com.microsoft.krestsdk.services.KRestException;
import com.microsoft.krestsdk.services.RestService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Seconds;
/* loaded from: classes.dex */
public class EventSensorDataUploaderImpl implements EventSensorDataUploader {
    private static final int MILLS_PER_MIN = 60000;
    private static final String TAG = EventSensorDataUploaderImpl.class.getName();
    private RestService mRestService;

    public EventSensorDataUploaderImpl(RestService restService) {
        this.mRestService = restService;
    }

    @Override // com.microsoft.kapp.sensor.upload.SensorDataUploader
    public boolean upload(String deviceId, DateTime startTime, Map<KSensorEventType, KSensorEvent[]> events) {
        Validate.notNull(deviceId, "DeviceId");
        Validate.notNull(startTime, "startTime");
        boolean success = false;
        SensorData sensorData = new SensorData();
        sensorData.setDeviceId(deviceId);
        sensorData.setOperationId(DateTime.now().getMillis());
        sensorData.setLogStartUTCTime(startTime.withZone(DateTimeZone.UTC));
        int offsetInMins = DateTimeZone.getDefault().getOffset(new DateTime()) / 60000;
        sensorData.setUploadTimeZoneOffset(offsetInMins);
        for (KSensorEventType eventType : events.keySet()) {
            success |= populateEvent(sensorData, eventType, events.get(eventType));
        }
        if (success) {
            try {
                this.mRestService.uploadSensorData(sensorData);
                return success;
            } catch (KRestException e) {
                KLog.w(TAG, "Unable to upload sensor data");
                return false;
            }
        }
        return success;
    }

    @Override // com.microsoft.kapp.sensor.upload.SensorDataUploader
    public KSyncResult legacyUpload(boolean isForeground, LegacySyncProgressCallback progressCallback) {
        return null;
    }

    private boolean populateEvent(SensorData sensorData, KSensorEventType eventType, KSensorEvent[] events) {
        switch (eventType) {
            case STEP:
                boolean success = populateSteps(sensorData, events);
                return success;
            default:
                KLog.e(TAG, "Unhandled sensor type");
                return false;
        }
    }

    private boolean populateSteps(SensorData sensorData, KSensorEvent[] events) {
        List<StepsSensor> steps = new ArrayList<>();
        DateTime startTime = sensorData.getLogStartUTCTime();
        if (events == null || events.length <= 0) {
            return false;
        }
        DateTime binStartTime = startTime;
        StepsSensor step = new StepsSensor();
        step.setOffset(Seconds.secondsBetween(binStartTime, startTime).getSeconds());
        step.setDuration(MultiDeviceConstants.BIN_SIZE_SECONDS);
        for (KSensorEvent event : events) {
            if (Seconds.secondsBetween(binStartTime, event.getEventTime()).getSeconds() > 300) {
                steps.add(step);
                binStartTime = binStartTime.plusSeconds(MultiDeviceConstants.BIN_SIZE_SECONDS);
                while (true) {
                    if ((!event.getEventTime().isAfter(binStartTime) || !event.getEventTime().isBefore(binStartTime.plusSeconds(MultiDeviceConstants.BIN_SIZE_SECONDS))) && !event.getEventTime().isEqual(binStartTime)) {
                        binStartTime = binStartTime.plusSeconds(MultiDeviceConstants.BIN_SIZE_SECONDS);
                    }
                }
                step = new StepsSensor();
                step.setOffset(Seconds.secondsBetween(startTime, binStartTime).getSeconds());
                step.setDuration(MultiDeviceConstants.BIN_SIZE_SECONDS);
            }
            if (event.getValue() > Constants.SPLITS_ACCURACY) {
                step.setSteps(step.getSteps() + ((long) event.getValue()));
            }
        }
        steps.add(step);
        sensorData.setSteps((StepsSensor[]) steps.toArray(new StepsSensor[steps.size()]));
        return true;
    }
}
