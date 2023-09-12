package com.microsoft.kapp.logging;

import android.content.Context;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.logging.fileLogger.RollingFileLogger;
import com.microsoft.kapp.logging.images.ImageLogger;
import com.microsoft.kapp.logging.models.LogEntryType;
import com.microsoft.kapp.logging.notification.NotificationManagerLogger;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class DefaultLogListProvider implements LogListProvider {
    private Context mContext;
    private CredentialsManager mCredentialsManager;
    private Logger mImageLogger;
    private LogConfiguration mLogConfiguration;
    private final Logger mLogcatLogger;
    private final Logger mMessageFileLogger;
    private final Logger mNotificationManagerLogger;
    private SettingsProvider mSettingsProvider;

    public DefaultLogListProvider(Context context, CredentialsManager credentialsManager, SettingsProvider settingsProvider, LogConfiguration logFormatManager) {
        this.mContext = context;
        this.mCredentialsManager = credentialsManager;
        this.mLogConfiguration = logFormatManager;
        this.mSettingsProvider = settingsProvider;
        this.mLogcatLogger = new LogCatLogger(this.mContext, new LogCatProxy());
        this.mNotificationManagerLogger = new NotificationManagerLogger(this.mContext, this.mSettingsProvider);
        this.mMessageFileLogger = new RollingFileLogger(this.mContext, this.mSettingsProvider, LogConstants.MESSAGE_LOGS_FOLDER);
        this.mImageLogger = new ImageLogger(this.mContext, this.mMessageFileLogger);
    }

    @Override // com.microsoft.kapp.logging.LogListProvider
    public List<Logger> provideLoggers(LogEntryType type) {
        switch (type) {
            case MESSAGE:
                return Compatibility.isPublicRelease() ? Arrays.asList(this.mMessageFileLogger) : Arrays.asList(this.mLogcatLogger, this.mNotificationManagerLogger, this.mMessageFileLogger);
            case IMAGE:
                return Arrays.asList(this.mImageLogger);
            default:
                return null;
        }
    }
}
