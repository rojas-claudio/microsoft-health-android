package com.microsoft.kapp.logging.notification;

import android.content.Context;
import com.microsoft.kapp.logging.models.LogEntry;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class NotificationManagerTagLogger extends NotificationManagerLogger {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NotificationManagerTagLogger.class.desiredAssertionStatus();
    }

    public NotificationManagerTagLogger(Context context, SettingsProvider settingsProvider) {
        super(context, settingsProvider);
    }

    @Override // com.microsoft.kapp.logging.notification.NotificationManagerLogger
    protected boolean shouldLog(LogEntry context) {
        if ($assertionsDisabled || context != null) {
            String tag = context.getCategory();
            return tag != null && tag.equalsIgnoreCase(Constants.ALWAYS_LOG_TAG);
        }
        throw new AssertionError("context cannot be null");
    }
}
