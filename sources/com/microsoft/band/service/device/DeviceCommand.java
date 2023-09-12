package com.microsoft.band.service.device;

import android.os.Bundle;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.KDKLog;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Locale;
/* loaded from: classes.dex */
public class DeviceCommand {
    private final int mArgSize;
    private final CommandBase mCommand;
    private volatile byte[] mCommandBuffer;
    private final int mCommandId;
    private final long mCommandIndex;
    private volatile int mCommandResultCode;
    private BandDeviceConstants.Command mCommandType;
    private volatile boolean mHasResponse;
    private volatile byte[] mPayload;
    private volatile int mPayloadSize;
    private int mQueueLimit;
    private volatile int mResultCode;
    private static final String TAG = DeviceCommand.class.getSimpleName();
    public static int MIN_USB_COMMAND_HEADER_SIZE = 8;
    public static int MIN_BT_COMMAND_HEADER_SIZE = MIN_USB_COMMAND_HEADER_SIZE + 1;
    private static final ArrayList<Integer> mCanRetryResultCodes = new ArrayList<>();

    static {
        mCanRetryResultCodes.add(Integer.valueOf(BandDeviceConstants.ResultCode.BATTERY_READ_BUSY_ERROR.getCode()));
        mCanRetryResultCodes.add(Integer.valueOf(BandDeviceConstants.ResultCode.LOGGER_BUSY_ERROR.getCode()));
        mCanRetryResultCodes.add(Integer.valueOf(BandDeviceConstants.ResultCode.LOGGER_FLASH_OPERATION_IN_PROGRESS_ERROR.getCode()));
    }

    public int getCommandId() {
        return this.mCommandId;
    }

    public BandDeviceConstants.Command getCommandType() {
        if (this.mCommandType == null || this.mCommandType.getCode() != getCommandId()) {
            this.mCommandType = BandDeviceConstants.Command.lookup(getCommandId());
        }
        return this.mCommandType;
    }

    public int getQueueLimit() {
        return this.mQueueLimit;
    }

    public void setQueueLimit(int value) {
        this.mQueueLimit = value;
    }

    public boolean isReceivingPayload() {
        return BandDeviceConstants.isTxCommand(this.mCommandId);
    }

    public byte[] getCommandBuffer() {
        return this.mCommandBuffer;
    }

    public int getCommandDataSize() {
        return MIN_BT_COMMAND_HEADER_SIZE + this.mArgSize;
    }

    public int getBufferOffsetToStatusPacket() {
        return MIN_BT_COMMAND_HEADER_SIZE + this.mArgSize;
    }

    public byte[] getPayload() {
        return this.mPayload;
    }

    public int getPayloadSize() {
        return this.mPayloadSize;
    }

    public ByteBuffer getArgBuffer() {
        return ByteBuffer.wrap(this.mCommandBuffer, MIN_BT_COMMAND_HEADER_SIZE, this.mArgSize).order(ByteOrder.LITTLE_ENDIAN);
    }

