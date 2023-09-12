package com.microsoft.band.device;

import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.tiles.pages.ElementColorSource;
import com.microsoft.band.tiles.pages.LayoutPageElement;
import com.microsoft.band.tiles.pages.LayoutPageElementType;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class StrappFilledButton extends StrappPageElement {
    private static int STRAPP_QUADBUTTON_BASIC_STRUCTURE_SIZE = 4;
    private static final long serialVersionUID = 1;
    private int mColor;

    public StrappFilledButton(int elementId, int color, ElementColorSource colorSource) {
        this(elementId, LayoutPageElement.readColor(color, colorSource));
    }

    private StrappFilledButton(int elementId, int color) {
        super(elementId);
        setColor(color);
    }

    private void setColor(int color) {
        this.mColor = color;
    }

    @Override // com.microsoft.band.device.StrappPageElement
    protected byte[] toSpecializedBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(STRAPP_QUADBUTTON_BASIC_STRUCTURE_SIZE);
        return buffer.putInt(this.mColor).array();
    }

    @Override // com.microsoft.band.device.StrappPageElement
    protected LayoutPageElementType getElementType() {
        return LayoutPageElementType.ELEMENT_TYPE_BUTTON_WITH_BORDER;
    }
}
