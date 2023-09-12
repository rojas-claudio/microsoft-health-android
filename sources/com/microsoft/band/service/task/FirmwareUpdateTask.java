package com.microsoft.band.service.task;

import android.os.Bundle;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.client.SyncResult;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.service.CargoClientSession;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.band.service.cloud.CloudServiceProvider;
import com.microsoft.band.service.device.DeviceServiceProvider;
/* loaded from: classes.dex */
public class FirmwareUpdateTask extends CargoSessionUpdateTask {
    public static final int UPGRADE_FIRMWARE_VALUE_AFTER_BOOT_TO_UPDATE_MODE = 15;
    public static final int UPGRADE_FIRMWARE_VALUE_AFTER_DATA_READ = 10;
    public static final int UPGRADE_FIRMWARE_VALUE_AFTER_SENSOR_LOGS_HANDLED = 8;
    public static final int UPGRADE_FIRMWARE_VALUE_AFTER_UPDATE_SENT = 25;
    private volatile CargoFirmwareUpdateInfo mFirmwareUpdateInfo;

    public FirmwareUpdateTask() {
        this.mTAG = FirmwareUpdateTask.class.getSimpleName();
    }

    public void execute(CargoClientSession clientSession, CargoFirmwareUpdateInfo firmwareUpdateInfo, boolean isUpload) {
        if (firmwareUpdateInfo == null) {
            throw new IllegalArgumentException("firmwareUpdateInfo not specified");
        }
        this.mFirmwareUpdateInfo = firmwareUpdateInfo;
        execute(clientSession, isUpload);
    }

    @Override // com.microsoft.band.service.task.CargoSessionUpdateTask
    protected BandServiceMessage.Response performUpgrade(CargoClientSession clientSession) {
        BandServiceMessage.Response response = BandServiceMessage.Response.SUCCESS;
        SyncResult result = new SyncResult();
        Bundle bundle = clientSession.getToken().toBundle();
        bundle.putParcelable(InternalBandConstants.EXTRA_DEVICE_INFO, clientSession.getDeviceInfo());
        bundle.putParcelable(CargoConstants.EXTRA_CLOUD_DATA, this.mFirmwareUpdateInfo);
        clientSession.sendServiceMessage(BandServiceMessage.UPGRADE_NOTIFICATION, BandServiceMessage.Response.UPGRADE_FIRMWARE_STARTED, 0, bundle);
        CloudServiceProvider cloudProvider = clientSession.getCloudProvider();
        try {
            DeviceServiceProvider deviceProvider = clientSession.getDeviceProvider();
            if (isRunning() && deviceProvider.isOobeCompleted() && deviceProvider != null && cloudProvider != null && isRunning()) {
                response = SyncDeviceToCloudTask.SyncSensorLog(clientSession, false, 0, null, result, deviceProvider, cloudProvider, false, this.mTAG);
            }
            if (result.wasSyncSuccess()) {
                response = deviceProvider.upgradeFirmware(clientSession, this.mFirmwareUpdateInfo);
            }
        } catch (CargoServiceException e) {
            response = e.getResponse();
        }
        bundle.putParcelable(CargoConstants.EXTRA_DOWNLOAD_SYNC_RESULT, result);
        clientSession.sendServiceMessage(BandServiceMessage.UPGRADE_NOTIFICATION, BandServiceMessage.Response.UPGRADE_FIRMWARE_COMPLETED, response.getCode(), bundle);
        return response;
    }

    @Override // com.microsoft.band.service.task.CargoSessionUpdateTask
    protected BandServiceMessage.Response performDownload(CargoClientSession clientSession) {
        Bundle bundle = clientSession.getToken().toBundle();
        bundle.putParcelable(CargoConstants.EXTRA_CLOUD_DATA, this.mFirmwareUpdateInfo);
        clientSession.sendServiceMessage(BandServiceMessage.DOWNLOAD_NOTIFICATION, BandServiceMessage.Response.DOWNLOAD_FIRMWARE_UPDATE_STARTED, 0, bundle);
        BandServiceMessage.Response response = clientSession.getCloudProvider().downloadFirmware(clientSession, this.mFirmwareUpdateInfo);
        clientSession.sendServiceMessage(BandServiceMessage.DOWNLOAD_NOTIFICATION, BandServiceMessage.Response.DOWNLOAD_FIRMWARE_UPDATE_COMPLETED, response.getCode(), bundle);
        return response;
    }
}
