package com.microsoft.band.util.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.microsoft.band.internal.util.BluetoothAdapterHelper;
import com.microsoft.band.internal.util.KDKLog;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
/* loaded from: classes.dex */
public class BluetoothAdapterObserver extends BluetoothAdapterHelper {
    private static final String TAG = BluetoothAdapterObserver.class.getSimpleName();
    private static final IntentFilter mBluetoothIntentFilter = new IntentFilter();
    private Context mOwnerContext;
    private final BluetoothBroadcastReceiver mReceiver;
    private final BluetoothStatusListenerProxy mStatusListenerProxy;

    /* loaded from: classes.dex */
    public interface IBluetoothEnabledListener extends IBluetoothStatusListener {
        void onBluetoothDisabled(BluetoothAdapterObserver bluetoothAdapterObserver);

        void onBluetoothEnabled(BluetoothAdapterObserver bluetoothAdapterObserver);
    }

    /* loaded from: classes.dex */
    public interface IBluetoothObserverListerner extends IBluetoothEnabledListener, IBluetoothPairingListener {
    }

    /* loaded from: classes.dex */
    public interface IBluetoothPairingListener extends IBluetoothStatusListener {
        void onBluetoothDevicePaired(BluetoothAdapterObserver bluetoothAdapterObserver, BluetoothDevice bluetoothDevice, boolean z);

        void onBluetoothDeviceUnpaired(BluetoothAdapterObserver bluetoothAdapterObserver, BluetoothDevice bluetoothDevice);
    }

    /* loaded from: classes.dex */
    public interface IBluetoothStatusListener {
    }

    static {
        mBluetoothIntentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        mBluetoothIntentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
    }

    public BluetoothAdapterObserver() {
        if (BluetoothAdapterHelper.isBluetoothAdapterAvailable()) {
            this.mReceiver = new BluetoothBroadcastReceiver();
        } else {
            this.mReceiver = null;
        }
        this.mStatusListenerProxy = new BluetoothStatusListenerProxy();
    }

    public boolean isStarted() {
        return this.mOwnerContext != null;
    }

    public Context getOwnerContext() {
        return this.mOwnerContext;
    }

    public void start(Context ownerContext) {
        if (isStarted()) {
            throw new IllegalStateException("already started.");
        }
        if (ownerContext == null) {
            throw new NullPointerException("ownerContext argument is null");
        }
        this.mOwnerContext = ownerContext;
        registerBroadCastReceivers();
    }

    public void stop() {
        if (isStarted()) {
            unregisterBroadCastReceivers();
            this.mOwnerContext = null;
        }
    }

    public void registerListener(IBluetoothStatusListener listener) {
        this.mStatusListenerProxy.registerListener(listener);
    }

