package com.shinobicontrols.charts;
/* loaded from: classes.dex */
class g extends Animation {
    private AnimationCurve a = new bp();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(AnimationCurve animationCurve) {
        if (animationCurve == null) {
            this.a = new bp();
        } else {
            this.a = animationCurve;
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
}
