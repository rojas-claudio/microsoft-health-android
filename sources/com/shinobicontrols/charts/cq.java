package com.shinobicontrols.charts;

import com.shinobicontrols.charts.dc;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class cq {
    protected final CartesianSeries<?> a;
    private final boolean b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a();

    /* JADX INFO: Access modifiers changed from: package-private */
    public cq(boolean z, CartesianSeries<?> cartesianSeries) {
        this.b = z;
        this.a = cartesianSeries;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(List<CartesianSeries<?>> list) {
        for (CartesianSeries<?> cartesianSeries : list) {
            if (cartesianSeries.s()) {
                cartesianSeries.k.a();
            }
        }
    }

    void a(List<CartesianSeries<?>> list, dc.a aVar, NumberRange numberRange) {
    }

    void b(List<CartesianSeries<?>> list, dc.a aVar, NumberRange numberRange) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NumberRange a(Axis<?, ?> axis, List<CartesianSeries<?>> list) {
        NumberRange numberRange = new NumberRange();
        if (!this.b || axis.a(this.a.j) || list.size() == 1) {
            for (CartesianSeries<?> cartesianSeries : list) {
                if (!cartesianSeries.y) {
                    numberRange.c(cartesianSeries.b(axis));
                }
            }
        } else {
            dc dcVar = new dc(list, false);
            while (dcVar.hasNext()) {
                a(list, dcVar.next(), numberRange);
            }
        }
        return numberRange;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NumberRange b(Axis<?, ?> axis, List<CartesianSeries<?>> list) {
        NumberRange numberRange = new NumberRange();
        if (!this.b || axis.a(this.a.j) || list.size() == 1) {
            for (CartesianSeries<?> cartesianSeries : list) {
                numberRange.c(cartesianSeries.c(axis));
            }
        } else {
            dc dcVar = new dc(list, true);
            while (dcVar.hasNext()) {
                b(list, dcVar.next(), numberRange);
            }
        }
        return numberRange;
    }
}
