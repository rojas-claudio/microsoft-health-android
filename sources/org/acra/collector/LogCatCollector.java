package org.acra.collector;

import android.os.Process;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.acra.ACRA;
import org.acra.util.BoundedLinkedList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class LogCatCollector {
    private static final int DEFAULT_TAIL_COUNT = 100;

    LogCatCollector() {
    }

    public static String collectLogCat(String bufferName) {
        int tailCount;
        int myPid = Process.myPid();
        String myPidStr = null;
        if (ACRA.getConfig().logcatFilterByPid() && myPid > 0) {
            myPidStr = Integer.toString(myPid) + "):";
        }
        List<String> commandLine = new ArrayList<>();
        commandLine.add("logcat");
        if (bufferName != null) {
            commandLine.add("-b");
            commandLine.add(bufferName);
        }
        List<String> logcatArgumentsList = new ArrayList<>(Arrays.asList(ACRA.getConfig().logcatArguments()));
        int tailIndex = logcatArgumentsList.indexOf("-t");
        if (tailIndex > -1 && tailIndex < logcatArgumentsList.size()) {
            tailCount = Integer.parseInt(logcatArgumentsList.get(tailIndex + 1));
            if (Compatibility.getAPILevel() < 8) {
                logcatArgumentsList.remove(tailIndex + 1);
                logcatArgumentsList.remove(tailIndex);
                logcatArgumentsList.add("-d");
            }
        } else {
            tailCount = -1;
        }
        if (tailCount <= 0) {
            tailCount = 100;
        }
        LinkedList<String> logcatBuf = new BoundedLinkedList<>(tailCount);
        commandLine.addAll(logcatArgumentsList);
        try {
            final Process process = Runtime.getRuntime().exec((String[]) commandLine.toArray(new String[commandLine.size()]));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()), 8192);
            Log.d(ACRA.LOG_TAG, "Retrieving logcat output...");
            new Thread(new Runnable() { // from class: org.acra.collector.LogCatCollector.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        InputStream stderr = process.getErrorStream();
                        byte[] dummy = new byte[8192];
                        do {
                        } while (stderr.read(dummy) >= 0);
                    } catch (IOException e) {
                    }
                }
            }).start();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                } else if (myPidStr == null || line.contains(myPidStr)) {
                    logcatBuf.add(line + "\n");
                }
            }
        } catch (IOException e) {
            Log.e(ACRA.LOG_TAG, "LogCatCollector.collectLogCat could not retrieve data.", e);
        }
        return logcatBuf.toString();
    }
}
