package com.microsoft.band.service.task;

import android.os.Bundle;
import android.os.Parcelable;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.client.SyncResult;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.device.DeviceProfileInfo;
import com.microsoft.band.device.command.DeviceProfileByteArrayGet;
import com.microsoft.band.device.command.DeviceProfileGet;
import com.microsoft.band.device.command.DeviceProfileSet;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.CargoClientSession;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.band.service.cloud.CloudDataResource;
import com.microsoft.band.service.cloud.CloudServiceProvider;
import com.microsoft.band.service.device.DeviceServiceProvider;
import com.microsoft.band.service.device.SensorLogDownload;
import com.microsoft.band.service.device.SensorLogMetadata;
import java.util.ArrayList;
import java.util.Date;
/* loaded from: classes.dex */
public class SyncDeviceToCloudTask extends CargoSessionTask<BandServiceMessage.Response> {
    private static final int DEVICE_CRASHDUMP_DOWNLOAD_WEIGHT_FULL = 1;
    private static final int DEVICE_CRASHDUMP_UPLOAD_WEIGHT_FULL = 1;
    private static final int DEVICE_TELEMETRY_DOWNLOAD_WEIGHT_FULL = 2;
    private static final int DEVICE_TELEMETRY_UPLOAD_WEIGHT_FULL = 2;
    private static final int EPHEMERIS_DOWNLOAD_WEIGHT_FULL = 3;
    private static final int EPHEMERIS_UPGRADE_WEIGHT_FULL = 3;
    private static final int LOG_UPLOAD_WEIGHT_FULL = 82;
    private static final int LOG_UPLOAD_WEIGHT_PARTIAL = 91;
    private static final int TIMEZONE_LIST_DOWNLOAD_WEIGHT_FULL = 2;
    private static final int TIMEZONE_LIST_UPGRADE_WEIGHT_FULL = 2;
    private static final int TIME_SYNC_PROCESSING_WEIGHT_FULL = 1;
    private static final int TIME_SYNC_PROCESSING_WEIGHT_PARTIAL = 1;
    private static final int USER_PROFILE_AND_BYTE_ARRAY_PROCESSING_WEIGHT_FULL = 1;
    private static final int USER_PROFILE_AND_BYTE_ARRAY_PROCESSING_WEIGHT_PARTIAL = 2;
    private boolean mIsFullSync;

    public SyncDeviceToCloudTask() {
        this.mTAG = SyncDeviceToCloudTask.class.getSimpleName();
    }

    public boolean isFullSync() {
        return this.mIsFullSync;
    }

