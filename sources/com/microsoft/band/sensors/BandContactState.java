package com.microsoft.band.sensors;
/* loaded from: classes.dex */
public enum BandContactState {
    NOT_WORN((byte) 0),
    WORN((byte) 1),
    UNKNOWN((byte) 2);
    
    private final byte mId;

    BandContactState(byte id) {
        this.mId = id;
    }

    public byte getId() {
        return this.mId;
    }
}
