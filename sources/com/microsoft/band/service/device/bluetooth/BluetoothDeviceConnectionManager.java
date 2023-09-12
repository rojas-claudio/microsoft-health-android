package com.microsoft.band.service.device.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.os.PowerManager;
import com.microsoft.band.internal.EventHandlerThread;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.device.CargoDeviceManager;
import com.microsoft.band.service.device.DeviceServiceProvider;
import com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection;
import com.microsoft.band.util.bluetooth.BluetoothAdapterObserver;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;
/* loaded from: classes.dex */
public class BluetoothDeviceConnectionManager extends BluetoothAdapterObserver {
    protected static final int BIND_DELAY_PERIOD = 5000;
    protected static final int DEFAULT_SCREEN_OFF_TIMEOUT = 0;
    protected static final int MSG_BIND_BLUETOOTH_DEVICE = 10002;
    protected static final int MSG_BIND_CONNECTION_LISTENER = 10001;
    protected static final int MSG_BIND_FREE_CONNECTIONS = 10003;
    protected static final int MSG_BIND_PAIRED_DEVICES = 10000;
    protected static final int MSG_DISCONNECT_ALL_IF_SCREEN_OFF = 10010;
    static final String TAG = BluetoothDeviceConnectionManager.class.getSimpleName();
    private static final IntentFilter mBroadcastIntentFilter = new IntentFilter();
    private volatile EventHandlerThread mEventHandlerThread;
    private final EventHandlerThread.IEventHandlerDelegate mEventMessageHandler = new EventHandlerThread.IEventHandlerDelegate() { // from class: com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnectionManager.1
        @Override // com.microsoft.band.internal.EventHandlerThread.IEventHandlerDelegate
        public void handleMessage(Message msg) {
            if (msg.what < BluetoothDeviceConnectionManager.MSG_BIND_PAIRED_DEVICES) {
                if (msg.obj instanceof BluetoothDeviceConnection) {
                    synchronized (BluetoothDeviceConnectionManager.this.mSyncRoot) {
                        BluetoothDeviceConnectionBinder connectionBinder = BluetoothDeviceConnectionManager.this.findConnectionBinder((BluetoothDeviceConnection) msg.obj);
                        if (connectionBinder != null) {
                            connectionBinder.unbind();
                        }
                    }
                }
                switch (msg.what) {
                    case 1000:
                        BluetoothDeviceConnectionManager.this.removeThenSendDelayedMessage(10003, msg.obj, 5000L);
                        return;
                    case 1001:
                        BluetoothDeviceConnectionManager.this.removeThenSendDelayedMessage(10001, msg.obj, 5000L);
                        return;
                    default:
                        return;
                }
            } else if (msg.what == BluetoothDeviceConnectionManager.MSG_DISCONNECT_ALL_IF_SCREEN_OFF) {
                KDKLog.i(BluetoothDeviceConnectionManager.TAG, "Screen off timed out, disconnecting all devices");
                synchronized (BluetoothDeviceConnectionManager.this.mSyncRoot) {
                    BluetoothDeviceConnectionManager.this.disconnectAll();
                }
            } else {
                synchronized (BluetoothDeviceConnectionManager.this.mSyncRoot) {
                    BluetoothDeviceConnectionManager.this.onEventHandlerMessage(msg);
                }
            }
        }
    };
    final Object mSyncRoot = new Object();
    private final LinkedList<BluetoothDeviceConnectionBinder> mConnectionBinders = new LinkedList<>();
    private volatile int mScreenOffTimeoutMillis = 0;

    static {
        mBroadcastIntentFilter.addAction("android.intent.action.SCREEN_ON");
        mBroadcastIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        mBroadcastIntentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        mBroadcastIntentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
    }

    @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver
    public void start(Context ownerContext) {
        synchronized (this.mSyncRoot) {
            super.start(ownerContext);
            ensureEventHandlerThreadInstance();
            if (this.mEventHandlerThread.startLooper()) {
                if (isScreenOn() || getScreenOffTimeoutMillis() == 0) {
                    sendMessage(MSG_BIND_PAIRED_DEVICES, null);
                }
            } else {
                KDKLog.e(TAG, "Event handler could not be started...");
                throw new RuntimeException("Unable to continue due to low system resources.");
            }
        }
    }

    @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver
    public void stop() {
        synchronized (this.mSyncRoot) {
            if (isStarted()) {
                disconnectAll();
                this.mEventHandlerThread.stopLooper();
                this.mEventHandlerThread = null;
            }
            super.stop();
        }
    }

