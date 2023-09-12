package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
/* loaded from: classes.dex */
public class ValueHighlightView extends View {
    private float mHighYValue;
    private float mLowYValue;
    private final Paint mPaint;

    public ValueHighlightView(Context context, ChartThemeCache themeCache) {
        super(context);
        this.mPaint = new Paint();
        this.mPaint.setColor(-1);
        this.mPaint.setAlpha(128);
    }

    public void setPixelYValues(float high, float low) {
        this.mHighYValue = high;
        this.mLowYValue = low;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0.0f, this.mHighYValue, canvas.getWidth(), this.mLowYValue, this.mPaint);
    }
}
