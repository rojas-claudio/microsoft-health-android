package com.shinobicontrols.charts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.PieDonutSeriesStyle;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.b;
/* loaded from: classes.dex */
public abstract class PieDonutSeries<T extends PieDonutSeriesStyle> extends Series<T> {
    private float f;
    private NumberRange g;
    private DrawDirection c = DrawDirection.ANTICLOCKWISE;
    final dj<Float> a = new dj<>(Float.valueOf(0.0f));
    final dj<Float> b = new dj<>(Float.valueOf(0.0f));
    private final ChartUtils d = new ChartUtils();
    private boolean e = true;
    private final BounceAnimationCurve h = new BounceAnimationCurve();
    private final BounceAnimationCurve i = new BounceAnimationCurve();
    private final EaseInOutAnimationCurve j = new EaseInOutAnimationCurve();
    private Float k = null;
    private final b l = new b();
    private final a A = new a(this);

    /* loaded from: classes.dex */
    public enum DrawDirection {
        CLOCKWISE,
        ANTICLOCKWISE
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract float b(float f);

    /* loaded from: classes.dex */
    public enum RadialEffect {
        FLAT(0),
        BEVELLED(1),
        BEVELLED_LIGHT(2),
        ROUNDED(3),
        ROUNDED_LIGHT(4),
        DEFAULT(5);
        
        private final int a;

        RadialEffect(int xmlValue) {
            this.a = xmlValue;
        }

