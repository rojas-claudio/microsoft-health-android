package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
/* loaded from: classes.dex */
public final class FilledButtonData extends PageElementData {
    public static final Parcelable.Creator<FilledButtonData> CREATOR = new Parcelable.Creator<FilledButtonData>() { // from class: com.microsoft.band.tiles.pages.FilledButtonData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FilledButtonData createFromParcel(Parcel source) {
            return new FilledButtonData(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FilledButtonData[] newArray(int size) {
            return new FilledButtonData[size];
        }
    };
    private int mColor;
    private ElementColorSource mColorSource;

    public FilledButtonData(int id, int color) {
        super(id);
        this.mColor = -8355712;
        setPressedColor(color);
    }

    public FilledButtonData(int id, ElementColorSource color) {
        super(id);
        this.mColor = -8355712;
        setPressedColorSource(color);
    }

    public int getPressedColor() {
        return this.mColor;
    }

    public void setPressedColor(int color) {
        this.mColor = color;
        this.mColorSource = ElementColorSource.CUSTOM;
    }

    public ElementColorSource getPressedColorSource() {
        return this.mColorSource;
    }

    public FilledButtonData setPressedColorSource(ElementColorSource colorSource) {
        Validation.notNull(colorSource, "Color source cannot be null");
        this.mColorSource = colorSource;
        return this;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private FilledButtonData(Parcel in) {
        super(in);
        this.mColor = -8355712;
        this.mColorSource = (ElementColorSource) in.readSerializable();
        this.mColor = in.readInt();
    }

    @Override // com.microsoft.band.tiles.pages.PageElementData, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(this.mColorSource);
        dest.writeInt(this.mColor);
    }
}
