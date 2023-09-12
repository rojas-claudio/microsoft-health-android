package com.microsoft.band;

import com.microsoft.band.internal.util.Validation;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes.dex */
final class ErrorBandPendingResult<T> implements BandPendingResult<T> {
    private final T mReturnValue;
    private final Throwable mThrowable;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ErrorBandPendingResult(Throwable e, T returnValue) {
        this.mThrowable = e;
        this.mReturnValue = returnValue;
    }

    @Override // com.microsoft.band.BandPendingResult
    public T await() throws InterruptedException, BandException {
        return this.mReturnValue;
    }

    @Override // com.microsoft.band.BandPendingResult
    public T await(long timeout, TimeUnit timeUnit) throws InterruptedException, TimeoutException, BandException {
        return this.mReturnValue;
    }

    @Override // com.microsoft.band.BandPendingResult
    public void registerResultCallback(BandResultCallback<T> callback) {
        Validation.notNull(callback, "Callback cannot be null");
        callback.onResult(this.mReturnValue, this.mThrowable);
    }

    @Override // com.microsoft.band.BandPendingResult
    public void registerResultCallback(BandResultCallback<T> callback, long timeout, TimeUnit timeUnit) {
        Validation.notNull(callback, "Callback cannot be null");
        callback.onResult(this.mReturnValue, this.mThrowable);
    }
}
