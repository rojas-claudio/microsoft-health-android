package com.shinobicontrols.charts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import com.shinobicontrols.charts.Axis;
/* loaded from: classes.dex */
public final class ChartUtils {
    private final ar a = new ar();
    private final PointF b = new PointF();
    private final Rect c = new Rect();

    public static void drawText(Canvas canvas, String labelText, int x, int y, Paint labelPaint) {
        int descent = (int) ((labelPaint.descent() + labelPaint.ascent()) / 2.0f);
        if (labelText != null) {
            String[] split = labelText.split("\n");
            if (split.length > 1) {
                y = (int) (y - ((labelPaint.getFontSpacing() / 2.0f) * (split.length - 1)));
            }
            for (String str : split) {
                canvas.drawText(str, x, y - descent, labelPaint);
                y = (int) (y + labelPaint.getFontSpacing());
            }
        }
    }

    public static void drawTextBackground(Canvas canvas, Rect backgroundLabelRect, Paint backgroundLabelPaint) {
        canvas.drawRect(backgroundLabelRect, backgroundLabelPaint);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect a(int i, int i2, String str, float f, Typeface typeface, v vVar) {
        if (str == null) {
            this.c.set(0, 0, 0, 0);
            return this.c;
        }
        this.a.a(this.b, str, f, typeface, vVar);
        this.c.set(0, 0, (int) (this.b.x + 5.0f), (int) this.b.y);
        this.c.offset(i - (this.c.width() / 2), i2 - (this.c.height() / 2));
        String[] split = str.split("\n");
        if (split.length > 1) {
            int height = this.c.height() / 2;
            for (int i3 = 1; i3 < split.length; i3++) {
                this.c.top -= height;
                this.c.bottom += height;
            }
        }
        return this.c;
    }

    public static void drawTickMarkLine(Canvas canvas, TickMark tickMark) {
        canvas.drawRect(tickMark.g, tickMark.getLinePaint());
    }

    public static void updateTooltipContent(Tooltip tooltip, DataPoint<?, ?> dataPoint) {
        if (tooltip == null) {
            throw new IllegalArgumentException("Cannot update null tooltip.");
        }
        if (dataPoint == null) {
            throw new IllegalArgumentException(tooltip.getContext().getString(R.string.TooltipNullView));
        }
        tooltip.b.a(tooltip, dataPoint);
    }

    public static void drawCrosshair(ShinobiChart chart, Canvas canvas, Rect drawingBoundary, float pixelXValue, float pixelYValue, float targetCircleRadius, Paint paint) {
        a(canvas, pixelXValue, pixelYValue, targetCircleRadius, paint);
        if (chart.getCrosshair().d()) {
            a(chart, canvas, drawingBoundary, pixelXValue, pixelYValue, targetCircleRadius, paint);
            b(chart, canvas, drawingBoundary, pixelXValue, pixelYValue, targetCircleRadius, paint);
        }
    }

    private static void a(Canvas canvas, float f, float f2, float f3, Paint paint) {
        canvas.drawCircle(f, f2, f3, paint);
    }

    private static void a(ShinobiChart shinobiChart, Canvas canvas, Rect rect, float f, float f2, float f3, Paint paint) {
        Crosshair crosshair = shinobiChart.getCrosshair();
        boolean z = (crosshair.f != null ? crosshair.f.getYAxis() : shinobiChart.getYAxis()).d == Axis.Position.NORMAL;
        canvas.drawLine(f + ((z ? -1.0f : 1.0f) * f3), f2, z ? rect.left : rect.right, f2, paint);
    }

    private static void b(ShinobiChart shinobiChart, Canvas canvas, Rect rect, float f, float f2, float f3, Paint paint) {
        Crosshair crosshair = shinobiChart.getCrosshair();
        boolean z = (crosshair.f != null ? crosshair.f.getXAxis() : shinobiChart.getXAxis()).d == Axis.Position.NORMAL;
        canvas.drawLine(f, f2 + ((z ? 1.0f : -1.0f) * f3), f, z ? rect.bottom : rect.top, paint);
    }
}
