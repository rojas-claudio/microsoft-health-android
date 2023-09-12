package com.shinobicontrols.kcompanionapp.providers;

import android.content.Context;
import com.microsoft.kapp.R;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartThemeCache;
import com.shinobicontrols.kcompanionapp.charts.internal.StyledNumberAxis;
import com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy;
import java.text.DecimalFormat;
/* loaded from: classes.dex */
public enum XAxisProvider implements IAxisProvider {
    GOLF { // from class: com.shinobicontrols.kcompanionapp.providers.XAxisProvider.1
        @Override // com.shinobicontrols.kcompanionapp.providers.IAxisProvider
        public Axis<Double, Double> getAxis(Context context, ChartThemeCache themeCache, boolean isMetric) {
            StyledNumberAxis axis = new StyledNumberAxis(context, themeCache);
            axis.setPosition(Axis.Position.REVERSE);
            axis.setTickMarkClippingModeLow(TickMark.ClippingMode.TICKS_AND_LABELS_PERSIST);
            axis.setTickMarkClippingModeHigh(TickMark.ClippingMode.TICKS_AND_LABELS_PERSIST);
            axis.getStyle().setInterSeriesPadding(0.05f);
            axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.XAxisLabelCenteredWithTickLineToLeftTickMarkDrawingStrategy());
            return axis;
        }
    },
    MEASURED { // from class: com.shinobicontrols.kcompanionapp.providers.XAxisProvider.2
        @Override // com.shinobicontrols.kcompanionapp.providers.IAxisProvider
        public Axis<Double, Double> getAxis(Context context, ChartThemeCache themeCache, boolean isMetric) {
            StyledNumberAxis axis = new StyledNumberAxis(context, themeCache);
            String unit = context.getString(isMetric ? R.string.shinobicharts_run_splits_distance_label_metric_format : R.string.shinobicharts_run_splits_distance_label_imperial_format);
            axis.setPosition(Axis.Position.REVERSE);
            axis.setTickMarkClippingModeLow(TickMark.ClippingMode.TICKS_PERSIST);
            axis.setLabelFormat(new DecimalFormat(unit));
            axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.XAxisLabelLeftAndTickAcrossCanvasTickMarkDrawingStrategy());
            return axis;
        }
    },
    EXERCISE { // from class: com.shinobicontrols.kcompanionapp.providers.XAxisProvider.3
        @Override // com.shinobicontrols.kcompanionapp.providers.IAxisProvider
        public Axis<Double, Double> getAxis(Context context, ChartThemeCache themeCache, boolean isMetric) {
            StyledNumberAxis axis = new StyledNumberAxis(context, themeCache);
            axis.setPosition(Axis.Position.REVERSE);
            axis.setTickMarkClippingModeLow(TickMark.ClippingMode.TICKS_PERSIST);
            axis.setLabelFormat(new DecimalFormat(context.getString(R.string.shinobicharts_workout_time_label_format)));
            axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.XAxisLabelLeftAndTickAcrossCanvasTickMarkDrawingStrategy());
            return axis;
        }
    }
}
