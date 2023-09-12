package com.microsoft.band;

import com.microsoft.band.internal.util.Validation;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
final class EmptyResult implements BandPendingResult<Void> {
    @Override // com.microsoft.band.BandPendingResult
    public Void await() {
        return null;
    }

    @Override // com.microsoft.band.BandPendingResult
    public Void await(long timeout, TimeUnit timeUnit) {
        return null;
    }

    @Override // com.microsoft.band.BandPendingResult
    public void registerResultCallback(BandResultCallback<Void> callback) {
        Validation.notNull(callback, "Callback cannot be null");
        callback.onResult(null, null);
    }

    @Override // com.microsoft.band.BandPendingResult
    public void registerResultCallback(BandResultCallback<Void> callback, long timeout, TimeUnit timeUnit) {
        Validation.notNull(callback, "Callback cannot be null");
        callback.onResult(null, null);
    }
}
