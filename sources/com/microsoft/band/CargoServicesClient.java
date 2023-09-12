package com.microsoft.band;

import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.Validation;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes.dex */
public abstract class CargoServicesClient {
    private static final String TAG = CargoServicesClient.class.getSimpleName();
    private volatile ExecutorService mExecutorService = Executors.newCachedThreadPool();
    private volatile BandServiceConnection mServiceConnection;

    protected CargoServicesClient() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CargoServicesClient(BandServiceConnection serviceConnection) throws CargoException {
        Validation.validateNullParameter(serviceConnection, "Service Connection cannot be null");
        this.mServiceConnection = serviceConnection;
        bindToService();
    }

    protected void bindToService() throws CargoException {
        getServiceConnection().bind();
    }

    public boolean isServiceBound() {
        return this.mServiceConnection != null && this.mServiceConnection.isServiceBound();
    }

    public boolean waitForServiceToBind() throws CargoException {
        return getServiceConnection().waitForServiceToBind();
    }

    public BandServiceConnection getServiceConnection() throws CargoException {
        BandServiceConnection serviceConnection = this.mServiceConnection;
        if (serviceConnection == null) {
            throw new CargoException("Service connection terminated.", BandServiceMessage.Response.SERVICE_TERMINATED_ERROR);
        }
        return serviceConnection;
    }

    public <V> Future<V> execute(Callable<V> callable, long waitForCompletionTimeoutInMillis) throws CargoException {
        ExecutorService executorService = this.mExecutorService;
        if (executorService == null) {
            throw new CargoException("Service instance terminated.", BandServiceMessage.Response.SERVICE_TERMINATED_ERROR);
        }
        Future<V> future = executorService.submit(callable);
        if (waitForCompletionTimeoutInMillis != 0) {
            try {
                if (waitForCompletionTimeoutInMillis > 0) {
                    future.get(waitForCompletionTimeoutInMillis, TimeUnit.MILLISECONDS);
                } else {
                    future.get();
                }
            } catch (InterruptedException e) {
                KDKLog.e(TAG, e.getMessage());
                throw new CargoException("Operation was interrupted", e, BandServiceMessage.Response.OPERATION_INTERRUPTED_ERROR);
            } catch (CancellationException e2) {
                KDKLog.e(TAG, e2.getMessage());
                throw new CargoException("Operation was cancelled", e2, BandServiceMessage.Response.OPERATION_INTERRUPTED_ERROR);
            } catch (ExecutionException e3) {
                KDKLog.e(TAG, e3.getMessage());
                throw new CargoException("Operation raised an exception", e3, BandServiceMessage.Response.OPERATION_EXCEPTION_ERROR);
            } catch (TimeoutException e4) {
                if (!future.isDone()) {
                    future.cancel(true);
                    String message = String.format(Locale.getDefault(), "Operation timed out after %d ms.", Long.valueOf(waitForCompletionTimeoutInMillis));
                    KDKLog.e(TAG, message);
                    throw new CargoException(message, BandServiceMessage.Response.OPERATION_TIMEOUT_ERROR);
                }
            }
        }
        return future;
    }

    public void destroy() {
        if (this.mExecutorService != null) {
            this.mExecutorService.shutdownNow();
            this.mExecutorService = null;
        }
        BandServiceConnection serviceConnection = this.mServiceConnection;
        this.mServiceConnection = null;
        if (serviceConnection != null) {
            serviceConnection.dispose();
            KDKLog.i(TAG, "destroy: services have been stopped and unbound for %s.", getClass().getSimpleName());
        }
    }

    public boolean isDestroyed() {
        return this.mExecutorService == null && this.mServiceConnection == null;
    }
}
