package com.microsoft.band.service.task;

import com.microsoft.band.cloud.EphemerisUpdateInfo;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.service.CargoClientSession;
import com.microsoft.band.service.CargoServiceException;
/* loaded from: classes.dex */
public class EphemerisUpdateTask extends CargoSessionUpdateTask {
    private volatile EphemerisUpdateInfo mEphemerisUpdateInfo;

    public EphemerisUpdateTask() {
        this.mTAG = EphemerisUpdateTask.class.getSimpleName();
    }

    public void execute(CargoClientSession clientSession, EphemerisUpdateInfo ephemerisUpdateInfo, boolean isUpload) {
        if (ephemerisUpdateInfo == null) {
            throw new IllegalArgumentException("ephemerisUpdateInfo not specified");
        }
        this.mEphemerisUpdateInfo = ephemerisUpdateInfo;
        execute(clientSession, isUpload);
    }

    @Override // com.microsoft.band.service.task.CargoSessionUpdateTask
    protected BandServiceMessage.Response performUpgrade(CargoClientSession clientSession) {
        try {
            return clientSession.getDeviceProvider().upgradeEphemerisWithoutLogic(clientSession, this.mEphemerisUpdateInfo);
        } catch (CargoServiceException e) {
            return e.getResponse();
        }
    }

    @Override // com.microsoft.band.service.task.CargoSessionUpdateTask
    protected BandServiceMessage.Response performDownload(CargoClientSession clientSession) {
        return clientSession.getCloudProvider().downloadEphemerisFile(clientSession, this.mEphemerisUpdateInfo);
    }
}
