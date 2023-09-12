package com.microsoft.band.device.command;
/* loaded from: classes.dex */
public enum SmsResponseType {
    CALL(0),
    SMS(1);
    
    public final int mId;

    SmsResponseType(int value) {
        this.mId = value;
    }
}
