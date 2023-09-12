package com.shinobicontrols.charts;

import com.shinobicontrols.charts.Series;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class j extends cq {
    /* JADX INFO: Access modifiers changed from: package-private */
    public j(BandSeries bandSeries) {
        super(false, bandSeries);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.cq
    public void a() {
        boolean z;
        k kVar = (k) this.a.p;
        kVar.i = 0;
        kVar.g = 0;
        kVar.h = 0;
        kVar.a(this.a.n.c.length);
        int i = 0;
        boolean z2 = true;
        while (i < this.a.n.c.length) {
            InternalDataPoint internalDataPoint = this.a.n.c[i];
            a(i, internalDataPoint, kVar.a, kVar.d, kVar);
            if (internalDataPoint.h) {
                a(kVar.g, internalDataPoint, kVar.c, kVar.f, kVar);
                kVar.g++;
            } else {
                a(kVar.h, internalDataPoint, kVar.b, kVar.e, kVar);
                kVar.h++;
            }
            int i2 = i * 2;
            int a = a(i2);
            if (i2 > 0) {
                if (kVar.a[a] >= kVar.d[a]) {
                    if (!z2) {
                        kVar.i++;
                        z = true;
                    }
                    z = z2;
                } else {
                    if (z2) {
                        kVar.i++;
                        z = false;
                    }
                    z = z2;
                }
            } else {
                z = kVar.a[a] >= kVar.d[a];
            }
            i++;
            z2 = z;
        }
    }

    private int a(int i) {
        return this.a.j == Series.Orientation.HORIZONTAL ? i + 1 : i;
    }

    private void a(int i, InternalDataPoint internalDataPoint, float[] fArr, float[] fArr2, k kVar) {
        int i2 = i * 2;
        if (this.a.j == Series.Orientation.VERTICAL) {
            fArr[i2 + 1] = (float) internalDataPoint.a;
            fArr[i2] = internalDataPoint.j.get("High").floatValue();
            fArr2[i2 + 1] = (float) internalDataPoint.a;
            fArr2[i2] = internalDataPoint.j.get("Low").floatValue();
            internalDataPoint.c = (internalDataPoint.j.get("High").doubleValue() + internalDataPoint.j.get("Low").floatValue()) / 2.0d;
            internalDataPoint.d = internalDataPoint.a;
            return;
        }
        fArr[i2] = (float) internalDataPoint.a;
        fArr[i2 + 1] = internalDataPoint.j.get("High").floatValue();
        fArr2[i2] = (float) internalDataPoint.a;
        fArr2[i2 + 1] = internalDataPoint.j.get("Low").floatValue();
        internalDataPoint.c = internalDataPoint.a;
        internalDataPoint.d = (internalDataPoint.j.get("High").doubleValue() + internalDataPoint.j.get("Low").floatValue()) / 2.0d;
    }
}
