package com.shinobicontrols.charts;

import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.dc;
import java.util.List;
/* loaded from: classes.dex */
class bh extends cq {
    private int b;
    private boolean c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public bh(LineSeries lineSeries) {
        super(true, lineSeries);
    }

    @Override // com.shinobicontrols.charts.cq
    public void a(List<CartesianSeries<?>> list) {
        LineSeries lineSeries = (LineSeries) list.get(0);
        bh bhVar = (bh) lineSeries.k;
        bhVar.a(lineSeries);
        b(list);
        c(list);
        bhVar.a(true);
        bhVar.b();
        bhVar.c();
    }

    @Override // com.shinobicontrols.charts.cq
    public void a() {
        a(this.a);
        a(false);
        b();
        c();
    }

    private void a(CartesianSeries<?> cartesianSeries) {
        ((bi) cartesianSeries.p).m = (float) cartesianSeries.t.b(cartesianSeries);
    }

    private void a(boolean z) {
        float f = 0.0f;
        if (z) {
            f = ((bi) this.a.p).m;
        }
        for (int i = 0; i < this.a.n.c.length; i++) {
            InternalDataPoint internalDataPoint = this.a.n.c[i];
            if (this.a.j == Series.Orientation.VERTICAL) {
                internalDataPoint.c = internalDataPoint.a + f;
            } else {
                internalDataPoint.d = internalDataPoint.b + f;
            }
        }
    }

    private void b() {
        bi biVar = (bi) this.a.p;
        d();
        biVar.h = 0;
        biVar.i = 0;
        biVar.a((this.a.n.c.length + (biVar.a * 2)) * 2);
        biVar.e(this.a.n.c.length);
    }

    private void c() {
        boolean z;
        float f;
        bi biVar = (bi) this.a.p;
        int i = 0;
        boolean z2 = true;
        for (int i2 = 0; i2 < this.a.n.c.length; i2++) {
            InternalDataPoint internalDataPoint = this.a.n.c[i2];
            float f2 = (float) (this.a.j == Series.Orientation.VERTICAL ? internalDataPoint.c : internalDataPoint.d);
            float f3 = (float) (this.a.j == Series.Orientation.VERTICAL ? internalDataPoint.d : internalDataPoint.c);
            if (i > 0) {
                if (f2 >= biVar.m) {
                    if (!z2) {
                        z = true;
                        z2 = true;
                    }
                    z = false;
                } else {
                    if (z2) {
                        z = true;
                        z2 = false;
                    }
                    z = false;
                }
            } else {
                z2 = f2 >= biVar.m;
                z = false;
            }
            if (z) {
                float abs = Math.abs(biVar.f[a(i - 2)] - biVar.m);
                float abs2 = Math.abs(f2 - biVar.m);
                if (abs == 0.0f && abs2 == 0.0f) {
                    f = 1.0f;
                } else {
                    f = abs / (abs2 + abs);
                }
                float f4 = biVar.m;
                float f5 = biVar.f[b(i - 2)];
                float f6 = (f * (f3 - f5)) + f5;
                biVar.f[a(i)] = f4;
                biVar.f[b(i)] = f6;
                int i3 = i + 2;
                biVar.f[a(i3)] = f4;
                biVar.f[b(i3)] = f6;
                i = i3 + 2;
            }
            biVar.f[i] = (float) internalDataPoint.c;
            biVar.f[i + 1] = (float) internalDataPoint.d;
            a(biVar, internalDataPoint);
            i += 2;
        }
        if (biVar.d()) {
            biVar.c(this.a.n.c.length);
            biVar.d(this.a.n.c.length);
        }
        biVar.b = i;
    }

    private static void a(bi biVar, InternalDataPoint internalDataPoint) {
        if (internalDataPoint.h) {
            biVar.e = true;
            biVar.h++;
            return;
        }
        biVar.i++;
    }

    private static void b(List<CartesianSeries<?>> list) {
        int i;
        int i2 = 0;
        dc dcVar = new dc(list, false);
        while (true) {
            i = i2;
            if (!dcVar.hasNext()) {
                break;
            }
            dcVar.next();
            i2 = i + 1;
        }
        CartesianSeries<?> cartesianSeries = list.get(list.size() - 1);
        while (true) {
            LineSeries lineSeries = (LineSeries) cartesianSeries;
            if (lineSeries.l()) {
                ((bh) lineSeries.k).a(lineSeries, i);
                cartesianSeries = lineSeries.j();
            } else {
                return;
            }
        }
    }

    private void a(LineSeries lineSeries, int i) {
        bi biVar = (bi) lineSeries.p;
        biVar.a(i * 2);
        biVar.b(i * 2);
        biVar.a = 0;
        biVar.b = 0;
        biVar.h = 0;
        biVar.i = 0;
        this.b = 0;
        this.c = false;
    }

    private static void c(List<CartesianSeries<?>> list) {
        dc dcVar = new dc(list, false);
        while (dcVar.hasNext()) {
            dc.a next = dcVar.next();
            LineSeries lineSeries = (LineSeries) list.get(list.size() - 1);
            while (true) {
                LineSeries lineSeries2 = lineSeries;
                if (lineSeries2.l()) {
                    lineSeries = (LineSeries) lineSeries2.j();
                    dc.b a = next.a(lineSeries2);
                    if (a(a, next.a)) {
                        InternalDataPoint internalDataPoint = lineSeries2.n.c[a.a];
                        bi biVar = (bi) lineSeries2.p;
                        biVar.m = (float) lineSeries2.t.b((CartesianSeries<?>) lineSeries2);
                        ((bh) lineSeries2.k).a(biVar, internalDataPoint, next, lineSeries);
                        a(biVar, internalDataPoint);
                    }
                }
            }
        }
    }

