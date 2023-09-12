package com.j256.ormlite.logger;

import com.j256.ormlite.logger.Log;
import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class LocalLog implements Log {
    public static final String LOCAL_LOG_FILE_PROPERTY = "com.j256.ormlite.logger.file";
    public static final String LOCAL_LOG_LEVEL_PROPERTY = "com.j256.ormlite.logger.level";
    public static final String LOCAL_LOG_PROPERTIES_FILE = "/ormliteLocalLog.properties";
    private static final List<PatternLevel> classLevels;
    private static PrintStream printStream;
    private final String className;
    private final Log.Level level;
    private static final Log.Level DEFAULT_LEVEL = Log.Level.DEBUG;
    private static final ThreadLocal<DateFormat> dateFormatThreadLocal = new ThreadLocal<DateFormat>() { // from class: com.j256.ormlite.logger.LocalLog.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        }
    };

    static {
        InputStream stream = LocalLog.class.getResourceAsStream(LOCAL_LOG_PROPERTIES_FILE);
        List<PatternLevel> levels = readLevelResourceFile(stream);
        classLevels = levels;
        String logPath = System.getProperty(LOCAL_LOG_FILE_PROPERTY);
        openLogFile(logPath);
    }

    public LocalLog(String className) {
        this.className = LoggerFactory.getSimpleClassName(className);
        Log.Level level = null;
        if (classLevels != null) {
            for (PatternLevel patternLevel : classLevels) {
                if (patternLevel.pattern.matcher(className).matches() && (level == null || patternLevel.level.ordinal() < level.ordinal())) {
                    level = patternLevel.level;
                }
            }
        }
        if (level == null) {
            String levelName = System.getProperty(LOCAL_LOG_LEVEL_PROPERTY);
            if (levelName == null) {
                level = DEFAULT_LEVEL;
            } else {
                try {
                    Log.Level matchedLevel = Log.Level.valueOf(levelName.toUpperCase());
                    level = matchedLevel;
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Level '" + levelName + "' was not found", e);
                }
            }
        }
        this.level = level;
    }

    public static void openLogFile(String logPath) {
        if (logPath == null) {
            printStream = System.out;
            return;
        }
        try {
            printStream = new PrintStream(new File(logPath));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Log file " + logPath + " was not found", e);
        }
    }

    @Override // com.j256.ormlite.logger.Log
    public boolean isLevelEnabled(Log.Level level) {
        return this.level.isEnabled(level);
    }

    @Override // com.j256.ormlite.logger.Log
    public void log(Log.Level level, String msg) {
        printMessage(level, msg, null);
    }

    @Override // com.j256.ormlite.logger.Log
    public void log(Log.Level level, String msg, Throwable throwable) {
        printMessage(level, msg, throwable);
    }

    void flush() {
        printStream.flush();
    }

    static List<PatternLevel> readLevelResourceFile(InputStream stream) {
        List<PatternLevel> levels = null;
        try {
            if (stream != null) {
                try {
                    levels = configureClassLevels(stream);
                } catch (IOException e) {
                    System.err.println("IO exception reading the log properties file '/ormliteLocalLog.properties': " + e);
                    try {
                        stream.close();
                    } catch (IOException e2) {
                    }
                }
            }
            return levels;
        } finally {
            try {
                stream.close();
            } catch (IOException e3) {
            }
        }
    }

    private static List<PatternLevel> configureClassLevels(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<PatternLevel> list = new ArrayList<>();
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                if (line.length() != 0 && line.charAt(0) != '#') {
                    String[] parts = line.split(SimpleComparison.EQUAL_TO_OPERATION);
                    if (parts.length != 2) {
                        System.err.println("Line is not in the format of 'pattern = level': " + line);
                    } else {
                        Pattern pattern = Pattern.compile(parts[0].trim());
                        try {
                            Log.Level level = Log.Level.valueOf(parts[1].trim());
                            list.add(new PatternLevel(pattern, level));
                        } catch (IllegalArgumentException e) {
                            System.err.println("Level '" + parts[1] + "' was not found");
                        }
                    }
                }
            } else {
                return list;
            }
        }
    }

    private void printMessage(Log.Level level, String message, Throwable throwable) {
        if (isLevelEnabled(level)) {
            StringBuilder sb = new StringBuilder(128);
            DateFormat dateFormat = dateFormatThreadLocal.get();
            sb.append(dateFormat.format(new Date()));
            sb.append(" [").append(level.name()).append("] ");
            sb.append(this.className).append(' ');
            sb.append(message);
            printStream.println(sb.toString());
            if (throwable != null) {
                throwable.printStackTrace(printStream);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class PatternLevel {
        Log.Level level;
        Pattern pattern;

        public PatternLevel(Pattern pattern, Log.Level level) {
            this.pattern = pattern;
            this.level = level;
        }
    }
}
