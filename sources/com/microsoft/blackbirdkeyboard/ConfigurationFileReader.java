package com.microsoft.blackbirdkeyboard;

import android.content.res.AssetManager;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/* loaded from: classes.dex */
public class ConfigurationFileReader {
    private static final String mPathSeparator = "/";
    private static final String mRootDir = "blackbirdkeyboard";
    String mLine;
    BufferedReader mReader;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConfigurationFileReader(AssetManager assetManager, String subdir, String filename) {
        String path = (subdir.isEmpty() ? "blackbirdkeyboard/" : "blackbirdkeyboard/" + subdir + mPathSeparator) + filename;
        try {
            InputStream is = assetManager.open(path);
            InputStreamReader isr = new InputStreamReader(is);
            this.mReader = new BufferedReader(isr);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ConfigurationFileReader: failed to open file " + path);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String[] parseLine(String separator) {
        String separator2;
        if (",=".contains(separator) && separator.length() == 1) {
            separator2 = "\\" + separator;
        } else if (separator.equals(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE)) {
            separator2 = "\\s";
        } else {
            throw new IllegalArgumentException("Invalid separator");
        }
        while (true) {
            try {
                String readLine = this.mReader.readLine();
                this.mLine = readLine;
                if (readLine == null) {
                    break;
                }
                int trimmedLength = this.mLine.length();
                while (trimmedLength > 0 && (this.mLine.charAt(trimmedLength - 1) == '\r' || this.mLine.charAt(trimmedLength - 1) == '\n')) {
                    trimmedLength--;
                }
                this.mLine = this.mLine.substring(0, trimmedLength);
                if (!this.mLine.startsWith("//") && !this.mLine.isEmpty()) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("ConfigurationFileReader: failed to read file");
            }
        }
        if (this.mLine != null) {
            return this.mLine.split(separator2);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        if (this.mReader != null) {
            try {
                this.mReader.close();
                this.mReader = null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("ConfigurationFileReader: failed to close file");
            }
        }
    }
}
