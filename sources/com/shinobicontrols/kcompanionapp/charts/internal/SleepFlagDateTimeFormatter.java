package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.KAppDateFormatter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/* loaded from: classes.dex */
public class SleepFlagDateTimeFormatter {
    private final DateTimeFormatter dateTimeFormatter;
    private boolean is24HourFormat;
    private final String prefix;
    private final Resources resources;

    public SleepFlagDateTimeFormatter(Resources resources, String prefix, Context context) {
        this(resources, prefix, DateFormat.is24HourFormat(context));
    }

    public SleepFlagDateTimeFormatter(Resources resources, String prefix, boolean is24HourFormat) {
        this.resources = resources;
        this.prefix = prefix;
        this.is24HourFormat = is24HourFormat;
        if (this.is24HourFormat) {
            this.dateTimeFormatter = DateTimeFormat.forPattern(resources.getString(R.string.shinobicharts_sleep_flag_time_format_24hr));
        } else {
            this.dateTimeFormatter = DateTimeFormat.forPattern(resources.getString(R.string.shinobicharts_sleep_flag_time_format));
        }
    }

    public String format(DateTime dateTime) {
        return format(dateTime, false);
    }

    public String format(DateTime dateTime, boolean includeDate) {
        String formattedTime;
        DateTime dateTimeInDefaultTimeZone = new DateTime(dateTime.getMillis());
        if (this.is24HourFormat) {
            if (includeDate) {
                formattedTime = this.resources.getString(R.string.shinobicharts_no_suffix_with_date, KAppDateFormatter.formatToMonthDay(dateTimeInDefaultTimeZone), dateTimeInDefaultTimeZone.toString(this.dateTimeFormatter));
            } else {
                formattedTime = dateTimeInDefaultTimeZone.toString(this.dateTimeFormatter);
            }
        } else if (includeDate) {
            int res = dateTimeInDefaultTimeZone.hourOfDay().get() < 12 ? R.string.shinobicharts_am_suffix_with_date : R.string.shinobicharts_pm_suffix_with_date;
            formattedTime = this.resources.getString(res, KAppDateFormatter.formatToMonthDay(dateTimeInDefaultTimeZone), dateTimeInDefaultTimeZone.toString(this.dateTimeFormatter));
        } else {
            int res2 = dateTimeInDefaultTimeZone.hourOfDay().get() < 12 ? R.string.shinobicharts_am_suffix : R.string.shinobicharts_pm_suffix;
            formattedTime = this.resources.getString(res2, dateTimeInDefaultTimeZone.toString(this.dateTimeFormatter));
        }
        return String.format(this.prefix, formattedTime);
    }
}
