package com.microsoft.band.tiles;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.UUID;
/* loaded from: classes.dex */
public final class TileButtonEvent extends TileEvent {
    public static final Parcelable.Creator<TileButtonEvent> CREATOR = new Parcelable.Creator<TileButtonEvent>() { // from class: com.microsoft.band.tiles.TileButtonEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TileButtonEvent createFromParcel(Parcel in) {
            return new TileButtonEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TileButtonEvent[] newArray(int size) {
            return new TileButtonEvent[size];
        }
    };
    private int mElementID;
    private UUID mPageID;

    private TileButtonEvent(Parcel in) {
        super(in);
        this.mPageID = (UUID) in.readSerializable();
        this.mElementID = in.readInt();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TileButtonEvent(String tileName, UUID tileID, UUID pageID, int elementID, long timeStamp) {
        super(tileName, tileID, timeStamp);
        this.mPageID = pageID;
        this.mElementID = elementID;
    }

    public UUID getPageID() {
        return this.mPageID;
    }

    public int getElementID() {
        return this.mElementID;
    }

    @Override // com.microsoft.band.tiles.TileEvent
    public String toString() {
        StringBuffer sb = new StringBuffer(super.toString());
        sb.append(String.format("     |--Page ID = %s\n", this.mPageID)).append(String.format("     |--Element ID = %d\n", Integer.valueOf(this.mElementID)));
        return sb.toString();
    }

    @Override // com.microsoft.band.tiles.TileEvent, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(this.mPageID);
        dest.writeInt(this.mElementID);
    }
}
