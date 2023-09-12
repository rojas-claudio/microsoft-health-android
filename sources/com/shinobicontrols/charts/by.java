package com.shinobicontrols.charts;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.View;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class by extends GLSurfaceView implements bw, cl {
    private final x a;
    private int b;
    private float c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public by(Context context, v vVar) {
        super(context);
        this.a = new x(vVar, false, getResources());
        b_(2);
        a(new z());
        setZOrderOnTop(false);
        a(this.a);
        b(0);
        getHolder().setFormat(-3);
    }

    @Override // com.shinobicontrols.charts.GLSurfaceView, android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.a.a();
        super.surfaceDestroyed(holder);
    }

    @Override // android.view.View, com.shinobicontrols.charts.bw
    public void setBackgroundColor(int backgroundColor) {
        this.a.a(backgroundColor);
    }

    @Override // com.shinobicontrols.charts.bw
    public void a(int i) {
        this.b = i;
    }

    @Override // com.shinobicontrols.charts.bw
    public void a(float f) {
        this.c = f;
    }

    @Override // com.shinobicontrols.charts.bw
    public View a() {
        return this;
    }

    @Override // com.shinobicontrols.charts.cl
    public void e() {
        d_();
    }

    @Override // com.shinobicontrols.charts.cl
    public void f() {
        this.a.b();
    }

    @Override // com.shinobicontrols.charts.bw
    public void b_() {
        this.a.c();
    }
}
