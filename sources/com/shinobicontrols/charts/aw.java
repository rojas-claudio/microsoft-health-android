package com.shinobicontrols.charts;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shinobicontrols.charts.Legend;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class aw extends LinearLayout {
    private final LinearLayout a;
    private final LinearLayout b;
    private final float c;

    public aw(Context context) {
        super(context);
        this.c = getResources().getDisplayMetrics().density;
        setOrientation(0);
        this.a = new LinearLayout(context);
        this.a.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.a.setOrientation(1);
        this.b = new LinearLayout(context);
        this.b.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.b.setOrientation(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(av avVar) {
        ay b = avVar.b();
        b.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        this.a.addView(b);
        TextView a = avVar.a();
        a.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.b.addView(a);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i = 0;
        this.b.measure(widthMeasureSpec, heightMeasureSpec);
        if (this.b.getChildAt(0) != null) {
            Paint.FontMetrics fontMetrics = ((TextView) this.b.getChildAt(0)).getPaint().getFontMetrics();
            int ceil = ((int) Math.ceil(fontMetrics.bottom - fontMetrics.top)) + ((TextView) this.b.getChildAt(0)).getPaddingTop() + ((TextView) this.b.getChildAt(0)).getPaddingBottom();
            if (this.a.getVisibility() == 0) {
                for (int i2 = 0; i2 < this.a.getChildCount(); i2++) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.a.getChildAt(i2).getLayoutParams();
                    marginLayoutParams.height = ceil;
                    ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.b.getChildAt(i2).getLayoutParams();
                    double measuredHeight = this.b.getChildAt(i2).getMeasuredHeight();
                    int floor = ((int) Math.floor((measuredHeight - ceil) / 2.0d)) + marginLayoutParams2.topMargin;
                    int ceil2 = marginLayoutParams2.bottomMargin + ((int) Math.ceil((measuredHeight - ceil) / 2.0d));
                    if (floor < 0) {
                        floor = 0;
                    }
                    if (ceil2 < 0) {
                        ceil2 = 0;
                    }
                    marginLayoutParams.topMargin = floor;
                    marginLayoutParams.bottomMargin = ceil2;
                }
                this.a.measure(widthMeasureSpec, heightMeasureSpec);
                i = at.b(this.a);
            }
            setMeasuredDimension(View.resolveSize(at.b(this.b) + i, widthMeasureSpec), View.resolveSize(at.a(this.b), heightMeasureSpec));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(LegendStyle legendStyle) {
        a(legendStyle.areSymbolsShown());
        int a = at.a(this.c, legendStyle.getRowVerticalMargin() / 2.0f);
        a(legendStyle.getSymbolAlignment(), legendStyle.getSymbolLabelGap());
        int a2 = at.a(this.c, legendStyle.getSymbolWidth());
        int childCount = this.a.getChildCount();
        int i = 0;
        while (i < childCount) {
            View childAt = this.a.getChildAt(i);
            a(childAt, legendStyle.getSymbolCornerRadius());
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
            layoutParams.width = a2;
            layoutParams.topMargin = i > 0 ? a : 0;
            layoutParams.bottomMargin = i < childCount + (-1) ? a : 0;
            i++;
        }
        int childCount2 = this.b.getChildCount();
        int i2 = 0;
        while (i2 < childCount2) {
            View childAt2 = this.b.getChildAt(i2);
            a(childAt2, legendStyle.getTypeface(), legendStyle.getTextSize(), legendStyle.getTextColor(), legendStyle.getTextAlignment());
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) childAt2.getLayoutParams();
            layoutParams2.gravity = legendStyle.getTextAlignment();
            layoutParams2.topMargin = i2 > 0 ? a : 0;
            layoutParams2.bottomMargin = i2 < childCount2 + (-1) ? a : 0;
            i2++;
        }
        a(legendStyle.getSymbolAlignment());
    }

    private void a(boolean z) {
        this.a.setVisibility(z ? 0 : 8);
    }

    private void a(Legend.SymbolAlignment symbolAlignment, float f) {
        int a = at.a(this.c, f / 2.0f);
        if (symbolAlignment == Legend.SymbolAlignment.LEFT) {
            ((LinearLayout.LayoutParams) this.a.getLayoutParams()).setMargins(0, 0, a, 0);
            ((LinearLayout.LayoutParams) this.b.getLayoutParams()).setMargins(a, 0, 0, 0);
            return;
        }
        ((LinearLayout.LayoutParams) this.b.getLayoutParams()).setMargins(0, 0, a, 0);
        ((LinearLayout.LayoutParams) this.a.getLayoutParams()).setMargins(a, 0, 0, 0);
    }

    private void a(View view, Typeface typeface, float f, int i, int i2) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setTypeface(typeface);
            textView.setTextSize(2, f);
            textView.setTextColor(i);
            textView.setGravity(i2);
        }
    }

    private void a(View view, float f) {
        if (view instanceof ay) {
            Drawable a = ((ay) view).a();
            if (a instanceof GradientDrawable) {
                ((GradientDrawable) a).setCornerRadius(f);
            }
        }
    }

    private void a(Legend.SymbolAlignment symbolAlignment) {
        if (this.a.getVisibility() != 0) {
            addView(this.b);
        } else if (symbolAlignment == Legend.SymbolAlignment.LEFT) {
            addView(this.a);
            addView(this.b);
        } else {
            addView(this.b);
            addView(this.a);
        }
    }
}
