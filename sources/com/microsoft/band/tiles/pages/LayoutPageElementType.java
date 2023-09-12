package com.microsoft.band.tiles.pages;

import com.google.android.gms.games.GamesClient;
/* loaded from: classes.dex */
public enum LayoutPageElementType {
    ELEMENT_TYPE_PAGEHEADER(1),
    ELEMENT_TYPE_NON_DATA_TYPES_START(1000),
    ELEMENT_TYPE_FLOWLIST_TYPES_START(1000),
    ELEMENT_TYPE_FLOWLIST(1001),
    ELEMENT_TYPE_SCROLL_FLOWLIST(1002),
    ELEMENT_TYPE_FLOWLIST_TYPES_END(1100),
    ELEMENT_TYPE_SHAPE_TYPES_START(1100),
    ELEMENT_TYPE_FILLED_QUAD(1101),
    ELEMENT_TYPE_SHAPE_TYPES_END(1200),
    ELEMENT_TYPE_NON_DATA_TYPES_END(3000),
    ELEMENT_TYPE_DATA_TYPES_START(3000),
    ELEMENT_TYPE_TEXT_TYPES_START(3000),
    ELEMENT_TYPE_TEXT(GamesClient.STATUS_ACHIEVEMENT_UNKNOWN),
    ELEMENT_TYPE_WRAPPABLE_TEXT(GamesClient.STATUS_ACHIEVEMENT_NOT_INCREMENTAL),
    ELEMENT_TYPE_TEXT_TYPES_END(3100),
    ELEMENT_TYPE_ICON_TYPES_START(3100),
    ELEMENT_TYPE_ICON(3101),
    ELEMENT_TYPE_ICON_TYPES_END(3200),
    ELEMENT_TYPE_BARCODE_TYPES_START(3200),
    ELEMENT_TYPE_BARCODE_CODE39(3201),
    ELEMENT_TYPE_BARCODE_PDF417(3202),
    ELEMENT_TYPE_BARCODE_TYPES_END(3300),
    ELEMENT_TYPE_INTERACTIVE_ELEMENTS_START(3300),
    ELEMENT_TYPE_BUTTON(3301),
    ELEMENT_TYPE_BUTTON_WITH_BORDER(3302),
    ELEMENT_TYPE_BUTTON_WITH_TEXT(3303),
    ELEMENT_TYPE_INTERACTIVE_ELEMENTS_END(3400),
    ELEMENT_TYPE_DATA_TYPES_END(13000),
    ELEMENT_TYPE_INVALID(65535);
    
    private final int mType;

    LayoutPageElementType(int type) {
        this.mType = type;
    }

    public int getType() {
        return this.mType;
    }

    public static LayoutPageElementType lookup(int type) {
        LayoutPageElementType[] arr$ = values();
        for (LayoutPageElementType s : arr$) {
            if (s.getType() == type) {
                return s;
            }
        }
        return ELEMENT_TYPE_INVALID;
    }

    public static LayoutPageElementType toLayoutPageElementType(ElementType type) {
        switch (type) {
            case FLOWLIST:
                return ELEMENT_TYPE_FLOWLIST;
            case SCROLL_FLOWLIST:
                return ELEMENT_TYPE_SCROLL_FLOWLIST;
            case FILLED_QUAD:
                return ELEMENT_TYPE_FILLED_QUAD;
            case TEXT:
                return ELEMENT_TYPE_TEXT;
            case WRAPPABLE_TEXT:
                return ELEMENT_TYPE_WRAPPABLE_TEXT;
            case ICON:
                return ELEMENT_TYPE_ICON;
            case BARCODE_CODE39:
                return ELEMENT_TYPE_BARCODE_CODE39;
            case BARCODE_PDF147:
                return ELEMENT_TYPE_BARCODE_PDF417;
            case BUTTON_QUAD:
                return ELEMENT_TYPE_BUTTON_WITH_BORDER;
            case BUTTON_TEXT:
                return ELEMENT_TYPE_BUTTON_WITH_TEXT;
            default:
                return ELEMENT_TYPE_INVALID;
        }
    }
}
