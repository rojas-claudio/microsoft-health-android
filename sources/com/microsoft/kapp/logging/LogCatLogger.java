package com.microsoft.kapp.logging;

import android.content.Context;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.models.LogEntry;
import com.microsoft.kapp.logging.models.LogLevel;
import com.microsoft.kapp.logging.models.LogMode;
import com.microsoft.kapp.utils.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
/* loaded from: classes.dex */
public class LogCatLogger extends BaseLogger {
    private final LogLevel mMinimumLogLevel;
    private LogCatProxy mProxy;

    public LogCatLogger(Context context, LogCatProxy proxy) {
        super(context);
        Validate.notNull(proxy, "proxy");
        this.mMinimumLogLevel = this.mLogConfiguration.getLogCatLoggerMinimumLogLevel();
        this.mProxy = proxy;
    }

    @Override // com.microsoft.kapp.logging.BaseLogger, com.microsoft.kapp.logging.Logger
    public void log(LogEntry entry) {
        Validate.notNull(entry, "entry");
        if (this.mLogConfiguration.getLogMode() != LogMode.DO_NOT_LOG_PRIVATE_DATA) {
            LogLevel logLevel = entry.getLevel();
            if (logLevel.isGreaterThanOrEqualTo(this.mMinimumLogLevel)) {
                switch (logLevel) {
                    case DEBUG:
                        this.mProxy.d(entry.getCategory(), entry.getMessage(), entry.getOriginException());
                        return;
                    case ERROR:
                        this.mProxy.e(entry.getCategory(), entry.getMessage(), entry.getOriginException());
                        return;
                    case INFORMATION:
                        this.mProxy.i(entry.getCategory(), entry.getMessage(), entry.getOriginException());
                        return;
                    case VERBOSE:
                        this.mProxy.v(entry.getCategory(), entry.getMessage(), entry.getOriginException());
                        return;
                    case WARNING:
                        this.mProxy.w(entry.getCategory(), entry.getMessage(), entry.getOriginException());
                        return;
                    default:
                        return;
                }
            }
        }
    }

    @Override // com.microsoft.kapp.logging.Logger
    public void packageLogs(String baseDir) {
        if (this.mLogConfiguration.getLogMode() == LogMode.CAN_LOG_PRIVATE_DATA) {
            OutputStream packageOut = null;
            try {
                File outputOtherDir = new File(baseDir, LogConstants.OTHER_LOGS_FOLDER);
                outputOtherDir.mkdirs();
                File outputFile = new File(outputOtherDir, LogConstants.LOGCAT_FILE_NAME);
                OutputStream packageOut2 = new FileOutputStream(outputFile, false);
                try {
                    Process process = Runtime.getRuntime().exec("logcat -d");
                    FileUtils.copyStream(process.getInputStream(), packageOut2);
                    StreamUtils.closeQuietly(packageOut2);
                } catch (Exception e) {
                    packageOut = packageOut2;
                    StreamUtils.closeQuietly(packageOut);
                } catch (Throwable th) {
                    th = th;
                    packageOut = packageOut2;
                    StreamUtils.closeQuietly(packageOut);
                    throw th;
                }
            } catch (Exception e2) {
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }
}
