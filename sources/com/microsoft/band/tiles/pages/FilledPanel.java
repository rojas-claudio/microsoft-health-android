package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
import java.util.List;
/* loaded from: classes.dex */
public final class FilledPanel extends PagePanel<FilledPanel> {
    public static final Parcelable.Creator<FilledPanel> CREATOR = new Parcelable.Creator<FilledPanel>() { // from class: com.microsoft.band.tiles.pages.FilledPanel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FilledPanel createFromParcel(Parcel in) {
            return new FilledPanel(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FilledPanel[] newArray(int size) {
            return new FilledPanel[size];
        }
    };

    public FilledPanel(int originX, int originY, int width, int height) {
        super(originX, originY, width, height);
    }

    public FilledPanel(PageRect bound) {
        super(bound);
    }

    public FilledPanel(PageRect bound, List<PageElement> elements) {
        super(bound, elements == null ? null : (PageElement[]) elements.toArray(new PageElement[elements.size()]));
    }

    public FilledPanel(int originX, int originY, int width, int height, PageElement... elements) {
        super(originX, originY, width, height, elements);
    }

    public FilledPanel(PageRect bound, PageElement... elements) {
        super(bound, elements);
    }

    public ElementColorSource getBackgroundColorSource() {
        return this.mColorSource;
    }

    public FilledPanel setBackgroundColorSource(ElementColorSource colorSource) {
        Validation.notNull(colorSource, "Color source cannot be null");
        this.mColorSource = colorSource;
        return this;
    }

    public int getBackgroundColor() {
        return this.mColor;
    }

    public FilledPanel setBackgroundColor(int color) {
        this.mColor = color;
        this.mColorSource = ElementColorSource.CUSTOM;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FilledPanel(Parcel in) {
        super(in);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.band.tiles.pages.PageElement
    public ElementType getType() {
        return ElementType.FILLED_QUAD;
    }
}
