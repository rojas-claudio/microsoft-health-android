package com.microsoft.kapp.logging;

import com.microsoft.kapp.debug.KappConfig;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.logging.models.LogLevel;
import com.microsoft.kapp.logging.models.LogMode;
import com.microsoft.kapp.services.SettingsProvider;
/* loaded from: classes.dex */
public class LogConfiguration {
    private SettingsProvider mSettingsProvider;

    public LogConfiguration(SettingsProvider settingsProvider) {
        this.mSettingsProvider = settingsProvider;
    }

    public LogLevel getLogCatLoggerMinimumLogLevel() {
        return !KappConfig.isDebbuging ? LogLevel.WARNING : LogLevel.VERBOSE;
    }

    public LogLevel getNotificationManagerLoggerMinimumLogLevel() {
        return LogLevel.WARNING;
    }

    public boolean isNotificationManagerLoggerEnabled() {
        return KappConfig.isDebbuging;
    }

    public LogMode getLogMode() {
        return (this.mSettingsProvider.shouldEmulatePublicReleaseForLogging() || Compatibility.isPublicRelease()) ? LogMode.DO_NOT_LOG_PRIVATE_DATA : LogMode.CAN_LOG_PRIVATE_DATA;
    }

    public LogLevel getMessageLoggerMinimumLogLevel() {
        return Compatibility.isPublicRelease() ? LogLevel.DEBUG : LogLevel.VERBOSE;
    }
}
