package com.microsoft.band.device;

import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.tiles.pages.BarcodeType;
import com.microsoft.band.tiles.pages.LayoutPageElementType;
import com.microsoft.band.util.StringHelper;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class StrappBarcode extends StrappPageElement {
    private static final int STRAPP_BARCODE_BASIC_STRUCTURE_SIZE = 2;
    private static final long serialVersionUID = 1;
    private BarcodeType mBarcodeType;
    private String mBarcodeValue;

    public StrappBarcode(int elementId, BarcodeType codeType, String barcodeValue) {
        super(elementId);
        setBarcodeType(codeType);
        setBarcodeValue(barcodeValue);
    }

    public BarcodeType getBarcodeType() {
        return this.mBarcodeType;
    }

    public void setBarcodeType(BarcodeType barcodeType) {
        Validation.validateNullParameter(barcodeType, "Barcode Type");
        this.mBarcodeType = barcodeType;
    }

    public String getBarcodeValue() {
        return this.mBarcodeValue;
    }

    public void setBarcodeValue(String barcodeValue) {
        Validation.validateNullAndEmptyString(barcodeValue, "BarcodeValue");
        Validation.validStringNullAndLength(barcodeValue, 192, "BarcodeValue");
        this.mBarcodeValue = barcodeValue;
    }

    @Override // com.microsoft.band.device.StrappPageElement
    protected byte[] toSpecializedBytes() {
        byte[] barcodeValueBytes = StringHelper.getBytes(this.mBarcodeValue);
        int barcodeValueBytesLength = barcodeValueBytes.length;
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(barcodeValueBytesLength + 2);
        return buffer.putShort((short) this.mBarcodeValue.length()).put(barcodeValueBytes).array();
    }

    @Override // com.microsoft.band.device.StrappPageElement
    protected LayoutPageElementType getElementType() {
        switch (this.mBarcodeType) {
            case CODE39:
                return LayoutPageElementType.ELEMENT_TYPE_BARCODE_CODE39;
            case PDF417:
                return LayoutPageElementType.ELEMENT_TYPE_BARCODE_PDF417;
            default:
                throw new IllegalArgumentException("Invalid Barcode Type");
        }
    }
}
