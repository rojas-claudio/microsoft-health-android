package com.microsoft.band.service;

import android.os.Bundle;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.ServiceCommand;
import com.microsoft.band.service.ServiceCommandHandler;
/* loaded from: classes.dex */
public abstract class CommandHandlerWithPermissionCheck implements ServiceCommandHandler.ICommandHandler {
    protected abstract BandServiceMessage.Response executeChecked(CargoClientSession cargoClientSession, ServiceCommand serviceCommand, Bundle bundle) throws CargoServiceException;

    @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
    public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
        return session.isAdminSession() ? executeChecked(session, command, responseBundle) : BandServiceMessage.Response.PERMISSION_DENIED;
    }
}
