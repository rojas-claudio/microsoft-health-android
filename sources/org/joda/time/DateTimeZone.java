package org.joda.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.ClassUtils;
import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.chrono.BaseChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.FormatUtils;
import org.joda.time.tz.DefaultNameProvider;
import org.joda.time.tz.FixedDateTimeZone;
import org.joda.time.tz.NameProvider;
import org.joda.time.tz.Provider;
import org.slf4j.Marker;
/* loaded from: classes.dex */
public abstract class DateTimeZone implements Serializable {
    private static final int MAX_MILLIS = 86399999;
    private static final long serialVersionUID = 5546345482340108586L;
    private final String iID;
    public static final DateTimeZone UTC = UTCDateTimeZone.INSTANCE;
    private static final AtomicReference<Provider> cProvider = new AtomicReference<>();
    private static final AtomicReference<NameProvider> cNameProvider = new AtomicReference<>();
    private static final AtomicReference<DateTimeZone> cDefault = new AtomicReference<>();

    public abstract boolean equals(Object obj);

    public abstract String getNameKey(long j);

    public abstract int getOffset(long j);

    public abstract int getStandardOffset(long j);

    public abstract boolean isFixed();

    public abstract long nextTransition(long j);

    public abstract long previousTransition(long j);

    public static DateTimeZone getDefault() {
        DateTimeZone dateTimeZone = cDefault.get();
        if (dateTimeZone == null) {
            try {
                String property = System.getProperty("user.timezone");
                if (property != null) {
                    dateTimeZone = forID(property);
                }
            } catch (RuntimeException e) {
            }
            if (dateTimeZone == null) {
                try {
                    dateTimeZone = forTimeZone(TimeZone.getDefault());
                } catch (IllegalArgumentException e2) {
                }
            }
            if (dateTimeZone == null) {
                dateTimeZone = UTC;
            }
            if (!cDefault.compareAndSet(null, dateTimeZone)) {
                return cDefault.get();
            }
            return dateTimeZone;
        }
        return dateTimeZone;
    }

