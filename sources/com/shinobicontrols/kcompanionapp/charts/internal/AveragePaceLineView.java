package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class AveragePaceLineView extends View {
    private Rect chartCanvasRect;
    private Rect chartPlotAreaRect;
    private final Paint linePaint;
    private final int padding;
    private final String text;
    private final Paint textPaint;
    private float xPixelValue;
    private float yPixelValue;

    public AveragePaceLineView(Context context, ChartThemeCache themeCache, String annoationText) {
        super(context);
        this.linePaint = new Paint();
        this.textPaint = new Paint();
        setLayerType(1, null);
        this.text = annoationText;
        this.padding = getResources().getDimensionPixelSize(R.dimen.shinobicharts_average_pace_label_text_padding);
        setupLinePaint(themeCache);
        setupTextPaint(themeCache);
    }

    private void setupLinePaint(ChartThemeCache themeCache) {
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.linePaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.shinobicharts_average_line_stroke));
        this.linePaint.setColor(themeCache.getAveragePaceLineColor());
        float dashWidth = getResources().getDimensionPixelSize(R.dimen.shinobicharts_average_line_dash_width);
        float dashGap = getResources().getDimensionPixelSize(R.dimen.shinobicharts_average_line_dash_gap);
        this.linePaint.setPathEffect(new DashPathEffect(new float[]{dashWidth, dashGap}, 0.0f));
    }

    private void setupTextPaint(ChartThemeCache themeCache) {
        this.textPaint.setTextAlign(Paint.Align.RIGHT);
        this.textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.shinobicharts_average_pace_label_text_size));
        this.textPaint.setColor(themeCache.getAveragePaceLineColor());
        String typefaceAssetName = themeCache.getPathToAveragePaceLineLabelTypeface();
        this.textPaint.setTypeface(Typeface.createFromAsset(getResources().getAssets(), typefaceAssetName));
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        drawLine(canvas);
        drawText(canvas);
    }

    private void drawLine(Canvas canvas) {
        if (this.chartCanvasRect != null) {
            float startX = this.chartCanvasRect.left;
            float startY = this.yPixelValue;
            float stopX = this.chartPlotAreaRect.right;
            float stopY = this.yPixelValue;
            canvas.drawLine(startX, startY, stopX, stopY, this.linePaint);
        }
    }

    private void drawText(Canvas canvas) {
        canvas.drawText(this.text, this.xPixelValue - this.padding, this.yPixelValue - this.padding, this.textPaint);
    }

    public void setXPixelValue(float xPixelValue) {
        this.xPixelValue = xPixelValue;
    }

    public void setYPixelValue(float yPixelValue) {
        this.yPixelValue = yPixelValue;
    }

    public void setChartCanvasRect(Rect chartCanvasRect) {
        this.chartCanvasRect = chartCanvasRect;
    }

    public void setChartPlotAreaRect(Rect chartPlotAreaRect) {
        this.chartPlotAreaRect = chartPlotAreaRect;
    }
}
