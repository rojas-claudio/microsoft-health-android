package org.joda.time.chrono;

import java.io.Serializable;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.UnsupportedDateTimeField;
import org.joda.time.field.UnsupportedDurationField;
/* loaded from: classes.dex */
public abstract class BaseChronology extends Chronology implements Serializable {
    private static final long serialVersionUID = -7310865996721419676L;

    @Override // org.joda.time.Chronology
    public abstract DateTimeZone getZone();

    @Override // org.joda.time.Chronology
    public abstract String toString();

    @Override // org.joda.time.Chronology
    public abstract Chronology withUTC();

    @Override // org.joda.time.Chronology
    public abstract Chronology withZone(DateTimeZone dateTimeZone);

    @Override // org.joda.time.Chronology
    public long getDateTimeMillis(int i, int i2, int i3, int i4) throws IllegalArgumentException {
        return millisOfDay().set(dayOfMonth().set(monthOfYear().set(year().set(0L, i), i2), i3), i4);
    }

    @Override // org.joda.time.Chronology
    public long getDateTimeMillis(int i, int i2, int i3, int i4, int i5, int i6, int i7) throws IllegalArgumentException {
        return millisOfSecond().set(secondOfMinute().set(minuteOfHour().set(hourOfDay().set(dayOfMonth().set(monthOfYear().set(year().set(0L, i), i2), i3), i4), i5), i6), i7);
    }

    @Override // org.joda.time.Chronology
    public long getDateTimeMillis(long j, int i, int i2, int i3, int i4) throws IllegalArgumentException {
        return millisOfSecond().set(secondOfMinute().set(minuteOfHour().set(hourOfDay().set(j, i), i2), i3), i4);
    }

    @Override // org.joda.time.Chronology
    public void validate(ReadablePartial readablePartial, int[] iArr) {
        int size = readablePartial.size();
        for (int i = 0; i < size; i++) {
            int i2 = iArr[i];
            DateTimeField field = readablePartial.getField(i);
            if (i2 < field.getMinimumValue()) {
                throw new IllegalFieldValueException(field.getType(), Integer.valueOf(i2), Integer.valueOf(field.getMinimumValue()), (Number) null);
            }
            if (i2 > field.getMaximumValue()) {
                throw new IllegalFieldValueException(field.getType(), Integer.valueOf(i2), (Number) null, Integer.valueOf(field.getMaximumValue()));
            }
        }
        for (int i3 = 0; i3 < size; i3++) {
            int i4 = iArr[i3];
            DateTimeField field2 = readablePartial.getField(i3);
            if (i4 < field2.getMinimumValue(readablePartial, iArr)) {
                throw new IllegalFieldValueException(field2.getType(), Integer.valueOf(i4), Integer.valueOf(field2.getMinimumValue(readablePartial, iArr)), (Number) null);
            }
            if (i4 > field2.getMaximumValue(readablePartial, iArr)) {
                throw new IllegalFieldValueException(field2.getType(), Integer.valueOf(i4), (Number) null, Integer.valueOf(field2.getMaximumValue(readablePartial, iArr)));
            }
        }
    }

