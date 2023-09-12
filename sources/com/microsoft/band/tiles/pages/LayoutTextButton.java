package com.microsoft.band.tiles.pages;

import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class LayoutTextButton extends LayoutPageElement {
    public LayoutTextButton(TextButton button) {
        super(button);
    }

    public LayoutTextButton(ByteBuffer buffer) {
        super(buffer);
        buffer.getShort();
        this.mPageElement = new TextButton(this.mRect.getPageRect());
        setPageElementBaseFields();
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPageElement
    protected byte[] toSpecializedBytes() {
        return BufferUtil.EMPTY;
    }
}
