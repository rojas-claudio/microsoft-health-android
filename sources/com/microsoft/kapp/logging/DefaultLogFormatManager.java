package com.microsoft.kapp.logging;

import android.content.Context;
import com.microsoft.kapp.logging.models.LogEntry;
import com.microsoft.kapp.logging.models.LogEntryContext;
import com.microsoft.kapp.logging.models.LogEntryException;
import com.microsoft.kapp.logging.models.LogEntryType;
import com.microsoft.kapp.utils.Constants;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class DefaultLogFormatManager implements LogFormatManager {
    private LogConfiguration mLogConfiguration;
    private LogFormatManager mLogFormatManager;
    private LogListProvider mLogListProvider;

    public DefaultLogFormatManager(Context context, LogListProvider logListProvider, LogConfiguration logConfiguration) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO, new Object[0]);
        Validate.notNull(logListProvider, "logListProvider", new Object[0]);
        Validate.notNull(logConfiguration, "logConfiguration", new Object[0]);
        this.mLogListProvider = logListProvider;
        this.mLogConfiguration = logConfiguration;
    }

    @Override // com.microsoft.kapp.logging.LogFormatManager
    public void log(LogContext logContext) {
        LogEntry entry = convertLogContext(logContext);
        log(entry);
    }

    @Override // com.microsoft.kapp.logging.LogFormatManager
    public void log(LogEntry entry) {
        Validate.notNull(entry, "logentry", new Object[0]);
        switch (entry.getType()) {
            case MESSAGE:
                if (entry.getLevel().isGreaterThanOrEqualTo(this.mLogConfiguration.getMessageLoggerMinimumLogLevel())) {
                    logMultiple(this.mLogListProvider.provideLoggers(LogEntryType.MESSAGE), entry);
                    return;
                }
                return;
            case IMAGE:
                logMultiple(this.mLogListProvider.provideLoggers(LogEntryType.IMAGE), entry);
                return;
            default:
                return;
        }
    }

    private void logMultiple(List<Logger> loggerList, LogEntry log) {
        if (loggerList != null) {
            for (Logger logger : loggerList) {
                logger.log(log);
            }
        }
    }

    @Override // com.microsoft.kapp.logging.LogFormatManager
    public void packageLog(String baseDir, LogEntryType type) {
        switch (type) {
            case MESSAGE:
                packageMultiple(this.mLogListProvider.provideLoggers(LogEntryType.MESSAGE), baseDir);
                return;
            case IMAGE:
                packageMultiple(this.mLogListProvider.provideLoggers(LogEntryType.IMAGE), baseDir);
                return;
            default:
                return;
        }
    }

    private void packageMultiple(List<Logger> loggerList, String baseDir) {
        if (loggerList != null) {
            for (Logger logger : loggerList) {
                logger.packageLogs(baseDir);
            }
        }
    }

    private LogEntry convertLogContext(LogContext logContext) {
        LogEntry entry = new LogEntry();
        entry.setCategory(logContext.getTag());
        entry.setFileReference(null);
        entry.setLevel(logContext.getLogLevel());
        entry.setMessage(logContext.getMessage());
        entry.setTime(new DateTime(logContext.getTimestamp()));
        entry.setOriginException(logContext.getException());
        Throwable exception = logContext.getException();
        if (exception != null) {
            LogEntryException leException = new LogEntryException();
            leException.setMessage(exception.getMessage());
            leException.setType(exception.getClass().getSimpleName());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            leException.setInfo(sw.toString());
            entry.setException(leException);
            if (exception.getStackTrace() != null && exception.getStackTrace().length > 0) {
                LogEntryContext leContext = new LogEntryContext();
                StackTraceElement traceElement = exception.getStackTrace()[0];
                leContext.setFile(traceElement.getFileName());
                leContext.setLine(traceElement.getLineNumber());
                leContext.setMethod(traceElement.getMethodName());
                entry.setContext(leContext);
            }
        }
        if (logContext.getImage() != null) {
            entry.setType(LogEntryType.IMAGE);
            entry.setImage(logContext.getImage());
        } else {
            entry.setType(LogEntryType.MESSAGE);
        }
        return entry;
    }

    @Override // com.microsoft.kapp.logging.LogFormatManager
    public void flushAndClose() {
        LogEntryType[] arr$ = LogEntryType.values();
        for (LogEntryType logType : arr$) {
            flushAndCloseLoggers(this.mLogListProvider.provideLoggers(logType));
        }
    }

    private void flushAndCloseLoggers(List<Logger> loggerList) {
        if (loggerList != null) {
            for (Logger logger : loggerList) {
                logger.flushAndClose();
            }
        }
    }
}