    public void registerBluetoothBinding(BluetoothDeviceConnection.BluetoothDeviceConnectionListener listener, String bluetoothAddress, UUID serviceUuid) {
        if (listener == null) {
            throw new NullPointerException("listener argument is null");
        }
        if (bluetoothAddress == null) {
            throw new NullPointerException("bluetoothAddress is null");
        }
        synchronized (this.mSyncRoot) {
            if (findConnectionBinder(listener) != null) {
                throw new IllegalStateException("Duplicate listeners not allowed");
            }
            ensureEventHandlerThreadInstance();
            BluetoothDeviceConnectionBinder connectionBinder = new BluetoothDeviceConnectionBinder(this.mEventHandlerThread, listener, bluetoothAddress, serviceUuid);
            this.mConnectionBinders.add(connectionBinder);
            sendMessage(10001, connectionBinder);
        }
    }

    public void unregisterBluetoothBinding(BluetoothDeviceConnection.BluetoothDeviceConnectionListener listener) {
        BluetoothDeviceConnectionBinder connectionBinder = null;
        synchronized (this.mSyncRoot) {
            Iterator i$ = this.mConnectionBinders.iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                BluetoothDeviceConnectionBinder entry = i$.next();
                if (entry.getListener() == listener) {
                    connectionBinder = entry;
                    break;
                }
            }
            if (connectionBinder != null) {
                removeMessage(10001, connectionBinder);
                this.mConnectionBinders.remove(connectionBinder);
                connectionBinder.unbind();
            }
        }
    }

    public int getScreenOffTimeoutMillis() {
        return this.mScreenOffTimeoutMillis;
    }

    public void setScreenOffTimeoutMillis(int timeoutMillis) {
        if (timeoutMillis < 0) {
            throw new IllegalArgumentException("timeMillis is less than zero");
        }
        this.mScreenOffTimeoutMillis = timeoutMillis;
        ensureScreenOffTimeout();
    }

    public boolean isDeviceBound(BluetoothDevice bluetoothDevice, UUID connectionUUID) {
        boolean isBound = false;
        if (bluetoothDevice != null) {
            synchronized (this.mSyncRoot) {
                Iterator i$ = this.mConnectionBinders.iterator();
                while (true) {
                    if (!i$.hasNext()) {
                        break;
                    }
                    BluetoothDeviceConnectionBinder connectionBinder = i$.next();
                    if (connectionBinder.isBoundTo(bluetoothDevice) && connectionBinder.getPrimaryUuid().equals(connectionUUID)) {
                        isBound = true;
                        break;
                    }
                }
            }
        }
        return isBound;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver
    public void registerBroadCastReceivers() {
        super.registerBroadCastReceivers();
        BluetoothAdapterObserver.BluetoothBroadcastReceiver receiver = getBluetoothBroadcastReceiver();
        if (receiver != null) {
            getOwnerContext().registerReceiver(receiver, mBroadcastIntentFilter);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver
    public void onBroadcastReceived(Context context, Intent intent) {
        super.onBroadcastReceived(context, intent);
        String action = intent.getAction();
        if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
            KDKLog.d(TAG, "ACL Disconnect Received");
            BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            handleBluetoothDeviceDisconnected(bluetoothDevice);
        } else if ("android.bluetooth.device.action.ACL_CONNECTED".equals(action)) {
            KDKLog.d(TAG, "ACL Connect Received");
            BluetoothDevice bluetoothDevice2 = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            handleBluetoothDeviceConnected(bluetoothDevice2);
        } else if ("android.intent.action.SCREEN_OFF".equals(action) || "android.intent.action.SCREEN_ON".equals(action)) {
            ensureScreenOffTimeout();
        }
        if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action) || "android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action) || "android.intent.action.SCREEN_ON".equals(action)) {
            removeThenSendMessage(MSG_BIND_PAIRED_DEVICES, null);
        }
    }

    protected void ensureEventHandlerThreadInstance() {
        if (this.mEventHandlerThread == null) {
            this.mEventHandlerThread = new EventHandlerThread(TAG + ".MessageHandler", this.mEventMessageHandler);
        }
    }

    protected void ensureScreenOffTimeout() {
        removeMessage(MSG_DISCONNECT_ALL_IF_SCREEN_OFF, null);
        int timeoutMillis = this.mScreenOffTimeoutMillis;
        if (isBluetoothEnabled() && isStarted() && !isScreenOn() && timeoutMillis > 0) {
            KDKLog.i(TAG, "Screen is off, will disconnect all if timed out in " + timeoutMillis + "ms");
            sendDelayedMessage(MSG_DISCONNECT_ALL_IF_SCREEN_OFF, null, timeoutMillis);
        }
    }

    protected EventHandlerThread getEventHandler() {
        EventHandlerThread eventHandler = this.mEventHandlerThread;
        if (eventHandler == null) {
            throw new IllegalStateException("Event handler not initialized. Did you call start()?");
        }
        return eventHandler;
    }

    protected BluetoothDeviceConnectionBinder findConnectionBinder(BluetoothDeviceConnection.BluetoothDeviceConnectionListener listener) {
        Iterator i$ = this.mConnectionBinders.iterator();
        while (i$.hasNext()) {
            BluetoothDeviceConnectionBinder entry = i$.next();
            if (entry.getListener() == listener) {
                return entry;
            }
        }
        return null;
    }

    protected BluetoothDeviceConnectionBinder findConnectionBinder(BluetoothDeviceConnection connection) {
        Iterator i$ = this.mConnectionBinders.iterator();
        while (i$.hasNext()) {
            BluetoothDeviceConnectionBinder entry = i$.next();
            if (entry.isBoundTo(connection)) {
                return entry;
            }
        }
        return null;
    }

    protected BluetoothDeviceConnectionBinder findConnectionBinder(BluetoothDevice bluetoothDevice) {
        Iterator i$ = this.mConnectionBinders.iterator();
        while (i$.hasNext()) {
            BluetoothDeviceConnectionBinder entry = i$.next();
            if (entry.isBoundTo(bluetoothDevice)) {
                return entry;
            }
        }
        return null;
    }

    protected void disconnectAll() {
        if (this.mEventHandlerThread != null) {
            this.mEventHandlerThread.getHandler().removeCallbacksAndMessages(null);
        }
        Iterator i$ = this.mConnectionBinders.iterator();
        while (i$.hasNext()) {
            BluetoothDeviceConnectionBinder connectionBinder = i$.next();
            connectionBinder.disconnect();
        }
    }

    protected void onEventHandlerMessage(Message msg) {
        if (canConnect()) {
            switch (msg.what) {
                case MSG_BIND_PAIRED_DEVICES /* 10000 */:
                    handleBindPairedDevices();
                    return;
                case 10001:
                    BluetoothDeviceConnectionBinder connectionBinder = null;
                    if (msg.obj instanceof BluetoothDeviceConnection) {
                        BluetoothDeviceConnection connection = (BluetoothDeviceConnection) msg.obj;
                        connectionBinder = findConnectionBinder(connection);
                    } else if (msg.obj instanceof BluetoothDeviceConnectionBinder) {
                        connectionBinder = (BluetoothDeviceConnectionBinder) msg.obj;
                    }
                    if (connectionBinder != null) {
                        handleBindConnectionListener(connectionBinder);
                        return;
                    }
                    return;
                case 10002:
                    handleBindBluetoothDevice((BluetoothDevice) msg.obj);
                    return;
                case 10003:
                    handleBindFreeConnections((BluetoothDeviceConnection) msg.obj);
                    return;
                default:
                    return;
            }
        }
    }

    protected void handleBindPairedDevices() {
        KDKLog.i(TAG, "MSG_BIND_PAIRED_DEVICES");
        for (BluetoothDevice bluetoothDevice : getPairedDevices()) {
            removeMessage(10002, bluetoothDevice);
            handleBindBluetoothDevice(bluetoothDevice);
        }
    }

    protected void handleBindFreeConnections(BluetoothDeviceConnection bluetoothConnection) {
        KDKLog.i(TAG, "MSG_BIND_FREE_CONNECTIONS");
        Iterator i$ = this.mConnectionBinders.iterator();
        while (i$.hasNext()) {
            BluetoothDeviceConnectionBinder connectionBinder = i$.next();
            if (!connectionBinder.isBound() || connectionBinder.isConnectionIdle()) {
                connectionBinder.unbind();
                removeThenSendMessage(10001, connectionBinder);
            }
        }
    }

    protected void handleBindConnectionListener(BluetoothDeviceConnectionBinder connectionBinder) {
        KDKLog.i(TAG, "MSG_BIND_CONNECTION_LISTENER");
        if (this.mConnectionBinders.contains(connectionBinder)) {
            if (!connectionBinder.isBound() || connectionBinder.isConnectionIdle()) {
                for (BluetoothDevice bluetoothDevice : getPairedDevices()) {
                    if (!isDeviceBound(bluetoothDevice, connectionBinder.getPrimaryUuid()) || connectionBinder.isBoundTo(bluetoothDevice)) {
                        if (connectionBinder.bindAndConnect(bluetoothDevice)) {
                            return;
                        }
                    }
                }
            }
        }
    }

    protected void handleBindBluetoothDevice(BluetoothDevice bluetoothDevice) {
        KDKLog.i(TAG, "MSG_BIND_BLUETOOTH_DEVICE: " + bluetoothDevice);
        Iterator i$ = this.mConnectionBinders.iterator();
        while (i$.hasNext()) {
            BluetoothDeviceConnectionBinder connectionBinder = i$.next();
            if (!connectionBinder.isBound() || connectionBinder.isConnectionIdle()) {
                connectionBinder.bindAndConnect(bluetoothDevice);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver
    public void handleBluetoothDeviceBonded(BluetoothDevice bluetoothDevice) {
        super.handleBluetoothDeviceBonded(bluetoothDevice);
        removeThenSendMessage(10002, bluetoothDevice);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver
    public void handleBluetoothDeviceNotBonded(BluetoothDevice bluetoothDevice) {
        super.handleBluetoothDeviceNotBonded(bluetoothDevice);
        synchronized (this.mSyncRoot) {
            Iterator i$ = this.mConnectionBinders.iterator();
            while (i$.hasNext()) {
                BluetoothDeviceConnectionBinder connectionBinder = i$.next();
                if (connectionBinder.isBoundTo(bluetoothDevice)) {
                    KDKLog.i(TAG, "%s, unbind.", connectionBinder.getConnection());
                    connectionBinder.unbind();
                }
            }
        }
    }

    protected void handleBluetoothDeviceDisconnected(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice != null) {
            synchronized (this.mSyncRoot) {
                Iterator i$ = this.mConnectionBinders.iterator();
                while (i$.hasNext()) {
                    BluetoothDeviceConnectionBinder entry = i$.next();
                    if (entry.isBoundTo(bluetoothDevice) && entry.isConnected()) {
                        entry.disconnect();
                    }
                }
            }
        }
    }

    protected void handleBluetoothDeviceConnected(BluetoothDevice bluetoothDevice) {
        DeviceServiceProvider dp;
        if (bluetoothDevice != null && (dp = CargoDeviceManager.getInstance().getDeviceServiceProvider(new DeviceInfo(bluetoothDevice.getName(), bluetoothDevice.getAddress()))) != null) {
            dp.connectDevice();
        }
    }

    protected void removeMessage(int what, Object obj) {
        EventHandlerThread eventHandler = this.mEventHandlerThread;
        if (eventHandler != null && eventHandler.isLooping()) {
            if (obj == null) {
                eventHandler.getHandler().removeMessages(what);
            } else {
                eventHandler.getHandler().removeMessages(what, obj);
            }
        }
    }

    protected void sendMessage(int what, Object obj) {
        EventHandlerThread eventHandler = this.mEventHandlerThread;
        if (eventHandler != null && eventHandler.isLooping()) {
            Message msg = eventHandler.getHandler().obtainMessage(what, obj);
            msg.sendToTarget();
        }
    }

    protected void removeThenSendMessage(int what, Object obj) {
        removeMessage(what, obj);
        sendMessage(what, obj);
    }

    protected void removeThenSendDelayedMessage(int what, Object obj, long delayMillis) {
        removeMessage(what, obj);
        sendDelayedMessage(what, obj, delayMillis);
    }

    protected void sendDelayedMessage(int what, Object obj, long delayMillis) {
        EventHandlerThread eventHandler = this.mEventHandlerThread;
        if (eventHandler != null && eventHandler.isLooping()) {
            Message msg = eventHandler.getHandler().obtainMessage(what, obj);
            eventHandler.getHandler().sendMessageDelayed(msg, delayMillis);
        }
    }

    protected boolean isScreenOn() {
        Context context;
        if (!isStarted() || (context = getOwnerContext()) == null) {
            return false;
        }
        PowerManager powermanager = (PowerManager) context.getSystemService("power");
        boolean screenOn = powermanager.isScreenOn();
        return screenOn;
    }

    protected boolean canConnect() {
        return isBluetoothEnabled() && !isDiscovering() && isStarted() && (isScreenOn() || this.mScreenOffTimeoutMillis == 0);
    }
}
