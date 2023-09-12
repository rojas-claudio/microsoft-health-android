package com.microsoft.band.tiles.pages;

import android.support.v4.media.session.PlaybackStateCompat;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public abstract class LayoutTextElement extends LayoutPageElement {
    public LayoutTextElement(PageElement element) {
        super(element);
        this.mCustomStyleMask = TextStyleMask.NONE.getMaskBit();
    }

    public LayoutTextElement(ByteBuffer buffer) {
        super(buffer);
    }

    /* loaded from: classes.dex */
    public enum TextStyleMask {
        NONE("NONE", 0),
        VERTICAL_BASELINE_ABSOLUTE("VerticalBaselineAbsolute", PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH),
        VERTICAL_BASELINE_RELATIVE("VerticalBaselineRelative", 4096),
        AUTO_RESIZE("AutoResize", 8192),
        AUTO_RESIZE_WIDTH("AutoResizeWidth", 16384);
        
        private final String mName;
        private final long mStyleMask;

        TextStyleMask(String name, long bit) {
            this.mName = name;
            this.mStyleMask = bit;
        }

        public String getName() {
            return this.mName;
        }

        public long getMaskBit() {
            return this.mStyleMask;
        }

        public static TextStyleMask fromName(String v) {
            TextStyleMask[] arr$ = values();
            for (TextStyleMask c : arr$) {
                if (c.getName().equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

        public boolean checkBit(long mask) {
            return (this.mStyleMask & mask) == this.mStyleMask;
        }
    }

    /* loaded from: classes.dex */
    public enum TextFont {
        SMALL("Small", 0, TextBlockFont.SMALL),
        MEDIUM("Medium", 1, TextBlockFont.MEDIUM),
        LARGE("Large", 2, TextBlockFont.LARGE),
        EXTRA_LARGE("ExtraLargeNumbers", 3, TextBlockFont.EXTRA_LARGE_NUMBERS),
        EXTRA_LARGE_BOLD("ExtraLargeNumbersBold", 4, TextBlockFont.EXTRA_LARGE_NUMBERS_BOLD);
        
        private final short mFont;
        private final String mName;
        private final TextBlockFont mTextBlockFont;

        TextFont(String name, int font, TextBlockFont textBlockFont) {
            this.mName = name;
            this.mFont = (short) font;
            this.mTextBlockFont = textBlockFont;
        }

        public String getName() {
            return this.mName;
        }

        public short getFont() {
            return this.mFont;
        }

        public TextBlockFont getTextBlockFont() {
            return this.mTextBlockFont;
        }

        public static TextFont lookup(short font) {
            TextFont[] arr$ = values();
            for (TextFont s : arr$) {
                if (s.getFont() == font) {
                    return s;
                }
            }
            return SMALL;
        }

        public static TextFont lookup(TextBlockFont font) {
            TextFont[] arr$ = values();
            for (TextFont s : arr$) {
                if (s.getTextBlockFont() == font) {
                    return s;
                }
            }
            return SMALL;
        }
    }
}
