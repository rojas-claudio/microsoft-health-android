package com.microsoft.band.service.device;

import com.microsoft.band.internal.device.DeviceInfo;
import java.io.IOException;
import java.util.UUID;
/* loaded from: classes.dex */
public interface DeviceConnection {
    void connect() throws IOException;

    void connect(boolean z) throws IOException;

    void disconnect();

    void dispose();

    String getDeviceAddress();

    DeviceInfo getDeviceInfo();

    String getDeviceName();

    UUID getProtocolUUID();

    DeviceServiceProvider getServiceProvider();

    boolean isConnected();

    boolean isDisposed();

    void reset();

    void waitForDeviceToDisconnect();
}
