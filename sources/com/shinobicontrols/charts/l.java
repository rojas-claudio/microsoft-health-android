package com.shinobicontrols.charts;

import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.dc;
import java.util.List;
/* loaded from: classes.dex */
class l extends cq {
    /* JADX INFO: Access modifiers changed from: package-private */
    public l(BarColumnSeries<?> barColumnSeries) {
        super(true, barColumnSeries);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.cq
    public void a(List<CartesianSeries<?>> list) {
        b(list);
        c(list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.cq
    public void a() {
        m mVar = (m) this.a.p;
        mVar.m = (float) this.a.t.b(this.a);
        mVar.c = 0;
        mVar.a(this.a.n.a() * 2);
        mVar.b = null;
        for (int i = 0; i < this.a.n.c.length; i++) {
            InternalDataPoint internalDataPoint = this.a.n.c[i];
            float[] fArr = mVar.a;
            int i2 = mVar.c;
            mVar.c = i2 + 1;
            fArr[i2] = (float) internalDataPoint.c;
            float[] fArr2 = mVar.a;
            int i3 = mVar.c;
            mVar.c = i3 + 1;
            fArr2[i3] = (float) internalDataPoint.d;
        }
    }

    private static void b(List<CartesianSeries<?>> list) {
        for (CartesianSeries<?> cartesianSeries : list) {
            m mVar = (m) cartesianSeries.p;
            mVar.m = (float) cartesianSeries.t.b(cartesianSeries);
            mVar.a(cartesianSeries.n.a() * 2);
            mVar.b(cartesianSeries.n.a());
            mVar.c = 0;
        }
    }

    private static void c(List<CartesianSeries<?>> list) {
        dc dcVar = new dc(list, false);
        while (dcVar.hasNext()) {
            dc.a next = dcVar.next();
            CartesianSeries<?> cartesianSeries = list.get(list.size() - 1);
            while (true) {
                CartesianSeries<?> cartesianSeries2 = cartesianSeries;
                dc.b a = next.a(cartesianSeries2);
                if (a.a()) {
                    m mVar = (m) cartesianSeries2.p;
                    InternalDataPoint internalDataPoint = cartesianSeries2.n.c[a.a];
                    if ((cartesianSeries2.j == Series.Orientation.HORIZONTAL ? internalDataPoint.a : internalDataPoint.b) == next.a) {
                        double a2 = a(cartesianSeries2, next);
                        if (cartesianSeries2.j == Series.Orientation.HORIZONTAL) {
                            float[] fArr = mVar.a;
                            int i = mVar.c;
                            mVar.c = i + 1;
                            fArr[i] = (float) internalDataPoint.c;
                            float[] fArr2 = mVar.a;
                            int i2 = mVar.c;
                            mVar.c = i2 + 1;
                            fArr2[i2] = (float) a2;
                            mVar.b[a.a] = (float) (a2 - internalDataPoint.b);
                            internalDataPoint.d = a2;
                        } else {
                            float[] fArr3 = mVar.a;
                            int i3 = mVar.c;
                            mVar.c = i3 + 1;
                            fArr3[i3] = (float) a2;
                            float[] fArr4 = mVar.a;
                            int i4 = mVar.c;
                            mVar.c = i4 + 1;
                            fArr4[i4] = (float) internalDataPoint.d;
                            mVar.b[a.a] = (float) (a2 - internalDataPoint.a);
                            internalDataPoint.c = a2;
                        }
                    }
                }
                cartesianSeries = cartesianSeries2.j();
                if (cartesianSeries != null) {
                }
            }
        }
    }

    @Override // com.shinobicontrols.charts.cq
    void a(List<CartesianSeries<?>> list, dc.a aVar, NumberRange numberRange) {
        CartesianSeries<?> cartesianSeries = list.get(list.size() - 1);
        do {
            if (!cartesianSeries.y && aVar.a(cartesianSeries).a()) {
                numberRange.a(a(cartesianSeries, aVar) * 1.01d);
            }
            cartesianSeries = cartesianSeries.j();
        } while (cartesianSeries != null);
        CartesianSeries<?> cartesianSeries2 = list.get(0);
        numberRange.a(cartesianSeries2.t.b(cartesianSeries2));
    }

    @Override // com.shinobicontrols.charts.cq
    void b(List<CartesianSeries<?>> list, dc.a aVar, NumberRange numberRange) {
        CartesianSeries<?> cartesianSeries = list.get(list.size() - 1);
        do {
            if (aVar.a(cartesianSeries).a()) {
                numberRange.a(a(cartesianSeries, aVar));
            }
            cartesianSeries = cartesianSeries.j();
        } while (cartesianSeries != null);
        numberRange.a(list.get(0).t.b(list.get(0)));
    }

    private static double a(CartesianSeries<?> cartesianSeries, dc.a aVar) {
        CartesianSeries<?> j = cartesianSeries.j();
        double a = j == null ? ((m) cartesianSeries.p).m : a(j, aVar);
        dc.b a2 = aVar.a(cartesianSeries);
        if (a2.a()) {
            InternalDataPoint internalDataPoint = cartesianSeries.n.c[a2.a];
            double d = cartesianSeries.j == Series.Orientation.HORIZONTAL ? internalDataPoint.a : internalDataPoint.b;
            double d2 = cartesianSeries.j == Series.Orientation.HORIZONTAL ? internalDataPoint.b : internalDataPoint.a;
            if (d == aVar.a) {
                return a + d2;
            }
            return a;
        }
        return a;
    }
}
