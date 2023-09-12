package com.microsoft.kapp;

import com.microsoft.band.CargoClient;
import com.microsoft.band.client.CargoException;
/* loaded from: classes.dex */
public interface CargoConnectionExecutor<T> {
    T execute(CargoClient cargoClient) throws CargoException;
}
