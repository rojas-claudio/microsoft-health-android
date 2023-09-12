package com.microsoft.band;

import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.KDKLog;
/* loaded from: classes.dex */
abstract class PersonalizationWaitingCommandTask<ResultT, C extends CommandBase> extends WaitingCommandTask<ResultT, C> {
    private static final String TAG = PersonalizationWaitingCommandTask.class.getSimpleName();

    public abstract ResultT toPersonalizationResult(C c, boolean z) throws BandException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PersonalizationWaitingCommandTask(C command) {
        super(command);
    }

    @Override // com.microsoft.band.WaitingCommandTask
    public ResultT toResult(C command, boolean isTimeOut) throws BandException {
        throwBandPersonalizationException(command, isTimeOut);
        return toPersonalizationResult(command, isTimeOut);
    }

    private void throwBandPersonalizationException(C command, boolean isTimeOut) throws BandException {
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
                    case NO_METILE_IMAGE:
                        return;
                    default:
                        throw new BandException("Unknown error occurred.", BandErrorType.UNKNOWN_ERROR);
                }
            }
        }
    }
}