    public void setPayloadSize(int payloadSize) {
        byte[] payload;
        if (payloadSize > 0 && (payload = this.mPayload) != null && payload.length < payloadSize) {
            throw new IllegalArgumentException(String.format("Payload size of %d is will exceed payload buffer size of %d", Integer.valueOf(payloadSize), Integer.valueOf(payload.length)));
        }
        this.mPayloadSize = payloadSize;
        ByteBuffer buffer = ByteBuffer.wrap(getCommandBuffer(), 5, 4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(payloadSize);
    }

    public int getResultCode() {
        return this.mCommandResultCode;
    }

    public void setResultCode(int resultCode) {
        this.mCommandResultCode = resultCode;
        if (this.mCommand != null) {
            this.mCommand.setResultCode(resultCode);
            if (BandDeviceConstants.isResultSevere(resultCode)) {
                this.mCommand.setResult(false);
                return;
            }
            this.mCommand.setResult(true);
            if (isReceivingPayload() && hasResponse()) {
                ByteBuffer buffer = ByteBuffer.wrap(getPayload(), 0, getPayloadSize()).order(ByteOrder.LITTLE_ENDIAN);
                this.mCommand.loadResult(buffer);
            }
        }
    }

    public boolean canRetry() {
        return mCanRetryResultCodes.contains(Integer.valueOf(this.mCommandResultCode));
    }

    public CommandBase getCommand() {
        return this.mCommand;
    }

    public boolean hasResponse() {
        return this.mHasResponse;
    }

    public boolean isResultSuccessful() {
        return hasResponse() && !BandDeviceConstants.isResultSevere(this.mResultCode);
    }

    public long getCommandIndex() {
        return this.mCommandIndex;
    }

    public DeviceCommand(CommandBase command) throws IllegalArgumentException {
        if (command == null) {
            throw new NullPointerException("command");
        }
        this.mCommand = command;
        this.mCommandId = command.getCommandId();
        byte[] argData = command.getCommandRelatedData();
        this.mArgSize = BufferUtil.length(argData);
        byte[] payload = command.getExtendedData();
        int payloadSize = command.getMessageSize();
        if (payload != null && payloadSize == 0) {
            payloadSize = payload.length;
        }
        this.mPayloadSize = payloadSize;
        this.mCommandBuffer = createBuffer(argData, payload);
        this.mCommandIndex = command.getCommandIndex();
        this.mQueueLimit = command.getQueueLimit();
    }

    public DeviceCommand(int commandId, Bundle bundle) throws IllegalArgumentException {
        if (bundle == null) {
            throw new NullPointerException("bundle");
        }
        byte[] argData = bundle.getByteArray(InternalBandConstants.EXTRA_COMMAND_DATA);
        byte[] payload = bundle.getByteArray(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
        int payloadSize = bundle.getInt(InternalBandConstants.EXTRA_COMMAND_PAYLOAD_SIZE);
        this.mCommand = null;
        this.mCommandId = commandId;
        this.mArgSize = BufferUtil.length(argData);
        this.mPayloadSize = payloadSize;
        this.mCommandBuffer = createBuffer(argData, payload);
        this.mCommandIndex = bundle.getLong(InternalBandConstants.EXTRA_COMMAND_INDEX);
        this.mQueueLimit = bundle.getInt(InternalBandConstants.EXTRA_COMMAND_QUEUE_LIMIT, 0);
    }

    public DeviceCommand(int commandId, byte[] argData, byte[] payload, int payloadSize) throws IllegalArgumentException {
        this.mCommand = null;
        this.mCommandId = commandId;
        this.mArgSize = BufferUtil.length(argData);
        this.mPayloadSize = payloadSize;
        this.mCommandBuffer = createBuffer(argData, payload);
        this.mCommandIndex = System.currentTimeMillis();
    }

    public DeviceCommand(BandDeviceConstants.Command commandType) {
        this(commandType, null, null);
    }

    public DeviceCommand(BandDeviceConstants.Command commandType, byte[] argData) {
        this(commandType, argData, null);
    }

    public DeviceCommand(BandDeviceConstants.Command commandType, byte[] argData, byte[] payload) {
        this.mCommand = null;
        this.mCommandId = commandType.getCode();
        this.mArgSize = commandType.getArgSize();
        this.mPayloadSize = payload == null ? commandType.getPayloadSize() : payload.length;
        this.mCommandBuffer = createBuffer(argData, payload);
        this.mCommandIndex = System.currentTimeMillis();
    }

    public String toString() {
        return String.format(Locale.getDefault(), "Command(%04X), argSize = %s, payloadSize = %d, receiving payload = %s", Integer.valueOf(this.mCommandId & 65535), Integer.valueOf(this.mArgSize), Integer.valueOf(this.mPayloadSize), String.valueOf(isReceivingPayload()));
    }

    public boolean equals(Object obj) {
        boolean isEqual = obj == this;
        if (!isEqual && (obj instanceof DeviceCommand)) {
            DeviceCommand other = (DeviceCommand) obj;
            isEqual = other.getCommandId() == getCommandId();
            if (isEqual && getCommandId() == BandDeviceConstants.Command.CargoNotification.getCode() && getPayloadSize() >= 18) {
                byte[] otherPayload = other.getPayload();
                for (int i = 0; i < 18 && isEqual; i++) {
                    isEqual = otherPayload[i] == this.mPayload[i];
                }
            }
        }
        return isEqual;
    }

    public int hashCode() {
        return getCommandId();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BandServiceMessage.Response processResponse() {
        BandServiceMessage.Response response = BandServiceMessage.Response.DEVICE_DATA_ERROR;
        ByteBuffer record = ByteBuffer.wrap(this.mCommandBuffer, getBufferOffsetToStatusPacket(), 6);
        record.order(ByteOrder.LITTLE_ENDIAN);
        short packetType = record.getShort();
        if (-22786 == packetType) {
            int resultCode = record.getInt();
            if (resultCode == 0) {
                resultCode = BandServiceMessage.Response.SUCCESS.getCode();
            }
            this.mHasResponse = true;
            setResultCode(resultCode);
            if (BandDeviceConstants.isResultSevere(resultCode)) {
                BandServiceMessage.Response response2 = BandServiceMessage.Response.DEVICE_COMMAND_RESPONSE_ERROR;
                String str = TAG;
                Object[] objArr = new Object[3];
                objArr[0] = getCommandType();
                objArr[1] = Integer.valueOf(resultCode);
                objArr[2] = canRetry() ? "can retry command.." : "command failed";
                KDKLog.e(str, "%s Received severe result %08X, %s.", objArr);
                if (resultCode == BandDeviceConstants.ResultCode.TIME_SYNC_DISABLED.getCode()) {
                    BandServiceMessage.Response response3 = BandServiceMessage.Response.DEVICE_TIME_SYNC_DISABLE_ERROR;
                    return response3;
                } else if (resultCode == BandDeviceConstants.ResultCode.FBUI_SYNC_IN_PROGRESS.getCode()) {
                    BandServiceMessage.Response response4 = BandServiceMessage.Response.DEVICE_FBUI_SYNC_IN_PROGRESS_ERROR;
                    return response4;
                } else if (resultCode == BandDeviceConstants.ResultCode.NOTIFICATION_GENERIC_DATA_MULT_LONG_STR_NOT_SUPP.getCode()) {
                    BandServiceMessage.Response response5 = BandServiceMessage.Response.DEVICE_NOTIFICATION_DATA_LONG_STRING_ERROR;
                    return response5;
                } else if (resultCode == BandDeviceConstants.ResultCode.FILE_ALREADY_OPEN.getCode()) {
                    BandServiceMessage.Response response6 = BandServiceMessage.Response.DEVICE_FILE_ALREADY_OPEN_ERROR;
                    return response6;
                } else if (resultCode == BandDeviceConstants.ResultCode.INSTALLED_APP_LIST_APP_NOT_FOUND.getCode()) {
                    BandServiceMessage.Response response7 = BandServiceMessage.Response.TILE_NOT_FOUND_ERROR;
                    return response7;
                } else if (resultCode == BandDeviceConstants.ResultCode.NOTIFICATION_GENERIC_DATA_NO_LAYOUT.getCode()) {
                    BandServiceMessage.Response response8 = BandServiceMessage.Response.TILE_LAYOUT_INDEX_ERROR;
                    return response8;
                } else {
                    return response2;
                }
            }
            KDKLog.i(TAG, "%s Received status result %08X.", getCommandType(), Integer.valueOf(resultCode));
            BandServiceMessage.Response response9 = BandServiceMessage.Response.SUCCESS;
            return response9;
        }
        return response;
    }

    private byte[] createBuffer(byte[] argData, byte[] payload) {
        if (this.mCommandId <= 0 || this.mCommandId > 65535) {
            throw new IllegalArgumentException(String.format("commandID %04X is invalid.", Integer.valueOf(this.mCommandId)));
        }
        if (this.mArgSize > 55) {
            throw new IllegalArgumentException(String.format("argData size of %d cannot exceed the %d bytes limit.", Integer.valueOf(this.mArgSize), Byte.valueOf((byte) BandDeviceConstants.MAX_COMMAND_RELATED_DATA_SIZE)));
        }
        if (this.mPayloadSize < 0) {
            throw new IllegalArgumentException("payloadSize cannot be less than zero.");
        }
        if (BandDeviceConstants.isTxCommand(this.mCommandId)) {
            if (this.mPayloadSize == 0) {
                throw new IllegalArgumentException("payloadSize cannot be zero if command is receiving a payload.");
            }
        } else if (this.mPayloadSize > 0) {
            if (this.mPayloadSize > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(String.format("Payload size cannot exceed %d bytes.", Integer.MAX_VALUE));
            }
            if (BufferUtil.length(payload) != this.mPayloadSize) {
                throw new IllegalArgumentException(String.format("payloadSize of %d bytes to send, but payload data has %d bytes.", Integer.valueOf(BufferUtil.length(payload))));
            }
        }
        int commandBufferSize = MIN_BT_COMMAND_HEADER_SIZE + this.mArgSize + 6;
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(commandBufferSize);
        buffer.put((byte) (MIN_USB_COMMAND_HEADER_SIZE + this.mArgSize));
        buffer.putShort(BandDeviceConstants.PACKET_TYPE_COMMAND);
        buffer.putShort((short) this.mCommandId);
        buffer.putInt(this.mPayloadSize);
        if (this.mArgSize > 0 && argData != null) {
            buffer.put(argData);
        }
        this.mPayload = payload;
        if (BandDeviceConstants.isTxCommand(this.mCommandId) && this.mPayloadSize > 0 && this.mPayload == null) {
            this.mPayload = new byte[this.mPayloadSize];
        }
        return buffer.array();
    }
}
