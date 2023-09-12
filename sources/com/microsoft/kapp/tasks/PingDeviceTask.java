package com.microsoft.kapp.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.diagnostics.Validate;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public abstract class PingDeviceTask extends AsyncTask<Void, Void, Boolean> {
    public static final int ACTIVITY = 0;
    public static final int FRAGMENT = 1;
    private final String TAG = getClass().getSimpleName();
    private CargoConnection mCargoConnection;
    protected Exception mExecuteException;
    private WeakReference<Activity> mParentActivityWeakReference;
    private WeakReference<Fragment> mParentFragmentWeakReference;
    private int mType;

    /* JADX INFO: Access modifiers changed from: protected */
    public PingDeviceTask(CargoConnection cargoConnection, Fragment parentFragment) {
        Validate.notNull(cargoConnection, "cargoConnection");
        this.mCargoConnection = cargoConnection;
        this.mParentFragmentWeakReference = new WeakReference<>(parentFragment);
        this.mType = 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PingDeviceTask(CargoConnection cargoConnection, Activity parentActivity) {
        Validate.notNull(cargoConnection, "cargoConnection");
        this.mCargoConnection = cargoConnection;
        this.mParentActivityWeakReference = new WeakReference<>(parentActivity);
        this.mType = 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public final Boolean doInBackground(Void... params) {
        DeviceInfo deviceInfo = getDevice();
        if (deviceInfo == null) {
            return false;
        }
        try {
            boolean isDeviceConnected = this.mCargoConnection.isDeviceAvailable(deviceInfo);
            return Boolean.valueOf(isDeviceConnected);
        } catch (Exception ex) {
            reportExecuteException(ex);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public final void onPostExecute(Boolean isDeviceConnected) {
        if (isParentValid()) {
            if (this.mExecuteException != null) {
                onExecuteFailed(this.mExecuteException);
            } else {
                onExecuteSucceeded(isDeviceConnected);
            }
        }
    }

    protected CargoConnection getCargoConnection() {
        return this.mCargoConnection;
    }

    protected void reportExecuteException(Exception ex) {
        Validate.notNull(ex, "ex");
        this.mExecuteException = ex;
    }

    protected void onExecuteSucceeded(Boolean result) {
    }

    protected boolean isParentValid() {
        Fragment parentFragment;
        if (this.mType == 0) {
            if (this.mParentActivityWeakReference != null && Validate.isActivityAlive(this.mParentActivityWeakReference.get())) {
                return true;
            }
        } else if (this.mType == 1 && this.mParentFragmentWeakReference != null && (parentFragment = this.mParentFragmentWeakReference.get()) != null && Validate.isActivityAlive(parentFragment.getActivity())) {
            return true;
        }
        return false;
    }

    protected void onExecuteFailed(Exception exception) {
    }

    private DeviceInfo getDevice() {
        try {
            return this.mCargoConnection.getConnectedDevice(true, false);
        } catch (Exception e) {
            Log.e(this.TAG, "An unexpected error occurred fetching devices", e);
            reportExecuteException(e);
            return null;
        }
    }
}
