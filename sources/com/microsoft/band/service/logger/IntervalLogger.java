package com.microsoft.band.service.logger;

import android.os.SystemClock;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class IntervalLogger {
    private final CargoLogger mLogger;
    private final List<String> mTimeLabels;
    private final List<Long> mTimeMarks;

    public IntervalLogger(CargoLogger logger) {
        if (logger == null) {
            throw new NullPointerException("logger is null");
        }
        this.mLogger = logger;
        this.mTimeMarks = new ArrayList();
        this.mTimeLabels = new ArrayList();
    }

    public void mark(String label) {
        if (this.mLogger.isPerfLoggingEnabled()) {
            this.mTimeMarks.add(Long.valueOf(SystemClock.elapsedRealtime()));
            this.mTimeLabels.add(label);
        }
    }

    public void reset() {
        if (this.mLogger.isPerfLoggingEnabled()) {
            this.mTimeMarks.clear();
            this.mTimeLabels.clear();
        }
    }

    public long total() {
        if (this.mLogger.isPerfLoggingEnabled() && this.mTimeMarks.size() != 0) {
            long total = this.mTimeMarks.get(this.mTimeMarks.size() - 1).longValue() - this.mTimeMarks.get(0).longValue();
            if (total <= 0) {
                total = -1;
            }
            return total;
        }
        return -1L;
    }

    public void dump(String tag, String name, String format, Object... params) {
        if (this.mLogger.isPerfLoggingEnabled()) {
            this.mLogger.perfLog(System.currentTimeMillis(), tag, name, "Interval log", new Object[0]);
            this.mLogger.perfLog(System.currentTimeMillis(), tag, name, format, params);
            if (this.mTimeMarks.size() != 0) {
                long first = this.mTimeMarks.get(0).longValue();
                long now = first;
                long prev = first;
                for (int i = 0; i < this.mTimeMarks.size(); i++) {
                    now = this.mTimeMarks.get(i).longValue();
                    this.mLogger.perfLog(now - first, tag, name, (now - prev) + " ms since prev mark. Label:" + this.mTimeLabels.get(i), new Object[0]);
                    prev = now;
                }
                this.mLogger.perfLog(System.currentTimeMillis(), tag, name, "Total:" + (now - first) + " ms", new Object[0]);
            }
            this.mLogger.perfLog(System.currentTimeMillis(), tag, name, "End Interval log", new Object[0]);
        }
    }
}
