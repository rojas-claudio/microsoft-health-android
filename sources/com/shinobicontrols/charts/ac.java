package com.shinobicontrols.charts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
/* loaded from: classes.dex */
class ac {
    Range<Date> a;
    private final Calendar b = new GregorianCalendar();

    /* JADX INFO: Access modifiers changed from: package-private */
    public ac() {
        this.b.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Range<Date>> a(RepeatedTimePeriod repeatedTimePeriod, Range<Date> range) {
        this.a = range;
        ArrayList arrayList = new ArrayList();
        if (Range.a(range)) {
            return arrayList;
        }
        Date b = b(repeatedTimePeriod, range);
        Date maximum = range.getMaximum();
        DateFrequency dateFrequency = repeatedTimePeriod.b;
        DateFrequency dateFrequency2 = repeatedTimePeriod.c;
        while (b.before(maximum)) {
            arrayList.add(new DateRange(b, a(b, dateFrequency)));
            b = a(b, dateFrequency2);
        }
        return arrayList;
    }

    private Date b(RepeatedTimePeriod repeatedTimePeriod, Range<Date> range) {
        Date start = repeatedTimePeriod.getStart();
        Date minimum = range.getMinimum();
        DateFrequency frequency = repeatedTimePeriod.getFrequency();
        this.b.clear();
        this.b.setTime(start);
        while (start.before(minimum)) {
            start = a(start, frequency);
        }
        while (start.after(minimum)) {
            start = b(start, frequency);
        }
        return this.b.getTime();
    }

    private Date a(Date date, DateFrequency dateFrequency) {
        return a(date, dateFrequency, true);
    }

    private Date b(Date date, DateFrequency dateFrequency) {
        return a(date, dateFrequency, false);
    }

    private Date a(Date date, DateFrequency dateFrequency, boolean z) {
        int i = z ? dateFrequency.a : -dateFrequency.a;
        this.b.setTime(date);
        this.b.add(dateFrequency.b.a, i);
        return this.b.getTime();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(Range<Date> range) {
        return this.a == null || !this.a.d(range);
    }
}
