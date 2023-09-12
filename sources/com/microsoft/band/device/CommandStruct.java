package com.microsoft.band.device;

import com.microsoft.band.internal.BandDeviceConstants;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public interface CommandStruct {
    BandDeviceConstants.Command getCommand();

    int getSize();

    void parseData(ByteBuffer byteBuffer);
}
