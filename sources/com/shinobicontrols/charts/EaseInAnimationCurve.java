package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class EaseInAnimationCurve extends AnimationCurve {
    @Override // com.shinobicontrols.charts.AnimationCurve
    public float valueAtTime(float time) {
        return (float) Math.pow(time, 4.0d);
    }
}
