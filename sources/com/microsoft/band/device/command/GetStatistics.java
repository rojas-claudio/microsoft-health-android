package com.microsoft.band.device.command;

import com.microsoft.band.device.CommandStruct;
import com.microsoft.band.internal.CommandBase;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class GetStatistics<T extends CommandStruct> extends CommandBase {
    private static final long serialVersionUID = 6403224216225373097L;
    private T mStatisticsStruct;

    public GetStatistics(T struct) {
        super(struct.getCommand(), 0, struct.getSize());
        this.mStatisticsStruct = struct;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public void loadResult(ByteBuffer buffer) {
        this.mStatisticsStruct.parseData(buffer);
    }

    public T getStatisticsStruct() {
        return this.mStatisticsStruct;
    }
}
