package com.microsoft.band.service.subscription;

import com.microsoft.band.service.device.PushServicePayload;
import java.util.BitSet;
/* loaded from: classes.dex */
public interface PushConnectionListener {
    int getSubscriptionCount();

    void onPushPacketReceived(PushServicePayload pushServicePayload);

    void updateSubscriptionsSet(BitSet bitSet);
}
