package com.microsoft.kapp.telephony;

import android.content.Context;
import android.content.Intent;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.services.NotificationIntentService;
import com.microsoft.kapp.telephony.event.MessageEvent;
import com.microsoft.kapp.telephony.event.MessageListener;
import com.microsoft.kapp.utils.Constants;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class DefaultMessagesManager implements MessagesManager, MessageListener {
    private Context mContext;
    @Inject
    MessagesObserver mMessagesObserver;

    public DefaultMessagesManager(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mContext = context;
        ((KApplication) context.getApplicationContext()).inject(this);
        this.mMessagesObserver.addListener(this);
    }

    @Override // com.microsoft.kapp.telephony.event.MessageListener
    public void messageReceived(MessageEvent e) {
        Validate.notNull(e, "e");
        Intent intent = new Intent(this.mContext, NotificationIntentService.class);
        intent.setAction(Constants.NOTIFICATION_ACTION_MESSAGE);
        intent.putExtra(Constants.NOTIFICATION_MESSAGE_METADATA, e.getMetadata());
        this.mContext.startService(intent);
    }

    @Override // com.microsoft.kapp.telephony.event.MessageListener
    public void messageDeleted(MessageEvent e) {
    }

    @Override // com.microsoft.kapp.telephony.event.MessageListener
    public void messageRead(MessageEvent e) {
    }
}
