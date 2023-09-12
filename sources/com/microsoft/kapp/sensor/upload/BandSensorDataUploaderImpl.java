package com.microsoft.kapp.sensor.upload;

import android.content.Context;
import com.microsoft.applicationinsights.contracts.EventData;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.client.SyncResult;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.service.cloud.CloudDataResource;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.CargoExtensions;
import com.microsoft.kapp.SyncProgressListener;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.multidevice.KSyncResult;
import com.microsoft.kapp.multidevice.LegacySyncProgressCallback;
import com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager;
import com.microsoft.kapp.sensor.models.KSensorEvent;
import com.microsoft.kapp.sensor.models.KSensorEventType;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.LogScenarioTags;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class BandSensorDataUploaderImpl implements BandSensorDataUploader, SyncProgressListener {
    private static final String TAG = BandSensorDataUploaderImpl.class.getSimpleName();
    private CargoConnection mCargoConnection;
    private Context mContext;
    private LegacySyncProgressCallback mProgressCallback;
    private SettingsProvider mSettingsProvider;
    private SingleDeviceEnforcementManager mSingleDeviceEnforcementManager;

    public BandSensorDataUploaderImpl(Context context, CargoConnection cargoConnection, SingleDeviceEnforcementManager singleDeviceEnforcementManager, SettingsProvider settingsProvider) {
        this.mContext = context;
        this.mCargoConnection = cargoConnection;
        this.mSingleDeviceEnforcementManager = singleDeviceEnforcementManager;
        this.mSettingsProvider = settingsProvider;
    }

    @Override // com.microsoft.kapp.sensor.upload.SensorDataUploader
    public boolean upload(String deviceId, DateTime startTime, Map<KSensorEventType, KSensorEvent[]> events) {
        return false;
    }

    @Override // com.microsoft.kapp.sensor.upload.SensorDataUploader
    public KSyncResult legacyUpload(boolean isForegroundSync, LegacySyncProgressCallback progressCallback) {
        this.mProgressCallback = progressCallback;
        new KSyncResult(KSyncResult.SyncResultCode.SUCCESS);
        EventData syncTimedEvent = Telemetry.startTimedEvent(TelemetryConstants.TimedEvents.Sync.EVENT_NAME);
        KSyncResult result = checkSDE();
        if (result.isSuccess()) {
            SyncResult cloudSyncResult = null;
            try {
                onSyncProgress(0);
                Object[] objArr = new Object[1];
                objArr[0] = Boolean.valueOf(!isForegroundSync);
                KLog.d(LogScenarioTags.Sync, "SYNC: calling KDK. isBackground=[%b]", objArr);
                CargoConnection.SynchronizeDeviceToCloudResult synchronizeDeviceToCloudResult = this.mCargoConnection.synchronizeDeviceToCloud(isForegroundSync, this);
                cloudSyncResult = synchronizeDeviceToCloudResult.getSyncResult();
                if (cloudSyncResult != null) {
                    if (cloudSyncResult.wasSyncSuccess()) {
                        result.setStatus(KSyncResult.SyncResultCode.SUCCESS);
                    } else {
                        BandServiceMessage.Response error = synchronizeDeviceToCloudResult.getError();
                        result = CommonUtils.getSyncResultFromKDKSyncResult(error);
                    }
                    if (result.isSuccess()) {
                        KLog.d(LogScenarioTags.Sync, "BAND SYNC: successful.");
                        List<CloudDataResource> cloudDataList = synchronizeDeviceToCloudResult.getCloudDataList();
                        onSyncProgress(90);
                        KSyncResult.CloudErrorState waitForProcessingCloudErrorState = waitForCloudProcessing(cloudDataList);
                        if (waitForProcessingCloudErrorState != KSyncResult.CloudErrorState.SUCCESS) {
                            result.setStatus(KSyncResult.SyncResultCode.CLOUD_ERROR);
                            result.setCloudError(waitForProcessingCloudErrorState);
                        }
                    }
                } else {
                    result.setStatus(KSyncResult.SyncResultCode.UNKNOWN_ERROR);
                    KLog.d(LogScenarioTags.Sync, "BAND SYNC: mCargoConnection.synchronizeDeviceToCloud return null SyncResult.");
                    KLog.d(TAG, "BAND mCargoConnection.synchronizeDeviceToCloud return null SyncResult.");
                }
            } catch (CargoException ex) {
                result = CommonUtils.getSyncResultFromKDKSyncResult(ex.getResponse());
                KLog.d(TAG, "Exception encountered while syncing device to cloud.", ex);
            } catch (Exception ex2) {
                result.setStatus(KSyncResult.SyncResultCode.UNKNOWN_ERROR);
                KLog.d(TAG, "Exception encountered while syncing device to cloud.", ex2);
            }
            onSyncProgress(100);
            logTelemetryEvent(syncTimedEvent, result, cloudSyncResult);
            return result;
        }
        return result;
    }

    private void logTelemetryEvent(EventData syncTimedEvent, KSyncResult result, SyncResult syncResult) {
        syncTimedEvent.getProperties().put("Status", result.isSuccess() ? "Success" : "Failure");
        if (syncResult != null) {
            double numOfBytesSentToCloud = Double.valueOf(syncResult.getBytesSentToCloud()).doubleValue();
            syncTimedEvent.getMeasurements().put(TelemetryConstants.TimedEvents.Sync.Dimensions.BYTESUPLOADED, Double.valueOf(numOfBytesSentToCloud));
        }
        Telemetry.endTimedEvent(syncTimedEvent);
    }

    private KSyncResult checkSDE() {
        KSyncResult result = new KSyncResult(KSyncResult.SyncResultCode.SUCCESS);
        try {
            this.mCargoConnection.checkSingleDeviceEnforcement();
        } catch (CargoExtensions.SingleDeviceCheckFailedException e) {
            KLog.w(LogScenarioTags.Sync, "SYNC: SDE error", e);
            result.setStatus(KSyncResult.SyncResultCode.SDE_ERROR);
            result.setSDEError(e.getDeviceErrorState());
        } catch (CargoException e2) {
            KLog.w(LogScenarioTags.Sync, "SYNC: CargoException during SDE check", e2);
            if (e2.getResponse().getCategory() == 4) {
                result.setStatus(KSyncResult.SyncResultCode.CLOUD_ERROR);
                result.setCloudError(KSyncResult.CloudErrorState.UNKNOWN);
            } else {
                result.setStatus(KSyncResult.SyncResultCode.DEVICE_ERROR);
                result.setDeviceErrorCode(KSyncResult.DeviceErrorCode.UNKNOWN);
            }
        } catch (Exception e3) {
            KLog.e(LogScenarioTags.Sync, "SYNC: generic exception during SDE check", e3);
            result.setStatus(KSyncResult.SyncResultCode.DEVICE_ERROR);
        }
        return result;
    }

    @Override // com.microsoft.kapp.SyncProgressListener
    public void onSyncProgress(int progress) {
        if (this.mProgressCallback != null) {
            this.mProgressCallback.onSyncProgress(((int) (progress * 0.5d)) + 25);
        }
    }

    private KSyncResult.CloudErrorState waitForCloudProcessing(List<CloudDataResource> cloudDataList) {
        KSyncResult.CloudErrorState cloudErrorState = KSyncResult.CloudErrorState.SUCCESS;
        if (cloudDataList != null && cloudDataList.size() > 0) {
            EventData timedEvent = Telemetry.startTimedEvent(TelemetryConstants.TimedEvents.SyncWaitForCloudProcessing.EVENT_NAME);
            try {
                this.mCargoConnection.waitForCloudProcessingToComplete(cloudDataList);
                timedEvent.getProperties().put("Status", "Success");
            } catch (Exception ex) {
                KLog.d(TAG, "Exception encountered while waiting for cloud to process data after sync.", ex);
                KLog.e(LogScenarioTags.Sync, "SYNC: Exception encountered while waiting for cloud to process data after sync.", ex);
                cloudErrorState = KSyncResult.CloudErrorState.CLOUD_PROCESSING_FAILURE;
                timedEvent.getProperties().put("Status", "Failure");
            }
            Telemetry.endTimedEvent(timedEvent);
        }
        return cloudErrorState;
    }
}
