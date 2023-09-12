package com.microsoft.kapp.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
/* loaded from: classes.dex */
public final class MathUtils {
    private static final DecimalFormatSymbols US_SYMBOLS = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat US_DECIMAL_FORMAT = new DecimalFormat();

    static {
        US_DECIMAL_FORMAT.setMinimumIntegerDigits(1);
        US_DECIMAL_FORMAT.setMaximumIntegerDigits(309);
        US_DECIMAL_FORMAT.setRoundingMode(RoundingMode.FLOOR);
        US_DECIMAL_FORMAT.setDecimalFormatSymbols(US_SYMBOLS);
    }

    private MathUtils() {
        throw new UnsupportedOperationException();
    }

    public static final double truncate(double number, int digits) {
        US_DECIMAL_FORMAT.setMaximumFractionDigits(digits);
        US_DECIMAL_FORMAT.setMinimumFractionDigits(digits);
        String formattedNumber = US_DECIMAL_FORMAT.format(number);
        return Double.parseDouble(formattedNumber);
    }

    public static final double truncate(double number, String pattern) {
        DecimalFormat format = new DecimalFormat(pattern);
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setDecimalFormatSymbols(US_SYMBOLS);
        String formattedNumber = format.format(number);
        return Double.parseDouble(formattedNumber);
    }
}
