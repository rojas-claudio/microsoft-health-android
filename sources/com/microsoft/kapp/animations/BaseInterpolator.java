package com.microsoft.kapp.animations;

import android.animation.TimeInterpolator;
/* loaded from: classes.dex */
public abstract class BaseInterpolator implements TimeInterpolator {
    protected Easing mEasing = Easing.OUT;

    public void setEasing(Easing easing) {
        this.mEasing = easing;
    }

    public Easing getEasing() {
        return this.mEasing;
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float input) {
        return this.mEasing == Easing.OUT ? 1.0f - calculateInterpolation(1.0f - input) : calculateInterpolation(input);
    }

    protected float calculateInterpolation(float time) {
        return time;
    }
}
