package com.shinobicontrols.charts;
/* loaded from: classes.dex */
abstract class Animation {
    private float a = 0.0f;
    private float b = 0.016666668f;
    private boolean c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public float a() {
        return this.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float b() {
        return this.a / this.b;
    }

    public float getDuration() {
        return this.b;
    }

    public void setDuration(float duration) {
        this.b = duration;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(float f) {
        if (this.c) {
            this.a -= f;
        } else {
            this.a += f;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(boolean z) {
        this.c = z;
        if (z) {
            this.a = this.b;
        } else {
            this.a = 0.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c() {
        return this.c ? this.a <= 0.0f : this.a >= this.b;
    }
}
