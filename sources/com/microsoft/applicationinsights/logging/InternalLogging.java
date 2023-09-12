package com.microsoft.applicationinsights.logging;

import android.util.Log;
import com.microsoft.applicationinsights.library.ApplicationInsights;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public class InternalLogging {
    private static final String PREFIX = InternalLogging.class.getPackage().getName();

    private InternalLogging() {
    }

    public static void info(String tag, String message, String payload) {
        if (ApplicationInsights.isDeveloperMode()) {
            Log.i(PREFIX + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + tag, message + TreeNode.NODES_ID_SEPARATOR + payload);
        }
    }

    public static void warn(String tag, String message) {
        if (ApplicationInsights.isDeveloperMode()) {
            Log.w(PREFIX + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + tag, message);
        }
    }

    public static void error(String tag, String message) {
        Log.e(PREFIX + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + tag, message);
    }
}
