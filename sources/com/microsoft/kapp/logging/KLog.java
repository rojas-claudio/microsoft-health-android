package com.microsoft.kapp.logging;

import android.graphics.Bitmap;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.LogContext;
import com.microsoft.kapp.logging.models.LogLevel;
import com.microsoft.kapp.logging.models.LogMode;
import com.microsoft.kapp.utils.SettingsUtils;
import java.util.Locale;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class KLog {
    @Inject
    static LogConfiguration mLogConfiguration;
    @Inject
    static LogFormatManager mLogFormatManager;

    private KLog() {
    }

    public static void d(String tag, String message) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.DEBUG, tag, message, null));
        } catch (Exception e) {
        }
    }

    public static void d(String tag, String message, Throwable exception) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.DEBUG, tag, message, exception));
        } catch (Exception e) {
        }
    }

    public static void d(String tag, String format, Object... args) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.DEBUG, tag, formatString(format, args), null));
        } catch (Exception e) {
        }
    }

    public static void e(String tag, String message) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.ERROR, tag, message, null));
        } catch (Exception e) {
        }
    }

    public static void e(String tag, String message, Throwable exception) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.ERROR, tag, message, exception));
        } catch (Exception e) {
        }
    }

    public static void e(String tag, String format, Object... args) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.ERROR, tag, formatString(format, args), null));
        } catch (Exception e) {
        }
    }

    public static void i(String tag, String message) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.INFORMATION, tag, message, null));
        } catch (Exception e) {
        }
    }

    public static void i(String tag, String message, Throwable exception) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.INFORMATION, tag, message, exception));
        } catch (Exception e) {
        }
    }

    public static void i(String tag, String format, Object... args) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.INFORMATION, tag, formatString(format, args), null));
        } catch (Exception e) {
        }
    }

    public static void v(String tag, String message) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.VERBOSE, tag, message, null));
        } catch (Exception e) {
        }
    }

    public static void v(String tag, String message, Throwable exception) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.VERBOSE, tag, message, exception));
        } catch (Exception e) {
        }
    }

    public static void v(String tag, String format, Object... args) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.VERBOSE, tag, formatString(format, args), null));
        } catch (Exception e) {
        }
    }

    public static void logPrivate(String tag, String message) {
        if (mLogConfiguration.getLogMode().equals(LogMode.CAN_LOG_PRIVATE_DATA)) {
            try {
                mLogFormatManager.log(buildLogContext(LogLevel.VERBOSE, tag, message, null));
            } catch (Exception e) {
            }
        }
    }

    public static void logPrivate(String tag, String message, Throwable exception) {
        if (mLogConfiguration.getLogMode().equals(LogMode.CAN_LOG_PRIVATE_DATA)) {
            try {
                mLogFormatManager.log(buildLogContext(LogLevel.VERBOSE, tag, message, exception));
            } catch (Exception e) {
            }
        }
    }

    public static void logPrivate(String tag, String format, Object... args) {
        if (mLogConfiguration.getLogMode().equals(LogMode.CAN_LOG_PRIVATE_DATA)) {
            try {
                mLogFormatManager.log(buildLogContext(LogLevel.VERBOSE, tag, formatString(format, args), null));
            } catch (Exception e) {
            }
        }
    }

    public static void w(String tag, String message) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.WARNING, tag, message, null));
        } catch (Exception e) {
        }
    }

    public static void w(String tag, String message, Throwable exception) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.WARNING, tag, message, exception));
        } catch (Exception e) {
        }
    }

    public static void w(String tag, String format, Object... args) {
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.WARNING, tag, formatString(format, args), null));
        } catch (Exception e) {
        }
    }

    public static void image(String tag, Bitmap image, String message) {
        SettingsUtils.throwExceptionIfOnMainThread();
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.VERBOSE, tag, image, message, null));
        } catch (Exception e) {
        }
    }

    public static void image(String tag, Bitmap image, String message, Throwable exception) {
        SettingsUtils.throwExceptionIfOnMainThread();
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.VERBOSE, tag, image, message, exception));
        } catch (Exception e) {
        }
    }

    public static void image(String tag, Bitmap image, String format, Object... args) {
        SettingsUtils.throwExceptionIfOnMainThread();
        try {
            mLogFormatManager.log(buildLogContext(LogLevel.VERBOSE, tag, image, formatString(format, args), null));
        } catch (Exception e) {
        }
    }

    public static void flushAndClose() {
        try {
            mLogFormatManager.flushAndClose();
        } catch (Exception e) {
        }
    }

    private static LogContext buildLogContext(LogLevel logLevel, String tag, String message, Throwable exception) {
        return buildLogContext(logLevel, tag, null, message, exception);
    }

    private static LogContext buildLogContext(LogLevel logLevel, String tag, Bitmap image, String message, Throwable exception) {
        LogContext.Builder builder = new LogContext.Builder();
        builder.setLogLevel(logLevel);
        builder.setTag(tag);
        builder.setImage(image);
        builder.setMessage(message);
        builder.setException(exception);
        return builder.build();
    }

    private static String formatString(String format, Object... args) {
        Validate.notNullOrEmpty(format, "format");
        return String.format(Locale.getDefault(), format, args);
    }
}
