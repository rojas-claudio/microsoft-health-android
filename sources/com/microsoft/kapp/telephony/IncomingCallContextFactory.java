package com.microsoft.kapp.telephony;

import android.content.Context;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.Constants;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
@Singleton
/* loaded from: classes.dex */
public class IncomingCallContextFactory {
    private Context mContext;

    @Inject
    public IncomingCallContextFactory(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mContext = context;
    }

    public IncomingCallContext create(String number) {
        if (StringUtils.isEmpty(number)) {
            IncomingCallContext context = new PrivateIncomingCallContext(this.mContext);
            return context;
        }
        IncomingCallContext context2 = new DefaultIncomingCallContext(number);
        return context2;
    }
}
