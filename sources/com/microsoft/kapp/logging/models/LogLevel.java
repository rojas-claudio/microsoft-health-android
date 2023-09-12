package com.microsoft.kapp.logging.models;

import android.util.SparseArray;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public enum LogLevel {
    VERBOSE(2),
    DEBUG(3),
    INFORMATION(4),
    WARNING(5),
    ERROR(6);
    
    private static final SparseArray<LogLevel> mMapping = new SparseArray<>();
    private final int mLevel;

    static {
        LogLevel[] arr$ = values();
        for (LogLevel logLevel : arr$) {
            mMapping.put(logLevel.value(), logLevel);
        }
    }

    LogLevel(int level) {
        validate(level);
        this.mLevel = level;
    }

    public static LogLevel valueOf(int value) {
        validate(value);
        return mMapping.get(value);
    }

    public boolean isGreaterThanOrEqualTo(LogLevel logLevel) {
        return this.mLevel >= logLevel.mLevel;
    }

    public int value() {
        return this.mLevel;
    }

    @Override // java.lang.Enum
    public String toString() {
        switch (this) {
            case VERBOSE:
                return "Verbose";
            case DEBUG:
                return TelemetryConstants.Events.ShakeDialog.Dimensions.DEBUG;
            case INFORMATION:
                return "Info";
            case WARNING:
                return "Warning";
            case ERROR:
                return TelemetryConstants.Events.Error.Dimensions.LOG_TYPE_ERROR;
            default:
                return "Unknown";
        }
    }

    private static void validate(int value) {
        Validate.isTrue(value >= 2 && value <= 6, "value must be greater than or equal to %d and less than or equal to %d", 2, 6);
    }
}
