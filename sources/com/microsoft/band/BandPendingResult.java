package com.microsoft.band;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes.dex */
public interface BandPendingResult<R> {
    R await() throws InterruptedException, BandException;

    R await(long j, TimeUnit timeUnit) throws InterruptedException, TimeoutException, BandException;

    void registerResultCallback(BandResultCallback<R> bandResultCallback);

    void registerResultCallback(BandResultCallback<R> bandResultCallback, long j, TimeUnit timeUnit);
}
