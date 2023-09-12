package com.microsoft.kapp.logging;

import android.content.Context;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.models.LogEntry;
import com.microsoft.kapp.utils.Constants;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class BaseLogger implements Logger {
    protected Context mContext;
    @Inject
    protected LogConfiguration mLogConfiguration;

    @Override // com.microsoft.kapp.logging.Logger
    public abstract void log(LogEntry logEntry);

    @Override // com.microsoft.kapp.logging.Logger
    public void flushAndClose() {
    }

    public BaseLogger(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        ((KApplication) context).inject(this);
        this.mContext = context;
    }
}
