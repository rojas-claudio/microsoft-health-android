package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
/* loaded from: classes.dex */
public class NightTimeBackgroundView extends View {
    private float baselinePixelYValue;
    private final Paint paint;

    public NightTimeBackgroundView(Context context, ChartThemeCache themeCache) {
        super(context);
        this.paint = new Paint();
        this.paint.setColor(themeCache.getSleepNightTimeBackgroundColor());
    }

    public void setBaselinePixelYValue(float baselinePixelYValue) {
        this.baselinePixelYValue = baselinePixelYValue;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0.0f, this.baselinePixelYValue, canvas.getWidth(), canvas.getHeight(), this.paint);
    }
}
