package com.microsoft.band.service.task;

import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.service.CargoClientSession;
import com.microsoft.band.service.CargoServiceException;
/* loaded from: classes.dex */
public abstract class CargoSessionUpdateTask extends CargoSessionTask<BandServiceMessage.Response> {
    private volatile boolean mIsUpgrade;

    protected abstract BandServiceMessage.Response performDownload(CargoClientSession cargoClientSession);

    protected abstract BandServiceMessage.Response performUpgrade(CargoClientSession cargoClientSession);

    public void execute(CargoClientSession clientSession, boolean isUpload) {
        if (!isRunning()) {
            this.mIsUpgrade = isUpload;
            execute(clientSession);
        }
    }

    public boolean isUpgrade() {
        return this.mIsUpgrade;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.microsoft.band.service.task.CargoSessionTask
    public BandServiceMessage.Response doWork() throws CargoServiceException {
        CargoClientSession clientSession = getSession();
        BandServiceMessage.Response response = BandServiceMessage.Response.SERVICE_TERMINATED_ERROR;
        if (clientSession != null && !clientSession.isTerminating()) {
            if (isUpgrade()) {
                return performUpgrade(clientSession);
            }
            return performDownload(clientSession);
        }
        return response;
    }
}
