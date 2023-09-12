package com.google.android.gms.internal;
/* loaded from: classes.dex */
public abstract class cg {
    private final Runnable el = new Runnable() { // from class: com.google.android.gms.internal.cg.1
        @Override // java.lang.Runnable
        public final void run() {
            cg.this.hD = Thread.currentThread();
            cg.this.ac();
        }
    };
    private volatile Thread hD;

    public abstract void ac();

    public final void cancel() {
        onStop();
        if (this.hD != null) {
            this.hD.interrupt();
        }
    }

    public abstract void onStop();

    public final void start() {
        ch.execute(this.el);
    }
}
