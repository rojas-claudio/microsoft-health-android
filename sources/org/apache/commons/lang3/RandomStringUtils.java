package org.apache.commons.lang3;

import android.support.v4.media.TransportMediator;
import java.util.Random;
/* loaded from: classes.dex */
public class RandomStringUtils {
    private static final Random RANDOM = new Random();

    public static String random(int count) {
        return random(count, false, false);
    }

    public static String randomAscii(int count) {
        return random(count, 32, TransportMediator.KEYCODE_MEDIA_PAUSE, false, false);
    }

    public static String randomAlphabetic(int count) {
        return random(count, true, false);
    }

    public static String randomAlphanumeric(int count) {
        return random(count, true, true);
    }

    public static String randomNumeric(int count) {
        return random(count, false, true);
    }

    public static String random(int count, boolean letters, boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return random(count, start, end, letters, numbers, null, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char... chars) {
        return random(count, start, end, letters, numbers, chars, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
        char ch;
        if (count == 0) {
            return "";
        }
        if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }
        if (start == 0 && end == 0) {
            end = 123;
            start = 32;
            if (!letters && !numbers) {
                start = 0;
                end = Integer.MAX_VALUE;
            }
        }
        char[] buffer = new char[count];
        int gap = end - start;
        while (true) {
            int count2 = count;
            count = count2 - 1;
            if (count2 != 0) {
                if (chars == null) {
                    ch = (char) (random.nextInt(gap) + start);
                } else {
                    ch = chars[random.nextInt(gap) + start];
                }
                if ((letters && Character.isLetter(ch)) || ((numbers && Character.isDigit(ch)) || (!letters && !numbers))) {
                    if (ch >= 56320 && ch <= 57343) {
                        if (count == 0) {
                            count++;
                        } else {
                            buffer[count] = ch;
                            count--;
                            buffer[count] = (char) (random.nextInt(128) + 55296);
                        }
                    } else if (ch >= 55296 && ch <= 56191) {
                        if (count == 0) {
                            count++;
                        } else {
                            buffer[count] = (char) (random.nextInt(128) + 56320);
                            count--;
                            buffer[count] = ch;
                        }
                    } else if (ch >= 56192 && ch <= 56319) {
                        count++;
                    } else {
                        buffer[count] = ch;
                    }
                } else {
                    count++;
                }
            } else {
                return new String(buffer);
            }
        }
    }

    public static String random(int count, String chars) {
        return chars == null ? random(count, 0, 0, false, false, null, RANDOM) : random(count, chars.toCharArray());
    }

    public static String random(int count, char... chars) {
        return chars == null ? random(count, 0, 0, false, false, null, RANDOM) : random(count, 0, chars.length, false, false, chars, RANDOM);
    }
}
