package com.microsoft.kapp.logging;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import com.microsoft.kapp.logging.models.LogMode;
import com.microsoft.kapp.services.InjectableIntentService;
import com.microsoft.kapp.utils.FileUtils;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class LogCleanupService extends InjectableIntentService {
    private static final String SERVICE_NAME = "LogCleanupService";
    private static final String TAG = LogCleanupService.class.getSimpleName();
    @Inject
    Context mContext;
    @Inject
    LogConfiguration mLogConfiguration;

    public LogCleanupService() {
        super(SERVICE_NAME);
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent arg0) {
        try {
            KLog.i(TAG, "Log cleanup started");
            cleanupMessageLogs();
            cleanupImages();
            cleanupOldDiagnosticsZip();
            KLog.i(TAG, "Log cleanup complete");
        } catch (Exception exception) {
            KLog.e(TAG, "Unexpected exception during log cleanup.", exception);
        }
    }

    private void cleanupOldDiagnosticsZip() {
        File storageLocation;
        if (this.mLogConfiguration.getLogMode() == LogMode.DO_NOT_LOG_PRIVATE_DATA) {
            storageLocation = this.mContext.getFilesDir();
        } else {
            storageLocation = Environment.getExternalStorageDirectory();
        }
        File diagnosticsZipStore = new File(storageLocation, LogConstants.DIAGNOSTICS_ZIP_FOLDER);
        cleanupOldFiles(diagnosticsZipStore, 3600000L);
    }

    private void cleanupMessageLogs() {
        File diagnosticsDir = new File(this.mContext.getFilesDir(), LogConstants.DIAGNOSTICS_FOLDER);
        File messageDir = new File(diagnosticsDir, LogConstants.MESSAGE_LOGS_FOLDER);
        cleanupOldFiles(messageDir, 86400000L);
        cleanupFilesByCount(messageDir, 15);
    }

    private void cleanupImages() {
        File diagnosticsDir = new File(this.mContext.getFilesDir(), LogConstants.DIAGNOSTICS_FOLDER);
        File imageDir = new File(diagnosticsDir, LogConstants.DIAGNOSTIC_IMAGES_FOLDER);
        cleanupOldFiles(imageDir, 900000L);
        cleanupFilesByCount(imageDir, 10);
    }

    private void cleanupOldFiles(File directory, long maxLogAge) {
        File[] logFiles;
        if (directory.exists() && (logFiles = directory.listFiles()) != null) {
            for (File logFile : logFiles) {
                if (logFile.isFile() && DateTime.now().getMillis() - maxLogAge > logFile.lastModified()) {
                    logFile.delete();
                }
            }
        }
    }

    private void cleanupFilesByCount(File directory, int maxFileCount) {
        File[] logFiles;
        if (directory.exists() && (logFiles = directory.listFiles()) != null && logFiles.length > 0) {
            List<File> logFileList = Arrays.asList(logFiles);
            FileUtils.sortFilesbyLastModified(logFileList);
            if (logFileList.size() > maxFileCount) {
                int filesTodelete = logFileList.size() - maxFileCount;
                for (int i = 0; i < filesTodelete; i++) {
                    if (logFileList.get(i).isFile()) {
                        logFileList.get(i).delete();
                    }
                }
            }
        }
    }
}
