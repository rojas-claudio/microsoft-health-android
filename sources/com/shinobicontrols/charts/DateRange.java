package com.shinobicontrols.charts;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
/* loaded from: classes.dex */
public class DateRange extends Range<Date> {
    private static Calendar c = new GregorianCalendar();

    DateRange() {
        super(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    public DateRange(Date min, Date max) {
        super(min.getTime(), max.getTime());
    }

    @Override // com.shinobicontrols.charts.Range
    public Date getMinimum() {
        return new Date((long) this.a);
    }

    @Override // com.shinobicontrols.charts.Range
    public Date getMaximum() {
        return new Date((long) this.b);
    }

    @Override // com.shinobicontrols.charts.Range
    public double getSpan() {
        return b() / 1000.0d;
    }
}
