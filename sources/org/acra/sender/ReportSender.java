package org.acra.sender;

import org.acra.collector.CrashReportData;
/* loaded from: classes.dex */
public interface ReportSender {
    void send(CrashReportData crashReportData) throws ReportSenderException;
}
