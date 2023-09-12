package com.shinobicontrols.charts;

import java.lang.Comparable;
/* loaded from: classes.dex */
public abstract class Range<T extends Comparable<T>> {
    double a;
    double b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract T getMaximum();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract T getMinimum();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(Range<?> range) {
        return range == null || range.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b(Range<?> range) {
        return !a(range);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Range(double min, double max) {
        this.a = min;
        this.b = max;
    }

    public double getSpan() {
        return b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double b() {
        return this.b - this.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(double d) {
        if (d < this.a) {
            this.a = d;
        }
        if (d > this.b) {
            this.b = d;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(Range<T> range) {
        if (!a((Range<?>) range)) {
            a(range.a);
            a(range.b);
        }
    }

    public boolean equals(Object other) {
        Range<T> range = (Range) other;
        return (other == null || range == null || !d(range)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d(Range<T> range) {
        return range != null && this.a == range.a && this.b == range.b;
    }

    private boolean a() {
        return Double.isInfinite(this.a) || Double.isInfinite(this.b) || this.a > this.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c() {
        return this.a == this.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(double d, double d2) {
        boolean z = false;
        double b = b();
        if (b > d2 - d) {
            this.a = d;
            this.b = d2;
            b = this.b - this.a;
            z = true;
        }
        if (this.a < d) {
            this.a = d;
            this.b = this.a + b;
            z = true;
        }
        if (this.b > d2) {
            this.b = d2;
            this.a = this.b - b;
            return true;
        }
        return z;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.a);
        long doubleToLongBits2 = Double.doubleToLongBits(this.b);
        return ((((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 527) * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(Range<T> range, boolean z) {
        if (range == null) {
            return false;
        }
        if (this.a > range.a) {
            this = range;
            range = this;
        }
        return z ? this.a + this.b() >= range.a : this.a + this.b() > range.a;
    }

    public String toString() {
        return String.format("[%s - %s]", getMinimum().toString(), getMaximum().toString());
    }
}
