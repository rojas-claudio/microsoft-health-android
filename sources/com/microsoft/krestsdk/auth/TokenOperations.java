package com.microsoft.krestsdk.auth;

import com.j256.ormlite.stmt.query.SimpleComparison;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
/* loaded from: classes.dex */
public class TokenOperations {
    public static String wrapSwtForHeader(String swt) {
        return "WRAP access_token=\"" + swt + "\"";
    }

    public static String extractSwtFromHeader(String headerValue) {
        int firstIndex = headerValue.indexOf(34);
        int lastIndex = headerValue.lastIndexOf(34);
        String swtString = headerValue.substring(firstIndex + 1, lastIndex);
        return swtString;
    }

    public static DateTime extractExpirationFromSwt(String swt) {
        String[] swtParts = swt.split("&");
        for (String part : swtParts) {
            int equalsIndex = part.indexOf(SimpleComparison.EQUAL_TO_OPERATION);
            if (equalsIndex >= 0) {
                String key = part.substring(0, equalsIndex);
                String value = part.substring(equalsIndex + 1, part.length());
                if (key.equals("ExpiresOn")) {
                    long secondsSince1970 = Long.parseLong(value);
                    DateTime referenceTime = new DateTime(1970, 1, 1, 0, 0, DateTimeZone.UTC);
                    return referenceTime.plus(Duration.standardSeconds(secondsSince1970));
                }
            }
        }
        return null;
    }
}
