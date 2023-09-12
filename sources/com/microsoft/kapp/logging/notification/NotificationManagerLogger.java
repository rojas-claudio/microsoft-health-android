package com.microsoft.kapp.logging.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.BaseLogger;
import com.microsoft.kapp.logging.models.LogEntry;
import com.microsoft.kapp.logging.models.LogLevel;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class NotificationManagerLogger extends BaseLogger {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String TAG = "NotificationManagerLogger";
    private final boolean mEnabled;
    private NotificationManager mManager;
    private final LogLevel mMinimumLogLevel;
    private AtomicInteger mNotificationCount;

    static {
        $assertionsDisabled = !NotificationManagerLogger.class.desiredAssertionStatus();
    }

    public NotificationManagerLogger(Context context, SettingsProvider settingsProvider) {
        super(context);
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(settingsProvider, "settingsProvider");
        this.mContext = context;
        this.mManager = (NotificationManager) this.mContext.getSystemService("notification");
        this.mNotificationCount = new AtomicInteger(0);
        ((KApplication) context).inject(this);
        this.mMinimumLogLevel = this.mLogConfiguration.getNotificationManagerLoggerMinimumLogLevel();
        this.mEnabled = this.mLogConfiguration.isNotificationManagerLoggerEnabled() && settingsProvider.isNotificationCenterLoggingEnabled();
    }

    @Override // com.microsoft.kapp.logging.BaseLogger, com.microsoft.kapp.logging.Logger
    public void log(LogEntry context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        if (shouldLog(context)) {
            logNotification(context);
        }
    }

    protected boolean shouldLog(LogEntry context) {
        if ($assertionsDisabled || context != null) {
            return this.mEnabled && context.getLevel().isGreaterThanOrEqualTo(this.mMinimumLogLevel);
        }
        throw new AssertionError("context cannot be null");
    }

    protected void logNotification(LogEntry context) {
        String bigText;
        LogLevel logLevel = context.getLevel();
        int notificationId = this.mNotificationCount.addAndGet(1);
        int iconId = 0;
        switch (logLevel) {
            case VERBOSE:
            case INFORMATION:
            case DEBUG:
                iconId = R.drawable.ic_stat_action_about;
                break;
            case ERROR:
            case WARNING:
                iconId = R.drawable.ic_stat_warning;
                break;
        }
        Locale locale = Locale.getDefault();
        Resources resources = this.mContext.getResources();
        Throwable exception = context.getOriginException();
        if (exception == null) {
            String format = resources.getString(R.string.notificationManagerLogger_Message);
            bigText = String.format(locale, format, context.getMessage());
        } else {
            String format2 = resources.getString(R.string.notificationManagerLogger_MessageError);
            bigText = String.format(locale, format2, context.getMessage(), exception.getClass().getSimpleName(), exception.getMessage());
        }
        String title = String.format(locale, resources.getString(R.string.notificationManagerLogger_Title), logLevel.toString(), context.getCategory());
        Intent intent = new Intent(this.mContext, (Class<?>) null);
        NotificationLogEntry entry = new NotificationLogEntry(TAG, notificationId, context);
        intent.putExtra(Constants.NOTIFICATION_MANAGER_LOGGER_ENTRY, entry);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.mContext, notificationId, intent, 268435456);
        Notification notification = new NotificationCompat.Builder(this.mContext).setContentTitle(title).setContentText(context.getMessage()).setStyle(new NotificationCompat.BigTextStyle().bigText(bigText)).setTicker(title).setSmallIcon(iconId).setContentIntent(pendingIntent).build();
        this.mManager.notify(TAG, notificationId, notification);
    }

    @Override // com.microsoft.kapp.logging.Logger
    public void packageLogs(String baseDir) {
    }
}
