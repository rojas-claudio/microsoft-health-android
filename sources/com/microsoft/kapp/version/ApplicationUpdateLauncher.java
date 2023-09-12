package com.microsoft.kapp.version;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.microsoft.kapp.diagnostics.Validate;
import javax.inject.Singleton;
@Singleton
/* loaded from: classes.dex */
public class ApplicationUpdateLauncher {
    private static final String MARKET_URI_PREFIX = "market://details?id=%s";

    public void launch(Activity activity) {
        Validate.notNull(activity, "activity");
        Intent intent = new Intent("android.intent.action.VIEW");
        Uri marketDetailsUri = Uri.parse(String.format(MARKET_URI_PREFIX, activity.getPackageName()));
        intent.setData(marketDetailsUri);
        activity.startActivity(intent);
    }
}
