package org.joda.time.format;

import com.google.android.gms.appstate.AppStateClient;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
/* loaded from: classes.dex */
public class DateTimeFormatter {
    private final Chronology iChrono;
    private final int iDefaultYear;
    private final Locale iLocale;
    private final boolean iOffsetParsed;
    private final InternalParser iParser;
    private final Integer iPivotYear;
    private final InternalPrinter iPrinter;
    private final DateTimeZone iZone;

    public DateTimeFormatter(DateTimePrinter dateTimePrinter, DateTimeParser dateTimeParser) {
        this(DateTimePrinterInternalPrinter.of(dateTimePrinter), DateTimeParserInternalParser.of(dateTimeParser));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DateTimeFormatter(InternalPrinter internalPrinter, InternalParser internalParser) {
        this.iPrinter = internalPrinter;
        this.iParser = internalParser;
        this.iLocale = null;
        this.iOffsetParsed = false;
        this.iChrono = null;
        this.iZone = null;
        this.iPivotYear = null;
        this.iDefaultYear = AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION;
    }

    private DateTimeFormatter(InternalPrinter internalPrinter, InternalParser internalParser, Locale locale, boolean z, Chronology chronology, DateTimeZone dateTimeZone, Integer num, int i) {
        this.iPrinter = internalPrinter;
        this.iParser = internalParser;
        this.iLocale = locale;
        this.iOffsetParsed = z;
        this.iChrono = chronology;
        this.iZone = dateTimeZone;
        this.iPivotYear = num;
        this.iDefaultYear = i;
    }

    public boolean isPrinter() {
        return this.iPrinter != null;
    }

    public DateTimePrinter getPrinter() {
        return InternalPrinterDateTimePrinter.of(this.iPrinter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InternalPrinter getPrinter0() {
        return this.iPrinter;
    }

    public boolean isParser() {
        return this.iParser != null;
    }

    public DateTimeParser getParser() {
        return InternalParserDateTimeParser.of(this.iParser);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InternalParser getParser0() {
        return this.iParser;
    }

    public DateTimeFormatter withLocale(Locale locale) {
        return (locale == getLocale() || (locale != null && locale.equals(getLocale()))) ? this : new DateTimeFormatter(this.iPrinter, this.iParser, locale, this.iOffsetParsed, this.iChrono, this.iZone, this.iPivotYear, this.iDefaultYear);
    }

    public Locale getLocale() {
        return this.iLocale;
    }

    public DateTimeFormatter withOffsetParsed() {
        return this.iOffsetParsed ? this : new DateTimeFormatter(this.iPrinter, this.iParser, this.iLocale, true, this.iChrono, null, this.iPivotYear, this.iDefaultYear);
    }

    public boolean isOffsetParsed() {
        return this.iOffsetParsed;
    }

    public DateTimeFormatter withChronology(Chronology chronology) {
        return this.iChrono == chronology ? this : new DateTimeFormatter(this.iPrinter, this.iParser, this.iLocale, this.iOffsetParsed, chronology, this.iZone, this.iPivotYear, this.iDefaultYear);
    }

    public Chronology getChronology() {
        return this.iChrono;
    }

    @Deprecated
    public Chronology getChronolgy() {
        return this.iChrono;
    }

    public DateTimeFormatter withZoneUTC() {
        return withZone(DateTimeZone.UTC);
    }

    public DateTimeFormatter withZone(DateTimeZone dateTimeZone) {
        return this.iZone == dateTimeZone ? this : new DateTimeFormatter(this.iPrinter, this.iParser, this.iLocale, false, this.iChrono, dateTimeZone, this.iPivotYear, this.iDefaultYear);
    }

    public DateTimeZone getZone() {
        return this.iZone;
    }

    public DateTimeFormatter withPivotYear(Integer num) {
        return (this.iPivotYear == num || (this.iPivotYear != null && this.iPivotYear.equals(num))) ? this : new DateTimeFormatter(this.iPrinter, this.iParser, this.iLocale, this.iOffsetParsed, this.iChrono, this.iZone, num, this.iDefaultYear);
    }

    public DateTimeFormatter withPivotYear(int i) {
        return withPivotYear(Integer.valueOf(i));
    }

    public Integer getPivotYear() {
        return this.iPivotYear;
    }

    public DateTimeFormatter withDefaultYear(int i) {
        return new DateTimeFormatter(this.iPrinter, this.iParser, this.iLocale, this.iOffsetParsed, this.iChrono, this.iZone, this.iPivotYear, i);
    }

    public int getDefaultYear() {
        return this.iDefaultYear;
    }

    public void printTo(StringBuffer stringBuffer, ReadableInstant readableInstant) {
        try {
            printTo((Appendable) stringBuffer, readableInstant);
        } catch (IOException e) {
        }
    }

    public void printTo(Writer writer, ReadableInstant readableInstant) throws IOException {
        printTo((Appendable) writer, readableInstant);
    }

    public void printTo(Appendable appendable, ReadableInstant readableInstant) throws IOException {
        printTo(appendable, DateTimeUtils.getInstantMillis(readableInstant), DateTimeUtils.getInstantChronology(readableInstant));
    }

    public void printTo(StringBuffer stringBuffer, long j) {
        try {
            printTo((Appendable) stringBuffer, j);
        } catch (IOException e) {
        }
    }

    public void printTo(Writer writer, long j) throws IOException {
        printTo((Appendable) writer, j);
    }

    public void printTo(Appendable appendable, long j) throws IOException {
        printTo(appendable, j, null);
    }

    public void printTo(StringBuffer stringBuffer, ReadablePartial readablePartial) {
        try {
            printTo((Appendable) stringBuffer, readablePartial);
        } catch (IOException e) {
        }
    }

    public void printTo(Writer writer, ReadablePartial readablePartial) throws IOException {
        printTo((Appendable) writer, readablePartial);
    }

    public void printTo(Appendable appendable, ReadablePartial readablePartial) throws IOException {
        InternalPrinter requirePrinter = requirePrinter();
        if (readablePartial == null) {
            throw new IllegalArgumentException("The partial must not be null");
        }
        requirePrinter.printTo(appendable, readablePartial, this.iLocale);
    }

    public String print(ReadableInstant readableInstant) {
        StringBuilder sb = new StringBuilder(requirePrinter().estimatePrintedLength());
        try {
            printTo(sb, readableInstant);
        } catch (IOException e) {
        }
        return sb.toString();
    }

    public String print(long j) {
        StringBuilder sb = new StringBuilder(requirePrinter().estimatePrintedLength());
        try {
            printTo(sb, j);
        } catch (IOException e) {
        }
        return sb.toString();
    }

    public String print(ReadablePartial readablePartial) {
        StringBuilder sb = new StringBuilder(requirePrinter().estimatePrintedLength());
        try {
            printTo(sb, readablePartial);
        } catch (IOException e) {
        }
        return sb.toString();
    }

    private void printTo(Appendable appendable, long j, Chronology chronology) throws IOException {
        InternalPrinter requirePrinter = requirePrinter();
        Chronology selectChronology = selectChronology(chronology);
        DateTimeZone zone = selectChronology.getZone();
        int offset = zone.getOffset(j);
        long j2 = offset + j;
        if ((j ^ j2) < 0 && (offset ^ j) >= 0) {
            zone = DateTimeZone.UTC;
            offset = 0;
            j2 = j;
        }
        requirePrinter.printTo(appendable, j2, selectChronology.withUTC(), offset, zone, this.iLocale);
    }

    private InternalPrinter requirePrinter() {
        InternalPrinter internalPrinter = this.iPrinter;
        if (internalPrinter == null) {
            throw new UnsupportedOperationException("Printing not supported");
        }
        return internalPrinter;
    }

    public int parseInto(ReadWritableInstant readWritableInstant, String str, int i) {
        InternalParser requireParser = requireParser();
        if (readWritableInstant == null) {
            throw new IllegalArgumentException("Instant must not be null");
        }
        long millis = readWritableInstant.getMillis();
        Chronology chronology = readWritableInstant.getChronology();
        int i2 = DateTimeUtils.getChronology(chronology).year().get(millis);
        Chronology selectChronology = selectChronology(chronology);
        DateTimeParserBucket dateTimeParserBucket = new DateTimeParserBucket(millis + chronology.getZone().getOffset(millis), selectChronology, this.iLocale, this.iPivotYear, i2);
        int parseInto = requireParser.parseInto(dateTimeParserBucket, str, i);
        readWritableInstant.setMillis(dateTimeParserBucket.computeMillis(false, str));
        if (this.iOffsetParsed && dateTimeParserBucket.getOffsetInteger() != null) {
            selectChronology = selectChronology.withZone(DateTimeZone.forOffsetMillis(dateTimeParserBucket.getOffsetInteger().intValue()));
        } else if (dateTimeParserBucket.getZone() != null) {
            selectChronology = selectChronology.withZone(dateTimeParserBucket.getZone());
        }
        readWritableInstant.setChronology(selectChronology);
        if (this.iZone != null) {
            readWritableInstant.setZone(this.iZone);
        }
        return parseInto;
    }

    public long parseMillis(String str) {
        return new DateTimeParserBucket(0L, selectChronology(this.iChrono), this.iLocale, this.iPivotYear, this.iDefaultYear).doParseMillis(requireParser(), str);
    }

    public LocalDate parseLocalDate(String str) {
        return parseLocalDateTime(str).toLocalDate();
    }

    public LocalTime parseLocalTime(String str) {
        return parseLocalDateTime(str).toLocalTime();
    }

    public LocalDateTime parseLocalDateTime(String str) {
        int i;
        InternalParser requireParser = requireParser();
        Chronology withUTC = selectChronology(null).withUTC();
        DateTimeParserBucket dateTimeParserBucket = new DateTimeParserBucket(0L, withUTC, this.iLocale, this.iPivotYear, this.iDefaultYear);
        int parseInto = requireParser.parseInto(dateTimeParserBucket, str, 0);
        if (parseInto >= 0) {
            if (parseInto >= str.length()) {
                long computeMillis = dateTimeParserBucket.computeMillis(true, str);
                if (dateTimeParserBucket.getOffsetInteger() != null) {
                    withUTC = withUTC.withZone(DateTimeZone.forOffsetMillis(dateTimeParserBucket.getOffsetInteger().intValue()));
                } else if (dateTimeParserBucket.getZone() != null) {
                    withUTC = withUTC.withZone(dateTimeParserBucket.getZone());
                }
                return new LocalDateTime(computeMillis, withUTC);
            }
            i = parseInto;
        } else {
            i = parseInto ^ (-1);
        }
        throw new IllegalArgumentException(FormatUtils.createErrorMessage(str, i));
    }

    public DateTime parseDateTime(String str) {
        int i;
        InternalParser requireParser = requireParser();
        Chronology selectChronology = selectChronology(null);
        DateTimeParserBucket dateTimeParserBucket = new DateTimeParserBucket(0L, selectChronology, this.iLocale, this.iPivotYear, this.iDefaultYear);
        int parseInto = requireParser.parseInto(dateTimeParserBucket, str, 0);
        if (parseInto >= 0) {
            if (parseInto >= str.length()) {
                long computeMillis = dateTimeParserBucket.computeMillis(true, str);
                if (this.iOffsetParsed && dateTimeParserBucket.getOffsetInteger() != null) {
                    selectChronology = selectChronology.withZone(DateTimeZone.forOffsetMillis(dateTimeParserBucket.getOffsetInteger().intValue()));
                } else if (dateTimeParserBucket.getZone() != null) {
                    selectChronology = selectChronology.withZone(dateTimeParserBucket.getZone());
                }
                DateTime dateTime = new DateTime(computeMillis, selectChronology);
                if (this.iZone != null) {
                    return dateTime.withZone(this.iZone);
                }
                return dateTime;
            }
            i = parseInto;
        } else {
            i = parseInto ^ (-1);
        }
        throw new IllegalArgumentException(FormatUtils.createErrorMessage(str, i));
    }

    public MutableDateTime parseMutableDateTime(String str) {
        int i;
        InternalParser requireParser = requireParser();
        Chronology selectChronology = selectChronology(null);
        DateTimeParserBucket dateTimeParserBucket = new DateTimeParserBucket(0L, selectChronology, this.iLocale, this.iPivotYear, this.iDefaultYear);
        int parseInto = requireParser.parseInto(dateTimeParserBucket, str, 0);
        if (parseInto >= 0) {
            if (parseInto >= str.length()) {
                long computeMillis = dateTimeParserBucket.computeMillis(true, str);
                if (this.iOffsetParsed && dateTimeParserBucket.getOffsetInteger() != null) {
                    selectChronology = selectChronology.withZone(DateTimeZone.forOffsetMillis(dateTimeParserBucket.getOffsetInteger().intValue()));
                } else if (dateTimeParserBucket.getZone() != null) {
                    selectChronology = selectChronology.withZone(dateTimeParserBucket.getZone());
                }
                MutableDateTime mutableDateTime = new MutableDateTime(computeMillis, selectChronology);
                if (this.iZone != null) {
                    mutableDateTime.setZone(this.iZone);
                }
                return mutableDateTime;
            }
            i = parseInto;
        } else {
            i = parseInto ^ (-1);
        }
        throw new IllegalArgumentException(FormatUtils.createErrorMessage(str, i));
    }

    private InternalParser requireParser() {
        InternalParser internalParser = this.iParser;
        if (internalParser == null) {
            throw new UnsupportedOperationException("Parsing not supported");
        }
        return internalParser;
    }

    private Chronology selectChronology(Chronology chronology) {
        Chronology chronology2 = DateTimeUtils.getChronology(chronology);
        if (this.iChrono != null) {
            chronology2 = this.iChrono;
        }
        if (this.iZone != null) {
            return chronology2.withZone(this.iZone);
        }
        return chronology2;
    }
}
