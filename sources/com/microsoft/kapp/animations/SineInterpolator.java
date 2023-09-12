package com.microsoft.kapp.animations;
/* loaded from: classes.dex */
public class SineInterpolator extends BaseInterpolator {
    @Override // com.microsoft.kapp.animations.BaseInterpolator
    protected float calculateInterpolation(float time) {
        return (float) (1.0d - Math.sin((1.0f - time) * 1.5707963267948966d));
    }
}
