package com.shinobicontrols.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.View;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class bx extends GLTextureView implements bw, cl {
    private final x a;
    private int b;
    private float c;

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public bx(Context context, v vVar) {
        super(context);
        this.a = new x(vVar, false, getResources());
        a_(2);
        a(new z());
        a(this.a);
        b(0);
        setOpaque(false);
        this.a.a(0);
    }

    @Override // com.shinobicontrols.charts.GLTextureView
    public void b(SurfaceTexture surfaceTexture) {
        this.a.a();
        super.b(surfaceTexture);
    }

    @Override // android.view.View, com.shinobicontrols.charts.bw
    public void setBackgroundColor(int backgroundColor) {
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
        c_();
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
