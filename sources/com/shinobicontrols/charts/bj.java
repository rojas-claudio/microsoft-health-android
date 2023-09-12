package com.shinobicontrols.charts;

import java.util.Locale;
/* loaded from: classes.dex */
class bj {
    final double a;
    final double b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public bj(double d, double d2) {
        this.a = d;
        this.b = d2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double a(double d) {
        return (this.a * d) + this.b;
    }

    public String toString() {
        return String.format(Locale.US, "f(x) = %fx + %f", Double.valueOf(this.a), Double.valueOf(this.b));
    }
}
