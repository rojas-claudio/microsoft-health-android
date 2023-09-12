package com.microsoft.kapp.telephony;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.telephony.event.MessageEvent;
import com.microsoft.kapp.telephony.event.MessageListener;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.LogScenarioTags;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
@Singleton
/* loaded from: classes.dex */
public class MmsSmsMessagesObserver extends ContentObserver implements MessagesObserver {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String TAG;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private final CopyOnWriteArrayList<MessageListener> mListeners;
    @Inject
    MmsSmsMessageMetadataRetriever mMetadataRetriever;

    static {
        $assertionsDisabled = !MmsSmsMessagesObserver.class.desiredAssertionStatus();
        TAG = MmsSmsMessagesObserver.class.getSimpleName();
    }

    @Inject
    @SuppressLint({"NewApi"})
    public MmsSmsMessagesObserver(Context context) {
        super(null);
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mContext = context;
        this.mContentResolver = this.mContext.getContentResolver();
        KApplication application = (KApplication) context.getApplicationContext();
        application.inject(this);
        this.mContentResolver.registerContentObserver(Build.VERSION.SDK_INT >= 19 ? Telephony.MmsSms.CONTENT_URI : Constants.MMS_SMS_CONVERSATION_CONTENT_PROVIDER_URI, false, this);
        this.mListeners = new CopyOnWriteArrayList<>();
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean selfChange, Uri uri) {
        List<MessageMetadata> newMessageIdentifiers = this.mMetadataRetriever.retrieveNewMessageMetadata();
        if (newMessageIdentifiers != null && newMessageIdentifiers.size() > 0) {
            KLog.logPrivate(LogScenarioTags.SmsMmsMessage, "Detected %d new incoming message(s).", Integer.valueOf(newMessageIdentifiers.size()));
            for (MessageMetadata identifier : newMessageIdentifiers) {
                notifyMessageReceived(identifier);
            }
        }
    }

    @Override // com.microsoft.kapp.telephony.MessagesObserver
    public void addListener(MessageListener listener) {
        Validate.notNull(listener, "listener");
        this.mListeners.add(listener);
    }

    @Override // com.microsoft.kapp.telephony.MessagesObserver
    public void removeListener(MessageListener listener) {
        Validate.notNull(listener, "listener");
        this.mListeners.remove(listener);
    }

    private void notifyMessageReceived(MessageMetadata metadata) {
        if (!$assertionsDisabled && metadata == null) {
            throw new AssertionError("metadata should not be null");
        }
        Iterator i$ = this.mListeners.iterator();
        while (i$.hasNext()) {
            MessageListener listener = i$.next();
            try {
                listener.messageReceived(new MessageEvent(this, metadata));
            } catch (Exception ex) {
                KLog.e(TAG, "Exception in notifyMessageReceived", ex);
            }
        }
    }
}
