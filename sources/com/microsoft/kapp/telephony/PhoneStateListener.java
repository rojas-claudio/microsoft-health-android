package com.microsoft.kapp.telephony;

import android.content.Context;
import android.content.Intent;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.CallDismissManager;
import com.microsoft.kapp.services.NotificationIntentService;
import com.microsoft.kapp.utils.Constants;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class PhoneStateListener extends android.telephony.PhoneStateListener {
    private static final String TAG = PhoneStateListener.class.getSimpleName();
    @Inject
    CallDismissManager mCallDismissManager;
    private CallStateChangeHandler mCallStateChangeHandler;
    private Context mContext;

    public PhoneStateListener(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mContext = context;
        this.mCallStateChangeHandler = new CallStateChangeHandler(new IncomingCallContextFactory(context));
    }

    @Override // android.telephony.PhoneStateListener
    public void onMessageWaitingIndicatorChanged(boolean hasUnreadVoicemail) {
        super.onMessageWaitingIndicatorChanged(hasUnreadVoicemail);
        if (hasUnreadVoicemail) {
            Intent intent = new Intent(this.mContext, NotificationIntentService.class);
            intent.setAction(Constants.NOTIFICATION_ACTION_VOICEMAIL);
            this.mContext.startService(intent);
        }
    }

    @Override // android.telephony.PhoneStateListener
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        try {
            IncomingCallContext context = this.mCallStateChangeHandler.getIncomingCallContext(state, incomingNumber);
            if (context != null) {
                this.mCallDismissManager.setPhoneState(context.getState());
                Intent intent = new Intent(this.mContext, NotificationIntentService.class);
                intent.setAction(Constants.NOTIFICATION_ACTION_CALL);
                intent.putExtra(Constants.NOTIFICATION_CONTEXT, context);
                this.mContext.startService(intent);
            }
        } catch (InvalidCallStateException ex) {
            KLog.e(TAG, "Invalid transition", ex);
        }
    }
}
