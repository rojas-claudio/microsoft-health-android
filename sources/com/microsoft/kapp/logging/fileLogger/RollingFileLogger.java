package com.microsoft.kapp.logging.fileLogger;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.BaseLogger;
import com.microsoft.kapp.logging.LogConstants;
import com.microsoft.kapp.logging.models.LogEntry;
import com.microsoft.kapp.services.KAppBroadcastReceiver;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FileUtils;
import com.microsoft.kapp.utils.GsonUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class RollingFileLogger extends BaseLogger {
    private static final long MAX_LOG_FILE_SIZE_BYTES = 1048576;
    long currentLogFileSize;
    BufferedOutputStream mBos;
    FileOutputStream mFos;
    private String mLogFolder;
    private SettingsProvider mSettingsProvider;

    public RollingFileLogger(Context context, SettingsProvider settingsProvider, String logFolder) {
        super(context);
        this.mBos = null;
        this.mFos = null;
        this.currentLogFileSize = 0L;
        Validate.notNull(settingsProvider, "settingsProvider");
        Validate.notNull(logFolder, "logFolder");
        this.mSettingsProvider = settingsProvider;
        this.mLogFolder = logFolder;
    }

    @Override // com.microsoft.kapp.logging.BaseLogger, com.microsoft.kapp.logging.Logger
    public void log(LogEntry logentry) {
        String text = GsonUtils.getCustomSerializer().toJson(logentry);
        writeToFile(text);
    }

    public void flush() {
        try {
            if (this.mBos != null) {
                this.mBos.flush();
            }
        } catch (Exception e) {
        }
    }

    @Override // com.microsoft.kapp.logging.BaseLogger, com.microsoft.kapp.logging.Logger
    public void flushAndClose() {
        flush();
        try {
            StreamUtils.closeQuietly(this.mBos);
            StreamUtils.closeQuietly(this.mFos);
            this.mBos = null;
            this.mFos = null;
        } catch (Exception e) {
        }
    }

    private void initLogger() {
        String fileName = getFileName(this.mSettingsProvider.getCurrentRollingLogFilename());
        if (TextUtils.isEmpty(fileName)) {
            fileName = String.valueOf(DateTime.now().getMillis());
            this.mSettingsProvider.setCurrentRollingLogFilename(fileName);
        }
        String fileName2 = fileName + LogConstants.MESSAGES_FILE_EXTENSION;
        try {
            File messageDir = getMessageDir();
            messageDir.mkdirs();
            File logFile = new File(messageDir, fileName2);
            this.currentLogFileSize = logFile.length();
            this.mFos = new FileOutputStream(logFile, true);
            this.mBos = new BufferedOutputStream(this.mFos);
        } catch (Exception e) {
            StreamUtils.closeQuietly(this.mBos);
            StreamUtils.closeQuietly(this.mFos);
            this.mFos = null;
            this.mBos = null;
        }
    }

    private File getMessageDir() {
        File diagnosticsDir = new File(this.mContext.getFilesDir(), LogConstants.DIAGNOSTICS_FOLDER);
        File messageDir = new File(diagnosticsDir, this.mLogFolder);
        return messageDir;
    }

    private synchronized void writeToFile(String line) {
        if (this.mBos == null || this.mFos == null) {
            initLogger();
        }
        if (this.mBos != null && this.mFos != null) {
            try {
                byte[] data = (line + System.getProperty("line.separator")).getBytes();
                this.mBos.write(data);
                this.currentLogFileSize += data.length;
                checkAndRollover(this.currentLogFileSize);
            } catch (Exception e) {
            }
        }
    }

    private void checkAndRollover(long currentFileSize) {
        if (currentFileSize > MAX_LOG_FILE_SIZE_BYTES) {
            String newLogFileName = String.valueOf(DateTime.now().getMillis());
            this.mSettingsProvider.setCurrentRollingLogFilename(newLogFileName);
            flushAndClose();
            initLogger();
            launchLogCleanupService();
        }
    }

    private String getFileName(String filename) {
        return filename;
    }

    @Override // com.microsoft.kapp.logging.Logger
    public void packageLogs(String baseDir) {
        InputStream fileIn;
        InputStream fileIn2 = null;
        OutputStream packageOut = null;
        try {
            File outputmessageDir = new File(baseDir, this.mLogFolder);
            outputmessageDir.mkdirs();
            File outputFile = new File(outputmessageDir, LogConstants.MESSAGE_LOGS_OUTPUT);
            OutputStream packageOut2 = new FileOutputStream(outputFile, false);
            try {
                File messageDir = getMessageDir();
                if (messageDir.isDirectory()) {
                    File[] logFiles = messageDir.listFiles();
                    List<File> logFileList = Arrays.asList(logFiles);
                    FileUtils.sortFilesbyLastModified(logFileList);
                    Iterator i$ = logFileList.iterator();
                    while (true) {
                        try {
                            fileIn = fileIn2;
                            if (!i$.hasNext()) {
                                break;
                            }
                            File file = i$.next();
                            fileIn2 = new FileInputStream(file);
                            FileUtils.copyStream(fileIn2, packageOut2);
                            fileIn2.close();
                        } catch (Exception e) {
                            packageOut = packageOut2;
                            fileIn2 = fileIn;
                            StreamUtils.closeQuietly(packageOut);
                            StreamUtils.closeQuietly(fileIn2);
                            return;
                        } catch (Throwable th) {
                            th = th;
                            packageOut = packageOut2;
                            fileIn2 = fileIn;
                            StreamUtils.closeQuietly(packageOut);
                            StreamUtils.closeQuietly(fileIn2);
                            throw th;
                        }
                    }
                    fileIn2 = fileIn;
                }
                StreamUtils.closeQuietly(packageOut2);
                StreamUtils.closeQuietly(fileIn2);
            } catch (Exception e2) {
                packageOut = packageOut2;
            } catch (Throwable th2) {
                th = th2;
                packageOut = packageOut2;
            }
        } catch (Exception e3) {
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private void launchLogCleanupService() {
        Intent logCleanup = new Intent(this.mContext, KAppBroadcastReceiver.class);
        logCleanup.setAction(Constants.INTENT_LOG_CLEANUP);
        this.mContext.sendBroadcast(logCleanup);
    }
}
