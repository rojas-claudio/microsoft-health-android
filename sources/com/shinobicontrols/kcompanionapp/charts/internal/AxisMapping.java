package com.shinobicontrols.kcompanionapp.charts.internal;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.Series;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class AxisMapping {
    private final Axis<?, ?> axis;
    private int axisWidth;
    private final View icon;
    private Drawable iconLegend;
    private final List<Series<?>> seriesList;
    private final CharSequence text;
    private final List<View> viewsToShowWhenSelected;

    public AxisMapping(View icon, List<Series<?>> seriesList, Axis<?, ?> axis) {
        this(icon, seriesList, axis, new ArrayList());
    }

    public AxisMapping(View icon, List<Series<?>> seriesList, Axis<?, ?> axis, List<View> viewsToShowWhenSelected) {
        this(icon, "", seriesList, axis, viewsToShowWhenSelected);
    }

    public AxisMapping(View icon, CharSequence text, List<Series<?>> seriesList, Axis<?, ?> axis, List<View> viewsToShowWhenSelected) {
        this(icon, text, seriesList, axis, viewsToShowWhenSelected, null);
    }

    public AxisMapping(View icon, CharSequence text, List<Series<?>> seriesList, Axis<?, ?> axis, List<View> viewsToShowWhenSelected, Drawable iconLegend) {
        this.seriesList = new ArrayList();
        this.viewsToShowWhenSelected = new ArrayList();
        this.icon = icon;
        this.axis = axis;
        this.seriesList.addAll(seriesList);
        this.viewsToShowWhenSelected.addAll(viewsToShowWhenSelected);
        this.text = text;
        this.axisWidth = 0;
        this.iconLegend = iconLegend;
    }

    public View getIcon() {
        return this.icon;
    }

    public List<Series<?>> getSeriesList() {
        return this.seriesList;
    }

    public Axis<?, ?> getAxis() {
        return this.axis;
    }

    public List<View> getViewToShowWhenSelected() {
        return this.viewsToShowWhenSelected;
    }

    public void setAxisWidth(int width) {
        this.axisWidth = width;
    }

    public int getAxisWidth() {
        return this.axisWidth;
    }

    public CharSequence getText() {
        return this.text;
    }

    public Drawable getIconLegend() {
        return this.iconLegend;
    }
}
