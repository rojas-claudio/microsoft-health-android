package com.microsoft.band.internal;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.microsoft.band.internal.util.KDKLog;
/* loaded from: classes.dex */
public final class EventHandlerThread extends Thread {
    final IEventHandlerDelegate mEventHandlerDelegate;
    private volatile Handler mHandler;
    private Runnable mSignalLoopStarted = new Runnable() { // from class: com.microsoft.band.internal.EventHandlerThread.1
        @Override // java.lang.Runnable
        public synchronized void run() {
            KDKLog.i(EventHandlerThread.this.getName(), "** LOOPER STARTED **");
            notifyAll();
        }
    };
    private Runnable mStopLooper = new Runnable() { // from class: com.microsoft.band.internal.EventHandlerThread.2
        @Override // java.lang.Runnable
        public void run() {
            Looper.myLooper().quit();
        }
    };

    /* loaded from: classes.dex */
    public interface IEventHandlerDelegate {
        void handleMessage(Message message);
    }

    public EventHandlerThread(String name, IEventHandlerDelegate eventHandlerDelegate) {
        if (eventHandlerDelegate == null) {
            throw new NullPointerException("IEventHandlerDelegate cannot be null");
        }
        setName(name);
        this.mEventHandlerDelegate = eventHandlerDelegate;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public boolean startLooper() {
        synchronized (this.mSignalLoopStarted) {
            start();
            while (this.mHandler == null && isAlive()) {
                try {
                    this.mSignalLoopStarted.wait();
                } catch (InterruptedException e) {
                    KDKLog.i(getName(), e.getMessage());
                }
            }
        }
        return isAlive();
    }

    public boolean isLooping() {
        return this.mHandler != null;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    @SuppressLint({"HandlerLeak"})
    public void run() {
        Looper.prepare();
        this.mHandler = new Handler() { // from class: com.microsoft.band.internal.EventHandlerThread.3
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                EventHandlerThread.this.mEventHandlerDelegate.handleMessage(msg);
            }
        };
        this.mHandler.post(this.mSignalLoopStarted);
        Looper.loop();
        KDKLog.i(getName(), "** LOOPER STOPPED **");
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public void stopLooper() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.post(this.mStopLooper);
        }
    }
}
