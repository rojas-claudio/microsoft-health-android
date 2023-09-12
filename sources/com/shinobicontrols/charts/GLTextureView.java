package com.shinobicontrols.charts;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLDebugHelper;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import com.microsoft.band.device.DeviceConstants;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
/* JADX INFO: Access modifiers changed from: package-private */
@TargetApi(14)
/* loaded from: classes.dex */
public class GLTextureView extends TextureView implements TextureView.SurfaceTextureListener {
    private static final g a = new g();
    private final WeakReference<GLTextureView> b;
    private f c;
    private Renderer d;
    private boolean e;
    private EGLConfigChooser f;
    private EGLContextFactory g;
    private EGLWindowSurfaceFactory h;
    private GLWrapper i;
    private int j;
    private int k;
    private boolean l;

    /* loaded from: classes.dex */
    public interface EGLConfigChooser {
        EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay);
    }

    /* loaded from: classes.dex */
    public interface EGLContextFactory {
        EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig);

        void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext);
    }

    /* loaded from: classes.dex */
    public interface EGLWindowSurfaceFactory {
        EGLSurface createWindowSurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj);

        void destroySurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface);
    }

    /* loaded from: classes.dex */
    public interface GLWrapper {
        GL wrap(GL gl);
    }

    /* loaded from: classes.dex */
    public interface Renderer {
        void onDrawFrame(GL10 gl10);

        void onSurfaceChanged(GL10 gl10, int i, int i2);

        void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig);
    }

    public GLTextureView(Context context) {
        super(context);
        this.b = new WeakReference<>(this);
        e();
    }

    protected void finalize() throws Throwable {
        try {
            if (this.c != null) {
                this.c.h();
            }
        } finally {
            super.finalize();
        }
    }

    private void e() {
        setSurfaceTextureListener(this);
        addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.shinobicontrols.charts.GLTextureView.1
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                GLTextureView.this.a(GLTextureView.this.getSurfaceTexture(), 0, right - left, bottom - top);
            }
        });
    }

    public void a(Renderer renderer) {
        f();
        if (this.f == null) {
            this.f = new i(true);
        }
        if (this.g == null) {
            this.g = new c();
        }
        if (this.h == null) {
            this.h = new d();
        }
        this.d = renderer;
        this.c = new f(this.b);
        this.c.start();
    }

    public void a(EGLConfigChooser eGLConfigChooser) {
        f();
        this.f = eGLConfigChooser;
    }

    public void a_(int i2) {
        f();
        this.k = i2;
    }

    public void b(int i2) {
        this.c.a(i2);
    }

    public void c_() {
        this.c.c();
    }

    public void a(SurfaceTexture surfaceTexture) {
        this.c.d();
    }

    public void b(SurfaceTexture surfaceTexture) {
        this.c.e();
    }

    public void a(SurfaceTexture surfaceTexture, int i2, int i3, int i4) {
        this.c.a(i3, i4);
    }

    public void b() {
        this.c.f();
    }

    public void c() {
        this.c.g();
    }

    @Override // android.view.TextureView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.e && this.d != null) {
            int b2 = this.c != null ? this.c.b() : 1;
            this.c = new f(this.b);
            if (b2 != 1) {
                this.c.a(b2);
            }
            this.c.start();
        }
        this.e = false;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        if (this.c != null) {
            this.c.h();
        }
        this.e = true;
        super.onDetachedFromWindow();
    }

    /* loaded from: classes.dex */
    private class c implements EGLContextFactory {
        private final int b;

        private c() {
            this.b = 12440;
        }

        @Override // com.shinobicontrols.charts.GLTextureView.EGLContextFactory
        public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig config) {
            int[] iArr = {12440, GLTextureView.this.k, 12344};
            EGLContext eGLContext = EGL10.EGL_NO_CONTEXT;
            if (GLTextureView.this.k == 0) {
                iArr = null;
            }
            return egl.eglCreateContext(display, config, eGLContext, iArr);
        }

        @Override // com.shinobicontrols.charts.GLTextureView.EGLContextFactory
        public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
            if (!egl.eglDestroyContext(display, context)) {
                Log.e("DefaultContextFactory", "display:" + display + " context: " + context);
                e.a("eglDestroyContex", egl.eglGetError());
            }
        }
    }

    /* loaded from: classes.dex */
    private static class d implements EGLWindowSurfaceFactory {
        private d() {
        }

        @Override // com.shinobicontrols.charts.GLTextureView.EGLWindowSurfaceFactory
        public EGLSurface createWindowSurface(EGL10 egl, EGLDisplay display, EGLConfig config, Object nativeWindow) {
            try {
                return egl.eglCreateWindowSurface(display, config, nativeWindow, null);
            } catch (IllegalArgumentException e) {
                Log.e("GLTextureView", "eglCreateWindowSurface", e);
                return null;
            }
        }

        @Override // com.shinobicontrols.charts.GLTextureView.EGLWindowSurfaceFactory
        public void destroySurface(EGL10 egl, EGLDisplay display, EGLSurface surface) {
            egl.eglDestroySurface(display, surface);
        }
    }

    /* loaded from: classes.dex */
    private abstract class a implements EGLConfigChooser {
        protected int[] a;

        abstract EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

        public a(int[] iArr) {
            this.a = a(iArr);
        }

        @Override // com.shinobicontrols.charts.GLTextureView.EGLConfigChooser
        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
            int[] iArr = new int[1];
            if (!egl.eglChooseConfig(display, this.a, null, 0, iArr)) {
                throw new IllegalArgumentException("eglChooseConfig failed");
            }
            int i = iArr[0];
            if (i <= 0) {
                throw new IllegalArgumentException("No configs match configSpec");
            }
            EGLConfig[] eGLConfigArr = new EGLConfig[i];
            if (!egl.eglChooseConfig(display, this.a, eGLConfigArr, i, iArr)) {
                throw new IllegalArgumentException("eglChooseConfig#2 failed");
            }
            EGLConfig a = a(egl, display, eGLConfigArr);
            if (a == null) {
                throw new IllegalArgumentException("No config chosen");
            }
            return a;
        }

        private int[] a(int[] iArr) {
            if (GLTextureView.this.k == 2) {
                int length = iArr.length;
                int[] iArr2 = new int[length + 2];
                System.arraycopy(iArr, 0, iArr2, 0, length - 1);
                iArr2[length - 1] = 12352;
                iArr2[length] = 4;
                iArr2[length + 1] = 12344;
                return iArr2;
            }
            return iArr;
        }
    }

    /* loaded from: classes.dex */
    private class b extends a {
        protected int c;
        protected int d;
        protected int e;
        protected int f;
        protected int g;
        protected int h;
        private final int[] j;

        public b(int i, int i2, int i3, int i4, int i5, int i6) {
            super(new int[]{12324, i, 12323, i2, 12322, i3, 12321, i4, 12325, i5, 12326, i6, 12344});
            this.j = new int[1];
            this.c = i;
            this.d = i2;
            this.e = i3;
            this.f = i4;
            this.g = i5;
            this.h = i6;
        }

        @Override // com.shinobicontrols.charts.GLTextureView.a
        public EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr) {
            for (EGLConfig eGLConfig : eGLConfigArr) {
                int a = a(egl10, eGLDisplay, eGLConfig, 12325, 0);
                int a2 = a(egl10, eGLDisplay, eGLConfig, 12326, 0);
                if (a >= this.g && a2 >= this.h) {
                    int a3 = a(egl10, eGLDisplay, eGLConfig, 12324, 0);
                    int a4 = a(egl10, eGLDisplay, eGLConfig, 12323, 0);
                    int a5 = a(egl10, eGLDisplay, eGLConfig, 12322, 0);
                    int a6 = a(egl10, eGLDisplay, eGLConfig, 12321, 0);
                    if (a3 == this.c && a4 == this.d && a5 == this.e && a6 == this.f) {
                        return eGLConfig;
                    }
                }
            }
            return null;
        }

        private int a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            if (egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, this.j)) {
                return this.j[0];
            }
            return i2;
        }
    }

    /* loaded from: classes.dex */
    private class i extends b {
        public i(boolean z) {
            super(8, 8, 8, 0, z ? 16 : 0, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class e {
        EGL10 a;
        EGLDisplay b;
        EGLSurface c;
        EGLConfig d;
        EGLContext e;
        private final WeakReference<GLTextureView> f;

        public e(WeakReference<GLTextureView> weakReference) {
            this.f = weakReference;
        }

        public void a() {
            this.a = (EGL10) EGLContext.getEGL();
            this.b = this.a.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (this.b == EGL10.EGL_NO_DISPLAY) {
                throw new RuntimeException("eglGetDisplay failed");
            }
            if (!this.a.eglInitialize(this.b, new int[2])) {
                throw new RuntimeException("eglInitialize failed");
            }
            GLTextureView gLTextureView = this.f.get();
            if (gLTextureView != null) {
                this.d = gLTextureView.f.chooseConfig(this.a, this.b);
                this.e = gLTextureView.g.createContext(this.a, this.b, this.d);
            } else {
                this.d = null;
                this.e = null;
            }
            if (this.e == null || this.e == EGL10.EGL_NO_CONTEXT) {
                this.e = null;
                a("createContext");
            }
            this.c = null;
        }

        public boolean b() {
            if (this.a == null) {
                throw new RuntimeException("egl not initialized");
            }
            if (this.b == null) {
                throw new RuntimeException("eglDisplay not initialized");
            }
            if (this.d == null) {
                throw new RuntimeException("mEglConfig not initialized");
            }
            g();
            GLTextureView gLTextureView = this.f.get();
            if (gLTextureView != null) {
                this.c = gLTextureView.h.createWindowSurface(this.a, this.b, this.d, gLTextureView.getSurfaceTexture());
            } else {
                this.c = null;
            }
            if (this.c == null || this.c == EGL10.EGL_NO_SURFACE) {
                if (this.a.eglGetError() == 12299) {
                    Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
                }
                return false;
            } else if (!this.a.eglMakeCurrent(this.b, this.c, this.c, this.e)) {
                a("EGLHelper", "eglMakeCurrent", this.a.eglGetError());
                return false;
            } else {
                return true;
            }
        }

        GL c() {
            h hVar;
            GL gl = this.e.getGL();
            GLTextureView gLTextureView = this.f.get();
            if (gLTextureView != null) {
                if (gLTextureView.i != null) {
                    gl = gLTextureView.i.wrap(gl);
                }
                if ((gLTextureView.j & 3) != 0) {
                    int i = 0;
                    if ((gLTextureView.j & 1) != 0) {
                        i = 1;
                    }
                    if ((gLTextureView.j & 2) == 0) {
                        hVar = null;
                    } else {
                        hVar = new h();
                    }
                    return GLDebugHelper.wrap(gl, i, hVar);
                }
                return gl;
            }
            return gl;
        }

        public int d() {
            return !this.a.eglSwapBuffers(this.b, this.c) ? this.a.eglGetError() : DeviceConstants.LOG_MAX_CHUNK;
        }

        public void e() {
            g();
        }

        private void g() {
            if (this.c != null && this.c != EGL10.EGL_NO_SURFACE) {
                this.a.eglMakeCurrent(this.b, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                GLTextureView gLTextureView = this.f.get();
                if (gLTextureView != null) {
                    gLTextureView.h.destroySurface(this.a, this.b, this.c);
                }
                this.c = null;
            }
        }

        public void f() {
            if (this.e != null) {
                GLTextureView gLTextureView = this.f.get();
                if (gLTextureView != null) {
                    gLTextureView.g.destroyContext(this.a, this.b, this.e);
                }
                this.e = null;
            }
            if (this.b != null) {
                this.a.eglTerminate(this.b);
                this.b = null;
            }
        }

        private void a(String str) {
            a(str, this.a.eglGetError());
        }

        public static void a(String str, int i) {
            throw new RuntimeException(b(str, i));
        }

        public static void a(String str, String str2, int i) {
            Log.w(str, b(str2, i));
        }

        public static String b(String str, int i) {
            return str + " failed: ";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class f extends Thread {
        private boolean a;
        private boolean b;
        private boolean c;
        private boolean d;
        private boolean e;
        private boolean f;
        private boolean g;
        private boolean h;
        private boolean i;
        private boolean j;
        private boolean k;
        private boolean p;
        private e s;
        private final WeakReference<GLTextureView> t;
        private final ArrayList<Runnable> q = new ArrayList<>();
        private boolean r = true;
        private int l = 0;
        private int m = 0;
        private boolean o = true;
        private int n = 1;

        f(WeakReference<GLTextureView> weakReference) {
            this.t = weakReference;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            setName("GLThread " + getId());
            try {
                l();
            } catch (InterruptedException e) {
            } finally {
                GLTextureView.a.a(this);
            }
        }

        private void j() {
            if (this.i) {
                this.i = false;
                this.s.e();
            }
        }

        private void k() {
            if (this.h) {
                this.s.f();
                this.h = false;
                GLTextureView.a.c(this);
            }
        }

        private void l() throws InterruptedException {
            boolean z;
            int i;
            Runnable remove;
            boolean z2;
            boolean z3;
            boolean z4;
            boolean z5;
            boolean z6;
            boolean z7;
            int i2;
            boolean z8;
            GL10 gl10;
            boolean z9;
            boolean z10;
            boolean z11;
            boolean z12;
            boolean z13;
            boolean z14;
            int i3;
            int i4;
            this.s = new e(this.t);
            this.h = false;
            this.i = false;
            boolean z15 = false;
            GL10 gl102 = null;
            int i5 = 0;
            boolean z16 = false;
            boolean z17 = false;
            boolean z18 = false;
            boolean z19 = false;
            boolean z20 = false;
            boolean z21 = false;
            Runnable runnable = null;
            int i6 = 0;
            boolean z22 = false;
            while (true) {
                try {
                    synchronized (GLTextureView.a) {
                        while (!this.a) {
                            if (!this.q.isEmpty()) {
                                z = z22;
                                i = i6;
                                remove = this.q.remove(0);
                                z2 = z21;
                                z3 = z20;
                                z4 = z19;
                                z5 = z18;
                                z6 = z17;
                                z7 = z16;
                                i2 = i5;
                            } else {
                                if (this.d == this.c) {
                                    z9 = false;
                                } else {
                                    boolean z23 = this.c;
                                    this.d = this.c;
                                    GLTextureView.a.notifyAll();
                                    z9 = z23;
                                }
                                if (this.k) {
                                    j();
                                    k();
                                    this.k = false;
                                    z16 = true;
                                }
                                if (z19) {
                                    j();
                                    k();
                                    z19 = false;
                                }
                                if (z9 && this.i) {
                                    j();
                                }
                                if (z9 && this.h) {
                                    GLTextureView gLTextureView = this.t.get();
                                    if (!(gLTextureView == null ? false : gLTextureView.l) || GLTextureView.a.a()) {
                                        k();
                                    }
                                }
                                if (z9 && GLTextureView.a.b()) {
                                    this.s.f();
                                }
                                if (!this.e && !this.g) {
                                    if (this.i) {
                                        j();
                                    }
                                    this.g = true;
                                    this.f = false;
                                    GLTextureView.a.notifyAll();
                                }
                                if (this.e && this.g) {
                                    this.g = false;
                                    GLTextureView.a.notifyAll();
                                }
                                if (z22) {
                                    z17 = false;
                                    z22 = false;
                                    this.p = true;
                                    GLTextureView.a.notifyAll();
                                }
                                if (m()) {
                                    if (!this.h) {
                                        if (!z16) {
                                            if (GLTextureView.a.b(this)) {
                                                try {
                                                    this.s.a();
                                                    this.h = true;
                                                    z15 = true;
                                                    GLTextureView.a.notifyAll();
                                                } catch (RuntimeException e) {
                                                    GLTextureView.a.c(this);
                                                    throw e;
                                                }
                                            }
                                        } else {
                                            z16 = false;
                                        }
                                    }
                                    if (!this.h || this.i) {
                                        z10 = z18;
                                        z11 = z20;
                                    } else {
                                        this.i = true;
                                        z21 = true;
                                        z10 = true;
                                        z11 = true;
                                    }
                                    if (this.i) {
                                        if (this.r) {
                                            z14 = true;
                                            i4 = this.l;
                                            i3 = this.m;
                                            z13 = true;
                                            z12 = true;
                                            this.r = false;
                                        } else {
                                            z12 = z21;
                                            int i7 = i6;
                                            z13 = z17;
                                            z14 = z10;
                                            i3 = i5;
                                            i4 = i7;
                                        }
                                        this.o = false;
                                        GLTextureView.a.notifyAll();
                                        z3 = z11;
                                        z6 = z13;
                                        remove = runnable;
                                        z = z22;
                                        i = i4;
                                        int i8 = i3;
                                        z2 = z12;
                                        z4 = z19;
                                        z5 = z14;
                                        z7 = z16;
                                        i2 = i8;
                                    } else {
                                        z20 = z11;
                                        z18 = z10;
                                    }
                                }
                                GLTextureView.a.wait();
                            }
                        }
                        synchronized (GLTextureView.a) {
                            j();
                            k();
                        }
                        return;
                    }
                    if (remove != null) {
                        remove.run();
                        i5 = i2;
                        z16 = z7;
                        z17 = z6;
                        z18 = z5;
                        z19 = z4;
                        z20 = z3;
                        z21 = z2;
                        boolean z24 = z;
                        runnable = null;
                        i6 = i;
                        z22 = z24;
                    } else {
                        if (!z2) {
                            z8 = z2;
                        } else if (this.s.b()) {
                            synchronized (GLTextureView.a) {
                                this.j = true;
                                GLTextureView.a.notifyAll();
                            }
                            z8 = false;
                        } else {
                            synchronized (GLTextureView.a) {
                                this.j = true;
                                this.f = true;
                                GLTextureView.a.notifyAll();
                            }
                            i5 = i2;
                            z16 = z7;
                            z17 = z6;
                            z18 = z5;
                            z19 = z4;
                            z20 = z3;
                            z21 = z2;
                            boolean z25 = z;
                            runnable = remove;
                            i6 = i;
                            z22 = z25;
                        }
                        if (z3) {
                            GL10 gl103 = (GL10) this.s.c();
                            GLTextureView.a.a(gl103);
                            z3 = false;
                            gl10 = gl103;
                        } else {
                            gl10 = gl102;
                        }
                        if (z15) {
                            GLTextureView gLTextureView2 = this.t.get();
                            if (gLTextureView2 != null) {
                                gLTextureView2.d.onSurfaceCreated(gl10, this.s.d);
                            }
                            z15 = false;
                        }
                        if (z5) {
                            GLTextureView gLTextureView3 = this.t.get();
                            if (gLTextureView3 != null) {
                                gLTextureView3.d.onSurfaceChanged(gl10, i, i2);
                            }
                            z5 = false;
                        }
                        GLTextureView gLTextureView4 = this.t.get();
                        if (gLTextureView4 != null) {
                            gLTextureView4.d.onDrawFrame(gl10);
                        }
                        int d = this.s.d();
                        switch (d) {
                            case DeviceConstants.LOG_MAX_CHUNK /* 12288 */:
                                break;
                            case 12302:
                                z4 = true;
                                break;
                            default:
                                e.a("GLThread", "eglSwapBuffers", d);
                                synchronized (GLTextureView.a) {
                                    this.f = true;
                                    GLTextureView.a.notifyAll();
                                    break;
                                }
                        }
                        boolean z26 = z6 ? true : z;
                        runnable = remove;
                        gl102 = gl10;
                        i6 = i;
                        z22 = z26;
                        boolean z27 = z7;
                        z17 = z6;
                        z18 = z5;
                        z19 = z4;
                        z20 = z3;
                        z21 = z8;
                        i5 = i2;
                        z16 = z27;
                    }
                } catch (Throwable th) {
                    synchronized (GLTextureView.a) {
                        j();
                        k();
                        throw th;
                    }
                }
            }
        }

        public boolean a() {
            return this.h && this.i && m();
        }

        private boolean m() {
            return !this.d && this.e && !this.f && this.l > 0 && this.m > 0 && (this.o || this.n == 1);
        }

        public void a(int i) {
            if (i >= 0 && i <= 1) {
                synchronized (GLTextureView.a) {
                    this.n = i;
                    GLTextureView.a.notifyAll();
                }
                return;
            }
            throw new IllegalArgumentException("renderMode");
        }

        public int b() {
            int i;
            synchronized (GLTextureView.a) {
                i = this.n;
            }
            return i;
        }

        public void c() {
            synchronized (GLTextureView.a) {
                this.o = true;
                GLTextureView.a.notifyAll();
            }
        }

        public void d() {
            synchronized (GLTextureView.a) {
                this.e = true;
                this.j = false;
                GLTextureView.a.notifyAll();
                while (this.g && !this.j && !this.b) {
                    try {
                        GLTextureView.a.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void e() {
            synchronized (GLTextureView.a) {
                this.e = false;
                GLTextureView.a.notifyAll();
                while (!this.g && !this.b) {
                    try {
                        GLTextureView.a.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void f() {
            synchronized (GLTextureView.a) {
                this.c = true;
                GLTextureView.a.notifyAll();
                while (!this.b && !this.d) {
                    try {
                        GLTextureView.a.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void g() {
            synchronized (GLTextureView.a) {
                this.c = false;
                this.o = true;
                this.p = false;
                GLTextureView.a.notifyAll();
                while (!this.b && this.d && !this.p) {
                    try {
                        GLTextureView.a.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void a(int i, int i2) {
            synchronized (GLTextureView.a) {
                this.l = i;
                this.m = i2;
                this.r = true;
                this.o = true;
                this.p = false;
                GLTextureView.a.notifyAll();
                while (!this.b && !this.d && !this.p && a()) {
                    try {
                        GLTextureView.a.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void h() {
            synchronized (GLTextureView.a) {
                this.a = true;
                GLTextureView.a.notifyAll();
                while (!this.b) {
                    try {
                        GLTextureView.a.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void i() {
            this.k = true;
            GLTextureView.a.notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class h extends Writer {
        private final StringBuilder a = new StringBuilder();

        h() {
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            a();
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
            a();
        }

        @Override // java.io.Writer
        public void write(char[] buf, int offset, int count) {
            for (int i = 0; i < count; i++) {
                char c = buf[offset + i];
                if (c == '\n') {
                    a();
                } else {
                    this.a.append(c);
                }
            }
        }

        private void a() {
            if (this.a.length() > 0) {
                Log.v("GLSurfaceView", this.a.toString());
                this.a.delete(0, this.a.length());
            }
        }
    }

    private void f() {
        if (this.c != null) {
            throw new IllegalStateException("setRenderer has already been called for this instance.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class g {
        private static String a = "GLThreadManager";
        private boolean b;
        private int c;
        private boolean d;
        private boolean e;
        private boolean f;
        private f g;

        private g() {
        }

        public synchronized void a(f fVar) {
            fVar.b = true;
            if (this.g == fVar) {
                this.g = null;
            }
            notifyAll();
        }

        public boolean b(f fVar) {
            if (this.g == fVar || this.g == null) {
                this.g = fVar;
                notifyAll();
                return true;
            }
            c();
            if (this.e) {
                return true;
            }
            if (this.g != null) {
                this.g.i();
            }
            return false;
        }

        public void c(f fVar) {
            if (this.g == fVar) {
                this.g = null;
            }
            notifyAll();
        }

        public synchronized boolean a() {
            return this.f;
        }

        public synchronized boolean b() {
            c();
            return !this.e;
        }

        public synchronized void a(GL10 gl10) {
            synchronized (this) {
                if (!this.d) {
                    c();
                    String glGetString = gl10.glGetString(7937);
                    if (this.c < 131072) {
                        this.e = !glGetString.startsWith("Q3Dimension MSM7500 ");
                        notifyAll();
                    }
                    this.f = this.e ? false : true;
                    this.d = true;
                }
            }
        }

        private void c() {
            if (!this.b) {
                this.b = true;
            }
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        a(surface);
        a(surface, 0, width, height);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        a(surface, 0, width, height);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        b(surface);
        return true;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }
}
