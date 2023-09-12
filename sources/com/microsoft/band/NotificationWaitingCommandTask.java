package com.microsoft.band;

import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.KDKLog;
/* loaded from: classes.dex */
abstract class NotificationWaitingCommandTask<ResultT, C extends CommandBase> extends WaitingCommandTask<ResultT, C> {
    private static final String TAG = NotificationWaitingCommandTask.class.getSimpleName();

    public abstract ResultT toNotificationResult(C c, boolean z) throws BandException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public NotificationWaitingCommandTask(C command) {
        super(command);
    }

    @Override // com.microsoft.band.WaitingCommandTask
    public ResultT toResult(C command, boolean isTimeOut) throws BandException {
        throwBandNotificationException(command, isTimeOut);
        return toNotificationResult(command, isTimeOut);
    }

    private void throwBandNotificationException(C command, boolean isTimeOut) throws BandException {
        if (isTimeOut) {
            KDKLog.e(TAG, String.format("Process %s with timeout", command.getCommandType()));
            throw new BandException("Command timed out.", BandErrorType.TIMEOUT_ERROR);
        } else if (command.isResultCodeSevere()) {
            KDKLog.e(TAG, String.format("Process %s with severe result %s", command.getCommandType(), command.getResultString()));
            if (command.getResponseCode() != null) {
                switch (command.getResponseCode()) {
                    case DEVICE_NOT_BONDED_ERROR:
                    case DEVICE_NOT_CONNECTED_ERROR:
                    case DEVICE_TIMEOUT_ERROR:
                    case DEVICE_IO_ERROR:
                    case DEVICE_STATE_ERROR:
                        throw new BandException("Please make sure bluetooth is on and the band is in range.", BandErrorType.DEVICE_ERROR);
                    case TILE_SECURITY_ERROR:
                        throw new BandException("Access to the specified tile is not available to your application.", BandErrorType.PERMISSION_ERROR);
                    case TILE_NOT_FOUND_ERROR:
                        throw new BandException("Tile is not on the band.", BandErrorType.TILE_NOT_FOUND_ERROR);
                    default:
                        throw new BandException("Unknown error occurred.", BandErrorType.UNKNOWN_ERROR);
                }
            }
        }
    }
}
