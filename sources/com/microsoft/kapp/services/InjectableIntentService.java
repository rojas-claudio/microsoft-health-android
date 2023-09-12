package com.microsoft.kapp.services;

import android.app.IntentService;
import com.microsoft.kapp.KApplication;
/* loaded from: classes.dex */
public abstract class InjectableIntentService extends IntentService {
    public InjectableIntentService(String name) {
        super(name);
    }

    @Override // android.app.IntentService, android.app.Service
    public void onCreate() {
        super.onCreate();
        KApplication application = (KApplication) getApplication();
        application.inject(this);
    }
}
