package com.shinobicontrols.charts;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import com.microsoft.band.internal.InternalBandConstants;
import java.nio.IntBuffer;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class x implements ak {
    static final Object a = new Object();
    private final v c;
    private final boolean g;
    private final Resources h;
    private boolean j;
    private int k;
    private int l;
    private ah b = new ah(0);
    private final AnimationManager d = new AnimationManager();
    private SChartGLDrawer e = null;
    private final SChartGLErrorHandler f = new SChartGLErrorHandler();
    private boolean i = false;
    private final ai m = new ai();

    /* JADX INFO: Access modifiers changed from: package-private */
    public x(v vVar, boolean z, Resources resources) {
        this.c = vVar;
        this.g = z;
        this.h = resources;
    }

    @Override // com.shinobicontrols.charts.GLSurfaceView.Renderer, com.shinobicontrols.charts.GLTextureView.Renderer
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        synchronized (a) {
            if (this.e != null) {
                this.e.a();
            }
            this.e = new SChartGLDrawer(this.g, this.f);
            this.i = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        synchronized (a) {
            if (this.e != null) {
                this.e.a();
                this.e = null;
            }
            this.m.e();
        }
    }

    @Override // com.shinobicontrols.charts.GLSurfaceView.Renderer, com.shinobicontrols.charts.GLTextureView.Renderer
    @SuppressLint({"WrongCall"})
    public void onDrawFrame(GL10 gl) {
        synchronized (a) {
            try {
                try {
                    List<Series<?>> list = this.c.c;
                    for (int i = 0; i < list.size(); i++) {
                        Series<?> series = list.get(i);
                        if (!this.i) {
                            this.i = series.p.b();
                        }
                    }
                    this.e.setPerformCalculations(this.i);
                    if (this.i) {
                        for (cr crVar : this.c.d.a()) {
                            crVar.a();
                        }
                    }
                    this.i = false;
                    this.e.beginRender(true);
                    for (int i2 = 0; i2 < list.size(); i2++) {
                        Series<?> series2 = list.get(i2);
                        if (!series2.y) {
                            series2.p.a(this.m, this.e);
                        }
                        series2.p.c();
                    }
                    GLES20.glClearColor(this.b.b, this.b.c, this.b.d, this.b.e);
                    GLES20.glClear(17664);
                    this.e.endRender(this.d);
                    if (this.j) {
                        this.c.a(a(this.k, this.l, gl));
                        this.j = false;
                    }
                } catch (RuntimeException e) {
                    cx.c(this.c.getContext().getString(R.string.ChartRendererExceptionInGL));
                    throw e;
                }
            }
        }
    }

    private static Bitmap a(int i, int i2, GL10 gl10) {
        int[] iArr = new int[i * i2];
        IntBuffer wrap = IntBuffer.wrap(iArr);
        wrap.position(0);
        gl10.glReadPixels(0, 0, i, i2, 6408, 5121, wrap);
        int[] iArr2 = new int[i * i2];
        for (int i3 = 0; i3 < i2; i3++) {
            for (int i4 = 0; i4 < i; i4++) {
                int i5 = iArr[(i3 * i) + i4];
                iArr2[(((i2 - i3) - 1) * i) + i4] = (i5 & (-16711936)) | ((i5 << 16) & InternalBandConstants.BAND_SDK_MINOR_VERSION_MASK) | ((i5 >> 16) & 255);
            }
        }
        return Bitmap.createBitmap(iArr2, i, i2, Bitmap.Config.ARGB_8888);
    }

    @Override // com.shinobicontrols.charts.GLSurfaceView.Renderer, com.shinobicontrols.charts.GLTextureView.Renderer
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        synchronized (a) {
            this.k = width;
            this.l = height;
            GLES20.glViewport(0, 0, width, height);
            this.m.a(2.0f / width, 2.0f / height, this.h.getDisplayMetrics());
            this.e.setFrameBufferSize(width, height);
            this.i = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i) {
        synchronized (a) {
            this.b = new ah(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        this.i = true;
    }

    public void c() {
        this.j = true;
    }
}
