package com.microsoft.band.device;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.microsoft.band.BandTheme;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.Validation;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: classes.dex */
public class StrappColorPalette implements Parcelable {
    public static final Parcelable.Creator<StrappColorPalette> CREATOR = new Parcelable.Creator<StrappColorPalette>() { // from class: com.microsoft.band.device.StrappColorPalette.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StrappColorPalette createFromParcel(Parcel source) {
            return new StrappColorPalette(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StrappColorPalette[] newArray(int size) {
            return new StrappColorPalette[size];
        }
    };
    public static final int STRAPP_COLOR_PALETTE_DATA_STRUCTURE_SIZE = 24;
    private BandTheme mTheme;

    public StrappColorPalette(int base, int highlight, int lowlight, int secondaryText, int highContrast, int muted) {
        this.mTheme = new BandTheme(base, highlight, lowlight, secondaryText, highContrast, muted);
    }

    public StrappColorPalette(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        this.mTheme = new BandTheme(buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt());
    }

    public StrappColorPalette(Parcel source) {
        this.mTheme = new BandTheme(source);
    }

    public StrappColorPalette(@NonNull BandTheme theme) {
        Validation.notNull(theme, "theme == null");
        this.mTheme = theme;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(24);
        return buffer.putInt(toRGB888(this.mTheme.getBaseColor())).putInt(toRGB888(this.mTheme.getHighlightColor())).putInt(toRGB888(this.mTheme.getLowlightColor())).putInt(toRGB888(this.mTheme.getSecondaryTextColor())).putInt(toRGB888(this.mTheme.getHighContrastColor())).putInt(toRGB888(this.mTheme.getMutedColor())).array();
    }

    public BandTheme getTheme() {
        return this.mTheme;
    }

    public String toString() {
        return String.format("ThemeColorPalette:%s", System.getProperty("line.separator")) + String.format("     |--base = %x %s", Integer.valueOf(this.mTheme.getBaseColor()), System.getProperty("line.separator")) + String.format("     |--highlight= %x %s", Integer.valueOf(this.mTheme.getHighlightColor()), System.getProperty("line.separator")) + String.format("     |--lowlight = %x %s", Integer.valueOf(this.mTheme.getLowlightColor()), System.getProperty("line.separator")) + String.format("     |--secondaryText = %x %s", Integer.valueOf(this.mTheme.getSecondaryTextColor()), System.getProperty("line.separator")) + String.format("     |--highContrast = %x %s", Integer.valueOf(this.mTheme.getHighContrastColor()), System.getProperty("line.separator")) + String.format("     |--muted = %x %s", Integer.valueOf(this.mTheme.getMutedColor()), System.getProperty("line.separator"));
    }

    private int toRGB888(int color) {
        return 16777215 & color;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        this.mTheme.writeToParcel(dest, flags);
    }
}
