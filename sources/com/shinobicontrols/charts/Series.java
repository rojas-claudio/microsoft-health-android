package com.shinobicontrols.charts;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.shinobicontrols.charts.PropertyChangedEvent;
import com.shinobicontrols.charts.SeriesStyle;
import com.shinobicontrols.charts.db;
import com.shinobicontrols.charts.di;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public abstract class Series<T extends SeriesStyle> {
    private am c;
    private am g;
    private am h;
    private String j;
    DataAdapter<?, ?> m;
    v o;
    cu p;
    T q;
    T r;
    cr t;
    SeriesAnimation u;
    SeriesAnimation v;
    SeriesAnimation w;
    boolean x;
    boolean y;
    final ao n = new ao();
    private final al a = new al();
    private final Map<Axis<?, ?>, am> b = new HashMap();
    private final Series<T>.c d = new c(this);
    private final Series<T>.b e = new b();
    private final Series<T>.d f = new d(this);
    SelectionMode s = SelectionMode.NONE;
    private boolean i = true;
    final ae z = ae.a(this);

    /* loaded from: classes.dex */
    public enum SelectionMode {
        NONE,
        SERIES,
        POINT_SINGLE,
        POINT_MULTIPLE
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Drawable a(float f);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract double b();

    abstract T b(dd ddVar, int i, boolean z);

    abstract T d();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract double h();

    abstract void m();

    /* loaded from: classes.dex */
    public enum Orientation {
        HORIZONTAL(0),
        VERTICAL(1);
        
        private int a;

        Orientation(int glOrientation) {
            this.a = glOrientation;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int a() {
            return this.a;
        }
    }

    /* loaded from: classes.dex */
    static class a {
        private final Series<?> c;
        private double a = Double.MAX_VALUE;
        private InternalDataPoint b = null;
        private bz d = null;

        public a(Series<?> series) {
            this.c = series;
        }

        public double a() {
            return this.a;
        }

        public void a(double d) {
            this.a = d;
        }

        public InternalDataPoint b() {
            return this.b;
        }

        public void a(InternalDataPoint internalDataPoint) {
            this.b = internalDataPoint;
        }

        public Series<?> c() {
            return this.c;
        }

        public bz d() {
            return this.d;
        }

        public void a(bz bzVar) {
            this.d = bzVar;
        }

        public void e() {
            this.b = null;
        }

        public boolean a(a aVar) {
            return b(this) && (aVar == null || this.a < aVar.a());
        }

        public static boolean b(a aVar) {
            return (aVar == null || aVar.b == null) ? false : true;
        }
    }

    public final DataAdapter<?, ?> getDataAdapter() {
        return this.m;
    }

    public final void setDataAdapter(DataAdapter<?, ?> dataAdapter) {
        if (dataAdapter == null) {
            if (this.o == null) {
                throw new IllegalArgumentException("Trying to set a null DataAdapter, DataAdapter cannot be null");
            }
            throw new IllegalArgumentException(this.o.getContext().getString(R.string.SeriesNullDataAdapter));
        }
        if (this.m != null) {
            this.c.a();
        }
        this.m = dataAdapter;
        this.c = this.m.a(this.d);
        m();
        r();
    }

    public boolean isSelected() {
        return false;
    }

    public void setSelected(boolean selected) {
    }

    public boolean isPointSelected(int index) {
        if (index >= this.n.c.length || index < 0) {
            cx.a(this.o != null ? this.o.getContext().getString(R.string.SeriesDataPointOutOfRange) : "Attempting to access data point out of range");
            return false;
        }
        return this.n.c[index].h;
    }

    public void setPointSelected(boolean selected, int index) {
        if (index >= this.n.c.length || index < 0) {
            cx.a(this.o != null ? this.o.getContext().getString(R.string.SeriesDataPointOutOfRange) : "Attempting to access data point out of range");
        } else {
            a(selected, index);
        }
    }

    void a(boolean z, int i) {
        a(this.n.c[i], z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(InternalDataPoint internalDataPoint, boolean z) {
        boolean z2 = z != internalDataPoint.h;
        if (z2) {
            internalDataPoint.h = z;
            if (this.o != null) {
                this.o.a((Series<?>) this, internalDataPoint.i);
                this.p.a();
            }
        }
        return z2;
    }

    public T getStyle() {
        return this.q;
    }

    public final void setStyle(T style) {
        if (style == null) {
            if (this.o == null) {
                throw new IllegalArgumentException("Styles may not be null");
            }
            throw new IllegalArgumentException(this.o.getContext().getString(R.string.SeriesStyleIsNull));
        }
        a((Series<T>) style);
    }

    private void a(T t) {
        synchronized (x.a) {
            if (this.q != null) {
                this.g.a();
            }
            this.q = t;
            if (this.q != null) {
                this.g = this.q.a(this.e);
                a_();
            }
        }
    }

    public T getSelectedStyle() {
        return this.r;
    }

    public final void setSelectedStyle(T style) {
        if (style == null) {
            if (this.o == null) {
                throw new IllegalArgumentException("Styles may not be null");
            }
            throw new IllegalArgumentException(this.o.getContext().getString(R.string.SeriesStyleIsNull));
        }
        b(style);
    }

    private void b(T t) {
        synchronized (x.a) {
            if (this.r != null) {
                this.h.a();
            }
            this.r = t;
            if (this.r != null) {
                this.h = this.r.a(this.e);
                a_();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(v vVar) {
        this.o = vVar;
        m();
        r();
    }

    public final ShinobiChart getChart() {
        return this.o;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(dd ddVar, int i, boolean z) {
        if (ddVar != null) {
            T b2 = b(ddVar, i, false);
            T b3 = b(ddVar, i, true);
            if (z || this.q == null) {
                this.q = d();
            }
            if (z || this.r == null) {
                this.r = d();
            }
            this.q.a(b2);
            this.r.a(b3);
            a((Series<T>) this.q);
            b(this.r);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k() {
        m();
        r();
    }

    public final Axis<?, ?> getXAxis() {
        if (this.o != null) {
            return this.o.getXAxisForSeries(this);
        }
        return null;
    }

    public final Axis<?, ?> getYAxis() {
        if (this.o != null) {
            return this.o.getYAxisForSeries(this);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a_() {
        this.p.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class b implements PropertyChangedEvent.Handler {
        private b() {
        }

        @Override // com.shinobicontrols.charts.PropertyChangedEvent.Handler
        public void onPropertyChanged() {
            Series.this.a_();
        }
    }

    void q() {
        m();
        r();
        if (this.o != null && this.o.h != null) {
            this.o.h.b(this);
        }
    }

    /* loaded from: classes.dex */
    private class c implements di.a {
        private final Series<?> b;

        public c(Series<?> series) {
            this.b = series;
        }

        @Override // com.shinobicontrols.charts.di.a
        public final void a() {
            this.b.q();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void r() {
        this.a.a(new di());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public am a(di.a aVar) {
        return this.a.a(di.a, aVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Axis<?, ?> axis) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(cr crVar) {
        this.t = crVar;
    }

    public SelectionMode getSelectionMode() {
        return this.s;
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.s = selectionMode;
    }

    public boolean isShownInLegend() {
        return this.i;
    }

    public void setShownInLegend(boolean shownInLegend) {
        this.i = shownInLegend;
    }

    public String getTitle() {
        return this.j;
    }

    public void setTitle(String title) {
        this.j = title;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Canvas canvas, Rect rect) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean s() {
        return (this.m == null || this.m.isEmpty()) ? false : true;
    }

    public boolean isHidden() {
        return this.y;
    }

    public void setHidden(boolean hidden) {
        synchronized (x.a) {
            if (this.x && this.o != null) {
                if (hidden) {
                    if (!this.y && this.u != this.w) {
                        this.o.j.a((Series<?>) this);
                    }
                } else if ((this.y || this.u != null) && this.u != this.v) {
                    this.o.j.b((Series<?>) this);
                }
            } else {
                a(hidden);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(boolean z) {
        this.y = z;
        if (this.o != null) {
            this.o.b.invalidate();
            this.p.a();
            this.o.b.f();
        }
        r();
    }

    public boolean isAnimationEnabled() {
        return this.x;
    }

    public void enableAnimation(boolean enabled) {
        this.x = enabled;
    }

    public SeriesAnimation getEntryAnimation() {
        return this.v;
    }

    public void setEntryAnimation(SeriesAnimation seriesAnimation) {
        if (seriesAnimation == null) {
            throw new IllegalArgumentException("Series entry animations may not be null");
        }
        this.v = seriesAnimation;
    }

    public SeriesAnimation getExitAnimation() {
        return this.w;
    }

    public void setExitAnimation(SeriesAnimation seriesAnimation) {
        if (seriesAnimation == null) {
            throw new IllegalArgumentException("Series exit animations may not be null");
        }
        this.w = seriesAnimation;
    }

    public boolean isAnimating() {
        return this.u != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(Axis<?, ?> axis) {
        this.b.put(axis, axis.a(this.f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(Axis<?, ?> axis) {
        am amVar = this.b.get(axis);
        if (amVar != null) {
            amVar.a();
            this.b.remove(axis);
        }
    }

    /* loaded from: classes.dex */
    private class d implements db.a {
        private final Series<?> b;

        public d(Series<?> series) {
            this.b = series;
        }

        @Override // com.shinobicontrols.charts.db.a
        public final void a() {
            this.b.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        m();
    }
}
