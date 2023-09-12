package com.microsoft.band;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class BandTheme implements Parcelable {
    public static final Parcelable.Creator<BandTheme> CREATOR = new Parcelable.Creator<BandTheme>() { // from class: com.microsoft.band.BandTheme.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BandTheme createFromParcel(Parcel in) {
            return new BandTheme(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BandTheme[] newArray(int size) {
            return new BandTheme[size];
        }
    };
    private int mBaseColor;
    private int mHighContrastColor;
    private int mHighlightColor;
    private int mLowlightColor;
    private int mMutedColor;
    private int mSecondaryTextColor;

    public BandTheme() {
        setBaseColor(-16777216);
        setHighlightColor(-16777216);
        setLowlightColor(-16777216);
        setSecondaryTextColor(-16777216);
        setHighContrastColor(-16777216);
        setMutedColor(-16777216);
    }

    public BandTheme(int base, int highlight, int lowlight, int secondaryText, int highContrast, int muted) {
        setBaseColor(base);
        setHighlightColor(highlight);
        setLowlightColor(lowlight);
        setSecondaryTextColor(secondaryText);
        setHighContrastColor(highContrast);
        setMutedColor(muted);
    }

    public int getBaseColor() {
        return this.mBaseColor;
    }

    public BandTheme setBaseColor(int baseColor) {
        this.mBaseColor = baseColor;
        return this;
    }

    public int getHighlightColor() {
        return this.mHighlightColor;
    }

    public BandTheme setHighlightColor(int highlightColor) {
        this.mHighlightColor = highlightColor;
        return this;
    }

    public int getLowlightColor() {
        return this.mLowlightColor;
    }

    public BandTheme setLowlightColor(int lowlightColor) {
        this.mLowlightColor = lowlightColor;
        return this;
    }

    public int getSecondaryTextColor() {
        return this.mSecondaryTextColor;
    }

    public BandTheme setSecondaryTextColor(int secondaryTextColor) {
        this.mSecondaryTextColor = secondaryTextColor;
        return this;
    }

    public int getHighContrastColor() {
        return this.mHighContrastColor;
    }

    public BandTheme setHighContrastColor(int highContrastColor) {
        this.mHighContrastColor = highContrastColor;
        return this;
    }

    public int getMutedColor() {
        return this.mMutedColor;
    }

    public BandTheme setMutedColor(int mutedColor) {
        this.mMutedColor = mutedColor;
        return this;
    }

    public BandTheme(Parcel in) {
        setBaseColor(in.readInt());
        setHighlightColor(in.readInt());
        setLowlightColor(in.readInt());
        setSecondaryTextColor(in.readInt());
        setHighContrastColor(in.readInt());
        setMutedColor(in.readInt());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mBaseColor);
        dest.writeInt(this.mHighlightColor);
        dest.writeInt(this.mLowlightColor);
        dest.writeInt(this.mSecondaryTextColor);
        dest.writeInt(this.mHighContrastColor);
        dest.writeInt(this.mMutedColor);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof BandTheme) {
            BandTheme other = (BandTheme) obj;
            return this.mBaseColor == other.mBaseColor && this.mHighlightColor == other.mHighlightColor && this.mLowlightColor == other.mLowlightColor && this.mSecondaryTextColor == other.mSecondaryTextColor && this.mHighContrastColor == other.mHighContrastColor && this.mMutedColor == other.mMutedColor;
        }
        return false;
    }

    public int hashCode() {
        int result = this.mBaseColor + 217;
        return (((((((((result * 31) + this.mHighlightColor) * 31) + this.mLowlightColor) * 31) + this.mSecondaryTextColor) * 31) + this.mHighContrastColor) * 31) + this.mMutedColor;
    }
}
