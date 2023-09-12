package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
import java.util.List;
/* loaded from: classes.dex */
public class FlowPanel extends PagePanel<FlowPanel> {
    public static final Parcelable.Creator<FlowPanel> CREATOR = new Parcelable.Creator<FlowPanel>() { // from class: com.microsoft.band.tiles.pages.FlowPanel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FlowPanel createFromParcel(Parcel in) {
            return new FlowPanel(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FlowPanel[] newArray(int size) {
            return new FlowPanel[size];
        }
    };
    private FlowPanelOrientation mOrientation;

    public FlowPanel(int originX, int originY, int width, int height) {
        super(originX, originY, width, height);
        this.mOrientation = FlowPanelOrientation.VERTICAL;
    }

    public FlowPanel(PageRect bound) {
        super(bound);
        this.mOrientation = FlowPanelOrientation.VERTICAL;
    }

    public FlowPanel(PageRect bound, FlowPanelOrientation orientation, List<PageElement> elements) {
        super(bound, elements == null ? null : (PageElement[]) elements.toArray(new PageElement[elements.size()]));
        this.mOrientation = FlowPanelOrientation.VERTICAL;
        setFlowPanelOrientation(orientation);
    }

    public FlowPanel(int originX, int originY, int width, int height, FlowPanelOrientation orientation, PageElement... elements) {
        super(originX, originY, width, height, elements);
        this.mOrientation = FlowPanelOrientation.VERTICAL;
        setFlowPanelOrientation(orientation);
    }

    public FlowPanel(int originX, int originY, int width, int height, PageElement... elements) {
        super(originX, originY, width, height, elements);
        this.mOrientation = FlowPanelOrientation.VERTICAL;
    }

    public FlowPanel(PageRect bound, FlowPanelOrientation orientation, PageElement... elements) {
        super(bound, elements);
        this.mOrientation = FlowPanelOrientation.VERTICAL;
        setFlowPanelOrientation(orientation);
    }

    public FlowPanel(PageRect bound, PageElement... elements) {
        super(bound, elements);
        this.mOrientation = FlowPanelOrientation.VERTICAL;
    }

    public FlowPanelOrientation getFlowPanelOrientation() {
        return this.mOrientation;
    }

    public FlowPanel setFlowPanelOrientation(FlowPanelOrientation orientation) {
        Validation.notNull(orientation, "Flowlist orientation cannot be null");
        this.mOrientation = orientation;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FlowPanel(Parcel in) {
        super(in);
        this.mOrientation = FlowPanelOrientation.VERTICAL;
        setFlowPanelOrientation((FlowPanelOrientation) in.readSerializable());
    }

    @Override // com.microsoft.band.tiles.pages.PagePanel, com.microsoft.band.tiles.pages.PageElement, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(this.mOrientation);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.band.tiles.pages.PageElement
    public ElementType getType() {
        return ElementType.FLOWLIST;
    }
}
