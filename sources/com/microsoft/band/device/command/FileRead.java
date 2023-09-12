package com.microsoft.band.device.command;

import com.microsoft.band.internal.CommandBase;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class FileRead extends CommandBase {
    private static final int ARG_SIZE = 0;
    private static final long serialVersionUID = 6262080627602694423L;
    private byte[] mFileBuff;

    public FileRead(CargoFileName fileName, int length) {
        super(fileName.mCmdReadFile, 0, length);
    }

    @Override // com.microsoft.band.internal.CommandBase
    public void loadResult(ByteBuffer record) {
        this.mFileBuff = record.array();
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }

    public byte[] getFileBuff() {
        return this.mFileBuff;
    }
}
