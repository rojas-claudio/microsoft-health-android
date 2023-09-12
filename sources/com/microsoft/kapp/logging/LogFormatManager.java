package com.microsoft.kapp.logging;

import com.microsoft.kapp.logging.models.LogEntry;
import com.microsoft.kapp.logging.models.LogEntryType;
/* loaded from: classes.dex */
public interface LogFormatManager {
    void flushAndClose();

    void log(LogContext logContext);

    void log(LogEntry logEntry);

    void packageLog(String str, LogEntryType logEntryType);
}
