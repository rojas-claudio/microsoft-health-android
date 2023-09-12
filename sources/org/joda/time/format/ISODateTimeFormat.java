package org.joda.time.format;

import java.util.Collection;
import java.util.HashSet;
import org.apache.commons.lang3.ClassUtils;
import org.joda.time.DateTimeFieldType;
/* loaded from: classes.dex */
public class ISODateTimeFormat {
    protected ISODateTimeFormat() {
    }

    public static DateTimeFormatter forFields(Collection<DateTimeFieldType> collection, boolean z, boolean z2) {
        boolean z3;
        if (collection == null || collection.size() == 0) {
            throw new IllegalArgumentException("The fields must not be null or empty");
        }
        HashSet hashSet = new HashSet(collection);
        int size = hashSet.size();
        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder();
        if (hashSet.contains(DateTimeFieldType.monthOfYear())) {
            z3 = dateByMonth(dateTimeFormatterBuilder, hashSet, z, z2);
        } else if (hashSet.contains(DateTimeFieldType.dayOfYear())) {
            z3 = dateByOrdinal(dateTimeFormatterBuilder, hashSet, z, z2);
        } else if (hashSet.contains(DateTimeFieldType.weekOfWeekyear())) {
            z3 = dateByWeek(dateTimeFormatterBuilder, hashSet, z, z2);
        } else if (hashSet.contains(DateTimeFieldType.dayOfMonth())) {
            z3 = dateByMonth(dateTimeFormatterBuilder, hashSet, z, z2);
        } else if (hashSet.contains(DateTimeFieldType.dayOfWeek())) {
            z3 = dateByWeek(dateTimeFormatterBuilder, hashSet, z, z2);
        } else if (!hashSet.remove(DateTimeFieldType.year())) {
            if (hashSet.remove(DateTimeFieldType.weekyear())) {
                dateTimeFormatterBuilder.append(Constants.we);
                z3 = true;
            } else {
                z3 = false;
            }
        } else {
            dateTimeFormatterBuilder.append(Constants.ye);
            z3 = true;
        }
        time(dateTimeFormatterBuilder, hashSet, z, z2, z3, hashSet.size() < size);
        if (!dateTimeFormatterBuilder.canBuildFormatter()) {
            throw new IllegalArgumentException("No valid format for fields: " + collection);
        }
        try {
            collection.retainAll(hashSet);
        } catch (UnsupportedOperationException e) {
        }
        return dateTimeFormatterBuilder.toFormatter();
    }

    private static boolean dateByMonth(DateTimeFormatterBuilder dateTimeFormatterBuilder, Collection<DateTimeFieldType> collection, boolean z, boolean z2) {
        if (!collection.remove(DateTimeFieldType.year())) {
            if (collection.remove(DateTimeFieldType.monthOfYear())) {
                dateTimeFormatterBuilder.appendLiteral('-');
                dateTimeFormatterBuilder.appendLiteral('-');
                dateTimeFormatterBuilder.appendMonthOfYear(2);
                if (collection.remove(DateTimeFieldType.dayOfMonth())) {
                    appendSeparator(dateTimeFormatterBuilder, z);
                    dateTimeFormatterBuilder.appendDayOfMonth(2);
                    return false;
                }
                return true;
            } else if (!collection.remove(DateTimeFieldType.dayOfMonth())) {
                return false;
            } else {
                dateTimeFormatterBuilder.appendLiteral('-');
                dateTimeFormatterBuilder.appendLiteral('-');
                dateTimeFormatterBuilder.appendLiteral('-');
                dateTimeFormatterBuilder.appendDayOfMonth(2);
                return false;
            }
        }
        dateTimeFormatterBuilder.append(Constants.ye);
        if (collection.remove(DateTimeFieldType.monthOfYear())) {
            if (collection.remove(DateTimeFieldType.dayOfMonth())) {
                appendSeparator(dateTimeFormatterBuilder, z);
                dateTimeFormatterBuilder.appendMonthOfYear(2);
                appendSeparator(dateTimeFormatterBuilder, z);
                dateTimeFormatterBuilder.appendDayOfMonth(2);
                return false;
            }
            dateTimeFormatterBuilder.appendLiteral('-');
            dateTimeFormatterBuilder.appendMonthOfYear(2);
            return true;
        } else if (collection.remove(DateTimeFieldType.dayOfMonth())) {
            checkNotStrictISO(collection, z2);
            dateTimeFormatterBuilder.appendLiteral('-');
            dateTimeFormatterBuilder.appendLiteral('-');
            dateTimeFormatterBuilder.appendDayOfMonth(2);
            return false;
        } else {
            return true;
        }
    }

