package com.shinobicontrols.charts;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.PropertyChangedEvent;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.charts.db;
import com.shinobicontrols.charts.di;
import java.lang.Comparable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
/* loaded from: classes.dex */
public abstract class Axis<T extends Comparable<T>, U> {
    private double D;
    private boolean F;
    private U G;
    private U H;
    private double I;
    private double J;
    private am K;
    private String O;
    private Title R;
    private double U;
    v b;
    Orientation c;
    double e;
    AxisStyle g;
    int h;
    Range<T> m;
    i n;
    int o;
    double[] t;
    float a = 1.0f;
    private final Map<Series<?>, am> A = new HashMap();
    private final Axis<T, U>.a B = new a(this);
    private final al C = new al();
    Position d = Position.NORMAL;
    double f = Constants.SPLITS_ACCURACY;
    private boolean E = false;
    private final Axis<T, U>.b L = new b();
    private TickMark.ClippingMode M = TickMark.ClippingMode.TICKS_AND_LABELS_PERSIST;
    private TickMark.ClippingMode N = TickMark.ClippingMode.TICKS_AND_LABELS_PERSIST;
    private Float P = null;
    NumberRange i = new NumberRange();
    NumberRange j = new NumberRange();
    NumberRange k = new NumberRange();
    NumberRange l = null;
    private boolean Q = false;
    private final as S = new as();
    private final Paint T = new Paint();
    U p = null;
    U q = null;
    U r = null;
    U s = null;
    private final Point V = new Point();
    final dg u = new dg(this);
    private final c W = new c();
    private final Rect X = new Rect();
    private final ar Y = new ar();
    private final PointF Z = new PointF();
    final ch v = new ch(this);
    private DoubleTapBehavior aa = DoubleTapBehavior.ZOOM_IN;
    String w = null;
    String x = null;
    private final List<Range<T>> ab = new ArrayList();
    final bv y = bv.a();
    final bv z = bv.a();
    private final da<T, U> ac = new da<>(this);
    private final aq<T> ad = new f();
    private final aq<T> ae = new ck();

    /* loaded from: classes.dex */
    public enum DoubleTapBehavior {
        RESET_TO_DEFAULT_RANGE,
        ZOOM_IN
    }

    /* loaded from: classes.dex */
    public enum MotionState {
        STOPPED,
        ANIMATING,
        GESTURE,
        MOMENTUM,
        BOUNCING
    }

