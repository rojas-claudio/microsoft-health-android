package com.microsoft.band.internal;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.BandServiceMessage;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: classes.dex */
public abstract class CommandBase implements Serializable {
    private static final long serialVersionUID = 0;
    private final int mArgumentSize;
    private long mCommandIndex;
    private byte[] mCommandRelatedData;
    private final BandDeviceConstants.Command mCommandType;
    private final int mMessageSize;
    private final short mPacketType;
    private int mQueueLimit;
    private boolean mResult;
    private Integer mResultCode;

    public abstract byte[] getExtendedData();

    public CommandBase(BandDeviceConstants.Command commandType) {
        this(commandType, commandType.getArgSize(), commandType.getPayloadSize());
    }

    public CommandBase(BandDeviceConstants.Command commandType, int argSize, int messageSize) {
        this.mPacketType = BandDeviceConstants.PACKET_TYPE_COMMAND;
        if (commandType == null) {
            throw new NullPointerException("CommandType cannot be null");
        }
        if (argSize < 0 || argSize > 55) {
            throw new IllegalArgumentException(String.format("argSize of %d bytes is outside of the 0-%d range.", Integer.valueOf(argSize), Byte.valueOf((byte) BandDeviceConstants.MAX_COMMAND_RELATED_DATA_SIZE)));
        }
        if (messageSize < 0 || messageSize > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(String.format("messageSize cannot be less than zero or greater than %d.", Integer.MAX_VALUE));
        }
        this.mCommandType = commandType;
        this.mArgumentSize = argSize;
        this.mMessageSize = messageSize;
        this.mResultCode = null;
    }

    public int getCommandId() {
        return this.mCommandType.getCode();
    }

    public BandDeviceConstants.Command getCommandType() {
        return this.mCommandType;
    }

    public boolean isTxCommand() {
        return this.mCommandType.isTX();
    }

    public boolean getResult() {
        return this.mResult;
    }

    public void setResult(boolean result) {
        this.mResult = result;
    }

    public boolean hasResponse() {
        return this.mResultCode != null;
    }

    public int getQueueLimit() {
        return this.mQueueLimit;
    }

    public void setQueueLimit(int value) {
        this.mQueueLimit = value;
    }

    public int getResultCode() {
        if (this.mResultCode == null) {
            return 0;
        }
        return this.mResultCode.intValue();
    }

    public void setResultCode(int resultCode) {
        this.mResultCode = Integer.valueOf(resultCode);
        setResult(!isResultCodeSevere());
    }

    public String getResultString() {
        int rCode = getResultCode();
        byte facility = BandDeviceConstants.getFacilityCodeFromResultCode(rCode);
        if (rCode != 0) {
            if (facility == BandDeviceConstants.Facility.CARGO_SERVICE.getCode()) {
                BandServiceMessage.Response[] arr$ = BandServiceMessage.Response.values();
                for (BandServiceMessage.Response item : arr$) {
                    if (item.getCode() == rCode) {
                        return "response: " + item.toString();
                    }
                }
            } else {
                BandDeviceConstants.ResultCode[] arr$2 = BandDeviceConstants.ResultCode.values();
                for (BandDeviceConstants.ResultCode item2 : arr$2) {
                    if (item2.getCode() == rCode) {
                        return "result code: " + item2.toString();
                    }
                }
            }
        }
        Boolean severeResult = Boolean.valueOf(BandDeviceConstants.isResultSevere(rCode));
        int code = getResultCode() & 65535;
        Object[] objArr = new Object[2];
        objArr[0] = severeResult.booleanValue() ? "severe" : "non-severe";
        objArr[1] = Integer.valueOf(code);
        return String.format("unknown %s result code %X", objArr);
    }

    public BandServiceMessage.Response getResponseCode() {
        int rCode = getResultCode();
        byte facility = BandDeviceConstants.getFacilityCodeFromResultCode(rCode);
        if (rCode != 0 && facility == BandDeviceConstants.Facility.CARGO_SERVICE.getCode()) {
            BandServiceMessage.Response[] arr$ = BandServiceMessage.Response.values();
            for (BandServiceMessage.Response item : arr$) {
                if (item.getCode() == rCode) {
                    return item;
                }
            }
        }
        return null;
    }

    public boolean isResultCodeSevere() {
        return BandDeviceConstants.isResultSevere(getResultCode());
    }

    public short getPacketType() {
        return BandDeviceConstants.PACKET_TYPE_COMMAND;
    }

    public int getArgSize() {
        return this.mArgumentSize;
    }

    public int getMessageSize() {
        byte[] payload;
        int messageSize = this.mMessageSize;
        if (messageSize == 0 && (payload = getExtendedData()) != null) {
            return payload.length;
        }
        return messageSize;
    }

    public byte[] getCommandRelatedData() {
        return this.mCommandRelatedData;
    }

    public void setCommandIndex(long index) {
        this.mCommandIndex = index;
    }

    public long getCommandIndex() {
        return this.mCommandIndex;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setCommandRelatedData(byte[] data) {
        if (data == null || data.length != this.mArgumentSize) {
            throw new IllegalArgumentException("Data cannot be null and must have the same length as the argument size for this command");
        }
        this.mCommandRelatedData = data;
    }

    public void processResponse(int resultCode, byte[] responseData) throws IOException {
        setResultCode(resultCode);
        if (isTxCommand()) {
            long payloadSize = getMessageSize();
            if (responseData == null) {
                throw new IOException("Expecting a payload in command response.");
            }
            if (responseData.length != payloadSize) {
                throw new IOException(String.format("Expecting a payload of %d bytes, but got %d bytes instead.", Long.valueOf(payloadSize), Integer.valueOf(responseData.length)));
            }
            loadResult(ByteBuffer.wrap(responseData).order(ByteOrder.LITTLE_ENDIAN));
        }
    }

    public void loadResult(ByteBuffer buffer) {
    }

    protected static String truncateString(String str, int maxChars) {
        int limit = Math.min(str.length(), maxChars);
        return str.substring(0, limit);
    }
}
