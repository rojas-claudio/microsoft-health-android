package com.microsoft.band.tiles.pages;

import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class LayoutFilledButton extends LayoutPageElement {
    public LayoutFilledButton(FilledButton button) {
        super(button);
    }

    public LayoutFilledButton(ByteBuffer buffer) {
        super(buffer);
        buffer.getShort();
        this.mPageElement = new FilledButton(this.mRect.getPageRect());
        setPageElementBaseFields();
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPageElement
    protected byte[] toSpecializedBytes() {
        return BufferUtil.EMPTY;
    }
}
