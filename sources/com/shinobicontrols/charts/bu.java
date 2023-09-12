package com.shinobicontrols.charts;

import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
class bu extends Animation {
    private AnimationCurve a = new bp();
    private AnimationCurve b = new bp();
    private AnimationCurve c = new bp();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(AnimationCurve animationCurve) {
        if (animationCurve == null) {
            this.a = new bp();
        } else {
            this.a = animationCurve;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(AnimationCurve animationCurve) {
        if (animationCurve == null) {
            this.b = new bp();
        } else {
            this.b = animationCurve;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(AnimationCurve animationCurve) {
        if (animationCurve == null) {
            this.c = new bp();
        } else {
            this.c = animationCurve;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float d() {
        if (a() < 0.0f) {
            return 0.0f;
        }
        if (a() > getDuration()) {
            return 1.0f;
        }
        return this.a.valueAtTime(b());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float e() {
        if (a() < 0.0f) {
            return 0.0f;
        }
        if (a() > getDuration()) {
            return 1.0f;
        }
        return 1.0f - this.b.valueAtTime(1.0f - b());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double f() {
        if (a() < 0.0f) {
            return Constants.SPLITS_ACCURACY;
        }
        if (a() > getDuration()) {
            return 1.0d;
        }
        return this.c.valueAtTime(b());
    }
}
