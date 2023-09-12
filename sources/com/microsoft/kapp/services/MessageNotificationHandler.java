package com.microsoft.kapp.services;

import android.content.Context;
import android.os.Bundle;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.ContactResolver;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.telephony.Message;
import com.microsoft.kapp.telephony.MessageMetadata;
import com.microsoft.kapp.telephony.MmsSmsMessageMetadataRetriever;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.LogScenarioTags;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class MessageNotificationHandler extends InjectableNotificationHandler {
    private static final String TAG = MessageNotificationHandler.class.getSimpleName();
    @Inject
    CargoConnection mCargoConnection;
    @Inject
    ContactResolver mContactResolver;
    @Inject
    MmsSmsMessageMetadataRetriever mMetadataRetriever;

    public MessageNotificationHandler(Context context) {
        super(context);
    }

    @Override // com.microsoft.kapp.services.NotificationHandler
    public void handleNotification(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        MessageMetadata metadata = (MessageMetadata) bundle.getParcelable(Constants.NOTIFICATION_MESSAGE_METADATA);
        if (metadata == null) {
            KLog.e(TAG, "MessageMetadata is missing from bundle for message notification.");
        } else if (this.mCargoConnection == null || this.mMetadataRetriever == null || this.mSettingsProvider == null) {
            KLog.e(TAG, "ContactResolver and/or MetadataRetriever is null. Make sure dependency injection is setup correctly.");
        } else if (this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN) {
            switch (metadata.getMessageState()) {
                case UNREAD:
                    handleUnreadMessage(metadata);
                    return;
                default:
                    return;
            }
        }
    }

    private void handleUnreadMessage(MessageMetadata metadata) {
        KLog.logPrivate(LogScenarioTags.SmsMmsMessage, "%d: Retrieving %s.", Integer.valueOf(metadata.getId()), metadata);
        Message message = this.mMetadataRetriever.retrieveMessage(metadata);
        if (message != null) {
            message.resolveDisplayName(this.mContactResolver);
            try {
                this.mCargoConnection.sendMessageNotification(message);
            } catch (Exception e) {
                KLog.w(TAG, "Unexpected error when sending the message notification to the device", e);
            }
        }
    }
}
