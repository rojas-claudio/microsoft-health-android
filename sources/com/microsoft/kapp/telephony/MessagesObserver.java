package com.microsoft.kapp.telephony;

import com.microsoft.kapp.telephony.event.MessageListener;
/* loaded from: classes.dex */
public interface MessagesObserver {
    void addListener(MessageListener messageListener);

    void removeListener(MessageListener messageListener);
}
