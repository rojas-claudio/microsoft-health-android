package com.microsoft.band.device;

import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.UUIDHelper;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;
/* loaded from: classes.dex */
public class StrappSettingsData implements Serializable {
    public static final int STRAPP_SETTINGS_DATA_STRUCTURE_SIZE = 18;
    public static final int STRAPP_SETTINGS_MASK_SIZE = 2;
    private static final long serialVersionUID = 1;
    private UUID mAppId;
    private int mSettingValue;

    public StrappSettingsData(UUID appId, int settingValue) {
        this.mAppId = appId;
        this.mSettingValue = settingValue;
    }

    public StrappSettingsData(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        byte[] guidArray = new byte[16];
        buffer.get(guidArray);
        this.mAppId = UUIDHelper.guidByteArrayToUuid(guidArray);
        this.mSettingValue = BitHelper.unsignedShortToInteger(buffer.getShort());
    }

    public byte[] toByte() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(18);
        return buffer.put(UUIDHelper.toGuidArray(this.mAppId)).putShort((short) this.mSettingValue).array();
    }
}
