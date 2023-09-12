package com.microsoft.kapp.telephony;

import android.telephony.PhoneNumberUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public class PhoneNumberPrettifier {
    private static final String NA_COUNTRY_CODE_PREFIX = "+1-";
    private static final String NA_PREFERRED_FORMAT = "(%s) %s-%s";
    private static final Pattern PHONE_NUMBER_FORMAT_PATTERN = Pattern.compile("(\\+\\d*?-)?(\\d{3})-(\\d{3})-(\\d{4})");

    public String prettify(String number) {
        String countryCode;
        if (StringUtils.isEmpty(number)) {
            return "";
        }
        String formattedNumber = PhoneNumberUtils.stripSeparators(number);
        if (!StringUtils.isEmpty(formattedNumber)) {
            String formattedNumber2 = PhoneNumberUtils.formatNumber(formattedNumber);
            Matcher matcher = PHONE_NUMBER_FORMAT_PATTERN.matcher(formattedNumber2);
            if (matcher.find() && ((countryCode = matcher.group(1)) == null || countryCode.equals(NA_COUNTRY_CODE_PREFIX))) {
                formattedNumber2 = String.format(NA_PREFERRED_FORMAT, matcher.group(2), matcher.group(3), matcher.group(4));
            }
            return formattedNumber2;
        }
        return number;
    }
}
