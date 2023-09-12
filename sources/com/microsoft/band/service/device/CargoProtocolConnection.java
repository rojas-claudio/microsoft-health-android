package com.microsoft.band.service.device;

import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.device.FWVersion;
import com.microsoft.band.internal.BandServiceMessage;
import java.util.UUID;
/* loaded from: classes.dex */
public interface CargoProtocolConnection extends DeviceConnection {
    BandServiceMessage.Response bootIntoFirmwareUpdateMode();

    UUID getDeviceUUID();

    FWVersion[] getFirmwareVersions();

    int getLogVersion();

    DeviceConstants.AppRunning getRunningApplication();

    String getSerialNumber();

    boolean isUpdatingFirmware();

    BandServiceMessage.Response performIO(DeviceCommand deviceCommand);

    BandServiceMessage.Response processCommand(DeviceCommand deviceCommand);

    void resetDevice();

    BandServiceMessage.Response waitForFirmwareUpdateToComplete();
}
