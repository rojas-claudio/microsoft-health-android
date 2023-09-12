package com.microsoft.band.service.task;

import com.microsoft.band.cloud.TimeZoneSettingsUpdateInfo;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.service.CargoClientSession;
import com.microsoft.band.service.CargoServiceException;
/* loaded from: classes.dex */
public class TimeZoneSettingsUpdateTask extends CargoSessionUpdateTask {
    private volatile TimeZoneSettingsUpdateInfo mTimeZoneSettingsUpdateInfo;

    public TimeZoneSettingsUpdateTask() {
        this.mTAG = TimeZoneSettingsUpdateTask.class.getSimpleName();
    }

    public void execute(CargoClientSession clientSession, TimeZoneSettingsUpdateInfo timeZoneSettingsUpdateInfo, boolean isUpload) {
        if (timeZoneSettingsUpdateInfo == null) {
            throw new IllegalArgumentException("timeZoneSettingsUpdateInfo not specified");
        }
        this.mTimeZoneSettingsUpdateInfo = timeZoneSettingsUpdateInfo;
        execute(clientSession, isUpload);
    }

    @Override // com.microsoft.band.service.task.CargoSessionUpdateTask
    protected BandServiceMessage.Response performUpgrade(CargoClientSession clientSession) {
        try {
            return clientSession.getDeviceProvider().upgradeTimeZoneSettingsWithoutLogic(clientSession, this.mTimeZoneSettingsUpdateInfo);
        } catch (CargoServiceException e) {
            return e.getResponse();
        }
    }

    @Override // com.microsoft.band.service.task.CargoSessionUpdateTask
    protected BandServiceMessage.Response performDownload(CargoClientSession clientSession) {
        return clientSession.getCloudProvider().downloadTimeZoneSettingsFile(clientSession, this.mTimeZoneSettingsUpdateInfo);
    }
}
