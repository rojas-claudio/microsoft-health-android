package com.microsoft.band.service.subscription;

import com.microsoft.band.internal.device.subscription.SubscriptionDataModel;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
interface SubscriptionDataConstructor {
    SubscriptionDataModel createSubscriptionData(ByteBuffer byteBuffer);
}
