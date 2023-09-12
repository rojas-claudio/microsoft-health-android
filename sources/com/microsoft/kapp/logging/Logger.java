package com.microsoft.kapp.logging;

import com.microsoft.kapp.logging.models.LogEntry;
/* loaded from: classes.dex */
public interface Logger {
    void flushAndClose();

    void log(LogEntry logEntry);

    void packageLogs(String str);
}
