package com.microsoft.kapp.services;

import android.content.Context;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.Constants;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class InjectableNotificationHandler implements NotificationHandler {
    private Context mContext;
    @Inject
    SettingsProvider mSettingsProvider;

    public InjectableNotificationHandler(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mContext = context;
        ((KApplication) context.getApplicationContext()).inject(this);
    }

    public Context getContext() {
        return this.mContext;
    }
}
