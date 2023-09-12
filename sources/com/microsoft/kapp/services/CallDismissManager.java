package com.microsoft.kapp.services;

import com.microsoft.kapp.telephony.PhoneState;
/* loaded from: classes.dex */
public interface CallDismissManager {
    void dismissCall();

    void setPhoneState(PhoneState phoneState);
}
