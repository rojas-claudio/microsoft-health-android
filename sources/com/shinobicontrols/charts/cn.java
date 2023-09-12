package com.shinobicontrols.charts;

import android.graphics.PointF;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.CartesianSeries;
import com.shinobicontrols.charts.Series;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class cn {
    private final v a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum b {
        CROSSHAIR_ENABLED,
        SELECTION_MODE_NOT_NONE
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public cn(v vVar) {
        this.a = vVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0042  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.shinobicontrols.charts.Series.a a(android.graphics.PointF r6, com.shinobicontrols.charts.cn.b r7) {
        /*
            r5 = this;
            r1 = 0
            com.shinobicontrols.charts.v r0 = r5.a
            java.util.List r0 = r0.h()
            java.util.Iterator r2 = r0.iterator()
        Lb:
            boolean r0 = r2.hasNext()
            if (r0 == 0) goto L41
            java.lang.Object r0 = r2.next()
            com.shinobicontrols.charts.CartesianSeries r0 = (com.shinobicontrols.charts.CartesianSeries) r0
            boolean r3 = r0.y
            if (r3 != 0) goto Lb
            int[] r3 = com.shinobicontrols.charts.cn.AnonymousClass1.a
            int r4 = r7.ordinal()
            r3 = r3[r4]
            switch(r3) {
                case 1: goto L33;
                case 2: goto L3a;
                default: goto L26;
            }
        L26:
            r3 = 0
            com.shinobicontrols.charts.Series$a r0 = r5.a(r0, r6, r3)
            boolean r3 = r0.a(r1)
            if (r3 == 0) goto L42
        L31:
            r1 = r0
            goto Lb
        L33:
            com.shinobicontrols.charts.aa r3 = r0.l
            boolean r3 = r3.a
            if (r3 != 0) goto L26
            goto Lb
        L3a:
            com.shinobicontrols.charts.Series$SelectionMode r3 = r0.s
            com.shinobicontrols.charts.Series$SelectionMode r4 = com.shinobicontrols.charts.Series.SelectionMode.NONE
            if (r3 != r4) goto L26
            goto Lb
        L41:
            return r1
        L42:
            r0 = r1
            goto L31
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shinobicontrols.charts.cn.a(android.graphics.PointF, com.shinobicontrols.charts.cn$b):com.shinobicontrols.charts.Series$a");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Series.a a(PointF pointF) {
        double d = -(pointF.x - this.a.b.b.centerX());
        double centerY = pointF.y - this.a.b.b.centerY();
        float sqrt = (float) (Math.sqrt((centerY * centerY) + (d * d)) / (Math.min(this.a.b.b.width(), this.a.b.b.height()) / 2.0d));
        for (Series<?> series : this.a.c) {
            if (series.getSelectionMode() != Series.SelectionMode.NONE && series.getSelectionMode() != Series.SelectionMode.SERIES && series.s()) {
                PieDonutSeries pieDonutSeries = (PieDonutSeries) series;
                if (pieDonutSeries.getInnerRadius() < sqrt) {
                    if (sqrt <= Math.max(((PieDonutSeriesStyle) pieDonutSeries.r).getProtrusion(), ((PieDonutSeriesStyle) pieDonutSeries.q).getProtrusion()) + pieDonutSeries.getOuterRadius()) {
                        Series.a aVar = new Series.a(pieDonutSeries);
                        int length = series.n.c.length;
                        if (length == 1) {
                            aVar.a(series.n.c[0]);
                            return aVar;
                        }
                        cg g = pieDonutSeries.g();
                        float rotation = pieDonutSeries.getRotation();
                        float a2 = g.a(d, centerY);
                        for (int i = 0; i < length; i++) {
                            PieDonutSlice pieDonutSlice = (PieDonutSlice) series.n.c[i];
                            float a3 = g.a(rotation, pieDonutSlice.n);
                            float b2 = g.b(rotation, pieDonutSlice.o);
                            if ((b2 > a2 && a2 > a3) || (a3 > b2 && (a2 > a3 || b2 > a2))) {
                                aVar.a(pieDonutSlice);
                                break;
                            }
                        }
                        return aVar;
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Series.a a(CartesianSeries<?> cartesianSeries, PointF pointF, boolean z) {
        Series.a aVar = new Series.a(cartesianSeries);
        cartesianSeries.a(aVar, new bz(cartesianSeries.getXAxis().e(pointF.x), cartesianSeries.getYAxis().e(pointF.y)), z);
        return aVar;
    }

    static double a(bz bzVar, bz bzVar2, bz bzVar3, CartesianSeries.a aVar) {
        double d = (bzVar.b - bzVar2.b) / bzVar3.b;
        double d2 = (bzVar.c - bzVar2.c) / bzVar3.c;
        switch (aVar) {
            case CROW_FLIES:
                return Math.sqrt((d * d) + (d2 * d2));
            case HORIZONTAL:
                return Math.abs(d);
            case VERTICAL:
                return Math.abs(d2);
            default:
                throw new IllegalStateException(String.format("Bad distance mode %d", aVar));
        }
    }

    static bz a(CartesianSeries<?> cartesianSeries) {
        return new bz(cartesianSeries.getXAxis().i.getSpan(), (cartesianSeries.o.b.b.width() / cartesianSeries.o.b.b.height()) * cartesianSeries.getYAxis().i.getSpan());
    }

    private static void b(CartesianSeries<?> cartesianSeries, Series.a aVar, bz bzVar, boolean z) {
        bz a2 = a(cartesianSeries);
        double d = Double.MAX_VALUE;
        bz bzVar2 = new bz();
        int length = cartesianSeries.n.c.length;
        int i = 0;
        while (i < length) {
            InternalDataPoint internalDataPoint = cartesianSeries.n.c[i];
            bzVar2.b = internalDataPoint.c;
            bzVar2.c = internalDataPoint.d;
            double a3 = a(bzVar, bzVar2, a2, z ? cartesianSeries.g() : cartesianSeries.f());
            if (a3 < d) {
                aVar.a(a3);
                aVar.a(internalDataPoint);
            } else {
                a3 = d;
            }
            i++;
            d = a3;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(CartesianSeries<?> cartesianSeries, Series.a aVar, bz bzVar, boolean z) {
        b(cartesianSeries, aVar, bzVar, z);
        if (Series.a.b(aVar)) {
            cartesianSeries.a(aVar, bzVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(BarColumnSeries<?> barColumnSeries, Series.a aVar, bz bzVar, boolean z) {
        a((CartesianSeries<?>) barColumnSeries, aVar, bzVar, z);
        if (Series.a.b(aVar)) {
            InternalDataPoint b2 = aVar.b();
            double d = barColumnSeries.a;
            a aVar2 = new a();
            aVar2.b = b2.d - (d / 2.0d);
            aVar2.d = d;
            double b3 = barColumnSeries.t.b((CartesianSeries<?>) barColumnSeries);
            double d2 = b2.a;
            if (barColumnSeries.c != null) {
                if (d2 < Constants.SPLITS_ACCURACY) {
                    aVar2.a = b2.c;
                    aVar2.c = b3 - b2.a;
                } else {
                    aVar2.a = b2.c - b2.a;
                    aVar2.c = d2;
                }
            } else {
                if (d2 < b3) {
                    aVar2.a = d2;
                    aVar2.c = b3 - d2;
                } else {
                    aVar2.a = b3;
                    aVar2.c = d2 - b3;
                }
            }
            a(aVar, bzVar, aVar2, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(BarColumnSeries<?> barColumnSeries, Series.a aVar, bz bzVar, boolean z) {
        a((CartesianSeries<?>) barColumnSeries, aVar, bzVar, z);
        if (Series.a.b(aVar)) {
            InternalDataPoint b2 = aVar.b();
            double d = barColumnSeries.a;
            a aVar2 = new a();
            aVar2.a = b2.c - (d / 2.0d);
            aVar2.c = d;
            double b3 = barColumnSeries.t.b((CartesianSeries<?>) barColumnSeries);
            double d2 = b2.b;
            if (barColumnSeries.c != null) {
                if (d2 < Constants.SPLITS_ACCURACY) {
                    aVar2.b = b2.d;
                    aVar2.d = b3 - b2.b;
                } else {
                    aVar2.b = b2.d - b2.b;
                    aVar2.d = d2;
                }
            } else {
                if (d2 < b3) {
                    aVar2.b = d2;
                    aVar2.d = b3 - d2;
                } else {
                    aVar2.b = b3;
                    aVar2.d = d2 - b3;
                }
            }
            a(aVar, bzVar, aVar2, z);
        }
    }

    private static void a(Series.a aVar, bz bzVar, a aVar2, boolean z) {
        if (a(bzVar, aVar2)) {
            aVar.a(Constants.SPLITS_ACCURACY);
            return;
        }
        aVar.a(Double.MAX_VALUE);
        if (!z) {
            aVar.e();
        }
    }

    private static boolean a(bz bzVar, a aVar) {
        a(aVar);
        return aVar.a <= bzVar.b && bzVar.b <= aVar.a + aVar.c && aVar.b <= bzVar.c && bzVar.c <= aVar.b + aVar.d;
    }

    private static void a(a aVar) {
        if (aVar.c < Constants.SPLITS_ACCURACY) {
            aVar.a += aVar.c;
            aVar.c = Math.abs(aVar.c);
        }
        if (aVar.d < Constants.SPLITS_ACCURACY) {
            aVar.b += aVar.d;
            aVar.d = Math.abs(aVar.d);
        }
    }
}
