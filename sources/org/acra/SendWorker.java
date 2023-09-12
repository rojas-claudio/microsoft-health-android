package org.acra;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SendWorker extends Thread {
    private final boolean approvePendingReports;
    private final Context context;
    private final CrashReportFileNameParser fileNameParser = new CrashReportFileNameParser();
    private final List<ReportSender> reportSenders;
    private final boolean sendOnlySilentReports;

    public SendWorker(Context context, List<ReportSender> reportSenders, boolean sendOnlySilentReports, boolean approvePendingReports) {
        this.context = context;
        this.reportSenders = reportSenders;
        this.sendOnlySilentReports = sendOnlySilentReports;
        this.approvePendingReports = approvePendingReports;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (this.approvePendingReports) {
            approvePendingReports();
        }
        checkAndSendReports(this.context, this.sendOnlySilentReports);
    }

    private void approvePendingReports() {
        Log.d(ACRA.LOG_TAG, "Mark all pending reports as approved.");
        CrashReportFinder reportFinder = new CrashReportFinder(this.context);
        String[] reportFileNames = reportFinder.getCrashReportFiles();
        for (String reportFileName : reportFileNames) {
            if (!this.fileNameParser.isApproved(reportFileName)) {
                File reportFile = new File(this.context.getFilesDir(), reportFileName);
                String newName = reportFileName.replace(ACRAConstants.REPORTFILE_EXTENSION, "-approved.stacktrace");
                File newFile = new File(this.context.getFilesDir(), newName);
                if (!reportFile.renameTo(newFile)) {
                    Log.e(ACRA.LOG_TAG, "Could not rename approved report from " + reportFile + " to " + newFile);
                }
            }
        }
    }

    private void checkAndSendReports(Context context, boolean sendOnlySilentReports) {
        Log.d(ACRA.LOG_TAG, "#checkAndSendReports - start");
        CrashReportFinder reportFinder = new CrashReportFinder(context);
        String[] reportFiles = reportFinder.getCrashReportFiles();
        Arrays.sort(reportFiles);
        int reportsSentCount = 0;
        for (String curFileName : reportFiles) {
            if (!sendOnlySilentReports || this.fileNameParser.isSilent(curFileName)) {
                if (reportsSentCount >= 5) {
                    break;
                }
                Log.i(ACRA.LOG_TAG, "Sending file " + curFileName);
                try {
                    CrashReportPersister persister = new CrashReportPersister(context);
                    CrashReportData previousCrashReport = persister.load(curFileName);
                    sendCrashReport(previousCrashReport);
                    deleteFile(context, curFileName);
                } catch (IOException e) {
                    Log.e(ACRA.LOG_TAG, "Failed to load crash report for " + curFileName, e);
                    deleteFile(context, curFileName);
                } catch (RuntimeException e2) {
                    Log.e(ACRA.LOG_TAG, "Failed to send crash reports for " + curFileName, e2);
                    deleteFile(context, curFileName);
                } catch (ReportSenderException e3) {
                    Log.e(ACRA.LOG_TAG, "Failed to send crash report for " + curFileName, e3);
                }
                reportsSentCount++;
            }
        }
        Log.d(ACRA.LOG_TAG, "#checkAndSendReports - finish");
    }

    private void sendCrashReport(CrashReportData errorContent) throws ReportSenderException {
        if (!ACRA.isDebuggable() || ACRA.getConfig().sendReportsInDevMode()) {
            boolean sentAtLeastOnce = false;
            for (ReportSender sender : this.reportSenders) {
                try {
                    sender.send(errorContent);
                    sentAtLeastOnce = true;
                } catch (ReportSenderException e) {
                    if (!sentAtLeastOnce) {
                        throw e;
                    }
                    Log.w(ACRA.LOG_TAG, "ReportSender of class " + sender.getClass().getName() + " failed but other senders completed their task. ACRA will not send this report again.");
                }
            }
        }
    }

    private void deleteFile(Context context, String fileName) {
        boolean deleted = context.deleteFile(fileName);
        if (!deleted) {
            Log.w(ACRA.LOG_TAG, "Could not delete error report : " + fileName);
        }
    }
}
