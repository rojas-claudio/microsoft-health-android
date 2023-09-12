package com.microsoft.kapp.animations;
/* loaded from: classes.dex */
public class ElasticInterpolator extends BaseInterpolator {
    private static final float Epsilon = 1.0E-4f;
    private float mOscillations;
    private float mSpringiness;

    public float getSpringiness() {
        return this.mSpringiness;
    }

    public void setSpringiness(float springiness) {
        this.mSpringiness = springiness;
    }

    public float getOscillations() {
        return this.mOscillations;
    }

    public void setOscillations(float oscillations) {
        this.mOscillations = oscillations;
    }

    @Override // com.microsoft.kapp.animations.BaseInterpolator, android.animation.TimeInterpolator
    public float getInterpolation(float time) {
        float expMod;
        if (this.mEasing == Easing.OUT) {
            time = 1.0f - time;
        }
        if (this.mSpringiness >= -1.0E-4f && this.mSpringiness <= Epsilon) {
            expMod = time;
        } else {
            expMod = (float) ((Math.exp(this.mSpringiness * time) - 1.0d) / (Math.exp(this.mSpringiness) - 1.0d));
        }
        float result = (float) (expMod * Math.sin(time * ((6.283185307179586d * this.mOscillations) + 1.5707963267948966d)));
        if (this.mEasing == Easing.OUT) {
            return 1.0f - result;
        }
        return result;
    }
}
