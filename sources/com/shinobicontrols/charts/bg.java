package com.shinobicontrols.charts;

import android.graphics.Rect;
import android.view.ViewGroup;
import com.shinobicontrols.charts.Axis;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class bg {
    private static bg a = new bg() { // from class: com.shinobicontrols.charts.bg.1
        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, ViewGroup.MarginLayoutParams marginLayoutParams, int i2, int i3, float f2) {
            rect.offset(0, (int) (marginLayoutParams.bottomMargin + (i3 / 2) + (f2 / 2.0f)));
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3) {
            rect.bottom -= i3;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(Axis.Position position, int i2) {
            if (position == Axis.Position.NORMAL) {
                return i2;
            }
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return (int) Math.max(0.0f, Math.max(0.0f, ((i2 / 2) - (f2 / 2.0f)) - a(position, i3)) - marginLayoutParams.bottomMargin);
        }

        @Override // com.shinobicontrols.charts.bg
        int b(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3, int i4, int i5) {
            rect.left += i2;
            rect.top += i3;
            rect.right -= i4;
            rect.bottom -= i5;
        }
    };
    private static bg b = new bg() { // from class: com.shinobicontrols.charts.bg.4
        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3) {
            rect.bottom -= i3;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return i2;
        }

        @Override // com.shinobicontrols.charts.bg
        int b(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3, int i4, int i5) {
            rect.left += i2;
            rect.right -= i4;
        }
    };
    private static bg c = new bg() { // from class: com.shinobicontrols.charts.bg.5
        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3) {
        }

        @Override // com.shinobicontrols.charts.bg
        int a(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        int b(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3, int i4, int i5) {
            rect.left += i2;
            rect.top += i3;
            rect.right -= i4;
            rect.bottom -= i5;
        }
    };
    private static bg d = new bg() { // from class: com.shinobicontrols.charts.bg.6
        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, ViewGroup.MarginLayoutParams marginLayoutParams, int i2, int i3, float f2) {
            rect.offset(-((int) (marginLayoutParams.leftMargin + (i2 / 2) + (f2 / 2.0f))), 0);
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3) {
            rect.left += i2;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(Axis.Position position, int i2) {
            if (position == Axis.Position.NORMAL) {
                return i2;
            }
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        int b(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return (int) Math.max(0.0f, Math.max(0.0f, ((i2 / 2) - (f2 / 2.0f)) - a(position, i3)) - marginLayoutParams.leftMargin);
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3, int i4, int i5) {
            rect.left += i2;
            rect.top += i3;
            rect.right -= i4;
            rect.bottom -= i5;
        }
    };
    private static bg e = new bg() { // from class: com.shinobicontrols.charts.bg.7
        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3) {
            rect.left += i2;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        int b(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return i2;
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3, int i4, int i5) {
            rect.top += i3;
            rect.bottom -= i5;
        }
    };
    private static bg f = new bg() { // from class: com.shinobicontrols.charts.bg.8
        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3) {
            rect.setEmpty();
        }

        @Override // com.shinobicontrols.charts.bg
        int a(Axis.Position position, int i2) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        int b(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3, int i4, int i5) {
        }
    };
    private static bg g = new bg() { // from class: com.shinobicontrols.charts.bg.9
        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, ViewGroup.MarginLayoutParams marginLayoutParams, int i2, int i3, float f2) {
            rect.offset((int) (marginLayoutParams.rightMargin + (i2 / 2) + (f2 / 2.0f)), 0);
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3) {
            rect.right -= i2;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(Axis.Position position, int i2) {
            if (position == Axis.Position.REVERSE) {
                return i2;
            }
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        int b(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return (int) Math.max(0.0f, Math.max(0.0f, ((i2 / 2) - (f2 / 2.0f)) - a(position, i3)) - marginLayoutParams.rightMargin);
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3, int i4, int i5) {
            rect.left += i2;
            rect.top += i3;
            rect.right -= i4;
            rect.bottom -= i5;
        }
    };
    private static bg h = new bg() { // from class: com.shinobicontrols.charts.bg.10
        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3) {
            rect.right -= i2;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        int b(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return i2;
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3, int i4, int i5) {
            rect.top += i3;
            rect.bottom -= i5;
        }
    };
    private static bg i = new bg() { // from class: com.shinobicontrols.charts.bg.11
        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, ViewGroup.MarginLayoutParams marginLayoutParams, int i2, int i3, float f2) {
            rect.offset(0, -((int) (marginLayoutParams.topMargin + (i3 / 2) + (f2 / 2.0f))));
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3) {
            rect.top += i3;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(Axis.Position position, int i2) {
            if (position == Axis.Position.REVERSE) {
                return i2;
            }
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return (int) Math.max(0.0f, Math.max(0.0f, ((i2 / 2) - (f2 / 2.0f)) - a(position, i3)) - marginLayoutParams.topMargin);
        }

        @Override // com.shinobicontrols.charts.bg
        int b(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3, int i4, int i5) {
            rect.left += i2;
            rect.top += i3;
            rect.right -= i4;
            rect.bottom -= i5;
        }
    };
    private static bg j = new bg() { // from class: com.shinobicontrols.charts.bg.2
        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3) {
            rect.top += i3;
        }

        @Override // com.shinobicontrols.charts.bg
        int a(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return i2;
        }

        @Override // com.shinobicontrols.charts.bg
        int b(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams) {
            return 0;
        }

        @Override // com.shinobicontrols.charts.bg
        void a(Rect rect, int i2, int i3, int i4, int i5) {
            rect.left += i2;
            rect.right -= i4;
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int a(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a(Rect rect, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a(Rect rect, int i2, int i3, int i4, int i5);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int b(int i2, float f2, Axis.Position position, int i3, ViewGroup.MarginLayoutParams marginLayoutParams);

    bg() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static bg a(Legend legend) {
        if (legend == null || legend.getPosition() == null || legend.getPlacement() == null) {
            return f;
        }
        switch (legend.getPosition()) {
            case TOP_LEFT:
            case MIDDLE_LEFT:
            case BOTTOM_LEFT:
                switch (legend.getPlacement()) {
                    case OUTSIDE_PLOT_AREA:
                        return e;
                    case ON_PLOT_AREA_BORDER:
                        return d;
                    case INSIDE_PLOT_AREA:
                        return c;
                    default:
                        throw new AssertionError("Legend Placement invalid:" + legend.getPlacement());
                }
            case TOP_RIGHT:
            case MIDDLE_RIGHT:
            case BOTTOM_RIGHT:
                switch (legend.getPlacement()) {
                    case OUTSIDE_PLOT_AREA:
                        return h;
                    case ON_PLOT_AREA_BORDER:
                        return g;
                    case INSIDE_PLOT_AREA:
                        return c;
                    default:
                        throw new AssertionError("Legend Placement invalid:" + legend.getPlacement());
                }
            case TOP_CENTER:
                switch (legend.getPlacement()) {
                    case OUTSIDE_PLOT_AREA:
                        return j;
                    case ON_PLOT_AREA_BORDER:
                        return i;
                    case INSIDE_PLOT_AREA:
                        return c;
                    default:
                        throw new AssertionError("Legend Placement invalid:" + legend.getPlacement());
                }
            case BOTTOM_CENTER:
                switch (legend.getPlacement()) {
                    case OUTSIDE_PLOT_AREA:
                        return b;
                    case ON_PLOT_AREA_BORDER:
                        return a;
                    case INSIDE_PLOT_AREA:
                        return c;
                    default:
                        throw new AssertionError("Legend Placement invalid:" + legend.getPlacement());
                }
            default:
                throw new AssertionError("Legend Position invalid:" + legend.getPosition());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Rect rect, ViewGroup.MarginLayoutParams marginLayoutParams, int i2, int i3, float f2) {
    }

    int a(Axis.Position position, int i2) {
        return 0;
    }
}
