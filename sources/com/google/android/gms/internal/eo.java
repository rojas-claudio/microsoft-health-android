package com.google.android.gms.internal;

import android.content.Intent;
import android.net.Uri;
import com.facebook.internal.ServerProtocol;
import com.google.android.gms.common.GooglePlayServicesUtil;
/* loaded from: classes.dex */
public final class eo {
    public static final Intent c(Intent intent) {
        intent.setData(Uri.fromParts(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION, Integer.toString(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE), null));
        return intent;
    }
}
