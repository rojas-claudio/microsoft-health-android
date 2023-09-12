package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public abstract class AnimationCurve {
    static final float a = (float) Math.sqrt(0.75d);
    static final float b = (float) Math.atan(a / 0.5f);

    public abstract float valueAtTime(float f);

    /* JADX INFO: Access modifiers changed from: package-private */
    public final float a(float f) {
        return 1.0f - (((1.0f / a) * ((float) Math.exp((-4.8368f) * f))) * ((float) Math.sin(((a * 9.6736f) * f) + b)));
    }
}
