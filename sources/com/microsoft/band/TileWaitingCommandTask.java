package com.microsoft.band;

import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.KDKLog;
/* loaded from: classes.dex */
abstract class TileWaitingCommandTask<ResultT, C extends CommandBase> extends WaitingCommandTask<ResultT, C> {
    private static final String TAG = TileWaitingCommandTask.class.getSimpleName();

    public abstract ResultT toTileResult(C c, boolean z) throws BandException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TileWaitingCommandTask(C command) {
        super(command);
    }

    @Override // com.microsoft.band.WaitingCommandTask
    public ResultT toResult(C command, boolean isTimeOut) throws BandException {
        throwBandTileException(command, isTimeOut);
        return toTileResult(command, isTimeOut);
    }

    private void throwBandTileException(C command, boolean isTimeOut) throws BandException {
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
                    case TILE_NOT_FOUND_ERROR:
                        throw new BandException("Tile is not on the band.", BandErrorType.TILE_NOT_FOUND_ERROR);
                    case TILE_SECURITY_ERROR:
                        throw new BandException("Access to the specified tile is not available to your application.", BandErrorType.PERMISSION_ERROR);
                    case BAND_IS_FULL_ERROR:
                        throw new BandException("No space on band for new tile.", BandErrorType.BAND_FULL_ERROR);
                    case TILE_ALREADY_EXISTS_ERROR:
                        throw new BandException("The tile already exists on the band.", BandErrorType.TILE_ALREADY_EXISTS_ERROR);
                    case TILE_PAGE_DATA_ERROR:
                        throw new BandException("Data being sent exceeds size limit for a single page.", BandErrorType.INVALID_PAGE_DATA_ERROR);
                    case TILE_LAYOUT_ERROR:
                        throw new BandException("Layout being sent exceeds the allowed size.", BandErrorType.INVALID_LAYOUT_ERROR);
                    case TILE_LAYOUT_INDEX_ERROR:
                        throw new BandException("A layout with the index provided does not exist on the tile", BandErrorType.INVALID_LAYOUT_INDEX_ERROR);
                    case DEVICE_NOTIFICATION_DATA_LONG_STRING_ERROR:
                        throw new BandException("Only one string per page can be greater than 20 characters", BandErrorType.MULTIPLE_LONG_STRING_ERROR);
                    default:
                        throw new BandException(String.format("Unknown error %s occurred.", command.getResponseCode()), BandErrorType.UNKNOWN_ERROR);
                }
            }
        }
    }
}
