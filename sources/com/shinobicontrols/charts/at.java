package com.shinobicontrols.charts;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: classes.dex */
class at {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(View view) {
        if (view == null || view.getVisibility() == 8) {
            return 0;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return marginLayoutParams.bottomMargin + view.getMeasuredHeight() + marginLayoutParams.topMargin;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b(View view) {
        if (view == null || view.getVisibility() == 8) {
            return 0;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return marginLayoutParams.rightMargin + view.getMeasuredWidth() + marginLayoutParams.leftMargin;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(View view, Rect rect) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        rect.left += marginLayoutParams.leftMargin;
        rect.top += marginLayoutParams.topMargin;
        rect.right -= marginLayoutParams.rightMargin;
        rect.bottom -= marginLayoutParams.bottomMargin;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(View view, Rect rect) {
        view.layout(rect.left, rect.top, rect.right, rect.bottom);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(View view, ci ciVar) {
        view.layout((int) Math.round(ciVar.a), (int) Math.round(ciVar.b), (int) Math.round(ciVar.c), (int) Math.round(ciVar.d));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(float f, int i, float f2) {
        return (int) (i + (f2 * f) + 0.51f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(float f, float f2) {
        return (int) ((f2 * f) + 0.51f);
    }
}
