package com.google.android.gms.ads;

import android.content.Context;
import com.google.android.gms.internal.cm;
import com.google.android.gms.internal.x;
import com.microsoft.kapp.multidevice.MultiDeviceConstants;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public final class AdSize {
    public static final int AUTO_HEIGHT = -2;
    public static final int FULL_WIDTH = -1;
    private final int dP;
    private final int dQ;
    private final String dR;
    public static final AdSize BANNER = new AdSize(320, 50, "320x50_mb");
    public static final AdSize FULL_BANNER = new AdSize(468, 60, "468x60_as");
    public static final AdSize LEADERBOARD = new AdSize(728, 90, "728x90_as");
    public static final AdSize MEDIUM_RECTANGLE = new AdSize(MultiDeviceConstants.BIN_SIZE_SECONDS, Constants.FILTER_TYPE_ALL, "300x250_as");
    public static final AdSize WIDE_SKYSCRAPER = new AdSize(160, Constants.MINIMUM_STEPS_IN_ACTIVE_HOUR, "160x600_as");
    public static final AdSize SMART_BANNER = new AdSize(-1, -2, "smart_banner");

    public AdSize(int width, int height) {
        this(width, height, (width == -1 ? "FULL" : String.valueOf(width)) + "x" + (height == -2 ? "AUTO" : String.valueOf(height)) + "_as");
    }

    public AdSize(int width, int height, String formatString) {
        if (width < 0 && width != -1) {
            throw new IllegalArgumentException("Invalid width for AdSize: " + width);
        }
        if (height < 0 && height != -2) {
            throw new IllegalArgumentException("Invalid height for AdSize: " + height);
        }
        this.dP = width;
        this.dQ = height;
        this.dR = formatString;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof AdSize) {
            AdSize adSize = (AdSize) other;
            return this.dP == adSize.dP && this.dQ == adSize.dQ && this.dR.equals(adSize.dR);
        }
        return false;
    }

    public int getHeight() {
        return this.dQ;
    }

    public int getHeightInPixels(Context context) {
        return this.dQ == -2 ? x.b(context.getResources().getDisplayMetrics()) : cm.a(context, this.dQ);
    }

    public int getWidth() {
        return this.dP;
    }

    public int getWidthInPixels(Context context) {
        return this.dP == -1 ? x.a(context.getResources().getDisplayMetrics()) : cm.a(context, this.dP);
    }

    public int hashCode() {
        return this.dR.hashCode();
    }

    public boolean isAutoHeight() {
        return this.dQ == -2;
    }

    public boolean isFullWidth() {
        return this.dP == -1;
    }

    public String toString() {
        return this.dR;
    }
}
