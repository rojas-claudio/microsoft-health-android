package com.microsoft.band.service.task;

import android.content.Context;
import android.os.Bundle;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.CargoClientSession;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.band.service.device.DeviceCommand;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
/* loaded from: classes.dex */
public abstract class CargoSessionTask<V> {
    private final Callable<V> mCallable = new Callable<V>() { // from class: com.microsoft.band.service.task.CargoSessionTask.1
        @Override // java.util.concurrent.Callable
        public synchronized V call() throws CargoServiceException {
            V value;
            value = null;
            if (CargoSessionTask.this.getSession() != null) {
                try {
                    KDKLog.i(CargoSessionTask.class.getSimpleName(), "%s task starting..", CargoSessionTask.this.getClass().getSimpleName());
                    value = (V) CargoSessionTask.this.doWork();
                    if (CargoSessionTask.this.isCancelled()) {
                        KDKLog.w(CargoSessionTask.class.getSimpleName(), "%s task cancelled.", CargoSessionTask.this.getClass().getSimpleName());
                    } else {
                        KDKLog.i(CargoSessionTask.class.getSimpleName(), "%s task ran to completion.", CargoSessionTask.this.getClass().getSimpleName());
                    }
                } catch (CargoServiceException e) {
                    KDKLog.e(CargoSessionTask.class.getSimpleName(), "Failed to complete task: " + e.getMessage(), e);
                    throw e;
                }
            }
            return value;
        }
    };
    private volatile WeakReference<CargoClientSession> mClientSessionRef;
    private volatile Future<V> mFutureTask;
    protected String mTAG;

    protected abstract V doWork() throws CargoServiceException;

    public CargoClientSession getSession() {
        WeakReference<CargoClientSession> clientSessionRef = this.mClientSessionRef;
        if (clientSessionRef == null) {
            return null;
        }
        return clientSessionRef.get();
    }

    public void setSession(CargoClientSession clientSession) {
        if (!isRunning()) {
            if (clientSession == null) {
                this.mClientSessionRef = null;
            } else {
                this.mClientSessionRef = new WeakReference<>(clientSession);
            }
        }
    }

    public Context getContext() {
        CargoClientSession session = getSession();
        if (session == null) {
            return null;
        }
        return session.getService();
    }

    public void cancel() {
        Future<V> task = this.mFutureTask;
        if (task != null) {
            task.cancel(false);
        }
    }

    public boolean isRunning() {
        CargoClientSession session = getSession();
        Future<V> task = this.mFutureTask;
        return (session == null || session.isTerminating() || task == null || task.isDone() || task.isCancelled()) ? false : true;
    }

    public boolean isCancelled() {
        Future<V> task = this.mFutureTask;
        return task != null && task.isCancelled();
    }

    public void execute(CargoClientSession clientSession) {
        if (clientSession == null) {
            throw new IllegalArgumentException("clientSession");
        }
        synchronized (this.mCallable) {
            if (!isRunning()) {
                setSession(clientSession);
                this.mFutureTask = clientSession.runAsyncTask(this.mCallable);
            }
        }
    }

    public V waitForCompletion() {
        Future<V> task = this.mFutureTask;
        if (task == null) {
            return null;
        }
        try {
            V value = task.get();
            return value;
        } catch (InterruptedException e) {
            KDKLog.e(this.mTAG, e.getLocalizedMessage(), e);
            return null;
        } catch (CancellationException e2) {
            return null;
        } catch (ExecutionException e3) {
            KDKLog.e(this.mTAG, e3.getLocalizedMessage(), e3);
            return null;
        }
    }

    protected void sendServiceMessage(BandServiceMessage message, BandServiceMessage.Response response, int arg1, Bundle data) {
        CargoClientSession session = getSession();
        if (session != null && !session.isTerminating()) {
            session.sendServiceMessage(message, response, arg1, data);
        }
    }

    protected <TCmd extends CommandBase> TCmd sendCommand(TCmd cmd) throws CargoServiceException {
        CargoClientSession session = getSession();
        if (session == null || session.isTerminating()) {
            throw new CargoServiceException("Session expired.", BandServiceMessage.Response.SERVICE_TERMINATED_ERROR);
        }
        BandServiceMessage.Response response = session.getDeviceProvider().processCommand(cmd);
        if (!cmd.getResult()) {
            throw new CargoServiceException(String.format("%s  command was unsuccessful.", cmd.getCommandType()), response);
        }
        return cmd;
    }

    protected BandServiceMessage.Response sendCommand(DeviceCommand cmd) throws CargoServiceException {
        CargoClientSession session = getSession();
        if (session == null || session.isTerminating()) {
            throw new CargoServiceException("Session expired.", BandServiceMessage.Response.SERVICE_TERMINATED_ERROR);
        }
        return session.getDeviceProvider().processCommand(cmd);
    }
}
