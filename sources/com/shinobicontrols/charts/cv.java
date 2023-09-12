package com.shinobicontrols.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class cv {
    private final b a = new b();
    private final b b = new b();
    private final a c = new a();
    private final List<cr> d = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Axis<?, ?> axis) {
        for (CartesianSeries<?> cartesianSeries : this.a.keySet()) {
            if (this.a.get(cartesianSeries) == null) {
                a(cartesianSeries, axis, this.b.get(cartesianSeries));
                cartesianSeries.k();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Axis<?, ?> axis) {
        for (CartesianSeries<?> cartesianSeries : this.b.keySet()) {
            if (this.b.get(cartesianSeries) == null) {
                a(cartesianSeries, this.a.get(cartesianSeries), axis);
                cartesianSeries.k();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(CartesianSeries<?> cartesianSeries, Axis<?, ?> axis, Axis<?, ?> axis2) {
        if (cartesianSeries == null) {
            throw new IllegalStateException();
        }
        if (axis != null && axis2 != null && axis == axis2) {
            throw new IllegalStateException();
        }
        a(cartesianSeries, axis, this.a);
        a(cartesianSeries, axis2, this.b);
        b(cartesianSeries, axis, axis2);
    }

    private void b(CartesianSeries<?> cartesianSeries, Axis<?, ?> axis, Axis<?, ?> axis2) {
        boolean z;
        if (axis != null && axis2 != null) {
            Iterator<cr> it = this.d.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                cr next = it.next();
                if (next.a(cartesianSeries, axis, axis2)) {
                    next.a(cartesianSeries);
                    z = true;
                    break;
                }
            }
            if (!z) {
                this.d.add(new cr(axis, axis2, cartesianSeries));
            }
        }
    }

    private void a(CartesianSeries<?> cartesianSeries, Axis<?, ?> axis, b bVar) {
        bVar.put(cartesianSeries, axis);
        if (axis != null) {
            Set<CartesianSeries<?>> set = this.c.get(axis);
            if (set == null) {
                set = new HashSet<>();
                this.c.put(axis, set);
            }
            set.add(cartesianSeries);
            axis.a(cartesianSeries);
            cartesianSeries.d(axis);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Series<?> series) {
        Axis<?, ?> b2 = b(series);
        if (b2 != null) {
            this.c.get(b2).remove(series);
            b2.b(series);
            series.e(b2);
        }
        Axis<?, ?> c = c(series);
        if (c != null) {
            this.c.get(c).remove(series);
            c.b(series);
            series.e(c);
        }
        this.a.remove(series);
        this.b.remove(series);
        d(series);
    }

    private void d(Series<?> series) {
        ArrayList<cr> arrayList = new ArrayList();
        for (cr crVar : this.d) {
            if (crVar.a(series)) {
                crVar.b(series);
                if (crVar.d() == 0) {
                    arrayList.add(crVar);
                }
            }
        }
        for (cr crVar2 : arrayList) {
            this.d.remove(crVar2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(Axis<?, ?> axis) {
        a(axis, this.a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(Axis<?, ?> axis) {
        a(axis, this.b);
    }

    private void a(Axis<?, ?> axis, b bVar) {
        Set<CartesianSeries<?>> set = this.c.get(axis);
        if (set != null) {
            for (CartesianSeries<?> cartesianSeries : set) {
                bVar.put(cartesianSeries, null);
                axis.b(cartesianSeries);
                cartesianSeries.e(axis);
            }
            this.c.remove(axis);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Axis<?, ?> b(Series<?> series) {
        return this.a.get(series);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Axis<?, ?> c(Series<?> series) {
        return this.b.get(series);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<CartesianSeries<?>> e(Axis<?, ?> axis) {
        return this.c.get(axis);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<cr> a() {
        return this.d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class b extends LinkedHashMap<CartesianSeries<?>, Axis<?, ?>> {
        private static final long serialVersionUID = 1;

        b() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class a extends HashMap<Axis<?, ?>, Set<CartesianSeries<?>>> {
        private static final long serialVersionUID = 1;

        a() {
        }
    }
}