        public int getXmlValue() {
            return this.a;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a implements b.a {
        float a;
        float b;
        float c;
        float d;
        private final PieDonutSeries<?> e;

        public a(PieDonutSeries<?> pieDonutSeries) {
            this.e = pieDonutSeries;
        }

        @Override // com.shinobicontrols.charts.b.a
        public void a() {
            int length = this.e.n.c.length;
            for (int i = 0; i < length; i++) {
                PieDonutSlice pieDonutSlice = (PieDonutSlice) this.e.n.c[i];
                pieDonutSlice.p = pieDonutSlice.q;
            }
        }

        @Override // com.shinobicontrols.charts.b.a
        public void a(Animation animation) {
            bu buVar = (bu) animation;
            a((float) buVar.f(), buVar.d(), buVar.e());
        }

        @Override // com.shinobicontrols.charts.b.a
        public void b() {
        }

        @Override // com.shinobicontrols.charts.b.a
        public void c() {
        }

        public void d() {
            a(1.0f, 1.0f, 1.0f);
        }

        private void a(float f, float f2, float f3) {
            synchronized (x.a) {
                ((PieDonutSeries) this.e).f = bk.a(this.c + (this.d * f));
                int length = this.e.n.c.length;
                for (int i = 0; i < length; i++) {
                    PieDonutSlice pieDonutSlice = (PieDonutSlice) this.e.n.c[i];
                    pieDonutSlice.q = pieDonutSlice.h ? pieDonutSlice.p + ((this.a - pieDonutSlice.p) * f2) : pieDonutSlice.p + ((this.b - pieDonutSlice.p) * f3);
                }
            }
            this.e.o.b.invalidate();
            this.e.p.a();
            this.e.o.b.f();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PieDonutSeries() {
        this.v = SeriesAnimation.createGrowAnimation();
        this.w = SeriesAnimation.createGrowAnimation();
    }

    @Override // com.shinobicontrols.charts.Series
    void m() {
        DataAdapter<?, ?> dataAdapter = getDataAdapter();
        if (this.o != null && dataAdapter != null && dataAdapter.size() >= 0) {
            if (dataAdapter.size() > 0) {
                a(dataAdapter);
            }
            synchronized (x.a) {
                Object[] a2 = this.n.a(dataAdapter);
                this.n.a(a2.length);
                a(this.n, a2);
            }
            this.g = new NumberRange();
            this.g.a(this.n.b.a);
            this.g.a(this.n.b.b);
            r();
            this.p.a();
            this.o.redrawChart();
        }
    }

    private void a(DataAdapter<?, ?> dataAdapter) {
        if (!(dataAdapter.get(0).getY() instanceof Number)) {
            throw new IllegalStateException(this.o.getContext().getString(R.string.PieDonutSeriesInvalidData));
        }
    }

    void a(ao aoVar, Object[] objArr) {
        int length = objArr.length;
        for (int i = 0; i < length; i++) {
            Data data = (Data) objArr[i];
            double d = i;
            double doubleValue = ((Number) data.getY()).doubleValue();
            if (doubleValue < Constants.SPLITS_ACCURACY) {
                throw new IllegalArgumentException(this.o.getContext().getString(R.string.PieDonutSeriesYDataMustBePositive));
            }
            PieDonutSlice a2 = a(d, doubleValue, data.getX().toString());
            if (data instanceof SelectableData) {
                a2.h = ((SelectableData) data).getSelected();
                a2.q = a2.h ? ((PieDonutSeriesStyle) this.r).getProtrusion() : ((PieDonutSeriesStyle) this.q).getProtrusion();
            }
            a2.i = i;
            aoVar.c[i] = a2;
            aoVar.b.a(doubleValue);
        }
        e();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float c() {
        return Math.max(((PieDonutSeriesStyle) this.q).getProtrusion(), ((PieDonutSeriesStyle) this.r).getProtrusion());
    }

    public DrawDirection getDrawDirection() {
        return this.c;
    }

    public void setDrawDirection(DrawDirection drawDirection) {
        synchronized (x.a) {
            this.c = drawDirection;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getInnerRadius() {
        return this.a.a.floatValue();
    }

    public float getOuterRadius() {
        return this.b.a.floatValue();
    }

    public void setOuterRadius(float outerRadius) {
        synchronized (x.a) {
            this.b.a(Float.valueOf(outerRadius));
            a_();
        }
    }

    public float getRotation() {
        if (this.e) {
            this.f = ((PieDonutSeriesStyle) this.q).getInitialRotation();
            this.e = false;
        }
        return this.f;
    }

    public void setRotation(float rotation) {
        synchronized (x.a) {
            this.f = rotation;
        }
    }

    public Float getSelectedPosition() {
        return this.k;
    }

    public void setSelectedPosition(Float selectedPosition) {
        synchronized (x.a) {
            this.k = selectedPosition;
        }
    }

    public Point getCenter() {
        if (this.o == null) {
            return null;
        }
        Point point = new Point();
        point.set((int) ((this.o.getPlotAreaRect().width() / 2.0f) + 0.5f), (int) ((this.o.getPlotAreaRect().height() / 2.0f) + 0.5f));
        return point;
    }

    Point a(Rect rect, int i) {
        float f;
        float floatValue = (this.a.a.floatValue() + this.b.a.floatValue()) / 2.0f;
        PieDonutSlice pieDonutSlice = (PieDonutSlice) this.n.c[i];
        float f2 = floatValue + pieDonutSlice.q;
        if (this.c == DrawDirection.ANTICLOCKWISE) {
            f = (-(pieDonutSlice.o + pieDonutSlice.n)) / 2.0f;
        } else {
            f = (pieDonutSlice.o + pieDonutSlice.n) / 2.0f;
        }
        float cos = ((float) Math.cos(((-getRotation()) + f) - 1.5707963267948966d)) * f2;
        float sin = ((float) Math.sin((f + (-getRotation())) - 1.5707963267948966d)) * f2;
        int min = Math.min(rect.width(), rect.height()) / 2;
        return new Point(((int) (cos * min)) + rect.centerX(), ((int) (sin * min)) + rect.centerY());
    }

    @Override // com.shinobicontrols.charts.Series
    void a(boolean z, int i) {
        this.o.g.a(z, this, (PieDonutSlice) this.n.c[i], Series.SelectionMode.POINT_MULTIPLE, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public void a(Canvas canvas, Rect rect) {
        if (((PieDonutSeriesStyle) this.q).areLabelsShown()) {
            if (this.u == null || this.u.c()) {
                int length = this.n.c.length;
                for (int i = 0; i < length; i++) {
                    PieDonutSlice pieDonutSlice = (PieDonutSlice) this.n.c[i];
                    PieDonutSeriesStyle pieDonutSeriesStyle = pieDonutSlice.h ? (PieDonutSeriesStyle) this.r : (PieDonutSeriesStyle) this.q;
                    pieDonutSlice.r = a(rect, i);
                    pieDonutSlice.s = new Point(pieDonutSlice.r.x, pieDonutSlice.r.y);
                    pieDonutSlice.u.setColor(pieDonutSeriesStyle.getLabelBackgroundColor());
                    pieDonutSlice.t.setTextAlign(Paint.Align.CENTER);
                    pieDonutSlice.t.setAntiAlias(true);
                    pieDonutSlice.t.setColor(pieDonutSeriesStyle.getLabelTextColor());
                    pieDonutSlice.t.setTypeface(pieDonutSeriesStyle.getLabelTypeface());
                    pieDonutSlice.t.setTextSize(pieDonutSeriesStyle.getLabelTextSize() * this.o.getResources().getDisplayMetrics().scaledDensity);
                    this.o.a(pieDonutSlice, (PieDonutSeries<?>) this);
                    Rect a2 = this.d.a(pieDonutSlice.s.x, pieDonutSlice.s.y, pieDonutSlice.m, pieDonutSeriesStyle.getLabelTextSize(), pieDonutSeriesStyle.getLabelTypeface(), this.o);
                    if (!this.o.a(canvas, pieDonutSlice, a2, (PieDonutSeries<?>) this)) {
                        if (pieDonutSlice.u.getColor() != 0) {
                            ChartUtils.drawTextBackground(canvas, a2, pieDonutSlice.u);
                        }
                        ChartUtils.drawText(canvas, pieDonutSlice.m, pieDonutSlice.s.x, pieDonutSlice.s.y, pieDonutSlice.t);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String a(int i) {
        return ((PieDonutSlice) this.n.c[i]).m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public Drawable a(float f) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Drawable a(int i, float f) {
        PieDonutSeriesStyle pieDonutSeriesStyle = (!this.n.c[i].h || this.r == 0) ? (PieDonutSeriesStyle) this.q : (PieDonutSeriesStyle) this.r;
        if (pieDonutSeriesStyle.c()) {
            return new be();
        }
        return new ba(pieDonutSeriesStyle.isFlavorShown() ? pieDonutSeriesStyle.flavorColorAtIndex(i) : 0, pieDonutSeriesStyle.isCrustShown() ? pieDonutSeriesStyle.crustColorAtIndex(i) : 0, f);
    }

    PieDonutSlice a(double d, double d2, String str) {
        PieDonutSlice pieDonutSlice = new PieDonutSlice(d, d2);
        pieDonutSlice.m = str;
        return pieDonutSlice;
    }

    void e() {
        int length = this.n.c.length;
        float f = 0.0f;
        for (int i = 0; i < length; i++) {
            f = (float) (f + this.n.c[i].b);
        }
        float f2 = 0.0f;
        for (int i2 = 0; i2 < length; i2++) {
            PieDonutSlice pieDonutSlice = (PieDonutSlice) this.n.c[i2];
            pieDonutSlice.n = f2;
            pieDonutSlice.o = (float) (f2 + ((pieDonutSlice.b / f) * 3.141592653589793d * 2.0d));
            f2 = pieDonutSlice.o;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(PieDonutSlice pieDonutSlice, boolean z) {
        a(pieDonutSlice);
        if (z) {
            a(1.5f, this.h, this.i, this.j, this.A);
        } else {
            this.A.d();
        }
    }

    private void a(PieDonutSlice pieDonutSlice) {
        this.A.c = this.f;
        this.A.a = ((PieDonutSeriesStyle) this.r).getProtrusion();
        this.A.b = ((PieDonutSeriesStyle) this.q).getProtrusion();
        if (this.k != null && pieDonutSlice.h) {
            float a2 = this.f + g().a(pieDonutSlice.getCenterAngle());
            this.A.d = this.k.floatValue() - a2;
            this.A.d = bk.b(this.A.d);
            return;
        }
        this.A.d = 0.0f;
    }

    private void a(float f, AnimationCurve animationCurve, AnimationCurve animationCurve2, AnimationCurve animationCurve3, a aVar) {
        bu buVar = new bu();
        buVar.setDuration(f);
        buVar.a(animationCurve);
        buVar.b(animationCurve2);
        buVar.c(animationCurve3);
        this.l.a(buVar);
        this.l.a(aVar);
        this.l.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public void a_() {
        int length = this.n.c.length;
        for (int i = 0; i < length; i++) {
            PieDonutSlice pieDonutSlice = (PieDonutSlice) this.n.c[i];
            pieDonutSlice.q = pieDonutSlice.h ? ((PieDonutSeriesStyle) this.r).getProtrusion() : ((PieDonutSeriesStyle) this.q).getProtrusion();
        }
        super.a_();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public cg g() {
        return cg.a(this.c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public double h() {
        return Constants.SPLITS_ACCURACY;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public double b() {
        return Constants.SPLITS_ACCURACY;
    }
}
