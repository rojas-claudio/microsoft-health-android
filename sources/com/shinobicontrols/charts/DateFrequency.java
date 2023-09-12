package com.shinobicontrols.charts;

import org.apache.commons.lang3.time.DateUtils;
/* loaded from: classes.dex */
public class DateFrequency {
    int a;
    Denomination b;

    /* loaded from: classes.dex */
    public enum Denomination {
        SECONDS(13, 1000),
        MINUTES(12, DateUtils.MILLIS_PER_MINUTE),
        HOURS(10, 3600000),
        DAYS(7, 86400000),
        WEEKS(3, 604800000),
        MONTHS(2, 0),
        YEARS(1, 0);
        
        final int a;
        final long b;

        Denomination(int value, long milliSeconds) {
            this.a = value;
            this.b = milliSeconds;
        }
    }

    public DateFrequency() {
        this.a = 1;
        this.b = Denomination.MINUTES;
    }

    public DateFrequency(int quantity, Denomination denomination) {
        this.a = 1;
        this.b = Denomination.MINUTES;
        this.a = quantity;
        this.b = denomination;
    }

    public final int getQuantity() {
        return this.a;
    }

    public final Denomination getDenomination() {
        return this.b;
    }

    public void setQuantity(int quantity) {
        this.a = quantity;
    }

    public void setDenomination(Denomination denomination) {
        this.b = denomination;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i, Denomination denomination) {
        this.a = i;
        this.b = denomination;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(int i, Denomination denomination) {
        return this.a == i && this.b == denomination;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long a() {
        switch (this.b) {
            case SECONDS:
                return this.a * 1000;
            case MINUTES:
                return this.a * DateUtils.MILLIS_PER_MINUTE;
            case HOURS:
                return this.a * 3600000;
            case DAYS:
                return this.a * 86400000;
            case WEEKS:
                return this.a * 7 * 86400000;
            case MONTHS:
                return this.a * 86400000 * 28;
            case YEARS:
                return this.a * 86400000 * 365;
            default:
                return 1L;
        }
    }

    /* renamed from: clone */
    public DateFrequency m8clone() {
        return new DateFrequency(this.a, this.b);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof DateFrequency) {
            DateFrequency dateFrequency = (DateFrequency) o;
            if (this.a == dateFrequency.a) {
                if (this.b == null) {
                    if (dateFrequency.b == null) {
                        return true;
                    }
                } else if (this.b == dateFrequency.b) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return (this.b == null ? 0 : this.b.hashCode()) + ((this.a + 527) * 31);
    }
}