    private static boolean dateByOrdinal(DateTimeFormatterBuilder dateTimeFormatterBuilder, Collection<DateTimeFieldType> collection, boolean z, boolean z2) {
        if (!collection.remove(DateTimeFieldType.year())) {
            if (!collection.remove(DateTimeFieldType.dayOfYear())) {
                return false;
            }
            dateTimeFormatterBuilder.appendLiteral('-');
            dateTimeFormatterBuilder.appendDayOfYear(3);
            return false;
        }
        dateTimeFormatterBuilder.append(Constants.ye);
        if (collection.remove(DateTimeFieldType.dayOfYear())) {
            appendSeparator(dateTimeFormatterBuilder, z);
            dateTimeFormatterBuilder.appendDayOfYear(3);
            return false;
        }
        return true;
    }

    private static boolean dateByWeek(DateTimeFormatterBuilder dateTimeFormatterBuilder, Collection<DateTimeFieldType> collection, boolean z, boolean z2) {
        if (!collection.remove(DateTimeFieldType.weekyear())) {
            if (collection.remove(DateTimeFieldType.weekOfWeekyear())) {
                dateTimeFormatterBuilder.appendLiteral('-');
                dateTimeFormatterBuilder.appendLiteral('W');
                dateTimeFormatterBuilder.appendWeekOfWeekyear(2);
                if (collection.remove(DateTimeFieldType.dayOfWeek())) {
                    appendSeparator(dateTimeFormatterBuilder, z);
                    dateTimeFormatterBuilder.appendDayOfWeek(1);
                    return false;
                }
                return true;
            } else if (!collection.remove(DateTimeFieldType.dayOfWeek())) {
                return false;
            } else {
                dateTimeFormatterBuilder.appendLiteral('-');
                dateTimeFormatterBuilder.appendLiteral('W');
                dateTimeFormatterBuilder.appendLiteral('-');
                dateTimeFormatterBuilder.appendDayOfWeek(1);
                return false;
            }
        }
        dateTimeFormatterBuilder.append(Constants.we);
        if (collection.remove(DateTimeFieldType.weekOfWeekyear())) {
            appendSeparator(dateTimeFormatterBuilder, z);
            dateTimeFormatterBuilder.appendLiteral('W');
            dateTimeFormatterBuilder.appendWeekOfWeekyear(2);
            if (collection.remove(DateTimeFieldType.dayOfWeek())) {
                appendSeparator(dateTimeFormatterBuilder, z);
                dateTimeFormatterBuilder.appendDayOfWeek(1);
                return false;
            }
            return true;
        } else if (collection.remove(DateTimeFieldType.dayOfWeek())) {
            checkNotStrictISO(collection, z2);
            appendSeparator(dateTimeFormatterBuilder, z);
            dateTimeFormatterBuilder.appendLiteral('W');
            dateTimeFormatterBuilder.appendLiteral('-');
            dateTimeFormatterBuilder.appendDayOfWeek(1);
            return false;
        } else {
            return true;
        }
    }

