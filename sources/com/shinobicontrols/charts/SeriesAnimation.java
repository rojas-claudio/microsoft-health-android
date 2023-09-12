package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class SeriesAnimation extends Animation {
    Float a = Float.valueOf(0.5f);
    Float b = Float.valueOf(0.5f);
    private AnimationCurve c = new FlatAnimationCurve();
    private AnimationCurve d = new FlatAnimationCurve();
    private AnimationCurve e = new FlatAnimationCurve();
    private AnimationCurve f = new FlatAnimationCurve();
    private AnimationCurve g = new FlatAnimationCurve();

    @Override // com.shinobicontrols.charts.Animation
    public /* bridge */ /* synthetic */ float getDuration() {
        return super.getDuration();
    }

    @Override // com.shinobicontrols.charts.Animation
    public /* bridge */ /* synthetic */ void setDuration(float x0) {
        super.setDuration(x0);
    }

    public SeriesAnimation() {
        setDuration(2.4f);
    }

    public static SeriesAnimation createGrowAnimation() {
        SeriesAnimation seriesAnimation = new SeriesAnimation();
        seriesAnimation.c = new BounceAnimationCurve();
        seriesAnimation.d = new BounceAnimationCurve();
        return seriesAnimation;
    }

    public static SeriesAnimation createGrowHorizontalAnimation() {
        SeriesAnimation seriesAnimation = new SeriesAnimation();
        seriesAnimation.c = new BounceAnimationCurve();
        seriesAnimation.a = null;
        return seriesAnimation;
    }

    public static SeriesAnimation createGrowVerticalAnimation() {
        SeriesAnimation seriesAnimation = new SeriesAnimation();
        seriesAnimation.d = new BounceAnimationCurve();
        seriesAnimation.b = null;
        return seriesAnimation;
    }

    public static SeriesAnimation createFadeAnimation() {
        SeriesAnimation seriesAnimation = new SeriesAnimation();
        seriesAnimation.g = new LinearAnimationCurve();
        return seriesAnimation;
    }

    public static SeriesAnimation createTelevisionAnimation() {
        SeriesAnimation seriesAnimation = new SeriesAnimation();
        seriesAnimation.c = new BounceDelayAnimationCurve();
        seriesAnimation.d = new DelayBounceAnimationCurve();
        return seriesAnimation;
    }

    public Float getXOrigin() {
        return this.a;
    }

    public void setXOrigin(Float origin) {
        this.a = origin;
    }

    public Float getYOrigin() {
        return this.b;
    }

    public void setYOrigin(Float origin) {
        this.b = origin;
    }

    public AnimationCurve getXScaleCurve() {
        return this.c;
    }

    public void setXScaleCurve(AnimationCurve curve) {
        if (curve == null) {
            throw new IllegalArgumentException("Animation curves may not be null");
        }
        this.c = curve;
    }

    public AnimationCurve getYScaleCurve() {
        return this.d;
    }

    public void setYScaleCurve(AnimationCurve curve) {
        if (curve == null) {
            throw new IllegalArgumentException("Animation curves may not be null");
        }
        this.d = curve;
    }

    public AnimationCurve getAlphaCurve() {
        return this.g;
    }

    public void setAlphaCurve(AnimationCurve curve) {
        if (curve == null) {
            throw new IllegalArgumentException("Animation curves may not be null");
        }
        this.g = curve;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float d() {
        if (a() <= 0.0f) {
            return 0.0f;
        }
        if (a() >= getDuration()) {
            return 1.0f;
        }
        return this.c.valueAtTime(b());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float e() {
        if (a() <= 0.0f) {
            return 0.0f;
        }
        if (a() >= getDuration()) {
            return 1.0f;
        }
        return this.d.valueAtTime(b());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float f() {
        if (a() <= 0.0f) {
            return 0.0f;
        }
        if (a() >= getDuration()) {
            return 1.0f;
        }
        return this.g.valueAtTime(b());
    }
}