    public void unregisterListener(IBluetoothStatusListener listener) {
        this.mStatusListenerProxy.unregisterListener(listener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void registerBroadCastReceivers() {
        BluetoothBroadcastReceiver receiver = getBluetoothBroadcastReceiver();
        if (receiver != null) {
            this.mOwnerContext.registerReceiver(receiver, mBluetoothIntentFilter);
        }
    }

    protected void unregisterBroadCastReceivers() {
        BluetoothBroadcastReceiver receiver = getBluetoothBroadcastReceiver();
        if (receiver != null) {
            try {
                this.mOwnerContext.unregisterReceiver(receiver);
            } catch (IllegalArgumentException e) {
                KDKLog.w(TAG, "Failed to unregister Bluetooth broadcast receiver: " + e.getMessage());
            }
        }
    }

    public void enableBluetooth() {
        enableBluetooth(getOwnerContext());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onBroadcastReceived(Context context, Intent intent) {
        String action = intent.getAction();
        KDKLog.i(TAG, "BroadcastReceived: " + action);
        if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
            int extraState = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
            if (extraState == 12) {
                this.mStatusListenerProxy.onBluetoothEnabled(this);
            } else if (extraState == 10) {
                this.mStatusListenerProxy.onBluetoothDisabled(this);
            }
        } else if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(action)) {
            int bondState = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", -1);
            BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            KDKLog.i(TAG, "MSG_BLUETOOTH_DEVICE_BONDING: %s, state = %d.", bluetoothDevice, Integer.valueOf(bondState));
            if (bluetoothDevice != null) {
                if (bondState == 12) {
                    handleBluetoothDeviceBonded(bluetoothDevice);
                } else if (bondState == 10) {
                    Set<BluetoothDevice> pairedDevices = getPairedDevices();
                    if (!pairedDevices.contains(bluetoothDevice)) {
                        handleBluetoothDeviceNotBonded(bluetoothDevice);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BluetoothBroadcastReceiver getBluetoothBroadcastReceiver() {
        return this.mReceiver;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class BluetoothBroadcastReceiver extends BroadcastReceiver {
        protected BluetoothBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BluetoothAdapterObserver.this.onBroadcastReceived(context, intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleBluetoothDeviceBonded(BluetoothDevice bluetoothDevice) {
        this.mStatusListenerProxy.onBluetoothDevicePaired(this, bluetoothDevice, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleBluetoothDeviceNotBonded(BluetoothDevice bluetoothDevice) {
        this.mStatusListenerProxy.onBluetoothDeviceUnpaired(this, bluetoothDevice);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class BluetoothStatusListenerProxy implements IBluetoothObserverListerner {
        private final LinkedList<IBluetoothStatusListener> mListeners = new LinkedList<>();

        public BluetoothStatusListenerProxy() {
        }

        @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver.IBluetoothEnabledListener
        public synchronized void onBluetoothEnabled(BluetoothAdapterObserver sender) {
            Iterator i$ = this.mListeners.iterator();
            while (i$.hasNext()) {
                IBluetoothStatusListener listener = i$.next();
                if (listener instanceof IBluetoothEnabledListener) {
                    try {
                        ((IBluetoothEnabledListener) listener).onBluetoothEnabled(sender);
                    } catch (Exception e) {
                        KDKLog.w(BluetoothAdapterObserver.TAG, "onBluetoothEnabled caught exception with message: " + e.getMessage(), e);
                    }
                }
            }
        }

        @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver.IBluetoothEnabledListener
        public synchronized void onBluetoothDisabled(BluetoothAdapterObserver sender) {
            Iterator i$ = this.mListeners.iterator();
            while (i$.hasNext()) {
                IBluetoothStatusListener listener = i$.next();
                if (listener instanceof IBluetoothEnabledListener) {
                    try {
                        ((IBluetoothEnabledListener) listener).onBluetoothDisabled(sender);
                    } catch (Exception e) {
                        KDKLog.w(BluetoothAdapterObserver.TAG, "onBluetoothDisabled caught exception with message: " + e.getMessage(), e);
                    }
                }
            }
        }

        @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver.IBluetoothPairingListener
        public synchronized void onBluetoothDevicePaired(BluetoothAdapterObserver sender, BluetoothDevice bluetoothDevice, boolean respondingToBondedIntent) {
            Iterator i$ = this.mListeners.iterator();
            while (i$.hasNext()) {
                IBluetoothStatusListener listener = i$.next();
                if (listener instanceof IBluetoothPairingListener) {
                    try {
                        ((IBluetoothPairingListener) listener).onBluetoothDevicePaired(sender, bluetoothDevice, respondingToBondedIntent);
                    } catch (Exception e) {
                        KDKLog.w(BluetoothAdapterObserver.TAG, "onBluetoothDevicePaired caught exception with message: " + e.getMessage(), e);
                    }
                }
            }
        }

        @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver.IBluetoothPairingListener
        public synchronized void onBluetoothDeviceUnpaired(BluetoothAdapterObserver sender, BluetoothDevice bluetoothDevice) {
            Iterator i$ = this.mListeners.iterator();
            while (i$.hasNext()) {
                IBluetoothStatusListener listener = i$.next();
                if (listener instanceof IBluetoothPairingListener) {
                    try {
                        ((IBluetoothPairingListener) listener).onBluetoothDeviceUnpaired(sender, bluetoothDevice);
                    } catch (Exception e) {
                        KDKLog.w(BluetoothAdapterObserver.TAG, "onBluetoothDeviceUnpaired caught exception with message: " + e.getMessage(), e);
                    }
                }
            }
        }

        public synchronized void registerListener(IBluetoothStatusListener listener) {
            if (listener == null) {
                throw new NullPointerException("listener argument is null");
            }
            if (!this.mListeners.contains(listener)) {
                this.mListeners.add(listener);
                if (BluetoothAdapterHelper.isBluetoothAdapterAvailable()) {
                    try {
                        if (listener instanceof IBluetoothEnabledListener) {
                            if (BluetoothAdapterHelper.isBluetoothEnabled()) {
                                ((IBluetoothEnabledListener) listener).onBluetoothEnabled(BluetoothAdapterObserver.this);
                            } else {
                                ((IBluetoothEnabledListener) listener).onBluetoothDisabled(BluetoothAdapterObserver.this);
                            }
                        }
                        if (listener instanceof IBluetoothPairingListener) {
                            for (BluetoothDevice bluetoothDevice : BluetoothAdapterHelper.getPairedDevices()) {
                                ((IBluetoothPairingListener) listener).onBluetoothDevicePaired(BluetoothAdapterObserver.this, bluetoothDevice, false);
                            }
                        }
                    } catch (Exception e) {
                        KDKLog.e(BluetoothAdapterObserver.class.getSimpleName(), "Listener thrown exception", e);
                    }
                }
            }
        }

        public synchronized void unregisterListener(IBluetoothStatusListener listener) {
            if (listener == null) {
                throw new NullPointerException("listener argument is null");
            }
            this.mListeners.remove(listener);
        }
    }
}
