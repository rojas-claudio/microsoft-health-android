package com.microsoft.kapp.animations;
/* loaded from: classes.dex */
public class BackInterpolator extends BaseInterpolator {
    private float mAmplitude;

    public float getAmplitude() {
        return this.mAmplitude;
    }

    public void setAmplitude(float amplitude) {
        this.mAmplitude = amplitude;
    }

    @Override // com.microsoft.kapp.animations.BaseInterpolator
    protected float calculateInterpolation(float time) {
        return (float) (((time * time) * time) - ((this.mAmplitude * time) * Math.sin(time * 3.141592653589793d)));
    }
}
