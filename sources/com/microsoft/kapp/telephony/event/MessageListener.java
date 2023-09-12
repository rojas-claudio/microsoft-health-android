package com.microsoft.kapp.telephony.event;

import java.util.EventListener;
/* loaded from: classes.dex */
public interface MessageListener extends EventListener {
    void messageDeleted(MessageEvent messageEvent);

    void messageRead(MessageEvent messageEvent);

    void messageReceived(MessageEvent messageEvent);
}
