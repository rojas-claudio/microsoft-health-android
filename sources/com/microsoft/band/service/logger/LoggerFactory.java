package com.microsoft.band.service.logger;

import android.content.Context;
import com.microsoft.band.internal.util.KDKLog;
/* loaded from: classes.dex */
public final class LoggerFactory {
    public static final String ACTION_TELEMETRY_MESSAGE = "com.microsoft.band.telemetry";
    public static final String LOG_EVENT = "event";
    private static volatile BroadcastLogger mLogger;
    private static final String TAG = LoggerFactory.class.getSimpleName();
    private static final CargoLogger EMPTY_LOGGER = new EmptyLogger();
    private static final Object LOCK = new Object();

    private LoggerFactory() throws InstantiationException {
        throw new InstantiationException("do not create " + LoggerFactory.class.getSimpleName());
    }

    public static void initLogger(Context context) {
        if (mLogger == null) {
            synchronized (LOCK) {
                if (mLogger == null) {
                    mLogger = new BroadcastLogger("Default", context);
                    KDKLog.d(TAG, "Logger " + mLogger.getName() + " initialized");
                }
            }
        }
    }

    public static CargoLogger getLogger(Context context) {
        initLogger(context);
        return mLogger;
    }

    public static CargoLogger getLogger() {
        if (mLogger == null) {
            KDKLog.w(TAG, "Cargo logger is not initialized. Used EMPTY_LOGGER");
            return EMPTY_LOGGER;
        }
        return mLogger;
    }

    public static void setTelemetryEnabled(boolean f) {
        if (mLogger != null) {
            KDKLog.i(TAG, "telemetry = " + f);
            mLogger.setTelemetryEnabled(f);
        }
    }

    public static void setPerformanceEnabled(boolean f) {
        if (mLogger != null) {
            KDKLog.i(TAG, "performance = " + f);
            mLogger.setPerformanceFlag(f);
        }
    }

    public static void setDiagnosticsEnabled(boolean f) {
        if (mLogger != null) {
            KDKLog.i(TAG, "performance = " + f);
            mLogger.setDiagnosticsFlag(f);
        }
    }

    /* loaded from: classes.dex */
    private static class EmptyLogger implements CargoLogger {
        @Override // com.microsoft.band.service.logger.CargoLogger
        public boolean isTelemetryEnabled() {
            return false;
        }

        @Override // com.microsoft.band.service.logger.CargoLogger
        public void signal(long timestamp, String tag, String name) {
        }

        @Override // com.microsoft.band.service.logger.CargoLogger
        public void signal(long timestamp, String tag, String name, String format, Object... params) {
        }

        @Override // com.microsoft.band.service.logger.CargoLogger
        public boolean isPerfLoggingEnabled() {
            return false;
        }

        @Override // com.microsoft.band.service.logger.CargoLogger
        public void perfLog(long timestamp, String tag, String name) {
        }

        @Override // com.microsoft.band.service.logger.CargoLogger
        public void perfLog(long timestamp, String tag, String name, String format, Object... params) {
        }

        @Override // com.microsoft.band.service.logger.CargoLogger
        public boolean isTraceDiagnosticsEnabled() {
            return false;
        }

        @Override // com.microsoft.band.service.logger.CargoLogger
        public void traceDiagnostics(long timestamp, String tag, String name) {
        }

        @Override // com.microsoft.band.service.logger.CargoLogger
        public void traceDiagnostics(long timestamp, String tag, String name, String format, Object... params) {
        }

        @Override // com.microsoft.band.service.logger.CargoLogger
        public void traceDiagnostics(String tag, String name, String format, Object... params) {
        }
    }
}
