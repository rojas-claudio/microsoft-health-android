package com.microsoft.band.tiles.pages;

import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class LayoutFilledQuad extends LayoutPagePanel {
    public LayoutFilledQuad(FilledPanel element) {
        super(element);
    }

    public LayoutFilledQuad(ByteBuffer buffer) {
        super(buffer);
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPagePanel
    protected void createSpecificPagePanel() {
        this.mPageElement = new FilledPanel(this.mRect.getPageRect());
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPagePanel, com.microsoft.band.tiles.pages.LayoutPageElement
    protected byte[] toSpecializedBytes() {
        return BufferUtil.EMPTY;
    }
}
