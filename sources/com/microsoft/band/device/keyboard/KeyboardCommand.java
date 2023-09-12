package com.microsoft.band.device.keyboard;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.util.StringHelper;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class KeyboardCommand extends CommandBase {
    private static final int MAX_KBDCMD_DATA_LEN = 400;
    private static final int MAX_NUM_OF_CANDIDATES = 4;
    private static final int STRUCT_SIZE = 407;
    private static final long serialVersionUID = 1;
    private ByteBuffer mBufferWrappingDataField;
    private ArrayList<String> mCandidates;
    private int mDataLength;
    private byte[] mDatafield;
    private byte[] mEncodedNull;
    private KeyboardMessageType mKeyboardMsgType;
    private byte mNumOfCandidates;
    private byte mWordIndex;
    private static final String TAG = KeyboardCommand.class.getSimpleName();
    private static final String KEYBOARD_TAG = TAG + ": " + InternalBandConstants.KEYBOARD_BASE_TAG;

    public KeyboardCommand(KeyboardMessageType keyboardMsgType) {
        super(BandDeviceConstants.Command.CargoKeyboardCommand, 0, STRUCT_SIZE);
        this.mEncodedNull = StringHelper.getBytes("\u0000");
        this.mKeyboardMsgType = keyboardMsgType;
        this.mNumOfCandidates = (byte) 0;
        this.mWordIndex = (byte) 0;
        this.mDataLength = 0;
        this.mDatafield = new byte[MAX_KBDCMD_DATA_LEN];
        this.mCandidates = new ArrayList<>();
        this.mBufferWrappingDataField = ByteBuffer.wrap(this.mDatafield);
    }

    public KeyboardCommand(KeyboardMessageType keyboardMsgType, List<String> candidates) {
        this(keyboardMsgType);
        addCandidates(candidates);
    }

    public KeyboardMessageType getKeyboardMsgType() {
        return this.mKeyboardMsgType;
    }

    private void addCandidates(List<String> candidates) {
        int candidateIndex = 0;
        for (String candidate : candidates) {
            if (!addCandidate(candidate)) {
                break;
            }
            KDKLog.d(KEYBOARD_TAG, "Keyboard Candidate " + candidateIndex + " : " + candidate);
            candidateIndex++;
        }
        KDKLog.d(KEYBOARD_TAG, "Finished adding " + candidateIndex + " candidates out of  " + candidates.size());
    }

    private boolean addCandidate(String candidate) {
        if (this.mCandidates.size() < 4 && this.mDataLength < MAX_KBDCMD_DATA_LEN) {
            byte[] data = StringHelper.getBytes(candidate);
            if (this.mDataLength + data.length + this.mEncodedNull.length < MAX_KBDCMD_DATA_LEN) {
                this.mCandidates.add(candidate);
                this.mBufferWrappingDataField.put(data);
                this.mBufferWrappingDataField.put(this.mEncodedNull);
                this.mDataLength = this.mBufferWrappingDataField.position();
                this.mNumOfCandidates = (byte) (this.mNumOfCandidates + 1);
                return true;
            }
            return false;
        }
        return false;
    }

    private byte[] toBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(STRUCT_SIZE);
        buffer.put(this.mKeyboardMsgType.getId());
        buffer.put(this.mNumOfCandidates);
        buffer.put(this.mWordIndex);
        buffer.putInt(this.mDataLength);
        buffer.put(this.mDatafield);
        return buffer.array();
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return toBytes();
    }
}
