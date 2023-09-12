package com.microsoft.band.webtiles;
/* loaded from: classes.dex */
public enum PageLayoutStyle {
    SCROLLING_TEXT("MSBand_ScrollingText", "msband_scrolling_text", new int[]{1, 2}, new int[0]),
    NO_SCROLLING_TEXT("MSBand_NoScrollingText", "msband_no_scrolling_text", new int[]{1, 2, 3}, new int[0]),
    SINGLE_METRIC("MSBand_SingleMetric", "msband_single_metric", new int[]{1, 2}, new int[0]),
    SINGLE_METRIC_WITH_ICON("MSBand_SingleMetricWithIcon", "msband_single_metric_with_icon", new int[]{12, 21}, new int[]{11}),
    SINGLE_METRIC_WITH_SECONDARY("MSBand_SingleMetricWithSecondary", "msband_single_metric_with_secondary", new int[]{11, 12, 22}, new int[]{21}),
    METRICS_WITH_ICONS("MSBand_MetricsWithIcons", "msband_metrics_with_icons", new int[]{12, 22, 32}, new int[]{11, 21, 31});
    
    private final int[] mIconList;
    private final String mLBlobName;
    private final String mName;
    private final int[] mTextList;

    PageLayoutStyle(String name, String lBlobName, int[] textList, int[] iconList) {
        this.mName = name;
        this.mLBlobName = lBlobName;
        this.mTextList = textList;
        this.mIconList = iconList;
    }

    public String getName() {
        return this.mName;
    }

    public String getLBlobName() {
        return this.mLBlobName;
    }

    public static PageLayoutStyle lookup(String name) {
        PageLayoutStyle[] arr$ = values();
        for (PageLayoutStyle s : arr$) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    public boolean hasTextElementId(int id) {
        return findValueInArray(this.mTextList, id);
    }

    public boolean hasIconElementId(int id) {
        return findValueInArray(this.mIconList, id);
    }

    private boolean findValueInArray(int[] array, int targetValue) {
        for (int v : array) {
            if (v == targetValue) {
                return true;
            }
        }
        return false;
    }
}
