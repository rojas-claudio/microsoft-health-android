package com.microsoft.band.tiles.pages;

import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.Validation;
import java.io.IOException;
/* loaded from: classes.dex */
public class LayoutTemplate {
    private LayoutPageHead mHead;

    public LayoutTemplate(PageLayout pageLayout) {
        Validation.notNull(pageLayout, "root element == null");
        this.mHead = new LayoutPageHead();
        this.mHead.addChild(LayoutPageElementCreator.createFromPageElement(pageLayout.getRoot()));
    }

    public LayoutTemplate(byte[] data) {
        this.mHead = new LayoutPageHead(data);
    }

    public byte[] toBytes() throws CargoException {
        try {
            byte[] layoutBlob = this.mHead.toBytes();
            if (layoutBlob.length > 768) {
                throw new CargoException(String.format("Layout blob size (%s) exceeds the limit", Integer.valueOf(layoutBlob.length)), BandServiceMessage.Response.TILE_LAYOUT_ERROR);
            }
            return layoutBlob;
        } catch (IOException e) {
            throw new CargoException("Cannot serialize to layout blob", BandServiceMessage.Response.TILE_LAYOUT_ERROR);
        }
    }

    public PageLayout toPageLayout() {
        PagePanel root = this.mHead.getRootElement();
        if (root == null) {
            return null;
        }
        return new PageLayout(root);
    }
}
