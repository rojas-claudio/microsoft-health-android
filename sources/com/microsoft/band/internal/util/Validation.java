package com.microsoft.band.internal.util;

import android.graphics.Bitmap;
import com.microsoft.band.BandErrorType;
import com.microsoft.band.BandException;
import com.microsoft.band.internal.CommandBase;
/* loaded from: classes.dex */
public final class Validation {
    public static final String VARIABLE_NAME = "[a-zA-Z_]\\w*";

    private Validation() {
        throw new UnsupportedOperationException();
    }

    public static <E> E validateNullParameter(E parameter, String prompt) {
        if (parameter == null) {
            throw new NullPointerException(String.format("%s cannot be Null", prompt));
        }
        return parameter;
    }

    public static <T> T notNull(T obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
        return obj;
    }

    public static void validateInRange(String prompt, int value, int min, int max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(String.format("%s must be between %d and %d, received %d.", prompt, Integer.valueOf(min), Integer.valueOf(max), Integer.valueOf(value)));
        }
    }

    public static void validateNullAndEmptyString(String parameter, String prompt) {
        if (parameter == null || parameter.length() == 0) {
            throw new IllegalArgumentException(String.format("%s was Null or Empty", prompt));
        }
    }

    public static void validStringNullAndLength(String parameter, int lengthLimit, String prompt) {
        validateNullParameter(parameter, prompt);
        if (parameter.length() > lengthLimit) {
            throw new IllegalArgumentException(String.format("The length of %s is limited to %d", prompt, Integer.valueOf(lengthLimit)));
        }
    }

    public static void validateZeroCount(String prompt, int count) {
        if (count == 0) {
            throw new IllegalArgumentException(String.format("%s has zero count", prompt));
        }
    }

    public static void validateTimeoutAndResultCode(CommandBase command, boolean isTimeOut, String tag) throws BandException {
        if (isTimeOut) {
            KDKLog.e(tag, String.format("Process %s with timeout", command.getCommandType()));
            throw new BandException("Command timed out.", BandErrorType.TIMEOUT_ERROR);
        } else if (command.isResultCodeSevere()) {
            KDKLog.e(tag, String.format("Process %s with severe result %s", command.getCommandType(), command.getResultString()));
            if (command.getResponseCode() != null) {
                switch (command.getResponseCode()) {
                    case DEVICE_NOT_BONDED_ERROR:
                    case DEVICE_NOT_CONNECTED_ERROR:
                    case DEVICE_TIMEOUT_ERROR:
                    case DEVICE_IO_ERROR:
                    case DEVICE_STATE_ERROR:
                        throw new BandException("Please make sure bluetooth is on and the band is in range.", BandErrorType.DEVICE_ERROR);
                    default:
                        throw new BandException("Unknown error occurred.", BandErrorType.UNKNOWN_ERROR);
                }
            }
        }
    }

    public static String lengthLessOrEq(String str, int maxLength, String promptName) {
        if (str.length() > maxLength) {
            throw new IllegalArgumentException(String.format("%s cannot be longer than %d characters", promptName, Integer.valueOf(maxLength)));
        }
        return str;
    }

    public static void validateStringEmptyOrWhiteSpace(String str, String promptName) {
        if (str.matches("\\s*")) {
            throw new IllegalArgumentException(String.format("%s must contain non-whitespace characters", promptName));
        }
    }

    public static void validateNotNegative(int value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateValueMatchesExpected(int value, int expectedValue, String prompt) {
        if (value != expectedValue) {
            throw new IllegalArgumentException(String.format("Expected value %d does not match given value %d of %s", Integer.valueOf(expectedValue), Integer.valueOf(value), prompt));
        }
    }

    public static void verifyIconPixelSize(Bitmap bitmap, int value) {
        notNull(bitmap, "Bitmap cannot be null");
        if (bitmap.getWidth() != value && bitmap.getHeight() != value) {
            throw new IllegalArgumentException(String.format("Icon must be %d x %d pixels. Current dimensions are %d x %d", Integer.valueOf(value), Integer.valueOf(value), Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight())));
        }
    }

    public static void validateVariableName(String value, String message) {
        if (!value.matches(VARIABLE_NAME)) {
            throw new IllegalArgumentException(String.format("%s: [%s] is Not a valid variable name --- must only contain underscore and alphanumeric characters and not start with number.", message, value));
        }
    }

    public static void validateUINT32(String prompt, long value) {
        if (value < 0 || value > 4294967295L) {
            throw new IllegalArgumentException(String.format("%s = %d is not a valid uint32.", prompt, Long.valueOf(value)));
        }
    }

    public static void validateUINT8(String prompt, short value) {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException(String.format("%s = %d is not a valid uint8.", prompt, Short.valueOf(value)));
        }
    }
}
