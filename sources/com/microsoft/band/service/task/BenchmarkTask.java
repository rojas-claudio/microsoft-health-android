package com.microsoft.band.service.task;

import android.os.Bundle;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.client.SyncResult;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.CargoClientSession;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.band.service.device.DeviceServiceProvider;
import com.microsoft.band.service.device.SensorLogDownload;
import com.microsoft.band.service.device.SensorLogMetadata;
/* loaded from: classes.dex */
public class BenchmarkTask extends CargoSessionTask<BandServiceMessage.Response> {
    private int mMaxTransferChunks;

    public BenchmarkTask() {
        this.mTAG = BenchmarkTask.class.getSimpleName();
    }

    public void execute(CargoClientSession clientSession, int maxTransferChunks) {
        this.mMaxTransferChunks = maxTransferChunks;
        super.execute(clientSession);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.microsoft.band.service.task.CargoSessionTask
    public BandServiceMessage.Response doWork() throws CargoServiceException {
        SyncResult result = new SyncResult();
        BandServiceMessage.Response syncResponse = BandServiceMessage.Response.SERVICE_SYNC_FAILED_ERROR;
        long bytesTransferred = 0;
        long deviceSyncTime = 0;
        int chuncksDeletedFromDevice = 0;
        SensorLogMetadata meta = new SensorLogMetadata();
        CargoClientSession clientSession = getSession();
        if (clientSession != null && !clientSession.isTerminating()) {
            DeviceServiceProvider deviceProvider = clientSession.getDeviceProvider();
            BandServiceMessage.Response response = BandServiceMessage.Response.OPERATION_NOT_REQUIRED;
            BandServiceMessage.Response logSyncResponse = deviceProvider.flushLogger();
            KDKLog.d(this.mTAG, "FlushLogger Completed With Response: " + logSyncResponse);
            if (!logSyncResponse.isError()) {
                logSyncResponse = deviceProvider.getSensorlogChunkRangeMetadata(meta, DeviceConstants.LOG_MAX_CHUNK);
                if (!logSyncResponse.isError()) {
                    long totalNumberOfBytesToTransfer = meta.getByteCount();
                    boolean moreChunksToTransfer = totalNumberOfBytesToTransfer > 0;
                    while (moreChunksToTransfer) {
                        SensorLogDownload logDownload = new SensorLogDownload();
                        long dSyncStart = System.currentTimeMillis();
                        logSyncResponse = deviceProvider.getSensorlogChunkRangeData(logDownload, this.mMaxTransferChunks);
                        deviceSyncTime += System.currentTimeMillis() - dSyncStart;
                        KDKLog.d(this.mTAG, "Download Sensor Log Completed With Response: " + logSyncResponse);
                        if (!logSyncResponse.isError()) {
                            bytesTransferred += logDownload.getMeta().getByteCount();
                            BandServiceMessage.Response logSyncResponse2 = deviceProvider.deleteSensorlogChunkRangeData(logDownload.getMeta());
                            if (!logSyncResponse2.isError()) {
                                chuncksDeletedFromDevice += logDownload.getMeta().getChunkCount();
                            }
                            KDKLog.d(this.mTAG, "Delete Sensor Log With Response: " + logSyncResponse2);
                            logSyncResponse = deviceProvider.getSensorlogChunkRangeMetadata(meta, DeviceConstants.LOG_MAX_CHUNK);
                            if (logSyncResponse.isError()) {
                                break;
                            }
                            moreChunksToTransfer = meta.getByteCount() > 0;
                        } else {
                            break;
                        }
                    }
                }
            }
            BandServiceMessage.Response syncResponse2 = logSyncResponse;
            result.setLogSyncResponseCode(logSyncResponse.getCode());
            result.setBytesSentToCloud(bytesTransferred);
            result.setDownloadDurationMillisTotal(deviceSyncTime);
            result.setChuncksDeletedFromDevice(chuncksDeletedFromDevice);
            Bundle bundle = new Bundle();
            bundle.putParcelable(CargoConstants.EXTRA_DOWNLOAD_SYNC_RESULT, result);
            clientSession.sendServiceMessage(BandServiceMessage.SYNC_NOTIFICATION, BandServiceMessage.Response.SYNC_DEVICE_TO_CLOUD_COMPLETED, syncResponse2.getCode(), bundle);
            return syncResponse2;
        }
        return syncResponse;
    }
}
