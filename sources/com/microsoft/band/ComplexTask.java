package com.microsoft.band;

import com.microsoft.band.internal.util.KDKLog;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
abstract class ComplexTask<ResultT> implements BandPendingResult<ResultT>, Runnable {
    private static final int SCHEDULED_POOL_THREAD_COUNT = 1;
    private BandResultCallback<ResultT> mCallback;
    private volatile boolean mDone;
    private volatile ResultT mResult;
    private Throwable mThrowable;
    private static final String TAG = ComplexTask.class.getSimpleName();
    private static ExecutorService mTaskExecutor = Executors.newSingleThreadExecutor();
    private static ScheduledThreadPoolExecutor mCallbackExecutor = new ScheduledThreadPoolExecutor(1);
    private final CountDownLatch mResultLatch = new CountDownLatch(1);
    private AtomicBoolean mIsCallbackCalled = new AtomicBoolean();

    public abstract ResultT tasks() throws BandException, InterruptedException;

    @Override // com.microsoft.band.BandPendingResult
    public ResultT await() throws InterruptedException, BandException {
        this.mResultLatch.await();
        throwExceptionIfFailure();
        return this.mResult;
    }

    private void throwExceptionIfFailure() throws BandException, InterruptedException {
        if (this.mThrowable instanceof BandException) {
            throw ((BandException) this.mThrowable);
        }
        if (this.mThrowable instanceof RuntimeException) {
            throw ((RuntimeException) this.mThrowable);
        }
        if (this.mThrowable instanceof InterruptedException) {
            throw ((InterruptedException) this.mThrowable);
        }
        if (this.mThrowable != null) {
            throw new RuntimeException(this.mThrowable);
        }
    }

    @Override // com.microsoft.band.BandPendingResult
    public ResultT await(long timeout, TimeUnit timeUnit) throws InterruptedException, TimeoutException, BandException {
        if (!this.mResultLatch.await(timeout, timeUnit)) {
            KDKLog.d(TAG, "Complex task timeout");
            throw new TimeoutException("Wait timed out");
        }
        throwExceptionIfFailure();
        return this.mResult;
    }

    @Override // com.microsoft.band.BandPendingResult
    public void registerResultCallback(BandResultCallback<ResultT> callback) {
        this.mCallback = callback;
        if (this.mDone) {
            fireCallback(callback, false);
        }
    }

    @Override // com.microsoft.band.BandPendingResult
    public void registerResultCallback(final BandResultCallback<ResultT> callback, long timeout, TimeUnit timeUnit) {
        this.mCallback = callback;
        if (this.mDone) {
            fireCallback(callback, false);
        } else {
            mCallbackExecutor.schedule(new Runnable() { // from class: com.microsoft.band.ComplexTask.1
                @Override // java.lang.Runnable
                public void run() {
                    ComplexTask.this.fireCallback(callback, true);
                }
            }, timeout, timeUnit);
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.mResult = tasks();
        } catch (Exception e) {
            this.mThrowable = e;
        }
        this.mDone = true;
        this.mResultLatch.countDown();
        fireCallback(this.mCallback, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireCallback(BandResultCallback<ResultT> callback, boolean isTimeOut) {
        if (callback != null && !this.mIsCallbackCalled.getAndSet(true)) {
            if (isTimeOut) {
                try {
                    this.mThrowable = new BandException("TimeoutException on thread", new TimeoutException(), BandErrorType.TIMEOUT_ERROR);
                } catch (Exception e) {
                    KDKLog.e(TAG, "Callback exception", e);
                    return;
                }
            }
            callback.onResult(this.mResult, this.mThrowable);
        }
    }

    public void start() {
        mTaskExecutor.execute(this);
    }

    public static void destroy() {
        mTaskExecutor.shutdown();
    }

    protected static void reInit() {
        if (mTaskExecutor.isShutdown()) {
            mTaskExecutor = Executors.newSingleThreadExecutor();
        }
    }
}
