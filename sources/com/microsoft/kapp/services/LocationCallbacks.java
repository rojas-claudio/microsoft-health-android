package com.microsoft.kapp.services;

import android.location.Location;
/* loaded from: classes.dex */
public interface LocationCallbacks {
    void onError();

    void onLocationFound(Location location);
}
