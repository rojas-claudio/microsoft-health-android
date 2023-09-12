package com.microsoft.band.device.keyboard;
/* loaded from: classes.dex */
public enum KeyboardMessageType {
    Init(0),
    Stroke(1),
    CandidatesForNextWord(2),
    CandidatesForWord(3),
    End(4),
    PreInit(5),
    TryReleaseClient(6),
    PreInitV2(7),
    Invalid(255);
    
    private final byte mId;

    KeyboardMessageType(int id) {
        this.mId = (byte) id;
    }

    public byte getId() {
        return this.mId;
    }

    public static KeyboardMessageType valueOf(byte b) {
        KeyboardMessageType[] arr$ = values();
        for (KeyboardMessageType message : arr$) {
            if (message.mId == b) {
                return message;
            }
        }
        return Invalid;
    }
}
