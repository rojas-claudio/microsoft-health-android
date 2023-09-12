package com.microsoft.band.device;

import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.tiles.pages.LayoutPageElementType;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class StrappIconbox extends StrappPageElement {
    private static final int STRAPP_ICONBOX_BASIC_STRUCTURE_SIZE = 2;
    private static final long serialVersionUID = 1;
    private int mIconIndex;

    public StrappIconbox(int elementId, int iconIndex) {
        super(elementId);
        setIconIndex(iconIndex);
    }

    public int getIconIndex() {
        return this.mIconIndex;
    }

    public void setIconIndex(int iconIndex) {
        this.mIconIndex = iconIndex;
    }

    @Override // com.microsoft.band.device.StrappPageElement
    public void validate(int hardwareVersion) {
        Validation.validateInRange("IconIndex", this.mIconIndex, 0, InternalBandConstants.getMaxIconsPerTile(hardwareVersion));
    }

    @Override // com.microsoft.band.device.StrappPageElement
    protected byte[] toSpecializedBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(2);
        return buffer.putShort((short) this.mIconIndex).array();
    }

    @Override // com.microsoft.band.device.StrappPageElement
    protected LayoutPageElementType getElementType() {
        return LayoutPageElementType.ELEMENT_TYPE_ICON;
    }
}
