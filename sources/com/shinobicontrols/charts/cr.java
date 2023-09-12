package com.shinobicontrols.charts;

import android.annotation.SuppressLint;
import com.shinobicontrols.charts.Series;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"UseSparseArrays"})
/* loaded from: classes.dex */
public class cr {
    private final Axis<?, ?> a;
    private final Axis<?, ?> b;
    private final List<CartesianSeries<?>> c = new ArrayList();
    private final List<CartesianSeries<?>> d = new ArrayList();
    private final Map<Integer, List<CartesianSeries<?>>> e = new HashMap();
    private final Class f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public cr(Axis<?, ?> axis, Axis<?, ?> axis2, CartesianSeries<?> cartesianSeries) {
        if (cartesianSeries == null) {
            throw new NullPointerException();
        }
        this.a = axis;
        this.b = axis2;
        a(cartesianSeries);
        this.f = cartesianSeries.getClass();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(Axis<?, ?> axis) {
        return axis == this.a || axis == this.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        for (CartesianSeries<?> cartesianSeries : this.d) {
            cartesianSeries.k.a();
        }
        for (List<CartesianSeries<?>> list : this.e.values()) {
            list.get(0).k.a(list);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NumberRange b(Axis axis) {
        NumberRange numberRange = new NumberRange();
        for (CartesianSeries<?> cartesianSeries : this.d) {
            if (!cartesianSeries.y) {
                numberRange.c(cartesianSeries.b((Axis<?, ?>) axis));
            }
        }
        for (List<CartesianSeries<?>> list : this.e.values()) {
            numberRange.c(list.get(0).k.a(axis, list));
        }
        return numberRange;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NumberRange c(Axis axis) {
        NumberRange numberRange = new NumberRange();
        for (CartesianSeries<?> cartesianSeries : this.d) {
            numberRange.c(cartesianSeries.c(axis));
        }
        for (List<CartesianSeries<?>> list : this.e.values()) {
            numberRange.c(list.get(0).k.b(axis, list));
        }
        return numberRange;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(CartesianSeries<?> cartesianSeries) {
        this.c.add(cartesianSeries);
        cartesianSeries.a(this);
        b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        List<CartesianSeries<?>> arrayList;
        int i;
        this.d.clear();
        this.e.clear();
        int i2 = 0;
        for (CartesianSeries<?> cartesianSeries : this.c) {
            if (cartesianSeries.c == null) {
                cartesianSeries.a(i2, 0, (CartesianSeries<?>) null);
                this.d.add(cartesianSeries);
                i = i2 + 1;
            } else {
                if (this.e.containsKey(cartesianSeries.c)) {
                    arrayList = this.e.get(cartesianSeries.c);
                    cartesianSeries.a(arrayList.get(0).i(), this.e.size(), arrayList.get(arrayList.size() - 1));
                } else {
                    cartesianSeries.a(i2, 0, (CartesianSeries<?>) null);
                    arrayList = new ArrayList<>();
                    this.e.put(cartesianSeries.c, arrayList);
                    i2++;
                }
                arrayList.add(cartesianSeries);
                i = i2;
            }
            i2 = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int c() {
        return this.d.size() + this.e.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(Axis<?, ?> axis) {
        for (CartesianSeries<?> cartesianSeries : this.c) {
            cartesianSeries.a(axis);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int d() {
        return this.c.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(Series<?> series) {
        return this.c.contains(series);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Series<?> series) {
        if (a(series)) {
            this.c.remove(series);
            series.a((cr) null);
            b();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(Series<?> series, Axis<?, ?> axis, Axis<?, ?> axis2) {
        return this.a == axis && this.b == axis2 && series.getClass().equals(this.f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double b(CartesianSeries<?> cartesianSeries) {
        if (cartesianSeries.l()) {
            return b(cartesianSeries.j());
        }
        if (cartesianSeries.b != null) {
            Axis<?, ?> c = c(cartesianSeries);
            if (!c.isDataValid(cartesianSeries.b)) {
                throw new IllegalStateException(cartesianSeries.o != null ? cartesianSeries.o.getContext().getString(R.string.CartesianBaselineWrongType) : "Current baseline for series is invalid for the assigned x/y axes.");
            }
            return c.translatePoint(cartesianSeries.b);
        } else if ((cartesianSeries instanceof BarColumnSeries) || cartesianSeries.c != null) {
            Axis<?, ?> c2 = c(cartesianSeries);
            return c2.translatePoint(c2.getDefaultBaseline());
        } else {
            Axis<?, ?> c3 = c(cartesianSeries);
            return c3.l != null ? c3.l.a : c3.e();
        }
    }

    private Axis<?, ?> c(CartesianSeries<?> cartesianSeries) {
        return cartesianSeries.j == Series.Orientation.HORIZONTAL ? this.b : this.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<? extends Series<?>> c(Series<?> series) {
        for (List<CartesianSeries<?>> list : this.e.values()) {
            if (list.contains(series)) {
                return list;
            }
        }
        return Collections.emptyList();
    }
}
