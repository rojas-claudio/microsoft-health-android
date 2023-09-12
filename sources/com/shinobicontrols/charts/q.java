package com.shinobicontrols.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.shinobicontrols.charts.Annotation;
import com.shinobicontrols.charts.ShinobiChart;
/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"ViewConstructor"})
/* loaded from: classes.dex */
public class q extends ViewGroup {
    final w a;
    private final bn b;
    private final cy c;
    private float d;
    private float e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(Context context, w wVar) {
        super(context);
        this.d = 0.0f;
        this.e = 0.0f;
        setWillNotDraw(false);
        this.a = wVar;
        this.b = new bn(wVar.a);
        this.c = new cy(wVar.a, this.b);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        event.offsetLocation(this.d, this.e);
        return this.c.a(event) || super.onTouchEvent(event);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        int size2 = View.MeasureSpec.getSize(heightMeasureSpec);
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE);
        if (this.a.a.h != null) {
            this.a.a.h.a(makeMeasureSpec, makeMeasureSpec2);
        }
        this.a.a.l.a(makeMeasureSpec, makeMeasureSpec2, Annotation.Position.IN_FRONT_OF_DATA);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.a.a.h != null) {
            this.a.a.h.a(this.a.b.left, this.a.b.top, this.a.b.right, this.a.b.bottom);
        }
        this.a.a.l.a(this.a.b.left, this.a.b.top, this.a.b.right, this.a.b.bottom, Annotation.Position.IN_FRONT_OF_DATA);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.clipRect(this.a.b);
        this.a.b(canvas);
        this.a.c(canvas);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(ShinobiChart.OnGestureListener onGestureListener) {
        this.b.a(onGestureListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(ShinobiChart.OnGestureListener onGestureListener) {
        this.b.b(onGestureListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(ShinobiChart.OnGestureListener onGestureListener) {
        this.b.c(onGestureListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(ShinobiChart.OnGestureListener onGestureListener) {
        this.b.d(onGestureListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(float f) {
        this.d = f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(float f) {
        this.e = f;
    }
}
