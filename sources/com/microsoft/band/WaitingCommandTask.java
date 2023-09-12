package com.microsoft.band;

import android.os.SystemClock;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.Validation;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
class WaitingCommandTask<ResultT, C extends CommandBase> implements BandPendingResult<ResultT> {
    private static final int SCHEDULED_POOL_THREAD_COUNT = 1;
    private static final String TAG = WaitingCommandTask.class.getSimpleName();
    private static ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);
    private volatile ResultT mCachedResult;
    private final C mCommand;
    private volatile boolean mIsDone;
    private volatile BandResultCallback<ResultT> mResultCallback;
    private final Object mLock = new Object();
    private AtomicBoolean mIsCallbackCalled = new AtomicBoolean();
    private final Object mCachedResultLock = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    public WaitingCommandTask(C command) {
        this.mCommand = command;
    }

    @Override // com.microsoft.band.BandPendingResult
    public ResultT await() throws InterruptedException, BandException {
        try {
            return await(0L, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            KDKLog.d(TAG, "this should not happened");
            throw new RuntimeException();
        }
    }

    @Override // com.microsoft.band.BandPendingResult
    public ResultT await(long timeout, TimeUnit timeUnit) throws InterruptedException, TimeoutException, BandException {
        Validation.notNull(timeUnit, "Time unit cannot be null");
        if (!this.mIsDone) {
            synchronized (this.mLock) {
                boolean timed = timeout > 0;
                long timeoutMills = timeUnit.toMillis(timeout);
                boolean isTimeout = false;
                long deadline = SystemClock.uptimeMillis() + timeoutMills;
                while (!this.mIsDone && !isTimeout) {
                    this.mLock.wait(timeoutMills);
                    if (timed) {
                        isTimeout = deadline - SystemClock.uptimeMillis() <= 0;
                    }
                }
                if (isTimeout) {
                    throw new TimeoutException("Command=" + this.mCommand.getCommandType());
                }
            }
        }
        return cacheResult(this.mCommand, false);
    }

    @Override // com.microsoft.band.BandPendingResult
    public void registerResultCallback(BandResultCallback<ResultT> callback) {
        Validation.notNull(callback, "Callback cannot be null");
        this.mResultCallback = callback;
        if (this.mIsDone) {
            fireCallback(callback, false);
        }
    }

    @Override // com.microsoft.band.BandPendingResult
    public void registerResultCallback(final BandResultCallback<ResultT> callback, long timeout, TimeUnit timeUnit) {
        Validation.notNull(callback, "Callback cannot be null");
        Validation.notNull(timeUnit, "Time unit cannot be null");
        this.mResultCallback = callback;
        if (this.mIsDone) {
            fireCallback(callback, false);
        } else {
            scheduledExecutor.schedule(new Runnable() { // from class: com.microsoft.band.WaitingCommandTask.1
                @Override // java.lang.Runnable
                public void run() {
                    WaitingCommandTask.this.fireCallback(callback, true);
                }
            }, timeout, timeUnit);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireCallback(BandResultCallback<ResultT> callback, boolean isTimeOut) {
        if (callback != null && !this.mIsCallbackCalled.getAndSet(true)) {
            try {
                callback.onResult(cacheResult(this.mCommand, isTimeOut), null);
            } catch (BandException e) {
                callback.onResult(exceptionToResult(this.mCommand, e), e);
            } catch (Exception e2) {
                KDKLog.e(TAG, "Callback exception", e2);
            }
        }
    }

    public Object lock() {
        return this.mLock;
    }

    public void ready() {
        synchronized (this.mLock) {
            this.mIsDone = true;
            this.mLock.notifyAll();
        }
        fireCallback(this.mResultCallback, false);
    }

    public C getCommand() {
        return this.mCommand;
    }

    private ResultT cacheResult(C command, boolean isTimeOut) throws BandException {
        ResultT result = this.mCachedResult;
        if (result == null) {
            synchronized (this.mCachedResultLock) {
                result = this.mCachedResult;
                if (result == null) {
                    result = toResult(command, isTimeOut);
                    this.mCachedResult = result;
                }
            }
        }
        return result;
    }

    public ResultT toResult(C command, boolean isTimeOut) throws BandException {
        Validation.validateTimeoutAndResultCode(command, isTimeOut, TAG);
        return null;
    }

    public ResultT exceptionToResult(C command, BandException e) {
        return null;
    }

    static void destroy() {
        scheduledExecutor.shutdown();
        scheduledExecutor = null;
    }
}
