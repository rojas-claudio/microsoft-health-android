package com.shinobicontrols.charts;

import android.graphics.PointF;
import com.microsoft.kapp.utils.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class CategoryAxis extends NumberAxis {
    private final List<String> A = new ArrayList();
    private final List<String> B = new ArrayList();

    public CategoryAxis() {
    }

    public CategoryAxis(NumberRange range) {
        setDefaultRange(range);
    }

    @Override // com.shinobicontrols.charts.NumberAxis, com.shinobicontrols.charts.Axis
    boolean isDataValid(Object point) {
        return point.toString() != null;
    }

    @Override // com.shinobicontrols.charts.NumberAxis, com.shinobicontrols.charts.Axis
    double convertPoint(Object userData) {
        validateUserData(userData);
        String obj = userData.toString();
        b(obj);
        c(obj);
        return this.A.indexOf(obj);
    }

    private void b(String str) {
        if (this.B.contains(str)) {
            throw new IllegalArgumentException(this.b.getContext().getString(R.string.CategoryAxisSeriesAlreadyContainsPoint) + " '" + str + "'");
        }
        this.B.add(str);
    }

    private void c(String str) {
        if (!this.A.contains(str)) {
            this.A.add(str);
        }
    }

    public List<String> getCategories() {
        return Collections.unmodifiableList(this.A);
    }

    public boolean requestCurrentDisplayedRange(int minimum, int maximum) {
        return requestCurrentDisplayedRange(Double.valueOf(minimum), Double.valueOf(maximum));
    }

    public boolean requestCurrentDisplayedRange(int minimum, int maximum, boolean animation, boolean bounceAtLimits) {
        return requestCurrentDisplayedRange(Double.valueOf(minimum), Double.valueOf(maximum), animation, bounceAtLimits);
    }

    @Override // com.shinobicontrols.charts.NumberAxis, com.shinobicontrols.charts.Axis
    String i() {
        int i;
        float f;
        if (k()) {
            return this.x;
        }
        if (this.A.size() == 0) {
            this.w = null;
        } else {
            float f2 = 0.0f;
            int size = this.A.size();
            int i2 = 0;
            int i3 = 0;
            while (i2 < size) {
                PointF pointF = new PointF();
                a(pointF, this.A.get(i2));
                float f3 = pointF.x;
                if (f3 > f2) {
                    i = this.A.indexOf(this.A.get(i2));
                    f = f3;
                } else {
                    i = i3;
                    f = f2;
                }
                i2++;
                f2 = f;
                i3 = i;
            }
            this.w = this.A.get(i3);
        }
        return this.w;
    }

    @Override // com.shinobicontrols.charts.NumberAxis, com.shinobicontrols.charts.Axis
    public String getFormattedString(Double value) {
        if (value == null) {
            return null;
        }
        int round = (int) Math.round(value.doubleValue());
        if (e(round)) {
            return this.A.get(round);
        }
        return null;
    }

    private boolean e(int i) {
        return i >= 0 && i < this.A.size();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Double, U] */
    @Override // com.shinobicontrols.charts.NumberAxis, com.shinobicontrols.charts.Axis
    void c(int i) {
        this.r = Double.valueOf(1.0d);
        r();
    }

    @Override // com.shinobicontrols.charts.NumberAxis, com.shinobicontrols.charts.Axis
    double a(int i) {
        double ceil = Math.ceil(this.i.a);
        int i2 = (int) ceil;
        int intValue = this.j.getMinimum().intValue();
        if (i2 < intValue) {
            ceil = intValue;
        }
        if (!a(ceil, i, this.i.b())) {
            return a(ceil, true);
        }
        return ceil;
    }

    @Override // com.shinobicontrols.charts.NumberAxis, com.shinobicontrols.charts.Axis
    double b(int i) {
        return Double.NaN;
    }

    @Override // com.shinobicontrols.charts.NumberAxis, com.shinobicontrols.charts.Axis
    boolean b(double d) {
        return Math.IEEEremainder(d, 2.0d) == Constants.SPLITS_ACCURACY;
    }

    @Override // com.shinobicontrols.charts.NumberAxis, com.shinobicontrols.charts.Axis
    double a(double d, boolean z) {
        return 1.0d + d;
    }

    @Override // com.shinobicontrols.charts.NumberAxis
    double b(double d, boolean z) {
        return d - 1.0d;
    }

    @Override // com.shinobicontrols.charts.Axis
    public void setMajorTickMarkValues(List<Double> values) {
        cx.b(this.b != null ? this.b.getContext().getString(R.string.CategoryAxisIgnoresCustomTickValues) : "Category axes ignore custom tick mark values");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public void A() {
        this.B.clear();
        this.u.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public df B() {
        return new u();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public boolean j() {
        return true;
    }

    @Override // com.shinobicontrols.charts.Axis
    public void addSkipRange(Range<Double> skipRange) {
        cx.b(this.b != null ? this.b.getContext().getString(R.string.CannotAddSkipToCategoryAxis) : "Cannot add a skip range to a category axis.");
    }
}
