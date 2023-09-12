package com.shinobicontrols.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
/* loaded from: classes.dex */
public class Title extends TextView {
    private final boolean a;
    private Orientation b;
    private final float c;

    /* loaded from: classes.dex */
    public enum CentersOn {
        CANVAS,
        CHART,
        PLOTTING_AREA
    }

    /* loaded from: classes.dex */
    public enum Position {
        BOTTOM_OR_LEFT(0, 3, 80),
        CENTER(1, 1, 16),
        TOP_OR_RIGHT(2, 5, 48);
        
        private final int a;
        private final int b;
        private final int c;

        Position(int xmlValue, int horizontalGravity, int verticalGravity) {
            this.a = xmlValue;
            this.b = horizontalGravity;
            this.c = verticalGravity;
        }

        public int getXmlValue() {
            return this.a;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int a() {
            return this.b;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int b() {
            return this.c;
        }
    }

    /* loaded from: classes.dex */
    public enum Orientation {
        HORIZONTAL(0),
        VERTICAL(1);
        
        private final int a;

        Orientation(int xmlValue) {
            this.a = xmlValue;
        }

        public int getXmlValue() {
            return this.a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Title(Context context) {
        super(context);
        this.b = Orientation.HORIZONTAL;
        this.c = getResources().getDisplayMetrics().density;
        int gravity = getGravity();
        if (Gravity.isVertical(gravity) && (gravity & 112) == 80) {
            setGravity((gravity & 7) | 48);
            this.a = false;
            return;
        }
        this.a = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Orientation orientation) {
        this.b = orientation;
        invalidate();
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(TitleStyle titleStyle) {
        setTextSize(2, titleStyle.e.a.floatValue());
        setTypeface(titleStyle.d.a);
        setTextColor(titleStyle.h.a.intValue());
        if (titleStyle.getBackgroundColor() == 0) {
            a.a(this, (Drawable) null);
        } else {
            setBackgroundColor(titleStyle.c.a.intValue());
        }
        int a = at.a(this.c, titleStyle.i.a.floatValue());
        setPadding(a, a, a, a);
        int a2 = at.a(this.c, titleStyle.j.a.floatValue());
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginLayoutParams.topMargin = a2;
        marginLayoutParams.bottomMargin = a2;
        marginLayoutParams.leftMargin = a2;
        marginLayoutParams.rightMargin = a2;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.b == Orientation.HORIZONTAL) {
            super.onDraw(canvas);
            return;
        }
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        canvas.save();
        if (!this.a) {
            canvas.translate(getWidth(), 0.0f);
            canvas.rotate(90.0f);
        } else {
            canvas.translate(0.0f, getHeight());
            canvas.rotate(-90.0f);
        }
        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
        getLayout().draw(canvas);
        canvas.restore();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.b == Orientation.HORIZONTAL) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
            return;
        }
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }
}
