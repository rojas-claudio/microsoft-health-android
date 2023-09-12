package com.microsoft.band.device;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: classes.dex */
public final class TileSettings {
    public static final int ENABLE_BADGING = 2;
    public static final int ENABLE_NOTIFICATION = 1;
    public static final int NONE = 0;
    public static final int SCREEN_TIMEOUT_30_SECONDS = 16;
    public static final int SCREEN_TIMEOUT_DISABLED = 32;
    public static final int USE_CUSTOM_COLOR_FOR_TILE = 4;

    private TileSettings() {
        throw new UnsupportedOperationException();
    }

    public static int get(byte[] settingBytes) {
        ByteBuffer buffer = ByteBuffer.wrap(settingBytes).order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getShort();
    }
}
