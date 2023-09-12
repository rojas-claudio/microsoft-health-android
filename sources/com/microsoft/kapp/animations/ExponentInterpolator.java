package com.microsoft.kapp.animations;
/* loaded from: classes.dex */
public class ExponentInterpolator extends BaseInterpolator {
    private float mExponent = 7.0f;

    public void setExponent(float exponent) {
        this.mExponent = exponent;
    }

    public float getExponent() {
        return this.mExponent;
    }

    @Override // com.microsoft.kapp.animations.BaseInterpolator
    protected float calculateInterpolation(float time) {
        return (float) ((Math.exp(this.mExponent * time) - 1.0d) / (Math.exp(this.mExponent) - 1.0d));
    }
}
