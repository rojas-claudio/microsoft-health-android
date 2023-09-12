package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class EaseOutAnimationCurve extends AnimationCurve {
    @Override // com.shinobicontrols.charts.AnimationCurve
    public float valueAtTime(float time) {
        return 1.0f - ((float) Math.pow(1.0f - time, 5.0d));
    }
}
