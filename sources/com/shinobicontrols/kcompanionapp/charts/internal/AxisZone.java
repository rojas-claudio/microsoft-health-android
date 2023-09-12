package com.shinobicontrols.kcompanionapp.charts.internal;
/* loaded from: classes.dex */
public class AxisZone {
    private final boolean showValueOnNewLine;
    private final String title;
    private final double value;
    private final String valuePostFix;
    private final String valueText;

    public AxisZone(double value, String title, boolean showValueOnNewLine) {
        this(value, title, showValueOnNewLine, "");
    }

    public AxisZone(double value, String title, boolean showValueOnNewLine, String valuePostFix) {
        this(value, title, showValueOnNewLine, valuePostFix, String.valueOf((int) value));
    }

    public AxisZone(double value, String title, boolean showValueOnNewLine, String valuePostFix, String valueText) {
        this.value = value;
        this.valueText = valueText;
        this.title = title;
        this.showValueOnNewLine = showValueOnNewLine;
        this.valuePostFix = valuePostFix;
    }

    public double getValue() {
        return this.value;
    }

    public String getValueText() {
        return this.valueText;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isShowValueOnNewLine() {
        return this.showValueOnNewLine;
    }

    public String getValuePostFix() {
        return this.valuePostFix;
    }
}