    public void execute(CargoClientSession clientSession, boolean fullSync) {
        this.mIsFullSync = fullSync;
        super.execute(clientSession);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.microsoft.band.service.task.CargoSessionTask
    public BandServiceMessage.Response doWork() {
        BandServiceMessage.Response syncResponse = BandServiceMessage.Response.SERVICE_SYNC_FAILED_ERROR;
        try {
            CargoClientSession clientSession = getSession();
            byte[] fByteArray = null;
            DeviceProfileInfo dpi = null;
            UserProfileInfo upi = null;
            boolean crashdumpFilesRetrieved = false;
            SyncResult result = new SyncResult();
            ArrayList<Parcelable> uploadedCloudDataResources = new ArrayList<>();
            if (clientSession != null && !clientSession.isTerminating()) {
                clientSession.sendSyncProgressMessage(0);
                KDKLog.d(this.mTAG, isFullSync() ? "Performing Full Sync" : "Performing Fast Sync");
                DeviceServiceProvider deviceProvider = null;
                try {
                    deviceProvider = clientSession.getDeviceProvider();
                } catch (CargoServiceException e) {
                    KDKLog.e(this.mTAG, e.getMessage());
                }
                CloudServiceProvider cloudProvider = clientSession.getCloudProvider();
                if (deviceProvider != null) {
                    if (isRunning()) {
                        KDKLog.d(this.mTAG, "Sync Device UTC Time Start");
                        BandServiceMessage.Response timeSyncResponse = deviceProvider.syncDeviceTime(clientSession, 0);
                        result.setSyncDeviceTimeResponseCode(timeSyncResponse.getCode());
                        KDKLog.d(this.mTAG, "Sync Device UTC Time Completed With Response " + timeSyncResponse);
                        if (timeSyncResponse.isError()) {
                            KDKLog.e(this.mTAG, "syncDeviceToCloud: Cannot sync time with " + timeSyncResponse.name());
                        }
                    }
                    if (isRunning()) {
                        KDKLog.d(this.mTAG, "Sync Device Timezone Start");
                        BandServiceMessage.Response timezoneSyncResponse = deviceProvider.syncDeviceTimeZone(clientSession);
                        result.setSyncDeviceTimeZoneResponseCode(timezoneSyncResponse.getCode());
                        KDKLog.d(this.mTAG, "Sync Device Timezone Completed With Response " + timezoneSyncResponse);
                        if (timezoneSyncResponse.isError()) {
                            KDKLog.e(this.mTAG, "syncDeviceToCloud: Cannot sync timezone with " + timezoneSyncResponse.name());
                            if (timezoneSyncResponse == BandServiceMessage.Response.DEVICE_IO_ERROR) {
                                KDKLog.e(this.mTAG, "syncDeviceToCloud: TimeSync is disabled.");
                            }
                        }
                    }
                }
                if (isFullSync()) {
                }
                clientSession.sendSyncProgressMessage(0 + 1);
                if (deviceProvider != null) {
                    if (isRunning()) {
                        KDKLog.d(this.mTAG, "Get Firmware Byte Array Start");
                        DeviceProfileByteArrayGet cmdBAG = new DeviceProfileByteArrayGet();
                        BandServiceMessage.Response byteArrayGetResponse = deviceProvider.processCommand(cmdBAG);
                        result.setPrivateByteArrayRetrievalResponseCode(byteArrayGetResponse.getCode());
                        if (!byteArrayGetResponse.isError()) {
                            fByteArray = cmdBAG.getProfileByteArray().getOpaqueByteArray();
                        }
                        KDKLog.d(this.mTAG, "Get Firmware Byte Array Completed With Response " + byteArrayGetResponse);
                    }
                    if (isRunning()) {
                        KDKLog.d(this.mTAG, "Get Device Profile Start");
                        int hardwareVersion = deviceProvider.getHardwareVersion();
                        DeviceProfileGet cmdDP = new DeviceProfileGet(hardwareVersion);
                        BandServiceMessage.Response deviceProfileGetResponse = deviceProvider.processCommand(cmdDP);
                        result.setDeviceProfileRetrievalResponseCode(deviceProfileGetResponse.getCode());
                        if (!deviceProfileGetResponse.isError() && hardwareVersion != 0) {
                            dpi = cmdDP.getDeviceProfileInfo();
                        }
                        KDKLog.d(this.mTAG, "Get Device Profile Completed With Response " + deviceProfileGetResponse);
                    }
                }
                if (cloudProvider != null && isRunning()) {
                    KDKLog.d(this.mTAG, "Get Cloud Profile Start");
                    BandServiceMessage.Response cloudProfileGetResponse = BandServiceMessage.Response.SUCCESS;
                    try {
                        upi = cloudProvider.getCloudProfile();
                    } catch (CargoServiceException e2) {
                        cloudProfileGetResponse = e2.getResponse();
                        KDKLog.w(this.mTAG, "CargoServiceException caught while trying to get profile from the cloud during sync", e2);
                    }
                    result.setCloudProfileRetrievalResponseCode(cloudProfileGetResponse.getCode());
                    KDKLog.d(this.mTAG, "Get Cloud Profile Completed With Response " + cloudProfileGetResponse);
                }
                if (dpi != null && upi != null) {
                    Date dpiLastSync = dpi.getTimeStampUTC();
                    Date upiLastSync = upi.getLastKDKSyncUpdateOn();
                    if (upiLastSync == null || dpiLastSync.getTime() > upiLastSync.getTime()) {
                        KDKLog.d(this.mTAG, "Upload Firmware Byte Array Will Update Cloud Profile As Well");
                        upi.updateUsingDeviceUserProfile(dpi);
                        result.setUserProfileSyncValue((byte) 1);
                    } else if (dpiLastSync.getTime() == upiLastSync.getTime()) {
                        KDKLog.d(this.mTAG, "Profile Sync Not Needed");
                        upi = null;
                        result.setUserProfileSyncValue((byte) 2);
                    } else {
                        dpi.updateUsingCloudUserProfile(upi);
                        if (deviceProvider != null && isRunning()) {
                            KDKLog.d(this.mTAG, "Update Device Profile Start");
                            DeviceProfileSet cmdDPS = new DeviceProfileSet(dpi);
                            BandServiceMessage.Response updateDeviceProfileResponse = deviceProvider.processCommand(cmdDPS);
                            if (!updateDeviceProfileResponse.isError()) {
                                result.setUserProfileSyncValue((byte) 3);
                            } else {
                                result.setUserProfileSyncValue((byte) 4);
                            }
                            KDKLog.d(this.mTAG, "Update Device Profile Completed With Response " + updateDeviceProfileResponse);
                        }
                        upi = null;
                    }
                }
                if (cloudProvider != null && isRunning() && fByteArray != null) {
                    KDKLog.d(this.mTAG, "Upload Fimrware Byte Array Start");
                    if (upi == null) {
                        upi = new UserProfileInfo();
                    }
                    upi.setFirmwareByteArray(fByteArray);
                    BandServiceMessage.Response uploadFirmwareByteArrayResponse = cloudProvider.saveUserProfileFromSync(upi);
                    result.setPrivateByteArraySavingResponseCode(uploadFirmwareByteArrayResponse.getCode());
                    KDKLog.d(this.mTAG, "Upload Fimrware Byte Array Completed With Response " + uploadFirmwareByteArrayResponse);
                }
                int progress = (isFullSync() ? 1 : 2) + 1;
                clientSession.sendSyncProgressMessage(progress);
                if (cloudProvider != null && isRunning()) {
                    KDKLog.d(this.mTAG, "Download Ephemeris Start");
                    BandServiceMessage.Response downloadEphemerisResponse = cloudProvider.downloadEphemeris(clientSession);
                    result.setDownloadEphemerisResponseCode(downloadEphemerisResponse.getCode());
                    KDKLog.d(this.mTAG, "Download Ephemeris Completed With Response " + downloadEphemerisResponse);
                }
                int progress2 = progress + 3;
                clientSession.sendSyncProgressMessage(progress2);
                if (deviceProvider != null && isRunning()) {
                    KDKLog.d(this.mTAG, "Upgrade Ephemeris Start");
                    BandServiceMessage.Response upgradeEphemerisResponse = deviceProvider.upgradeEphemerisWithLogic(clientSession, null);
                    result.setUpgradeEphemerisResponseCode(upgradeEphemerisResponse.getCode());
                    KDKLog.d(this.mTAG, "Upgrade Ephemeris Completed With Response " + upgradeEphemerisResponse);
                }
                int progress3 = progress2 + 3;
                clientSession.sendSyncProgressMessage(progress3);
                if (isFullSync()) {
                    if (cloudProvider != null && dpi != null && isRunning()) {
                        KDKLog.d(this.mTAG, "Download Timezone List Start");
                        BandServiceMessage.Response downloadTimezoneResponse = cloudProvider.downloadTimeZoneSettings(clientSession, dpi.getLocaleName());
                        result.setDownloadTimeZoneFileResponseCode(downloadTimezoneResponse.getCode());
                        KDKLog.d(this.mTAG, "Download Timezone List Completed With Response " + downloadTimezoneResponse);
                    }
                    int progress4 = progress3 + 2;
                    clientSession.sendSyncProgressMessage(progress4);
                    if (deviceProvider != null && isRunning()) {
                        KDKLog.d(this.mTAG, "Upgrade Timezone List Start");
                        BandServiceMessage.Response upgradeTimezoneResponse = deviceProvider.upgradeTimeZoneSettingsWithLogic(clientSession, null);
                        result.setUpgradeTimeZoneFileResponseCode(upgradeTimezoneResponse.getCode());
                        KDKLog.d(this.mTAG, "Upgrade Timezone List Completed With Response " + upgradeTimezoneResponse);
                    }
                    int progress5 = progress4 + 2;
                    clientSession.sendSyncProgressMessage(progress5);
                    if (deviceProvider != null && isRunning()) {
                        KDKLog.d(this.mTAG, "Get CrashDump Files Start");
                        BandServiceMessage.Response getCrashdumpFileResponse = deviceProvider.getCrashDumpFilesFromDevice();
                        if (getCrashdumpFileResponse == BandServiceMessage.Response.SUCCESS) {
                            crashdumpFilesRetrieved = true;
                        }
                        result.setCrashdumpFileRetrievalResponseCode(getCrashdumpFileResponse.getCode());
                        KDKLog.d(this.mTAG, "Get CrashDump Files Completed With Response " + getCrashdumpFileResponse);
                    }
                    int progress6 = progress5 + 1;
                    clientSession.sendSyncProgressMessage(progress6);
                    if (cloudProvider != null && isRunning()) {
                        KDKLog.d(this.mTAG, "Send CrashDump Files To Cloud Start");
                        BandServiceMessage.Response sendCrashdumpResponse = cloudProvider.sendCrashDumpFilesToCloud(clientSession);
                        result.setCrashdumpFileSendingResponseCode(sendCrashdumpResponse.getCode());
                        KDKLog.d(this.mTAG, "Send CrashDump Files To Cloud Completed With Response " + sendCrashdumpResponse);
                    }
                    int progress7 = progress6 + 1;
                    clientSession.sendSyncProgressMessage(progress7);
                    if (deviceProvider != null && isRunning()) {
                        KDKLog.d(this.mTAG, "Get Telemetry File Start");
                        BandServiceMessage.Response getTelemetryFileResponse = deviceProvider.getTelemetryFilesFromDevice(clientSession, crashdumpFilesRetrieved);
                        result.setTelemetryFileRetrievalResponseCode(getTelemetryFileResponse.getCode());
                        KDKLog.d(this.mTAG, "Get Telemetry File Completed With Response " + getTelemetryFileResponse);
                    }
                    int progress8 = progress7 + 2;
                    clientSession.sendSyncProgressMessage(progress8);
                    if (cloudProvider != null && isRunning()) {
                        KDKLog.d(this.mTAG, "Send Telemetry File To Cloud Start");
                        BandServiceMessage.Response sendTelemetryFileResponse = cloudProvider.sendTelemetryFilesToCloud(clientSession);
                        result.setTelemetryFileSendingResponseCode(sendTelemetryFileResponse.getCode());
                        KDKLog.d(this.mTAG, "Send Telemetry File To Cloud Completed With Response " + sendTelemetryFileResponse);
                    }
                    progress3 = progress8 + 2;
                    clientSession.sendSyncProgressMessage(progress3);
                }
                if (deviceProvider != null && cloudProvider != null && isRunning()) {
                    syncResponse = SyncSensorLog(clientSession, isFullSync(), progress3, uploadedCloudDataResources, result, deviceProvider, cloudProvider, true, this.mTAG);
                }
                clientSession.sendSyncProgressMessage(progress3 + (isFullSync() ? LOG_UPLOAD_WEIGHT_FULL : LOG_UPLOAD_WEIGHT_PARTIAL));
                Bundle bundle = new Bundle();
                bundle.putParcelable(CargoConstants.EXTRA_DOWNLOAD_SYNC_RESULT, result);
                bundle.putParcelableArrayList(CargoConstants.EXTRA_CLOUD_DATA, uploadedCloudDataResources);
                KDKLog.d(this.mTAG, "Send Sync Download notification with resources count=%s", Integer.valueOf(uploadedCloudDataResources.size()));
                clientSession.sendServiceMessage(BandServiceMessage.SYNC_NOTIFICATION, BandServiceMessage.Response.SYNC_DEVICE_TO_CLOUD_COMPLETED, syncResponse.getCode(), bundle);
            }
        } catch (Exception e3) {
            KDKLog.e(this.mTAG, "Exception thrown during sync", e3);
        }
        return syncResponse;
    }

    public static BandServiceMessage.Response SyncSensorLog(CargoClientSession clientSession, boolean fullSync, int progress, ArrayList<Parcelable> uploadedCloudDataResources, SyncResult result, DeviceServiceProvider deviceProvider, CloudServiceProvider cloudProvider, boolean usedInSync, String logTag) {
        long bytesTransferred = 0;
        long deviceSyncTime = 0;
        long cloudSyncTime = 0;
        int chuncksSentToCloud = 0;
        int chuncksDeletedFromDevice = 0;
        SensorLogMetadata meta = new SensorLogMetadata();
        BandServiceMessage.Response response = BandServiceMessage.Response.OPERATION_NOT_REQUIRED;
        BandServiceMessage.Response logSyncResponse = deviceProvider.flushLogger();
        KDKLog.d(logTag, "FlushLogger Completed With Response: " + logSyncResponse);
        if (!logSyncResponse.isError()) {
            logSyncResponse = deviceProvider.getSensorlogChunkRangeMetadata(meta, DeviceConstants.LOG_MAX_CHUNK);
            if (!logSyncResponse.isError()) {
                long totalNumberOfBytesToTransfer = meta.getByteCount();
                boolean moreChunksToTransfer = totalNumberOfBytesToTransfer > 0;
                while (moreChunksToTransfer) {
                    long dSyncStart = System.currentTimeMillis();
                    SensorLogDownload logDownload = new SensorLogDownload();
                    logSyncResponse = deviceProvider.getSensorlogChunkRangeData(logDownload, 128);
                    deviceSyncTime += System.currentTimeMillis() - dSyncStart;
                    KDKLog.d(logTag, "Download Sensor Log Completed With Response: " + logSyncResponse);
                    if (!logSyncResponse.isError()) {
                        KDKLog.d(logTag, "Download Sensor Log: " + logDownload.getMeta().toString());
                        long cSyncStart = System.currentTimeMillis();
                        CloudDataResource cdr = new CloudDataResource();
                        logSyncResponse = cloudProvider.uploadSensorLog(logDownload, clientSession.getUploadMetadata(), cdr);
                        cloudSyncTime += System.currentTimeMillis() - cSyncStart;
                        KDKLog.d(logTag, "Upload Sensor Log Completed With Response " + logSyncResponse);
                        bytesTransferred += logDownload.getMeta().getByteCount();
                        chuncksSentToCloud += logDownload.getMeta().getChunkCount();
                        if (!logSyncResponse.isError()) {
                            if (usedInSync && logSyncResponse == BandServiceMessage.Response.PENDING) {
                                KDKLog.d(logTag, "Sensor Log Pending Added : %s", logSyncResponse);
                                uploadedCloudDataResources.add(cdr);
                            }
                            BandServiceMessage.Response logSyncResponse2 = deviceProvider.deleteSensorlogChunkRangeData(logDownload.getMeta());
                            if (!logSyncResponse2.isError()) {
                                chuncksDeletedFromDevice += logDownload.getMeta().getChunkCount();
                            }
                            KDKLog.d(logTag, "Delete Sensor Log With Response: " + logSyncResponse2);
                            if (usedInSync) {
                                clientSession.sendSyncProgressMessage(Math.min(100, ((int) ((fullSync ? LOG_UPLOAD_WEIGHT_FULL : LOG_UPLOAD_WEIGHT_PARTIAL) * (((float) bytesTransferred) / ((float) totalNumberOfBytesToTransfer)))) + progress));
                            }
                            logSyncResponse = deviceProvider.getSensorlogChunkRangeMetadata(meta, DeviceConstants.LOG_MAX_CHUNK);
                            if (logSyncResponse.isError()) {
                                break;
                            }
                            moreChunksToTransfer = meta.getByteCount() > 0;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        BandServiceMessage.Response syncResponse = logSyncResponse;
        result.setLogSyncResponseCode(logSyncResponse.getCode());
        result.setBytesSentToCloud(bytesTransferred);
        result.setDownloadDurationMillisTotal(deviceSyncTime);
        result.setUploadDurationMillisTotal(cloudSyncTime);
        result.setChuncksSentToCloud(chuncksSentToCloud);
        result.setChuncksDeletedFromDevice(chuncksDeletedFromDevice);
        return syncResponse;
    }
}
