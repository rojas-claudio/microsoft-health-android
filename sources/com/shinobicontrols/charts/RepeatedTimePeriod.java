package com.shinobicontrols.charts;

import java.util.Date;
/* loaded from: classes.dex */
public class RepeatedTimePeriod {
    final Date a;
    final DateFrequency b;
    final DateFrequency c;

    public RepeatedTimePeriod(Date start, DateFrequency length, DateFrequency frequency) {
        if (start == null) {
            throw new IllegalArgumentException("TimePeriod cannot have null start date.");
        }
        if (length == null) {
            throw new IllegalArgumentException("TimePeriod cannot have null lenth.");
        }
        if (frequency == null) {
            throw new IllegalArgumentException("RepeatedTimePeriod cannot have null frequency.");
        }
        this.a = start;
        this.b = length;
        this.c = frequency;
    }

    public Date getStart() {
        return this.a;
    }

    public DateFrequency getLength() {
        return this.b;
    }

    public DateFrequency getFrequency() {
        return this.c;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof RepeatedTimePeriod) {
            RepeatedTimePeriod repeatedTimePeriod = (RepeatedTimePeriod) o;
            return this.a.equals(repeatedTimePeriod.a) && this.b.equals(repeatedTimePeriod.b) && this.c.equals(repeatedTimePeriod.c);
        }
        return false;
    }

    public int hashCode() {
        return ((((this.a.hashCode() + 527) * 31) + this.b.hashCode()) * 31) + this.c.hashCode();
    }
}
