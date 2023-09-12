package com.j256.ormlite.logger;
/* loaded from: classes.dex */
public interface Log {
    boolean isLevelEnabled(Level level);

    void log(Level level, String str);

    void log(Level level, String str, Throwable th);

    /* loaded from: classes.dex */
    public enum Level {
        TRACE(1),
        DEBUG(2),
        INFO(3),
        WARNING(4),
        ERROR(5),
        FATAL(6);
        
        private int level;

        Level(int level) {
            this.level = level;
        }

        public boolean isEnabled(Level otherLevel) {
            return this.level <= otherLevel.level;
        }
    }
}
