package com.microsoft.band;

import com.microsoft.band.notifications.BandNotificationManager;
import com.microsoft.band.personalization.BandPersonalizationManager;
import com.microsoft.band.sensors.BandSensorManager;
import com.microsoft.band.tiles.BandTileManager;
/* loaded from: classes.dex */
public interface BandClient {
    BandPendingResult<ConnectionState> connect();

    BandPendingResult<Void> disconnect();

    ConnectionState getConnectionState();

    BandPendingResult<String> getFirmwareVersion() throws BandIOException;

    BandPendingResult<String> getHardwareVersion() throws BandIOException;

    BandNotificationManager getNotificationManager();

    BandPersonalizationManager getPersonalizationManager();

    BandSensorManager getSensorManager();

    BandTileManager getTileManager();

    boolean isConnected();

    void registerConnectionCallback(BandConnectionCallback bandConnectionCallback);

    void unregisterConnectionCallback();
}
