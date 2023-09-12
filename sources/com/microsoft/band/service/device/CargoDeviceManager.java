package com.microsoft.band.service.device;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.CargoKit;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.BluetoothAdapterHelper;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnectionManager;
import com.microsoft.band.util.bluetooth.BluetoothAdapterObserver;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.StrappConstants;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public final class CargoDeviceManager {
    static final String TAG = CargoDeviceManager.class.getSimpleName();
    private static volatile CargoDeviceManager mSharedInstance;
    private final BluetoothAdapterObserver.IBluetoothObserverListerner mBluetoothStatusListener = new BluetoothAdapterObserver.IBluetoothObserverListerner() { // from class: com.microsoft.band.service.device.CargoDeviceManager.1
        @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver.IBluetoothEnabledListener
        public void onBluetoothEnabled(BluetoothAdapterObserver sender) {
            for (BluetoothDevice bluetoothDevice : BluetoothAdapterHelper.getPairedDevices()) {
                if (CargoKit.isCargoDevice(bluetoothDevice)) {
                    CargoDeviceManager.this.registerBluetoothDevice(bluetoothDevice, false);
                }
            }
        }

        @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver.IBluetoothEnabledListener
        public void onBluetoothDisabled(BluetoothAdapterObserver sender) {
            synchronized (CargoDeviceManager.this.mServiceProviders) {
                for (DeviceServiceProvider serviceProvider : CargoDeviceManager.this.mServiceProviders.values()) {
                    CargoDeviceManager.this.unregisterServiceProvider(serviceProvider);
                }
                CargoDeviceManager.this.mServiceProviders.clear();
            }
        }

        @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver.IBluetoothPairingListener
        public void onBluetoothDevicePaired(BluetoothAdapterObserver sender, BluetoothDevice bluetoothDevice, boolean respondingToBondedIntent) {
            boolean isCargoDevice = CargoKit.isCargoDevice(bluetoothDevice);
            KDKLog.d(CargoConstants.DISCOVERY_TAG, "Bluetooth device paired: Name = %s, MAC = %s, Major Device Class = %d, Device Class = %d, isCargoDevice = %s", bluetoothDevice.getName(), bluetoothDevice.getAddress(), Integer.valueOf(bluetoothDevice.getBluetoothClass().getMajorDeviceClass()), Integer.valueOf(bluetoothDevice.getBluetoothClass().getDeviceClass()), Boolean.valueOf(isCargoDevice));
            if (isCargoDevice) {
                CargoDeviceManager.this.registerBluetoothDevice(bluetoothDevice, respondingToBondedIntent);
            } else if (respondingToBondedIntent) {
                KDKLog.d(CargoConstants.DISCOVERY_TAG, "Device not recognized as cargo device.  Trying again...");
                try {
                    Thread.sleep(200L);
                    if (CargoKit.isCargoDevice(bluetoothDevice)) {
                        CargoDeviceManager.this.registerBluetoothDevice(bluetoothDevice, respondingToBondedIntent);
                    }
                } catch (InterruptedException e) {
                    KDKLog.e(CargoConstants.DISCOVERY_TAG, "Interupted while attempting second check of whether a device is a cargo device", e);
                }
            }
        }

        @Override // com.microsoft.band.util.bluetooth.BluetoothAdapterObserver.IBluetoothPairingListener
        public void onBluetoothDeviceUnpaired(BluetoothAdapterObserver sender, BluetoothDevice bluetoothDevice) {
            CargoDeviceManager.this.unregisterBluetoothDevice(bluetoothDevice);
        }
    };
    private final BluetoothDeviceConnectionManager mConnectionManager;
    private Context mContext;
    volatile ExecutorService mExecutorService;
    final Map<String, LinkedList<DeviceCommand>> mQueuedCommands;
    final Map<String, DeviceServiceProvider> mServiceProviders;

    public static CargoDeviceManager create(Context context) {
        CargoDeviceManager cargoDeviceManager = new CargoDeviceManager(context);
        mSharedInstance = cargoDeviceManager;
        return cargoDeviceManager;
    }

    public static CargoDeviceManager getInstance() {
        return mSharedInstance;
    }

    private CargoDeviceManager(Context context) {
        if (context == null) {
            throw new NullPointerException(Constants.FRE_INTENT_EXTRA_INFO);
        }
        this.mContext = context;
        this.mServiceProviders = new HashMap();
        this.mQueuedCommands = new HashMap();
        this.mConnectionManager = new BluetoothDeviceConnectionManager();
        this.mConnectionManager.registerListener(this.mBluetoothStatusListener);
    }

    Context getContext() {
        return this.mContext;
    }

    public void startup() {
        KDKLog.i(TAG, "Starting up Cargo Device Management.");
        this.mConnectionManager.registerListener(this.mBluetoothStatusListener);
        this.mConnectionManager.start(this.mContext);
        this.mExecutorService = Executors.newCachedThreadPool();
    }

    public void shutdown() {
        KDKLog.i(TAG, "Shutting down Cargo Device Management.");
        this.mConnectionManager.unregisterListener(this.mBluetoothStatusListener);
        this.mConnectionManager.stop();
        this.mExecutorService.shutdownNow();
        this.mExecutorService = null;
    }

    public DeviceServiceProvider getDeviceServiceProvider(DeviceInfo deviceInfo) {
        DeviceServiceProvider serviceProvider = null;
        synchronized (this.mServiceProviders) {
            if (!this.mServiceProviders.isEmpty()) {
                String bluetoothAddress = deviceInfo == null ? null : deviceInfo.getMacAddress();
                if (StringUtil.isNullOrEmpty(bluetoothAddress)) {
                    Iterator i$ = this.mServiceProviders.values().iterator();
                    while (true) {
                        if (!i$.hasNext()) {
                            break;
                        }
                        DeviceServiceProvider entry = i$.next();
                        if (!entry.hasListeners()) {
                            serviceProvider = entry;
                            break;
                        }
                    }
                    if (serviceProvider == null) {
                        serviceProvider = this.mServiceProviders.values().iterator().next();
                    }
                } else {
                    serviceProvider = this.mServiceProviders.get(bluetoothAddress);
                }
            }
        }
        return serviceProvider;
    }

    public void enqueueCommand(DeviceInfo deviceInfo, DeviceCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command argument not specified");
        }
        LinkedList<DeviceCommand> commandList = obtainCommandList(deviceInfo);
        if (commandList != null) {
            synchronized (commandList) {
                int queuedCount = 0;
                if (!commandList.isEmpty()) {
                    Iterator<DeviceCommand> iter = commandList.iterator();
                    while (iter.hasNext()) {
                        if (iter.next().equals(command) && (queuedCount = queuedCount + 1) >= command.getQueueLimit()) {
                            KDKLog.w(TAG, "Evicting eldest queued %s command to keep queue limit at %d.", command.getCommandType(), Integer.valueOf(command.getQueueLimit()));
                            iter.remove();
                        }
                    }
                }
                KDKLog.d(TAG, "Queuing %s command for dispatch when device is connected.", command.getCommandType());
                command.setQueueLimit(0);
                commandList.addFirst(command);
                commandList.notifyAll();
            }
        }
    }

    public void dispatchQueuedCommands(DeviceInfo deviceInfo) {
        final LinkedList<DeviceCommand> commandList;
        ExecutorService executor = this.mExecutorService;
        if (executor == null) {
            throw new IllegalStateException("Service terminated.");
        }
        final DeviceServiceProvider deviceProvider = getDeviceServiceProvider(deviceInfo);
        if (deviceProvider != null && (commandList = obtainCommandList(deviceInfo)) != null) {
            executor.submit(new Runnable() { // from class: com.microsoft.band.service.device.CargoDeviceManager.2
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (commandList) {
                        while (!commandList.isEmpty()) {
                            DeviceCommand command = (DeviceCommand) commandList.getLast();
                            BandServiceMessage.Response response = deviceProvider.processCommand(command);
                            if (response.isError()) {
                                KDKLog.w(CargoDeviceManager.TAG, "Failed to process queued %s command, result = %s", command.getCommandType(), response);
                                if (!command.hasResponse()) {
                                    break;
                                }
                                commandList.removeLast();
                            } else {
                                commandList.removeLast();
                                KDKLog.w(CargoDeviceManager.TAG, "Processed queued %s command successfully.", command.getCommandType());
                            }
                        }
                    }
                }
            });
        }
    }

    public boolean hasQueuedCommands(DeviceInfo deviceInfo) {
        DeviceServiceProvider deviceProvider = getDeviceServiceProvider(deviceInfo);
        boolean hasQueuedCommands = false;
        if (deviceProvider != null) {
            List<DeviceCommand> commandList = obtainCommandList(deviceInfo);
            synchronized (commandList) {
                hasQueuedCommands = !commandList.isEmpty();
            }
        }
        return hasQueuedCommands;
    }

    private LinkedList<DeviceCommand> obtainCommandList(DeviceInfo deviceInfo) {
        if (deviceInfo == null) {
            throw new IllegalArgumentException("deviceInfo argument not specified");
        }
        synchronized (this.mQueuedCommands) {
            try {
                LinkedList<DeviceCommand> commandList = this.mQueuedCommands.get(deviceInfo.getMacAddress());
                if (commandList == null) {
                    LinkedList<DeviceCommand> commandList2 = new LinkedList<>();
                    try {
                        this.mQueuedCommands.put(deviceInfo.getMacAddress(), commandList2);
                        commandList = commandList2;
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                return commandList;
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    protected void registerBluetoothDevice(BluetoothDevice bluetoothDevice, boolean respondingToBondedIntent) {
        if (bluetoothDevice != null) {
            synchronized (this.mServiceProviders) {
                String bluetoothAddress = bluetoothDevice.getAddress();
                String deviceName = bluetoothDevice.getName();
                if (!this.mServiceProviders.containsKey(bluetoothAddress)) {
                    DeviceInfo deviceInfo = new DeviceInfo(deviceName, bluetoothAddress);
                    DeviceServiceProvider serviceProvider = new DeviceServiceProvider(deviceInfo);
                    this.mServiceProviders.put(bluetoothAddress, serviceProvider);
                    this.mConnectionManager.registerBluetoothBinding(serviceProvider.getBluetoothDeviceProtocolConnectionListener(), bluetoothAddress, CargoBluetoothProtocolConnection.NORMAL_PROTOCOL_UUID);
                    this.mConnectionManager.registerBluetoothBinding(serviceProvider.getBluetoothDevicePushConnectionListener(), bluetoothAddress, CargoBluetoothPushConnection.PUSH_PROTOCOL_UUID);
                }
                if (respondingToBondedIntent) {
                    Intent pairingComplete = new Intent();
                    pairingComplete.setAction(CargoConstants.ACTION_PAIRING_REACTION_FINISHED);
                    pairingComplete.setPackage(StrappConstants.NOTIFICATION_SERVICE_MICROSOFT_HEALTH);
                    pairingComplete.putExtra(CargoConstants.EXTRA_REGISTERED_DEVICE, bluetoothDevice);
                    getContext().sendBroadcast(pairingComplete);
                }
            }
        }
    }

    protected void unregisterBluetoothDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice != null) {
            synchronized (this.mServiceProviders) {
                String bluetoothAddress = bluetoothDevice.getAddress();
                DeviceServiceProvider serviceProvider = this.mServiceProviders.remove(bluetoothAddress);
                unregisterServiceProvider(serviceProvider);
            }
        }
    }

    void unregisterServiceProvider(DeviceServiceProvider serviceProvider) {
        if (serviceProvider != null) {
            this.mConnectionManager.unregisterBluetoothBinding(serviceProvider.getBluetoothDeviceProtocolConnectionListener());
            this.mConnectionManager.unregisterBluetoothBinding(serviceProvider.getBluetoothDevicePushConnectionListener());
            serviceProvider.dispose();
        }
    }
}
