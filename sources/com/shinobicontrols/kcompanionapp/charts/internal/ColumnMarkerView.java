package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class ColumnMarkerView extends View {
    private float columnWidth;
    private final Paint paint;
    private final Path path;
    private float xDataValueInPixels;
    private float yDataValueInPixels;

    public ColumnMarkerView(Context context, int color) {
        super(context);
        this.path = new Path();
        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(color);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float halfColumnWidth = this.columnWidth / 2.0f;
        float markerHeight = getResources().getDimensionPixelSize(R.dimen.shinobicharts_beyond_max_range_marker_height);
        this.path.moveTo(this.xDataValueInPixels, this.yDataValueInPixels);
        this.path.lineTo(this.xDataValueInPixels - halfColumnWidth, this.yDataValueInPixels);
        this.path.lineTo(this.xDataValueInPixels - halfColumnWidth, this.yDataValueInPixels + markerHeight);
        this.path.lineTo(this.xDataValueInPixels, this.yDataValueInPixels);
        this.path.lineTo(this.xDataValueInPixels + halfColumnWidth, this.yDataValueInPixels);
        this.path.lineTo(this.xDataValueInPixels + halfColumnWidth, this.yDataValueInPixels + markerHeight);
        canvas.drawPath(this.path, this.paint);
    }

    public float getColumnWidth() {
        return this.columnWidth;
    }

    public void setColumnWidth(float columnWidth) {
        this.columnWidth = columnWidth;
    }

    public float getXDataValueInPixels() {
        return this.xDataValueInPixels;
    }

    public void setXDataValueInPixels(float xDataValueInPixels) {
        this.xDataValueInPixels = xDataValueInPixels;
    }

    public float getYDataValueInPixels() {
        return this.yDataValueInPixels;
    }

    public void setYDataValueInPixels(float yDataValueInPixels) {
        this.yDataValueInPixels = yDataValueInPixels;
    }
}
