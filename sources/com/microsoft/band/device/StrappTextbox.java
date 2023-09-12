package com.microsoft.band.device;

import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.tiles.pages.LayoutPageElementType;
import com.microsoft.band.util.StringHelper;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class StrappTextbox extends StrappPageElement {
    private static int STRAPP_TEXTBOX_BASIC_STRUCTURE_SIZE = 2;
    private static final long serialVersionUID = 1;
    private String mTextboxValue;

    public StrappTextbox(int elementId, String textboxValue) {
        super(elementId);
        setTextboxValue(textboxValue);
    }

    public String getTextboxValue() {
        return this.mTextboxValue;
    }

    public void setTextboxValue(String textboxValue) {
        Validation.validateNullParameter(textboxValue, "Textbox Value");
        if (textboxValue.isEmpty()) {
            textboxValue = MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE;
        }
        this.mTextboxValue = StringUtil.truncateString(textboxValue, 160);
    }

    @Override // com.microsoft.band.device.StrappPageElement
    protected byte[] toSpecializedBytes() {
        byte[] textboxValueBytes = StringHelper.getBytes(this.mTextboxValue);
        int textboxValueBytesLength = textboxValueBytes.length;
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(STRAPP_TEXTBOX_BASIC_STRUCTURE_SIZE + textboxValueBytesLength);
        return buffer.putShort((short) this.mTextboxValue.length()).put(textboxValueBytes).array();
    }

    @Override // com.microsoft.band.device.StrappPageElement
    protected LayoutPageElementType getElementType() {
        return LayoutPageElementType.ELEMENT_TYPE_TEXT;
    }
}
