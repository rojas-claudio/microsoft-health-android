package com.microsoft.band.webtiles;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public class WebTileSyncResult implements Parcelable {
    public static final Parcelable.Creator<WebTileSyncResult> CREATOR = new Parcelable.Creator<WebTileSyncResult>() { // from class: com.microsoft.band.webtiles.WebTileSyncResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileSyncResult createFromParcel(Parcel in) {
            return new WebTileSyncResult(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileSyncResult[] newArray(int size) {
            return new WebTileSyncResult[size];
        }
    };
    private Map<UUID, Integer> mFailedTiles;
    private long mSyncDurationMillisTotal;
    private List<WebTilePageSyncInfo> mSyncResult;

    public WebTileSyncResult() {
        this.mSyncDurationMillisTotal = 0L;
        this.mSyncResult = new ArrayList();
        this.mFailedTiles = new HashMap();
    }

    public List<WebTilePageSyncInfo> getSyncResult() {
        return this.mSyncResult;
    }

    public void setSyncResult(WebTilePageSyncInfo syncInfo) {
        this.mSyncResult.add(syncInfo);
    }

    public long getSyncDurationMillisTotal() {
        return this.mSyncDurationMillisTotal;
    }

    public void setSyncDurationMillisTotal(long syncDurationMillisTotal) {
        this.mSyncDurationMillisTotal = syncDurationMillisTotal;
    }

    public Map<UUID, Integer> getFailedTiles() {
        return this.mFailedTiles;
    }

    public void setFailedTiles(UUID id, int code) {
        this.mFailedTiles.put(id, Integer.valueOf(code));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("*** Sync Web Tile Result ***");
        sb.append(String.format("\n** Total %d WebTile\n", Integer.valueOf(this.mFailedTiles.size() + this.mSyncResult.size())));
        if (this.mFailedTiles.size() > 0) {
            sb.append(String.format("\n** Faile to sync %d WebTile\n", Integer.valueOf(this.mFailedTiles.size())));
            for (Map.Entry<UUID, Integer> entry : this.mFailedTiles.entrySet()) {
                sb.append(String.format("  |-- Tile %s :\t %d\n", entry.getKey().toString(), entry.getValue()));
            }
        }
        for (WebTilePageSyncInfo info : this.mSyncResult) {
            sb.append(info.toString());
        }
        sb.append(String.format("*** Total Sync Time (ms):\t%d ***\n", Long.valueOf(this.mSyncDurationMillisTotal)));
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.mSyncResult);
        dest.writeInt(this.mFailedTiles.size());
        for (Map.Entry<UUID, Integer> entry : this.mFailedTiles.entrySet()) {
            dest.writeSerializable(entry.getKey());
            dest.writeInt(entry.getValue().intValue());
        }
        dest.writeLong(this.mSyncDurationMillisTotal);
    }

    WebTileSyncResult(Parcel in) {
        this.mSyncDurationMillisTotal = 0L;
        this.mSyncResult = new ArrayList();
        in.readList(this.mSyncResult, WebTilePageSyncInfo.class.getClassLoader());
        this.mFailedTiles = new HashMap();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            UUID id = (UUID) in.readValue(UUID.class.getClassLoader());
            int code = in.readInt();
            this.mFailedTiles.put(id, Integer.valueOf(code));
        }
        this.mSyncDurationMillisTotal = in.readLong();
    }
}
