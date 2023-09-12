package com.microsoft.kapp.multidevice;

import com.microsoft.band.CargoClient;
import com.microsoft.band.client.CargoException;
/* loaded from: classes.dex */
public interface SingleDeviceEnforcementManager {
    boolean isUserPairedDevice(CargoClient cargoClient) throws CargoException;

    void performSDECheck(CargoClient cargoClient) throws CargoException;
}
