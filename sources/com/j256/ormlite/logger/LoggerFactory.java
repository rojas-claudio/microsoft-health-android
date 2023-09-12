package com.j256.ormlite.logger;

import com.j256.ormlite.logger.Log;
/* loaded from: classes.dex */
public class LoggerFactory {
    public static final String LOG_TYPE_SYSTEM_PROPERTY = "com.j256.ormlite.logger.type";
    private static LogType logType;

    private LoggerFactory() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String className) {
        if (logType == null) {
            logType = findLogType();
        }
        return new Logger(logType.createLog(className));
    }

    public static String getSimpleClassName(String className) {
        String[] parts = className.split("\\.");
        return parts.length <= 1 ? className : parts[parts.length - 1];
    }

    private static LogType findLogType() {
        String logTypeString = System.getProperty(LOG_TYPE_SYSTEM_PROPERTY);
        if (logTypeString != null) {
            try {
                return LogType.valueOf(logTypeString);
            } catch (IllegalArgumentException e) {
                Log log = new LocalLog(LoggerFactory.class.getName());
                log.log(Log.Level.WARNING, "Could not find valid log-type from system property 'com.j256.ormlite.logger.type', value '" + logTypeString + "'");
            }
        }
        LogType[] arr$ = LogType.values();
        for (LogType logType2 : arr$) {
            if (logType2.isAvailable()) {
                return logType2;
            }
        }
        return LogType.LOCAL;
    }

    /* loaded from: classes.dex */
    public enum LogType {
        ANDROID("android.util.Log", "com.j256.ormlite.android.AndroidLog"),
        SLF4J("org.slf4j.LoggerFactory", "com.j256.ormlite.logger.Slf4jLoggingLog"),
        COMMONS_LOGGING("org.apache.commons.logging.LogFactory", "com.j256.ormlite.logger.CommonsLoggingLog"),
        LOG4J2("org.apache.logging.log4j.LogManager", "com.j256.ormlite.logger.Log4j2Log"),
        LOG4J("org.apache.log4j.Logger", "com.j256.ormlite.logger.Log4jLog"),
        LOCAL(LocalLog.class.getName(), LocalLog.class.getName()) { // from class: com.j256.ormlite.logger.LoggerFactory.LogType.1
            @Override // com.j256.ormlite.logger.LoggerFactory.LogType
            public Log createLog(String classLabel) {
                return new LocalLog(classLabel);
            }

            @Override // com.j256.ormlite.logger.LoggerFactory.LogType
            public boolean isAvailable() {
                return true;
            }
        };
        
        private final String detectClassName;
        private final String logClassName;

        LogType(String detectClassName, String logClassName) {
            this.detectClassName = detectClassName;
            this.logClassName = logClassName;
        }

        public Log createLog(String classLabel) {
            try {
                return createLogFromClassName(classLabel);
            } catch (Exception e) {
                Log log = new LocalLog(classLabel);
                log.log(Log.Level.WARNING, "Unable to call constructor with single String argument for class " + this.logClassName + ", so had to use local log: " + e.getMessage());
                return log;
            }
        }

        public boolean isAvailable() {
            if (isAvailableTestClass()) {
                try {
                    Log log = createLogFromClassName(getClass().getName());
                    log.isLevelEnabled(Log.Level.INFO);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            return false;
        }

        Log createLogFromClassName(String classLabel) throws Exception {
            Class<?> clazz = Class.forName(this.logClassName);
            return (Log) clazz.getConstructor(String.class).newInstance(classLabel);
        }

        boolean isAvailableTestClass() {
            try {
                Class.forName(this.detectClassName);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
