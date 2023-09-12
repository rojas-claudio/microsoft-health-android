package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
/* loaded from: classes.dex */
public final class FilledButton extends PageElement<FilledButton> {
    public static final Parcelable.Creator<FilledButton> CREATOR = new Parcelable.Creator<FilledButton>() { // from class: com.microsoft.band.tiles.pages.FilledButton.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FilledButton createFromParcel(Parcel in) {
            return new FilledButton(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FilledButton[] newArray(int size) {
            return new FilledButton[size];
        }
    };

    public FilledButton(int originX, int originY, int width, int height) {
        super(originX, originY, width, height);
        setBackgroundColor(-1);
    }

    public FilledButton(PageRect bound) {
        super(bound);
        setBackgroundColor(-1);
    }

    public int getBackgroundColor() {
        return this.mColor;
    }

    public FilledButton setBackgroundColor(int color) {
        this.mColor = color;
        this.mColorSource = ElementColorSource.CUSTOM;
        return this;
    }

    public ElementColorSource getBackgroundColorSource() {
        return this.mColorSource;
    }

    public FilledButton setBackgroundColorSource(ElementColorSource colorSource) {
        Validation.notNull(colorSource, "Color source cannot be null");
        this.mColorSource = colorSource;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FilledButton(Parcel in) {
        super(in);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.band.tiles.pages.PageElement
    public ElementType getType() {
        return ElementType.BUTTON_QUAD;
    }
}
