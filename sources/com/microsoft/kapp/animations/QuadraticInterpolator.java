package com.microsoft.kapp.animations;
/* loaded from: classes.dex */
public class QuadraticInterpolator extends BaseInterpolator {
    @Override // com.microsoft.kapp.animations.BaseInterpolator
    protected float calculateInterpolation(float time) {
        return time * time;
    }
}
