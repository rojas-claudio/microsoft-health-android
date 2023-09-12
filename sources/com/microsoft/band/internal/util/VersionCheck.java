package com.microsoft.band.internal.util;
/* loaded from: classes.dex */
public class VersionCheck {
    private VersionCheck() {
        throw new UnsupportedOperationException();
    }

    public static boolean isV2DeviceOrGreater(int hardwareVersion) {
        return hardwareVersion >= 20;
    }
}
