package com.microsoft.band.tiles.pages;

import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.tiles.pages.LayoutFlowList;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class LayoutScrollFlowList extends LayoutFlowList {
    public LayoutScrollFlowList(ScrollFlowPanel flowList) {
        super(flowList);
    }

    public LayoutScrollFlowList(ByteBuffer buffer) {
        super(buffer);
    }

    @Override // com.microsoft.band.tiles.pages.LayoutFlowList, com.microsoft.band.tiles.pages.LayoutPagePanel
    protected void createSpecificPagePanel() {
        this.mPageElement = new ScrollFlowPanel(this.mRect.getPageRect(), LayoutFlowList.FlowPanelStyleMask.HORIZONTAL.checkBit(this.mStyleMask) ? FlowPanelOrientation.HORIZONTAL : FlowPanelOrientation.VERTICAL, new PageElement[0]);
    }

    @Override // com.microsoft.band.tiles.pages.LayoutFlowList, com.microsoft.band.tiles.pages.LayoutPagePanel, com.microsoft.band.tiles.pages.LayoutPageElement
    protected byte[] toSpecializedBytes() {
        return BufferUtil.EMPTY;
    }
}
