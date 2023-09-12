package com.microsoft.band.device;

import com.microsoft.band.tiles.pages.LayoutPageElementType;
/* loaded from: classes.dex */
public class StrappTextButton extends StrappTextbox {
    private static final long serialVersionUID = 1;

    public StrappTextButton(int elementId, String textValue) {
        super(elementId, textValue);
    }

    @Override // com.microsoft.band.device.StrappTextbox, com.microsoft.band.device.StrappPageElement
    protected LayoutPageElementType getElementType() {
        return LayoutPageElementType.ELEMENT_TYPE_BUTTON_WITH_TEXT;
    }
}
