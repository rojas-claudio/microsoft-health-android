package com.microsoft.band.tiles.pages;

import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class LayoutBarcode extends LayoutPageElement {
    public LayoutBarcode(Barcode barcode) {
        super(barcode);
    }

    public LayoutBarcode(ByteBuffer buffer, BarcodeType type) {
        super(buffer);
        buffer.getShort();
        this.mPageElement = new Barcode(this.mRect.getPageRect(), type);
        setPageElementBaseFields();
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPageElement
    protected byte[] toSpecializedBytes() {
        return BufferUtil.EMPTY;
    }
}
