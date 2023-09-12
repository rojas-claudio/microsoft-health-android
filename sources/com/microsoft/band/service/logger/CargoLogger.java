package com.microsoft.band.service.logger;
/* loaded from: classes.dex */
public interface CargoLogger {

    /* loaded from: classes.dex */
    public interface FinishPoint {
        void perfFinish();

        void perfFinish(String str, Object... objArr);
    }

    boolean isPerfLoggingEnabled();

    boolean isTelemetryEnabled();

    boolean isTraceDiagnosticsEnabled();

    void perfLog(long j, String str, String str2);

    void perfLog(long j, String str, String str2, String str3, Object... objArr);

    void signal(long j, String str, String str2);

    void signal(long j, String str, String str2, String str3, Object... objArr);

    void traceDiagnostics(long j, String str, String str2);

    void traceDiagnostics(long j, String str, String str2, String str3, Object... objArr);

    void traceDiagnostics(String str, String str2, String str3, Object... objArr);
}
