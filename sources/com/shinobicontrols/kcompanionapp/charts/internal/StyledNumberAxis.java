package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import com.microsoft.kapp.R;
import com.shinobicontrols.charts.NumberAxis;
import com.shinobicontrols.charts.TickStyle;
import com.shinobicontrols.kcompanionapp.charts.internal.TickLabelUpdateStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy;
/* loaded from: classes.dex */
public class StyledNumberAxis extends NumberAxis {
    private TickMarkDrawingStrategy tickMarkDrawingStrategy = new TickMarkDrawingStrategy.NullTickMarkDrawingStrategy();
    private TickLabelUpdateStrategy tickLabelUpdateStrategy = new TickLabelUpdateStrategy.NullTickLabelUpdateStrategy();

    public StyledNumberAxis(Context context, ChartThemeCache themeCache) {
        Resources resources = context.getResources();
        TickStyle tickStyle = getStyle().getTickStyle();
        tickStyle.setLabelColor(themeCache.getAxisTickLabelColor());
        float axisTextSize = resources.getDimension(R.dimen.shinobicharts_axis_text_size);
        tickStyle.setLabelTextSize(axisTextSize / resources.getDisplayMetrics().scaledDensity);
        float axisTickLineWidth = resources.getDimension(R.dimen.shinobicharts_axis_tick_line_width);
        tickStyle.setLineWidth(axisTickLineWidth / resources.getDisplayMetrics().density);
        tickStyle.setLineColor(themeCache.getAxisTickLineColor());
        String typefaceAssetName = themeCache.getPathToAxisTickLabelTypeface();
        tickStyle.setLabelTypeface(Typeface.createFromAsset(resources.getAssets(), typefaceAssetName));
        enableAnimation(false);
    }

    public TickMarkDrawingStrategy getTickMarkDrawingStrategy() {
        return this.tickMarkDrawingStrategy;
    }

    public void setTickMarkDrawingStrategy(TickMarkDrawingStrategy tickMarkDrawingStrategy) {
        this.tickMarkDrawingStrategy = tickMarkDrawingStrategy;
    }

    public TickLabelUpdateStrategy getTickLabelUpdateStrategy() {
        return this.tickLabelUpdateStrategy;
    }

    public void setTickLabelUpdateStrategy(TickLabelUpdateStrategy tickLabelUpdateStrategy) {
        this.tickLabelUpdateStrategy = tickLabelUpdateStrategy;
    }
}
