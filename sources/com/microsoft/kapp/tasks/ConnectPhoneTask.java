package com.microsoft.kapp.tasks;

import com.microsoft.band.client.CargoException;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.models.ConnectedDevice;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class ConnectPhoneTask extends ScopedAsyncTask<Void, Void, Void> {
    private ConnectedDevice mDevice;
    private MultiDeviceManager mMultiDeviceManager;
    private SettingsProvider mSettingsProvider;
    private UserProfileFetcher mUserProfileFetcher;
    private WeakReference<IConnectPhoneTaskListener> mWeakActivity;

    /* loaded from: classes.dex */
    public interface IConnectPhoneTaskListener extends OnTaskListener {
        void onConnectPhoneFailed(Exception exc, boolean z, boolean z2);

        void onConnectPhoneSucceeded();
    }

    public ConnectPhoneTask(MultiDeviceManager multiDeviceManager, IConnectPhoneTaskListener onTaskListener, SettingsProvider settingsProvider, UserProfileFetcher userProfileFetcher, SensorUtils sensorUtils) {
        super(onTaskListener);
        this.mWeakActivity = new WeakReference<>(onTaskListener);
        this.mSettingsProvider = settingsProvider;
        this.mMultiDeviceManager = multiDeviceManager;
        this.mUserProfileFetcher = userProfileFetcher;
        ConnectedDevice device = new ConnectedDevice();
        device.setName(Constants.ANDROID_PHONE_IDENTIFIER);
        device.setUUID(sensorUtils.createUuidFromPhoneId());
        this.mDevice = device;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public Void doInBackground(Void... params) {
        try {
            this.mMultiDeviceManager.registerDevice(this.mDevice);
            this.mSettingsProvider.setIsSensorLoggingEnabled(true);
            this.mUserProfileFetcher.updateLocallyStoredValues();
            return null;
        } catch (CargoException e) {
            setException(e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public void onPostExecute(Void result) {
        IConnectPhoneTaskListener listener = this.mWeakActivity.get();
        if (listener != null) {
            if (getException() == null) {
                listener.onConnectPhoneSucceeded();
                return;
            }
            KLog.e(this.TAG, "ConnectPhoneTask failed!", getException());
            listener.onConnectPhoneFailed(getException(), true, false);
        }
    }
}
