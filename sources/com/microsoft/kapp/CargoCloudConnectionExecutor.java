package com.microsoft.kapp;

import com.microsoft.band.CargoCloudClient;
import com.microsoft.band.client.CargoException;
/* loaded from: classes.dex */
public interface CargoCloudConnectionExecutor<T> {
    T execute(CargoCloudClient cargoCloudClient) throws CargoException;
}
