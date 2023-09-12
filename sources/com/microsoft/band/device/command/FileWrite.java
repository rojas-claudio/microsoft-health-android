package com.microsoft.band.device.command;

import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.BufferUtil;
/* loaded from: classes.dex */
public class FileWrite extends CommandBase {
    private static final int MESSAGE_SIZE = 0;
    private static final long serialVersionUID = 9129662726160734360L;
    private byte[] mExtendedData;
    private byte[] mFileBuff;

    public FileWrite(CargoFileName fileName, byte[] buff) {
        super(fileName.mCmdWriteFile, 0, 0);
        this.mFileBuff = buff;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        if (this.mExtendedData == null) {
            this.mExtendedData = BufferUtil.allocateLittleEndian(this.mFileBuff.length).put(this.mFileBuff).array();
        }
        return this.mExtendedData;
    }
}
