package com.microsoft.band.sensors;

import android.app.Activity;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.UserConsent;
/* loaded from: classes.dex */
public interface BandSensorManager {
    UserConsent getCurrentHeartRateConsent();

    boolean registerAccelerometerEventListener(BandAccelerometerEventListener bandAccelerometerEventListener, SampleRate sampleRate) throws BandIOException;

    boolean registerAltimeterEventListener(BandAltimeterEventListener bandAltimeterEventListener) throws BandIOException;

    boolean registerAmbientLightEventListener(BandAmbientLightEventListener bandAmbientLightEventListener) throws BandIOException;

    boolean registerBarometerEventListener(BandBarometerEventListener bandBarometerEventListener) throws BandIOException;

    boolean registerCaloriesEventListener(BandCaloriesEventListener bandCaloriesEventListener) throws BandIOException;

    boolean registerContactEventListener(BandContactEventListener bandContactEventListener) throws BandIOException;

    boolean registerDistanceEventListener(BandDistanceEventListener bandDistanceEventListener) throws BandIOException;

    boolean registerGsrEventListener(BandGsrEventListener bandGsrEventListener) throws BandIOException;

    boolean registerGyroscopeEventListener(BandGyroscopeEventListener bandGyroscopeEventListener, SampleRate sampleRate) throws BandIOException;

    boolean registerHeartRateEventListener(BandHeartRateEventListener bandHeartRateEventListener) throws BandIOException, BandException;

    boolean registerPedometerEventListener(BandPedometerEventListener bandPedometerEventListener) throws BandIOException;

    boolean registerRRIntervalEventListener(BandRRIntervalEventListener bandRRIntervalEventListener) throws BandIOException, BandException;

    boolean registerSkinTemperatureEventListener(BandSkinTemperatureEventListener bandSkinTemperatureEventListener) throws BandIOException;

    boolean registerUVEventListener(BandUVEventListener bandUVEventListener) throws BandIOException;

    void requestHeartRateConsent(Activity activity, HeartRateConsentListener heartRateConsentListener);

    void unregisterAccelerometerEventListener(BandAccelerometerEventListener bandAccelerometerEventListener) throws BandIOException;

    void unregisterAccelerometerEventListeners() throws BandIOException;

    void unregisterAllListeners() throws BandIOException;

    void unregisterAltimeterEventListener(BandAltimeterEventListener bandAltimeterEventListener) throws BandIOException;

    void unregisterAltimeterEventListeners() throws BandIOException;

    void unregisterAmbientLightEventListener(BandAmbientLightEventListener bandAmbientLightEventListener) throws BandIOException;

    void unregisterAmbientLightEventListeners() throws BandIOException;

    void unregisterBarometerEventListener(BandBarometerEventListener bandBarometerEventListener) throws BandIOException;

    void unregisterBarometerEventListeners() throws BandIOException;

    void unregisterCaloriesEventListener(BandCaloriesEventListener bandCaloriesEventListener) throws BandIOException;

    void unregisterCaloriesEventListeners() throws BandIOException;

    void unregisterContactEventListener(BandContactEventListener bandContactEventListener) throws BandIOException;

    void unregisterContactEventListeners() throws BandIOException;

    void unregisterDistanceEventListener(BandDistanceEventListener bandDistanceEventListener) throws BandIOException;

    void unregisterDistanceEventListeners() throws BandIOException;

    void unregisterGsrEventListener(BandGsrEventListener bandGsrEventListener) throws BandIOException;

    void unregisterGsrEventListeners() throws BandIOException;

    void unregisterGyroscopeEventListener(BandGyroscopeEventListener bandGyroscopeEventListener) throws BandIOException;

    void unregisterGyroscopeEventListeners() throws BandIOException;

    void unregisterHeartRateEventListener(BandHeartRateEventListener bandHeartRateEventListener) throws BandIOException;

    void unregisterHeartRateEventListeners() throws BandIOException;

    void unregisterPedometerEventListener(BandPedometerEventListener bandPedometerEventListener) throws BandIOException;

    void unregisterPedometerEventListeners() throws BandIOException;

    void unregisterRRIntervalEventListener(BandRRIntervalEventListener bandRRIntervalEventListener) throws BandIOException;

    void unregisterRRIntervalEventListeners() throws BandIOException;

    void unregisterSkinTemperatureEventListener(BandSkinTemperatureEventListener bandSkinTemperatureEventListener) throws BandIOException;

    void unregisterSkinTemperatureEventListeners() throws BandIOException;

    void unregisterUVEventListener(BandUVEventListener bandUVEventListener) throws BandIOException;

    void unregisterUVEventListeners() throws BandIOException;
}
