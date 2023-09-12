package com.microsoft.kapp.models;

import com.microsoft.band.internal.BandServiceMessage;
/* loaded from: classes.dex */
public interface FirmwareUpdateCallback {
    void onCheckFirmwareVersionFailed(BandServiceMessage.Response response);

    void onCheckFirmwareVersionStarted();

    void onDownloadFirmwareFailed(BandServiceMessage.Response response);

    void onDownloadFirmwareStarted();

    void onFirmwareUpToDate();

    void onSingleDeviceEnforcementFailed(BandServiceMessage.Response response);

    void onUpdateFirmwareFailed(BandServiceMessage.Response response);

    void onUpdateFirmwareStarted();

    void onUpdateFirmwareSucceeded();
}
