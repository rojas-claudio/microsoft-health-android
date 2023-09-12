package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;
import com.microsoft.kapp.R;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public class PauseLinesView extends View {
    private Rect chartCanvasRect;
    private Rect chartPlotAreaRect;
    private final Paint linePaint;
    private ArrayList<Line> lines;
    private final int padding;
    private final String text;
    private final Paint textPaint;

    public PauseLinesView(Context context, ChartThemeCache themeCache) {
        super(context);
        this.linePaint = new Paint();
        this.textPaint = new Paint();
        this.lines = new ArrayList<>();
        setLayerType(1, null);
        this.text = getResources().getString(R.string.glyph_pause_flag);
        this.padding = getResources().getDimensionPixelSize(R.dimen.shinobicharts_pause_flag_top_padding);
        setupLinePaint(themeCache);
        setupTextPaint(themeCache);
    }

    private void setupLinePaint(ChartThemeCache themeCache) {
        this.linePaint.setStyle(Paint.Style.FILL);
        this.linePaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.shinobicharts_average_line_stroke));
        this.linePaint.setColor(themeCache.getAveragePaceLineColor());
        this.linePaint.setColor(themeCache.getBelowAveragePaceColor());
    }

    private void setupTextPaint(ChartThemeCache themeCache) {
        this.textPaint.setTextAlign(Paint.Align.RIGHT);
        this.textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.shinobicharts_average_pause_flag_text_size));
        this.textPaint.setColor(themeCache.getBelowAveragePaceColor());
        String typefaceAssetName = themeCache.getPathToGlyphTypeface();
        this.textPaint.setTypeface(Typeface.createFromAsset(getResources().getAssets(), typefaceAssetName));
        this.textPaint.setAntiAlias(true);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        drawLines(canvas);
    }

    private void drawLines(Canvas canvas) {
        Iterator i$ = this.lines.iterator();
        while (i$.hasNext()) {
            Line line = i$.next();
            float startX = line.xPixelValue1;
            float startY = line.yPixelValue1;
            float stopX = line.xPixelValue2;
            float stopY = line.yPixelValue2;
            canvas.drawLine(startX, startY, stopX, stopY, this.linePaint);
            canvas.drawText(this.text, startX, startY, this.textPaint);
        }
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }

    public void clearLines() {
        this.lines.clear();
    }

    public void setChartCanvasRect(Rect chartCanvasRect) {
        this.chartCanvasRect = chartCanvasRect;
    }

    public void setChartPlotAreaRect(Rect chartPlotAreaRect) {
        this.chartPlotAreaRect = chartPlotAreaRect;
    }
}
