package com.shinobicontrols.charts;

import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.SeriesStyle;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public abstract class CartesianSeries<T extends SeriesStyle> extends Series<T> {
    private CartesianSeries<?> A;
    private int a;
    boolean d;
    cq k;
    Object b = null;
    Integer c = null;
    boolean e = false;
    boolean f = false;
    boolean g = false;
    boolean h = false;
    boolean i = false;
    Series.Orientation j = Series.Orientation.HORIZONTAL;
    private NumberRange B = new NumberRange();
    private NumberRange C = new NumberRange();
    private final List<InternalDataPoint> D = new ArrayList();
    aa l = c();

    /* loaded from: classes.dex */
    enum a {
        CROW_FLIES,
        HORIZONTAL,
        VERTICAL
    }

    abstract aa c();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i, int i2, CartesianSeries<?> cartesianSeries) {
        this.a = i;
        this.A = cartesianSeries;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int i() {
        return this.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CartesianSeries<?> j() {
        return this.A;
    }

    public Object getBaseline() {
        return this.b;
    }

    public void setBaseline(Object baseline) {
        this.b = baseline;
        this.p.a();
        Axis<?, ?> yAxis = this.j == Series.Orientation.HORIZONTAL ? getYAxis() : getXAxis();
        if (yAxis != null) {
            yAxis.v.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public void k() {
        super.k();
        this.p.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public void a(v vVar) {
        super.a(vVar);
        this.p.a();
    }

    public Integer getStackId() {
        return this.c;
    }

    public void setStackId(Integer stackId) {
        synchronized (x.a) {
            this.c = stackId;
            this.p.a();
            if (this.t != null) {
                this.t.b();
            }
            r();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean l() {
        return this.A != null;
    }

    @Override // com.shinobicontrols.charts.Series
    void m() {
        if (this.o != null && this.m != null) {
            Axis<?, ?> xAxisForSeries = this.o.getXAxisForSeries(this);
            Axis<?, ?> yAxisForSeries = this.o.getYAxisForSeries(this);
            if (xAxisForSeries != null && yAxisForSeries != null) {
                if (this.m.size() > 0) {
                    a(xAxisForSeries, yAxisForSeries);
                }
                synchronized (x.a) {
                    a(this.n, this.n.a(this.m));
                }
                this.B = new NumberRange();
                this.C = new NumberRange();
                this.B.c(this.n.a);
                this.C.c(this.n.b);
                this.p.a();
                this.o.redrawChart();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Axis<?, ?> axis, Axis<?, ?> axis2) {
        if (!axis.isDataValid(this.m.get(0).getX()) || !axis2.isDataValid(this.m.get(0).getY())) {
            if (this.o == null) {
                throw new IllegalStateException("Chart is null and data is invalid for assigned x/y axes.");
            }
            throw new IllegalStateException(this.o.getContext().getString(R.string.CartesianInvalidDataXY));
        }
    }

    void a(ao aoVar, Object[] objArr) {
        int i;
        Axis<?, ?> xAxisForSeries = this.o.getXAxisForSeries(this);
        Axis<?, ?> yAxisForSeries = this.o.getYAxisForSeries(this);
        xAxisForSeries.A();
        yAxisForSeries.A();
        this.D.clear();
        int length = objArr.length;
        int i2 = 0;
        int i3 = 0;
        while (i2 < length) {
            try {
                Data<?, ?> data = (Data) objArr[i2];
                if ((xAxisForSeries.F() || yAxisForSeries.F()) && a(xAxisForSeries, yAxisForSeries, data)) {
                    i = i3;
                } else {
                    InternalDataPoint a2 = a(data, xAxisForSeries, yAxisForSeries);
                    a2.i = i3;
                    this.D.add(a2);
                    a(aoVar, a2);
                    i = i3 + 1;
                }
                i2++;
                i3 = i;
            } catch (IllegalArgumentException e) {
                cx.b(this.o.getContext().getString(R.string.CartesianCannotAddPoint) + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + e.getMessage());
                throw e;
            }
        }
        aoVar.a(this.D.size());
        aoVar.c = (InternalDataPoint[]) this.D.toArray(aoVar.c);
    }

    boolean a(Axis<?, ?> axis, Axis<?, ?> axis2, Data<?, ?> data) {
        return axis.isUserDataPointWithinASkipRange(data.getX()) || axis2.isUserDataPointWithinASkipRange(data.getY());
    }

    void a(ao aoVar, InternalDataPoint internalDataPoint) {
        aoVar.a.a(internalDataPoint.a);
        aoVar.b.a(internalDataPoint.b);
    }

    InternalDataPoint a(Data<?, ?> data, Axis<?, ?> axis, Axis<?, ?> axis2) {
        InternalDataPoint internalDataPoint = new InternalDataPoint(axis.convertPoint(data.getX()), axis2.convertPoint(data.getY()));
        if (data instanceof SelectableData) {
            internalDataPoint.h = ((SelectableData) data).getSelected();
        }
        return internalDataPoint;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n() {
        if (!(this.m.get(0) instanceof MultiValueData)) {
            if (this.o == null) {
                throw new IllegalStateException("Chart is null and this kind of chart requires MultiValueData data points");
            }
            throw new IllegalStateException(this.o.getContext().getString(R.string.CartesianNeedMultiValueData));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final NumberRange o() {
        return this.B;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final NumberRange p() {
        return this.C;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NumberRange b(Axis<?, ?> axis) {
        return (NumberRange) (axis.a() ? this.B.a() : this.C.a());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final NumberRange c(Axis<?, ?> axis) {
        return (NumberRange) (axis.a() ? this.B.a() : this.C.a());
    }

    @Override // com.shinobicontrols.charts.Series
    public boolean isSelected() {
        return this.d;
    }

    @Override // com.shinobicontrols.charts.Series
    public void setSelected(boolean selected) {
        if (this.d != selected) {
            synchronized (x.a) {
                this.d = selected;
                if (this.n != null) {
                    int length = this.n.c.length;
                    for (int i = 0; i < length; i++) {
                        this.n.c[i].h = selected;
                    }
                }
            }
            if (this.o != null) {
                this.p.a();
                this.o.a((CartesianSeries<?>) this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Series.a aVar, bz bzVar, boolean z) {
        cn.a((CartesianSeries<?>) this, aVar, bzVar, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a f() {
        return a.CROW_FLIES;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a g() {
        return a.HORIZONTAL;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Series.a aVar, bz bzVar) {
        aVar.a((bz) null);
    }

    public boolean isCrosshairEnabled() {
        return this.l.a;
    }

    public void setCrosshairEnabled(boolean crosshairEnabled) {
        this.l.a = crosshairEnabled;
    }

    public Series.Orientation getOrientation() {
        return this.j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public double h() {
        return this.u.a != null ? (getXAxis().i.a * (1.0d - this.u.a.floatValue())) + (getXAxis().i.b * this.u.a.floatValue()) : 0.5d * (getXAxis().i.a + getXAxis().i.b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public double b() {
        return this.u.b != null ? (getYAxis().i.a * (1.0d - this.u.b.floatValue())) + (getYAxis().i.b * this.u.b.floatValue()) : 0.5d * (getYAxis().i.a + getYAxis().i.b);
    }
}
