package com.shinobicontrols.charts;

import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
class ci {
    double a = Constants.SPLITS_ACCURACY;
    double b = Constants.SPLITS_ACCURACY;
    double c = Constants.SPLITS_ACCURACY;
    double d = Constants.SPLITS_ACCURACY;

    /* JADX INFO: Access modifiers changed from: package-private */
    public double a() {
        return this.c - this.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double b() {
        return this.d - this.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(double d, double d2, double d3, double d4) {
        this.a = d;
        this.b = d2;
        this.c = d3;
        this.d = d4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(double d, double d2) {
        this.a += d;
        this.b += d2;
        this.c += d;
        this.d += d2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(double d, double d2) {
        this.c += d - this.a;
        this.d += d2 - this.b;
        this.a = d;
        this.b = d2;
    }
}
