package com.microsoft.applicationinsights.library;

import android.content.Context;
import android.os.Process;
import com.microsoft.applicationinsights.library.CreateDataTask;
import com.microsoft.applicationinsights.logging.InternalLogging;
import java.lang.Thread;
import java.util.LinkedHashMap;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ExceptionTracking implements Thread.UncaughtExceptionHandler {
    private static final Object LOCK = new Object();
    private static String TAG = "ExceptionHandler";
    private boolean ignoreDefaultHandler;
    protected Thread.UncaughtExceptionHandler preexistingExceptionHandler;

    protected ExceptionTracking(Context context, Thread.UncaughtExceptionHandler preexistingExceptionHandler, boolean ignoreDefaultHandler) {
        this.preexistingExceptionHandler = preexistingExceptionHandler;
        if (context != null) {
            this.ignoreDefaultHandler = ignoreDefaultHandler;
        } else {
            InternalLogging.error(TAG, "Failed to initialize ExceptionHandler because the provided Context was null");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void registerExceptionHandler(Context context) {
        registerExceptionHandler(context, false);
    }

    protected static void registerExceptionHandler(Context context, boolean ignoreDefaultHandler) {
        synchronized (LOCK) {
            Thread.UncaughtExceptionHandler preexistingExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            if (preexistingExceptionHandler instanceof ExceptionTracking) {
                InternalLogging.error(TAG, "ExceptionHandler was already registered for this thread");
            } else {
                ExceptionTracking handler = new ExceptionTracking(context, preexistingExceptionHandler, ignoreDefaultHandler);
                Thread.setDefaultUncaughtExceptionHandler(handler);
            }
        }
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable exception) {
        Map<String, String> properties = null;
        if (thread != null) {
            properties = new LinkedHashMap<>();
            properties.put("threadName", thread.getName());
            properties.put("threadId", Long.toString(thread.getId()));
            properties.put("threadPriority", Integer.toString(thread.getPriority()));
        }
        new CreateDataTask(CreateDataTask.DataType.UNHANDLED_EXCEPTION, exception, properties).execute(new Void[0]);
        if (!this.ignoreDefaultHandler && this.preexistingExceptionHandler != null) {
            this.preexistingExceptionHandler.uncaughtException(thread, exception);
        } else {
            killProcess();
        }
    }

    protected void killProcess() {
        Process.killProcess(Process.myPid());
        System.exit(10);
    }
}
