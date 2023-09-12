package com.microsoft.band.tiles;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.device.DeviceDataModel;
import com.microsoft.band.sensors.BandSensorEvent;
import java.util.UUID;
/* loaded from: classes.dex */
public class TileEvent extends DeviceDataModel implements BandSensorEvent {
    public static final String ACTION_TILE_BUTTON_PRESSED = "com.microsoft.band.action.ACTION_TILE_BUTTON_PRESSED";
    public static final String ACTION_TILE_CLOSED = "com.microsoft.band.action.ACTION_TILE_CLOSED";
    public static final String ACTION_TILE_OPENED = "com.microsoft.band.action.ACTION_TILE_OPENED";
    public static final Parcelable.Creator<TileEvent> CREATOR = new Parcelable.Creator<TileEvent>() { // from class: com.microsoft.band.tiles.TileEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TileEvent createFromParcel(Parcel in) {
            return new TileEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TileEvent[] newArray(int size) {
            return new TileEvent[size];
        }
    };
    public static final String TILE_EVENT_DATA = "TILE_EVENT_DATA";
    private UUID mTileID;
    private String mTileName;
    private long mTimeStamp;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TileEvent(Parcel in) {
        super(in);
        this.mTileName = in.readString();
        this.mTileID = (UUID) in.readSerializable();
        this.mTimeStamp = in.readLong();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TileEvent(String tileName, UUID tileID, long timeStamp) {
        this.mTileName = tileName;
        this.mTileID = tileID;
        this.mTimeStamp = timeStamp;
    }

    public String getTileName() {
        return this.mTileName;
    }

    public UUID getTileID() {
        return this.mTileID;
    }

    @Override // com.microsoft.band.sensors.BandSensorEvent
    public long getTimestamp() {
        return this.mTimeStamp;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("     |--Tile Name = %s\n", this.mTileName)).append(String.format("     |--Tile ID = %s\n", this.mTileID));
        return sb.toString();
    }

    @Override // com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mTileName);
        dest.writeSerializable(this.mTileID);
        dest.writeLong(this.mTimeStamp);
    }
}
