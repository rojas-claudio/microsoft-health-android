package com.microsoft.band;

import android.os.Looper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes.dex */
abstract class ComplexNonUIAwaitableTask<ResultT> extends ComplexTask<ResultT> {
    @Override // com.microsoft.band.ComplexTask, com.microsoft.band.BandPendingResult
    public ResultT await() throws InterruptedException, BandException {
        if (isMainThread()) {
            throw new RuntimeException("This method cannot be called from main thread");
        }
        return (ResultT) super.await();
    }

    @Override // com.microsoft.band.ComplexTask, com.microsoft.band.BandPendingResult
    public ResultT await(long timeout, TimeUnit timeUnit) throws InterruptedException, TimeoutException, BandException {
        if (isMainThread()) {
            throw new RuntimeException("This method cannot be called from main thread");
        }
        return (ResultT) super.await();
    }

    private boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
