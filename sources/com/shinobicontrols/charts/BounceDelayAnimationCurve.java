package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class BounceDelayAnimationCurve extends AnimationCurve {
    @Override // com.shinobicontrols.charts.AnimationCurve
    public float valueAtTime(float time) {
        float time2;
        if (time > 0.5f) {
            time2 = 1.0f;
        } else {
            time2 = time * 2.0f;
        }
        return a(time2);
    }
}
