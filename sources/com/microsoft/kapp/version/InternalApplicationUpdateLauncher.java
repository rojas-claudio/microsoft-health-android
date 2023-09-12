package com.microsoft.kapp.version;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.Constants;
import javax.inject.Singleton;
@Singleton
/* loaded from: classes.dex */
public class InternalApplicationUpdateLauncher extends ApplicationUpdateLauncher {
    private static final Uri INTERNAL_SITE_URI = Uri.parse(Constants.INTERNAL_ANDROID_DISTRIBUTION_PAGE);

    @Override // com.microsoft.kapp.version.ApplicationUpdateLauncher
    public void launch(Activity activity) {
        Validate.notNull(activity, "activity");
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(INTERNAL_SITE_URI);
        activity.startActivity(intent);
    }
}
