package com.microsoft.band.service.device;

import java.io.Serializable;
/* loaded from: classes.dex */
public class SensorLogDownload implements Serializable {
    private static final long serialVersionUID = 1;
    private byte[] mData;
    private SensorLogMetadata mMeta;

    public void initializeSensorLogDownload(byte[] data, SensorLogMetadata meta) {
        this.mData = data;
        this.mMeta = meta;
    }

    public byte[] getData() {
        return this.mData;
    }

    public void setData(byte[] data) {
        this.mData = data;
    }

    public SensorLogMetadata getMeta() {
        return this.mMeta;
    }

    public void setMeta(SensorLogMetadata meta) {
        this.mMeta = meta;
    }
}
