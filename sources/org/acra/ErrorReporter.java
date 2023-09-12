package org.acra;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.Process;
import android.text.format.Time;
import android.util.Log;
import java.io.File;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.acra.annotation.ReportsCrashes;
import org.acra.collector.Compatibility;
import org.acra.collector.ConfigurationCollector;
import org.acra.collector.CrashReportData;
import org.acra.collector.CrashReportDataFactory;
import org.acra.jraf.android.util.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksCompat;
import org.acra.jraf.android.util.activitylifecyclecallbackscompat.ApplicationHelper;
import org.acra.sender.EmailIntentSender;
import org.acra.sender.GoogleFormSender;
import org.acra.sender.HttpSender;
import org.acra.sender.ReportSender;
import org.acra.util.PackageManagerWrapper;
import org.acra.util.ToastSender;
/* loaded from: classes.dex */
public class ErrorReporter implements Thread.UncaughtExceptionHandler {
    private Thread brokenThread;
    private final CrashReportDataFactory crashReportDataFactory;
    private boolean enabled;
    private transient Activity lastActivityCreated;
    private final Application mContext;
    private final Thread.UncaughtExceptionHandler mDfltExceptionHandler;
    private final SharedPreferences prefs;
    private Throwable unhandledThrowable;
    private static boolean toastWaitEnded = true;
    private static int mNotificationCounter = 0;
    private final List<ReportSender> mReportSenders = new ArrayList();
    private final CrashReportFileNameParser fileNameParser = new CrashReportFileNameParser();

