package com.microsoft.band.device;

import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.util.StringHelper;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class NotificationGenericDialog {
    public static final int GENERIC_DIALOG_BASIC_STRUCTURE_SIZE = 6;
    private DeviceConstants.NotificationFlag mDialogFlag;
    private String mLine1;
    private String mLine2;

    public NotificationGenericDialog(String lineOne, String lineTwo, DeviceConstants.NotificationFlag flag) {
        checkLines(lineOne, lineTwo);
        setDialogFlag(flag);
    }

    public NotificationGenericDialog(String lineOne, String lineTwo, boolean forceDialog) {
        checkLines(lineOne, lineTwo);
        setDialogFlag(forceDialog);
    }

    public NotificationGenericDialog(String lineOne, String lineTwo) {
        checkLines(lineOne, lineTwo);
        setDialogFlag(true);
    }

    private void checkLines(String lineOne, String lineTwo) {
        if (lineOne == null) {
            lineOne = "";
        }
        if (lineTwo == null) {
            lineTwo = "";
        }
        if (StringUtil.isWhitespace(lineOne) && StringUtil.isWhitespace(lineTwo)) {
            throw new IllegalArgumentException("Both lines are Null or Empty");
        }
        this.mLine1 = StringUtil.truncateString(lineOne, 20);
        this.mLine2 = StringUtil.truncateString(lineTwo, 20);
    }

    public DeviceConstants.NotificationFlag getDialogFlag() {
        return this.mDialogFlag;
    }

    public String getLineOne() {
        return this.mLine1;
    }

    public String getLineTwo() {
        return this.mLine2;
    }

    public void setDialogFlag(DeviceConstants.NotificationFlag dialogFlag) {
        if (dialogFlag == null) {
            this.mDialogFlag = DeviceConstants.NotificationFlag.UNMODIFIED_SETTING;
        } else {
            this.mDialogFlag = dialogFlag;
        }
    }

    public void setDialogFlag(boolean forceDialog) {
        if (forceDialog) {
            this.mDialogFlag = DeviceConstants.NotificationFlag.FORCE_GENERIC_DIALOG;
        } else {
            this.mDialogFlag = DeviceConstants.NotificationFlag.UNMODIFIED_SETTING;
        }
    }

    public byte[] toBytes() {
        byte[] line1Bytes = StringHelper.getBytes(this.mLine1);
        byte[] line2Bytes = StringHelper.getBytes(this.mLine2);
        int line1BytesLength = line1Bytes.length;
        int line2BytesLength = line2Bytes.length;
        byte rsvd1 = this.mDialogFlag.getFlag();
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(line1BytesLength + 6 + line2BytesLength);
        return buffer.putShort((short) line1BytesLength).putShort((short) line2BytesLength).put(rsvd1).put((byte) 0).put(line1Bytes).put(line2Bytes).array();
    }
}
