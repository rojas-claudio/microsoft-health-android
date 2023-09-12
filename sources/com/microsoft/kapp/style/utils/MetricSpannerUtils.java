package com.microsoft.kapp.style.utils;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.R;
import com.microsoft.kapp.style.text.StylableString;
import com.microsoft.kapp.utils.Formatter;
import java.util.Locale;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class MetricSpannerUtils {
    private static final int DEFAULT_METRIC_UNIT_FF_ID = 0;
    private static final int DEFAULT_METRIC_UNIT_TEXT_COLOR_RES_ID = 2131362321;
    private static final int DEFAULT_METRIC_UNIT_TEXT_SIZE_RES_ID = 2131427390;
    private static final int DEFAULT_METRIC_VALUE_FF_ID = 2;
    private static final int DEFAULT_METRIC_VALUE_TEXT_COLOR_RES_ID = 2131362321;
    private static final int DEFAULT_METRIC_VALUE_TEXT_SIZE_RES_ID = 2131427392;
    public static final String METRIC_UNITS_SEPARATOR_NONE = "";
    public static final String METRIC_UNITS_SEPARATOR_SPACE = " ";
    public static final int SKIP_THIS_METRIC_UNIT_PARAM = -1;

    private static boolean validateTimeUnitParam(int timeUnitValue) {
        return timeUnitValue > -1;
    }

    public static CharSequence formatTimeForLocale(Context context, FontManager fontManager, DateTime time, Locale locale) {
        String metricValue = Formatter.formatTimeValueForLocale(context, time, locale);
        int metricUnitResId = DateFormat.is24HourFormat(context) ? -1 : Formatter.formatTimeUnitShortForLocale(context, time, locale);
        return metricUnitResId == -1 ? metricValue : StylableString.format(context, (int) R.string.tracker_time_unit_halfday_short_with_value, (int) R.array.MerticFormat, metricValue, context.getString(metricUnitResId));
    }

    public static CharSequence formatHourShort(Context context, FontManager fontManager, int hour) {
        return StylableString.format(context, (int) R.string.time_value_short, (int) R.array.MerticSmallUnitFormat, Integer.valueOf(hour), context.getString(R.string.hours_unit_short));
    }

    public static CharSequence formatMinuteShort(Context context, FontManager fontManager, int minute) {
        return StylableString.format(context, (int) R.string.time_value_short, (int) R.array.MerticSmallUnitFormat, Integer.valueOf(minute), context.getString(R.string.mins_unit_short));
    }

    public static CharSequence formatSecondShort(Context context, FontManager fontManager, int second) {
        return StylableString.format(context, (int) R.string.time_value_short, (int) R.array.MerticSmallUnitFormat, Integer.valueOf(second), context.getString(R.string.secs_unit_short));
    }

    public static CharSequence formatTimeShort(Context context, FontManager fontManager, int hour, int minute, int second) {
        return formatTimeShort(context, fontManager, hour, minute, second, "");
    }

    public static CharSequence formatTimeShort(Context context, FontManager fontManager, int hour, int minute, int second, CharSequence separator) {
        boolean hasHourUnit = false;
        boolean hasMinuteUnit = false;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (validateTimeUnitParam(hour)) {
            CharSequence hourText = formatHourShort(context, fontManager, hour);
            builder.append(hourText);
            hasHourUnit = true;
        }
        if (validateTimeUnitParam(minute)) {
            if (hasHourUnit) {
                builder.append(separator);
            }
            CharSequence minuteText = formatMinuteShort(context, fontManager, minute);
            builder.append(minuteText);
            hasMinuteUnit = true;
        }
        if (validateTimeUnitParam(second)) {
            if (hasHourUnit || hasMinuteUnit) {
                builder.append(separator);
            }
            CharSequence secondText = formatSecondShort(context, fontManager, second);
            builder.append(secondText);
        }
        return builder;
    }
}
