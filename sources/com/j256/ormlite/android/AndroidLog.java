package com.j256.ormlite.android;

import com.j256.ormlite.logger.Log;
import com.j256.ormlite.logger.LoggerFactory;
/* loaded from: classes.dex */
public class AndroidLog implements Log {
    private static final String ALL_LOGS_NAME = "ORMLite";
    private static final int MAX_TAG_LENGTH = 23;
    private static final int REFRESH_LEVEL_CACHE_EVERY = 200;
    private String className;
    private final boolean[] levelCache;
    private volatile int levelCacheC = 0;

    public AndroidLog(String className) {
        this.className = LoggerFactory.getSimpleClassName(className);
        int length = this.className.length();
        if (length > 23) {
            this.className = this.className.substring(length - 23, length);
        }
        int maxLevel = 0;
        Log.Level[] arr$ = Log.Level.values();
        for (Log.Level level : arr$) {
            int androidLevel = levelToAndroidLevel(level);
            if (androidLevel > maxLevel) {
                maxLevel = androidLevel;
            }
        }
        this.levelCache = new boolean[maxLevel + 1];
        refreshLevelCache();
    }

    @Override // com.j256.ormlite.logger.Log
    public boolean isLevelEnabled(Log.Level level) {
        int i = this.levelCacheC + 1;
        this.levelCacheC = i;
        if (i >= REFRESH_LEVEL_CACHE_EVERY) {
            refreshLevelCache();
            this.levelCacheC = 0;
        }
        int androidLevel = levelToAndroidLevel(level);
        return androidLevel < this.levelCache.length ? this.levelCache[androidLevel] : isLevelEnabledInternal(androidLevel);
    }

    @Override // com.j256.ormlite.logger.Log
    public void log(Log.Level level, String msg) {
        switch (level) {
            case TRACE:
                android.util.Log.v(this.className, msg);
                return;
            case DEBUG:
                android.util.Log.d(this.className, msg);
                return;
            case INFO:
                android.util.Log.i(this.className, msg);
                return;
            case WARNING:
                android.util.Log.w(this.className, msg);
                return;
            case ERROR:
                android.util.Log.e(this.className, msg);
                return;
            case FATAL:
                android.util.Log.e(this.className, msg);
                return;
            default:
                android.util.Log.i(this.className, msg);
                return;
        }
    }

    @Override // com.j256.ormlite.logger.Log
    public void log(Log.Level level, String msg, Throwable t) {
        switch (level) {
            case TRACE:
                android.util.Log.v(this.className, msg, t);
                return;
            case DEBUG:
                android.util.Log.d(this.className, msg, t);
                return;
            case INFO:
                android.util.Log.i(this.className, msg, t);
                return;
            case WARNING:
                android.util.Log.w(this.className, msg, t);
                return;
            case ERROR:
                android.util.Log.e(this.className, msg, t);
                return;
            case FATAL:
                android.util.Log.e(this.className, msg, t);
                return;
            default:
                android.util.Log.i(this.className, msg, t);
                return;
        }
    }

    private void refreshLevelCache() {
        Log.Level[] arr$ = Log.Level.values();
        for (Log.Level level : arr$) {
            int androidLevel = levelToAndroidLevel(level);
            if (androidLevel < this.levelCache.length) {
                this.levelCache[androidLevel] = isLevelEnabledInternal(androidLevel);
            }
        }
    }

    private boolean isLevelEnabledInternal(int androidLevel) {
        return android.util.Log.isLoggable(this.className, androidLevel) || android.util.Log.isLoggable(ALL_LOGS_NAME, androidLevel);
    }

    private int levelToAndroidLevel(Log.Level level) {
        switch (level) {
            case TRACE:
                return 2;
            case DEBUG:
                return 3;
            case INFO:
            default:
                return 4;
            case WARNING:
                return 5;
            case ERROR:
                return 6;
            case FATAL:
                return 6;
        }
    }
}
