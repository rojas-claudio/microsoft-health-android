package com.microsoft.band.internal;

import android.os.Bundle;
import com.microsoft.band.internal.BandDeviceConstants;
/* loaded from: classes.dex */
public final class ServiceCommand extends CommandBase {
    private static final long serialVersionUID = 1;
    private Bundle mBundle;

    public ServiceCommand(BandDeviceConstants.Command commandType) {
        super(commandType);
        this.mBundle = new Bundle();
    }

    public ServiceCommand(BandDeviceConstants.Command commandType, Bundle bundle) {
        super(commandType);
        this.mBundle = bundle == null ? new Bundle() : bundle;
    }

    public ServiceCommand(int commandId, Bundle bundle) {
        super(BandDeviceConstants.Command.lookup(commandId));
        this.mBundle = bundle == null ? new Bundle() : bundle;
        setCommandIndex(this.mBundle.getLong(InternalBandConstants.EXTRA_COMMAND_INDEX));
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public void setBundle(Bundle bundle, int responseCode) {
        this.mBundle = bundle;
        setResultCode(responseCode);
    }
}
