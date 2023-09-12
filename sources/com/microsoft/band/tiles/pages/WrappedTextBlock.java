package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
/* loaded from: classes.dex */
public final class WrappedTextBlock extends PageElement<WrappedTextBlock> {
    public static final Parcelable.Creator<WrappedTextBlock> CREATOR = new Parcelable.Creator<WrappedTextBlock>() { // from class: com.microsoft.band.tiles.pages.WrappedTextBlock.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WrappedTextBlock createFromParcel(Parcel in) {
            return new WrappedTextBlock(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WrappedTextBlock[] newArray(int size) {
            return new WrappedTextBlock[size];
        }
    };
    private boolean mAutoHeight;
    private WrappedTextBlockFont mFont;

    public WrappedTextBlock(int originX, int originY, int width, int height, WrappedTextBlockFont font) {
        super(originX, originY, width, height);
        this.mAutoHeight = true;
        setFont(font);
        setColor(-1);
    }

    public WrappedTextBlock(PageRect bound, WrappedTextBlockFont font) {
        super(bound);
        this.mAutoHeight = true;
        setFont(font);
        setColor(-1);
    }

    public WrappedTextBlockFont getFont() {
        return this.mFont;
    }

    public WrappedTextBlock setFont(WrappedTextBlockFont font) {
        Validation.notNull(font, "Font cannot be null");
        this.mFont = font;
        return this;
    }

    public boolean isAutoHeightEnabled() {
        return this.mAutoHeight;
    }

    public WrappedTextBlock setAutoHeightEnabled(boolean autoHeight) {
        this.mAutoHeight = autoHeight;
        return this;
    }

    public ElementColorSource getColorSource() {
        return this.mColorSource;
    }

    public WrappedTextBlock setColorSource(ElementColorSource colorSource) {
        Validation.notNull(colorSource, "Color source cannot be null");
        this.mColorSource = colorSource;
        return this;
    }

    public int getColor() {
        return this.mColor;
    }

    public WrappedTextBlock setColor(int color) {
        this.mColor = color;
        this.mColorSource = ElementColorSource.CUSTOM;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WrappedTextBlock(Parcel in) {
        super(in);
        this.mAutoHeight = true;
        setFont((WrappedTextBlockFont) in.readSerializable());
        setAutoHeightEnabled(in.readByte() != 0);
    }

    @Override // com.microsoft.band.tiles.pages.PageElement, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(this.mFont);
        dest.writeByte((byte) (this.mAutoHeight ? 1 : 0));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.band.tiles.pages.PageElement
    public ElementType getType() {
        return ElementType.WRAPPABLE_TEXT;
    }
}