    private void a(bi biVar, InternalDataPoint internalDataPoint, dc.a aVar, LineSeries lineSeries) {
        float a;
        float a2;
        float f = (float) aVar.a;
        if (internalDataPoint.a == aVar.a) {
            internalDataPoint.d = a(this.a, aVar);
            a = (float) internalDataPoint.d;
            a2 = (float) (internalDataPoint.d - internalDataPoint.b);
        } else {
            a = (float) a(this.a, aVar);
            a2 = lineSeries != null ? (float) a(this.a.j(), aVar) : biVar.m;
        }
        biVar.f[this.b] = f;
        biVar.f[this.b + 1] = a;
        if (this.a.j == Series.Orientation.VERTICAL) {
            biVar.g[this.b] = f;
            biVar.g[this.b + 1] = a;
        } else {
            biVar.g[this.b] = f;
            biVar.g[this.b + 1] = a2;
        }
        if (this.b > 0) {
            if (biVar.f[a(this.b)] >= biVar.g[a(this.b)]) {
                if (!this.c) {
                    this.c = true;
                    biVar.a++;
                }
            } else if (this.c) {
                this.c = false;
                biVar.a++;
            }
        } else {
            this.c = biVar.f[a(this.b)] >= biVar.g[a(this.b)];
        }
        this.b += 2;
        biVar.b = this.b;
    }

    private void d() {
        bi biVar = (bi) this.a.p;
        biVar.a = 0;
        float f = -3.4028235E38f;
        boolean z = true;
        float f2 = Float.MAX_VALUE;
        for (int i = 0; i < this.a.n.c.length; i++) {
            InternalDataPoint internalDataPoint = this.a.n.c[i];
            float f3 = (float) (this.a.j == Series.Orientation.VERTICAL ? internalDataPoint.c : internalDataPoint.d);
            if (f3 > f) {
                f = f3;
            }
            if (f3 < f2) {
                f2 = f3;
            }
            if (i > 0) {
                if (f3 >= biVar.m) {
                    if (!z) {
                        biVar.a++;
                        z = true;
                    }
                } else if (z) {
                    biVar.a++;
                    z = false;
                }
            } else {
                z = f3 >= biVar.m;
            }
        }
        biVar.c = Math.abs(f - biVar.m);
        if (biVar.c == 0.0f) {
            biVar.c = 0.01f;
        }
        biVar.d = Math.abs(biVar.m - f2);
        if (biVar.d == 0.0f) {
            biVar.d = 0.01f;
        }
    }

    private static double a(InternalDataPoint internalDataPoint, InternalDataPoint internalDataPoint2, double d, Series.Orientation orientation) {
        return orientation == Series.Orientation.HORIZONTAL ? internalDataPoint.b + (((internalDataPoint2.b - internalDataPoint.b) * (d - internalDataPoint.a)) / (internalDataPoint2.a - internalDataPoint.a)) : internalDataPoint.a + (((internalDataPoint2.a - internalDataPoint.a) * (d - internalDataPoint.b)) / (internalDataPoint2.b - internalDataPoint.b));
    }

    private static double a(CartesianSeries<?> cartesianSeries, dc.a aVar) {
        CartesianSeries<?> j = cartesianSeries.j();
        double b = j == null ? cartesianSeries.t.b(cartesianSeries) : a(j, aVar);
        dc.b a = aVar.a(cartesianSeries);
        if (a(a, aVar.a)) {
            InternalDataPoint internalDataPoint = cartesianSeries.n.c[a.a];
            double d = cartesianSeries.j == Series.Orientation.HORIZONTAL ? internalDataPoint.a : internalDataPoint.b;
            double d2 = cartesianSeries.j == Series.Orientation.HORIZONTAL ? internalDataPoint.b : internalDataPoint.a;
            if (d == aVar.a) {
                return b + d2;
            }
            return b + a(cartesianSeries.n.c[a.a - 1], cartesianSeries.n.c[a.a], aVar.a, cartesianSeries.j);
        }
        return b;
    }

    @Override // com.shinobicontrols.charts.cq
    void a(List<CartesianSeries<?>> list, dc.a aVar, NumberRange numberRange) {
        CartesianSeries<?> cartesianSeries = list.get(list.size() - 1);
        do {
            if (!cartesianSeries.y && a(aVar.a(cartesianSeries), aVar.a)) {
                numberRange.a(a(cartesianSeries, aVar));
            }
            cartesianSeries = cartesianSeries.j();
        } while (cartesianSeries != null);
    }

    @Override // com.shinobicontrols.charts.cq
    void b(List<CartesianSeries<?>> list, dc.a aVar, NumberRange numberRange) {
        CartesianSeries<?> cartesianSeries = list.get(list.size() - 1);
        do {
            if (a(aVar.a(cartesianSeries), aVar.a)) {
                numberRange.a(a(cartesianSeries, aVar));
            }
            cartesianSeries = cartesianSeries.j();
        } while (cartesianSeries != null);
    }

    private int a(int i) {
        return this.a.j == Series.Orientation.HORIZONTAL ? i + 1 : i;
    }

    private int b(int i) {
        return this.a.j == Series.Orientation.HORIZONTAL ? i : i + 1;
    }

    private static boolean a(dc.b bVar, double d) {
        return bVar.a() && bVar.b.n.c[0].a <= d && bVar.b.n.c[bVar.b.n.a() + (-1)].a >= d;
    }
}
