package com.microsoft.band.tiles.pages;

import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.KDKLog;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: classes.dex */
public class LayoutPageHead extends LayoutPageElement {
    protected static final int LAYOUT_PAGE_HEAD_BASIC_STRUCTURE_SIZE = 16;
    private static final String TAG = LayoutPageHead.class.getSimpleName();

    public LayoutPageHead() {
        this.mType = LayoutPageElementType.ELEMENT_TYPE_PAGEHEADER;
    }

    public LayoutPageHead(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        if (isValidLayout(buffer)) {
            this.mType = LayoutPageElementType.lookup(buffer.getShort());
            buffer.getShort();
            int childrenSize = buffer.getShort();
            buffer.getShort();
            buffer.getShort();
            for (int i = 0; i < childrenSize; i++) {
                addChild(LayoutPageElementCreator.createFromByteBuffer(buffer));
            }
            return;
        }
        KDKLog.i(TAG, "This layout is not set up");
    }

    private boolean isValidLayout(ByteBuffer buffer) {
        return buffer.getShort() == 1 && buffer.getShort() == 0 && buffer.getShort() == 0;
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPageElement
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ByteBuffer bufferForPageHead = BufferUtil.allocateLittleEndian(16).putShort((short) 1).putShort((short) 0).putShort((short) 0).putShort((short) this.mType.getType()).putShort((short) 0).putShort((short) this.mChildren.size()).putShort((short) getChildCount()).putShort(getCheckNumber());
        buffer.write(bufferForPageHead.array());
        for (LayoutPageElement element : this.mChildren) {
            buffer.write(element.toBytes());
        }
        return buffer.toByteArray();
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPageElement
    protected byte[] toSpecializedBytes() {
        return BufferUtil.EMPTY;
    }

    public PagePanel getRootElement() {
        if (this.mChildren.size() > 0) {
            return (PagePanel) this.mChildren.get(0).getPageElement();
        }
        return null;
    }
}
