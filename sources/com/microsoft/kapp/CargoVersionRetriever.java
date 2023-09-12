package com.microsoft.kapp;

import com.microsoft.band.client.CargoException;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.kapp.version.Version;
/* loaded from: classes.dex */
public interface CargoVersionRetriever {
    Version getDeviceFirmwareVersion();

    CargoFirmwareUpdateInfo getLatestAvailableFirmwareVersion() throws CargoException;

    Version getMinimumRequiredFirmwareVersion();
}
