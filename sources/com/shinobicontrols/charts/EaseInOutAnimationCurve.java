package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class EaseInOutAnimationCurve extends AnimationCurve {
    @Override // com.shinobicontrols.charts.AnimationCurve
    public float valueAtTime(float time) {
        if (time < 0.5f) {
            float time2 = time * 2.0f;
            return time2 * time2 * time2 * 0.5f;
        }
        float time3 = 1.0f - ((time - 0.5f) * 2.0f);
        return ((1.0f - ((time3 * time3) * time3)) * 0.5f) + 0.5f;
    }
}
