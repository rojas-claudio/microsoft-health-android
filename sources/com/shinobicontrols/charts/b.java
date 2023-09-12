package com.shinobicontrols.charts;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
/* loaded from: classes.dex */
class b implements Runnable {
    private Animation a;
    private a b;
    private long d;
    private boolean e = false;
    private final Handler c = new Handler(Looper.getMainLooper());

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface a {
        void a();

        void a(Animation animation);

        void b();

        void c();
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.e && this.b != null) {
            long uptimeMillis = SystemClock.uptimeMillis();
            this.d = uptimeMillis;
            this.a.a(((float) (uptimeMillis - this.d)) / 1000.0f);
            this.b.a(this.a);
            if (this.a.c()) {
                this.e = false;
                this.b.b();
                return;
            }
            this.c.post(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Animation animation) {
        this.a = animation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(a aVar) {
        this.b = aVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        if (this.b != null) {
            this.d = SystemClock.uptimeMillis() - 17;
            this.b.a();
            this.e = true;
            run();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        this.c.removeCallbacks(this);
        if (this.e) {
            this.e = false;
            this.b.c();
        }
    }
}