    /* loaded from: classes.dex */
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    /* loaded from: classes.dex */
    public enum Position {
        NORMAL,
        REVERSE
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract double a(double d, boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract double a(int i);

    abstract T applyMappingForSkipRangesToUserValue(T t);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract double b(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean b(double d);

    abstract void c(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract double convertPoint(Object obj);

    abstract double convertUserValueTypeToInternalDataType(Object obj);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Range<T> createRange(T t, T t2);

    abstract void g();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract T getDefaultBaseline();

    abstract String getFormattedString(T t);

    abstract String i();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean isDataValid(Object obj);

    abstract void p();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void q();

    abstract T removeMappingForSkipRangesFromChartValue(T t);

    abstract void setMajorTickFrequencyInternal(U u);

    abstract void setMinorTickFrequencyInternal(U u);

    abstract T transformChartValueToUserValue(T t);

    abstract double transformExternalFrequencyToInternal(U u);

    abstract double transformExternalValueToInternal(T t);

    abstract T transformInternalValueToExternal(double d);

    abstract T transformUserValueToChartValue(T t);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract double translatePoint(Object obj);

    abstract double x();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(String str) {
        return TextUtils.isEmpty(str) || TextUtils.getTrimmedLength(str) == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Axis() {
        setStyle(new AxisStyle());
        this.n = i.a((Axis<?, ?>) this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Series<?> series) {
        this.A.put(series, series.a((di.a) this.B));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Series<?> series) {
        am amVar = this.A.get(series);
        if (amVar != null) {
            amVar.a();
            this.A.remove(series);
        }
    }

    public void specifyBarColumnSpacing(U spacing) {
        if (spacing != null) {
            this.f = transformExternalFrequencyToInternal(spacing);
            this.E = true;
        } else {
            this.E = false;
            P();
        }
        if (this.b != null) {
            Q();
            O();
        }
    }

    public final Orientation getOrientation() {
        return this.c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean a() {
        return this.c == Orientation.HORIZONTAL;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(Orientation orientation) {
        this.c = orientation;
        this.n = i.a((Axis<?, ?>) this);
    }

    public final Position getPosition() {
        return this.d;
    }

    public final void setPosition(Position axisPosition) {
        this.d = axisPosition;
        this.n = i.a((Axis<?, ?>) this);
    }

    float b() {
        for (int i = 0; i < this.b.e.a.length; i++) {
            Axis<?, ?> axis = this.b.e.a[i];
            if (axis.d == Position.REVERSE) {
                return axis.g.d.a.floatValue();
            }
        }
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final double c() {
        return this.f;
    }

    private boolean G() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double transformUserValueToInternal(T userValue) {
        return transformExternalValueToInternal(transformUserValueToChartValue(applyMappingForSkipRangesToUserValue(userValue)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public T transformInternalValueToUser(double internalDataValue) {
        return transformChartValueToUserValue(removeMappingForSkipRangesFromChartValue(transformInternalValueToExternal(internalDataValue)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void validateUserData(Object userData) {
        if (userData == null) {
            throw new IllegalArgumentException(this.b != null ? this.b.getContext().getString(R.string.AxisDataPointsNotNull) : "You must supply all DataPoint parameter arguments, non-null");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String a(double d) {
        return getFormattedString(transformInternalValueToUser(d));
    }

    public final Range<T> getCurrentDisplayedRange() {
        return b(this.i);
    }

    void a(NumberRange numberRange) {
        boolean z = (this.i.a == numberRange.a && this.i.b == numberRange.b) ? false : true;
        synchronized (x.a) {
            this.i = numberRange;
        }
        if (z && this.b != null) {
            this.b.b((Axis<?, ?>) this);
        }
    }

    public final Range<T> getDataRange() {
        return b(this.k);
    }

    public final Range<T> getDefaultRange() {
        if (this.l != null) {
            return b(this.l);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double d() {
        double d = this.j.b + this.I;
        Double H = H();
        if (H != null && H.doubleValue() > d) {
            return H.doubleValue();
        }
        if (J()) {
            return d + (x() / 2.0d);
        }
        return d;
    }

    private Double H() {
        Double d;
        Double d2 = null;
        if (this.b != null) {
            Iterator<Series<?>> it = this.b.getSeriesForAxis(this).iterator();
            while (it.hasNext()) {
                CartesianSeries<?> cartesianSeries = (CartesianSeries) it.next();
                if (!a(cartesianSeries.j) && cartesianSeries.b != null) {
                    double b2 = cartesianSeries.t.b(cartesianSeries);
                    if (d2 == null || b2 > d2.doubleValue()) {
                        d = Double.valueOf(b2);
                        d2 = d;
                    }
                }
                d = d2;
                d2 = d;
            }
        }
        return d2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double e() {
        double d = this.j.a - this.J;
        Double I = I();
        if (I != null && I.doubleValue() < d) {
            return I.doubleValue();
        }
        if (J()) {
            return d - (x() / 2.0d);
        }
        return d;
    }

    private Double I() {
        Double d;
        Double d2 = null;
        if (this.b != null) {
            Iterator<Series<?>> it = this.b.getSeriesForAxis(this).iterator();
            while (it.hasNext()) {
                CartesianSeries<?> cartesianSeries = (CartesianSeries) it.next();
                if (!a(cartesianSeries.j) && cartesianSeries.b != null) {
                    double b2 = cartesianSeries.t.b(cartesianSeries);
                    if (d2 == null || b2 < d2.doubleValue()) {
                        d = Double.valueOf(b2);
                        d2 = d;
                    }
                }
                d = d2;
                d2 = d;
            }
        }
        return d2;
    }

    private boolean J() {
        return this.j.c() && this.I == Constants.SPLITS_ACCURACY && this.J == Constants.SPLITS_ACCURACY;
    }

    public final void setDefaultRange(Range<T> defaultRange) {
        c(defaultRange);
        this.m = defaultRange;
        a(defaultRange);
        g();
    }

    private void c(Range<T> range) {
        if (range != null) {
            if (!Range.b(range)) {
                throw new IllegalArgumentException("Cannot set an undefined range as the default range: infinite minimum or maximum values or negative span not allowed.");
            }
            if (range.c()) {
                throw new IllegalArgumentException(this.b != null ? this.b.getContext().getString(R.string.AxisDefaultRangeIsEmpty) : "Cannot set a default range with equal minimum and maximum values.");
            }
        }
    }

    final void a(Range<T> range) {
        this.l = range == null ? null : d(range);
        if (this.l != null && this.l.c()) {
            throw new IllegalStateException(this.b != null ? this.b.getContext().getString(R.string.AxisDefaultRangeInternalIsEmpty) : "Cannot set default range due to transformations applied to this axis: have you set skip ranges that completely cover the data or default ranges?");
        }
        if (range != null && Range.a(this.i)) {
            a((NumberRange) this.l.a());
        } else {
            this.v.a();
        }
        O();
    }

    public final boolean isCurrentDisplayedRangePreservedOnUpdate() {
        return this.Q;
    }

    public final void setCurrentDisplayedRangePreservedOnUpdate(boolean preserved) {
        this.Q = preserved;
    }

    public boolean requestCurrentDisplayedRange(T minimum, T maximum) {
        return this.v.a(transformUserValueToInternal(minimum), transformUserValueToInternal(maximum));
    }

    public boolean requestCurrentDisplayedRange(T minimum, T maximum, boolean animation, boolean bounceAtLimits) {
        return this.v.a(transformUserValueToInternal(minimum), transformUserValueToInternal(maximum), animation, bounceAtLimits);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(double d, double d2) {
        boolean z = (this.i.a == d && this.i.b == d2) ? false : true;
        synchronized (x.a) {
            this.i.a(d, d2);
        }
        if (z && this.b != null) {
            this.b.b.b();
            this.b.b((Axis<?, ?>) this);
        }
    }

    public DoubleTapBehavior getDoubleTapBehavior() {
        return this.aa;
    }

    public void setDoubleTapBehavior(DoubleTapBehavior behavior) {
        this.aa = behavior;
    }

    public boolean isDoubleTapEnabled() {
        return this.v.k;
    }

    public void enableDoubleTap(boolean doubleTapEnabled) {
        this.v.k = doubleTapEnabled;
    }

    public final Range<T> getVisibleRange() {
        return b(this.j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void f() {
        if (this.b != null) {
            P();
            this.j = L();
            this.k = M();
            N();
            Q();
            if (!K()) {
                g();
            }
        }
    }

    private boolean K() {
        Set<CartesianSeries<?>> e;
        if (this.b != null && Range.a(this.j) && (e = this.b.d.e(this)) != null) {
            for (CartesianSeries<?> cartesianSeries : e) {
                if (cartesianSeries.s()) {
                    return true;
                }
            }
        }
        return false;
    }

    private NumberRange L() {
        NumberRange numberRange = new NumberRange();
        for (cr crVar : this.b.a()) {
            if (crVar.a((Axis<?, ?>) this)) {
                numberRange.c(crVar.b(this));
            }
        }
        return numberRange;
    }

    private NumberRange M() {
        NumberRange numberRange = new NumberRange();
        for (cr crVar : this.b.a()) {
            if (crVar.a((Axis<?, ?>) this)) {
                numberRange.c(crVar.c(this));
            }
        }
        return numberRange;
    }

    private void N() {
        NumberRange numberRange;
        if (!this.Q || Range.a(this.i)) {
            if (Range.b(this.l)) {
                numberRange = (NumberRange) this.l.a();
            } else if (Range.b(this.j)) {
                numberRange = new NumberRange(Double.valueOf(e()), Double.valueOf(d()));
            } else {
                numberRange = new NumberRange();
            }
            a(numberRange);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void h() {
        this.u.a();
        this.b = null;
    }

    /* loaded from: classes.dex */
    private class a implements di.a {
        private final Axis<?, ?> b;

        public a(Axis<?, ?> axis) {
            this.b = axis;
        }

        @Override // com.shinobicontrols.charts.di.a
        public final void a() {
            this.b.f();
        }
    }

    private NumberRange d(Range<T> range) {
        if (range == null) {
            return null;
        }
        return new NumberRange(Double.valueOf(transformUserValueToInternal(range.getMinimum())), Double.valueOf(transformUserValueToInternal(range.getMaximum())));
    }

    private Range<T> b(NumberRange numberRange) {
        if (numberRange == null) {
            return null;
        }
        return createRange(transformInternalValueToUser(numberRange.a), transformInternalValueToUser(numberRange.b));
    }

    public final U getRangePaddingHigh() {
        return this.G;
    }

    public final void setRangePaddingHigh(U rangePaddingHigh) {
        this.G = rangePaddingHigh;
        if (rangePaddingHigh == null) {
            this.I = Constants.SPLITS_ACCURACY;
        } else {
            this.I = transformExternalFrequencyToInternal(rangePaddingHigh);
        }
        f();
        if (this.b != null) {
            O();
            this.b.b.b();
        }
    }

    public final U getRangePaddingLow() {
        return this.H;
    }

    public final void setRangePaddingLow(U rangePaddingLow) {
        this.H = rangePaddingLow;
        if (rangePaddingLow == null) {
            this.J = Constants.SPLITS_ACCURACY;
        } else {
            this.J = transformExternalFrequencyToInternal(rangePaddingLow);
        }
        f();
        if (this.b != null) {
            O();
            this.b.b.b();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean j() {
        return !l();
    }

    public String getExpectedLongestLabel() {
        return this.x;
    }

    public void setExpectedLongestLabel(String longestLabel) {
        this.x = longestLabel;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean k() {
        return this.x != null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setMajorTickMarkValues(List<T> values) {
        if (values == null) {
            this.t = null;
        } else if (values.contains(null)) {
            throw new IllegalArgumentException(this.b != null ? this.b.getContext().getString(R.string.AxisNullCustomTickMarkValues) : "Custom tick mark values cannot contain null.");
        } else {
            TreeSet treeSet = new TreeSet(values);
            this.t = new double[treeSet.size()];
            int i = 0;
            Iterator it = treeSet.iterator();
            while (true) {
                int i2 = i;
                if (it.hasNext()) {
                    this.t[i2] = transformUserValueToInternal((Comparable) it.next());
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    public final U getMajorTickFrequency() {
        return this.p;
    }

    public final void setMajorTickFrequency(U majorTickFrequency) {
        setMajorTickFrequencyInternal(majorTickFrequency);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean l() {
        return this.p != null;
    }

    public final U getMinorTickFrequency() {
        return this.q;
    }

    public final void setMinorTickFrequency(U minorTickFrequency) {
        setMinorTickFrequencyInternal(minorTickFrequency);
    }

    public final U getCurrentMajorTickFrequency() {
        return this.r;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean m() {
        return this.r != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double n() {
        if (this.r == null) {
            throw new IllegalStateException(this.b != null ? this.b.getContext().getString(R.string.AxisNullMajorTickFrequency) : "Null currentMajorTickFrequency");
        }
        return transformExternalFrequencyToInternal(this.r);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCurrentMajorTickFrequency(U frequency) {
        this.r = frequency;
    }

    public final U getCurrentMinorTickFrequency() {
        return this.s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean o() {
        return this.s != null;
    }

    void setCurrentMinorTickFrequency(U frequency) {
        this.s = frequency;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(int i) {
        if (Range.a(this.i)) {
            cx.b(this.b != null ? this.b.getContext().getString(R.string.AxisUndefinedRange) : "The axis has an undefined data range and cannot be displayed");
        } else {
            a(j(), i);
        }
    }

    private void a(boolean z, int i) {
        if (z) {
            c(i);
            this.U = n();
            return;
        }
        p();
        r();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r() {
        String i = i();
        if (i != null) {
            a(this.Z, i);
        }
        a(this.Z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(PointF pointF, String str) {
        this.Y.b(pointF, str, this.g.g.c.a.floatValue(), this.g.g.b.a, this.b);
    }

    void a(PointF pointF) {
        double d;
        double d2;
        switch (this.g.g.l.a) {
            case HORIZONTAL:
                d = pointF.x;
                d2 = pointF.y;
                break;
            case DIAGONAL:
                d = (pointF.x + pointF.y) * 0.70710677f;
                d2 = (pointF.x + pointF.y) * 0.70710677f;
                break;
            case VERTICAL:
                d = pointF.y;
                d2 = pointF.x;
                break;
            default:
                throw new IllegalStateException(this.b != null ? this.b.getContext().getString(R.string.AxisUnrecognisedOrientation) : "tickLabel orientation not recognised");
        }
        this.V.x = (int) (d + 0.5d);
        this.V.y = (int) (d2 + 0.5d);
    }

    void s() {
        if (!this.F && !this.b.l()) {
            cx.b(this.b != null ? this.b.getContext().getString(R.string.AxisInsufficientWidth) : "Axis width does not provide enough space to fit the tickmarks and ticklabels.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(double d, double d2, double d3) {
        if (a()) {
            return b(d, d2, d3) + ((double) i.a(this.b.f, Position.NORMAL)) > Constants.SPLITS_ACCURACY;
        }
        return b(d, d2, d3) <= ((double) (this.b.getHeight() - i.a(this.b.e, Position.REVERSE)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int a(int i, int i2) {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double b(double d, double d2, double d3) {
        if (this.c == Orientation.HORIZONTAL) {
            return ((d - this.i.a) * d2) / d3;
        }
        return ((this.i.b - d) * d2) / d3;
    }

    public AxisStyle getStyle() {
        return this.g;
    }

    public final void setStyle(AxisStyle style) {
        if (this.g != null) {
            this.K.a();
        }
        this.g = style;
        if (this.g != null) {
            this.K = this.g.a(this.L);
            if (this.b != null) {
                P();
                Q();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class b implements PropertyChangedEvent.Handler {
        private b() {
        }

        @Override // com.shinobicontrols.charts.PropertyChangedEvent.Handler
        public void onPropertyChanged() {
            Axis.this.f();
            Axis.this.O();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void O() {
        if (this.b != null) {
            for (Series<?> series : this.b.getSeriesForAxis(this)) {
                series.p.a();
            }
        }
    }

    public TickMark.ClippingMode getTickMarkClippingModeHigh() {
        return this.M;
    }

    public TickMark.ClippingMode getTickMarkClippingModeLow() {
        return this.N;
    }

    public void setTickMarkClippingModeHigh(TickMark.ClippingMode tickLabelClippingModeHigh) {
        this.M = tickLabelClippingModeHigh;
    }

    public void setTickMarkClippingModeLow(TickMark.ClippingMode tickLabelClippingModeLow) {
        this.N = tickLabelClippingModeLow;
    }

    public final String getTitle() {
        return this.O;
    }

    public final void setTitle(String title) {
        this.O = title;
        if (this.R != null) {
            this.R.setText(title);
            a(this.R);
        }
    }

    private void a(Title title) {
        int i = 8;
        if (title != null && !a(title.getText().toString())) {
            i = 0;
        }
        title.setVisibility(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Title t() {
        return u();
    }

    Title u() {
        if (this.R == null && this.b != null) {
            this.R = new Title(this.b.getContext());
            this.R.setLayoutParams(new ViewGroup.MarginLayoutParams(-2, -2));
            v();
            this.R.setText(this.O);
            a(this.R);
        }
        return this.R;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v() {
        if (this.R != null && this.g.h != null) {
            AxisTitleStyle axisTitleStyle = this.g.h;
            this.R.a(axisTitleStyle.getOrientation());
            this.R.a(axisTitleStyle);
        }
    }

    public final Float getWidth() {
        return this.P;
    }

    public final void setWidth(Float width) {
        this.P = width;
    }

    public boolean isPanningOutOfDefaultRangeAllowed() {
        return this.v.a;
    }

    public boolean isPanningOutOfMaxRangeAllowed() {
        return this.v.b;
    }

    public boolean isBouncingAtLimitsEnabled() {
        return this.v.c;
    }

    public boolean isAnimationEnabled() {
        return this.v.d;
    }

    public boolean isGesturePanningEnabled() {
        return this.v.e;
    }

    public boolean isGestureZoomingEnabled() {
        return this.v.f;
    }

    public boolean isMomentumPanningEnabled() {
        return this.v.g;
    }

    public boolean isMomentumZoomingEnabled() {
        return this.v.h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(double d, boolean z, boolean z2) {
        if (d == Constants.SPLITS_ACCURACY) {
            return false;
        }
        return this.v.a(d, z, z2);
    }

    public void allowPanningOutOfDefaultRange(boolean allowPanningOutOfDefaultRange) {
        this.v.a = allowPanningOutOfDefaultRange;
        if (!allowPanningOutOfDefaultRange) {
            this.v.a();
        }
    }

    public void allowPanningOutOfMaxRange(boolean allowPanningOutOfMaxRange) {
        this.v.b = allowPanningOutOfMaxRange;
        if (!allowPanningOutOfMaxRange) {
            this.v.a();
        }
    }

    public void enableBouncingAtLimits(boolean bounceAtLimits) {
        this.v.c = bounceAtLimits;
    }

    public void enableAnimation(boolean animationEnabled) {
        this.v.d = animationEnabled;
    }

    public void enableGesturePanning(boolean enableGesturePanning) {
        this.v.e = enableGesturePanning;
    }

    public void enableGestureZooming(boolean enableGestureZooming) {
        this.v.f = enableGestureZooming;
    }

    public void enableMomentumPanning(boolean enableMomentumPanning) {
        this.v.g = enableMomentumPanning;
    }

    public void enableMomentumZooming(boolean enableMomentumZooming) {
        this.v.h = enableMomentumZooming;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(double d, double d2, boolean z, boolean z2) {
        if (d <= Constants.SPLITS_ACCURACY || Double.isInfinite(d) || Double.isNaN(d)) {
            cx.a("Zoom must be greater than 0 and a real number");
            return false;
        }
        return this.v.b(d, d2, z, z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double w() {
        return this.v.b();
    }

    boolean c(double d) {
        return d > n();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class c {
        int A;
        double B;
        float C;
        float D;
        float E;
        TickMark.ClippingMode F;
        TickMark.ClippingMode G;
        float H;
        float I;
        boolean J;
        Rect K = new Rect();
        Rect L = new Rect();
        Point M = new Point();
        Rect a;
        boolean b;
        boolean c;
        boolean d;
        boolean e;
        boolean f;
        float g;
        float h;
        float i;
        float j;
        float k;
        float l;
        float m;
        int n;
        int o;
        int p;
        int q;
        Typeface r;
        float s;
        TickMark.Orientation t;
        boolean u;
        DashPathEffect v;
        int w;
        int x;
        Point y;
        float z;

        c() {
        }
    }

    private void e(int i) {
        this.W.A = i;
        this.W.B = this.i.b();
        this.W.b = this.g.g.h.a.booleanValue();
        this.W.c = this.g.g.i.a.booleanValue();
        this.W.d = this.g.g.j.a.booleanValue();
        this.W.e = this.g.f.a.a.booleanValue();
        this.W.f = this.g.e.c.a.booleanValue();
        this.W.g = this.g.g.f.a.floatValue();
        this.W.h = this.g.g.g.a.floatValue();
        this.W.k = this.W.h / 2.0f;
        this.W.m = this.g.g.k.a.floatValue();
        this.W.n = this.g.g.e.a.intValue();
        this.W.o = this.g.f.c.a.intValue();
        this.W.t = this.g.g.l.a;
        this.W.p = this.g.g.a.a.intValue();
        this.W.q = this.g.g.d.a.intValue();
        this.W.r = this.g.g.b.a;
        this.W.s = this.g.g.c.a.floatValue() * this.b.getResources().getDisplayMetrics().scaledDensity;
        this.W.u = this.g.f.b.a.booleanValue();
        this.W.v = S();
        this.W.w = this.g.e.a.a.intValue();
        this.W.x = this.g.e.b.a.intValue();
        this.W.y = this.V;
        this.W.C = this.W.A;
        this.W.D = 0.0f;
        this.W.E = a() ? this.W.y.x : this.W.y.y;
        this.W.F = a() ? this.M : this.N;
        this.W.G = a() ? this.N : this.M;
        this.W.H = 0.0f;
        if (this.W.c || this.W.d) {
            this.W.H = this.W.g + this.W.m;
        }
        this.W.I = this.h;
        this.W.J = G();
        this.W.l = getStyle().getLineWidth();
        this.W.i = getStyle().getGridlineStyle().getLineWidth();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Canvas canvas, Rect rect) {
        this.n.a(this.X, rect, this.g.getLineWidth(), this.h, b());
        this.T.setColor(this.g.c.a.intValue());
        canvas.drawRect(this.X, this.T);
        e(this.c == Orientation.HORIZONTAL ? rect.width() : rect.height());
        this.W.a = rect;
        this.n.a(this.W);
        this.u.a(this.W);
        s();
        this.u.a(canvas, this.W);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(boolean z) {
        float max = Math.max(this.g.d.a.floatValue(), 0.0f);
        if (Range.a(this.i)) {
            this.o = this.n.a(0, max);
            return;
        }
        this.o = this.n.a(0, ((this.g.g.i.a.booleanValue() || this.g.g.j.a.booleanValue()) ? Math.max(this.g.g.f.a.floatValue(), 0.0f) + Math.max(this.g.g.k.a.floatValue(), 0.0f) : 0.0f) + max);
        if (this.g.g.h.a.booleanValue()) {
            this.o = (a() ? this.V.y : this.V.x) + this.o;
        }
        if (u().getVisibility() == 0 && z) {
            this.o = (a() ? at.a(u()) : at.b(u())) + this.o;
        }
        if (this.P != null) {
            float max2 = Math.max(0.0f, this.P.floatValue());
            this.F = max2 >= ((float) this.o);
            this.o = Math.round(max2);
            return;
        }
        this.F = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(int i, int i2, PointF pointF) {
        float f = pointF.x * this.V.x;
        float f2 = this.V.y * pointF.y;
        if (c(this.U)) {
            f *= 1.3f;
            f2 *= 1.3f;
        }
        return i >= 0 && (!a() || ((float) i2) - (f * ((float) i)) >= 0.0f) && (a() || ((float) i2) - (f2 * ((float) i)) >= 0.0f);
    }

    w y() {
        return this.b.k();
    }

    private void P() {
        if (!this.E) {
            this.f = Constants.SPLITS_ACCURACY;
            List<Series<?>> seriesForAxis = this.b.getSeriesForAxis(this);
            if (!seriesForAxis.isEmpty()) {
                List<InternalDataPoint> b2 = b(seriesForAxis);
                if (b2.size() != 0) {
                    this.e = a(b2.get(0));
                    this.D = a(b2.get(b2.size() - 1));
                    if (this.D - this.e == Constants.SPLITS_ACCURACY) {
                        this.f = x();
                        return;
                    }
                    boolean z = false;
                    for (int i = 0; i < b2.size() - 1; i++) {
                        double a2 = a(b2.get(i));
                        double a3 = a(b2.get(i + 1));
                        double abs = Math.abs(a3 - a2);
                        if (a2 != a3 && (!z || abs < this.f)) {
                            this.f = abs;
                            z = true;
                        }
                    }
                }
            }
        }
    }

    private List<InternalDataPoint> b(List<Series<?>> list) {
        ArrayList arrayList = new ArrayList();
        for (Series<?> series : list) {
            if ((series instanceof BarColumnSeries) && a(((BarColumnSeries) series).j)) {
                for (int i = 0; i < series.n.c.length; i++) {
                    arrayList.add(series.n.c[i]);
                }
            }
        }
        if (a()) {
            Collections.sort(arrayList, InternalDataPoint.k);
        } else {
            Collections.sort(arrayList, InternalDataPoint.l);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(Series.Orientation orientation) {
        return (orientation == Series.Orientation.HORIZONTAL && this.c == Orientation.HORIZONTAL) || (orientation == Series.Orientation.VERTICAL && this.c == Orientation.VERTICAL);
    }

    private double a(InternalDataPoint internalDataPoint) {
        return this.c == Orientation.HORIZONTAL ? internalDataPoint.a : internalDataPoint.b;
    }

    private void Q() {
        List<Series<?>> seriesForAxis = this.b.getSeriesForAxis(this);
        ArrayList<cr> arrayList = new ArrayList();
        for (Series<?> series : seriesForAxis) {
            cr crVar = series.t;
            if (crVar != null && !arrayList.contains(crVar)) {
                arrayList.add(crVar);
            }
        }
        for (cr crVar2 : arrayList) {
            crVar2.d(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShinobiChart.OnGestureListener z() {
        return this.v;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double d(double d) {
        Rect rect = y().b;
        return (d / (this.c == Orientation.HORIZONTAL ? rect.width() : rect.height())) * this.i.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double e(double d) {
        Rect rect = y().b;
        if (this.c != Orientation.HORIZONTAL) {
            d = rect.height() - d;
        }
        return d(d) + this.i.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double a(double d, CartesianSeries<?> cartesianSeries) {
        double d2;
        if (this.c == Orientation.VERTICAL) {
            d2 = this.b.b.b.top;
        } else {
            d2 = this.b.b.b.left;
        }
        return d2 + this.n.a(d, this.b.b.b.width(), this.b.b.b.height());
    }

    public float getPixelValueForUserValue(T userValue) {
        if (Range.a(this.i)) {
            cx.a(this.b != null ? this.b.getContext().getString(R.string.AxisRangeNotSetPixelCall) : "Calling getPixelValueForUserValue before an axisRange has been set.");
            return 0.0f;
        }
        return (float) f(transformUserValueToInternal(userValue));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double f(double d) {
        return this.n.a(d, this.b.b.b.width(), this.b.b.b.height()) + R();
    }

    public T getUserValueForPixelValue(float pixelValue) {
        if (Range.a(this.i)) {
            cx.a(this.b != null ? this.b.getContext().getString(R.string.AxisRangeNotSetUserCall) : "Calling getUserValueForPixelValue before an axisRange has been set.");
            return null;
        }
        return transformInternalValueToUser(this.n.a(pixelValue - R(), this.b.b.b));
    }

    private int R() {
        return this.c == Orientation.HORIZONTAL ? this.b.a.left + this.b.b.b.left : this.b.a.top + this.b.b.b.top;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public df B() {
        return new df();
    }

    public MotionState getMotionState() {
        return this.v.l;
    }

    public final ShinobiChart getChart() {
        return this.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Rect rect, int i, int i2) {
        if (this.R != null) {
            this.n.a(rect, this.o, this.h, this.R, this.S.a);
            Gravity.apply(this.n.a(this.g.h.g.a), this.R.getMeasuredWidth(), this.R.getMeasuredHeight(), this.S.a, this.S.b());
            at.b(this.R, this.S.b);
        }
    }

    private DashPathEffect S() {
        if (this.g.f.d.a == null || this.g.f.d.a.length < 1) {
            return null;
        }
        float[] fArr = new float[this.g.f.d.a.length];
        for (int i = 0; i < fArr.length; i++) {
            fArr[i] = at.a(this.a, 0, this.g.f.d.a[i]);
        }
        return new DashPathEffect(fArr, 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(v vVar) {
        this.b = vVar;
        if (vVar != null) {
            this.a = vVar.getContext().getResources().getDisplayMetrics().density;
            if (this.ab.size() > 0) {
                vVar.s();
            }
        }
    }

    public void removeAllSkipRanges() {
        this.ab.clear();
        D();
    }

    public List<Range<T>> getSkipRanges() {
        return Collections.unmodifiableList(this.ab);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public am a(db.a aVar) {
        return this.C.a(db.a, aVar);
    }

    final void C() {
        this.C.a(new db());
    }

    public void addSkipRange(Range<T> skipRange) {
        if (this.b != null) {
            this.b.s();
        }
        if (!b(skipRange)) {
            cx.b(this.b != null ? this.b.getContext().getString(R.string.CannotAddNullUndefinedOrEmptySkip) : "Cannot add a null skip range or one with a zero or negative span.");
            return;
        }
        this.ab.add(skipRange);
        D();
    }

    public void removeSkipRange(Range<T> skipRange) {
        this.ab.remove(skipRange);
        D();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void D() {
        Range<T> currentDisplayedRange = getCurrentDisplayedRange();
        a(E());
        if (Range.b(this.m) && !this.m.c()) {
            a(this.m);
        }
        C();
        T();
        if (this.b != null) {
            this.b.redrawChart();
            e(currentDisplayedRange);
        }
    }

    private void T() {
        Set<CartesianSeries<?>> e;
        HashSet<Axis> hashSet = new HashSet();
        hashSet.add(this);
        if (this.b != null && (e = this.b.d.e(this)) != null) {
            for (CartesianSeries<?> cartesianSeries : e) {
                hashSet.add(cartesianSeries.getXAxis());
                hashSet.add(cartesianSeries.getYAxis());
            }
        }
        for (Axis axis : hashSet) {
            axis.f();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Range<T>> E() {
        return this.ab;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean F() {
        return E().size() > 0;
    }

    private void e(final Range<T> range) {
        if (Range.b(range)) {
            this.b.post(new Runnable() { // from class: com.shinobicontrols.charts.Axis.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.lang.Runnable
                public void run() {
                    Axis.this.requestCurrentDisplayedRange(range.getMinimum(), range.getMaximum(), false, false);
                }
            });
        }
    }

    void a(List<Range<T>> list) {
        List<Range<T>> b2 = this.ac.b(this.ac.a(list));
        this.y.a(this.ad.a(b2));
        this.z.a(this.ae.a(b2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(Range<T> range) {
        return Range.b(range) && !range.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUserDataPointWithinASkipRange(Object rawUserValue) {
        ap b2 = this.y.b(convertUserValueTypeToInternalDataType(rawUserValue));
        return b2 != null && b2.c.a == Constants.SPLITS_ACCURACY;
    }
}
