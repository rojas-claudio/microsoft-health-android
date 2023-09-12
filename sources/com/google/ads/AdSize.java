package com.google.ads;

import android.content.Context;
import com.microsoft.kapp.multidevice.MultiDeviceConstants;
import com.microsoft.kapp.utils.Constants;
@Deprecated
/* loaded from: classes.dex */
public final class AdSize {
    public static final int AUTO_HEIGHT = -2;
    public static final int FULL_WIDTH = -1;
    public static final int LANDSCAPE_AD_HEIGHT = 32;
    public static final int LARGE_AD_HEIGHT = 90;
    public static final int PORTRAIT_AD_HEIGHT = 50;
    private final com.google.android.gms.ads.AdSize c;
    public static final AdSize SMART_BANNER = new AdSize(-1, -2, "mb");
    public static final AdSize BANNER = new AdSize(320, 50, "mb");
    public static final AdSize IAB_MRECT = new AdSize(MultiDeviceConstants.BIN_SIZE_SECONDS, Constants.FILTER_TYPE_ALL, "as");
    public static final AdSize IAB_BANNER = new AdSize(468, 60, "as");
    public static final AdSize IAB_LEADERBOARD = new AdSize(728, 90, "as");
    public static final AdSize IAB_WIDE_SKYSCRAPER = new AdSize(160, Constants.MINIMUM_STEPS_IN_ACTIVE_HOUR, "as");

    public AdSize(int width, int height) {
        this(new com.google.android.gms.ads.AdSize(width, height));
    }

    private AdSize(int width, int height, String type) {
        this(new com.google.android.gms.ads.AdSize(width, height));
    }

    public AdSize(com.google.android.gms.ads.AdSize adSize) {
        this.c = adSize;
    }

    public boolean equals(Object other) {
        if (other instanceof AdSize) {
            return this.c.equals(((AdSize) other).c);
        }
        return false;
    }

    public AdSize findBestSize(AdSize... options) {
        int width;
        int height;
        float f;
        AdSize adSize;
        AdSize adSize2 = null;
        if (options != null) {
            float f2 = 0.0f;
            int width2 = getWidth();
            int height2 = getHeight();
            int length = options.length;
            int i = 0;
            while (i < length) {
                AdSize adSize3 = options[i];
                if (isSizeAppropriate(adSize3.getWidth(), adSize3.getHeight())) {
                    f = (width * height) / (width2 * height2);
                    if (f > 1.0f) {
                        f = 1.0f / f;
                    }
                    if (f > f2) {
                        adSize = adSize3;
                        i++;
                        adSize2 = adSize;
                        f2 = f;
                    }
                }
                f = f2;
                adSize = adSize2;
                i++;
                adSize2 = adSize;
                f2 = f;
            }
        }
        return adSize2;
    }

    public int getHeight() {
        return this.c.getHeight();
    }

    public int getHeightInPixels(Context context) {
        return this.c.getHeightInPixels(context);
    }

    public int getWidth() {
        return this.c.getWidth();
    }

    public int getWidthInPixels(Context context) {
        return this.c.getWidthInPixels(context);
    }

    public int hashCode() {
        return this.c.hashCode();
    }

    public boolean isAutoHeight() {
        return this.c.isAutoHeight();
    }

    public boolean isCustomAdSize() {
        return false;
    }

    public boolean isFullWidth() {
        return this.c.isFullWidth();
    }

    public boolean isSizeAppropriate(int width, int height) {
        int width2 = getWidth();
        int height2 = getHeight();
        return ((float) width) <= ((float) width2) * 1.25f && ((float) width) >= ((float) width2) * 0.8f && ((float) height) <= ((float) height2) * 1.25f && ((float) height) >= ((float) height2) * 0.8f;
    }

    public String toString() {
        return this.c.toString();
    }
}
