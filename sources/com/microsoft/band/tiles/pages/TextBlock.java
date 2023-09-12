package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
/* loaded from: classes.dex */
public final class TextBlock extends PageElement<TextBlock> {
    public static final Parcelable.Creator<TextBlock> CREATOR = new Parcelable.Creator<TextBlock>() { // from class: com.microsoft.band.tiles.pages.TextBlock.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextBlock createFromParcel(Parcel in) {
            return new TextBlock(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextBlock[] newArray(int size) {
            return new TextBlock[size];
        }
    };
    private boolean mAutoWidth;
    private int mBaseline;
    private TextBlockBaselineAlignment mBaselineAlignment;
    private TextBlockFont mFont;

    public TextBlock(int originX, int originY, int width, int height, TextBlockFont font, int baseline) {
        this(new PageRect(originX, originY, width, height), font, baseline);
    }

    public TextBlock(int originX, int originY, int width, int height, TextBlockFont font) {
        this(new PageRect(originX, originY, width, height), font);
    }

    public TextBlock(PageRect bound, TextBlockFont font, int baseline) {
        this(bound, font);
        setBaseline(baseline);
    }

    public TextBlock(PageRect bound, TextBlockFont font) {
        super(bound);
        this.mBaseline = 0;
        this.mBaselineAlignment = TextBlockBaselineAlignment.AUTOMATIC;
        this.mAutoWidth = true;
        setFont(font);
        setColor(-1);
    }

    public TextBlockFont getFont() {
        return this.mFont;
    }

    public TextBlock setFont(TextBlockFont font) {
        Validation.notNull(font, "Font cannot be null");
        this.mFont = font;
        return this;
    }

    public int getBaseline() {
        return this.mBaseline;
    }

    public TextBlock setBaseline(int baseline) {
        Validation.validateNotNegative(baseline, "Baseline cannot be negative");
        this.mBaseline = baseline;
        return this;
    }

    public TextBlockBaselineAlignment getBaselineAlignment() {
        return this.mBaselineAlignment;
    }

    public TextBlock setBaselineAlignment(TextBlockBaselineAlignment baselineAlignment) {
        Validation.notNull(baselineAlignment, "Baseline alignment cannot be null");
        this.mBaselineAlignment = baselineAlignment;
        return this;
    }

    public boolean isAutoWidthEnabled() {
        return this.mAutoWidth;
    }

    public TextBlock setAutoWidthEnabled(boolean autoWidth) {
        this.mAutoWidth = autoWidth;
        return this;
    }

    public ElementColorSource getColorSource() {
        return this.mColorSource;
    }

    public TextBlock setColorSource(ElementColorSource colorSource) {
        Validation.notNull(colorSource, "Color source cannot be null");
        this.mColorSource = colorSource;
        return this;
    }

    public int getColor() {
        return this.mColor;
    }

    public TextBlock setColor(int color) {
        this.mColor = color;
        this.mColorSource = ElementColorSource.CUSTOM;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TextBlock(Parcel in) {
        super(in);
        this.mBaseline = 0;
        this.mBaselineAlignment = TextBlockBaselineAlignment.AUTOMATIC;
        this.mAutoWidth = true;
        setFont((TextBlockFont) in.readSerializable());
        setBaseline(in.readInt());
        setBaselineAlignment((TextBlockBaselineAlignment) in.readSerializable());
        setAutoWidthEnabled(in.readByte() != 0);
    }

    @Override // com.microsoft.band.tiles.pages.PageElement, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(this.mFont);
        dest.writeInt(this.mBaseline);
        dest.writeSerializable(this.mBaselineAlignment);
        dest.writeByte((byte) (this.mAutoWidth ? 1 : 0));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.band.tiles.pages.PageElement
    public ElementType getType() {
        return ElementType.TEXT;
    }
}
