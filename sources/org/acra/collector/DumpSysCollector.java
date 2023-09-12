package org.acra.collector;

import android.os.Process;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.acra.ACRA;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class DumpSysCollector {
    DumpSysCollector() {
    }

    public static String collectMemInfo() {
        StringBuilder meminfo = new StringBuilder();
        try {
            List<String> commandLine = new ArrayList<>();
            commandLine.add("dumpsys");
            commandLine.add("meminfo");
            commandLine.add(Integer.toString(Process.myPid()));
            Process process = Runtime.getRuntime().exec((String[]) commandLine.toArray(new String[commandLine.size()]));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()), 8192);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                meminfo.append(line);
                meminfo.append("\n");
            }
        } catch (IOException e) {
            Log.e(ACRA.LOG_TAG, "DumpSysCollector.meminfo could not retrieve data", e);
        }
        return meminfo.toString();
    }
}
