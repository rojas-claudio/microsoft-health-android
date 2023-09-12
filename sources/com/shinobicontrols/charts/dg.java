package com.shinobicontrols.charts;

import android.graphics.Canvas;
import android.graphics.PointF;
import com.shinobicontrols.charts.Axis;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class dg {
    private final Axis<?, ?> b;
    private final df f;
    private final List<TickMark> c = new ArrayList();
    private final List<TickMark> d = new ArrayList();
    private final Stack<TickMark> e = new Stack<>();
    private final a g = new a();
    private TickMark[] h = new TickMark[0];
    boolean a = false;

    public dg(Axis<?, ?> axis) {
        this.b = axis;
        this.f = axis.B();
    }

    public void a(Axis.c cVar) {
        if (this.a) {
            b();
            this.a = false;
        } else {
            d();
        }
        if (!Range.a(this.b.i)) {
            if (cVar.b || cVar.c || cVar.e || cVar.f || cVar.d) {
                d(cVar);
            }
        }
    }

    public void a() {
        this.c.clear();
    }

    void b() {
        this.c.clear();
        this.d.clear();
        this.e.clear();
    }

    private void d() {
        int size = this.d.size();
        for (int i = 0; i < size; i++) {
            this.e.push(this.d.get(i));
        }
        this.d.clear();
        int size2 = this.c.size();
        this.h = (TickMark[]) this.c.toArray(this.h);
        for (int i2 = 0; i2 < size2; i2++) {
            TickMark tickMark = this.h[i2];
            if (tickMark.b) {
                this.d.add(tickMark);
            } else {
                this.e.push(tickMark);
            }
        }
        this.c.clear();
    }

    public void a(Canvas canvas, Axis.c cVar) {
        int size = this.c.size();
        this.h = (TickMark[]) this.c.toArray(this.h);
        for (int i = 0; i < size; i++) {
            this.h[i].a(canvas, this, i, cVar);
        }
    }

    private TickMark a(double d, Axis.c cVar) {
        TickMark tickMark;
        int size = this.d.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                tickMark = null;
                break;
            }
            tickMark = this.d.get(i);
            if (tickMark.a == d) {
                break;
            }
            i++;
        }
        if (tickMark == null) {
            tickMark = c(cVar);
        } else {
            this.d.remove(tickMark);
        }
        tickMark.b = true;
        tickMark.e = cVar.c;
        return tickMark;
    }

    private TickMark b(Axis.c cVar) {
        TickMark c = c(cVar);
        c.b = false;
        c.e = cVar.d;
        return c;
    }

    private TickMark c(Axis.c cVar) {
        if (this.e.isEmpty()) {
            return new TickMark(this.b);
        }
        return this.e.pop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a {
        public int a;

        private a() {
        }
    }

    private void d(Axis.c cVar) {
        if (this.b.t != null) {
            a(this.b.t, cVar);
        } else {
            e(cVar);
        }
    }

    private void a(double[] dArr, Axis.c cVar) {
        this.g.a = 0;
        int a2 = a(dArr);
        if (a2 != -1) {
            a(cVar, this.g, a2, dArr, false);
        }
    }

    private void a(Axis.c cVar, a aVar, int i, double[] dArr, boolean z) {
        boolean z2 = z;
        while (aVar.a < 2 && i < dArr.length) {
            a(dArr[i], cVar, z2, cVar.b, aVar);
            z2 = !z2;
            i++;
        }
    }

    private int a(double[] dArr) {
        for (int i = 0; i < dArr.length; i++) {
            if (dArr[i] >= this.b.i.a) {
                return i;
            }
        }
        return -1;
    }

    private void e(Axis.c cVar) {
        this.g.a = 0;
        double a2 = this.b.a(cVar.A);
        boolean b = this.b.b(a2);
        a(cVar, this.g, a2, b);
        b(cVar, this.g, a2, b);
    }

    private void a(Axis.c cVar, a aVar, double d, boolean z) {
        boolean j = this.b.j();
        int a2 = a(j, this.b.n(), cVar);
        int a3 = j ? 0 : this.b.a(cVar.A, a2);
        int i = 0;
        boolean z2 = z;
        double d2 = d;
        while (aVar.a < 2) {
            a(d2, cVar, z2, cVar.b && i == a3, aVar);
            int i2 = i + 1;
            if (i2 == a2) {
                i2 = 0;
            }
            d2 = this.b.a(d2, true);
            z2 = !z2;
            i = i2;
        }
    }

    private int a(boolean z, double d, Axis.c cVar) {
        if (z) {
            return 1;
        }
        int ceil = (int) Math.ceil(this.b.i.b() / d);
        if (this.b.a()) {
            return (int) Math.ceil(1.0f / ((cVar.A / cVar.y.x) / ceil));
        }
        return (int) Math.ceil(1.0f / ((cVar.A / cVar.y.y) / ceil));
    }

    private void b(Axis.c cVar, a aVar, double d, boolean z) {
        boolean j = this.b.j();
        this.b.q();
        if (this.b.o() && cVar.d && this.b.b(cVar.A) != Double.NaN) {
            double b = this.b.b(cVar.A);
            while (b <= this.b.i.b) {
                a(b, cVar, z, j);
                b = this.b.a(b, false);
            }
        }
    }

    private void a(double d, Axis.c cVar, boolean z, boolean z2, a aVar) {
        TickMark a2 = a(d, cVar);
        this.c.add(a2);
        a2.a = d;
        a2.c = z;
        a2.d = z2;
        double b = this.b.b(d, cVar.A, cVar.B);
        boolean a3 = this.f.a(a2, cVar, this.b, b);
        if (this.b.a() && a3) {
            aVar.a++;
        }
        boolean b2 = this.f.b(a2, cVar, this.b, b);
        if (a3 || b2) {
            if (!this.b.a()) {
                aVar.a++;
            }
            a2.d = false;
            a2.e = !this.f.a(a2, cVar, this.b, a3, b2);
        }
    }

    private void a(double d, Axis.c cVar, boolean z, boolean z2) {
        TickMark b = b(cVar);
        this.c.add(b);
        b.a = d;
        b.c = z;
        b.d = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i, PointF pointF, TickMark tickMark, PointF pointF2, Axis.c cVar) {
        int i2 = i - 1;
        while (i2 > 0 && !this.c.get(i2).b) {
            i2--;
        }
        if (i2 < 0 || !this.c.get(i2).b) {
            pointF.x = cVar.a.left;
            pointF.y = cVar.a.bottom;
        } else {
            pointF.x = this.c.get(i2).g.exactCenterX();
            pointF.y = this.c.get(i2).g.exactCenterY();
        }
        pointF2.x = tickMark.g.exactCenterX();
        pointF2.y = tickMark.g.exactCenterY();
        if (pointF2.x > cVar.a.right) {
            pointF2.x = cVar.a.right;
        }
        if (pointF2.y < cVar.a.top) {
            pointF2.y = cVar.a.top;
        }
        if (pointF.x > cVar.a.right) {
            pointF.x = cVar.a.right;
        }
        if (pointF.y < cVar.a.top) {
            pointF.y = cVar.a.top;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        int size = this.c.size();
        this.h = (TickMark[]) this.c.toArray(this.h);
        for (int i = 0; i < size; i++) {
            this.h[i].a();
        }
    }
}
