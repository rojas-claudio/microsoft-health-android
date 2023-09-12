package com.microsoft.band.webtiles;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.BandServiceMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public final class WebTilePageSyncInfo implements Parcelable {
    public static final Parcelable.Creator<WebTilePageSyncInfo> CREATOR = new Parcelable.Creator<WebTilePageSyncInfo>() { // from class: com.microsoft.band.webtiles.WebTilePageSyncInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTilePageSyncInfo createFromParcel(Parcel in) {
            return new WebTilePageSyncInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTilePageSyncInfo[] newArray(int size) {
            return new WebTilePageSyncInfo[size];
        }
    };
    private int mClearPagesResult;
    private int mNotificationResult;
    private Map<UUID, Integer> mPageSyncResult;
    private UUID mTileId;

    public WebTilePageSyncInfo(UUID id) {
        this.mClearPagesResult = BandServiceMessage.Response.DEVICE_COMMAND_ERROR.getCode();
        this.mNotificationResult = BandServiceMessage.Response.DEVICE_COMMAND_ERROR.getCode();
        this.mTileId = id;
        this.mPageSyncResult = new HashMap();
    }

    public UUID getTileId() {
        return this.mTileId;
    }

    public Map<UUID, Integer> getPageSyncResult() {
        return this.mPageSyncResult;
    }

    public void setPageSyncResult(UUID id, int code) {
        this.mPageSyncResult.put(id, Integer.valueOf(code));
    }

    public int getClearPagesResult() {
        return this.mClearPagesResult;
    }

    public void setClearPagesResult(int clearPagesResult) {
        this.mClearPagesResult = clearPagesResult;
    }

    public int getNotificationResult() {
        return this.mNotificationResult;
    }

    public void setNotificationResult(int notificationResult) {
        this.mNotificationResult = notificationResult;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\n** WebTile %s Sync %d page\n", this.mTileId.toString(), Integer.valueOf(this.mPageSyncResult.size())));
        for (Map.Entry<UUID, Integer> entry : this.mPageSyncResult.entrySet()) {
            Object[] objArr = new Object[2];
            objArr[0] = entry.getKey().toString();
            objArr[1] = entry.getValue().intValue() == BandServiceMessage.Response.SUCCESS.getCode() ? "Success" : "Fail";
            sb.append(String.format("  |-- Page %s :\t %s\n", objArr));
        }
        if (this.mClearPagesResult != 0) {
            Object[] objArr2 = new Object[1];
            objArr2[0] = getClearPagesResult() == BandServiceMessage.Response.SUCCESS.getCode() ? "Success" : "Fail";
            sb.append(String.format("\n** ClearPage: %s \n", objArr2));
        }
        if (this.mNotificationResult != 0) {
            Object[] objArr3 = new Object[1];
            objArr3[0] = getNotificationResult() == BandServiceMessage.Response.SUCCESS.getCode() ? "Success" : "Fail";
            sb.append(String.format("\n** SendNotificationc: %s \n", objArr3));
        }
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mTileId);
        dest.writeInt(this.mClearPagesResult);
        dest.writeInt(this.mNotificationResult);
        dest.writeInt(this.mPageSyncResult.size());
        for (Map.Entry<UUID, Integer> entry : this.mPageSyncResult.entrySet()) {
            dest.writeValue(entry.getKey());
            dest.writeInt(entry.getValue().intValue());
        }
    }

    WebTilePageSyncInfo(Parcel in) {
        this.mClearPagesResult = BandServiceMessage.Response.DEVICE_COMMAND_ERROR.getCode();
        this.mNotificationResult = BandServiceMessage.Response.DEVICE_COMMAND_ERROR.getCode();
        this.mTileId = (UUID) in.readValue(UUID.class.getClassLoader());
        this.mClearPagesResult = in.readInt();
        this.mNotificationResult = in.readInt();
        this.mPageSyncResult = new HashMap();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            UUID id = (UUID) in.readValue(UUID.class.getClassLoader());
            int code = in.readInt();
            this.mPageSyncResult.put(id, Integer.valueOf(code));
        }
    }
}
