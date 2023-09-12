package com.microsoft.band.device;

import com.microsoft.band.tiles.pages.LayoutPageElementType;
/* loaded from: classes.dex */
public class StrappWrappableTextbox extends StrappTextbox {
    private static final long serialVersionUID = 1;

    public StrappWrappableTextbox(int elementId, String textboxValue) {
        super(elementId, textboxValue);
    }

    @Override // com.microsoft.band.device.StrappTextbox, com.microsoft.band.device.StrappPageElement
    protected LayoutPageElementType getElementType() {
        return LayoutPageElementType.ELEMENT_TYPE_WRAPPABLE_TEXT;
    }
}
