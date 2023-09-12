package com.microsoft.band.tiles.pages;

import android.support.v4.media.session.PlaybackStateCompat;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public abstract class LayoutPageElement {
    protected static final int LAYOUT_PAGE_ELEMENT_BASIC_STRUCTURE_SIZE = 34;
    protected List<LayoutPageElement> mChildren;
    protected int mChildrenSize;
    private int mColor;
    protected long mCustomStyleMask;
    protected int mId;
    protected Margin mMargin;
    protected PageElement mPageElement;
    protected Rect mRect;
    private long mStatusMask;
    protected long mStyleMask;
    protected LayoutPageElementType mType;

    protected abstract byte[] toSpecializedBytes();

    public LayoutPageElement() {
        this.mStyleMask = StyleMask.NONE.getMaskBit();
        this.mStatusMask = StatusMask.NONE.getMaskBit();
        this.mCustomStyleMask = 0L;
        this.mChildren = new ArrayList();
    }

    public LayoutPageElement(PageElement element) {
        this.mStyleMask = StyleMask.NONE.getMaskBit();
        this.mStatusMask = StatusMask.NONE.getMaskBit();
        this.mCustomStyleMask = 0L;
        this.mChildren = new ArrayList();
        this.mPageElement = element;
        this.mType = LayoutPageElementType.toLayoutPageElementType(this.mPageElement.getType());
        setColor(element.mColor, element.mColorSource);
        setStyleMask(element);
        setStatusMask(element);
        if (isPagePanel(element)) {
            for (PageElement e : ((PagePanel) element).getElements()) {
                this.mChildren.add(LayoutPageElementCreator.createFromPageElement(e));
            }
        }
    }

    public LayoutPageElement(ByteBuffer buffer) {
        this.mStyleMask = StyleMask.NONE.getMaskBit();
        this.mStatusMask = StatusMask.NONE.getMaskBit();
        this.mCustomStyleMask = 0L;
        this.mChildren = new ArrayList();
        this.mId = buffer.getShort();
        this.mChildrenSize = buffer.getShort();
        this.mRect = new Rect(buffer);
        this.mMargin = new Margin(buffer);
        this.mStyleMask = buffer.getInt();
        this.mStatusMask = buffer.getInt();
        this.mColor = buffer.getInt();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setPageElementBaseFields() {
        this.mPageElement.setId(this.mId).setMargins(this.mMargin.getMargins());
        ElementColorSource colorSource = getElementColorSource();
        this.mPageElement.mColorSource = colorSource;
        this.mPageElement.mColor = getElementColor(colorSource);
        this.mPageElement.setHorizontalAlignment(getHorizontalAlignment());
        this.mPageElement.setVerticalAlignment(getVerticalAlignment());
        this.mPageElement.setVisible(!StatusMask.HIDDEN.checkBit(this.mStatusMask));
    }

    private HorizontalAlignment getHorizontalAlignment() {
        if (StyleMask.HORIZONTAL_CENTER.checkBit(this.mStyleMask)) {
            return HorizontalAlignment.CENTER;
        }
        if (StyleMask.HORIZONTAL_RIGHT.checkBit(this.mStyleMask)) {
            return HorizontalAlignment.RIGHT;
        }
        return HorizontalAlignment.LEFT;
    }

    private VerticalAlignment getVerticalAlignment() {
        if (StyleMask.VERTICAL_CENTER.checkBit(this.mStyleMask)) {
            return VerticalAlignment.CENTER;
        }
        if (StyleMask.VERTICAL_BOTTOM.checkBit(this.mStyleMask)) {
            return VerticalAlignment.BOTTOM;
        }
        return VerticalAlignment.TOP;
    }

    private ElementColorSource getElementColorSource() {
        return PageElementThemeColor.lookup(this.mColor);
    }

    private int getElementColor(ElementColorSource colorSource) {
        if (colorSource != ElementColorSource.CUSTOM) {
            return -16777216;
        }
        return (-16777216) | this.mColor;
    }

    private boolean isPagePanel(PageElement element) {
        return element instanceof PagePanel;
    }

    private boolean isButton(PageElement element) {
        return (element instanceof FilledButton) || (element instanceof TextButton);
    }

    public byte[] toBaseBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(34);
        return buffer.putShort(BitHelper.intToUnsignedShort(this.mType.getType())).putShort((short) this.mPageElement.getId()).putShort((short) this.mChildren.size()).put(new Rect(this.mPageElement.getRect()).toBytes()).put(new Margin(this.mPageElement.getMargins()).toBytes()).putInt(BitHelper.longToUnsignedInt(this.mStyleMask | this.mCustomStyleMask)).putInt(BitHelper.longToUnsignedInt(this.mStatusMask)).putInt(this.mColor).array();
    }

    public byte[] toBytes() throws IOException {
        byte[] baseArray = toBaseBytes();
        byte[] specializedArray = toSpecializedBytes();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write(baseArray);
        buffer.write(specializedArray);
        ByteBuffer bufferForCheckNumber = BufferUtil.allocateLittleEndian(2).putShort(getCheckNumber());
        buffer.write(bufferForCheckNumber.array());
        for (LayoutPageElement element : this.mChildren) {
            buffer.write(element.toBytes());
        }
        return buffer.toByteArray();
    }

    public short getCheckNumber() {
        return (short) (65535 - this.mType.getType());
    }

    public void addChild(LayoutPageElement element) {
        this.mChildren.add(element);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getChildCount() {
        int count = this.mChildren.size();
        for (int i = 0; i < this.mChildren.size(); i++) {
            count += this.mChildren.get(i).getChildCount();
        }
        return (short) count;
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColor(int color, ElementColorSource colorSource) {
        this.mColor = readColor(color, colorSource);
    }

    public static int readColor(int color, ElementColorSource colorSource) {
        if (colorSource == null) {
            return -16777216;
        }
        if (colorSource == ElementColorSource.CUSTOM) {
            return (16777216 | color) & 33554431;
        }
        return PageElementThemeColor.lookup(colorSource).getColor();
    }

    public long getStyleMasks() {
        return this.mStyleMask;
    }

    public void setStyleMask(PageElement element) {
        if (isButton(element)) {
            this.mStyleMask |= StyleMask.TRANSPARENT.getMaskBit();
        }
        HorizontalAlignment hAlignment = element.getHorizontalAlignment();
        if (hAlignment != null) {
            switch (hAlignment) {
                case RIGHT:
                    this.mStyleMask |= StyleMask.HORIZONTAL_RIGHT.getMaskBit();
                    break;
                case LEFT:
                    this.mStyleMask |= StyleMask.HORIZONTAL_LEFT.getMaskBit();
                    break;
                case CENTER:
                    this.mStyleMask |= StyleMask.HORIZONTAL_CENTER.getMaskBit();
                    break;
            }
        }
        VerticalAlignment vAlignment = element.getVerticalAlignment();
        if (vAlignment != null) {
            switch (vAlignment) {
                case TOP:
                    this.mStyleMask |= StyleMask.VERTICAL_TOP.getMaskBit();
                    return;
                case BOTTOM:
                    this.mStyleMask |= StyleMask.VERTICAL_BOTTOM.getMaskBit();
                    return;
                case CENTER:
                    this.mStyleMask |= StyleMask.VERTICAL_CENTER.getMaskBit();
                    return;
                default:
                    return;
            }
        }
    }

    public long getStatusMask() {
        return this.mStatusMask;
    }

    public void setStatusMask(PageElement element) {
        if (!element.isVisible()) {
            this.mStatusMask = StatusMask.HIDDEN.getMaskBit();
        }
    }

    public PageElement getPageElement() {
        return this.mPageElement;
    }

    public List<LayoutPageElement> getChildren() {
        return this.mChildren;
    }

    /* loaded from: classes.dex */
    public enum StyleMask {
        NONE("None", 0),
        HORIZONTAL_RIGHT("HorizontalRight", 32),
        HORIZONTAL_LEFT("HorizontalLeft", 64),
        HORIZONTAL_CENTER("HorizontalCenter", 128),
        VERTICAL_TOP("VerticalTop", 256),
        VERTICAL_CENTER("VerticalCenter", 512),
        VERTICAL_BOTTOM("VerticalBottom", PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID),
        TRANSPARENT("Transparent", 1073741824);
        
        private final long mMask;
        private final String mName;

        StyleMask(String name, long bit) {
            this.mName = name;
            this.mMask = bit;
        }

        public String getName() {
            return this.mName;
        }

        public long getMaskBit() {
            return this.mMask;
        }

        public static StyleMask fromName(String v) {
            StyleMask[] arr$ = values();
            for (StyleMask c : arr$) {
                if (c.getName().equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

        public boolean checkBit(long mask) {
            return (this.mMask & mask) == this.mMask;
        }
    }

    /* loaded from: classes.dex */
    public enum StatusMask {
        NONE("None", 0),
        HIDDEN("Hidden", 65536);
        
        private final long mMask;
        private final String mName;

        StatusMask(String name, long bit) {
            this.mName = name;
            this.mMask = bit;
        }

        public String getName() {
            return this.mName;
        }

        public long getMaskBit() {
            return this.mMask;
        }

        public static StatusMask fromValue(String v) {
            StatusMask[] arr$ = values();
            for (StatusMask c : arr$) {
                if (c.getName().equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

        public boolean checkBit(long mask) {
            return (this.mMask & mask) == this.mMask;
        }
    }

    /* loaded from: classes.dex */
    public static class Margin {
        protected static final int MARGIN_STRUCTURE_SIZE = 8;
        private Margins mMargin;

        public Margin(Margins margin) {
            this.mMargin = margin;
        }

        public Margin(ByteBuffer buffer) {
            this.mMargin = new Margins(buffer.getShort(), buffer.getShort(), buffer.getShort(), buffer.getShort());
        }

        public Margins getMargins() {
            return this.mMargin;
        }

        public byte[] toBytes() {
            return BufferUtil.allocateLittleEndian(8).putShort((short) this.mMargin.getLeft()).putShort((short) this.mMargin.getTop()).putShort((short) this.mMargin.getRight()).putShort((short) this.mMargin.getBottom()).array();
        }
    }

    /* loaded from: classes.dex */
    public static class Rect {
        protected static final int RECT_STRUCTURE_SIZE = 8;
        private PageRect mRect;

        public Rect(PageRect rect) {
            this.mRect = rect;
        }

        public Rect(ByteBuffer buffer) {
            int x = BitHelper.unsignedShortToInteger(buffer.getShort());
            int y = BitHelper.unsignedShortToInteger(buffer.getShort());
            int width = BitHelper.unsignedShortToInteger(buffer.getShort()) - x;
            int height = BitHelper.unsignedShortToInteger(buffer.getShort()) - y;
            this.mRect = new PageRect(x, y, width, height);
        }

        public PageRect getPageRect() {
            return this.mRect;
        }

        public byte[] toBytes() {
            return BufferUtil.allocateLittleEndian(8).putShort((short) this.mRect.getOriginX()).putShort((short) this.mRect.getOriginY()).putShort((short) (this.mRect.getOriginX() + this.mRect.getWidth())).putShort((short) (this.mRect.getOriginY() + this.mRect.getHeight())).array();
        }
    }

    /* loaded from: classes.dex */
    public enum PageElementThemeColor {
        BAND_BASE(ElementColorSource.BAND_BASE, 33554432),
        BAND_HIGHLIGHT(ElementColorSource.BAND_HIGHLIGHT, 33554433),
        BAND_LOWLIGHT(ElementColorSource.BAND_LOWLIGHT, 33554434),
        BAND_SECONDARY_TEXT(ElementColorSource.BAND_SECONDARY_TEXT, 33554435),
        BAND_HIGH_CONTRAST(ElementColorSource.BAND_HIGH_CONTRAST, 33554436),
        BAND_MUTED(ElementColorSource.BAND_MUTED, 33554437),
        TILE_BASE(ElementColorSource.TILE_BASE, 50331648),
        TILE_HIGHLIGHT(ElementColorSource.TILE_HIGHLIGHT, 50331649),
        TILE_LOWLIGHT(ElementColorSource.TILE_LOWLIGHT, 50331650),
        TILE_SECONDARY_TEXT(ElementColorSource.TILE_SECONDARY_TEXT, 50331651),
        TILE_HIGH_CONTRAST(ElementColorSource.TILE_HIGH_CONTRAST, 50331652),
        TILE_MUTED(ElementColorSource.TILE_MUTED, 50331653);
        
        private final int mColor;
        private final ElementColorSource mColorSource;

        PageElementThemeColor(ElementColorSource themeColor, int color) {
            this.mColorSource = themeColor;
            this.mColor = color;
        }

        public ElementColorSource getColorSource() {
            return this.mColorSource;
        }

        public int getColor() {
            return this.mColor;
        }

        public static ElementColorSource lookup(int color) {
            PageElementThemeColor[] arr$ = values();
            for (PageElementThemeColor s : arr$) {
                if (s.getColor() == color) {
                    return s.getColorSource();
                }
            }
            return ElementColorSource.CUSTOM;
        }

        public static PageElementThemeColor lookup(ElementColorSource colorSource) {
            PageElementThemeColor[] arr$ = values();
            for (PageElementThemeColor s : arr$) {
                if (s.getColorSource() == colorSource) {
                    return s;
                }
            }
            return BAND_BASE;
        }
    }
}
