package com.microsoft.band.tiles.pages;

import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.tiles.pages.LayoutTextElement;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class LayoutTextBlock extends LayoutTextElement {
    private static int LAYOUT_TEXT_BASIC_STRUCTURE_SIZE = 4;
    private int mBaseline;
    private LayoutTextElement.TextFont mTextFont;

    public LayoutTextBlock(TextBlock element) {
        super(element);
        setCustomStyleMask(element);
        this.mTextFont = LayoutTextElement.TextFont.lookup(element.getFont());
        this.mBaseline = element.getBaseline();
    }

    public LayoutTextBlock(ByteBuffer buffer) {
        super(buffer);
        this.mTextFont = LayoutTextElement.TextFont.lookup(buffer.getShort());
        this.mBaseline = BitHelper.unsignedShortToInteger(buffer.getShort());
        buffer.getShort();
        this.mPageElement = new TextBlock(this.mRect.getPageRect(), this.mTextFont.getTextBlockFont(), this.mBaseline);
        setPageElementBaseFields();
        if (LayoutTextElement.TextStyleMask.VERTICAL_BASELINE_ABSOLUTE.checkBit(this.mStyleMask)) {
            ((TextBlock) this.mPageElement).setBaselineAlignment(TextBlockBaselineAlignment.ABSOLUTE);
        } else if (LayoutTextElement.TextStyleMask.VERTICAL_BASELINE_RELATIVE.checkBit(this.mStyleMask)) {
            ((TextBlock) this.mPageElement).setBaselineAlignment(TextBlockBaselineAlignment.RELATIVE);
        }
        if (LayoutTextElement.TextStyleMask.AUTO_RESIZE_WIDTH.checkBit(this.mStyleMask)) {
            ((TextBlock) this.mPageElement).setAutoWidthEnabled(true);
        } else {
            ((TextBlock) this.mPageElement).setAutoWidthEnabled(false);
        }
    }

    private void setCustomStyleMask(TextBlock element) {
        if (element.isAutoWidthEnabled()) {
            this.mCustomStyleMask |= LayoutTextElement.TextStyleMask.AUTO_RESIZE_WIDTH.getMaskBit();
        }
        TextBlockBaselineAlignment textBaselineAlignment = element.getBaselineAlignment();
        if (textBaselineAlignment != null) {
            switch (textBaselineAlignment) {
                case ABSOLUTE:
                    this.mCustomStyleMask |= LayoutTextElement.TextStyleMask.VERTICAL_BASELINE_ABSOLUTE.getMaskBit();
                    return;
                case RELATIVE:
                    this.mCustomStyleMask |= LayoutTextElement.TextStyleMask.VERTICAL_BASELINE_RELATIVE.getMaskBit();
                    return;
                default:
                    return;
            }
        }
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPageElement
    protected byte[] toSpecializedBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(LAYOUT_TEXT_BASIC_STRUCTURE_SIZE);
        return buffer.putShort(this.mTextFont.getFont()).putShort(BitHelper.intToUnsignedShort(this.mBaseline)).array();
    }
}
