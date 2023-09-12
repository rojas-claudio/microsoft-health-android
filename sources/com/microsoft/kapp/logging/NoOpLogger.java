package com.microsoft.kapp.logging;

import com.microsoft.kapp.logging.models.LogEntry;
/* loaded from: classes.dex */
public class NoOpLogger implements Logger {
    @Override // com.microsoft.kapp.logging.Logger
    public void log(LogEntry logentry) {
    }

    @Override // com.microsoft.kapp.logging.Logger
    public void flushAndClose() {
    }

    @Override // com.microsoft.kapp.logging.Logger
    public void packageLogs(String baseDir) {
    }
}
