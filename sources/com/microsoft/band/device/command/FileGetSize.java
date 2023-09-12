package com.microsoft.band.device.command;

import com.microsoft.band.internal.CommandBase;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class FileGetSize extends CommandBase {
    private static final int ARG_SIZE = 0;
    private static final byte MESSAGE_SIZE = 4;
    private static final long serialVersionUID = 4111903560729905162L;
    private int mFileSize;

    public FileGetSize(CargoFileName fileName) {
        super(fileName.mCmdGetSize, 0, 4);
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public void loadResult(ByteBuffer record) {
        this.mFileSize = record.getInt();
    }

    public int getFileSize() {
        return this.mFileSize;
    }
}
