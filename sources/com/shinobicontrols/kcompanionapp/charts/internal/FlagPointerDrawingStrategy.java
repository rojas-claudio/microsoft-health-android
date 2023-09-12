package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public abstract class FlagPointerDrawingStrategy {
    public static FlagPointerDrawingStrategy NULL = new FlagPointerDrawingStrategy() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.FlagPointerDrawingStrategy.1
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPointerDrawingStrategy
        public void drawPointer(Canvas canvas, Paint pointerPaint, int flagTextViewWidth, int flagTextViewHeight, RectF placementRect, Resources resources) {
        }
    };
    public static FlagPointerDrawingStrategy TOP_LEFT_QUADRANT_FLAG_POINTER_DRAWING_STRATEGY = new FlagPointerDrawingStrategy() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.FlagPointerDrawingStrategy.2
        private final Path path = new Path();

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPointerDrawingStrategy
        public void drawPointer(Canvas canvas, Paint pointerPaint, int flagTextViewWidth, int flagTextViewHeight, RectF placementRect, Resources resources) {
            this.path.reset();
            int triangleHeight = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
            int triangleWidth = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_width);
            float x = canvas.getWidth() / 2.0f;
            float y = canvas.getHeight() / 2.0f;
            this.path.moveTo(x, y);
            float y2 = y - (triangleHeight + (flagTextViewHeight / 2.0f));
            this.path.lineTo(x, y2);
            this.path.lineTo(x - triangleWidth, y2);
            canvas.drawPath(this.path, pointerPaint);
        }
    };
    public static FlagPointerDrawingStrategy TOP_RIGHT_QUADRANT_FLAG_POINTER_DRAWING_STRATEGY = new FlagPointerDrawingStrategy() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.FlagPointerDrawingStrategy.3
        private final Path path = new Path();

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPointerDrawingStrategy
        public void drawPointer(Canvas canvas, Paint pointerPaint, int flagTextViewWidth, int flagTextViewHeight, RectF placementRect, Resources resources) {
            this.path.reset();
            int triangleHeight = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
            int triangleWidth = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_width);
            float x = canvas.getWidth() / 2.0f;
            float y = canvas.getHeight() / 2.0f;
            this.path.moveTo(x, y);
            float y2 = y - (triangleHeight + (flagTextViewHeight / 2.0f));
            this.path.lineTo(x, y2);
            this.path.lineTo(x + triangleWidth, y2);
            canvas.drawPath(this.path, pointerPaint);
        }
    };
    public static FlagPointerDrawingStrategy BOTTOM_LEFT_QUADRANT_FLAG_POINTER_DRAWING_STRATEGY = new FlagPointerDrawingStrategy() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.FlagPointerDrawingStrategy.4
        private final Path path = new Path();

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPointerDrawingStrategy
        public void drawPointer(Canvas canvas, Paint pointerPaint, int flagTextViewWidth, int flagTextViewHeight, RectF placementRect, Resources resources) {
            this.path.reset();
            int triangleHeight = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
            int triangleWidth = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_width);
            float x = canvas.getWidth() / 2.0f;
            float y = canvas.getHeight() / 2.0f;
            this.path.moveTo(x, y);
            float y2 = y + triangleHeight + (flagTextViewHeight / 2.0f);
            this.path.lineTo(x, y2);
            this.path.lineTo(x - triangleWidth, y2);
            canvas.drawPath(this.path, pointerPaint);
        }
    };
    public static FlagPointerDrawingStrategy BOTTOM_RIGHT_QUADRANT_FLAG_POINTER_DRAWING_STRATEGY = new FlagPointerDrawingStrategy() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.FlagPointerDrawingStrategy.5
        private final Path path = new Path();

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPointerDrawingStrategy
        public void drawPointer(Canvas canvas, Paint pointerPaint, int flagTextViewWidth, int flagTextViewHeight, RectF placementRect, Resources resources) {
            this.path.reset();
            int triangleHeight = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
            int triangleWidth = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_width);
            float x = canvas.getWidth() / 2.0f;
            float y = canvas.getHeight() / 2.0f;
            this.path.moveTo(x, y);
            float y2 = y + triangleHeight + (flagTextViewHeight / 2.0f);
            this.path.lineTo(x, y2);
            this.path.lineTo(x + triangleWidth, y2);
            canvas.drawPath(this.path, pointerPaint);
        }
    };

    public abstract void drawPointer(Canvas canvas, Paint paint, int i, int i2, RectF rectF, Resources resources);

    /* loaded from: classes.dex */
    public static class XPositionWithinRangeFlagPointerDrawingStrategy extends FlagPointerDrawingStrategy {
        private final Path path = new Path();
        private final float xMax;
        private final float xMin;
        private final float xPosition;

        public XPositionWithinRangeFlagPointerDrawingStrategy(float xPosition, float xMin, float xMax) {
            this.xPosition = xPosition;
            this.xMin = xMin;
            this.xMax = xMax;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPointerDrawingStrategy
        public void drawPointer(Canvas canvas, Paint pointerPaint, int flagTextViewWidth, int flagTextViewHeight, RectF placementRect, Resources resources) {
            int triangleHeight = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
            int triangleWidth = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_width);
            float x = this.xPosition;
            float y = placementRect.bottom;
            this.path.moveTo(x, y);
            float x2 = Math.max(x - (triangleWidth / 2.0f), this.xMin);
            float y2 = y - (triangleHeight + (flagTextViewHeight / 2.0f));
            this.path.lineTo(x2, y2);
            this.path.lineTo(Math.min(x2 + triangleWidth, this.xMax), y2);
            canvas.drawPath(this.path, pointerPaint);
            canvas.drawLine(this.xPosition, placementRect.bottom, this.xPosition, canvas.getHeight(), pointerPaint);
        }
    }
}
