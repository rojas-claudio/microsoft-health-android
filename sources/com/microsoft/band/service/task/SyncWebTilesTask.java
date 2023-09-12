package com.microsoft.band.service.task;

import android.os.Bundle;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.device.StartStrip;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.CargoClientSession;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.band.service.device.DeviceServiceProvider;
import com.microsoft.band.tiles.pages.PageData;
import com.microsoft.band.webtiles.WebTile;
import com.microsoft.band.webtiles.WebTileManager;
import com.microsoft.band.webtiles.WebTilePageSyncInfo;
import com.microsoft.band.webtiles.WebTileRefreshResult;
import com.microsoft.band.webtiles.WebTileSyncResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public class SyncWebTilesTask extends CargoSessionTask<Void> {
    private boolean mForegroundSync;

    public SyncWebTilesTask() {
        this.mTAG = SyncWebTilesTask.class.getSimpleName();
    }

    public boolean isForgroundSync() {
        return this.mForegroundSync;
    }

    public void execute(CargoClientSession clientSession, boolean foregroundSync) {
        this.mForegroundSync = foregroundSync;
        super.execute(clientSession);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.band.service.task.CargoSessionTask
    public Void doWork() throws CargoServiceException {
        CargoClientSession clientSession = getSession();
        WebTileSyncResult result = new WebTileSyncResult();
        long syncStart = System.currentTimeMillis();
        if (clientSession != null && !clientSession.isTerminating()) {
            KDKLog.d(this.mTAG, isForgroundSync() ? "Performing Foreground Webtile Sync" : "Performing Background Webtile Sync");
            BandServiceMessage.Response syncResponse = syncWebTile(clientSession.getDeviceProvider(), CargoClientSession.getDirectoryPath(), result);
            result.setSyncDurationMillisTotal(System.currentTimeMillis() - syncStart);
            Bundle bundle = new Bundle();
            bundle.putParcelable(CargoConstants.EXTRA_DOWNLOAD_SYNC_RESULT, result);
            clientSession.sendServiceMessage(BandServiceMessage.SYNC_NOTIFICATION, BandServiceMessage.Response.SYNC_WEB_TILE_COMPLETED, syncResponse.getCode(), bundle);
        }
        KDKLog.i(this.mTAG, result.toString());
        return null;
    }

    private BandServiceMessage.Response syncWebTile(DeviceServiceProvider deviceProvider, String basePath, WebTileSyncResult result) {
        Bundle bundle = new Bundle();
        BandServiceMessage.Response syncResponse = deviceProvider.getInstalledAppListWithoutImage(bundle);
        if (syncResponse.isError()) {
            return syncResponse;
        }
        StartStrip strip = (StartStrip) bundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
        if (strip == null) {
            return BandServiceMessage.Response.DEVICE_DATA_ERROR;
        }
        List<UUID> tilesToRemove = new ArrayList<>();
        List<UUID> webtileIds = WebTileManager.getInstalledWebTileIds(basePath);
        for (CargoStrapp strapp : strip.getAppList()) {
            if (Arrays.equals(DeviceConstants.WEB_TILE_HASH, strapp.getHashedAppId())) {
                if (webtileIds.contains(strapp.getId())) {
                    try {
                        WebTile wt = WebTileManager.readWebTileFromPackagePath(basePath, strapp.getId());
                        long syncCheckTime = System.currentTimeMillis();
                        if (isForgroundSync() || wt.hasRefreshIntervalElapsed(syncCheckTime)) {
                            WebTileRefreshResult data = wt.refresh();
                            WebTilePageSyncInfo syncInfo = new WebTilePageSyncInfo(wt.getTileId());
                            BandServiceMessage.Response updatePageResponse = BandServiceMessage.Response.SERVICE_SYNC_FAILED_ERROR;
                            if (data.isClearPage()) {
                                updatePageResponse = deviceProvider.clearTile(wt.getTileId());
                                syncInfo.setClearPagesResult(updatePageResponse.getCode());
                            }
                            if (updatePageResponse == BandServiceMessage.Response.SERVICE_SYNC_FAILED_ERROR || !updatePageResponse.isError()) {
                                for (PageData pageData : data.getPageDatas()) {
                                    updatePageResponse = deviceProvider.sendPageToBand(wt.getTileId(), pageData, data.isSendAsMessage());
                                    syncInfo.setPageSyncResult(pageData.getPageId(), updatePageResponse.getCode());
                                }
                                if (!updatePageResponse.isError()) {
                                    wt.saveLastSync(syncCheckTime);
                                }
                                if (!updatePageResponse.isError() && data.getDialog() != null) {
                                    BandServiceMessage.Response updatePageResponse2 = deviceProvider.showDialogWithoutValidation(wt.getTileId().toString(), data.getDialog());
                                    syncInfo.setNotificationResult(updatePageResponse2.getCode());
                                }
                            }
                            result.setSyncResult(syncInfo);
                        }
                    } catch (Exception e) {
                        KDKLog.e(this.mTAG, e, "Exception caught when trying to read Webtile %s : %s", strapp.getId(), e.getMessage());
                        result.setFailedTiles(strapp.getId(), BandServiceMessage.Response.WEB_TILE_READ_ERROR.getCode());
                    }
                } else {
                    tilesToRemove.add(strapp.getId());
                }
            }
        }
        if (tilesToRemove.size() > 0) {
            syncResponse = deviceProvider.removeTiles(tilesToRemove);
        }
        return syncResponse;
    }
}
