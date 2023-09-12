package com.microsoft.band.tiles;

import android.content.Intent;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.UUIDHelper;
import com.microsoft.band.util.StringHelper;
import java.nio.ByteBuffer;
import java.util.UUID;
/* loaded from: classes.dex */
public class CargoTileEvent {
    private static final int TILE_NAME_LENGTH = 44;
    private int mContextValue;
    private int mElementID;
    private byte[] mHashArray;
    private UUID mPageID;
    private UUID mTileID;
    private String mTileName;
    private long mTimeStamp;

    public CargoTileEvent(ByteBuffer buffer, long timeStamp) {
        this.mTimeStamp = timeStamp;
        this.mContextValue = (int) BitHelper.unsignedIntegerToLong(buffer.getInt());
        byte[] guidArray = new byte[16];
        buffer.get(guidArray);
        this.mTileID = UUIDHelper.guidByteArrayToUuid(guidArray);
        byte[] guidArray2 = new byte[16];
        buffer.get(guidArray2);
        this.mPageID = UUIDHelper.guidByteArrayToUuid(guidArray2);
        this.mElementID = BitHelper.unsignedShortToInteger(buffer.getShort());
        byte[] nameByte = new byte[44];
        buffer.get(nameByte);
        this.mTileName = StringHelper.valueOf(nameByte).trim();
        byte[] hashArray = new byte[16];
        buffer.get(hashArray);
        this.mHashArray = hashArray;
        buffer.getLong();
    }

    public byte[] getHashArray() {
        return this.mHashArray;
    }

    public Intent createIntentForEvent() {
        Intent intent = new Intent();
        switch (this.mContextValue) {
            case 0:
                intent.setAction(TileEvent.ACTION_TILE_OPENED);
                intent.putExtra(TileEvent.TILE_EVENT_DATA, new TileEvent(this.mTileName, this.mTileID, this.mTimeStamp));
                break;
            case 1:
                intent.setAction(TileEvent.ACTION_TILE_BUTTON_PRESSED);
                intent.putExtra(TileEvent.TILE_EVENT_DATA, new TileButtonEvent(this.mTileName, this.mTileID, this.mPageID, this.mElementID, this.mTimeStamp));
                break;
            case 2:
                intent.setAction(TileEvent.ACTION_TILE_CLOSED);
                intent.putExtra(TileEvent.TILE_EVENT_DATA, new TileEvent(this.mTileName, this.mTileID, this.mTimeStamp));
                break;
        }
        return intent;
    }
}
