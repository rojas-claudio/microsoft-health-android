package com.microsoft.band.tiles.pages;

import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public abstract class LayoutPagePanel extends LayoutPageElement {
    protected abstract void createSpecificPagePanel();

    public LayoutPagePanel(PagePanel pagePanel) {
        super(pagePanel);
    }

    public LayoutPagePanel(ByteBuffer buffer) {
        super(buffer);
        buffer.getShort();
        createSpecificPagePanel();
        setPageElementBaseFields();
        for (int i = 0; i < this.mChildrenSize; i++) {
            LayoutPageElement child = LayoutPageElementCreator.createFromByteBuffer(buffer);
            addChild(child);
            ((PagePanel) this.mPageElement).addElements(child.mPageElement);
        }
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPageElement
    protected byte[] toSpecializedBytes() {
        return BufferUtil.EMPTY;
    }
}
