package com.microsoft.kapp.animations;
/* loaded from: classes.dex */
public class CircleInterpolator extends BaseInterpolator {
    @Override // com.microsoft.kapp.animations.BaseInterpolator
    protected float calculateInterpolation(float time) {
        return (float) ((-1.0d) * (Math.sqrt(1.0f - (time * time)) - 1.0d));
    }
}
