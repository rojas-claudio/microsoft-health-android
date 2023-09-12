package com.microsoft.kapp;

import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.krestsdk.auth.credentials.AccountMetadata;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import java.util.HashMap;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
/* loaded from: classes.dex */
public class TelemetryCrashReportSender implements ReportSender {
    private CredentialsManager mCredentialsManager;

    public TelemetryCrashReportSender(CredentialsManager credentialsManager) {
        Validate.notNull(credentialsManager, "credentialsManager");
        this.mCredentialsManager = credentialsManager;
    }

    @Override // org.acra.sender.ReportSender
    public void send(CrashReportData crashReportData) throws ReportSenderException {
        HashMap<String, String> properties = new HashMap<>();
        AccountMetadata accountMetadata = this.mCredentialsManager.getAccountMetada();
        if (accountMetadata != null) {
            properties.put(TelemetryConstants.Events.Error.Dimensions.USER_ID, accountMetadata.getUserId());
        }
        properties.put(TelemetryConstants.Events.Error.Dimensions.APP_VERSION, crashReportData.getProperty(ReportField.APP_VERSION_NAME));
        properties.put(TelemetryConstants.Events.Error.Dimensions.MODEL, crashReportData.getProperty(ReportField.PHONE_MODEL));
        properties.put(TelemetryConstants.Events.Error.Dimensions.BRAND, crashReportData.getProperty(ReportField.BRAND));
        properties.put(TelemetryConstants.Events.Error.Dimensions.OS_VERSION, crashReportData.getProperty(ReportField.ANDROID_VERSION));
        properties.put(TelemetryConstants.Events.Error.Dimensions.STACK_TRACE, crashReportData.getProperty(ReportField.STACK_TRACE));
        if (!Compatibility.isPublicRelease()) {
            String crashReportDataString = crashReportData.toString();
            properties.put(TelemetryConstants.Events.Error.Dimensions.EXTRA, crashReportDataString);
        }
        Telemetry.logEvent(TelemetryConstants.Events.Error.APP_CRASHED_EVENT_NAME, properties, null);
    }
}
