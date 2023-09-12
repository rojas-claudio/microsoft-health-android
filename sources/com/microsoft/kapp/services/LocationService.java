package com.microsoft.kapp.services;

import android.content.Context;
/* loaded from: classes.dex */
public interface LocationService {
    void getCurrentLocation(Context context, LocationCallbacks locationCallbacks);

    boolean isLocationServiceAvailable(Context context);
}
