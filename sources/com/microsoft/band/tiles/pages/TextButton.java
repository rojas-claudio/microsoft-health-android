package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
/* loaded from: classes.dex */
public final class TextButton extends PageElement<TextButton> {
    public static final Parcelable.Creator<TextButton> CREATOR = new Parcelable.Creator<TextButton>() { // from class: com.microsoft.band.tiles.pages.TextButton.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextButton createFromParcel(Parcel in) {
            return new TextButton(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextButton[] newArray(int size) {
            return new TextButton[size];
        }
    };
    private static final int DEFAULT_PRESSED_COLOR = -8355712;

    public TextButton(int originX, int originY, int width, int height) {
        super(originX, originY, width, height);
        setPressedColor(DEFAULT_PRESSED_COLOR);
    }

    public TextButton(PageRect bound) {
        super(bound);
        setPressedColor(DEFAULT_PRESSED_COLOR);
    }

    public int getPressedColor() {
        return this.mColor;
    }

    public TextButton setPressedColor(int color) {
        this.mColor = color;
        this.mColorSource = ElementColorSource.CUSTOM;
        return this;
    }

    public ElementColorSource getPressedColorSource() {
        return this.mColorSource;
    }

    public TextButton setPressedColorSource(ElementColorSource colorSource) {
        Validation.notNull(colorSource, "Color source cannot be null");
        this.mColorSource = colorSource;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TextButton(Parcel in) {
        super(in);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.band.tiles.pages.PageElement
    public ElementType getType() {
        return ElementType.BUTTON_TEXT;
    }
}
