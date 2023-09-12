package com.shinobicontrols.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import com.shinobicontrols.charts.Annotation;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.Crosshair;
import com.shinobicontrols.charts.ShinobiChart;
/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"ViewConstructor"})
/* loaded from: classes.dex */
public class w extends ViewGroup {
    final v a;
    final Rect b;
    int c;
    int d;
    int e;
    int f;
    int g;
    int h;
    private bw i;
    private q j;
    private s k;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w(Context context, v vVar) {
        super(context);
        this.b = new Rect();
        this.a = vVar;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        a(measuredWidth, measuredHeight);
        this.g = (measuredWidth - this.e) - this.f;
        this.h = (measuredHeight - this.d) - this.c;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(measuredHeight, 1073741824);
        this.j.measure(makeMeasureSpec, makeMeasureSpec2);
        this.k.measure(makeMeasureSpec, makeMeasureSpec2);
    }

    private void a(int i, int i2) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE);
        if (i > 0) {
            for (int i3 = 0; i3 < this.a.e.a.length; i3++) {
                Axis<?, ?> axis = this.a.e.a[i3];
                a(axis, makeMeasureSpec, makeMeasureSpec2);
                axis.d(i);
                axis.a(true);
            }
        }
        if (i2 > 0) {
            for (int i4 = 0; i4 < this.a.f.a.length; i4++) {
                Axis<?, ?> axis2 = this.a.f.a[i4];
                a(axis2, makeMeasureSpec, makeMeasureSpec2);
                axis2.d(i2);
                axis2.a(true);
            }
        }
        a(this.a.e);
        a(this.a.f);
        k();
    }

    private void a(Axis<?, ?> axis, int i, int i2) {
        Title t = axis.t();
        if (t != null && t.getVisibility() != 8) {
            measureChildWithMargins(t, i, 0, i2, 0);
        }
    }

    private static void a(h hVar) {
        int i;
        int i2 = 0;
        if (hVar.b()) {
            Axis<?, ?> a = hVar.a();
            if (a.d == Axis.Position.REVERSE) {
                i = 0 - a.o;
            } else {
                i2 = a.o + 0;
                i = 0;
            }
            int i3 = i2;
            int i4 = i;
            for (int i5 = 1; i5 < hVar.a.length; i5++) {
                Axis<?, ?> axis = hVar.a[i5];
                if (axis.d == Axis.Position.REVERSE) {
                    axis.h = i4;
                    i4 -= axis.o;
                } else {
                    axis.h = i3;
                    i3 += axis.o;
                }
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (l <= r && t <= b) {
            this.b.set(0, 0, r - l, b - t);
            this.b.left += this.e;
            this.b.top += this.d;
            this.b.right -= this.f;
            this.b.bottom -= this.c;
            if (!this.b.isEmpty()) {
                m();
                this.k.layout(0, 0, r - l, b - t);
                at.b(this.i.a(), this.b);
                this.j.a(-this.e);
                this.j.b(-this.d);
                if (this.a.f()) {
                    a(l, t, r, b);
                }
                f();
                this.j.layout(0, 0, r - l, b - t);
            }
        }
    }

    private void m() {
        Crosshair crosshair = this.a.h;
        if (crosshair != null && crosshair.isActive()) {
            crosshair.g();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (!this.b.isEmpty()) {
            super.dispatchDraw(canvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(ChartStyle chartStyle) {
        this.k.setBackgroundColor(chartStyle.getCanvasBackgroundColor());
        a.a(this, (Drawable) null);
        a.a(this.j, (Drawable) null);
        this.i.setBackgroundColor(chartStyle.getPlotAreaBackgroundColor());
        this.i.a(chartStyle.d());
        this.i.a(chartStyle.e());
        for (Axis<?, ?> axis : this.a.getAllXAxes()) {
            axis.v();
        }
        for (Axis<?, ?> axis2 : this.a.getAllYAxes()) {
            axis2.v();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        this.k = new s(getContext(), this);
        this.k.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        addView(this.k);
        this.i = n();
        this.i.setLayoutParams(new ViewGroup.MarginLayoutParams(-1, -1));
        addView(this.i.a());
        this.j = new q(getContext(), this);
        this.j.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        addView(this.j);
    }

    private bw n() {
        return a.a(getContext(), this.a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        if (this.a.l() || this.a.f()) {
            invalidate();
            requestLayout();
            Crosshair crosshair = this.a.h;
            if (crosshair != null && crosshair.isActive()) {
                crosshair.c();
            }
            this.a.l.a();
        }
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        c();
        d();
    }

    void c() {
        if (this.k != null) {
            this.k.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d() {
        if (this.j != null) {
            this.j.invalidate();
        }
    }

    public void e() {
        this.i.f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        this.i.e();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g() {
        if (this.j != null) {
            this.j.forceLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h() {
        if (this.k != null) {
            this.k.forceLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i() {
        this.i.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        this.i.b();
    }

    void k() {
        this.c = i.a(this.a.e, Axis.Position.NORMAL);
        this.d = i.a(this.a.e, Axis.Position.REVERSE);
        this.e = i.a(this.a.f, Axis.Position.NORMAL);
        this.f = i.a(this.a.f, Axis.Position.REVERSE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Canvas canvas) {
        for (int i = 0; i < this.a.e.a.length; i++) {
            this.a.e.a[i].a(canvas, this.b);
        }
        for (int i2 = 0; i2 < this.a.f.a.length; i2++) {
            this.a.f.a[i2].a(canvas, this.b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Canvas canvas) {
        for (Series<?> series : this.a.c) {
            if (!series.y) {
                series.a(canvas, this.b);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(Canvas canvas) {
        Crosshair crosshair = this.a.h;
        if (crosshair != null && !this.a.l() && crosshair.a == Crosshair.a.SHOWN) {
            crosshair.b(canvas, this.b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(ShinobiChart.OnGestureListener onGestureListener) {
        this.j.a(onGestureListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(ShinobiChart.OnGestureListener onGestureListener) {
        this.j.b(onGestureListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(ShinobiChart.OnGestureListener onGestureListener) {
        this.j.c(onGestureListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(ShinobiChart.OnGestureListener onGestureListener) {
        this.j.d(onGestureListener);
    }

    private void a(int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        int i6 = i4 - i2;
        for (int i7 = 0; i7 < this.a.e.a.length; i7++) {
            this.a.e.a[i7].a(this.b, i5, i6);
        }
        for (int i8 = 0; i8 < this.a.f.a.length; i8++) {
            this.a.f.a[i8].a(this.b, i5, i6);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Tooltip tooltip) {
        this.j.addView(tooltip);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Tooltip tooltip) {
        this.j.removeView(tooltip);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(View view, Annotation.Position position) {
        if (position == Annotation.Position.BEHIND_DATA) {
            this.k.addView(view);
        } else {
            this.j.addView(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(View view, Annotation.Position position) {
        if (position == Annotation.Position.BEHIND_DATA) {
            this.k.removeView(view);
        } else {
            this.j.removeView(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l() {
        this.i.b_();
    }

    public void a(Bitmap bitmap) {
        this.k.a(bitmap);
    }
}
