package com.microsoft.band;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandAltimeterEvent;
import com.microsoft.band.sensors.BandAltimeterEventListener;
import com.microsoft.band.sensors.BandAmbientLightEvent;
import com.microsoft.band.sensors.BandAmbientLightEventListener;
import com.microsoft.band.sensors.BandBarometerEvent;
import com.microsoft.band.sensors.BandBarometerEventListener;
import com.microsoft.band.sensors.BandCaloriesEvent;
import com.microsoft.band.sensors.BandCaloriesEventListener;
import com.microsoft.band.sensors.BandContactEvent;
import com.microsoft.band.sensors.BandContactEventListener;
import com.microsoft.band.sensors.BandDistanceEvent;
import com.microsoft.band.sensors.BandDistanceEventListener;
import com.microsoft.band.sensors.BandGsrEvent;
import com.microsoft.band.sensors.BandGsrEventListener;
import com.microsoft.band.sensors.BandGyroscopeEvent;
import com.microsoft.band.sensors.BandGyroscopeEventListener;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandPedometerEvent;
import com.microsoft.band.sensors.BandPedometerEventListener;
import com.microsoft.band.sensors.BandRRIntervalEvent;
import com.microsoft.band.sensors.BandRRIntervalEventListener;
import com.microsoft.band.sensors.BandSensorManager;
import com.microsoft.band.sensors.BandSkinTemperatureEvent;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;
import com.microsoft.band.sensors.BandUVEvent;
import com.microsoft.band.sensors.BandUVEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;
import com.microsoft.band.sensors.SampleRate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
class BandSensorManagerImpl implements BandSensorManager, PushHandler {
    private static final int DIALOG_TOP_BOTTOM_PADDING_DIP = 65;
    private static final int LEFT_DIALOG_PADDING_DIP = 10;
    private static final String TAG = BandSensorManagerImpl.class.getSimpleName();
    private static final int TOP_TEXT_DIALOG_PADDING_DIP = 35;
    private final BandServiceConnection mServiceConnection;
    private List<BandAccelerometerEventListener> mAccelerometer16msEventListeners = new CopyOnWriteArrayList();
    private List<BandAccelerometerEventListener> mAccelerometer32msEventListeners = new CopyOnWriteArrayList();
    private List<BandAccelerometerEventListener> mAccelerometer128msEventListeners = new CopyOnWriteArrayList();
    private List<BandGyroscopeEventListener> mGyroscope16msEventListeners = new CopyOnWriteArrayList();
    private List<BandGyroscopeEventListener> mGyroscope32msEventListeners = new CopyOnWriteArrayList();
    private List<BandGyroscopeEventListener> mGyroscope128msEventListeners = new CopyOnWriteArrayList();
    private List<BandHeartRateEventListener> mHeartRateEventListeners = new CopyOnWriteArrayList();
    private List<BandPedometerEventListener> mPedometerEventListeners = new CopyOnWriteArrayList();
    private List<BandDistanceEventListener> mDistanceEventListeners = new CopyOnWriteArrayList();
    private List<BandSkinTemperatureEventListener> mSkinTemperatureEventListeners = new CopyOnWriteArrayList();
    private List<BandUVEventListener> mUVEventListeners = new CopyOnWriteArrayList();
    private List<BandContactEventListener> mContactEventListeners = new CopyOnWriteArrayList();
    private List<BandCaloriesEventListener> mCaloriesEventListeners = new CopyOnWriteArrayList();
    private List<BandGsrEventListener> mGsrEventListeners = new CopyOnWriteArrayList();
    private List<BandAmbientLightEventListener> mAmbientLightEventListeners = new CopyOnWriteArrayList();
    private List<BandRRIntervalEventListener> mRRIntervalEventListeners = new CopyOnWriteArrayList();
    private List<BandBarometerEventListener> mBarometerEventListeners = new CopyOnWriteArrayList();
    private List<BandAltimeterEventListener> mAltimeterEventListeners = new CopyOnWriteArrayList();
    private long mAccelerometerEventTracker = 0;
    private long mGyroscopeEventTracker = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandSensorManagerImpl(BandServiceConnection mServiceConnection) {
        this.mServiceConnection = mServiceConnection;
        this.mServiceConnection.setPushHandler(this);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerAccelerometerEventListener(BandAccelerometerEventListener bandAccelerationEventListener, SampleRate reportingInterval) throws BandIOException {
        if (bandAccelerationEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (reportingInterval == null) {
            throw new NullPointerException("Must have a reporting interval");
        }
        if (reportingInterval == SampleRate.MS16) {
            this.mAccelerometer32msEventListeners.remove(bandAccelerationEventListener);
            this.mAccelerometer128msEventListeners.remove(bandAccelerationEventListener);
            if (!this.mAccelerometer16msEventListeners.contains(bandAccelerationEventListener)) {
                this.mAccelerometer16msEventListeners.add(bandAccelerationEventListener);
            }
        } else if (reportingInterval == SampleRate.MS32) {
            this.mAccelerometer16msEventListeners.remove(bandAccelerationEventListener);
            this.mAccelerometer128msEventListeners.remove(bandAccelerationEventListener);
            if (!this.mAccelerometer32msEventListeners.contains(bandAccelerationEventListener)) {
                this.mAccelerometer32msEventListeners.add(bandAccelerationEventListener);
            }
        } else {
            this.mAccelerometer16msEventListeners.remove(bandAccelerationEventListener);
            this.mAccelerometer32msEventListeners.remove(bandAccelerationEventListener);
            if (!this.mAccelerometer128msEventListeners.contains(bandAccelerationEventListener)) {
                this.mAccelerometer128msEventListeners.add(bandAccelerationEventListener);
            }
        }
        boolean isEmpty = this.mAccelerometer16msEventListeners.isEmpty();
        if (!isEmpty) {
            return subscribe(48);
        }
        boolean isEmpty2 = this.mAccelerometer32msEventListeners.isEmpty();
        if (!isEmpty2) {
            return subscribe(1);
        }
        return subscribe(0);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterAccelerometerEventListeners() throws BandIOException {
        boolean hadListeners16 = !this.mAccelerometer16msEventListeners.isEmpty();
        if (hadListeners16) {
            this.mAccelerometer16msEventListeners.clear();
        }
        boolean hadListeners32 = !this.mAccelerometer32msEventListeners.isEmpty();
        if (hadListeners32) {
            this.mAccelerometer32msEventListeners.clear();
        }
        boolean hadListeners128 = !this.mAccelerometer128msEventListeners.isEmpty();
        if (hadListeners128) {
            this.mAccelerometer128msEventListeners.clear();
        }
        if (hadListeners16) {
            unsubscribe(48);
        } else if (hadListeners32) {
            unsubscribe(1);
        } else if (hadListeners128) {
            unsubscribe(0);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterAccelerometerEventListener(BandAccelerometerEventListener bandAccelerationEventListener) throws BandIOException {
        boolean wasRemoved16 = this.mAccelerometer16msEventListeners.remove(bandAccelerationEventListener);
        if (wasRemoved16 && !this.mAccelerometer32msEventListeners.isEmpty()) {
            subscribe(1);
            return;
        }
        boolean wasRemoved32 = this.mAccelerometer32msEventListeners.remove(bandAccelerationEventListener);
        if ((wasRemoved16 || wasRemoved32) && !this.mAccelerometer128msEventListeners.isEmpty()) {
            subscribe(0);
            return;
        }
        boolean wasRemoved128 = this.mAccelerometer128msEventListeners.remove(bandAccelerationEventListener);
        if (wasRemoved16) {
            unsubscribe(48);
        } else if (wasRemoved32) {
            unsubscribe(1);
        } else if (wasRemoved128) {
            unsubscribe(0);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerGyroscopeEventListener(BandGyroscopeEventListener bandGyroscopeEventListener, SampleRate reportingInterval) throws BandIOException {
        if (bandGyroscopeEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (reportingInterval == null) {
            throw new NullPointerException("Must have a reporting interval");
        }
        if (reportingInterval == SampleRate.MS16) {
            this.mGyroscope32msEventListeners.remove(bandGyroscopeEventListener);
            this.mGyroscope128msEventListeners.remove(bandGyroscopeEventListener);
            if (!this.mGyroscope16msEventListeners.contains(bandGyroscopeEventListener)) {
                this.mGyroscope16msEventListeners.add(bandGyroscopeEventListener);
            }
        } else if (reportingInterval == SampleRate.MS32) {
            this.mGyroscope16msEventListeners.remove(bandGyroscopeEventListener);
            this.mGyroscope128msEventListeners.remove(bandGyroscopeEventListener);
            if (!this.mGyroscope32msEventListeners.contains(bandGyroscopeEventListener)) {
                this.mGyroscope32msEventListeners.add(bandGyroscopeEventListener);
            }
        } else {
            this.mGyroscope16msEventListeners.remove(bandGyroscopeEventListener);
            this.mGyroscope32msEventListeners.remove(bandGyroscopeEventListener);
            if (!this.mGyroscope128msEventListeners.contains(bandGyroscopeEventListener)) {
                this.mGyroscope128msEventListeners.add(bandGyroscopeEventListener);
            }
        }
        boolean isEmpty = this.mGyroscope16msEventListeners.isEmpty();
        if (!isEmpty) {
            return subscribe(49);
        }
        boolean isEmpty2 = this.mGyroscope32msEventListeners.isEmpty();
        if (!isEmpty2) {
            return subscribe(5);
        }
        return subscribe(4);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterGyroscopeEventListeners() throws BandIOException {
        boolean hadListeners16 = !this.mGyroscope16msEventListeners.isEmpty();
        if (hadListeners16) {
            this.mGyroscope16msEventListeners.clear();
        }
        boolean hadListeners32 = !this.mGyroscope32msEventListeners.isEmpty();
        if (hadListeners32) {
            this.mGyroscope32msEventListeners.clear();
        }
        boolean hadListeners128 = !this.mGyroscope128msEventListeners.isEmpty();
        if (hadListeners128) {
            this.mGyroscope128msEventListeners.clear();
        }
        if (hadListeners16) {
            unsubscribe(49);
        } else if (hadListeners32) {
            unsubscribe(5);
        } else if (hadListeners128) {
            unsubscribe(4);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterGyroscopeEventListener(BandGyroscopeEventListener bandGyroscopeEventListener) throws BandIOException {
        boolean wasRemoved16 = this.mGyroscope16msEventListeners.remove(bandGyroscopeEventListener);
        if (wasRemoved16 && !this.mGyroscope32msEventListeners.isEmpty()) {
            subscribe(5);
            return;
        }
        boolean wasRemoved32 = this.mGyroscope32msEventListeners.remove(bandGyroscopeEventListener);
        if ((wasRemoved16 || wasRemoved32) && !this.mGyroscope128msEventListeners.isEmpty()) {
            subscribe(4);
            return;
        }
        boolean wasRemoved128 = this.mGyroscope128msEventListeners.remove(bandGyroscopeEventListener);
        if (wasRemoved16) {
            unsubscribe(49);
        } else if (wasRemoved32) {
            unsubscribe(5);
        } else if (wasRemoved128) {
            unsubscribe(4);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    @SuppressLint({"RtlHardcoded"})
    public void requestHeartRateConsent(Activity activity, final HeartRateConsentListener consentListener) {
        Resources r = activity.getResources();
        Validation.notNull(activity, "Activity cannot be null");
        final SharedPreferences preferences = getConsentPrefernces();
        LinearLayout mainLayout = new LinearLayout(activity);
        mainLayout.setOrientation(1);
        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(-1, -1);
        mainLayout.setPadding(0, BandTileManagerImpl.convertDipTpPixels(r, 65), 0, BandTileManagerImpl.convertDipTpPixels(r, 65));
        mainLayout.setLayoutParams(mainLayoutParams);
        mainLayout.setBackgroundColor(-16777216);
        View line = new View(activity);
        LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(-1, 1);
        line.setLayoutParams(lineLayoutParams);
        line.setBackgroundColor(-1);
        TextView tv1 = new TextView(activity);
        tv1.setTextColor(-1);
        tv1.setText("Microsoft Band Sensor Access");
        tv1.setTextSize(2, 20.0f);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(-2, -1);
        params1.gravity = 19;
        tv1.setPadding(BandTileManagerImpl.convertDipTpPixels(r, 10), 0, 0, 0);
        tv1.setLayoutParams(params1);
        TextView tv2 = new TextView(activity);
        tv2.setTextColor(-1);
        tv2.setText("Allow this application to access the heart rate sensor on your Microsoft Band?");
        tv2.setTextSize(2, 18.0f);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(-2, -1);
        params2.gravity = 19;
        tv2.setPadding(BandTileManagerImpl.convertDipTpPixels(r, 10), BandTileManagerImpl.convertDipTpPixels(r, 35), 0, 0);
        tv2.setLayoutParams(params2);
        mainLayout.addView(tv1);
        mainLayout.addView(line);
        mainLayout.addView(tv2);
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity, 3);
        alert.setView(mainLayout);
        alert.setCancelable(false);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.microsoft.band.BandSensorManagerImpl.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                KDKLog.d(BandSensorManagerImpl.TAG, "Positive button pressed on HR permission dialog");
                preferences.edit().putInt(InternalBandConstants.HR_ALLOWED, 1).commit();
                if (consentListener != null) {
                    consentListener.userAccepted(true);
                }
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() { // from class: com.microsoft.band.BandSensorManagerImpl.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                KDKLog.d(BandSensorManagerImpl.TAG, "Negative button pressed on HR permission dialog");
                preferences.edit().putInt(InternalBandConstants.HR_ALLOWED, 2).commit();
                if (consentListener != null) {
                    consentListener.userAccepted(false);
                }
                try {
                    BandSensorManagerImpl.this.unregisterHeartRateEventListeners();
                    BandSensorManagerImpl.this.unregisterRRIntervalEventListeners();
                } catch (BandIOException e) {
                    KDKLog.w(BandSensorManagerImpl.TAG, "Could not connect to service to unregister heart rate.");
                }
                dialog.dismiss();
            }
        });
        KDKLog.d(TAG, "HR permission dialog ready");
        Runnable dialogAction = new Runnable() { // from class: com.microsoft.band.BandSensorManagerImpl.3
            @Override // java.lang.Runnable
            public void run() {
                AlertDialog alertDialog = alert.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.microsoft.band.BandSensorManagerImpl.3.1
                    @Override // android.content.DialogInterface.OnShowListener
                    public void onShow(DialogInterface dialog) {
                        Button positiveButton = ((AlertDialog) dialog).getButton(-1);
                        positiveButton.setBackgroundColor(-16777216);
                        positiveButton.setTextColor(-1);
                        Button negativeButton = ((AlertDialog) dialog).getButton(-2);
                        negativeButton.setBackgroundColor(-16777216);
                        negativeButton.setTextColor(-1);
                    }
                });
                alertDialog.show();
                KDKLog.d(BandSensorManagerImpl.TAG, "HR permission dialog shown");
            }
        };
        activity.runOnUiThread(dialogAction);
        KDKLog.d(TAG, "HR permission dialog should be running in the main thread");
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public UserConsent getCurrentHeartRateConsent() {
        switch (getConsentPrefernces().getInt(InternalBandConstants.HR_ALLOWED, 0)) {
            case 1:
                return UserConsent.GRANTED;
            case 2:
                return UserConsent.DECLINED;
            default:
                return UserConsent.UNSPECIFIED;
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerHeartRateEventListener(BandHeartRateEventListener bandHeartRateEventListener) throws BandIOException, BandException {
        if (bandHeartRateEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (getConsentPrefernces().getInt(InternalBandConstants.HR_ALLOWED, 0) == 1) {
            if (!this.mHeartRateEventListeners.contains(bandHeartRateEventListener)) {
                this.mHeartRateEventListeners.add(bandHeartRateEventListener);
            }
            return subscribe(16);
        }
        throw new BandException("User has not given consent for the use of heart rate data.", BandErrorType.PERMISSION_ERROR);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterHeartRateEventListeners() throws BandIOException {
        boolean hasListenersToUnregister = !this.mHeartRateEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mHeartRateEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(16);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterHeartRateEventListener(BandHeartRateEventListener bandHeartRateEventListener) throws BandIOException {
        if (this.mHeartRateEventListeners.remove(bandHeartRateEventListener) && this.mHeartRateEventListeners.isEmpty()) {
            unsubscribe(16);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerPedometerEventListener(BandPedometerEventListener bandPedometerEventListener) throws BandIOException {
        if (bandPedometerEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (!this.mPedometerEventListeners.contains(bandPedometerEventListener)) {
            this.mPedometerEventListeners.add(bandPedometerEventListener);
        }
        return subscribe(19);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterPedometerEventListeners() throws BandIOException {
        boolean hasListenersToUnregister = !this.mPedometerEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mPedometerEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(19);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterPedometerEventListener(BandPedometerEventListener bandPedometerEventListener) throws BandIOException {
        if (this.mPedometerEventListeners.remove(bandPedometerEventListener) && this.mPedometerEventListeners.isEmpty()) {
            unsubscribe(19);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerDistanceEventListener(BandDistanceEventListener bandDistanceEventListener) throws BandIOException {
        if (bandDistanceEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (!this.mDistanceEventListeners.contains(bandDistanceEventListener)) {
            this.mDistanceEventListeners.add(bandDistanceEventListener);
        }
        return subscribe(13);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterDistanceEventListeners() throws BandIOException {
        boolean hasListenersToUnregister = !this.mDistanceEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mDistanceEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(13);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterDistanceEventListener(BandDistanceEventListener bandDistanceEventListener) throws BandIOException {
        if (this.mDistanceEventListeners.remove(bandDistanceEventListener) && this.mDistanceEventListeners.isEmpty()) {
            unsubscribe(13);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerSkinTemperatureEventListener(BandSkinTemperatureEventListener bandSkinTemperatureEventListener) throws BandIOException {
        if (bandSkinTemperatureEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (!this.mSkinTemperatureEventListeners.contains(bandSkinTemperatureEventListener)) {
            this.mSkinTemperatureEventListeners.add(bandSkinTemperatureEventListener);
        }
        return subscribe(20);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterSkinTemperatureEventListeners() throws BandIOException {
        boolean hasListenersToUnregister = !this.mSkinTemperatureEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mSkinTemperatureEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(20);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterSkinTemperatureEventListener(BandSkinTemperatureEventListener bandSkinTemperatureEventListener) throws BandIOException {
        if (this.mSkinTemperatureEventListeners.remove(bandSkinTemperatureEventListener) && this.mSkinTemperatureEventListeners.isEmpty()) {
            unsubscribe(20);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerUVEventListener(BandUVEventListener bandUVEventListener) throws BandIOException {
        if (bandUVEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (!this.mUVEventListeners.contains(bandUVEventListener)) {
            this.mUVEventListeners.add(bandUVEventListener);
        }
        return subscribe(21);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterUVEventListeners() throws BandIOException {
        boolean hasListenersToUnregister = !this.mUVEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mUVEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(21);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterUVEventListener(BandUVEventListener bandUVEventListener) throws BandIOException {
        if (this.mUVEventListeners.remove(bandUVEventListener) && this.mUVEventListeners.isEmpty()) {
            unsubscribe(21);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerContactEventListener(BandContactEventListener bandContactEventListener) throws BandIOException {
        if (bandContactEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (!this.mContactEventListeners.contains(bandContactEventListener)) {
            this.mContactEventListeners.add(bandContactEventListener);
        }
        return subscribe(35);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterContactEventListeners() throws BandIOException {
        boolean hasListenersToUnregister = !this.mContactEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mContactEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(35);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterContactEventListener(BandContactEventListener bandContactEventListener) throws BandIOException {
        if (this.mContactEventListeners.remove(bandContactEventListener) && this.mContactEventListeners.isEmpty()) {
            unsubscribe(35);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerCaloriesEventListener(BandCaloriesEventListener bandCaloriesEventListener) throws BandIOException {
        if (bandCaloriesEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (!this.mCaloriesEventListeners.contains(bandCaloriesEventListener)) {
            this.mCaloriesEventListeners.add(bandCaloriesEventListener);
        }
        return subscribe(46);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterCaloriesEventListeners() throws BandIOException {
        boolean hasListenersToUnregister = !this.mCaloriesEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mCaloriesEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(46);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterCaloriesEventListener(BandCaloriesEventListener bandCaloriesEventListener) throws BandIOException {
        if (this.mCaloriesEventListeners.remove(bandCaloriesEventListener) && this.mCaloriesEventListeners.isEmpty()) {
            unsubscribe(46);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerGsrEventListener(BandGsrEventListener bandGsrEventListener) throws BandIOException {
        if (bandGsrEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (!this.mGsrEventListeners.contains(bandGsrEventListener)) {
            this.mGsrEventListeners.add(bandGsrEventListener);
        }
        return subscribe(15);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterGsrEventListeners() throws BandIOException {
        boolean hasListenersToUnregister = !this.mGsrEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mGsrEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(15);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterGsrEventListener(BandGsrEventListener bandGsrEventListener) throws BandIOException {
        if (this.mGsrEventListeners.remove(bandGsrEventListener) && this.mGsrEventListeners.isEmpty()) {
            unsubscribe(15);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerAmbientLightEventListener(BandAmbientLightEventListener bandAmbientLightEventListener) throws BandIOException {
        if (bandAmbientLightEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (!this.mAmbientLightEventListeners.contains(bandAmbientLightEventListener)) {
            this.mAmbientLightEventListeners.add(bandAmbientLightEventListener);
        }
        return subscribe(25);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterAmbientLightEventListeners() throws BandIOException {
        boolean hasListenersToUnregister = !this.mAmbientLightEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mAmbientLightEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(25);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterAmbientLightEventListener(BandAmbientLightEventListener bandAmbientLightEventListener) throws BandIOException {
        if (this.mAmbientLightEventListeners.remove(bandAmbientLightEventListener) && this.mAmbientLightEventListeners.isEmpty()) {
            unsubscribe(25);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerRRIntervalEventListener(BandRRIntervalEventListener bandRRIntervalEventListener) throws BandIOException, BandException {
        if (bandRRIntervalEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (getConsentPrefernces().getInt(InternalBandConstants.HR_ALLOWED, 0) == 1) {
            if (!this.mRRIntervalEventListeners.contains(bandRRIntervalEventListener)) {
                this.mRRIntervalEventListeners.add(bandRRIntervalEventListener);
            }
            return subscribe(26);
        }
        throw new BandException("User has not given consent for the use of heart rate data.", BandErrorType.PERMISSION_ERROR);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterRRIntervalEventListeners() throws BandIOException {
        boolean hasListenersToUnregister = !this.mRRIntervalEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mRRIntervalEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(26);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterRRIntervalEventListener(BandRRIntervalEventListener bandRRIntervalEventListener) throws BandIOException {
        if (this.mRRIntervalEventListeners.remove(bandRRIntervalEventListener) && this.mRRIntervalEventListeners.isEmpty()) {
            unsubscribe(26);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerBarometerEventListener(BandBarometerEventListener bandBarometerEventListener) throws BandIOException {
        if (bandBarometerEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (!this.mBarometerEventListeners.contains(bandBarometerEventListener)) {
            this.mBarometerEventListeners.add(bandBarometerEventListener);
        }
        return subscribe(58);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterBarometerEventListeners() throws BandIOException {
        boolean hasListenersToUnregister = !this.mBarometerEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mBarometerEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(58);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterBarometerEventListener(BandBarometerEventListener bandBarometerEventListener) throws BandIOException {
        if (this.mBarometerEventListeners.remove(bandBarometerEventListener) && this.mBarometerEventListeners.isEmpty()) {
            unsubscribe(58);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public boolean registerAltimeterEventListener(BandAltimeterEventListener bandAltimeterEventListener) throws BandIOException {
        if (bandAltimeterEventListener == null) {
            throw new NullPointerException("Cannot register a null listener");
        }
        if (!this.mAltimeterEventListeners.contains(bandAltimeterEventListener)) {
            this.mAltimeterEventListeners.add(bandAltimeterEventListener);
        }
        return subscribe(71);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterAltimeterEventListeners() throws BandIOException {
        boolean hasListenersToUnregister = !this.mAltimeterEventListeners.isEmpty();
        if (hasListenersToUnregister) {
            this.mAltimeterEventListeners.clear();
        }
        if (hasListenersToUnregister) {
            unsubscribe(71);
        }
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterAltimeterEventListener(BandAltimeterEventListener bandAltimeterEventListener) throws BandIOException {
        if (this.mAltimeterEventListeners.remove(bandAltimeterEventListener) && this.mAltimeterEventListeners.isEmpty()) {
            unsubscribe(71);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean subscribe(int sensorCode) throws BandIOException {
        Bundle bundle = new Bundle();
        bundle.putInt(InternalBandConstants.EXTRA_COMMAND_DATA, sensorCode);
        this.mServiceConnection.sendWriteCommand(BandDeviceConstants.Command.SubscribeToSensor, bundle);
        return this.mServiceConnection.isDeviceConnected();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void unsubscribe(int sensorCode) throws BandIOException {
        Bundle bundle = new Bundle();
        bundle.putInt(InternalBandConstants.EXTRA_COMMAND_DATA, sensorCode);
        this.mServiceConnection.sendWriteCommand(BandDeviceConstants.Command.UnsubscribeToSensor, bundle);
    }

    @Override // com.microsoft.band.sensors.BandSensorManager
    public void unregisterAllListeners() throws BandIOException {
        this.mAccelerometer16msEventListeners.clear();
        this.mAccelerometer32msEventListeners.clear();
        this.mAccelerometer128msEventListeners.clear();
        this.mGyroscope16msEventListeners.clear();
        this.mGyroscope32msEventListeners.clear();
        this.mGyroscope128msEventListeners.clear();
        this.mHeartRateEventListeners.clear();
        this.mPedometerEventListeners.clear();
        this.mDistanceEventListeners.clear();
        this.mSkinTemperatureEventListeners.clear();
        this.mUVEventListeners.clear();
        this.mContactEventListeners.clear();
        this.mCaloriesEventListeners.clear();
        this.mGsrEventListeners.clear();
        this.mAmbientLightEventListeners.clear();
        this.mRRIntervalEventListeners.clear();
        this.mBarometerEventListeners.clear();
        this.mAltimeterEventListeners.clear();
        this.mServiceConnection.sendWriteCommand(BandDeviceConstants.Command.UnsubscribeToAllSensors, null);
    }

    @Override // com.microsoft.band.PushHandler
    public void handlePushData(int arg1, Parcelable parcelable) {
        switch (arg1) {
            case 0:
                BandAccelerometerEvent accelerometer128Event = (BandAccelerometerEvent) parcelable;
                for (BandAccelerometerEventListener listener : this.mAccelerometer128msEventListeners) {
                    listener.onBandAccelerometerChanged(accelerometer128Event);
                }
                return;
            case 1:
                this.mAccelerometerEventTracker++;
                BandAccelerometerEvent accelerometer32Event = (BandAccelerometerEvent) parcelable;
                for (BandAccelerometerEventListener listener2 : this.mAccelerometer32msEventListeners) {
                    listener2.onBandAccelerometerChanged(accelerometer32Event);
                }
                if (this.mAccelerometerEventTracker % 4 == 0) {
                    for (BandAccelerometerEventListener listener3 : this.mAccelerometer128msEventListeners) {
                        listener3.onBandAccelerometerChanged(accelerometer32Event);
                    }
                    return;
                }
                return;
            case 4:
                BandGyroscopeEvent gyroscope128Event = (BandGyroscopeEvent) parcelable;
                for (BandGyroscopeEventListener listener4 : this.mGyroscope128msEventListeners) {
                    listener4.onBandGyroscopeChanged(gyroscope128Event);
                }
                return;
            case 5:
                this.mGyroscopeEventTracker++;
                BandGyroscopeEvent gyroscope32Event = (BandGyroscopeEvent) parcelable;
                for (BandGyroscopeEventListener listener5 : this.mGyroscope32msEventListeners) {
                    listener5.onBandGyroscopeChanged(gyroscope32Event);
                }
                if (this.mGyroscopeEventTracker % 4 == 0) {
                    for (BandGyroscopeEventListener listener6 : this.mGyroscope128msEventListeners) {
                        listener6.onBandGyroscopeChanged(gyroscope32Event);
                    }
                    return;
                }
                return;
            case 13:
                BandDistanceEvent distanceEvent = (BandDistanceEvent) parcelable;
                for (BandDistanceEventListener listener7 : this.mDistanceEventListeners) {
                    listener7.onBandDistanceChanged(distanceEvent);
                }
                return;
            case 15:
                BandGsrEvent gsrEvent = (BandGsrEvent) parcelable;
                for (BandGsrEventListener listener8 : this.mGsrEventListeners) {
                    listener8.onBandGsrChanged(gsrEvent);
                }
                return;
            case 16:
                BandHeartRateEvent heartRateEvent = (BandHeartRateEvent) parcelable;
                for (BandHeartRateEventListener listener9 : this.mHeartRateEventListeners) {
                    listener9.onBandHeartRateChanged(heartRateEvent);
                }
                return;
            case 19:
                BandPedometerEvent pedometerEvent = (BandPedometerEvent) parcelable;
                for (BandPedometerEventListener listener10 : this.mPedometerEventListeners) {
                    listener10.onBandPedometerChanged(pedometerEvent);
                }
                return;
            case 20:
                BandSkinTemperatureEvent skinTempEvent = (BandSkinTemperatureEvent) parcelable;
                for (BandSkinTemperatureEventListener listener11 : this.mSkinTemperatureEventListeners) {
                    listener11.onBandSkinTemperatureChanged(skinTempEvent);
                }
                return;
            case 21:
                BandUVEvent uvEvent = (BandUVEvent) parcelable;
                for (BandUVEventListener listener12 : this.mUVEventListeners) {
                    listener12.onBandUVChanged(uvEvent);
                }
                return;
            case 25:
                BandAmbientLightEvent alsEvent = (BandAmbientLightEvent) parcelable;
                for (BandAmbientLightEventListener listener13 : this.mAmbientLightEventListeners) {
                    listener13.onBandAmbientLightChanged(alsEvent);
                }
                return;
            case 26:
                BandRRIntervalEvent rrIntervalEvent = (BandRRIntervalEvent) parcelable;
                for (BandRRIntervalEventListener listener14 : this.mRRIntervalEventListeners) {
                    listener14.onBandRRIntervalChanged(rrIntervalEvent);
                }
                return;
            case 35:
                BandContactEvent contactEvent = (BandContactEvent) parcelable;
                for (BandContactEventListener listener15 : this.mContactEventListeners) {
                    listener15.onBandContactChanged(contactEvent);
                }
                return;
            case 46:
                BandCaloriesEvent caloriesEvent = (BandCaloriesEvent) parcelable;
                for (BandCaloriesEventListener listener16 : this.mCaloriesEventListeners) {
                    listener16.onBandCaloriesChanged(caloriesEvent);
                }
                return;
            case 48:
                this.mAccelerometerEventTracker++;
                BandAccelerometerEvent accelerometer16Event = (BandAccelerometerEvent) parcelable;
                for (BandAccelerometerEventListener listener17 : this.mAccelerometer16msEventListeners) {
                    listener17.onBandAccelerometerChanged(accelerometer16Event);
                }
                if (this.mAccelerometerEventTracker % 2 == 0) {
                    for (BandAccelerometerEventListener listener18 : this.mAccelerometer32msEventListeners) {
                        listener18.onBandAccelerometerChanged(accelerometer16Event);
                    }
                }
                if (this.mAccelerometerEventTracker % 8 == 0) {
                    for (BandAccelerometerEventListener listener19 : this.mAccelerometer128msEventListeners) {
                        listener19.onBandAccelerometerChanged(accelerometer16Event);
                    }
                    return;
                }
                return;
            case 49:
                this.mGyroscopeEventTracker++;
                BandGyroscopeEvent gyroscope16Event = (BandGyroscopeEvent) parcelable;
                for (BandGyroscopeEventListener listener20 : this.mGyroscope16msEventListeners) {
                    listener20.onBandGyroscopeChanged(gyroscope16Event);
                }
                if (this.mGyroscopeEventTracker % 2 == 0) {
                    for (BandGyroscopeEventListener listener21 : this.mGyroscope32msEventListeners) {
                        listener21.onBandGyroscopeChanged(gyroscope16Event);
                    }
                }
                if (this.mGyroscopeEventTracker % 8 == 0) {
                    for (BandGyroscopeEventListener listener22 : this.mGyroscope128msEventListeners) {
                        listener22.onBandGyroscopeChanged(gyroscope16Event);
                    }
                    return;
                }
                return;
            case 58:
                BandBarometerEvent barometerEvent = (BandBarometerEvent) parcelable;
                for (BandBarometerEventListener listener23 : this.mBarometerEventListeners) {
                    listener23.onBandBarometerChanged(barometerEvent);
                }
                return;
            case 71:
                BandAltimeterEvent altimeterEvent = (BandAltimeterEvent) parcelable;
                for (BandAltimeterEventListener listener24 : this.mAltimeterEventListeners) {
                    listener24.onBandAltimeterChanged(altimeterEvent);
                }
                return;
            default:
                return;
        }
    }

    private SharedPreferences getConsentPrefernces() {
        return this.mServiceConnection.getContext().getSharedPreferences(InternalBandConstants.PREFERENCES, 0);
    }
}
