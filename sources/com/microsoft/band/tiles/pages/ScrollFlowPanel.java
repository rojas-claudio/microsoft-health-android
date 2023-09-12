package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import com.microsoft.band.internal.util.Validation;
import java.util.List;
/* loaded from: classes.dex */
public final class ScrollFlowPanel extends FlowPanel {
    public ScrollFlowPanel(int originX, int originY, int width, int height) {
        this(new PageRect(originX, originY, width, height));
    }

    public ScrollFlowPanel(PageRect bound) {
        super(bound);
        setScrollBarColor(-1);
    }

    public ScrollFlowPanel(PageRect bound, FlowPanelOrientation orientation, List<PageElement> elements) {
        this(bound, orientation, elements == null ? null : (PageElement[]) elements.toArray(new PageElement[elements.size()]));
    }

    public ScrollFlowPanel(int originX, int originY, int width, int height, FlowPanelOrientation orientation, PageElement... elements) {
        this(new PageRect(originX, originY, width, height), orientation, elements);
    }

    public ScrollFlowPanel(int originX, int originY, int width, int height, PageElement... elements) {
        this(new PageRect(originX, originY, width, height), elements);
    }

    public ScrollFlowPanel(PageRect bound, FlowPanelOrientation orientation, PageElement... elements) {
        super(bound, orientation, elements);
        setScrollBarColor(-1);
    }

    public ScrollFlowPanel(PageRect bound, PageElement... elements) {
        super(bound, elements);
        setScrollBarColor(-1);
    }

    @Override // com.microsoft.band.tiles.pages.FlowPanel
    public ScrollFlowPanel setFlowPanelOrientation(FlowPanelOrientation orientation) {
        return (ScrollFlowPanel) super.setFlowPanelOrientation(orientation);
    }

    public ElementColorSource getScrollBarColorSource() {
        return this.mColorSource;
    }

    public ScrollFlowPanel setScrollBarColorSource(ElementColorSource colorSource) {
        Validation.notNull(colorSource, "Color source cannot be null");
        this.mColorSource = colorSource;
        return this;
    }

    public int getScrollBarColor() {
        return this.mColor;
    }

    public ScrollFlowPanel setScrollBarColor(int color) {
        this.mColor = color;
        this.mColorSource = ElementColorSource.CUSTOM;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ScrollFlowPanel(Parcel in) {
        super(in);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.band.tiles.pages.FlowPanel, com.microsoft.band.tiles.pages.PageElement
    public ElementType getType() {
        return ElementType.SCROLL_FLOWLIST;
    }
}