    public static void setDefault(DateTimeZone dateTimeZone) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new JodaTimePermission("DateTimeZone.setDefault"));
        }
        if (dateTimeZone == null) {
            throw new IllegalArgumentException("The datetime zone must not be null");
        }
        cDefault.set(dateTimeZone);
    }

    @FromString
    public static DateTimeZone forID(String str) {
        if (str == null) {
            return getDefault();
        }
        if (str.equals("UTC")) {
            return UTC;
        }
        DateTimeZone zone = getProvider().getZone(str);
        if (zone == null) {
            if (str.startsWith(Marker.ANY_NON_NULL_MARKER) || str.startsWith("-")) {
                int parseOffset = parseOffset(str);
                if (parseOffset == 0) {
                    return UTC;
                }
                return fixedOffsetZone(printOffset(parseOffset), parseOffset);
            }
            throw new IllegalArgumentException("The datetime zone id '" + str + "' is not recognised");
        }
        return zone;
    }

    public static DateTimeZone forOffsetHours(int i) throws IllegalArgumentException {
        return forOffsetHoursMinutes(i, 0);
    }

    public static DateTimeZone forOffsetHoursMinutes(int i, int i2) throws IllegalArgumentException {
        int abs;
        if (i == 0 && i2 == 0) {
            return UTC;
        }
        if (i < -23 || i > 23) {
            throw new IllegalArgumentException("Hours out of range: " + i);
        }
        if (i2 < -59 || i2 > 59) {
            throw new IllegalArgumentException("Minutes out of range: " + i2);
        }
        if (i > 0 && i2 < 0) {
            throw new IllegalArgumentException("Positive hours must not have negative minutes: " + i2);
        }
        int i3 = i * 60;
        if (i3 < 0) {
            try {
                abs = i3 - Math.abs(i2);
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException("Offset is too large");
            }
        } else {
            abs = i3 + i2;
        }
        return forOffsetMillis(FieldUtils.safeMultiply(abs, (int) DateTimeConstants.MILLIS_PER_MINUTE));
    }

    public static DateTimeZone forOffsetMillis(int i) {
        if (i < -86399999 || i > MAX_MILLIS) {
            throw new IllegalArgumentException("Millis out of range: " + i);
        }
        return fixedOffsetZone(printOffset(i), i);
    }

    public static DateTimeZone forTimeZone(TimeZone timeZone) {
        if (timeZone == null) {
            return getDefault();
        }
        String id = timeZone.getID();
        if (id == null) {
            throw new IllegalArgumentException("The TimeZone id must not be null");
        }
        if (id.equals("UTC")) {
            return UTC;
        }
        DateTimeZone dateTimeZone = null;
        String convertedId = getConvertedId(id);
        Provider provider = getProvider();
        if (convertedId != null) {
            dateTimeZone = provider.getZone(convertedId);
        }
        if (dateTimeZone == null) {
            dateTimeZone = provider.getZone(id);
        }
        if (dateTimeZone == null) {
            if (convertedId == null && (id.startsWith("GMT+") || id.startsWith("GMT-"))) {
                int parseOffset = parseOffset(id.substring(3));
                if (parseOffset == 0) {
                    return UTC;
                }
                return fixedOffsetZone(printOffset(parseOffset), parseOffset);
            }
            throw new IllegalArgumentException("The datetime zone id '" + id + "' is not recognised");
        }
        return dateTimeZone;
    }

    private static DateTimeZone fixedOffsetZone(String str, int i) {
        return i == 0 ? UTC : new FixedDateTimeZone(str, null, i, i);
    }

    public static Set<String> getAvailableIDs() {
        return getProvider().getAvailableIDs();
    }

    public static Provider getProvider() {
        Provider provider = cProvider.get();
        if (provider == null) {
            Provider defaultProvider = getDefaultProvider();
            if (!cProvider.compareAndSet(null, defaultProvider)) {
                return cProvider.get();
            }
            return defaultProvider;
        }
        return provider;
    }

    public static void setProvider(Provider provider) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new JodaTimePermission("DateTimeZone.setProvider"));
        }
        if (provider == null) {
            provider = getDefaultProvider();
        } else {
            validateProvider(provider);
        }
        cProvider.set(provider);
    }

    private static void validateProvider(Provider provider) {
        Set<String> availableIDs = provider.getAvailableIDs();
        if (availableIDs == null || availableIDs.size() == 0) {
            throw new IllegalArgumentException("The provider doesn't have any available ids");
        }
        if (!availableIDs.contains("UTC")) {
            throw new IllegalArgumentException("The provider doesn't support UTC");
        }
        if (!UTC.equals(provider.getZone("UTC"))) {
            throw new IllegalArgumentException("Invalid UTC zone provided");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0036  */
    /* JADX WARN: Type inference failed for: r0v11, types: [org.joda.time.tz.Provider] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static org.joda.time.tz.Provider getDefaultProvider() {
        /*
            r1 = 0
            java.lang.String r0 = "org.joda.time.DateTimeZone.Provider"
            java.lang.String r0 = java.lang.System.getProperty(r0)     // Catch: java.lang.SecurityException -> L2e
            if (r0 == 0) goto L3a
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch: java.lang.Exception -> L27
            java.lang.Object r0 = r0.newInstance()     // Catch: java.lang.Exception -> L27
            org.joda.time.tz.Provider r0 = (org.joda.time.tz.Provider) r0     // Catch: java.lang.Exception -> L27
        L14:
            r1 = r0
        L15:
            if (r1 != 0) goto L34
            org.joda.time.tz.ZoneInfoProvider r0 = new org.joda.time.tz.ZoneInfoProvider     // Catch: java.lang.Exception -> L30
            java.lang.String r2 = "org/joda/time/tz/data"
            r0.<init>(r2)     // Catch: java.lang.Exception -> L30
        L1f:
            if (r0 != 0) goto L36
            org.joda.time.tz.UTCProvider r0 = new org.joda.time.tz.UTCProvider
            r0.<init>()
        L26:
            return r0
        L27:
            r0 = move-exception
            java.lang.RuntimeException r2 = new java.lang.RuntimeException     // Catch: java.lang.SecurityException -> L2e
            r2.<init>(r0)     // Catch: java.lang.SecurityException -> L2e
            throw r2     // Catch: java.lang.SecurityException -> L2e
        L2e:
            r0 = move-exception
            goto L15
        L30:
            r0 = move-exception
            r0.printStackTrace()
        L34:
            r0 = r1
            goto L1f
        L36:
            validateProvider(r0)
            goto L26
        L3a:
            r0 = r1
            goto L14
        */
        throw new UnsupportedOperationException("Method not decompiled: org.joda.time.DateTimeZone.getDefaultProvider():org.joda.time.tz.Provider");
    }

    public static NameProvider getNameProvider() {
        NameProvider nameProvider = cNameProvider.get();
        if (nameProvider == null) {
            NameProvider defaultNameProvider = getDefaultNameProvider();
            if (!cNameProvider.compareAndSet(null, defaultNameProvider)) {
                return cNameProvider.get();
            }
            return defaultNameProvider;
        }
        return nameProvider;
    }

    public static void setNameProvider(NameProvider nameProvider) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new JodaTimePermission("DateTimeZone.setNameProvider"));
        }
        if (nameProvider == null) {
            nameProvider = getDefaultNameProvider();
        }
        cNameProvider.set(nameProvider);
    }

    private static NameProvider getDefaultNameProvider() {
        NameProvider nameProvider;
        try {
            String property = System.getProperty("org.joda.time.DateTimeZone.NameProvider");
            if (property == null) {
                nameProvider = null;
            } else {
                try {
                    nameProvider = (NameProvider) Class.forName(property).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SecurityException e2) {
            nameProvider = null;
        }
        if (nameProvider == null) {
            return new DefaultNameProvider();
        }
        return nameProvider;
    }

    private static String getConvertedId(String str) {
        return LazyInit.CONVERSION_MAP.get(str);
    }

    private static int parseOffset(String str) {
        return -((int) LazyInit.OFFSET_FORMATTER.parseMillis(str));
    }

    private static String printOffset(int i) {
        StringBuffer stringBuffer = new StringBuffer();
        if (i >= 0) {
            stringBuffer.append('+');
        } else {
            stringBuffer.append('-');
            i = -i;
        }
        int i2 = i / DateTimeConstants.MILLIS_PER_HOUR;
        FormatUtils.appendPaddedInteger(stringBuffer, i2, 2);
        int i3 = i - (i2 * DateTimeConstants.MILLIS_PER_HOUR);
        int i4 = i3 / DateTimeConstants.MILLIS_PER_MINUTE;
        stringBuffer.append(':');
        FormatUtils.appendPaddedInteger(stringBuffer, i4, 2);
        int i5 = i3 - (i4 * DateTimeConstants.MILLIS_PER_MINUTE);
        if (i5 == 0) {
            return stringBuffer.toString();
        }
        int i6 = i5 / 1000;
        stringBuffer.append(':');
        FormatUtils.appendPaddedInteger(stringBuffer, i6, 2);
        int i7 = i5 - (i6 * 1000);
        if (i7 == 0) {
            return stringBuffer.toString();
        }
        stringBuffer.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        FormatUtils.appendPaddedInteger(stringBuffer, i7, 3);
        return stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DateTimeZone(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        this.iID = str;
    }

    @ToString
    public final String getID() {
        return this.iID;
    }

    public final String getShortName(long j) {
        return getShortName(j, null);
    }

    public String getShortName(long j, Locale locale) {
        String shortName;
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String nameKey = getNameKey(j);
        if (nameKey == null) {
            return this.iID;
        }
        NameProvider nameProvider = getNameProvider();
        if (nameProvider instanceof DefaultNameProvider) {
            shortName = ((DefaultNameProvider) nameProvider).getShortName(locale, this.iID, nameKey, isStandardOffset(j));
        } else {
            shortName = nameProvider.getShortName(locale, this.iID, nameKey);
        }
        return shortName == null ? printOffset(getOffset(j)) : shortName;
    }

    public final String getName(long j) {
        return getName(j, null);
    }

    public String getName(long j, Locale locale) {
        String name;
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String nameKey = getNameKey(j);
        if (nameKey == null) {
            return this.iID;
        }
        NameProvider nameProvider = getNameProvider();
        if (nameProvider instanceof DefaultNameProvider) {
            name = ((DefaultNameProvider) nameProvider).getName(locale, this.iID, nameKey, isStandardOffset(j));
        } else {
            name = nameProvider.getName(locale, this.iID, nameKey);
        }
        return name == null ? printOffset(getOffset(j)) : name;
    }

    public final int getOffset(ReadableInstant readableInstant) {
        return readableInstant == null ? getOffset(DateTimeUtils.currentTimeMillis()) : getOffset(readableInstant.getMillis());
    }

    public boolean isStandardOffset(long j) {
        return getOffset(j) == getStandardOffset(j);
    }

    public int getOffsetFromLocal(long j) {
        int offset = getOffset(j);
        long j2 = j - offset;
        int offset2 = getOffset(j2);
        if (offset != offset2) {
            if (offset - offset2 < 0) {
                long nextTransition = nextTransition(j2);
                if (nextTransition == j - offset) {
                    nextTransition = Long.MAX_VALUE;
                }
                long nextTransition2 = nextTransition(j - offset2);
                if (nextTransition != (nextTransition2 != j - ((long) offset2) ? nextTransition2 : Long.MAX_VALUE)) {
                    return offset;
                }
            }
        } else if (offset >= 0) {
            long previousTransition = previousTransition(j2);
            if (previousTransition < j2) {
                int offset3 = getOffset(previousTransition);
                if (j2 - previousTransition <= offset3 - offset) {
                    return offset3;
                }
            }
        }
        return offset2;
    }

    public long convertUTCToLocal(long j) {
        int offset = getOffset(j);
        long j2 = offset + j;
        if ((j ^ j2) < 0 && (offset ^ j) >= 0) {
            throw new ArithmeticException("Adding time zone offset caused overflow");
        }
        return j2;
    }

    public long convertLocalToUTC(long j, boolean z, long j2) {
        int offset = getOffset(j2);
        long j3 = j - offset;
        return getOffset(j3) == offset ? j3 : convertLocalToUTC(j, z);
    }

    public long convertLocalToUTC(long j, boolean z) {
        int i;
        long j2;
        int offset = getOffset(j);
        int offset2 = getOffset(j - offset);
        if (offset != offset2 && (z || offset < 0)) {
            long nextTransition = nextTransition(j - offset);
            if (nextTransition == j - offset) {
                nextTransition = Long.MAX_VALUE;
            }
            long nextTransition2 = nextTransition(j - offset2);
            if (nextTransition != (nextTransition2 != j - ((long) offset2) ? nextTransition2 : Long.MAX_VALUE)) {
                if (z) {
                    throw new IllegalInstantException(j, getID());
                }
                i = offset;
                j2 = j - i;
                if ((j ^ j2) >= 0 && (i ^ j) < 0) {
                    throw new ArithmeticException("Subtracting time zone offset caused overflow");
                }
                return j2;
            }
        }
        i = offset2;
        j2 = j - i;
        if ((j ^ j2) >= 0) {
        }
        return j2;
    }

    public long getMillisKeepLocal(DateTimeZone dateTimeZone, long j) {
        DateTimeZone dateTimeZone2 = dateTimeZone == null ? getDefault() : dateTimeZone;
        return dateTimeZone2 == this ? j : dateTimeZone2.convertLocalToUTC(convertUTCToLocal(j), false, j);
    }

    public boolean isLocalDateTimeGap(LocalDateTime localDateTime) {
        if (isFixed()) {
            return false;
        }
        try {
            localDateTime.toDateTime(this);
            return false;
        } catch (IllegalInstantException e) {
            return true;
        }
    }

    public long adjustOffset(long j, boolean z) {
        long j2 = j - 10800000;
        long offset = getOffset(j2);
        long offset2 = getOffset(10800000 + j);
        if (offset > offset2) {
            long j3 = offset - offset2;
            long nextTransition = nextTransition(j2);
            long j4 = nextTransition - j3;
            long j5 = nextTransition + j3;
            if (j < j4 || j >= j5) {
                return j;
            }
            return j - j4 >= j3 ? !z ? j - j3 : j : z ? j + j3 : j;
        }
        return j;
    }

    public TimeZone toTimeZone() {
        return TimeZone.getTimeZone(this.iID);
    }

    public int hashCode() {
        return getID().hashCode() + 57;
    }

    public String toString() {
        return getID();
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new Stub(this.iID);
    }

    /* loaded from: classes.dex */
    private static final class Stub implements Serializable {
        private static final long serialVersionUID = -6471952376487863581L;
        private transient String iID;

        Stub(String str) {
            this.iID = str;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.writeUTF(this.iID);
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException {
            this.iID = objectInputStream.readUTF();
        }

        private Object readResolve() throws ObjectStreamException {
            return DateTimeZone.forID(this.iID);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class LazyInit {
        static final Map<String, String> CONVERSION_MAP = buildMap();
        static final DateTimeFormatter OFFSET_FORMATTER = buildFormatter();

        LazyInit() {
        }

        private static DateTimeFormatter buildFormatter() {
            return new DateTimeFormatterBuilder().appendTimeZoneOffset(null, true, 2, 4).toFormatter().withChronology(new BaseChronology() { // from class: org.joda.time.DateTimeZone.LazyInit.1
                private static final long serialVersionUID = -3128740902654445468L;

                @Override // org.joda.time.chrono.BaseChronology, org.joda.time.Chronology
                public DateTimeZone getZone() {
                    return null;
                }

                @Override // org.joda.time.chrono.BaseChronology, org.joda.time.Chronology
                public Chronology withUTC() {
                    return this;
                }

                @Override // org.joda.time.chrono.BaseChronology, org.joda.time.Chronology
                public Chronology withZone(DateTimeZone dateTimeZone) {
                    return this;
                }

                @Override // org.joda.time.chrono.BaseChronology, org.joda.time.Chronology
                public String toString() {
                    return getClass().getName();
                }
            });
        }

        private static Map<String, String> buildMap() {
            HashMap hashMap = new HashMap();
            hashMap.put("GMT", "UTC");
            hashMap.put("WET", "WET");
            hashMap.put("CET", "CET");
            hashMap.put("MET", "CET");
            hashMap.put("ECT", "CET");
            hashMap.put("EET", "EET");
            hashMap.put("MIT", "Pacific/Apia");
            hashMap.put("HST", "Pacific/Honolulu");
            hashMap.put("AST", "America/Anchorage");
            hashMap.put("PST", "America/Los_Angeles");
            hashMap.put("MST", "America/Denver");
            hashMap.put("PNT", "America/Phoenix");
            hashMap.put("CST", "America/Chicago");
            hashMap.put("EST", "America/New_York");
            hashMap.put("IET", "America/Indiana/Indianapolis");
            hashMap.put("PRT", "America/Puerto_Rico");
            hashMap.put("CNT", "America/St_Johns");
            hashMap.put("AGT", "America/Argentina/Buenos_Aires");
            hashMap.put("BET", "America/Sao_Paulo");
            hashMap.put("ART", "Africa/Cairo");
            hashMap.put("CAT", "Africa/Harare");
            hashMap.put("EAT", "Africa/Addis_Ababa");
            hashMap.put("NET", "Asia/Yerevan");
            hashMap.put("PLT", "Asia/Karachi");
            hashMap.put("IST", "Asia/Kolkata");
            hashMap.put("BST", "Asia/Dhaka");
            hashMap.put("VST", "Asia/Ho_Chi_Minh");
            hashMap.put("CTT", "Asia/Shanghai");
            hashMap.put("JST", "Asia/Tokyo");
            hashMap.put("ACT", "Australia/Darwin");
            hashMap.put("AET", "Australia/Sydney");
            hashMap.put("SST", "Pacific/Guadalcanal");
            hashMap.put("NST", "Pacific/Auckland");
            return Collections.unmodifiableMap(hashMap);
        }
    }
}
