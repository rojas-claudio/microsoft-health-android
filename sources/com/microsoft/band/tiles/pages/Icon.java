package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
/* loaded from: classes.dex */
public final class Icon extends PageElement<Icon> {
    public static final Parcelable.Creator<Icon> CREATOR = new Parcelable.Creator<Icon>() { // from class: com.microsoft.band.tiles.pages.Icon.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Icon createFromParcel(Parcel in) {
            return new Icon(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Icon[] newArray(int size) {
            return new Icon[size];
        }
    };

    public Icon(int originX, int originY, int width, int height) {
        super(originX, originY, width, height);
        setColor(-1);
    }

    public Icon(PageRect bound) {
        super(bound);
        setColor(-1);
    }

    public ElementColorSource getColorSource() {
        return this.mColorSource;
    }

    public Icon setColorSource(ElementColorSource colorSource) {
        Validation.notNull(colorSource, "Color source cannot be null");
        this.mColorSource = colorSource;
        return this;
    }

    public int getColor() {
        return this.mColor;
    }

    public Icon setColor(int color) {
        this.mColor = color;
        this.mColorSource = ElementColorSource.CUSTOM;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Icon(Parcel in) {
        super(in);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.band.tiles.pages.PageElement
    public ElementType getType() {
        return ElementType.ICON;
    }
}
