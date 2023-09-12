package com.shinobicontrols.charts;

import com.shinobicontrols.charts.Series;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class dc implements Iterator<a> {
    private final List<CartesianSeries<?>> a;
    private final Series.Orientation b;
    private final a c;
    private final boolean d;

    public dc(List<CartesianSeries<?>> list, boolean z) {
        if (list.size() < 1) {
            throw new IllegalStateException("There must be at least one series in a stacking group");
        }
        this.a = list;
        this.b = list.get(0).j;
        this.c = new a(list);
        this.d = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b {
        int a;
        final CartesianSeries<?> b;

        private b(CartesianSeries<?> cartesianSeries) {
            this.a = -1;
            this.b = cartesianSeries;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean b() {
            return this.a == -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean c() {
            return this.a != -2 && this.a + 1 < this.b.n.a();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean a() {
            return (this.a == -1 || this.a == -2) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void d() {
            this.a++;
            if (this.a >= this.b.n.a()) {
                this.a = -2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a {
        double a;
        private final Map<CartesianSeries<?>, b> c;

        private a(List<CartesianSeries<?>> list) {
            this.a = Double.NEGATIVE_INFINITY;
            this.c = new HashMap();
            for (CartesianSeries<?> cartesianSeries : list) {
                this.c.put(cartesianSeries, new b(cartesianSeries));
            }
        }

        public b a(CartesianSeries<?> cartesianSeries) {
            return this.c.get(cartesianSeries);
        }
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        for (CartesianSeries<?> cartesianSeries : this.a) {
            if (a(cartesianSeries)) {
                b a2 = this.c.a(cartesianSeries);
                if (a2.c()) {
                    return true;
                }
                if (a2.a()) {
                    InternalDataPoint internalDataPoint = cartesianSeries.n.c[a2.a];
                    if ((this.b == Series.Orientation.HORIZONTAL ? internalDataPoint.a : internalDataPoint.b) > this.c.a) {
                        return true;
                    }
                } else {
                    continue;
                }
            }
        }
        return false;
    }

    @Override // java.util.Iterator
    /* renamed from: a */
    public a next() {
        for (CartesianSeries<?> cartesianSeries : this.a) {
            if (a(cartesianSeries)) {
                b a2 = this.c.a(cartesianSeries);
                if (a2.b()) {
                    a2.d();
                } else if (a2.a()) {
                    InternalDataPoint internalDataPoint = cartesianSeries.n.c[a2.a];
                    if ((this.b == Series.Orientation.HORIZONTAL ? internalDataPoint.a : internalDataPoint.b) == this.c.a) {
                        a2.d();
                    }
                }
            }
        }
        double d = Double.POSITIVE_INFINITY;
        Iterator<CartesianSeries<?>> it = this.a.iterator();
        while (true) {
            double d2 = d;
            if (it.hasNext()) {
                CartesianSeries<?> next = it.next();
                if (a(next)) {
                    b a3 = this.c.a(next);
                    if (a3.a()) {
                        InternalDataPoint internalDataPoint2 = next.n.c[a3.a];
                        double d3 = this.b == Series.Orientation.HORIZONTAL ? internalDataPoint2.a : internalDataPoint2.b;
                        if (d3 <= this.c.a) {
                            throw new IllegalStateException(next.o.getContext().getString(R.string.StackSeriesIteratorOrdinatesOutofOrder));
                        }
                        if (d3 < d2) {
                            d = d3;
                        }
                    }
                }
                d = d2;
            } else {
                this.c.a = d2;
                return this.c;
            }
        }
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private boolean a(Series<?> series) {
        return this.d || !series.y;
    }
}
