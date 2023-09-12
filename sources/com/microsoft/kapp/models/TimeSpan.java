package com.microsoft.kapp.models;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.TextUtils;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.style.text.StylableString;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.LockedStringUtils;
import com.microsoft.kapp.utils.StringUtils;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public class TimeSpan {
    private static final int HOURS_PER_DAY = 24;
    private static final int MILLIS_PER_DAY = 86400000;
    private static final int MILLIS_PER_HOUR = 3600000;
    private static final int MILLIS_PER_MINUTE = 60000;
    private static final int MILLIS_PER_SECOND = 1000;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTE = 60;
    private double mMilliseconds;

    private TimeSpan(double milliseconds) {
        this.mMilliseconds = milliseconds;
    }

    public TimeSpan add(TimeSpan timeSpan) {
        Validate.notNull(timeSpan, "timeSpan");
        return new TimeSpan(getTotalMilliseconds() + timeSpan.getTotalMilliseconds());
    }

    public static TimeSpan fromMilliseconds(double value) {
        return new TimeSpan(value);
    }

    public static TimeSpan fromSeconds(double value) {
        return new TimeSpan(1000.0d * value);
    }

    public static TimeSpan fromMinutes(double value) {
        return new TimeSpan(60000.0d * value);
    }

    public static TimeSpan fromHours(double value) {
        return new TimeSpan(3600000.0d * value);
    }

    public static TimeSpan fromDays(double value) {
        return new TimeSpan(8.64E7d * value);
    }

    public double getTotalMilliseconds() {
        return this.mMilliseconds;
    }

    public double getTotalSeconds() {
        return this.mMilliseconds / 1000.0d;
    }

    public double getTotalMinutes() {
        return this.mMilliseconds / 60000.0d;
    }

    public double getTotalHours() {
        return this.mMilliseconds / 3600000.0d;
    }

    public double getTotalDays() {
        return this.mMilliseconds / 8.64E7d;
    }

    public int getMilliseconds() {
        return (int) this.mMilliseconds;
    }

    public int getSeconds() {
        return ((int) getTotalSeconds()) % 60;
    }

    public int getMinutes() {
        return ((int) getTotalMinutes()) % 60;
    }

    public int getHours() {
        return ((int) getTotalHours()) % 24;
    }

    public int getDays() {
        return (int) getTotalDays();
    }

    public String formatTime() {
        int hours = (int) getTotalHours();
        int minutes = getMinutes();
        int seconds = getSeconds();
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(String.format("%02d", Integer.valueOf(hours)));
            sb.append(TreeNode.NODES_ID_SEPARATOR);
        }
        sb.append(String.format("%02d:%02d", Integer.valueOf(minutes), Integer.valueOf(seconds)));
        return sb.toString();
    }

    public String formatToHrsMinsSecs(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        return formatTimeHrMinSec(context, false);
    }

    public String formatTimeHrMinSec(Context context, boolean longForm) {
        Resources resources = context.getResources();
        int hours = (int) getTotalHours();
        int minutes = getMinutes();
        int seconds = getSeconds();
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            if (longForm) {
                sb.append(StringUtils.unitHours(hours, resources));
            } else {
                sb.append(StringUtils.unitHrs(hours, resources));
            }
        }
        if (minutes > 0) {
            sb.append(hours > 0 ? MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE : "");
            if (longForm) {
                sb.append(StringUtils.unitMinutes(minutes, resources));
            } else {
                sb.append(StringUtils.unitMins(minutes, resources));
            }
        }
        if (seconds > 0) {
            sb.append((hours > 0 || minutes > 0) ? MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE : "");
            if (longForm) {
                sb.append(StringUtils.unitSeconds(seconds, resources));
            } else {
                sb.append(StringUtils.unitSecs(seconds, resources));
            }
        } else if (sb.length() == 0) {
            if (longForm) {
                sb.append(StringUtils.unitSeconds(0, resources));
            } else {
                sb.append(StringUtils.unitSecs(0, resources));
            }
        }
        return sb.toString();
    }

    public String formatTimeHrMinSecLocked(Context context, boolean longForm) {
        Resources resources = context.getResources();
        int hours = (int) getTotalHours();
        int minutes = getMinutes();
        int seconds = getSeconds();
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            if (longForm) {
                sb.append(StringUtils.unitHours(hours, resources));
            } else {
                sb.append(StringUtils.unitHrs(hours, resources));
            }
        }
        if (minutes > 0) {
            sb.append(hours > 0 ? MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE : "");
            if (longForm) {
                sb.append(LockedStringUtils.unitMinutes(minutes, resources));
            } else {
                sb.append(LockedStringUtils.unitMins(minutes, resources));
            }
        }
        if (seconds > 0) {
            sb.append((hours > 0 || minutes > 0) ? MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE : "");
            if (longForm) {
                sb.append(LockedStringUtils.unitSeconds(seconds, resources));
            } else {
                sb.append(LockedStringUtils.unitSecs(seconds, resources));
            }
        } else if (sb.length() == 0) {
            if (longForm) {
                sb.append(LockedStringUtils.unitSeconds(0, resources));
            } else {
                sb.append(LockedStringUtils.unitSecs(0, resources));
            }
        }
        return sb.toString();
    }

    public String formatTimePrimeNotation(Context context, boolean forceShowFullTime) {
        int minutes = (int) getTotalMinutes();
        int seconds = getSeconds();
        Resources resources = context.getResources();
        StringBuilder sb = new StringBuilder();
        if (forceShowFullTime || minutes > 0) {
            int minutesThresholdValueToIgnore = resources.getInteger(R.integer.pace_minutes_threshold_value_to_ignore);
            if (minutes > minutesThresholdValueToIgnore) {
                return resources.getString(R.string.no_value);
            }
            String minsFormatted = resources.getString(R.string.pace_format_mins, Integer.valueOf(minutes));
            int minutesThresholdValueToSkipSeconds = resources.getInteger(R.integer.pace_minutes_threshold_value_to_skip_seconds);
            if (minutes < minutesThresholdValueToSkipSeconds) {
                sb.append(minsFormatted);
            } else {
                return minsFormatted;
            }
        }
        if (seconds > 0 || forceShowFullTime) {
            if (minutes > 0 || forceShowFullTime) {
                return context.getString(R.string.pace_format_mins_and_secs, Integer.valueOf(minutes), Integer.valueOf(seconds));
            }
            return context.getString(R.string.pace_format_secs, Integer.valueOf(seconds));
        }
        return sb.toString();
    }

    public String formatTimePrimeNotation(Context context) {
        return formatTimePrimeNotation(context, true);
    }

    public String formatTimeShortNotation(Context context) {
        if (context != null) {
            Resources resources = context.getResources();
            int hours = (int) getTotalHours();
            int minutes = getMinutes();
            int seconds = getSeconds();
            if (hours > 0) {
                if (minutes > 0) {
                    return resources.getString(R.string.hours_mins_value_short, Integer.valueOf(hours), Integer.valueOf(minutes));
                }
                if (seconds > 0) {
                    return resources.getString(R.string.hours_secs_value_short, Integer.valueOf(hours), Integer.valueOf(seconds));
                }
                return resources.getString(R.string.hours_value_short_nospace, Integer.valueOf(hours));
            } else if (minutes > 0) {
                if (seconds > 0) {
                    return resources.getString(R.string.mins_secs_short_nospace, Integer.valueOf(minutes), Integer.valueOf(seconds));
                }
                return resources.getString(R.string.mins_value_short_nospace, Integer.valueOf(minutes));
            } else {
                return resources.getString(R.string.secs_short_nospace, Integer.valueOf(seconds));
            }
        }
        return "";
    }

    public CharSequence formatTimeShortNotationStyled(Context context) {
        int hours = (int) getTotalHours();
        int minutes = getMinutes();
        int seconds = getSeconds();
        Resources resources = context.getResources();
        SpannableString hoursString = null;
        SpannableString minutesString = null;
        SpannableString secondsString = null;
        if (hours > 0) {
            hoursString = StylableString.format(context, (int) R.string.metric_format_no_space, (int) R.array.MerticSmallUnitFormat, Integer.valueOf(hours), resources.getString(R.string.hours_unit_short));
        }
        if (minutes > 0) {
            minutesString = StylableString.format(context, (int) R.string.metric_format_no_space, (int) R.array.MerticSmallUnitFormat, Integer.valueOf(minutes), resources.getString(R.string.mins_unit_short));
        }
        if (seconds > 0) {
            secondsString = StylableString.format(context, (int) R.string.metric_format_no_space, (int) R.array.MerticSmallUnitFormat, Integer.valueOf(seconds), resources.getString(R.string.secs_unit_short));
        }
        if (hours > 0) {
            return minutes > 0 ? TextUtils.concat(hoursString, MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE, minutesString) : seconds > 0 ? TextUtils.concat(hoursString, MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE, secondsString) : hoursString;
        } else if (minutes > 0) {
            if (seconds > 0) {
                return TextUtils.concat(minutesString, MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE, secondsString);
            }
            SpannableString hoursString2 = minutesString;
            return hoursString2;
        } else if (seconds > 0) {
            SpannableString hoursString3 = secondsString;
            return hoursString3;
        } else {
            return "";
        }
    }
}
