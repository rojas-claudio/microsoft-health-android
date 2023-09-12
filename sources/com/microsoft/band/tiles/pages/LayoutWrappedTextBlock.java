package com.microsoft.band.tiles.pages;

import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.tiles.pages.LayoutTextElement;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class LayoutWrappedTextBlock extends LayoutTextElement {
    private static int LAYOUT_WRAPPED_TEXT_BASIC_STRUCTURE_SIZE = 2;
    private LayoutTextElement.TextFont mTextFont;

    public LayoutWrappedTextBlock(WrappedTextBlock element) {
        super(element);
        setCustomStyleMask(element);
        switch (element.getFont()) {
            case SMALL:
                this.mTextFont = LayoutTextElement.TextFont.SMALL;
                return;
            case MEDIUM:
                this.mTextFont = LayoutTextElement.TextFont.MEDIUM;
                return;
            default:
                return;
        }
    }

    public LayoutWrappedTextBlock(ByteBuffer buffer) {
        super(buffer);
        this.mTextFont = LayoutTextElement.TextFont.lookup(buffer.getShort());
        buffer.getShort();
        this.mPageElement = new WrappedTextBlock(this.mRect.getPageRect(), this.mTextFont == LayoutTextElement.TextFont.MEDIUM ? WrappedTextBlockFont.MEDIUM : WrappedTextBlockFont.SMALL);
        setPageElementBaseFields();
        if (LayoutTextElement.TextStyleMask.AUTO_RESIZE.checkBit(this.mStyleMask)) {
            ((WrappedTextBlock) this.mPageElement).setAutoHeightEnabled(true);
        } else {
            ((WrappedTextBlock) this.mPageElement).setAutoHeightEnabled(false);
        }
    }

    private void setCustomStyleMask(WrappedTextBlock element) {
        if (element.isAutoHeightEnabled()) {
            this.mCustomStyleMask |= LayoutTextElement.TextStyleMask.AUTO_RESIZE.getMaskBit();
        }
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPageElement
    protected byte[] toSpecializedBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(LAYOUT_WRAPPED_TEXT_BASIC_STRUCTURE_SIZE);
        return buffer.putShort(this.mTextFont.getFont()).array();
    }
}
