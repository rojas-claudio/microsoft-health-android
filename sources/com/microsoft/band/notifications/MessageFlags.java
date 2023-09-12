package com.microsoft.band.notifications;
/* loaded from: classes.dex */
public enum MessageFlags {
    NONE(0),
    SHOW_DIALOG(1);
    
    private final byte mFlag;

    MessageFlags(int b) {
        this.mFlag = (byte) (b & 255);
    }

    public byte getFlag() {
        return this.mFlag;
    }
}
