package com.microsoft.band.device;

import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.UUIDHelper;
import com.microsoft.band.internal.util.Validation;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public class NotificationGenericUpdate implements Serializable {
    public static int GENERIC_UPDATE_BASIC_STRUCTURE_SIZE = 22;
    private static final long serialVersionUID = 1;
    private List<StrappPageElement> mData;
    private int mGenericUpdateLen;
    private UUID mPageId;
    private int mPageLayoutIndex;
    private byte mRsvd1;
    private byte mRsvd2;

    public NotificationGenericUpdate(UUID pageId, int pageLayoutIndex, List<StrappPageElement> textFields) {
        Validation.validateNullParameter(pageId, "NotificationGenericUpdate: pageId");
        this.mPageId = pageId;
        Validation.validateInRange("NotificationGenericUpdate:pageLayoutIndex", pageLayoutIndex, 0, 4);
        this.mPageLayoutIndex = pageLayoutIndex;
        Validation.validateNullParameter(textFields, "NotificationGenericUpdate: textFields");
        Validation.validateZeroCount("NotificationGenericUpdate:textFields count", textFields.size());
        this.mGenericUpdateLen = 0;
        for (int i = 0; i < textFields.size(); i++) {
            StrappPageElement element = textFields.get(i);
            Validation.validateNullParameter(element, "NotificationGenericUpdate: textFields element");
            this.mGenericUpdateLen += element.getBytesLength();
        }
        Validation.validateInRange("NotificationGenericUpdate:textFieldLength", this.mGenericUpdateLen, 0, DeviceConstants.NOTIFICATION_GENERIC_UPDATE_LINE_MAX_LENGTH);
        this.mData = textFields;
        this.mRsvd1 = (byte) 0;
        this.mRsvd2 = (byte) 0;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(GENERIC_UPDATE_BASIC_STRUCTURE_SIZE + this.mGenericUpdateLen).putShort((short) this.mGenericUpdateLen).putShort((short) this.mPageLayoutIndex).put(UUIDHelper.toGuidArray(this.mPageId)).put(this.mRsvd1).put(this.mRsvd2);
        for (int i = 0; i < this.mData.size(); i++) {
            buffer.put(this.mData.get(i).toBytes());
        }
        return buffer.array();
    }

    public byte[] getDBlob() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < this.mData.size(); i++) {
            buffer.write(this.mData.get(i).toBytes());
        }
        return buffer.toByteArray();
    }
}
