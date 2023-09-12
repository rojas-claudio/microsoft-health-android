package com.microsoft.band;

import android.os.Bundle;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.ServiceCommand;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.notifications.BandNotificationManager;
import com.microsoft.band.notifications.MessageFlags;
import com.microsoft.band.notifications.VibrationType;
import java.util.Date;
import java.util.UUID;
/* loaded from: classes.dex */
final class BandNotificationManagerImpl implements BandNotificationManager {
    private final BandServiceConnection mServiceConnection;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandNotificationManagerImpl(BandServiceConnection connection) {
        Validation.notNull(connection, "BandServiceConnection cannot be null");
        this.mServiceConnection = connection;
    }

    @Override // com.microsoft.band.notifications.BandNotificationManager
    public BandPendingResult<Void> vibrate(VibrationType type) throws BandIOException {
        Validation.notNull(type, "Vibration type cannot be null");
        Bundle bundle = new Bundle();
        bundle.putSerializable(InternalBandConstants.EXTRA_COMMAND_DATA, type);
        return this.mServiceConnection.send(getNotificationCommandTask(BandDeviceConstants.Command.BandNotificationVibrate, bundle));
    }

    @Override // com.microsoft.band.notifications.BandNotificationManager
    public BandPendingResult<Void> showDialog(UUID tileId, String title, String body) throws BandIOException {
        Bundle bundle = prepareShowDialogBundle(tileId, title, body);
        return this.mServiceConnection.send(getNotificationCommandTask(BandDeviceConstants.Command.BandNotificationShowDialog, bundle));
    }

    private Bundle prepareShowDialogBundle(UUID tileId, String title, String body) {
        Validation.notNull(tileId, "Tile ID cannot be null");
        validateTitleAndBody(title, body);
        Bundle bundle = new Bundle();
        bundle.putString(InternalBandConstants.EXTRA_COMMAND_DATA, tileId.toString());
        bundle.putString(InternalBandConstants.EXTRA_MESSAGE_TITLE, title);
        bundle.putString(InternalBandConstants.EXTRA_MESSAGE_BODY, body);
        return bundle;
    }

    @Override // com.microsoft.band.notifications.BandNotificationManager
    public BandPendingResult<Void> sendMessage(UUID tileId, String title, String body, Date timeStamp, MessageFlags flags) throws BandIOException {
        Bundle bundle = prepareSendMessageBundle(tileId, title, body, timeStamp, flags);
        return this.mServiceConnection.send(getNotificationCommandTask(BandDeviceConstants.Command.BandNotificationSendMessage, bundle));
    }

    private Bundle prepareSendMessageBundle(UUID tileId, String title, String body, Date timeStamp, MessageFlags flags) {
        Validation.notNull(tileId, "Tile ID cannot be null");
        validateTitleAndBody(title, body);
        if (timeStamp == null) {
            timeStamp = new Date();
        }
        if (flags == null) {
            flags = MessageFlags.SHOW_DIALOG;
        }
        Bundle bundle = new Bundle();
        bundle.putString(InternalBandConstants.EXTRA_COMMAND_DATA, tileId.toString());
        bundle.putString(InternalBandConstants.EXTRA_MESSAGE_TITLE, title);
        bundle.putString(InternalBandConstants.EXTRA_MESSAGE_BODY, body);
        bundle.putLong(InternalBandConstants.EXTRA_MESSAGE_TIMESTAMP, timeStamp.getTime());
        bundle.putSerializable(InternalBandConstants.EXTRA_MESSAGE_FLAG, flags);
        return bundle;
    }

    private void validateTitleAndBody(String title, String body) {
        if (title == null) {
            title = "";
        }
        if (body == null) {
            body = "";
        }
        if (StringUtil.isWhitespace(title) && StringUtil.isWhitespace(body)) {
            throw new IllegalArgumentException("The title and body cannot both be empty or null");
        }
    }

    private NotificationWaitingCommandTask<Void, ServiceCommand> getNotificationCommandTask(BandDeviceConstants.Command commandType, Bundle bundle) {
        return new NotificationWaitingCommandTask<Void, ServiceCommand>(new ServiceCommand(commandType, bundle)) { // from class: com.microsoft.band.BandNotificationManagerImpl.1
            @Override // com.microsoft.band.NotificationWaitingCommandTask
            public Void toNotificationResult(ServiceCommand command, boolean isTimeOut) throws BandException {
                return null;
            }
        };
    }
}
