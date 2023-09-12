package com.shinobicontrols.charts;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.ViewGroup;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.Title;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class i {
    private static i b = new i(null) { // from class: com.shinobicontrols.charts.i.1
        @Override // com.shinobicontrols.charts.i
        int a(Title.Position position) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.i
        void a(Axis.c cVar) {
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, Rect rect2, float f, int i, float f2) {
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, Axis.c cVar, int i, boolean z) {
        }

        @Override // com.shinobicontrols.charts.i
        void a(Point point, Axis.c cVar, int i) {
        }

        @Override // com.shinobicontrols.charts.i
        double a(double d2, int i, int i2) {
            return Constants.SPLITS_ACCURACY;
        }

        @Override // com.shinobicontrols.charts.i
        double a(double d2, Rect rect) {
            return Constants.SPLITS_ACCURACY;
        }

        @Override // com.shinobicontrols.charts.i
        void c(Path path, Axis.c cVar, int i, Paint paint) {
        }

        @Override // com.shinobicontrols.charts.i
        void c(Rect rect, Axis.c cVar, PointF pointF, PointF pointF2) {
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, int i, int i2, Title title, Rect rect2) {
        }
    };
    protected Axis<?, ?> a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract double a(double d2, int i, int i2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract double a(double d2, Rect rect);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int a(Title.Position position);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a(Point point, Axis.c cVar, int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a(Rect rect, int i, int i2, Title title, Rect rect2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a(Rect rect, Rect rect2, float f, int i, float f2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a(Rect rect, Axis.c cVar, int i, boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a(Axis.c cVar);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void c(Path path, Axis.c cVar, int i, Paint paint);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void c(Rect rect, Axis.c cVar, PointF pointF, PointF pointF2);

    protected i(Axis<?, ?> axis) {
        this.a = axis;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static i a(Axis<?, ?> axis) {
        if (axis == null || axis.c == null || axis.d == null) {
            return b;
        }
        if (axis.c == Axis.Orientation.HORIZONTAL) {
            if (axis.d == Axis.Position.NORMAL) {
                return new a(axis);
            }
            if (axis.d == Axis.Position.REVERSE) {
                return new c(axis);
            }
            throw new AssertionError("Axis Position invalid:" + axis.d);
        } else if (axis.c == Axis.Orientation.VERTICAL) {
            if (axis.d == Axis.Position.NORMAL) {
                return new b(axis);
            }
            if (axis.d == Axis.Position.REVERSE) {
                return new d(axis);
            }
            throw new AssertionError("Axis Position invalid:" + axis.d);
        } else {
            throw new AssertionError("Axis Orientation invalid:" + axis.c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int a(int i, float f) {
        return at.a(this.a.a, i, f);
    }

    void a(Path path, Axis.c cVar, int i, Paint paint) {
        path.reset();
        int i2 = cVar.a.left + i;
        path.moveTo(i2, cVar.a.top);
        path.lineTo(i2, cVar.a.bottom);
        int i3 = i2 - cVar.a.left;
        if (i3 < cVar.j) {
            float f = cVar.j - i3;
            paint.setStrokeWidth(paint.getStrokeWidth() - f);
            path.offset(f / 2.0f, 0.0f);
        }
        int i4 = cVar.a.right - i2;
        if (i4 < cVar.j) {
            float f2 = cVar.j - i4;
            paint.setStrokeWidth(paint.getStrokeWidth() - f2);
            path.offset((-f2) / 2.0f, 0.0f);
        }
    }

    void b(Path path, Axis.c cVar, int i, Paint paint) {
        path.reset();
        int i2 = cVar.a.top + i;
        path.moveTo(cVar.a.left, i2);
        path.lineTo(cVar.a.right, i2);
        int i3 = cVar.a.bottom - i2;
        if (i3 < cVar.j) {
            float f = cVar.j - i3;
            paint.setStrokeWidth(paint.getStrokeWidth() - f);
            path.offset(0.0f, (-f) / 2.0f);
        }
        int i4 = i2 - cVar.a.top;
        if (i4 < cVar.j) {
            float f2 = cVar.j - i4;
            paint.setStrokeWidth(paint.getStrokeWidth() - f2);
            path.offset(0.0f, f2 / 2.0f);
        }
    }

    void a(Rect rect, Axis.c cVar, PointF pointF, PointF pointF2) {
        rect.left = (int) pointF.x;
        rect.right = (int) pointF2.x;
        rect.top = cVar.a.top;
        rect.bottom = a(cVar.a.bottom, cVar.I);
    }

    void b(Rect rect, Axis.c cVar, PointF pointF, PointF pointF2) {
        rect.left = cVar.a.left;
        rect.right = cVar.a.right;
        rect.top = (int) pointF2.y;
        rect.bottom = (int) pointF.y;
    }

    static double a(double d2, int i, double d3, double d4) {
        return ((d2 - d3) * i) / d4;
    }

    static double a(double d2, Rect rect, double d3, double d4) {
        return ((d2 * d4) / (rect.right - rect.left)) + d3;
    }

    static double b(double d2, int i, double d3, double d4) {
        return i - (((d2 - d3) * i) / d4);
    }

    static double b(double d2, Rect rect, double d3, double d4) {
        return ((1.0d - (d2 / (rect.bottom - rect.top))) * d4) + d3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(h hVar, Axis.Position position) {
        int i = 0;
        for (int i2 = 0; i2 < hVar.a.length; i2++) {
            Axis<?, ?> axis = hVar.a[i2];
            if (position == axis.d) {
                i += axis.o;
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a extends i {
        protected a(Axis<?, ?> axis) {
            super(axis);
        }

        @Override // com.shinobicontrols.charts.i
        int a(Title.Position position) {
            return position.a();
        }

        @Override // com.shinobicontrols.charts.i
        void a(Axis.c cVar) {
            float f = cVar.l + cVar.H + cVar.z;
            cVar.j = a(0, cVar.i / 2.0f);
            cVar.K.left = a(cVar.a.left, (-cVar.h) / 2.0f);
            cVar.K.right = a(cVar.a.left, cVar.h / 2.0f);
            cVar.K.top = (int) (a(cVar.a.bottom, (f - cVar.m) - cVar.g) + cVar.I);
            cVar.K.bottom = (int) (a(cVar.a.bottom, f - cVar.m) + cVar.I);
            cVar.L.bottom = (int) (a(cVar.a.bottom, (f - cVar.m) - (cVar.g * 0.5f)) + cVar.I);
            float a = cVar.I + a(0, cVar.l + cVar.H + cVar.z);
            float f2 = (-cVar.y.x) / 2.0f;
            float f3 = f2 + cVar.y.x;
            float f4 = cVar.y.y + a;
            if (cVar.J) {
                a -= cVar.z;
                f4 -= cVar.z;
            }
            cVar.M.x = (int) (((f2 + f3) / 2.0f) + cVar.a.left);
            cVar.M.y = (int) (((f4 + a) / 2.0f) + cVar.a.bottom);
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, Rect rect2, float f, int i, float f2) {
            rect.left = rect2.left;
            rect.right = rect2.right;
            rect.top = rect2.bottom + i;
            rect.bottom = a(rect2.bottom + i, f);
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, Axis.c cVar, int i, boolean z) {
            rect.left = cVar.K.left + i;
            rect.right = cVar.K.right + i;
            rect.top = cVar.K.top;
            if (z) {
                rect.bottom = cVar.K.bottom;
            } else {
                rect.bottom = cVar.L.bottom;
            }
        }

        @Override // com.shinobicontrols.charts.i
        void a(Point point, Axis.c cVar, int i) {
            point.x = cVar.M.x + i;
            point.y = cVar.M.y;
        }

        @Override // com.shinobicontrols.charts.i
        double a(double d, int i, int i2) {
            return a(d, i, this.a.i.a, this.a.i.b());
        }

        @Override // com.shinobicontrols.charts.i
        double a(double d, Rect rect) {
            return a(d, rect, this.a.i.a, this.a.i.b());
        }

        @Override // com.shinobicontrols.charts.i
        void c(Path path, Axis.c cVar, int i, Paint paint) {
            a(path, cVar, i, paint);
        }

        @Override // com.shinobicontrols.charts.i
        void c(Rect rect, Axis.c cVar, PointF pointF, PointF pointF2) {
            a(rect, cVar, pointF, pointF2);
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, int i, int i2, Title title, Rect rect2) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) title.getLayoutParams();
            int measuredHeight = marginLayoutParams.bottomMargin + marginLayoutParams.topMargin + title.getMeasuredHeight();
            rect2.left = rect.left;
            rect2.top = ((rect.bottom + i2) + i) - measuredHeight;
            rect2.right = rect.right;
            rect2.bottom = rect.bottom + i2 + i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class b extends i {
        protected b(Axis<?, ?> axis) {
            super(axis);
        }

        @Override // com.shinobicontrols.charts.i
        int a(Title.Position position) {
            return position.b();
        }

        @Override // com.shinobicontrols.charts.i
        void a(Axis.c cVar) {
            float f = -(cVar.l + cVar.H + (-cVar.z));
            cVar.j = a(0, cVar.i / 2.0f);
            cVar.K.left = (int) (a(cVar.a.left, cVar.m + f) - cVar.I);
            cVar.L.left = (int) (a(cVar.a.left, (cVar.m + f) + (cVar.g * 0.5f)) - cVar.I);
            cVar.K.right = (int) (a(cVar.a.left, (f + cVar.m) + cVar.g) - cVar.I);
            cVar.K.top = a(cVar.a.top, (-cVar.h) / 2.0f);
            cVar.K.bottom = a(cVar.a.top, cVar.h / 2.0f);
            float a = (a(0, -((cVar.l + cVar.H) + (-cVar.z))) - cVar.I) - cVar.y.x;
            float f2 = (-cVar.y.y) / 2.0f;
            float f3 = cVar.y.x + a;
            float f4 = cVar.y.y + f2;
            if (cVar.J) {
                a -= cVar.z;
                f3 -= cVar.z;
            }
            cVar.M.x = (int) (((f3 + a) / 2.0f) + cVar.a.left);
            cVar.M.y = (int) (cVar.a.top + ((f2 + f4) / 2.0f));
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, Rect rect2, float f, int i, float f2) {
            rect.left = a(rect2.left - i, -f);
            rect.right = rect2.left - i;
            rect.top = a(rect2.top, -f2);
            rect.bottom = a(rect2.bottom, f);
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, Axis.c cVar, int i, boolean z) {
            if (z) {
                rect.left = cVar.K.left;
            } else {
                rect.left = cVar.L.left;
            }
            rect.right = cVar.K.right;
            rect.top = cVar.K.top + i;
            rect.bottom = cVar.K.bottom + i;
        }

        @Override // com.shinobicontrols.charts.i
        void a(Point point, Axis.c cVar, int i) {
            point.x = cVar.M.x;
            point.y = cVar.M.y + i;
        }

        @Override // com.shinobicontrols.charts.i
        double a(double d, int i, int i2) {
            return b(d, i2, this.a.i.a, this.a.i.b());
        }

        @Override // com.shinobicontrols.charts.i
        double a(double d, Rect rect) {
            return b(d, rect, this.a.i.a, this.a.i.b());
        }

        @Override // com.shinobicontrols.charts.i
        void c(Path path, Axis.c cVar, int i, Paint paint) {
            b(path, cVar, i, paint);
        }

        @Override // com.shinobicontrols.charts.i
        void c(Rect rect, Axis.c cVar, PointF pointF, PointF pointF2) {
            b(rect, cVar, pointF, pointF2);
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, int i, int i2, Title title, Rect rect2) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) title.getLayoutParams();
            int measuredWidth = marginLayoutParams.rightMargin + marginLayoutParams.leftMargin + title.getMeasuredWidth();
            rect2.left = (rect.left - i2) - i;
            rect2.top = rect.top;
            rect2.right = measuredWidth + ((rect.left - i2) - i);
            rect2.bottom = rect.bottom;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class c extends i {
        protected c(Axis<?, ?> axis) {
            super(axis);
        }

        @Override // com.shinobicontrols.charts.i
        int a(Title.Position position) {
            return position.a();
        }

        @Override // com.shinobicontrols.charts.i
        void a(Axis.c cVar) {
            float f = ((-cVar.l) - cVar.H) + cVar.z;
            cVar.j = a(0, cVar.i / 2.0f);
            cVar.K.left = a(cVar.a.left, (-cVar.h) / 2.0f);
            cVar.K.right = a(cVar.a.left, cVar.h / 2.0f);
            cVar.K.top = (int) (a(cVar.a.top, cVar.m + f) + cVar.I);
            cVar.L.top = (int) (a(cVar.a.top, cVar.m + f + (cVar.g * 0.5f)) + cVar.I);
            cVar.K.bottom = (int) (a(cVar.a.top, f + cVar.m + cVar.g) + cVar.I);
            float a = a(0, ((-cVar.l) - cVar.H) + cVar.z) + cVar.I;
            float f2 = (-cVar.y.x) / 2.0f;
            float f3 = a - cVar.y.y;
            float f4 = f2 + cVar.y.x;
            float f5 = cVar.y.y + f3;
            if (cVar.J) {
                f3 -= cVar.z;
                f5 -= cVar.z;
            }
            cVar.M.x = (int) (((f2 + f4) / 2.0f) + cVar.a.left);
            cVar.M.y = (int) (((f5 + f3) / 2.0f) + cVar.a.top);
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, Rect rect2, float f, int i, float f2) {
            rect.left = rect2.left;
            rect.right = rect2.right;
            rect.top = a(rect2.top + i, -f);
            rect.bottom = rect2.top + i;
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, Axis.c cVar, int i, boolean z) {
            rect.left = cVar.K.left + i;
            rect.right = cVar.K.right + i;
            if (z) {
                rect.top = cVar.K.top;
            } else {
                rect.top = cVar.L.top;
            }
            rect.bottom = cVar.K.bottom;
        }

        @Override // com.shinobicontrols.charts.i
        void a(Point point, Axis.c cVar, int i) {
            point.x = cVar.M.x + i;
            point.y = cVar.M.y;
        }

        @Override // com.shinobicontrols.charts.i
        double a(double d, int i, int i2) {
            return a(d, i, this.a.i.a, this.a.i.b());
        }

        @Override // com.shinobicontrols.charts.i
        double a(double d, Rect rect) {
            return a(d, rect, this.a.i.a, this.a.i.b());
        }

        @Override // com.shinobicontrols.charts.i
        void c(Path path, Axis.c cVar, int i, Paint paint) {
            a(path, cVar, i, paint);
        }

        @Override // com.shinobicontrols.charts.i
        void c(Rect rect, Axis.c cVar, PointF pointF, PointF pointF2) {
            a(rect, cVar, pointF, pointF2);
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, int i, int i2, Title title, Rect rect2) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) title.getLayoutParams();
            int measuredHeight = marginLayoutParams.bottomMargin + marginLayoutParams.topMargin + title.getMeasuredHeight();
            rect2.left = rect.left;
            rect2.top = (rect.top + i2) - i;
            rect2.right = rect.right;
            rect2.bottom = measuredHeight + ((rect.top + i2) - i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class d extends i {
        protected d(Axis<?, ?> axis) {
            super(axis);
        }

        @Override // com.shinobicontrols.charts.i
        int a(Title.Position position) {
            return position.b();
        }

        @Override // com.shinobicontrols.charts.i
        void a(Axis.c cVar) {
            float f = (cVar.l + cVar.H) - cVar.z;
            cVar.j = a(0, cVar.i / 2.0f);
            cVar.K.left = (int) (a(cVar.a.right, (f - cVar.m) - cVar.g) - cVar.I);
            cVar.K.right = (int) (a(cVar.a.right, f - cVar.m) - cVar.I);
            cVar.L.right = (int) (a(cVar.a.right, (f - cVar.m) - (cVar.g * 0.5f)) - cVar.I);
            cVar.K.top = a(cVar.a.top, (-cVar.h) / 2.0f);
            cVar.K.bottom = a(cVar.a.top, cVar.h / 2.0f);
            float a = a(0, (cVar.l + cVar.H) - cVar.z) - cVar.I;
            float f2 = (-cVar.y.y) / 2.0f;
            float f3 = cVar.y.x + a;
            float f4 = cVar.y.y + f2;
            if (cVar.J) {
                a -= cVar.z;
                f3 -= cVar.z;
            }
            cVar.M.x = (int) (((f3 + a) / 2.0f) + cVar.a.right);
            cVar.M.y = (int) (cVar.a.top + ((f2 + f4) / 2.0f));
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, Rect rect2, float f, int i, float f2) {
            rect.left = rect2.right - i;
            rect.right = a(rect2.right - i, f);
            rect.top = a(rect2.top, -f2);
            rect.bottom = a(rect2.bottom, f);
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, Axis.c cVar, int i, boolean z) {
            rect.left = cVar.K.left;
            if (z) {
                rect.right = cVar.K.right;
            } else {
                rect.right = cVar.L.right;
            }
            rect.top = cVar.K.top + i;
            rect.bottom = cVar.K.bottom + i;
        }

        @Override // com.shinobicontrols.charts.i
        void a(Point point, Axis.c cVar, int i) {
            point.x = cVar.M.x;
            point.y = cVar.M.y + i;
        }

        @Override // com.shinobicontrols.charts.i
        double a(double d, int i, int i2) {
            return b(d, i2, this.a.i.a, this.a.i.b());
        }

        @Override // com.shinobicontrols.charts.i
        double a(double d, Rect rect) {
            return b(d, rect, this.a.i.a, this.a.i.b());
        }

        @Override // com.shinobicontrols.charts.i
        void c(Path path, Axis.c cVar, int i, Paint paint) {
            b(path, cVar, i, paint);
        }

        @Override // com.shinobicontrols.charts.i
        void c(Rect rect, Axis.c cVar, PointF pointF, PointF pointF2) {
            b(rect, cVar, pointF, pointF2);
        }

        @Override // com.shinobicontrols.charts.i
        void a(Rect rect, int i, int i2, Title title, Rect rect2) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) title.getLayoutParams();
            rect2.left = ((rect.right - i2) + i) - (marginLayoutParams.rightMargin + (marginLayoutParams.leftMargin + title.getMeasuredWidth()));
            rect2.top = rect.top;
            rect2.right = (rect.right - i2) + i;
            rect2.bottom = rect.bottom;
        }
    }
}
