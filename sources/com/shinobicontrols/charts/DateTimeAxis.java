package com.shinobicontrols.charts;

import android.graphics.PointF;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.DateFrequency;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class DateTimeAxis extends Axis<Date, DateFrequency> {
    private DateFormat B;
    private boolean D;
    private Date J;
    private final SimpleDateFormat A = (SimpleDateFormat) DateFormat.getDateInstance();
    private final PointF C = new PointF(1.0f, 1.0f);
    private long E = 0;
    private String F = "";
    private final Map<RepeatedTimePeriod, List<Range<Date>>> G = new LinkedHashMap();
    private final ad H = new ad(this);
    private final ac I = new ac();

    public DateTimeAxis() {
    }

    public DateTimeAxis(DateRange defaultRange) {
        setDefaultRange(defaultRange);
        a(defaultRange.getMinimum());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public boolean isDataValid(Object point) {
        return point instanceof Date;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double convertPoint(Object userData) {
        return translatePoint(userData);
    }

    private void a(Date date) {
        if (this.E == 0) {
            this.E = date.getTime();
            this.J = new Date(this.E);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double translatePoint(Object userData) {
        validateUserData(userData);
        return transformUserValueToInternal((Date) userData);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double transformExternalValueToInternal(Date externalValue) {
        long time = externalValue.getTime();
        a(externalValue);
        return time - this.E;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public Date transformInternalValueToExternal(double internalValue) {
        if (internalValue == Double.POSITIVE_INFINITY) {
            return new Date(Long.MAX_VALUE);
        }
        if (internalValue == Double.NEGATIVE_INFINITY) {
            return new Date(Long.MIN_VALUE);
        }
        return new Date(((long) internalValue) + this.E);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.shinobicontrols.charts.Axis
    public Date transformUserValueToChartValue(Date userValue) {
        return userValue;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.shinobicontrols.charts.Axis
    public Date transformChartValueToUserValue(Date chartValue) {
        return chartValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public Range<Date> createRange(Date min, Date max) {
        return new DateRange(min, max);
    }

    @Override // com.shinobicontrols.charts.Axis
    String i() {
        I();
        if (k()) {
            return this.x;
        }
        if (H()) {
            String format = this.B.format(Double.valueOf(this.i.a));
            String format2 = this.B.format(Double.valueOf(this.i.b));
            if (format2.length() <= format.length()) {
                format2 = format;
            }
            this.w = format2;
        } else {
            switch (((DateFrequency) this.r).b) {
                case SECONDS:
                case MINUTES:
                    this.w = " 00:00:00 ";
                    break;
                case HOURS:
                    this.w = " Mmm 00:00 ";
                    break;
                case MONTHS:
                    this.w = " Mmm 00 ";
                    break;
                case YEARS:
                    this.w = " 2000 ";
                    break;
                default:
                    this.w = " 00 Mmm ";
                    break;
            }
        }
        return this.w;
    }

    public DateFormat getLabelFormat() {
        return this.B;
    }

    public void setLabelFormat(DateFormat dateFormat) {
        this.B = dateFormat;
    }

    @Override // com.shinobicontrols.charts.Axis
    public String getFormattedString(Date value) {
        return H() ? this.B.format(value) : this.A.format(value);
    }

    private boolean H() {
        return this.B != null;
    }

    /* JADX WARN: Type inference failed for: r0v195, types: [U, com.shinobicontrols.charts.DateFrequency] */
    /* JADX WARN: Type inference failed for: r0v196, types: [U, com.shinobicontrols.charts.DateFrequency] */
    @Override // com.shinobicontrols.charts.Axis
    void c(int i) {
        char c;
        char c2;
        this.D = false;
        if (this.r == 0) {
            this.r = new DateFrequency(1, DateFrequency.Denomination.SECONDS);
        }
        if (this.s == 0) {
            this.s = new DateFrequency(1, DateFrequency.Denomination.SECONDS);
        }
        double b = this.i.b() / 10000.0d;
        if (b < 1.1d) {
            ((DateFrequency) this.r).a(1, DateFrequency.Denomination.SECONDS);
            c = 1;
        } else if (b < 16.5d) {
            ((DateFrequency) this.r).a(15, DateFrequency.Denomination.SECONDS);
            c = 1;
        } else if (b < 33.0d) {
            ((DateFrequency) this.r).a(30, DateFrequency.Denomination.SECONDS);
            ((DateFrequency) this.s).a(15, DateFrequency.Denomination.SECONDS);
            c = 1;
        } else if (b < 66.0d) {
            ((DateFrequency) this.r).a(1, DateFrequency.Denomination.MINUTES);
            ((DateFrequency) this.s).a(30, DateFrequency.Denomination.SECONDS);
            c = 1;
        } else if (b < 990.0d) {
            ((DateFrequency) this.r).a(15, DateFrequency.Denomination.MINUTES);
            ((DateFrequency) this.s).a(5, DateFrequency.Denomination.MINUTES);
            c = 1;
        } else if (b < 1980.0d) {
            ((DateFrequency) this.r).a(30, DateFrequency.Denomination.MINUTES);
            ((DateFrequency) this.s).a(15, DateFrequency.Denomination.MINUTES);
            c = 1;
        } else if (b < 3960.0d) {
            ((DateFrequency) this.r).a(1, DateFrequency.Denomination.HOURS);
            ((DateFrequency) this.s).a(30, DateFrequency.Denomination.MINUTES);
            c = 1;
        } else if (b < 23760.0d) {
            ((DateFrequency) this.r).a(6, DateFrequency.Denomination.HOURS);
            ((DateFrequency) this.s).a(1, DateFrequency.Denomination.HOURS);
            c = 1;
        } else if (b < 47520.0d) {
            ((DateFrequency) this.r).a(12, DateFrequency.Denomination.HOURS);
            ((DateFrequency) this.s).a(6, DateFrequency.Denomination.HOURS);
            c = 1;
        } else if (b < 95040.00000000001d) {
            ((DateFrequency) this.r).a(1, DateFrequency.Denomination.DAYS);
            ((DateFrequency) this.s).a(12, DateFrequency.Denomination.HOURS);
            c = 1;
        } else if (b < 665280.0d) {
            ((DateFrequency) this.r).a(7, DateFrequency.Denomination.DAYS);
            ((DateFrequency) this.s).a(1, DateFrequency.Denomination.DAYS);
            c = 1;
        } else if (b < 1330560.0d) {
            ((DateFrequency) this.r).a(14, DateFrequency.Denomination.DAYS);
            ((DateFrequency) this.s).a(7, DateFrequency.Denomination.DAYS);
            c = 1;
        } else if (b < 2946240.0000000005d) {
            ((DateFrequency) this.r).a(1, DateFrequency.Denomination.MONTHS);
            ((DateFrequency) this.s).a(7, DateFrequency.Denomination.DAYS);
            c = 1;
        } else if (b < 8838720.0d) {
            ((DateFrequency) this.r).a(3, DateFrequency.Denomination.MONTHS);
            ((DateFrequency) this.s).a(1, DateFrequency.Denomination.MONTHS);
            c = 1;
        } else if (b < 736560.0d) {
            ((DateFrequency) this.r).a(6, DateFrequency.Denomination.MONTHS);
            ((DateFrequency) this.s).a(3, DateFrequency.Denomination.MONTHS);
            c = 1;
        } else if (b < 3.471336E7d) {
            ((DateFrequency) this.r).a(1, DateFrequency.Denomination.YEARS);
            ((DateFrequency) this.s).a(3, DateFrequency.Denomination.MONTHS);
            c = 1;
        } else if (b < 6.942672E7d) {
            c = 2;
            ((DateFrequency) this.r).a(2, DateFrequency.Denomination.YEARS);
            ((DateFrequency) this.s).a(1, DateFrequency.Denomination.YEARS);
        } else {
            ((DateFrequency) this.r).a(5, DateFrequency.Denomination.YEARS);
            ((DateFrequency) this.s).a(2, DateFrequency.Denomination.YEARS);
            c = 5;
        }
        while (true) {
            r();
            if (a((int) Math.floor(this.i.b() / ((DateFrequency) this.r).a()), i, this.C)) {
                if (((DateFrequency) this.r).b(1, DateFrequency.Denomination.SECONDS) || ((DateFrequency) this.r).b(1, DateFrequency.Denomination.MONTHS)) {
                    this.D = true;
                    return;
                } else {
                    this.D = false;
                    return;
                }
            }
            switch (((DateFrequency) this.r).b) {
                case SECONDS:
                    switch (((DateFrequency) this.r).a) {
                        case 1:
                            ((DateFrequency) this.r).a(15, DateFrequency.Denomination.SECONDS);
                            ((DateFrequency) this.s).a(1, DateFrequency.Denomination.SECONDS);
                            break;
                        case 15:
                            ((DateFrequency) this.r).a(30, DateFrequency.Denomination.SECONDS);
                            ((DateFrequency) this.s).a(15, DateFrequency.Denomination.SECONDS);
                            break;
                        case 30:
                            ((DateFrequency) this.r).a(1, DateFrequency.Denomination.MINUTES);
                            ((DateFrequency) this.s).a(30, DateFrequency.Denomination.SECONDS);
                            break;
                    }
                    c2 = c;
                    continue;
                    c = c2;
                case MINUTES:
                    switch (((DateFrequency) this.r).a) {
                        case 1:
                            ((DateFrequency) this.r).a(5, DateFrequency.Denomination.MINUTES);
                            ((DateFrequency) this.s).a(1, DateFrequency.Denomination.MINUTES);
                            break;
                        case 5:
                            ((DateFrequency) this.r).a(15, DateFrequency.Denomination.MINUTES);
                            ((DateFrequency) this.s).a(5, DateFrequency.Denomination.MINUTES);
                            break;
                        case 15:
                            ((DateFrequency) this.r).a(30, DateFrequency.Denomination.MINUTES);
                            ((DateFrequency) this.s).a(15, DateFrequency.Denomination.MINUTES);
                            break;
                        case 30:
                            ((DateFrequency) this.r).a(1, DateFrequency.Denomination.HOURS);
                            ((DateFrequency) this.s).a(30, DateFrequency.Denomination.MINUTES);
                            break;
                    }
                    c2 = c;
                    continue;
                    c = c2;
                case HOURS:
                    switch (((DateFrequency) this.r).a) {
                        case 1:
                            ((DateFrequency) this.r).a(6, DateFrequency.Denomination.HOURS);
                            ((DateFrequency) this.s).a(1, DateFrequency.Denomination.HOURS);
                            break;
                        case 6:
                            ((DateFrequency) this.r).a(12, DateFrequency.Denomination.HOURS);
                            ((DateFrequency) this.s).a(6, DateFrequency.Denomination.HOURS);
                            break;
                        case 12:
                            ((DateFrequency) this.r).a(1, DateFrequency.Denomination.DAYS);
                            ((DateFrequency) this.s).a(12, DateFrequency.Denomination.HOURS);
                            break;
                    }
                    c2 = c;
                    continue;
                    c = c2;
                case MONTHS:
                    switch (((DateFrequency) this.r).a) {
                        case 1:
                            ((DateFrequency) this.r).a(3, DateFrequency.Denomination.MONTHS);
                            ((DateFrequency) this.s).a(1, DateFrequency.Denomination.MONTHS);
                            break;
                        case 3:
                            ((DateFrequency) this.r).a(6, DateFrequency.Denomination.MONTHS);
                            ((DateFrequency) this.s).a(3, DateFrequency.Denomination.MONTHS);
                            break;
                        case 6:
                            ((DateFrequency) this.r).a(1, DateFrequency.Denomination.YEARS);
                            ((DateFrequency) this.s).a(3, DateFrequency.Denomination.MONTHS);
                            break;
                    }
                    c2 = c;
                    continue;
                    c = c2;
                case YEARS:
                    int i2 = ((DateFrequency) this.r).a;
                    switch (c) {
                        case 1:
                            ((DateFrequency) this.r).a(i2 * 2, DateFrequency.Denomination.YEARS);
                            ((DateFrequency) this.s).a(i2, DateFrequency.Denomination.YEARS);
                            c2 = 2;
                            continue;
                        case 2:
                            ((DateFrequency) this.r).a((int) (i2 * 2.5d), DateFrequency.Denomination.YEARS);
                            ((DateFrequency) this.s).a(i2 / 2, DateFrequency.Denomination.YEARS);
                            c2 = 5;
                            break;
                        case 5:
                            ((DateFrequency) this.r).a(i2 * 2, DateFrequency.Denomination.YEARS);
                            ((DateFrequency) this.s).a(i2, DateFrequency.Denomination.YEARS);
                            c2 = 1;
                            break;
                    }
                    c = c2;
                    break;
                case DAYS:
                    switch (((DateFrequency) this.r).a) {
                        case 1:
                            ((DateFrequency) this.r).a(7, DateFrequency.Denomination.DAYS);
                            ((DateFrequency) this.s).a(1, DateFrequency.Denomination.DAYS);
                            break;
                        case 7:
                            ((DateFrequency) this.r).a(14, DateFrequency.Denomination.DAYS);
                            ((DateFrequency) this.s).a(7, DateFrequency.Denomination.DAYS);
                            break;
                        case 14:
                            ((DateFrequency) this.r).a(1, DateFrequency.Denomination.MONTHS);
                            ((DateFrequency) this.s).a(7, DateFrequency.Denomination.DAYS);
                            break;
                    }
                    c2 = c;
                    continue;
                    c = c2;
            }
            c2 = c;
            c = c2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double a(int i) {
        return this.H.a((DateFrequency) this.r);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double b(int i) {
        return this.H.b((DateFrequency) this.s);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public boolean b(double d) {
        return this.H.c(d, (DateFrequency) this.r);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double a(double d, boolean z) {
        return this.H.a(d, z ? (DateFrequency) this.r : (DateFrequency) this.s);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double transformExternalFrequencyToInternal(DateFrequency externalValue) {
        return externalValue.a();
    }

    private void I() {
        if (H()) {
            this.u.c();
            return;
        }
        String J = J();
        this.A.applyPattern(J);
        if (J != this.F) {
            this.u.c();
            this.F = J;
        }
    }

    @Override // com.shinobicontrols.charts.Axis
    double x() {
        return 1.728E8d;
    }

    private String J() {
        switch (((DateFrequency) this.r).b) {
            case SECONDS:
            case MINUTES:
                return "HH:mm:ss";
            case HOURS:
                return "EEE HH:mm";
            case MONTHS:
                return "MMM yy";
            case YEARS:
                return "yyyy";
            default:
                return "dd MMM";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.shinobicontrols.charts.Axis
    public void setMajorTickFrequencyInternal(DateFrequency frequency) {
        if (frequency == 0) {
            this.p = null;
        } else if (frequency.a > 0) {
            this.p = frequency;
        } else {
            cx.b(this.b != null ? this.b.getContext().getString(R.string.DateTimeAxisInvalidDateFrequency) : "The DateFrequency is invalid and will be ignored");
            this.p = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.shinobicontrols.charts.Axis
    public void setMinorTickFrequencyInternal(DateFrequency frequency) {
        if (frequency == 0) {
            this.q = null;
        } else if (frequency.a > 0) {
            this.q = frequency;
        } else {
            cx.b(this.b != null ? this.b.getContext().getString(R.string.DateTimeAxisInvalidDateFrequency) : "The DateFrequency is invalid and will be ignored");
            this.q = null;
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [U, com.shinobicontrols.charts.DateFrequency] */
    @Override // com.shinobicontrols.charts.Axis
    void p() {
        if (this.p != 0) {
            this.r = ((DateFrequency) this.p).m8clone();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Type inference failed for: r0v4, types: [U, com.shinobicontrols.charts.DateFrequency] */
    @Override // com.shinobicontrols.charts.Axis
    public void q() {
        if (l() && this.q != 0) {
            this.s = ((DateFrequency) this.q).m8clone();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public Date getDefaultBaseline() {
        return new Date(this.E);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public Date applyMappingForSkipRangesToUserValue(Date userValue) {
        return new Date((long) this.y.a(userValue.getTime()));
    }

    @Override // com.shinobicontrols.charts.Axis
    double convertUserValueTypeToInternalDataType(Object rawUserValue) {
        return ((Date) rawUserValue).getTime();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public Date removeMappingForSkipRangesFromChartValue(Date userValue) {
        return new Date((long) this.z.a(userValue.getTime()));
    }

    public void addRepeatedSkipRange(RepeatedTimePeriod repeatedTimePeriod) {
        if (this.b != null) {
            this.b.s();
        }
        if (repeatedTimePeriod == null) {
            cx.b(this.b != null ? this.b.getContext().getString(R.string.CannotAddNullRepeatedSkipRange) : "Cannot add a null repeated skip range.");
            return;
        }
        List<Range<Date>> a = this.I.a(repeatedTimePeriod, K());
        b(a);
        this.G.put(repeatedTimePeriod, a);
        if (!a.isEmpty()) {
            D();
        }
    }

    private void b(List<Range<Date>> list) {
        ArrayList arrayList = new ArrayList();
        for (Range<Date> range : list) {
            if (!b(range)) {
                arrayList.add(range);
                c(range);
            }
        }
        list.removeAll(arrayList);
    }

    private void c(Range<Date> range) {
        String format;
        String date = range.getMinimum().toString();
        String date2 = range.getMaximum().toString();
        if (this.b != null) {
            format = this.b.getContext().getString(R.string.CannotAddCalculatedUndefinedOrEmptySkip, date, date2);
        } else {
            format = String.format("Calculated skip range with min: %1$s and max: %2$s is invalid: cannot be added as it has zero or negative span", date, date2);
        }
        cx.b(format);
    }

    private DateRange K() {
        return new DateRange(transformInternalValueToUser(this.v.d()), transformInternalValueToUser(this.v.f()));
    }

    public void removeRepeatedSkipRange(RepeatedTimePeriod repeatedTimePeriod) {
        if (this.G.remove(repeatedTimePeriod) != null) {
            D();
        }
    }

    public void removeAllRepeatedSkipRanges() {
        ArrayList<RepeatedTimePeriod> arrayList = new ArrayList(this.G.keySet());
        if (!arrayList.isEmpty()) {
            for (RepeatedTimePeriod repeatedTimePeriod : arrayList) {
                this.G.remove(repeatedTimePeriod);
            }
            D();
        }
    }

    public List<RepeatedTimePeriod> getRepeatedSkipRanges() {
        return Collections.unmodifiableList(new ArrayList(this.G.keySet()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public List<Range<Date>> E() {
        ArrayList arrayList = new ArrayList(super.E());
        for (List<Range<Date>> list : this.G.values()) {
            arrayList.addAll(list);
        }
        return arrayList;
    }

    @Override // com.shinobicontrols.charts.Axis
    void g() {
        if (!this.G.isEmpty()) {
            DateRange K = K();
            if (this.I.a(K)) {
                for (RepeatedTimePeriod repeatedTimePeriod : this.G.keySet()) {
                    List<Range<Date>> a = this.I.a(repeatedTimePeriod, K);
                    b(a);
                    this.G.put(repeatedTimePeriod, a);
                }
                D();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public void a(v vVar) {
        super.a(vVar);
        if (vVar != null && !this.G.isEmpty()) {
            vVar.s();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public void D() {
        this.H.a();
        super.D();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double G() {
        return this.J == null ? Constants.SPLITS_ACCURACY : transformUserValueToInternal(this.J);
    }

    public void disableTickMarkCaching(boolean disableTickMarkCaching) {
        this.H.a = disableTickMarkCaching;
    }

    public boolean isTickMarkCachingDisabled() {
        return this.H.a;
    }
}
