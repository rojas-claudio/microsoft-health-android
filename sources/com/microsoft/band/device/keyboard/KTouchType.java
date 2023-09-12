package com.microsoft.band.device.keyboard;
/* loaded from: classes.dex */
public enum KTouchType {
    Down(0),
    Move(1),
    Up(2),
    Invalid(255);
    
    private final byte mId;

    KTouchType(int id) {
        this.mId = (byte) id;
    }

    public static KTouchType valueOf(byte b) {
        KTouchType[] arr$ = values();
        for (KTouchType touchType : arr$) {
            if (touchType.mId == b) {
                return touchType;
            }
        }
        return Invalid;
    }
}
