package com.microsoft.band.device;

import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.tiles.pages.BarcodeData;
import com.microsoft.band.tiles.pages.FilledButtonData;
import com.microsoft.band.tiles.pages.IconData;
import com.microsoft.band.tiles.pages.LayoutPageElementType;
import com.microsoft.band.tiles.pages.PageElementData;
import com.microsoft.band.tiles.pages.TextBlockData;
import com.microsoft.band.tiles.pages.TextButtonData;
import com.microsoft.band.tiles.pages.WrappedTextBlockData;
import java.io.Serializable;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public abstract class StrappPageElement implements Serializable {
    protected static final int STRAPP_PAGE_ELEMENT_BASIC_STRUCTURE_SIZE = 4;
    private static final long serialVersionUID = 1;
    private int mElementId;

    protected abstract LayoutPageElementType getElementType();

    protected abstract byte[] toSpecializedBytes();

    public StrappPageElement(int elementId) {
        Validation.validateInRange("elementId", elementId, 0, 65535);
        this.mElementId = elementId;
    }

    int getElementId() {
        return this.mElementId;
    }

    private byte[] toBaseBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(4);
        return buffer.putShort(BitHelper.intToUnsignedShort(getElementType().getType())).putShort((short) this.mElementId).array();
    }

    public void validate(int hardwareVersion) {
    }

    public byte[] toBytes() {
        byte[] baseArray = toBaseBytes();
        byte[] specializedArray = toSpecializedBytes();
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(getBytesLength());
        return buffer.put(baseArray).put(specializedArray).array();
    }

    public int getBytesLength() {
        return toSpecializedBytes().length + 4;
    }

    public String toString() {
        return String.format("StrappPageElement:%s", System.getProperty("line.separator")) + String.format("     |--Element id = %d %s", Integer.valueOf(this.mElementId), System.getProperty("line.separator")) + String.format("     |--Element type = %d %s", Integer.valueOf(getElementType().getType()), System.getProperty("line.separator")) + String.format("     |--bytesLength = %d %s", Integer.valueOf(getBytesLength()), System.getProperty("line.separator"));
    }

    public static StrappPageElement from(PageElementData data) throws CargoException {
        if (data instanceof BarcodeData) {
            return new StrappBarcode(data.getId(), ((BarcodeData) data).getBarcodeType(), ((BarcodeData) data).getBarCode());
        }
        if (data instanceof IconData) {
            return new StrappIconbox(data.getId(), ((IconData) data).getIconIndex());
        }
        if (data instanceof TextBlockData) {
            return new StrappTextbox(data.getId(), ((TextBlockData) data).getText());
        }
        if (data instanceof WrappedTextBlockData) {
            return new StrappWrappableTextbox(data.getId(), ((WrappedTextBlockData) data).getText());
        }
        if (data instanceof TextButtonData) {
            return new StrappTextButton(data.getId(), ((TextButtonData) data).getText());
        }
        if (data instanceof FilledButtonData) {
            return new StrappFilledButton(data.getId(), ((FilledButtonData) data).getPressedColor(), ((FilledButtonData) data).getPressedColorSource());
        }
        throw new CargoException("Unsupported element " + data.getClass().getSimpleName(), BandServiceMessage.Response.TILE_PAGE_DATA_ERROR);
    }
}
