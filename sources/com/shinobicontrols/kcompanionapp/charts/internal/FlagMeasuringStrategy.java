package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.res.Resources;
import android.graphics.Point;
import android.view.View;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public abstract class FlagMeasuringStrategy {
    public static FlagMeasuringStrategy FOUR_TIMES_FLAG_TEXT_PLUS_POINTER_OFFSET = new FlagMeasuringStrategy() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.FlagMeasuringStrategy.1
        private final Point measurements = new Point();

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagMeasuringStrategy
        public Point getMeasurements(int widthMeasureSpec, int heightMeasureSpec, int flagTextWidth, int flagTextHeight, Resources resources) {
            int triangleOffset = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
            this.measurements.x = flagTextWidth * 2;
            this.measurements.y = (flagTextHeight + triangleOffset) * 2;
            return this.measurements;
        }
    };
    public static FlagMeasuringStrategy MATCH_PARENT = new FlagMeasuringStrategy() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.FlagMeasuringStrategy.2
        private final Point measurements = new Point();

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagMeasuringStrategy
        public Point getMeasurements(int widthMeasureSpec, int heightMeasureSpec, int flagTextWidth, int flagTextHeight, Resources resources) {
            this.measurements.x = View.MeasureSpec.getSize(widthMeasureSpec);
            this.measurements.y = View.MeasureSpec.getSize(heightMeasureSpec);
            return this.measurements;
        }
    };

    public abstract Point getMeasurements(int i, int i2, int i3, int i4, Resources resources);
}