    /* JADX INFO: Access modifiers changed from: package-private */
    public ErrorReporter(Application context, SharedPreferences prefs, boolean enabled) {
        this.enabled = false;
        this.mContext = context;
        this.prefs = prefs;
        this.enabled = enabled;
        String initialConfiguration = ConfigurationCollector.collectConfiguration(this.mContext);
        Time appStartDate = new Time();
        appStartDate.setToNow();
        if (Compatibility.getAPILevel() >= 14) {
            ApplicationHelper.registerActivityLifecycleCallbacks(context, new ActivityLifecycleCallbacksCompat() { // from class: org.acra.ErrorReporter.1
                @Override // org.acra.jraf.android.util.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksCompat
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    if (!(activity instanceof CrashReportDialog)) {
                        ErrorReporter.this.lastActivityCreated = activity;
                    }
                }

                @Override // org.acra.jraf.android.util.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksCompat
                public void onActivityStarted(Activity activity) {
                }

                @Override // org.acra.jraf.android.util.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksCompat
                public void onActivityResumed(Activity activity) {
                }

                @Override // org.acra.jraf.android.util.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksCompat
                public void onActivityPaused(Activity activity) {
                }

                @Override // org.acra.jraf.android.util.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksCompat
                public void onActivityStopped(Activity activity) {
                }

                @Override // org.acra.jraf.android.util.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksCompat
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }

                @Override // org.acra.jraf.android.util.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksCompat
                public void onActivityDestroyed(Activity activity) {
                }
            });
        }
        this.crashReportDataFactory = new CrashReportDataFactory(this.mContext, prefs, appStartDate, initialConfiguration);
        this.mDfltExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        checkReportsOnApplicationStart();
    }

    public static ErrorReporter getInstance() {
        return ACRA.getErrorReporter();
    }

    @Deprecated
    public void addCustomData(String key, String value) {
        this.crashReportDataFactory.putCustomData(key, value);
    }

    public String putCustomData(String key, String value) {
        return this.crashReportDataFactory.putCustomData(key, value);
    }

    public String removeCustomData(String key) {
        return this.crashReportDataFactory.removeCustomData(key);
    }

    public String getCustomData(String key) {
        return this.crashReportDataFactory.getCustomData(key);
    }

    public void addReportSender(ReportSender sender) {
        this.mReportSenders.add(sender);
    }

    public void removeReportSender(ReportSender sender) {
        this.mReportSenders.remove(sender);
    }

    public void removeReportSenders(Class<?> senderClass) {
        if (ReportSender.class.isAssignableFrom(senderClass)) {
            for (ReportSender sender : this.mReportSenders) {
                if (senderClass.isInstance(sender)) {
                    this.mReportSenders.remove(sender);
                }
            }
        }
    }

    public void removeAllReportSenders() {
        this.mReportSenders.clear();
    }

    public void setReportSender(ReportSender sender) {
        removeAllReportSenders();
        addReportSender(sender);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread t, Throwable e) {
        try {
            if (!this.enabled) {
                if (this.mDfltExceptionHandler != null) {
                    Log.e(ACRA.LOG_TAG, "ACRA is disabled for " + this.mContext.getPackageName() + " - forwarding uncaught Exception on to default ExceptionHandler");
                    this.mDfltExceptionHandler.uncaughtException(t, e);
                } else {
                    Log.e(ACRA.LOG_TAG, "ACRA is disabled for " + this.mContext.getPackageName() + " - no default ExceptionHandler");
                }
            } else {
                this.brokenThread = t;
                this.unhandledThrowable = e;
                Log.e(ACRA.LOG_TAG, "ACRA caught a " + e.getClass().getSimpleName() + " exception for " + this.mContext.getPackageName() + ". Building report.");
                handleException(e, ACRA.getConfig().mode(), false, true);
            }
        } catch (Throwable th) {
            if (this.mDfltExceptionHandler != null) {
                this.mDfltExceptionHandler.uncaughtException(t, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endApplication() {
        if (ACRA.getConfig().mode() == ReportingInteractionMode.SILENT || (ACRA.getConfig().mode() == ReportingInteractionMode.TOAST && ACRA.getConfig().forceCloseDialogAfterToast())) {
            this.mDfltExceptionHandler.uncaughtException(this.brokenThread, this.unhandledThrowable);
            return;
        }
        Log.e(ACRA.LOG_TAG, this.mContext.getPackageName() + " fatal error : " + this.unhandledThrowable.getMessage(), this.unhandledThrowable);
        if (this.lastActivityCreated != null) {
            Log.i(ACRA.LOG_TAG, "Finishing the last Activity prior to killing the Process");
            this.lastActivityCreated.finish();
            Log.i(ACRA.LOG_TAG, "Finished " + this.lastActivityCreated.getClass());
            this.lastActivityCreated = null;
        }
        Process.killProcess(Process.myPid());
        System.exit(10);
    }

    public void handleSilentException(Throwable e) {
        if (this.enabled) {
            handleException(e, ReportingInteractionMode.SILENT, true, false);
            Log.d(ACRA.LOG_TAG, "ACRA sent Silent report.");
            return;
        }
        Log.d(ACRA.LOG_TAG, "ACRA is disabled. Silent report not sent.");
    }

    public void setEnabled(boolean enabled) {
        Log.i(ACRA.LOG_TAG, "ACRA is " + (enabled ? "enabled" : "disabled") + " for " + this.mContext.getPackageName());
        this.enabled = enabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SendWorker startSendingReports(boolean onlySendSilentReports, boolean approveReportsFirst) {
        SendWorker worker = new SendWorker(this.mContext, this.mReportSenders, onlySendSilentReports, approveReportsFirst);
        worker.start();
        return worker;
    }

    void deletePendingReports() {
        deletePendingReports(true, true, 0);
    }

    public void checkReportsOnApplicationStart() {
        long lastVersionNr = this.prefs.getInt(ACRA.PREF_LAST_VERSION_NR, 0);
        PackageManagerWrapper packageManagerWrapper = new PackageManagerWrapper(this.mContext);
        PackageInfo packageInfo = packageManagerWrapper.getPackageInfo();
        boolean newVersion = packageInfo != null && ((long) packageInfo.versionCode) > lastVersionNr;
        if (newVersion) {
            if (ACRA.getConfig().deleteOldUnsentReportsOnApplicationStart()) {
                deletePendingReports();
            }
            SharedPreferences.Editor prefsEditor = this.prefs.edit();
            prefsEditor.putInt(ACRA.PREF_LAST_VERSION_NR, packageInfo.versionCode);
            prefsEditor.commit();
        }
        if ((ACRA.getConfig().mode() == ReportingInteractionMode.NOTIFICATION || ACRA.getConfig().mode() == ReportingInteractionMode.DIALOG) && ACRA.getConfig().deleteUnapprovedReportsOnApplicationStart()) {
            deletePendingNonApprovedReports(true);
        }
        CrashReportFinder reportFinder = new CrashReportFinder(this.mContext);
        String[] filesList = reportFinder.getCrashReportFiles();
        if (filesList != null && filesList.length > 0) {
            ReportingInteractionMode reportingInteractionMode = ACRA.getConfig().mode();
            String[] filesList2 = reportFinder.getCrashReportFiles();
            boolean onlySilentOrApprovedReports = containsOnlySilentOrApprovedReports(filesList2);
            if (reportingInteractionMode == ReportingInteractionMode.SILENT || reportingInteractionMode == ReportingInteractionMode.TOAST || (onlySilentOrApprovedReports && (reportingInteractionMode == ReportingInteractionMode.NOTIFICATION || reportingInteractionMode == ReportingInteractionMode.DIALOG))) {
                if (reportingInteractionMode == ReportingInteractionMode.TOAST && !onlySilentOrApprovedReports) {
                    ToastSender.sendToast(this.mContext, ACRA.getConfig().resToastText(), 1);
                }
                Log.v(ACRA.LOG_TAG, "About to start ReportSenderWorker from #checkReportOnApplicationStart");
                startSendingReports(false, false);
            } else if (ACRA.getConfig().mode() == ReportingInteractionMode.NOTIFICATION) {
                notifySendReport(getLatestNonSilentReport(filesList2));
            } else {
                if (ACRA.getConfig().mode() == ReportingInteractionMode.DIALOG) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deletePendingNonApprovedReports(boolean keepOne) {
        int nbReportsToKeep = keepOne ? 1 : 0;
        deletePendingReports(false, true, nbReportsToKeep);
    }

    public void handleException(Throwable e, boolean endApplication) {
        handleException(e, ACRA.getConfig().mode(), false, endApplication);
    }

    public void handleException(Throwable e) {
        handleException(e, ACRA.getConfig().mode(), false, false);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [org.acra.ErrorReporter$4] */
    /* JADX WARN: Type inference failed for: r1v12, types: [org.acra.ErrorReporter$3] */
    /* JADX WARN: Type inference failed for: r1v18, types: [org.acra.ErrorReporter$2] */
    private void handleException(Throwable e, ReportingInteractionMode reportingInteractionMode, boolean forceSilentReport, final boolean endApplication) {
        final boolean showDirectDialog = true;
        if (this.enabled) {
            boolean sendOnlySilentReports = false;
            if (reportingInteractionMode == null) {
                reportingInteractionMode = ACRA.getConfig().mode();
            } else if (reportingInteractionMode == ReportingInteractionMode.SILENT && ACRA.getConfig().mode() != ReportingInteractionMode.SILENT) {
                sendOnlySilentReports = true;
            }
            if (e == null) {
                e = new Exception("Report requested by developer");
            }
            boolean shouldDisplayToast = reportingInteractionMode == ReportingInteractionMode.TOAST || (ACRA.getConfig().resToastText() != 0 && (reportingInteractionMode == ReportingInteractionMode.NOTIFICATION || reportingInteractionMode == ReportingInteractionMode.DIALOG));
            if (shouldDisplayToast) {
                new Thread() { // from class: org.acra.ErrorReporter.2
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        Looper.prepare();
                        ToastSender.sendToast(ErrorReporter.this.mContext, ACRA.getConfig().resToastText(), 1);
                        Looper.loop();
                    }
                }.start();
            }
            CrashReportData crashReportData = this.crashReportDataFactory.createCrashData(e, forceSilentReport, this.brokenThread);
            final String reportFileName = getReportFileName(crashReportData);
            saveCrashReportFile(reportFileName, crashReportData);
            SendWorker sender = null;
            if (reportingInteractionMode == ReportingInteractionMode.SILENT || reportingInteractionMode == ReportingInteractionMode.TOAST || this.prefs.getBoolean(ACRA.PREF_ALWAYS_ACCEPT, false)) {
                Log.d(ACRA.LOG_TAG, "About to start ReportSenderWorker from #handleException");
                sender = startSendingReports(sendOnlySilentReports, true);
            } else if (reportingInteractionMode == ReportingInteractionMode.NOTIFICATION) {
                Log.d(ACRA.LOG_TAG, "Notification will be created on application start.");
            }
            if (shouldDisplayToast) {
                toastWaitEnded = false;
                new Thread() { // from class: org.acra.ErrorReporter.3
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        Time beforeWait = new Time();
                        Time currentTime = new Time();
                        beforeWait.setToNow();
                        long beforeWaitInMillis = beforeWait.toMillis(false);
                        for (long elapsedTimeInMillis = 0; elapsedTimeInMillis < 3000; elapsedTimeInMillis = currentTime.toMillis(false) - beforeWaitInMillis) {
                            try {
                                Thread.sleep(3000L);
                            } catch (InterruptedException e1) {
                                Log.d(ACRA.LOG_TAG, "Interrupted while waiting for Toast to end.", e1);
                            }
                            currentTime.setToNow();
                        }
                        boolean unused = ErrorReporter.toastWaitEnded = true;
                    }
                }.start();
            }
            final SendWorker worker = sender;
            if (reportingInteractionMode != ReportingInteractionMode.DIALOG || this.prefs.getBoolean(ACRA.PREF_ALWAYS_ACCEPT, false)) {
                showDirectDialog = false;
            }
            new Thread() { // from class: org.acra.ErrorReporter.4
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    Log.d(ACRA.LOG_TAG, "Waiting for Toast + worker...");
                    while (true) {
                        if (!ErrorReporter.toastWaitEnded || (worker != null && worker.isAlive())) {
                            try {
                                Thread.sleep(100L);
                            } catch (InterruptedException e1) {
                                Log.e(ACRA.LOG_TAG, "Error : ", e1);
                            }
                        }
                    }
                    if (showDirectDialog) {
                        Log.d(ACRA.LOG_TAG, "About to create DIALOG from #handleException");
                        ErrorReporter.this.notifyDialog(reportFileName);
                    }
                    Log.d(ACRA.LOG_TAG, "Wait for Toast + worker ended. Kill Application ? " + endApplication);
                    if (endApplication) {
                        ErrorReporter.this.endApplication();
                    }
                }
            }.start();
        }
    }

    void notifyDialog(String reportFileName) {
        Log.d(ACRA.LOG_TAG, "Creating Dialog for " + reportFileName);
        Intent dialogIntent = new Intent(this.mContext, CrashReportDialog.class);
        dialogIntent.putExtra("REPORT_FILE_NAME", reportFileName);
        dialogIntent.setFlags(268435456);
        this.mContext.startActivity(dialogIntent);
    }

    private void notifySendReport(String reportFileName) {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        ReportsCrashes conf = ACRA.getConfig();
        int icon = conf.resNotifIcon();
        CharSequence tickerText = this.mContext.getText(conf.resNotifTickerText());
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        CharSequence contentTitle = this.mContext.getText(conf.resNotifTitle());
        CharSequence contentText = this.mContext.getText(conf.resNotifText());
        Intent notificationIntent = new Intent(this.mContext, CrashReportDialog.class);
        Log.d(ACRA.LOG_TAG, "Creating Notification for " + reportFileName);
        notificationIntent.putExtra("REPORT_FILE_NAME", reportFileName);
        Application application = this.mContext;
        int i = mNotificationCounter;
        mNotificationCounter = i + 1;
        PendingIntent contentIntent = PendingIntent.getActivity(application, i, notificationIntent, 134217728);
        notification.setLatestEventInfo(this.mContext, contentTitle, contentText, contentIntent);
        Intent deleteIntent = new Intent(this.mContext, CrashReportDialog.class);
        deleteIntent.putExtra("FORCE_CANCEL", true);
        PendingIntent pendingDeleteIntent = PendingIntent.getActivity(this.mContext, -1, deleteIntent, 0);
        notification.deleteIntent = pendingDeleteIntent;
        notificationManager.notify(666, notification);
    }

    private String getReportFileName(CrashReportData crashData) {
        Time now = new Time();
        now.setToNow();
        long timestamp = now.toMillis(false);
        String isSilent = crashData.getProperty(ReportField.IS_SILENT);
        return "" + timestamp + (isSilent != null ? ACRAConstants.SILENT_SUFFIX : "") + ACRAConstants.REPORTFILE_EXTENSION;
    }

    private void saveCrashReportFile(String fileName, CrashReportData crashData) {
        try {
            Log.d(ACRA.LOG_TAG, "Writing crash report file " + fileName + ".");
            CrashReportPersister persister = new CrashReportPersister(this.mContext);
            persister.store(crashData, fileName);
        } catch (Exception e) {
            Log.e(ACRA.LOG_TAG, "An error occurred while writing the report file...", e);
        }
    }

    private String getLatestNonSilentReport(String[] filesList) {
        if (filesList != null && filesList.length > 0) {
            for (int i = filesList.length - 1; i >= 0; i--) {
                if (!this.fileNameParser.isSilent(filesList[i])) {
                    return filesList[i];
                }
            }
            return filesList[filesList.length - 1];
        }
        return null;
    }

    private void deletePendingReports(boolean deleteApprovedReports, boolean deleteNonApprovedReports, int nbOfLatestToKeep) {
        CrashReportFinder reportFinder = new CrashReportFinder(this.mContext);
        String[] filesList = reportFinder.getCrashReportFiles();
        Arrays.sort(filesList);
        if (filesList != null) {
            for (int iFile = 0; iFile < filesList.length - nbOfLatestToKeep; iFile++) {
                String fileName = filesList[iFile];
                boolean isReportApproved = this.fileNameParser.isApproved(fileName);
                if ((isReportApproved && deleteApprovedReports) || (!isReportApproved && deleteNonApprovedReports)) {
                    File fileToDelete = new File(this.mContext.getFilesDir(), fileName);
                    ACRA.log.d(ACRA.LOG_TAG, "Deleting file " + fileName);
                    if (!fileToDelete.delete()) {
                        Log.e(ACRA.LOG_TAG, "Could not delete report : " + fileToDelete);
                    }
                }
            }
        }
    }

    private boolean containsOnlySilentOrApprovedReports(String[] reportFileNames) {
        for (String reportFileName : reportFileNames) {
            if (!this.fileNameParser.isApproved(reportFileName)) {
                return false;
            }
        }
        return true;
    }

    public void setDefaultReportSenders() {
        ReportsCrashes conf = ACRA.getConfig();
        Application mApplication = ACRA.getApplication();
        removeAllReportSenders();
        if (!"".equals(conf.mailTo())) {
            Log.w(ACRA.LOG_TAG, mApplication.getPackageName() + " reports will be sent by email (if accepted by user).");
            setReportSender(new EmailIntentSender(mApplication));
            return;
        }
        PackageManagerWrapper pm = new PackageManagerWrapper(mApplication);
        if (!pm.hasPermission("android.permission.INTERNET")) {
            Log.e(ACRA.LOG_TAG, mApplication.getPackageName() + " should be granted permission android.permission.INTERNET if you want your crash reports to be sent. If you don't want to add this permission to your application you can also enable sending reports by email. If this is your will then provide your email address in @ReportsCrashes(mailTo=\"your.account@domain.com\"");
        } else if (conf.formUri() != null && !"".equals(conf.formUri())) {
            setReportSender(new HttpSender(ACRA.getConfig().httpMethod(), ACRA.getConfig().reportType(), null));
        } else if (conf.formKey() != null && !"".equals(conf.formKey().trim())) {
            addReportSender(new GoogleFormSender());
        }
    }
}
