package com.microsoft.band.internal.util;

import android.graphics.Bitmap;
/* loaded from: classes.dex */
public final class BandImage {
    private BandImage() {
        throw new UnsupportedOperationException();
    }

    public static Bitmap getBitmapFromBGR565(byte[] data, int height, int width) {
        int size = height * width;
        int[] pixels = new int[size];
        for (int i = 0; i < size; i++) {
            short pixel = (short) (BitHelper.unsignedByteToShort(data[i * 2]) | (BitHelper.unsignedByteToShort(data[(i * 2) + 1]) << 8));
            pixels[i] = convertBGR565ToARGB32(pixel);
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

    public static byte[] bitmapToBGR565(Bitmap image) {
        int pixelWidth = image.getWidth();
        int pixelHeight = image.getHeight();
        int pixelSize = pixelHeight * pixelWidth;
        byte[] pixelArray = new byte[pixelSize * 2];
        int idx = 0;
        int j = 0;
        while (j < pixelHeight) {
            int idx2 = idx;
            for (int i = 0; i < pixelWidth; i++) {
                int c = image.getPixel(i, j);
                short bgr565 = convertARGB32ToBGR565(c);
                int idx3 = idx2 + 1;
                pixelArray[idx2] = (byte) (bgr565 & 255);
                idx2 = idx3 + 1;
                pixelArray[idx3] = (byte) ((65280 & bgr565) >> 8);
            }
            j++;
            idx = idx2;
        }
        return pixelArray;
    }

    public static void validateMeTileImage(Bitmap image, int hardwareVersion) {
        Validation.notNull(image, "Image cannot be null");
        if (image.getConfig() != Bitmap.Config.ARGB_8888) {
            throw new IllegalArgumentException("Bitmap must be ARGB with 4 bytes per pixel.");
        }
        if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            if (!isValidV2ImageDimension(image)) {
                throw new IllegalArgumentException(String.format("Bitmap must be [%d X %d]", 310, 128));
            }
        } else if (!isValidV1ImageDimension(image)) {
            throw new IllegalArgumentException(String.format("Bitmap must be [%d X %d]", 310, 102));
        }
    }

    private static boolean isValidV1ImageDimension(Bitmap image) {
        return image.getWidth() == 310 && image.getHeight() == 102;
    }

    private static boolean isValidV2ImageDimension(Bitmap image) {
        return isValidV1ImageDimension(image) || (image.getWidth() == 310 && image.getHeight() == 128);
    }

    public static short convertARGB32ToBGR565(int color) {
        int B = (color >> 3) & 31;
        int G = (color >> 5) & 2016;
        int R = (color >> 8) & 63488;
        return (short) (R | G | B);
    }

    public static int convertBGR565ToARGB32(short color) {
        int B = (color << 3) & 248;
        int G = (color << 5) & 64512;
        int R = (color << 8) & 16252928;
        return R | G | B | (-16777216);
    }
}