    @Override // org.joda.time.Chronology
    public int[] get(ReadablePartial readablePartial, long j) {
        int size = readablePartial.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = readablePartial.getFieldType(i).getField(this).get(j);
        }
        return iArr;
    }

    @Override // org.joda.time.Chronology
    public long set(ReadablePartial readablePartial, long j) {
        int size = readablePartial.size();
        for (int i = 0; i < size; i++) {
            j = readablePartial.getFieldType(i).getField(this).set(j, readablePartial.getValue(i));
        }
        return j;
    }

    @Override // org.joda.time.Chronology
    public int[] get(ReadablePeriod readablePeriod, long j, long j2) {
        int size = readablePeriod.size();
        int[] iArr = new int[size];
        if (j != j2) {
            long j3 = j;
            for (int i = 0; i < size; i++) {
                DurationField field = readablePeriod.getFieldType(i).getField(this);
                int difference = field.getDifference(j2, j3);
                if (difference != 0) {
                    j3 = field.add(j3, difference);
                }
                iArr[i] = difference;
            }
        }
        return iArr;
    }

    @Override // org.joda.time.Chronology
    public int[] get(ReadablePeriod readablePeriod, long j) {
        int size = readablePeriod.size();
        int[] iArr = new int[size];
        if (j != 0) {
            long j2 = 0;
            for (int i = 0; i < size; i++) {
                DurationField field = readablePeriod.getFieldType(i).getField(this);
                if (field.isPrecise()) {
                    int difference = field.getDifference(j, j2);
                    j2 = field.add(j2, difference);
                    iArr[i] = difference;
                }
            }
        }
        return iArr;
    }

    @Override // org.joda.time.Chronology
    public long add(ReadablePeriod readablePeriod, long j, int i) {
        if (i != 0 && readablePeriod != null) {
            int size = readablePeriod.size();
            long j2 = j;
            for (int i2 = 0; i2 < size; i2++) {
                long value = readablePeriod.getValue(i2);
                if (value != 0) {
                    j2 = readablePeriod.getFieldType(i2).getField(this).add(j2, value * i);
                }
            }
            return j2;
        }
        return j;
    }

    @Override // org.joda.time.Chronology
    public long add(long j, long j2, int i) {
        return (j2 == 0 || i == 0) ? j : FieldUtils.safeAdd(j, FieldUtils.safeMultiply(j2, i));
    }

    @Override // org.joda.time.Chronology
    public DurationField millis() {
        return UnsupportedDurationField.getInstance(DurationFieldType.millis());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField millisOfSecond() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.millisOfSecond(), millis());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField millisOfDay() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.millisOfDay(), millis());
    }

    @Override // org.joda.time.Chronology
    public DurationField seconds() {
        return UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField secondOfMinute() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.secondOfMinute(), seconds());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField secondOfDay() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.secondOfDay(), seconds());
    }

    @Override // org.joda.time.Chronology
    public DurationField minutes() {
        return UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField minuteOfHour() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.minuteOfHour(), minutes());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField minuteOfDay() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.minuteOfDay(), minutes());
    }

    @Override // org.joda.time.Chronology
    public DurationField hours() {
        return UnsupportedDurationField.getInstance(DurationFieldType.hours());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField hourOfDay() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.hourOfDay(), hours());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField clockhourOfDay() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.clockhourOfDay(), hours());
    }

    @Override // org.joda.time.Chronology
    public DurationField halfdays() {
        return UnsupportedDurationField.getInstance(DurationFieldType.halfdays());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField hourOfHalfday() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.hourOfHalfday(), hours());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField clockhourOfHalfday() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.clockhourOfHalfday(), hours());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField halfdayOfDay() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.halfdayOfDay(), halfdays());
    }

    @Override // org.joda.time.Chronology
    public DurationField days() {
        return UnsupportedDurationField.getInstance(DurationFieldType.days());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField dayOfWeek() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.dayOfWeek(), days());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField dayOfMonth() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.dayOfMonth(), days());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField dayOfYear() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.dayOfYear(), days());
    }

    @Override // org.joda.time.Chronology
    public DurationField weeks() {
        return UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField weekOfWeekyear() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.weekOfWeekyear(), weeks());
    }

    @Override // org.joda.time.Chronology
    public DurationField weekyears() {
        return UnsupportedDurationField.getInstance(DurationFieldType.weekyears());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField weekyear() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.weekyear(), weekyears());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField weekyearOfCentury() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.weekyearOfCentury(), weekyears());
    }

    @Override // org.joda.time.Chronology
    public DurationField months() {
        return UnsupportedDurationField.getInstance(DurationFieldType.months());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField monthOfYear() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.monthOfYear(), months());
    }

    @Override // org.joda.time.Chronology
    public DurationField years() {
        return UnsupportedDurationField.getInstance(DurationFieldType.years());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField year() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.year(), years());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField yearOfEra() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.yearOfEra(), years());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField yearOfCentury() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.yearOfCentury(), years());
    }

    @Override // org.joda.time.Chronology
    public DurationField centuries() {
        return UnsupportedDurationField.getInstance(DurationFieldType.centuries());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField centuryOfEra() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.centuryOfEra(), centuries());
    }

    @Override // org.joda.time.Chronology
    public DurationField eras() {
        return UnsupportedDurationField.getInstance(DurationFieldType.eras());
    }

    @Override // org.joda.time.Chronology
    public DateTimeField era() {
        return UnsupportedDateTimeField.getInstance(DateTimeFieldType.era(), eras());
    }
}
