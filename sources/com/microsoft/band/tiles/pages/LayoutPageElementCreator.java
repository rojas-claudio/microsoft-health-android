package com.microsoft.band.tiles.pages;

import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class LayoutPageElementCreator {
    private LayoutPageElementCreator() {
        throw new UnsupportedOperationException();
    }

    public static LayoutPageElement createFromPageElement(PageElement pageElement) {
        switch (pageElement.getType()) {
            case FLOWLIST:
                LayoutPageElement element = new LayoutFlowList((FlowPanel) pageElement);
                return element;
            case SCROLL_FLOWLIST:
                LayoutPageElement element2 = new LayoutScrollFlowList((ScrollFlowPanel) pageElement);
                return element2;
            case FILLED_QUAD:
                LayoutPageElement element3 = new LayoutFilledQuad((FilledPanel) pageElement);
                return element3;
            case TEXT:
                LayoutPageElement element4 = new LayoutTextBlock((TextBlock) pageElement);
                return element4;
            case WRAPPABLE_TEXT:
                LayoutPageElement element5 = new LayoutWrappedTextBlock((WrappedTextBlock) pageElement);
                return element5;
            case ICON:
                LayoutPageElement element6 = new LayoutIcon((Icon) pageElement);
                return element6;
            case BARCODE_CODE39:
            case BARCODE_PDF147:
                LayoutPageElement element7 = new LayoutBarcode((Barcode) pageElement);
                return element7;
            case BUTTON_QUAD:
                LayoutPageElement element8 = new LayoutFilledButton((FilledButton) pageElement);
                return element8;
            case BUTTON_TEXT:
                LayoutPageElement element9 = new LayoutTextButton((TextButton) pageElement);
                return element9;
            default:
                return null;
        }
    }

    public static LayoutPageElement createFromByteBuffer(ByteBuffer buffer) {
        LayoutPageElementType type = LayoutPageElementType.lookup(buffer.getShort());
        switch (type) {
            case ELEMENT_TYPE_FLOWLIST:
                LayoutPageElement element = new LayoutFlowList(buffer);
                return element;
            case ELEMENT_TYPE_SCROLL_FLOWLIST:
                LayoutPageElement element2 = new LayoutScrollFlowList(buffer);
                return element2;
            case ELEMENT_TYPE_FILLED_QUAD:
                LayoutPageElement element3 = new LayoutFilledQuad(buffer);
                return element3;
            case ELEMENT_TYPE_TEXT:
                LayoutPageElement element4 = new LayoutTextBlock(buffer);
                return element4;
            case ELEMENT_TYPE_WRAPPABLE_TEXT:
                LayoutPageElement element5 = new LayoutWrappedTextBlock(buffer);
                return element5;
            case ELEMENT_TYPE_ICON:
                LayoutPageElement element6 = new LayoutIcon(buffer);
                return element6;
            case ELEMENT_TYPE_BARCODE_CODE39:
                LayoutPageElement element7 = new LayoutBarcode(buffer, BarcodeType.CODE39);
                return element7;
            case ELEMENT_TYPE_BARCODE_PDF417:
                LayoutPageElement element8 = new LayoutBarcode(buffer, BarcodeType.PDF417);
                return element8;
            case ELEMENT_TYPE_BUTTON_WITH_BORDER:
                LayoutPageElement element9 = new LayoutFilledButton(buffer);
                return element9;
            case ELEMENT_TYPE_BUTTON_WITH_TEXT:
                LayoutPageElement element10 = new LayoutTextButton(buffer);
                return element10;
            default:
                return null;
        }
    }
}
