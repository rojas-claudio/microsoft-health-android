package com.microsoft.band.device;

import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.UUIDHelper;
import com.microsoft.band.util.StringHelper;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;
/* loaded from: classes.dex */
public class StrappData implements Serializable {
    public static final int STRAPP_DATA_STRUCTURE_SIZE = 88;
    private static final long serialVersionUID = 1;
    private String mFriendlyName;
    private byte[] mHashedAppId;
    private UUID mId;
    private int mSettingMask;
    private long mStartStripOrder;
    private long mThemeColor;

    public StrappData(UUID tileId, long startStripOrder, long themeColor, int settingMask, String friendlyName) {
        this.mHashedAppId = null;
        this.mId = tileId;
        this.mStartStripOrder = startStripOrder;
        this.mThemeColor = themeColor;
        this.mSettingMask = settingMask;
        this.mFriendlyName = friendlyName;
    }

    public StrappData(UUID tileId, long startStripOrder, long themeColor, int settingMask, String friendlyName, byte[] hashedAppId) {
        this(tileId, startStripOrder, themeColor, settingMask, friendlyName);
        this.mHashedAppId = hashedAppId;
    }

    public StrappData(byte[] data) {
        this.mHashedAppId = null;
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] guidArray = new byte[16];
        buffer.get(guidArray);
        this.mId = UUIDHelper.guidByteArrayToUuid(guidArray);
        this.mStartStripOrder = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mThemeColor = BitHelper.unsignedIntegerToLong(buffer.getInt());
        int friendlyNameLength = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mSettingMask = BitHelper.unsignedShortToInteger(buffer.getShort());
        byte[] nameByte = new byte[friendlyNameLength * 2];
        buffer.get(nameByte);
        this.mFriendlyName = StringHelper.valueOf(nameByte);
        if (friendlyNameLength <= 21) {
            this.mHashedAppId = new byte[16];
            buffer.position(72);
            buffer.get(this.mHashedAppId);
        }
    }

    public byte[] toByte() {
        byte[] nameBytes = StringHelper.getBytes(this.mFriendlyName);
        int nameLength = this.mFriendlyName.length();
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(88);
        buffer.put(UUIDHelper.toGuidArray(this.mId)).putInt((int) this.mStartStripOrder).putInt((int) this.mThemeColor).putShort((short) nameLength).putShort((short) this.mSettingMask).put(nameBytes);
        if (nameLength <= 42 && this.mHashedAppId != null) {
            buffer.position(72);
            buffer.put(this.mHashedAppId);
        }
        return buffer.array();
    }

    public UUID getAppId() {
        return this.mId;
    }

    public long getStartStripOrder() {
        return this.mStartStripOrder;
    }

    public void setStartStripOrder(long startStripOrder) {
        this.mStartStripOrder = startStripOrder;
    }

    public long getThemeColor() {
        return this.mThemeColor;
    }

    public int getSettingMask() {
        return this.mSettingMask;
    }

    public String getFriendlyName() {
        return this.mFriendlyName;
    }

    public byte[] getAppHashId() {
        return this.mHashedAppId;
    }
}