    private static void time(DateTimeFormatterBuilder dateTimeFormatterBuilder, Collection<DateTimeFieldType> collection, boolean z, boolean z2, boolean z3, boolean z4) {
        boolean remove = collection.remove(DateTimeFieldType.hourOfDay());
        boolean remove2 = collection.remove(DateTimeFieldType.minuteOfHour());
        boolean remove3 = collection.remove(DateTimeFieldType.secondOfMinute());
        boolean remove4 = collection.remove(DateTimeFieldType.millisOfSecond());
        if (remove || remove2 || remove3 || remove4) {
            if (remove || remove2 || remove3 || remove4) {
                if (z2 && z3) {
                    throw new IllegalArgumentException("No valid ISO8601 format for fields because Date was reduced precision: " + collection);
                }
                if (z4) {
                    dateTimeFormatterBuilder.appendLiteral('T');
                }
            }
            if ((!remove || !remove2 || !remove3) && (!remove || remove3 || remove4)) {
                if (z2 && z4) {
                    throw new IllegalArgumentException("No valid ISO8601 format for fields because Time was truncated: " + collection);
                }
                if ((remove || ((!remove2 || !remove3) && ((!remove2 || remove4) && !remove3))) && z2) {
                    throw new IllegalArgumentException("No valid ISO8601 format for fields: " + collection);
                }
            }
            if (remove) {
                dateTimeFormatterBuilder.appendHourOfDay(2);
            } else if (remove2 || remove3 || remove4) {
                dateTimeFormatterBuilder.appendLiteral('-');
            }
            if (z && remove && remove2) {
                dateTimeFormatterBuilder.appendLiteral(':');
            }
            if (remove2) {
                dateTimeFormatterBuilder.appendMinuteOfHour(2);
            } else if (remove3 || remove4) {
                dateTimeFormatterBuilder.appendLiteral('-');
            }
            if (z && remove2 && remove3) {
                dateTimeFormatterBuilder.appendLiteral(':');
            }
            if (remove3) {
                dateTimeFormatterBuilder.appendSecondOfMinute(2);
            } else if (remove4) {
                dateTimeFormatterBuilder.appendLiteral('-');
            }
            if (remove4) {
                dateTimeFormatterBuilder.appendLiteral(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                dateTimeFormatterBuilder.appendMillisOfSecond(3);
            }
        }
    }

    private static void checkNotStrictISO(Collection<DateTimeFieldType> collection, boolean z) {
        if (z) {
            throw new IllegalArgumentException("No valid ISO8601 format for fields: " + collection);
        }
    }

    private static void appendSeparator(DateTimeFormatterBuilder dateTimeFormatterBuilder, boolean z) {
        if (z) {
            dateTimeFormatterBuilder.appendLiteral('-');
        }
    }

    public static DateTimeFormatter dateParser() {
        return Constants.dp;
    }

    public static DateTimeFormatter localDateParser() {
        return Constants.ldp;
    }

    public static DateTimeFormatter dateElementParser() {
        return Constants.dpe;
    }

    public static DateTimeFormatter timeParser() {
        return Constants.tp;
    }

    public static DateTimeFormatter localTimeParser() {
        return Constants.ltp;
    }

    public static DateTimeFormatter timeElementParser() {
        return Constants.tpe;
    }

    public static DateTimeFormatter dateTimeParser() {
        return Constants.dtp;
    }

    public static DateTimeFormatter dateOptionalTimeParser() {
        return Constants.dotp;
    }

    public static DateTimeFormatter localDateOptionalTimeParser() {
        return Constants.ldotp;
    }

    public static DateTimeFormatter date() {
        return yearMonthDay();
    }

    public static DateTimeFormatter time() {
        return Constants.t;
    }

    public static DateTimeFormatter timeNoMillis() {
        return Constants.tx;
    }

    public static DateTimeFormatter tTime() {
        return Constants.tt;
    }

    public static DateTimeFormatter tTimeNoMillis() {
        return Constants.ttx;
    }

    public static DateTimeFormatter dateTime() {
        return Constants.dt;
    }

    public static DateTimeFormatter dateTimeNoMillis() {
        return Constants.dtx;
    }

    public static DateTimeFormatter ordinalDate() {
        return Constants.od;
    }

    public static DateTimeFormatter ordinalDateTime() {
        return Constants.odt;
    }

    public static DateTimeFormatter ordinalDateTimeNoMillis() {
        return Constants.odtx;
    }

    public static DateTimeFormatter weekDate() {
        return Constants.wwd;
    }

    public static DateTimeFormatter weekDateTime() {
        return Constants.wdt;
    }

    public static DateTimeFormatter weekDateTimeNoMillis() {
        return Constants.wdtx;
    }

    public static DateTimeFormatter basicDate() {
        return Constants.bd;
    }

    public static DateTimeFormatter basicTime() {
        return Constants.bt;
    }

    public static DateTimeFormatter basicTimeNoMillis() {
        return Constants.btx;
    }

    public static DateTimeFormatter basicTTime() {
        return Constants.btt;
    }

    public static DateTimeFormatter basicTTimeNoMillis() {
        return Constants.bttx;
    }

    public static DateTimeFormatter basicDateTime() {
        return Constants.bdt;
    }

    public static DateTimeFormatter basicDateTimeNoMillis() {
        return Constants.bdtx;
    }

    public static DateTimeFormatter basicOrdinalDate() {
        return Constants.bod;
    }

    public static DateTimeFormatter basicOrdinalDateTime() {
        return Constants.bodt;
    }

    public static DateTimeFormatter basicOrdinalDateTimeNoMillis() {
        return Constants.bodtx;
    }

    public static DateTimeFormatter basicWeekDate() {
        return Constants.bwd;
    }

    public static DateTimeFormatter basicWeekDateTime() {
        return Constants.bwdt;
    }

    public static DateTimeFormatter basicWeekDateTimeNoMillis() {
        return Constants.bwdtx;
    }

    public static DateTimeFormatter year() {
        return Constants.ye;
    }

    public static DateTimeFormatter yearMonth() {
        return Constants.ym;
    }

    public static DateTimeFormatter yearMonthDay() {
        return Constants.ymd;
    }

    public static DateTimeFormatter weekyear() {
        return Constants.we;
    }

    public static DateTimeFormatter weekyearWeek() {
        return Constants.ww;
    }

    public static DateTimeFormatter weekyearWeekDay() {
        return Constants.wwd;
    }

    public static DateTimeFormatter hour() {
        return Constants.hde;
    }

    public static DateTimeFormatter hourMinute() {
        return Constants.hm;
    }

    public static DateTimeFormatter hourMinuteSecond() {
        return Constants.hms;
    }

    public static DateTimeFormatter hourMinuteSecondMillis() {
        return Constants.hmsl;
    }

    public static DateTimeFormatter hourMinuteSecondFraction() {
        return Constants.hmsf;
    }

    public static DateTimeFormatter dateHour() {
        return Constants.dh;
    }

    public static DateTimeFormatter dateHourMinute() {
        return Constants.dhm;
    }

    public static DateTimeFormatter dateHourMinuteSecond() {
        return Constants.dhms;
    }

    public static DateTimeFormatter dateHourMinuteSecondMillis() {
        return Constants.dhmsl;
    }

    public static DateTimeFormatter dateHourMinuteSecondFraction() {
        return Constants.dhmsf;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class Constants {
        private static final DateTimeFormatter ye = yearElement();
        private static final DateTimeFormatter mye = monthElement();
        private static final DateTimeFormatter dme = dayOfMonthElement();
        private static final DateTimeFormatter we = weekyearElement();
        private static final DateTimeFormatter wwe = weekElement();
        private static final DateTimeFormatter dwe = dayOfWeekElement();
        private static final DateTimeFormatter dye = dayOfYearElement();
        private static final DateTimeFormatter hde = hourElement();
        private static final DateTimeFormatter mhe = minuteElement();
        private static final DateTimeFormatter sme = secondElement();
        private static final DateTimeFormatter fse = fractionElement();
        private static final DateTimeFormatter ze = offsetElement();
        private static final DateTimeFormatter lte = literalTElement();
        private static final DateTimeFormatter ym = yearMonth();
        private static final DateTimeFormatter ymd = yearMonthDay();
        private static final DateTimeFormatter ww = weekyearWeek();
        private static final DateTimeFormatter wwd = weekyearWeekDay();
        private static final DateTimeFormatter hm = hourMinute();
        private static final DateTimeFormatter hms = hourMinuteSecond();
        private static final DateTimeFormatter hmsl = hourMinuteSecondMillis();
        private static final DateTimeFormatter hmsf = hourMinuteSecondFraction();
        private static final DateTimeFormatter dh = dateHour();
        private static final DateTimeFormatter dhm = dateHourMinute();
        private static final DateTimeFormatter dhms = dateHourMinuteSecond();
        private static final DateTimeFormatter dhmsl = dateHourMinuteSecondMillis();
        private static final DateTimeFormatter dhmsf = dateHourMinuteSecondFraction();
        private static final DateTimeFormatter t = time();
        private static final DateTimeFormatter tx = timeNoMillis();
        private static final DateTimeFormatter tt = tTime();
        private static final DateTimeFormatter ttx = tTimeNoMillis();
        private static final DateTimeFormatter dt = dateTime();
        private static final DateTimeFormatter dtx = dateTimeNoMillis();
        private static final DateTimeFormatter wdt = weekDateTime();
        private static final DateTimeFormatter wdtx = weekDateTimeNoMillis();
        private static final DateTimeFormatter od = ordinalDate();
        private static final DateTimeFormatter odt = ordinalDateTime();
        private static final DateTimeFormatter odtx = ordinalDateTimeNoMillis();
        private static final DateTimeFormatter bd = basicDate();
        private static final DateTimeFormatter bt = basicTime();
        private static final DateTimeFormatter btx = basicTimeNoMillis();
        private static final DateTimeFormatter btt = basicTTime();
        private static final DateTimeFormatter bttx = basicTTimeNoMillis();
        private static final DateTimeFormatter bdt = basicDateTime();
        private static final DateTimeFormatter bdtx = basicDateTimeNoMillis();
        private static final DateTimeFormatter bod = basicOrdinalDate();
        private static final DateTimeFormatter bodt = basicOrdinalDateTime();
        private static final DateTimeFormatter bodtx = basicOrdinalDateTimeNoMillis();
        private static final DateTimeFormatter bwd = basicWeekDate();
        private static final DateTimeFormatter bwdt = basicWeekDateTime();
        private static final DateTimeFormatter bwdtx = basicWeekDateTimeNoMillis();
        private static final DateTimeFormatter dpe = dateElementParser();
        private static final DateTimeFormatter tpe = timeElementParser();
        private static final DateTimeFormatter dp = dateParser();
        private static final DateTimeFormatter ldp = localDateParser();
        private static final DateTimeFormatter tp = timeParser();
        private static final DateTimeFormatter ltp = localTimeParser();
        private static final DateTimeFormatter dtp = dateTimeParser();
        private static final DateTimeFormatter dotp = dateOptionalTimeParser();
        private static final DateTimeFormatter ldotp = localDateOptionalTimeParser();

        Constants() {
        }

        private static DateTimeFormatter dateParser() {
            if (dp == null) {
                return new DateTimeFormatterBuilder().append(dateElementParser()).appendOptional(new DateTimeFormatterBuilder().appendLiteral('T').append(offsetElement()).toParser()).toFormatter();
            }
            return dp;
        }

        private static DateTimeFormatter localDateParser() {
            return ldp == null ? dateElementParser().withZoneUTC() : ldp;
        }

        private static DateTimeFormatter dateElementParser() {
            return dpe == null ? new DateTimeFormatterBuilder().append((DateTimePrinter) null, new DateTimeParser[]{new DateTimeFormatterBuilder().append(yearElement()).appendOptional(new DateTimeFormatterBuilder().append(monthElement()).appendOptional(dayOfMonthElement().getParser()).toParser()).toParser(), new DateTimeFormatterBuilder().append(weekyearElement()).append(weekElement()).appendOptional(dayOfWeekElement().getParser()).toParser(), new DateTimeFormatterBuilder().append(yearElement()).append(dayOfYearElement()).toParser()}).toFormatter() : dpe;
        }

        private static DateTimeFormatter timeParser() {
            return tp == null ? new DateTimeFormatterBuilder().appendOptional(literalTElement().getParser()).append(timeElementParser()).appendOptional(offsetElement().getParser()).toFormatter() : tp;
        }

        private static DateTimeFormatter localTimeParser() {
            return ltp == null ? new DateTimeFormatterBuilder().appendOptional(literalTElement().getParser()).append(timeElementParser()).toFormatter().withZoneUTC() : ltp;
        }

        private static DateTimeFormatter timeElementParser() {
            if (tpe == null) {
                DateTimeParser parser = new DateTimeFormatterBuilder().append((DateTimePrinter) null, new DateTimeParser[]{new DateTimeFormatterBuilder().appendLiteral(ClassUtils.PACKAGE_SEPARATOR_CHAR).toParser(), new DateTimeFormatterBuilder().appendLiteral(',').toParser()}).toParser();
                return new DateTimeFormatterBuilder().append(hourElement()).append((DateTimePrinter) null, new DateTimeParser[]{new DateTimeFormatterBuilder().append(minuteElement()).append((DateTimePrinter) null, new DateTimeParser[]{new DateTimeFormatterBuilder().append(secondElement()).appendOptional(new DateTimeFormatterBuilder().append(parser).appendFractionOfSecond(1, 9).toParser()).toParser(), new DateTimeFormatterBuilder().append(parser).appendFractionOfMinute(1, 9).toParser(), null}).toParser(), new DateTimeFormatterBuilder().append(parser).appendFractionOfHour(1, 9).toParser(), null}).toFormatter();
            }
            return tpe;
        }

        private static DateTimeFormatter dateTimeParser() {
            if (dtp == null) {
                return new DateTimeFormatterBuilder().append((DateTimePrinter) null, new DateTimeParser[]{new DateTimeFormatterBuilder().appendLiteral('T').append(timeElementParser()).appendOptional(offsetElement().getParser()).toParser(), dateOptionalTimeParser().getParser()}).toFormatter();
            }
            return dtp;
        }

        private static DateTimeFormatter dateOptionalTimeParser() {
            if (dotp == null) {
                return new DateTimeFormatterBuilder().append(dateElementParser()).appendOptional(new DateTimeFormatterBuilder().appendLiteral('T').appendOptional(timeElementParser().getParser()).appendOptional(offsetElement().getParser()).toParser()).toFormatter();
            }
            return dotp;
        }

        private static DateTimeFormatter localDateOptionalTimeParser() {
            if (ldotp == null) {
                return new DateTimeFormatterBuilder().append(dateElementParser()).appendOptional(new DateTimeFormatterBuilder().appendLiteral('T').append(timeElementParser()).toParser()).toFormatter().withZoneUTC();
            }
            return ldotp;
        }

        private static DateTimeFormatter time() {
            return t == null ? new DateTimeFormatterBuilder().append(hourMinuteSecondFraction()).append(offsetElement()).toFormatter() : t;
        }

        private static DateTimeFormatter timeNoMillis() {
            return tx == null ? new DateTimeFormatterBuilder().append(hourMinuteSecond()).append(offsetElement()).toFormatter() : tx;
        }

        private static DateTimeFormatter tTime() {
            return tt == null ? new DateTimeFormatterBuilder().append(literalTElement()).append(time()).toFormatter() : tt;
        }

        private static DateTimeFormatter tTimeNoMillis() {
            return ttx == null ? new DateTimeFormatterBuilder().append(literalTElement()).append(timeNoMillis()).toFormatter() : ttx;
        }

        private static DateTimeFormatter dateTime() {
            return dt == null ? new DateTimeFormatterBuilder().append(ISODateTimeFormat.date()).append(tTime()).toFormatter() : dt;
        }

        private static DateTimeFormatter dateTimeNoMillis() {
            return dtx == null ? new DateTimeFormatterBuilder().append(ISODateTimeFormat.date()).append(tTimeNoMillis()).toFormatter() : dtx;
        }

        private static DateTimeFormatter ordinalDate() {
            return od == null ? new DateTimeFormatterBuilder().append(yearElement()).append(dayOfYearElement()).toFormatter() : od;
        }

        private static DateTimeFormatter ordinalDateTime() {
            return odt == null ? new DateTimeFormatterBuilder().append(ordinalDate()).append(tTime()).toFormatter() : odt;
        }

        private static DateTimeFormatter ordinalDateTimeNoMillis() {
            return odtx == null ? new DateTimeFormatterBuilder().append(ordinalDate()).append(tTimeNoMillis()).toFormatter() : odtx;
        }

        private static DateTimeFormatter weekDateTime() {
            return wdt == null ? new DateTimeFormatterBuilder().append(ISODateTimeFormat.weekDate()).append(tTime()).toFormatter() : wdt;
        }

        private static DateTimeFormatter weekDateTimeNoMillis() {
            return wdtx == null ? new DateTimeFormatterBuilder().append(ISODateTimeFormat.weekDate()).append(tTimeNoMillis()).toFormatter() : wdtx;
        }

        private static DateTimeFormatter basicDate() {
            return bd == null ? new DateTimeFormatterBuilder().appendYear(4, 4).appendFixedDecimal(DateTimeFieldType.monthOfYear(), 2).appendFixedDecimal(DateTimeFieldType.dayOfMonth(), 2).toFormatter() : bd;
        }

        private static DateTimeFormatter basicTime() {
            return bt == null ? new DateTimeFormatterBuilder().appendFixedDecimal(DateTimeFieldType.hourOfDay(), 2).appendFixedDecimal(DateTimeFieldType.minuteOfHour(), 2).appendFixedDecimal(DateTimeFieldType.secondOfMinute(), 2).appendLiteral(ClassUtils.PACKAGE_SEPARATOR_CHAR).appendFractionOfSecond(3, 9).appendTimeZoneOffset("Z", false, 2, 2).toFormatter() : bt;
        }

        private static DateTimeFormatter basicTimeNoMillis() {
            return btx == null ? new DateTimeFormatterBuilder().appendFixedDecimal(DateTimeFieldType.hourOfDay(), 2).appendFixedDecimal(DateTimeFieldType.minuteOfHour(), 2).appendFixedDecimal(DateTimeFieldType.secondOfMinute(), 2).appendTimeZoneOffset("Z", false, 2, 2).toFormatter() : btx;
        }

        private static DateTimeFormatter basicTTime() {
            return btt == null ? new DateTimeFormatterBuilder().append(literalTElement()).append(basicTime()).toFormatter() : btt;
        }

        private static DateTimeFormatter basicTTimeNoMillis() {
            return bttx == null ? new DateTimeFormatterBuilder().append(literalTElement()).append(basicTimeNoMillis()).toFormatter() : bttx;
        }

        private static DateTimeFormatter basicDateTime() {
            return bdt == null ? new DateTimeFormatterBuilder().append(basicDate()).append(basicTTime()).toFormatter() : bdt;
        }

        private static DateTimeFormatter basicDateTimeNoMillis() {
            return bdtx == null ? new DateTimeFormatterBuilder().append(basicDate()).append(basicTTimeNoMillis()).toFormatter() : bdtx;
        }

        private static DateTimeFormatter basicOrdinalDate() {
            return bod == null ? new DateTimeFormatterBuilder().appendYear(4, 4).appendFixedDecimal(DateTimeFieldType.dayOfYear(), 3).toFormatter() : bod;
        }

        private static DateTimeFormatter basicOrdinalDateTime() {
            return bodt == null ? new DateTimeFormatterBuilder().append(basicOrdinalDate()).append(basicTTime()).toFormatter() : bodt;
        }

        private static DateTimeFormatter basicOrdinalDateTimeNoMillis() {
            return bodtx == null ? new DateTimeFormatterBuilder().append(basicOrdinalDate()).append(basicTTimeNoMillis()).toFormatter() : bodtx;
        }

        private static DateTimeFormatter basicWeekDate() {
            return bwd == null ? new DateTimeFormatterBuilder().appendWeekyear(4, 4).appendLiteral('W').appendFixedDecimal(DateTimeFieldType.weekOfWeekyear(), 2).appendFixedDecimal(DateTimeFieldType.dayOfWeek(), 1).toFormatter() : bwd;
        }

        private static DateTimeFormatter basicWeekDateTime() {
            return bwdt == null ? new DateTimeFormatterBuilder().append(basicWeekDate()).append(basicTTime()).toFormatter() : bwdt;
        }

        private static DateTimeFormatter basicWeekDateTimeNoMillis() {
            return bwdtx == null ? new DateTimeFormatterBuilder().append(basicWeekDate()).append(basicTTimeNoMillis()).toFormatter() : bwdtx;
        }

        private static DateTimeFormatter yearMonth() {
            return ym == null ? new DateTimeFormatterBuilder().append(yearElement()).append(monthElement()).toFormatter() : ym;
        }

        private static DateTimeFormatter yearMonthDay() {
            return ymd == null ? new DateTimeFormatterBuilder().append(yearElement()).append(monthElement()).append(dayOfMonthElement()).toFormatter() : ymd;
        }

        private static DateTimeFormatter weekyearWeek() {
            return ww == null ? new DateTimeFormatterBuilder().append(weekyearElement()).append(weekElement()).toFormatter() : ww;
        }

        private static DateTimeFormatter weekyearWeekDay() {
            return wwd == null ? new DateTimeFormatterBuilder().append(weekyearElement()).append(weekElement()).append(dayOfWeekElement()).toFormatter() : wwd;
        }

        private static DateTimeFormatter hourMinute() {
            return hm == null ? new DateTimeFormatterBuilder().append(hourElement()).append(minuteElement()).toFormatter() : hm;
        }

        private static DateTimeFormatter hourMinuteSecond() {
            return hms == null ? new DateTimeFormatterBuilder().append(hourElement()).append(minuteElement()).append(secondElement()).toFormatter() : hms;
        }

        private static DateTimeFormatter hourMinuteSecondMillis() {
            return hmsl == null ? new DateTimeFormatterBuilder().append(hourElement()).append(minuteElement()).append(secondElement()).appendLiteral(ClassUtils.PACKAGE_SEPARATOR_CHAR).appendFractionOfSecond(3, 3).toFormatter() : hmsl;
        }

        private static DateTimeFormatter hourMinuteSecondFraction() {
            return hmsf == null ? new DateTimeFormatterBuilder().append(hourElement()).append(minuteElement()).append(secondElement()).append(fractionElement()).toFormatter() : hmsf;
        }

        private static DateTimeFormatter dateHour() {
            return dh == null ? new DateTimeFormatterBuilder().append(ISODateTimeFormat.date()).append(literalTElement()).append(ISODateTimeFormat.hour()).toFormatter() : dh;
        }

        private static DateTimeFormatter dateHourMinute() {
            return dhm == null ? new DateTimeFormatterBuilder().append(ISODateTimeFormat.date()).append(literalTElement()).append(hourMinute()).toFormatter() : dhm;
        }

        private static DateTimeFormatter dateHourMinuteSecond() {
            return dhms == null ? new DateTimeFormatterBuilder().append(ISODateTimeFormat.date()).append(literalTElement()).append(hourMinuteSecond()).toFormatter() : dhms;
        }

        private static DateTimeFormatter dateHourMinuteSecondMillis() {
            return dhmsl == null ? new DateTimeFormatterBuilder().append(ISODateTimeFormat.date()).append(literalTElement()).append(hourMinuteSecondMillis()).toFormatter() : dhmsl;
        }

        private static DateTimeFormatter dateHourMinuteSecondFraction() {
            return dhmsf == null ? new DateTimeFormatterBuilder().append(ISODateTimeFormat.date()).append(literalTElement()).append(hourMinuteSecondFraction()).toFormatter() : dhmsf;
        }

        private static DateTimeFormatter yearElement() {
            return ye == null ? new DateTimeFormatterBuilder().appendYear(4, 9).toFormatter() : ye;
        }

        private static DateTimeFormatter monthElement() {
            return mye == null ? new DateTimeFormatterBuilder().appendLiteral('-').appendMonthOfYear(2).toFormatter() : mye;
        }

        private static DateTimeFormatter dayOfMonthElement() {
            return dme == null ? new DateTimeFormatterBuilder().appendLiteral('-').appendDayOfMonth(2).toFormatter() : dme;
        }

        private static DateTimeFormatter weekyearElement() {
            return we == null ? new DateTimeFormatterBuilder().appendWeekyear(4, 9).toFormatter() : we;
        }

        private static DateTimeFormatter weekElement() {
            return wwe == null ? new DateTimeFormatterBuilder().appendLiteral("-W").appendWeekOfWeekyear(2).toFormatter() : wwe;
        }

        private static DateTimeFormatter dayOfWeekElement() {
            return dwe == null ? new DateTimeFormatterBuilder().appendLiteral('-').appendDayOfWeek(1).toFormatter() : dwe;
        }

        private static DateTimeFormatter dayOfYearElement() {
            return dye == null ? new DateTimeFormatterBuilder().appendLiteral('-').appendDayOfYear(3).toFormatter() : dye;
        }

        private static DateTimeFormatter literalTElement() {
            return lte == null ? new DateTimeFormatterBuilder().appendLiteral('T').toFormatter() : lte;
        }

        private static DateTimeFormatter hourElement() {
            return hde == null ? new DateTimeFormatterBuilder().appendHourOfDay(2).toFormatter() : hde;
        }

        private static DateTimeFormatter minuteElement() {
            return mhe == null ? new DateTimeFormatterBuilder().appendLiteral(':').appendMinuteOfHour(2).toFormatter() : mhe;
        }

        private static DateTimeFormatter secondElement() {
            return sme == null ? new DateTimeFormatterBuilder().appendLiteral(':').appendSecondOfMinute(2).toFormatter() : sme;
        }

        private static DateTimeFormatter fractionElement() {
            return fse == null ? new DateTimeFormatterBuilder().appendLiteral(ClassUtils.PACKAGE_SEPARATOR_CHAR).appendFractionOfSecond(3, 9).toFormatter() : fse;
        }

        private static DateTimeFormatter offsetElement() {
            return ze == null ? new DateTimeFormatterBuilder().appendTimeZoneOffset("Z", true, 2, 4).toFormatter() : ze;
        }
    }
}
