package com.microsoft.band;

import android.app.Activity;
import android.os.Parcelable;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandAltimeterEventListener;
import com.microsoft.band.sensors.BandAmbientLightEventListener;
import com.microsoft.band.sensors.BandBarometerEventListener;
import com.microsoft.band.sensors.BandBatteryLevelEvent;
import com.microsoft.band.sensors.BandBatteryLevelEventListener;
import com.microsoft.band.sensors.BandCaloriesEventListener;
import com.microsoft.band.sensors.BandConnectionStatusEvent;
import com.microsoft.band.sensors.BandConnectionStatusEventListener;
import com.microsoft.band.sensors.BandContactEventListener;
import com.microsoft.band.sensors.BandDistanceEventListener;
import com.microsoft.band.sensors.BandGsrEventListener;
import com.microsoft.band.sensors.BandGyroscopeEventListener;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandPedometerEventListener;
import com.microsoft.band.sensors.BandRRIntervalEventListener;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;
import com.microsoft.band.sensors.BandUVEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;
import com.microsoft.band.sensors.SampleRate;
import com.microsoft.band.service.subscription.SubscriptionDataContract;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public class KDKSensorManager extends BandSensorManagerImpl {
    public static final int BATTERY_LEVEL_EVENT_CONSTANT = 38;
    public static final int CONNECT_STATUS_EVENT_CONSTANT = 60;
    private List<BandBatteryLevelEventListener> mBatteryLevelEventListeners;
    private List<BandConnectionStatusEventListener> mConnectionStatusEventListeners;

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ UserConsent getCurrentHeartRateConsent() {
        return super.getCurrentHeartRateConsent();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerAccelerometerEventListener(BandAccelerometerEventListener x0, SampleRate x1) throws BandIOException {
        return super.registerAccelerometerEventListener(x0, x1);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerAltimeterEventListener(BandAltimeterEventListener x0) throws BandIOException {
        return super.registerAltimeterEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerAmbientLightEventListener(BandAmbientLightEventListener x0) throws BandIOException {
        return super.registerAmbientLightEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerBarometerEventListener(BandBarometerEventListener x0) throws BandIOException {
        return super.registerBarometerEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerCaloriesEventListener(BandCaloriesEventListener x0) throws BandIOException {
        return super.registerCaloriesEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerContactEventListener(BandContactEventListener x0) throws BandIOException {
        return super.registerContactEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerDistanceEventListener(BandDistanceEventListener x0) throws BandIOException {
        return super.registerDistanceEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerGsrEventListener(BandGsrEventListener x0) throws BandIOException {
        return super.registerGsrEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerGyroscopeEventListener(BandGyroscopeEventListener x0, SampleRate x1) throws BandIOException {
        return super.registerGyroscopeEventListener(x0, x1);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerHeartRateEventListener(BandHeartRateEventListener x0) throws BandIOException, BandException {
        return super.registerHeartRateEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerPedometerEventListener(BandPedometerEventListener x0) throws BandIOException {
        return super.registerPedometerEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerRRIntervalEventListener(BandRRIntervalEventListener x0) throws BandIOException, BandException {
        return super.registerRRIntervalEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerSkinTemperatureEventListener(BandSkinTemperatureEventListener x0) throws BandIOException {
        return super.registerSkinTemperatureEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ boolean registerUVEventListener(BandUVEventListener x0) throws BandIOException {
        return super.registerUVEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void requestHeartRateConsent(Activity x0, HeartRateConsentListener x1) {
        super.requestHeartRateConsent(x0, x1);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterAccelerometerEventListener(BandAccelerometerEventListener x0) throws BandIOException {
        super.unregisterAccelerometerEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterAccelerometerEventListeners() throws BandIOException {
        super.unregisterAccelerometerEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterAllListeners() throws BandIOException {
        super.unregisterAllListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterAltimeterEventListener(BandAltimeterEventListener x0) throws BandIOException {
        super.unregisterAltimeterEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterAltimeterEventListeners() throws BandIOException {
        super.unregisterAltimeterEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterAmbientLightEventListener(BandAmbientLightEventListener x0) throws BandIOException {
        super.unregisterAmbientLightEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterAmbientLightEventListeners() throws BandIOException {
        super.unregisterAmbientLightEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterBarometerEventListener(BandBarometerEventListener x0) throws BandIOException {
        super.unregisterBarometerEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterBarometerEventListeners() throws BandIOException {
        super.unregisterBarometerEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterCaloriesEventListener(BandCaloriesEventListener x0) throws BandIOException {
        super.unregisterCaloriesEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterCaloriesEventListeners() throws BandIOException {
        super.unregisterCaloriesEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterContactEventListener(BandContactEventListener x0) throws BandIOException {
        super.unregisterContactEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterContactEventListeners() throws BandIOException {
        super.unregisterContactEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterDistanceEventListener(BandDistanceEventListener x0) throws BandIOException {
        super.unregisterDistanceEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterDistanceEventListeners() throws BandIOException {
        super.unregisterDistanceEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterGsrEventListener(BandGsrEventListener x0) throws BandIOException {
        super.unregisterGsrEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterGsrEventListeners() throws BandIOException {
        super.unregisterGsrEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterGyroscopeEventListener(BandGyroscopeEventListener x0) throws BandIOException {
        super.unregisterGyroscopeEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterGyroscopeEventListeners() throws BandIOException {
        super.unregisterGyroscopeEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterHeartRateEventListener(BandHeartRateEventListener x0) throws BandIOException {
        super.unregisterHeartRateEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterHeartRateEventListeners() throws BandIOException {
        super.unregisterHeartRateEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterPedometerEventListener(BandPedometerEventListener x0) throws BandIOException {
        super.unregisterPedometerEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterPedometerEventListeners() throws BandIOException {
        super.unregisterPedometerEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterRRIntervalEventListener(BandRRIntervalEventListener x0) throws BandIOException {
        super.unregisterRRIntervalEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterRRIntervalEventListeners() throws BandIOException {
        super.unregisterRRIntervalEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterSkinTemperatureEventListener(BandSkinTemperatureEventListener x0) throws BandIOException {
        super.unregisterSkinTemperatureEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterSkinTemperatureEventListeners() throws BandIOException {
        super.unregisterSkinTemperatureEventListeners();
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterUVEventListener(BandUVEventListener x0) throws BandIOException {
        super.unregisterUVEventListener(x0);
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.sensors.BandSensorManager
    public /* bridge */ /* synthetic */ void unregisterUVEventListeners() throws BandIOException {
        super.unregisterUVEventListeners();
    }

    public KDKSensorManager(BandServiceConnection mServiceConnection) {
        super(mServiceConnection);
        this.mConnectionStatusEventListeners = new CopyOnWriteArrayList();
        this.mBatteryLevelEventListeners = new CopyOnWriteArrayList();
    }

    public boolean registerConnectionStatusEventListener(BandConnectionStatusEventListener bandConnectionStatusEventListener) throws BandException {
        if (bandConnectionStatusEventListener == null) {
            throw new IllegalArgumentException("Cannot register a null listener");
        }
        if (!this.mConnectionStatusEventListeners.contains(bandConnectionStatusEventListener)) {
            this.mConnectionStatusEventListeners.add(bandConnectionStatusEventListener);
        }
        return subscribe(SubscriptionDataContract.SensorType.DeviceStatus.getId());
    }

    public void unregisterConnectionStatusEventListeners() throws BandException {
        boolean hasListenersToUnregister = !this.mConnectionStatusEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mConnectionStatusEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(SubscriptionDataContract.SensorType.DeviceStatus.getId());
        }
    }

    public void unregisterConnectionStatusEventListener(BandConnectionStatusEventListener bandConnectionStatusEventListener) throws BandException {
        if (this.mConnectionStatusEventListeners.remove(bandConnectionStatusEventListener) && this.mConnectionStatusEventListeners.isEmpty()) {
            unsubscribe(SubscriptionDataContract.SensorType.DeviceStatus.getId());
        }
    }

    public boolean registerBatteryLevelEventListener(BandBatteryLevelEventListener bandBatteryLevelEventListener) throws BandException {
        if (bandBatteryLevelEventListener == null) {
            throw new IllegalArgumentException("Cannot register a null listener");
        }
        if (!this.mBatteryLevelEventListeners.contains(bandBatteryLevelEventListener)) {
            this.mBatteryLevelEventListeners.add(bandBatteryLevelEventListener);
        }
        return subscribe(SubscriptionDataContract.SensorType.BatteryGauge.getId());
    }

    public void unregisterBatteryLevelEventListeners() throws BandException {
        boolean hasListenersToUnregister = !this.mBatteryLevelEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mBatteryLevelEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(SubscriptionDataContract.SensorType.BatteryGauge.getId());
        }
    }

    public void unregisterBatteryLevelEventListener(BandBatteryLevelEventListener bandBatteryLevelEventListener) throws BandException {
        if (this.mBatteryLevelEventListeners.remove(bandBatteryLevelEventListener) && this.mBatteryLevelEventListeners.isEmpty()) {
            unsubscribe(SubscriptionDataContract.SensorType.BatteryGauge.getId());
        }
    }

    @Override // com.microsoft.band.BandSensorManagerImpl, com.microsoft.band.PushHandler
    public void handlePushData(int arg1, Parcelable parcelable) {
        super.handlePushData(arg1, parcelable);
        switch (arg1) {
            case 38:
                BandBatteryLevelEvent batteryLevelEvent = (BandBatteryLevelEvent) parcelable;
                for (BandBatteryLevelEventListener listener : this.mBatteryLevelEventListeners) {
                    listener.onBatteryLevelChanged(batteryLevelEvent);
                }
                return;
            case 60:
                BandConnectionStatusEvent connectionStatusEvent = (BandConnectionStatusEvent) parcelable;
                for (BandConnectionStatusEventListener listener2 : this.mConnectionStatusEventListeners) {
                    listener2.onConnectionStatusChanged(connectionStatusEvent);
                }
                return;
            default:
                return;
        }
    }
}
