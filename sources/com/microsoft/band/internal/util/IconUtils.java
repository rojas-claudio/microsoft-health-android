package com.microsoft.band.internal.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class IconUtils {
    public static final int BYTES_PER_PIXEL = 4;
    public static final int DATA_SHIFT = 4;
    public static final int ICON_BUFFER_SIZE = 1024;
    public static final int ICON_HEADER_SIZE = 6;
    public static final byte MAX_RUN_LENGTH = 15;

    private IconUtils() {
    }

    public static byte[] bitmapToByteArray(Bitmap image) {
        Validation.notNull(image, "Image cannot be null");
        if (!verifyIconImageConfig(image)) {
            throw new IllegalArgumentException("Bitmap must be ARGB with 4 bytes per pixel.");
        }
        try {
            int iconWidth = image.getWidth();
            int iconHeight = image.getHeight();
            byte[] iconData = compress(readBitmapAlpha(image), iconHeight, iconWidth, 1);
            if (iconData != null) {
                if (!verifyIconSize(iconData)) {
                    String errorMessage = String.format("The compressed %d x %d image is %d bytes, but Microsoft Band icons are limited to %d bytes.", Integer.valueOf(iconWidth), Integer.valueOf(iconHeight), Integer.valueOf(iconData.length), 1018);
                    KDKLog.e(IconUtils.class.getSimpleName(), errorMessage);
                    throw new IllegalArgumentException(errorMessage);
                }
                KDKLog.i(IconUtils.class.getSimpleName(), String.format("The compressed image is %d bytes.", Integer.valueOf(iconData.length)));
            }
            return iconData;
        } catch (Exception e) {
            throw new IllegalArgumentException("Bitmap cannot be compressed.");
        }
    }

    private static byte[] readBitmapAlpha(Bitmap image) {
        int w = image.getWidth();
        int h = image.getHeight();
        ByteBuffer buf = ByteBuffer.allocate(w * h);
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                buf.put((byte) Color.alpha(image.getPixel(col, row)));
            }
        }
        return buf.array();
    }

    private static boolean verifyIconSize(byte[] iconData) {
        return iconData.length <= 1018;
    }

    private static boolean verifyIconImageConfig(Bitmap image) {
        return image.getConfig() == Bitmap.Config.ARGB_8888;
    }

    private static byte[] compress(byte[] rawData, int height, int width, int channels) {
        int stride = width * channels;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(rawData.length);
        if (width == 0) {
            return bOut.toByteArray();
        }
        for (int row = 0; row < height; row++) {
            int runLength = 0;
            byte runChar = rawData[row * stride];
            for (int col = 0; col < width; col++) {
                byte value = rawData[(row * stride) + (col * channels)];
                if ((value >> 4) != (runChar >> 4)) {
                    if (runLength > 0) {
                        runLength = writeChar(bOut, runLength, runChar, 4);
                    }
                    runChar = value;
                }
                runLength++;
                if (runLength == 15) {
                    runLength = writeChar(bOut, runLength, runChar, 4);
                    runChar = value;
                }
                if (col == width - 1 && runLength > 0) {
                    runLength = writeChar(bOut, runLength, runChar, 4);
                }
            }
        }
        return bOut.toByteArray();
    }

    private static int writeChar(ByteArrayOutputStream bOut, int runLength, byte runChar, int dataShiftBits) {
        byte runLengthChar = (byte) BitHelper.unsignedIntegerToLong((runLength << dataShiftBits) | (BitHelper.unsignedByteToShort(runChar) >> dataShiftBits));
        bOut.write(runLengthChar);
        return 0;
    }
}
