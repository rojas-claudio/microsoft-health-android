package com.microsoft.band.tiles.pages;

import android.support.v4.media.session.PlaybackStateCompat;
import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class LayoutFlowList extends LayoutPagePanel {
    public LayoutFlowList(FlowPanel flowList) {
        super(flowList);
        switch (flowList.getFlowPanelOrientation()) {
            case HORIZONTAL:
                this.mCustomStyleMask = FlowPanelStyleMask.HORIZONTAL.getMaskBit();
                return;
            case VERTICAL:
                this.mCustomStyleMask = FlowPanelStyleMask.VERTICAL.getMaskBit();
                return;
            default:
                return;
        }
    }

    public LayoutFlowList(ByteBuffer buffer) {
        super(buffer);
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPagePanel
    protected void createSpecificPagePanel() {
        this.mPageElement = new FlowPanel(this.mRect.getPageRect(), FlowPanelStyleMask.HORIZONTAL.checkBit(this.mStyleMask) ? FlowPanelOrientation.HORIZONTAL : FlowPanelOrientation.VERTICAL, new PageElement[0]);
    }

    @Override // com.microsoft.band.tiles.pages.LayoutPagePanel, com.microsoft.band.tiles.pages.LayoutPageElement
    protected byte[] toSpecializedBytes() {
        return BufferUtil.EMPTY;
    }

    /* loaded from: classes.dex */
    public enum FlowPanelStyleMask {
        HORIZONTAL("Horizontal", PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH),
        VERTICAL("Vertical", 4096);
        
        private final String mName;
        private final long mStyleMask;

        FlowPanelStyleMask(String name, long bit) {
            this.mName = name;
            this.mStyleMask = bit;
        }

        public String getName() {
            return this.mName;
        }

        public long getMaskBit() {
            return this.mStyleMask;
        }

        public static FlowPanelStyleMask fromName(String v) {
            FlowPanelStyleMask[] arr$ = values();
            for (FlowPanelStyleMask c : arr$) {
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
}
