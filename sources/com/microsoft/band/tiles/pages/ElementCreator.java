package com.microsoft.band.tiles.pages;

import android.os.Parcel;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ElementCreator {
    private ElementCreator() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PageElement createFromParcel(Parcel source) {
        ElementType pageElementType = ElementType.values()[source.readInt()];
        switch (pageElementType) {
            case FLOWLIST:
                PageElement element = new FlowPanel(source);
                return element;
            case SCROLL_FLOWLIST:
                PageElement element2 = new ScrollFlowPanel(source);
                return element2;
            case FILLED_QUAD:
                PageElement element3 = new FilledPanel(source);
                return element3;
            case TEXT:
                PageElement element4 = new TextBlock(source);
                return element4;
            case WRAPPABLE_TEXT:
                PageElement element5 = new WrappedTextBlock(source);
                return element5;
            case ICON:
                PageElement element6 = new Icon(source);
                return element6;
            case BARCODE_CODE39:
            case BARCODE_PDF147:
                PageElement element7 = new Barcode(source);
                return element7;
            case BUTTON_QUAD:
                PageElement element8 = new FilledButton(source);
                return element8;
            case BUTTON_TEXT:
                PageElement element9 = new TextButton(source);
                return element9;
            default:
                return null;
        }
    }
}
