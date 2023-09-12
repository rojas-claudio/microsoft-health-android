package com.microsoft.kapp.sensor.upload;

import com.microsoft.kapp.multidevice.KSyncResult;
import com.microsoft.kapp.multidevice.LegacySyncProgressCallback;
import com.microsoft.kapp.sensor.models.KSensorEvent;
import com.microsoft.kapp.sensor.models.KSensorEventType;
import java.util.Map;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public interface SensorDataUploader {
    KSyncResult legacyUpload(boolean z, LegacySyncProgressCallback legacySyncProgressCallback);

    boolean upload(String str, DateTime dateTime, Map<KSensorEventType, KSensorEvent[]> map);
}
