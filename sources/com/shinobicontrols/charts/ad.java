package com.shinobicontrols.charts;

import java.util.Date;
import java.util.GregorianCalendar;
/* loaded from: classes.dex */
class ad {
    private final DateTimeAxis b;
    private final a d = new a();
    private final a e = new a();
    boolean a = false;
    private final GregorianCalendar c = new GregorianCalendar();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a {
        long a;
        boolean b;
        int c;
        int d;

        a() {
            a();
        }

        void a() {
            this.a = Long.MAX_VALUE;
            this.b = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ad(DateTimeAxis dateTimeAxis) {
        this.b = dateTimeAxis;
        this.c.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long a(double d, DateFrequency dateFrequency) {
        return a((long) d, dateFrequency, false);
    }

    long b(double d, DateFrequency dateFrequency) {
        return a((long) d, dateFrequency, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double a(DateFrequency dateFrequency) {
        if (!c(dateFrequency, this.d)) {
            this.d.a();
        }
        return a(dateFrequency, this.d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double b(DateFrequency dateFrequency) {
        if (!c(dateFrequency, this.e)) {
            this.e.a();
        }
        return a(dateFrequency, this.e);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c(double d, DateFrequency dateFrequency) {
        long b = b(dateFrequency, this.d);
        boolean z = this.d.b;
        if (b < d) {
            while (b < d) {
                b = a(b, dateFrequency);
                z = !z;
            }
        } else if (b > d) {
            while (b > d) {
                b = b(b, dateFrequency);
                z = !z;
            }
        }
        return z;
    }

    private long a(long j, DateFrequency dateFrequency, boolean z) {
        this.c.setTime(this.b.transformInternalValueToUser(j));
        int i = dateFrequency.a;
        if (z) {
            i *= -1;
        }
        if (this.c.get(0) == 0) {
            i *= -1;
        }
        this.c.add(dateFrequency.b.a, i);
        Date time = this.c.getTime();
        long transformUserValueToInternal = (long) this.b.transformUserValueToInternal(time);
        return transformUserValueToInternal == j ? a(time, dateFrequency.b.a, i) : transformUserValueToInternal;
    }

    private long a(Date date, int i, int i2) {
        this.c.setTimeInMillis((long) a(date).b);
        this.c.add(i, i2);
        return (long) this.b.transformUserValueToInternal(this.c.getTime());
    }

    private ap a(Date date) {
        return this.b.y.b(date.getTime());
    }

    private long a(DateFrequency dateFrequency, a aVar) {
        long b = b(dateFrequency, aVar);
        boolean z = aVar.b;
        long j = (long) this.b.i.a;
        long a2 = a(j, dateFrequency);
        while (b < j) {
            b = a(b, dateFrequency);
            z = !z;
        }
        while (b > a2) {
            b = b(b, dateFrequency);
            z = !z;
        }
        aVar.a = b;
        aVar.b = z;
        aVar.c = dateFrequency.a;
        aVar.d = dateFrequency.b.a;
        return b;
    }

    private long b(DateFrequency dateFrequency, a aVar) {
        return aVar.a != Long.MAX_VALUE ? aVar.a : (long) this.b.G();
    }

    private boolean c(DateFrequency dateFrequency, a aVar) {
        return (this.a && d(dateFrequency, aVar)) ? false : true;
    }

    private boolean d(DateFrequency dateFrequency, a aVar) {
        return (dateFrequency.a == aVar.c && dateFrequency.b.a == aVar.d) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        this.d.a();
        this.e.a();
    }
}
