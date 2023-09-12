package com.microsoft.band.tiles.pages;

import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class LayoutIcon extends LayoutPageElement {
    public LayoutIcon(Icon element) {
        super(element);
    }

    public LayoutIcon(ByteBuffer buffer) {
        super(buffer);
        buffer.getShort();
        this.mPageElement = new Icon(this.mRect.getPageRect());
        setPageElementBaseFields();
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPageElement
    protected byte[] toSpecializedBytes() {
        return BufferUtil.EMPTY;
    }
}
