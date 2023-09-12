package com.shinobicontrols.charts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.shinobicontrols.charts.Axis;
/* loaded from: classes.dex */
public final class TickMark {
    double a;
    boolean d;
    boolean e;
    String f;
    private final Axis<?, ?> h;
    private int i;
    private int j;
    private Typeface k;
    private float l;
    private int r;
    private int s;
    private int t;
    private int u;
    boolean b = false;
    boolean c = false;
    private double m = Double.NEGATIVE_INFINITY;
    private final Paint n = new Paint();
    private final Paint p = new Paint();
    private final Paint q = new Paint();
    private final Point v = new Point();
    final Rect g = new Rect();
    private final Rect w = new Rect();
    private final Path x = new Path();
    private final ChartUtils y = new ChartUtils();
    private final PointF z = new PointF();
    private final PointF A = new PointF();
    private final TextPaint o = new TextPaint();

    /* loaded from: classes.dex */
    public enum ClippingMode {
        NEITHER_PERSIST,
        TICKS_AND_LABELS_PERSIST,
        TICKS_PERSIST
    }

    /* loaded from: classes.dex */
    public enum Orientation {
        HORIZONTAL(0),
        VERTICAL(1),
        DIAGONAL(2);
        
        private final int a;

        Orientation(int xmlValue) {
            this.a = xmlValue;
        }

        public int getXmlValue() {
            return this.a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TickMark(Axis<?, ?> axis) {
        this.h = axis;
        this.o.setTextAlign(Paint.Align.CENTER);
        this.o.setAntiAlias(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Canvas canvas, dg dgVar, int i, Axis.c cVar) {
        int b = (int) (this.h.b(this.a, cVar.A, cVar.B) + 0.5d);
        boolean z = this.a <= this.h.i.b;
        a(canvas, cVar, b, z);
        if (z) {
            a(canvas, cVar, b);
        }
        if (z && this.b && (!(this.h instanceof CategoryAxis) || this.a <= this.h.j.b)) {
            b(canvas, cVar, b);
        }
        if (this.b && cVar.f && (!(this.h instanceof CategoryAxis) || (this.a > this.h.j.a && this.a <= this.h.j.b))) {
            dgVar.a(i, this.z, this, this.A, cVar);
            if ((this.h.a() && this.A.x > this.z.x) || (!this.h.a() && this.A.y < this.z.y)) {
                a(canvas, cVar);
            }
        }
        this.h.b.a(this, this.h);
        a(canvas, z, cVar);
    }

    private void a(Canvas canvas, Axis.c cVar, int i, boolean z) {
        this.h.n.a(this.g, cVar, i, this.b);
        if (z && this.r != cVar.n) {
            this.r = cVar.n;
            this.n.setColor(this.r);
        }
    }

    private void a(Canvas canvas, Axis.c cVar, int i) {
        this.h.n.a(this.v, cVar, i);
        if (this.i != cVar.p) {
            this.o.setColor(cVar.p);
            this.i = cVar.p;
        }
        if (this.j != cVar.q) {
            this.o.setShadowLayer(1.0f, 1.0f, 1.0f, cVar.q);
            this.j = cVar.q;
        }
        if (this.k != cVar.r) {
            this.o.setTypeface(cVar.r);
            this.k = cVar.r;
        }
        if (this.l != cVar.s) {
            this.o.setTextSize(cVar.s);
            this.l = cVar.s;
        }
        if (this.a != this.m) {
            this.f = this.h.a(this.a);
            this.f = Axis.a(this.f) ? MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE : this.f.trim();
            this.m = this.a;
        }
    }

    private void b(Canvas canvas, Axis.c cVar, int i) {
        this.p.setStrokeWidth(at.a(this.h.a, cVar.i));
        this.h.n.c(this.x, cVar, i, this.p);
        this.p.setStyle(Paint.Style.STROKE);
        if (this.s != cVar.o) {
            this.s = cVar.o;
            this.p.setColor(this.s);
        }
        if (cVar.u) {
            this.p.setPathEffect(cVar.v);
        }
    }

    private void a(Canvas canvas, Axis.c cVar) {
        if (this.t != cVar.w) {
            this.t = cVar.w;
        }
        if (this.u != cVar.x) {
            this.u = cVar.x;
        }
        if (this.c) {
            this.q.setColor(this.u);
        } else {
            this.q.setColor(this.t);
        }
        this.h.n.c(this.w, cVar, this.z, this.A);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        this.m = Double.NEGATIVE_INFINITY;
    }

    private void a(Canvas canvas, boolean z, Axis.c cVar) {
        Rect a = this.y.a(this.v.x, this.v.y, this.h.x != null ? this.h.x : this.h.w, this.h.getStyle().getTickStyle().getLabelTextSize(), this.h.getStyle().getTickStyle().getLabelTypeface(), this.h.b);
        if (z && !this.h.b.a(canvas, this, a, this.g, this.h)) {
            if (this.e) {
                canvas.drawRect(this.g, this.n);
            }
            if (this.d) {
                if (cVar.t != Orientation.HORIZONTAL) {
                    canvas.save();
                    canvas.rotate(cVar.t == Orientation.DIAGONAL ? -45.0f : -90.0f, this.v.x, this.v.y);
                }
                ChartUtils.drawText(canvas, this.f, this.v.x, this.v.y, this.o);
                if (cVar.t != Orientation.HORIZONTAL) {
                    canvas.restore();
                }
            }
        }
        if (z && this.b && cVar.e && (!(this.h instanceof CategoryAxis) || this.a <= this.h.j.b)) {
            canvas.drawPath(this.x, this.p);
        }
        if (this.b && cVar.f) {
            if (!(this.h instanceof CategoryAxis) || (this.a > this.h.j.a && this.a <= this.h.j.b)) {
                if ((this.h.a() && this.A.x > this.z.x) || (!this.h.a() && this.A.y < this.z.y)) {
                    canvas.drawRect(this.w, this.q);
                }
            }
        }
    }

    public boolean isMajor() {
        return this.b;
    }

    public Object getValue() {
        return this.h.transformInternalValueToUser(this.a);
    }

    public boolean isLabelShown() {
        return this.d;
    }

    public void setLabelShown(boolean showLabel) {
        this.d = showLabel;
    }

    public boolean isLineShown() {
        return this.e;
    }

    public void setLineShown(boolean showLine) {
        this.e = showLine;
    }

    public String getLabelText() {
        return this.f;
    }

    public void setLabelText(String labelText) {
        this.f = labelText;
    }

    public Point getLabelCenter() {
        return this.v;
    }

    public Paint getLinePaint() {
        return this.n;
    }

    public TextPaint getLabelPaint() {
        return this.o;
    }
}
