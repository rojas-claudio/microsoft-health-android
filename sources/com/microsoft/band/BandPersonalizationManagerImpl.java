package com.microsoft.band;

import android.graphics.Bitmap;
import android.os.Bundle;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.ServiceCommand;
import com.microsoft.band.internal.util.BandImage;
import com.microsoft.band.internal.util.VersionCheck;
import com.microsoft.band.personalization.BandPersonalizationManager;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class BandPersonalizationManagerImpl implements BandPersonalizationManager {
    private final BandServiceConnection mServiceConnection;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandPersonalizationManagerImpl(BandServiceConnection connection) {
        this.mServiceConnection = connection;
    }

    @Override // com.microsoft.band.personalization.BandPersonalizationManager
    public BandPendingResult<Void> setMeTileImage(Bitmap image) throws BandIOException {
        if (image == null) {
            return this.mServiceConnection.send(getPersonalizationCommandTask(BandDeviceConstants.Command.BandPersonalizationClearMeTile, null));
        }
        try {
            int hardwareVersion = this.mServiceConnection.getHardwareVersion();
            BandImage.validateMeTileImage(image, hardwareVersion);
            Bundle bundle = new Bundle();
            bundle.putByteArray(InternalBandConstants.EXTRA_SERVICE_COMMAND_PAYLOAD, BandImage.bitmapToBGR565(image));
            return this.mServiceConnection.send(getPersonalizationCommandTask(BandDeviceConstants.Command.BandPersonalizationSetMeTile, bundle));
        } catch (BandException e) {
            return new ErrorBandPendingResult(e, null);
        } catch (InterruptedException e2) {
            return new ErrorBandPendingResult(e2, null);
        }
    }

    @Override // com.microsoft.band.personalization.BandPersonalizationManager
    public BandPendingResult<Bitmap> getMeTileImage() throws BandIOException {
        ComplexTask<Bitmap> complexTask = new ComplexTask<Bitmap>() { // from class: com.microsoft.band.BandPersonalizationManagerImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.band.ComplexTask
            public Bitmap tasks() throws BandException, InterruptedException {
                final int hardwareVersion = BandPersonalizationManagerImpl.this.mServiceConnection.getHardwareVersion();
                ServiceCommand cmdGetMeTile = new ServiceCommand(BandDeviceConstants.Command.BandPersonalizationGetMeTile);
                return (Bitmap) BandPersonalizationManagerImpl.this.mServiceConnection.send(new PersonalizationWaitingCommandTask<Bitmap, ServiceCommand>(cmdGetMeTile) { // from class: com.microsoft.band.BandPersonalizationManagerImpl.1.1
                    @Override // com.microsoft.band.PersonalizationWaitingCommandTask
                    public Bitmap toPersonalizationResult(ServiceCommand command, boolean isTimeOut) {
                        if (command.getResponseCode() != BandServiceMessage.Response.NO_METILE_IMAGE) {
                            if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
                                return BandImage.getBitmapFromBGR565(command.getBundle().getByteArray(InternalBandConstants.EXTRA_COMMAND_PAYLOAD), 128, 310);
                            }
                            return BandImage.getBitmapFromBGR565(command.getBundle().getByteArray(InternalBandConstants.EXTRA_COMMAND_PAYLOAD), 102, 310);
                        }
                        return null;
                    }
                }).await();
            }
        };
        complexTask.start();
        return complexTask;
    }

    @Override // com.microsoft.band.personalization.BandPersonalizationManager
    public BandPendingResult<Void> setTheme(BandTheme theme) throws BandIOException {
        if (theme == null) {
            return this.mServiceConnection.send(getPersonalizationCommandTask(BandDeviceConstants.Command.BandPersonalizationResetTheme, null));
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(InternalBandConstants.EXTRA_SERVICE_COMMAND_PAYLOAD, theme);
        return this.mServiceConnection.send(getPersonalizationCommandTask(BandDeviceConstants.Command.BandPersonalizationSetTheme, bundle));
    }

    @Override // com.microsoft.band.personalization.BandPersonalizationManager
    public BandPendingResult<BandTheme> getTheme() throws BandIOException {
        ServiceCommand cmdGetTheme = new ServiceCommand(BandDeviceConstants.Command.BandPersonalizationGetTheme);
        return this.mServiceConnection.send(new PersonalizationWaitingCommandTask<BandTheme, ServiceCommand>(cmdGetTheme) { // from class: com.microsoft.band.BandPersonalizationManagerImpl.2
            @Override // com.microsoft.band.PersonalizationWaitingCommandTask
            public BandTheme toPersonalizationResult(ServiceCommand command, boolean isTimeOut) {
                return (BandTheme) command.getBundle().getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
            }
        });
    }

    private PersonalizationWaitingCommandTask<Void, ServiceCommand> getPersonalizationCommandTask(BandDeviceConstants.Command commandType, Bundle bundle) {
        return new PersonalizationWaitingCommandTask<Void, ServiceCommand>(new ServiceCommand(commandType, bundle)) { // from class: com.microsoft.band.BandPersonalizationManagerImpl.3
            @Override // com.microsoft.band.PersonalizationWaitingCommandTask
            public Void toPersonalizationResult(ServiceCommand command, boolean isTimeOut) throws BandException {
                return null;
            }
        };
    }
}
