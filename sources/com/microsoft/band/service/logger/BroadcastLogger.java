package com.microsoft.band.service.logger;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.KDKLog;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class BroadcastLogger implements CargoLogger {
    private final Context mContext;
    private volatile boolean mDiagnosticsFlag;
    private final LocalBroadcastManager mLocalBroadcastManager;
    private final String mLoggerName;
    private volatile boolean mPerformanceFlag;
    private volatile boolean mTelemetryFlag;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BroadcastLogger(String name, Context ctx) {
        this.mLoggerName = name;
        this.mContext = ctx;
        this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(this.mContext);
    }

    @Override // com.microsoft.band.service.logger.CargoLogger
    public boolean isTelemetryEnabled() {
        return this.mTelemetryFlag;
    }

    @Override // com.microsoft.band.service.logger.CargoLogger
    public void signal(long timestamp, String tag, String name) {
        if (this.mTelemetryFlag) {
            this.mLocalBroadcastManager.sendBroadcast(createMessage(timestamp, tag, name, null, new Object[0]));
        }
    }

    @Override // com.microsoft.band.service.logger.CargoLogger
    public void signal(long timestamp, String tag, String name, String format, Object... params) {
        if (this.mTelemetryFlag) {
            this.mLocalBroadcastManager.sendBroadcast(createMessage(timestamp, tag, name, format, params));
        }
    }

    @Override // com.microsoft.band.service.logger.CargoLogger
    public boolean isPerfLoggingEnabled() {
        return this.mPerformanceFlag;
    }

    @Override // com.microsoft.band.service.logger.CargoLogger
    public void perfLog(long timestamp, String tag, String name) {
        if (this.mPerformanceFlag) {
            KDKLog.i(tag, "Perormance: tag=" + tag + ", name=" + name + ", timestamp=" + timestamp);
        }
    }

    @Override // com.microsoft.band.service.logger.CargoLogger
    public void perfLog(long timestamp, String tag, String name, String format, Object... params) {
        if (this.mPerformanceFlag) {
            try {
                KDKLog.i(tag, "Perormance: tag=" + tag + ", name=" + name + ", timestamp=" + timestamp + ", comment={" + formatWithByteArray(format, params) + "}");
            } catch (Exception e) {
                KDKLog.w(tag, "Incorrect format string:" + format, e);
            }
        }
    }

    private String formatWithByteArray(String format, Object... params) {
        for (int i = 0; i < params.length; i++) {
            Object p = params[i];
            if (p instanceof byte[]) {
                byte[] bytes = (byte[]) p;
                params[i] = BufferUtil.toHexOctetString(bytes, 0, bytes.length);
            }
        }
        return String.format(format, params);
    }

    @Override // com.microsoft.band.service.logger.CargoLogger
    public boolean isTraceDiagnosticsEnabled() {
        return this.mDiagnosticsFlag;
    }

    @Override // com.microsoft.band.service.logger.CargoLogger
    public void traceDiagnostics(long timestamp, String tag, String name) {
        if (this.mDiagnosticsFlag) {
            KDKLog.i(tag, "Diagnostics: tag=" + tag + ", name=" + name + ", timestamp=" + timestamp);
        }
    }

    @Override // com.microsoft.band.service.logger.CargoLogger
    public void traceDiagnostics(long timestamp, String tag, String name, String format, Object... params) {
        if (this.mDiagnosticsFlag) {
            try {
                KDKLog.i(tag, "Diagnostics: tag=" + tag + ", name=" + name + ", timestamp=" + timestamp + "comment={" + formatWithByteArray(format, params) + "}");
            } catch (Exception e) {
                KDKLog.w(tag, "Incorrect format string:" + format, e);
            }
        }
    }

    @Override // com.microsoft.band.service.logger.CargoLogger
    public void traceDiagnostics(String tag, String name, String format, Object... params) {
        if (this.mDiagnosticsFlag) {
            try {
                KDKLog.i(tag, "Diagnostics: tag=" + tag + ", name=" + name + " comment={" + formatWithByteArray(format, params) + "}");
            } catch (Exception e) {
                KDKLog.w(tag, "Incorrect format string:" + format, e);
            }
        }
    }

    private Intent createMessage(long timestamp, String tag, String name, String format, Object... params) {
        return new Intent(LoggerFactory.ACTION_TELEMETRY_MESSAGE).putExtra("event", new LoggerEvent(timestamp, tag, name, format, params));
    }

    public String getName() {
        return this.mLoggerName;
    }

    public void setTelemetryEnabled(boolean flag) {
        this.mTelemetryFlag = flag;
    }

    public void setPerformanceFlag(boolean f) {
        this.mPerformanceFlag = f;
    }

    public void setDiagnosticsFlag(boolean f) {
        this.mDiagnosticsFlag = f;
    }
}
