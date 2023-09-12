package com.shinobicontrols.charts;

import com.microsoft.kapp.utils.Constants;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
class ca {
    private final bz a = new bz();
    private final bz b = new bz();
    private final List<a> c = new ArrayList();
    private final bz d = new bz();

    /* JADX INFO: Access modifiers changed from: package-private */
    public bz a(InternalDataPoint internalDataPoint, bz bzVar, InternalDataPoint[] internalDataPointArr, boolean z) {
        double d;
        double d2;
        a();
        if (z) {
            d = bzVar.b;
            d2 = bzVar.c;
        } else {
            d = bzVar.c;
            d2 = bzVar.b;
        }
        a(internalDataPointArr, d, z);
        if (!a(z, d, d2)) {
            a(internalDataPoint);
        }
        return this.d;
    }

    private void a() {
        this.a.b = Constants.SPLITS_ACCURACY;
        this.a.c = Constants.SPLITS_ACCURACY;
        this.b.b = Constants.SPLITS_ACCURACY;
        this.b.c = Constants.SPLITS_ACCURACY;
        this.c.clear();
        this.d.b = Constants.SPLITS_ACCURACY;
        this.d.c = Constants.SPLITS_ACCURACY;
    }

    private void a(InternalDataPoint[] internalDataPointArr, double d, boolean z) {
        int length = internalDataPointArr.length;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < length - 1) {
                this.a.b = internalDataPointArr[i2].c;
                this.a.c = internalDataPointArr[i2].d;
                this.b.b = internalDataPointArr[i2 + 1].c;
                this.b.c = internalDataPointArr[i2 + 1].d;
                a(z);
                double d2 = z ? this.a.b : this.a.c;
                double d3 = z ? this.b.b : this.b.c;
                if (d >= d2 && d <= d3) {
                    this.c.add(a.a(this.a, this.b));
                }
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    private void a(boolean z) {
        boolean z2 = true;
        if (z) {
            if (this.a.b <= this.b.b) {
                z2 = false;
            }
        } else if (this.a.c <= this.b.c) {
            z2 = false;
        }
        if (z2) {
            double d = this.a.b;
            double d2 = this.a.c;
            this.a.b = this.b.b;
            this.a.c = this.b.c;
            this.b.b = d;
            this.b.c = d2;
        }
    }

    private boolean a(boolean z, double d, double d2) {
        boolean z2;
        double d3;
        double d4 = Double.MAX_VALUE;
        boolean z3 = false;
        Object[] array = this.c.toArray();
        int length = array.length;
        int i = 0;
        while (i < length) {
            a aVar = (a) array[i];
            double a2 = a(aVar, z, d);
            double d5 = aVar.a + ((aVar.c - aVar.a) * a2);
            double d6 = aVar.b + (a2 * (aVar.d - aVar.b));
            double abs = z ? Math.abs(d2 - d6) : Math.abs(d2 - d5);
            if (abs < d4) {
                this.d.b = d5;
                this.d.c = d6;
                z2 = true;
                d3 = abs;
            } else {
                z2 = z3;
                d3 = d4;
            }
            i++;
            d4 = d3;
            z3 = z2;
        }
        return z3;
    }

    private double a(a aVar, boolean z, double d) {
        double d2 = z ? aVar.a : aVar.b;
        double d3 = z ? aVar.c : aVar.d;
        if (d3 != d2) {
            return Math.abs((d - d2) / (d3 - d2));
        }
        return 1.0d;
    }

    private void a(InternalDataPoint internalDataPoint) {
        this.d.b = internalDataPoint.c;
        this.d.c = internalDataPoint.d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a {
        double a;
        double b;
        double c;
        double d;

        private a() {
        }

        static a a(bz bzVar, bz bzVar2) {
            a aVar = new a();
            aVar.a = bzVar.b;
            aVar.b = bzVar.c;
            aVar.c = bzVar2.b;
            aVar.d = bzVar2.c;
            return aVar;
        }
    }
}
