package com.microsoft.band.service.command;

import android.support.annotation.NonNull;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.service.device.DeviceServiceProvider;
/* loaded from: classes.dex */
public interface BatchCommand {
    BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceServiceProvider);
}
