package net.hockeyapp.android;

import android.os.Process;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread;
import java.util.Date;
import java.util.UUID;
import org.acra.ACRAConstants;
/* loaded from: classes.dex */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
    private boolean ignoreDefaultHandler;
    private CrashManagerListener listener;

    public ExceptionHandler(Thread.UncaughtExceptionHandler defaultExceptionHandler, CrashManagerListener listener, boolean ignoreDefaultHandler) {
        this.ignoreDefaultHandler = false;
        this.defaultExceptionHandler = defaultExceptionHandler;
        this.ignoreDefaultHandler = ignoreDefaultHandler;
        this.listener = listener;
    }

    public void setListener(CrashManagerListener listener) {
        this.listener = listener;
    }

    public static void saveException(Throwable exception, CrashManagerListener listener) {
        Date now = new Date();
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        exception.printStackTrace(printWriter);
        try {
            String filename = UUID.randomUUID().toString();
            String path = Constants.FILES_PATH + "/" + filename + ACRAConstants.REPORTFILE_EXTENSION;
            Log.d("HockeyApp", "Writing unhandled exception to: " + path);
            BufferedWriter write = new BufferedWriter(new FileWriter(path));
            write.write("Package: " + Constants.APP_PACKAGE + "\n");
            write.write("Version Code: " + Constants.APP_VERSION + "\n");
            write.write("Version Name: " + Constants.APP_VERSION_NAME + "\n");
            if (listener == null || listener.includeDeviceData()) {
                write.write("Android: " + Constants.ANDROID_VERSION + "\n");
                write.write("Manufacturer: " + Constants.PHONE_MANUFACTURER + "\n");
                write.write("Model: " + Constants.PHONE_MODEL + "\n");
            }
            if (Constants.CRASH_IDENTIFIER != null && (listener == null || listener.includeDeviceIdentifier())) {
                write.write("CrashReporter Key: " + Constants.CRASH_IDENTIFIER + "\n");
            }
            write.write("Date: " + now + "\n");
            write.write("\n");
            write.write(result.toString());
            write.flush();
            write.close();
            if (listener != null) {
                writeValueToFile(limitedString(listener.getUserID()), filename + ".user");
                writeValueToFile(limitedString(listener.getContact()), filename + ".contact");
                writeValueToFile(listener.getDescription(), filename + ".description");
            }
        } catch (Exception another) {
            Log.e("HockeyApp", "Error saving exception stacktrace!\n", another);
        }
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable exception) {
        if (Constants.FILES_PATH == null) {
            this.defaultExceptionHandler.uncaughtException(thread, exception);
            return;
        }
        saveException(exception, this.listener);
        if (!this.ignoreDefaultHandler) {
            this.defaultExceptionHandler.uncaughtException(thread, exception);
            return;
        }
        Process.killProcess(Process.myPid());
        System.exit(10);
    }

    private static void writeValueToFile(String value, String filename) {
        try {
            String path = Constants.FILES_PATH + "/" + filename;
            if (value.trim().length() > 0) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(path));
                writer.write(value);
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
        }
    }

    private static String limitedString(String string) {
        if (string != null && string.length() > 255) {
            return string.substring(0, 255);
        }
        return string;
    }
}
