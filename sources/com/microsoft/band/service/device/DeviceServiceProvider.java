package com.microsoft.band.service.device;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Base64;
import com.microsoft.band.BandTheme;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.cloud.EphemerisUpdateInfo;
import com.microsoft.band.cloud.TimeZoneSettingsUpdateInfo;
import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.device.DeviceProfileInfo;
import com.microsoft.band.device.FWVersion;
import com.microsoft.band.device.NotificationGenericDialog;
import com.microsoft.band.device.NotificationGenericUpdate;
import com.microsoft.band.device.StartStrip;
import com.microsoft.band.device.StrappColorPalette;
import com.microsoft.band.device.StrappIcon;
import com.microsoft.band.device.StrappLayout;
import com.microsoft.band.device.StrappListFromDevice;
import com.microsoft.band.device.StrappMessage;
import com.microsoft.band.device.StrappPageElement;
import com.microsoft.band.device.StrappSettingsData;
import com.microsoft.band.device.TileSettings;
import com.microsoft.band.device.TimeZoneInfo;
import com.microsoft.band.device.command.CargoFileName;
import com.microsoft.band.device.command.CommandRead;
import com.microsoft.band.device.command.CommandWrite;
import com.microsoft.band.device.command.DeviceProfileGet;
import com.microsoft.band.device.command.FileGetSize;
import com.microsoft.band.device.command.FileRead;
import com.microsoft.band.device.command.FileWrite;
import com.microsoft.band.device.command.FlushLoggerCommand;
import com.microsoft.band.device.command.GetUTCTime;
import com.microsoft.band.device.command.GetVersion;
import com.microsoft.band.device.command.GetVersionValidation;
import com.microsoft.band.device.command.NotificationCommand;
import com.microsoft.band.device.command.SetUTCTime;
import com.microsoft.band.device.enums.VersionType;
import com.microsoft.band.device.subscription.TextMessageData;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.device.subscription.DeviceContactData;
import com.microsoft.band.internal.device.subscription.SubscriptionDataModel;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.UUIDHelper;
import com.microsoft.band.internal.util.VersionCheck;
import com.microsoft.band.sensors.BandContactState;
import com.microsoft.band.service.BandService;
import com.microsoft.band.service.CargoClientSession;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.band.service.CargoServiceProvider;
import com.microsoft.band.service.cloud.CloudDataResource;
import com.microsoft.band.service.command.BatchCommand;
import com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection;
import com.microsoft.band.service.logger.LoggerFactory;
import com.microsoft.band.service.subscription.PushConnectionListener;
import com.microsoft.band.service.subscription.SubscriptionDataContract;
import com.microsoft.band.service.util.FileHelper;
import com.microsoft.band.tiles.BandTile;
import com.microsoft.band.tiles.CargoTileEvent;
import com.microsoft.band.tiles.TileUtils;
import com.microsoft.band.tiles.pages.LayoutTemplate;
import com.microsoft.band.tiles.pages.PageData;
import com.microsoft.band.tiles.pages.PageElementData;
import com.microsoft.band.tiles.pages.PageLayout;
import com.microsoft.band.tiles.pages.TextBlockData;
import com.microsoft.kapp.utils.StrappConstants;
import java.io.File;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class DeviceServiceProvider extends CargoServiceProvider {
    private final DeviceInfo mDeviceInfo;
    private final LinkedList<DeviceConnectionListener> mListeners;
    private final CargoProtocolConnection mProtocolConnection;
    private final CargoPushConnection mPushConnection;
    private static final String TAG = DeviceServiceProvider.class.getSimpleName();
    private static final String STREAMING_TAG = TAG + ": " + InternalBandConstants.STREAM_TAG;
    protected static final ExecutorService mExecutorService = new ThreadPoolExecutor(0, 1, 20, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private final Object mSyncLock = new Object();
    private volatile BandContactState mContactState = BandContactState.UNKNOWN;

    /* loaded from: classes.dex */
    public interface DeviceConnectionListener extends PushConnectionListener {
        void onDeviceConnected(DeviceServiceProvider deviceServiceProvider);

        void onDeviceConnectionDisposed(DeviceServiceProvider deviceServiceProvider);

        void onDeviceDisconnected(DeviceServiceProvider deviceServiceProvider);

        void onDeviceTimeout(DeviceServiceProvider deviceServiceProvider);
    }

    public DeviceServiceProvider(DeviceInfo deviceInfo) {
        if (deviceInfo == null) {
            throw new IllegalArgumentException("deviceInfo not specified");
        }
        this.mDeviceInfo = deviceInfo;
        this.mProtocolConnection = new CargoBluetoothProtocolConnection(this);
        this.mPushConnection = new CargoBluetoothPushConnection(this);
        this.mListeners = new LinkedList<>();
    }

    public DeviceInfo getDeviceInfo() {
        return this.mDeviceInfo;
    }

    public UUID getDeviceUUID() {
        return getProtocolConnection().getDeviceUUID();
    }

    public String getFWVersion() {
        FWVersion[] fwVersions = getProtocolConnection().getFirmwareVersions();
        int appIndex = DeviceConstants.AppRunning.APP_RUNNING_APP.getFirmwareVersionIndex();
        if (fwVersions == null || fwVersions.length <= appIndex) {
            KDKLog.e(TAG, "getFWVersion Device firmware version does not contain App Version");
            return null;
        }
        return fwVersions[appIndex].getCurrentVersion();
    }

    public int getHardwareVersion() {
        FWVersion[] fwVersions = getProtocolConnection().getFirmwareVersions();
        int appIndex = DeviceConstants.AppRunning.APP_RUNNING_APP.getFirmwareVersionIndex();
        if (fwVersions == null || fwVersions.length <= appIndex) {
            KDKLog.e(TAG, "getHardwareVersion Device firmware version does not contain App Version");
            return 0;
        }
        return fwVersions[appIndex].getPcbId();
    }

    public int getLogVersion() {
        return getProtocolConnection().getLogVersion();
    }

    public String getSerialNumber() {
        return getProtocolConnection().getSerialNumber();
    }

    public boolean isDeviceConnected() {
        return getProtocolConnection().isConnected();
    }

    public boolean hasListeners() {
        boolean z;
        synchronized (this.mListeners) {
            z = !this.mListeners.isEmpty();
        }
        return z;
    }

    public boolean isUpdatingFirmware() {
        return this.mProtocolConnection.isUpdatingFirmware();
    }

    @NonNull
    protected CargoProtocolConnection getProtocolConnection() {
        return this.mProtocolConnection;
    }

    protected CargoPushConnection getPushConnection() {
        return this.mPushConnection;
    }

    public BandServiceMessage.Response connectDevice() {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.1
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response;
                BandServiceMessage.Response response2 = BandServiceMessage.Response.DEVICE_NOT_BONDED_ERROR;
                if (!DeviceServiceProvider.this.mProtocolConnection.isUpdatingFirmware()) {
                    try {
                        DeviceServiceProvider.this.mProtocolConnection.connect(true);
                        if (DeviceServiceProvider.this.mProtocolConnection.isConnected()) {
                            response = BandServiceMessage.Response.SUCCESS;
                            DeviceServiceProvider.this.getPushConnection().connect();
                        } else {
                            response = BandServiceMessage.Response.DEVICE_NOT_CONNECTED_ERROR;
                        }
                        return response;
                    } catch (IOException e) {
                        KDKLog.e(DeviceServiceProvider.TAG, e, "Failed to connect to device %s: %s", DeviceServiceProvider.this.mProtocolConnection, e.getMessage());
                        BandServiceMessage.Response response3 = BandServiceMessage.Response.DEVICE_IO_ERROR;
                        return response3;
                    }
                }
                BandServiceMessage.Response response4 = BandServiceMessage.Response.DEVICE_STATE_ERROR;
                return response4;
            }
        });
    }

    public void registerListener(DeviceConnectionListener listener) {
        if (listener != null) {
            synchronized (this.mListeners) {
                if (!this.mListeners.contains(listener)) {
                    this.mListeners.add(listener);
                    onDeviceConnectionStatusChanged(listener, false);
                }
            }
        }
    }

    public void unregisterListener(DeviceConnectionListener listener) {
        if (listener != null) {
            int count = listener.getSubscriptionCount();
            synchronized (this.mListeners) {
                this.mListeners.remove(listener);
            }
            if (count > 0) {
                updateSubscriptions();
            }
        }
    }

    public void dispose() {
        this.mPushConnection.dispose();
        this.mProtocolConnection.dispose();
        this.mProtocolConnection.waitForDeviceToDisconnect();
        synchronized (this.mListeners) {
            while (!this.mListeners.isEmpty()) {
                DeviceConnectionListener listener = this.mListeners.removeFirst();
                listener.onDeviceConnectionDisposed(this);
            }
        }
    }

    public BandServiceMessage.Response updateSubscriptions() {
        BitSet serviceSubscriptions = SubscriptionDataContract.createBitSet(null);
        synchronized (this.mListeners) {
            Iterator i$ = this.mListeners.iterator();
            while (i$.hasNext()) {
                DeviceConnectionListener listener = i$.next();
                listener.updateSubscriptionsSet(serviceSubscriptions);
            }
        }
        return modifyDeviceSubscriptions(serviceSubscriptions);
    }

    private BandServiceMessage.Response modifyDeviceSubscriptions(BitSet serviceSubscriptions) {
        BandServiceMessage.Response response = BandServiceMessage.Response.UNSPECIFIED;
        BitSet deviceSubscriptions = null;
        try {
            deviceSubscriptions = getDeviceSubscriptions();
        } catch (CargoServiceException e) {
            response = e.getResponse();
        }
        if (deviceSubscriptions != null) {
            extraSubscriptionLogic(serviceSubscriptions);
            BitSet deviceSubscriptionsClone = (BitSet) deviceSubscriptions.clone();
            deviceSubscriptions.andNot(serviceSubscriptions);
            unsubscribe(deviceSubscriptions);
            if (serviceSubscriptions.cardinality() == 0) {
                BandServiceMessage.Response response2 = BandServiceMessage.Response.SUCCESS;
                return response2;
            }
            serviceSubscriptions.andNot(deviceSubscriptionsClone);
            synchronized (this.mSyncLock) {
                try {
                    getPushConnection().connect();
                } catch (IOException e2) {
                    KDKLog.w(STREAMING_TAG, "Failed to establish push connecton.", e2);
                }
            }
            BandServiceMessage.Response response3 = subscribe(serviceSubscriptions);
            return response3;
        }
        return response;
    }

    private void extraSubscriptionLogic(BitSet serviceSubscriptions) {
        serviceSubscriptions.set(35);
        if (serviceSubscriptions.get(48)) {
            serviceSubscriptions.set(1, false);
            serviceSubscriptions.set(0, false);
        }
        if (serviceSubscriptions.get(1)) {
            serviceSubscriptions.set(0, false);
        }
        if (serviceSubscriptions.get(49)) {
            serviceSubscriptions.set(5, false);
            serviceSubscriptions.set(4, false);
        }
        if (serviceSubscriptions.get(5)) {
            serviceSubscriptions.set(4, false);
        }
    }

    private BitSet getDeviceSubscriptions() throws CargoServiceException {
        int bitsetSize = VersionCheck.isV2DeviceOrGreater(getHardwareVersion()) ? 16 : 7;
        DeviceCommand cmd = new DeviceCommand(BandDeviceConstants.Command.RemoteSubscriptionGetSubscriptions.getCode(), null, null, bitsetSize * 2);
        BandServiceMessage.Response response = processCommand(cmd);
        if (response == BandServiceMessage.Response.SUCCESS) {
            ByteBuffer buffer = ByteBuffer.wrap(cmd.getPayload(), 0, bitsetSize);
            BitSet subscriptions = SubscriptionDataContract.createBitSet(buffer);
            return subscriptions;
        }
        throw new CargoServiceException("Get Device Remote subscriptions failed", response);
    }

    public BandServiceMessage.Response subscribe(BitSet subscriptions) {
        int count;
        BandServiceMessage.Response response = BandServiceMessage.Response.INVALID_ARG_ERROR;
        if (subscriptions != null) {
            if (subscriptions.cardinality() == 0) {
                BandServiceMessage.Response response2 = BandServiceMessage.Response.SUCCESS;
                return response2;
            }
            SubscriptionDataContract.SensorType[] sensorTypes = new SubscriptionDataContract.SensorType[subscriptions.cardinality()];
            SubscriptionDataContract.SensorType[] arr$ = SubscriptionDataContract.SensorType.values();
            int len$ = arr$.length;
            int i$ = 0;
            int count2 = 0;
            while (i$ < len$) {
                SubscriptionDataContract.SensorType sensorType = arr$[i$];
                if (sensorType.getId() > 100 || !subscriptions.get(sensorType.getId())) {
                    count = count2;
                } else {
                    count = count2 + 1;
                    sensorTypes[count2] = sensorType;
                }
                i$++;
                count2 = count;
            }
            BandServiceMessage.Response response3 = subscribe(sensorTypes);
            return response3;
        }
        return response;
    }

    public BandServiceMessage.Response unsubscribe(BitSet subscriptions) {
        int count;
        BandServiceMessage.Response response = BandServiceMessage.Response.INVALID_ARG_ERROR;
        if (subscriptions != null) {
            if (subscriptions.cardinality() == 0) {
                BandServiceMessage.Response response2 = BandServiceMessage.Response.SUCCESS;
                return response2;
            }
            SubscriptionDataContract.SensorType[] sensorTypes = new SubscriptionDataContract.SensorType[subscriptions.cardinality()];
            SubscriptionDataContract.SensorType[] arr$ = SubscriptionDataContract.SensorType.values();
            int len$ = arr$.length;
            int i$ = 0;
            int count2 = 0;
            while (i$ < len$) {
                SubscriptionDataContract.SensorType sensorType = arr$[i$];
                if (sensorType.getId() > 100 || !subscriptions.get(sensorType.getId())) {
                    count = count2;
                } else {
                    count = count2 + 1;
                    sensorTypes[count2] = sensorType;
                }
                i$++;
                count2 = count;
            }
            BandServiceMessage.Response response3 = unsubscribe(sensorTypes);
            return response3;
        }
        return response;
    }

    public BandServiceMessage.Response subscribe(SubscriptionDataContract.SensorType... sensorTypes) {
        BandServiceMessage.Response response = BandServiceMessage.Response.INVALID_ARG_ERROR;
        if (sensorTypes != null) {
            boolean isV2OrGreater = VersionCheck.isV2DeviceOrGreater(getHardwareVersion());
            DeviceCommand cmd = new DeviceCommand(BandDeviceConstants.Command.RemoteSubscriptionSubscribe);
            for (SubscriptionDataContract.SensorType sensorType : sensorTypes) {
                if (sensorType != null && (isV2OrGreater || sensorType.getVersionAdded() < 2)) {
                    KDKLog.i(STREAMING_TAG, "Subscribing sensor type: %s", sensorType);
                    cmd.getArgBuffer().put((byte) sensorType.getId());
                    response = processCommand(cmd);
                    if (response.isError()) {
                        KDKLog.e(STREAMING_TAG, "Subscribing to sensor type %s failed with response %s", sensorType, response);
                    }
                } else {
                    KDKLog.w(STREAMING_TAG, "Subscribing to sensor type %s failed as intended on V1 device", sensorType);
                }
            }
        }
        return response;
    }

    public BandServiceMessage.Response unsubscribe(SubscriptionDataContract.SensorType... sensorTypes) {
        BandServiceMessage.Response response = BandServiceMessage.Response.INVALID_ARG_ERROR;
        if (sensorTypes != null) {
            DeviceCommand cmd = new DeviceCommand(BandDeviceConstants.Command.RemoteSubscriptionUnsubscribe);
            for (SubscriptionDataContract.SensorType sensorType : sensorTypes) {
                if (sensorType != null && sensorType.getId() <= 100) {
                    KDKLog.i(STREAMING_TAG, "Usubscribing sensor type: %s", sensorType);
                    cmd.getArgBuffer().put((byte) sensorType.getId());
                    response = processCommand(cmd);
                    if (response.isError()) {
                        KDKLog.e(STREAMING_TAG, "Unsubscribing to sensor type %s failed with response %s", sensorType, response);
                    }
                }
            }
        }
        return response;
    }

    public boolean hasSubscribersForSensor(SubscriptionDataContract.SensorType sensor) {
        BitSet serviceSubscriptions = SubscriptionDataContract.createBitSet(null);
        synchronized (this.mListeners) {
            Iterator i$ = this.mListeners.iterator();
            while (i$.hasNext()) {
                DeviceConnectionListener listener = i$.next();
                listener.updateSubscriptionsSet(serviceSubscriptions);
            }
        }
        return serviceSubscriptions.get(sensor.getId());
    }

    public void sendBroadcast(PushServicePayload payload) {
        if (payload.getSensorType().getId() == SubscriptionDataContract.SensorType.TileEvent.getId()) {
            broadcastTileEvent(payload);
        } else if (payload.getSensorType().getId() == SubscriptionDataContract.SensorType.SmsDismissed.getId()) {
            broadcastSMSToKapp(CargoConstants.ACTION_PUSH_SMS_DISMISSED, payload);
        } else if (payload.getSensorType().getId() == SubscriptionDataContract.SensorType.CallDismissed.getId()) {
            broadcastSMSToKapp(CargoConstants.ACTION_PUSH_CALL_DISMISSED, payload);
        }
    }

    private void broadcastTileEvent(PushServicePayload payload) {
        ByteBuffer buffer = payload.getDataBuffer();
        while (buffer.remaining() > 0) {
            try {
                CargoTileEvent tileData = new CargoTileEvent(buffer, payload.getTimestamp());
                String appPackage = BandService.getInstance().getSharedPreferences(CargoConstants.HASH_TO_APP_NAME_MAP, 0).getString(Base64.encodeToString(tileData.getHashArray(), 3), null);
                if (appPackage != null) {
                    Intent intent = tileData.createIntentForEvent();
                    intent.setPackage(appPackage);
                    BandService.getInstance().sendBroadcast(intent);
                }
            } catch (BufferUnderflowException e) {
                KDKLog.w(TAG, "Buffer underflow while trying to send tile event. Likely dealing with out of date firmware.");
            }
        }
    }

    private void broadcastSMSToKapp(String intentAction, PushServicePayload payload) {
        ByteBuffer buffer = payload.getDataBuffer();
        while (buffer.remaining() > 0) {
            SubscriptionDataModel dataModel = new TextMessageData(buffer);
            dataModel.setMissedSamples(payload.getMissedSamples());
            Intent intent = new Intent(intentAction);
            intent.setPackage(StrappConstants.NOTIFICATION_SERVICE_MICROSOFT_HEALTH);
            intent.putExtra(InternalBandConstants.EXTRA_SUBSCRIPTION_DATA, dataModel);
            BandService.getInstance().sendBroadcast(intent);
        }
    }

    public void sendSubscription(PushServicePayload payload) {
        if (payload.getSensorType() == SubscriptionDataContract.SensorType.DeviceContact) {
            this.mContactState = DeviceContactData.lookupState(payload.getDataBuffer().get());
        }
        synchronized (this.mListeners) {
            Iterator i$ = this.mListeners.iterator();
            while (i$.hasNext()) {
                DeviceConnectionListener listener = i$.next();
                listener.onPushPacketReceived(payload);
            }
        }
    }

    public BandContactState getCurrentBandContactState() {
        return this.mContactState;
    }

    public void updateBandSubscriptions() {
        mExecutorService.execute(new SubscriptionsUpdater());
    }

    /* loaded from: classes.dex */
    private class SubscriptionsUpdater implements Runnable {
        private SubscriptionsUpdater() {
        }

        @Override // java.lang.Runnable
        public void run() {
            DeviceServiceProvider.this.updateSubscriptions();
        }
    }

    public BandServiceMessage.Response processCommand(@NonNull CommandBase command) {
        DeviceCommand devCommand = new DeviceCommand(command);
        return processCommand(devCommand);
    }

    public BandServiceMessage.Response syncDeviceTime(@NonNull final CargoClientSession clientSession, final int differenceAllowedInMinutes) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.2
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response = BandServiceMessage.Response.DEVICE_COMMAND_ERROR;
                boolean syncTime = true;
                if (differenceAllowedInMinutes != 0) {
                    GetUTCTime cmdGetUTC = new GetUTCTime();
                    response = deviceProvider.processCommand(cmdGetUTC);
                    if (response.isError()) {
                        syncTime = false;
                    } else {
                        long milllisError = cmdGetUTC.getDeviceUTC().toSystemTime() - System.currentTimeMillis();
                        syncTime = (Math.abs(milllisError) / 1000) / 60 > ((long) Math.abs(differenceAllowedInMinutes));
                    }
                }
                if (syncTime) {
                    SetUTCTime cmd = new SetUTCTime();
                    response = deviceProvider.processCommand(cmd);
                }
                Bundle bundle = clientSession.getToken().toBundle();
                bundle.putParcelable(InternalBandConstants.EXTRA_DEVICE_INFO, clientSession.getDeviceInfo());
                clientSession.sendServiceMessage(BandServiceMessage.UPGRADE_NOTIFICATION, BandServiceMessage.Response.SYNC_TIME_COMPLETED, response.getCode(), bundle);
                return response;
            }
        });
    }

    public BandServiceMessage.Response syncDeviceTimeZone(@NonNull final CargoClientSession clientSession) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.3
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider provider) {
                TimeZoneInfo tzPhone = new TimeZoneInfo();
                DeviceCommand cmdSetTimeZone = new DeviceCommand(BandDeviceConstants.Command.CargoSystemSettingsSetTimeZone.getCode(), null, tzPhone.toBytes(), 96);
                BandServiceMessage.Response response = provider.processCommand(cmdSetTimeZone);
                Bundle bundle = clientSession.getToken().toBundle();
                bundle.putParcelable(InternalBandConstants.EXTRA_DEVICE_INFO, provider.getDeviceInfo());
                clientSession.sendServiceMessage(BandServiceMessage.UPGRADE_NOTIFICATION, BandServiceMessage.Response.SYNC_TIMEZONE_COMPLETED, response.getCode(), bundle);
                return response;
            }
        });
    }

    public BandServiceMessage.Response processAsBatch(@NonNull BatchCommand command) {
        BandServiceMessage.Response execute;
        synchronized (this.mSyncLock) {
            execute = command.execute(this);
        }
        return execute;
    }

    public BandServiceMessage.Response processCommand(@NonNull DeviceCommand command) {
        BandServiceMessage.Response response;
        BandServiceMessage.Response response2 = BandServiceMessage.Response.DEVICE_NOT_BONDED_ERROR;
        if (this.mProtocolConnection.isUpdatingFirmware()) {
            response = BandServiceMessage.Response.DEVICE_STATE_ERROR;
        } else {
            synchronized (this.mSyncLock) {
                response = this.mProtocolConnection.processCommand(command);
                if (this.mProtocolConnection.isConnected()) {
                    try {
                        getPushConnection().connect();
                    } catch (IOException e) {
                        KDKLog.w(STREAMING_TAG, "Failed to establish push connecton.", e);
                    }
                }
            }
        }
        if (!response.isError() || command.hasResponse() || command.getQueueLimit() <= 0) {
            return response;
        }
        if (BandServiceMessage.Response.DEVICE_NOT_BONDED_ERROR == response || BandServiceMessage.Response.DEVICE_STATE_ERROR == response || BandServiceMessage.Response.DEVICE_IO_ERROR == response || BandServiceMessage.Response.DEVICE_TIMEOUT_ERROR == response || BandServiceMessage.Response.DEVICE_NOT_CONNECTED_ERROR == response) {
            CargoDeviceManager.getInstance().enqueueCommand(getDeviceInfo(), command);
            BandServiceMessage.Response response3 = BandServiceMessage.Response.ENQUEUED;
            return response3;
        }
        return response;
    }

    public DeviceProfileInfo getDeviceProfileInfo() throws CargoServiceException {
        DeviceProfileGet cmdDP = new DeviceProfileGet(getHardwareVersion());
        BandServiceMessage.Response deviceProfileGetResponse = processCommand(cmdDP);
        if (!deviceProfileGetResponse.isError()) {
            return cmdDP.getDeviceProfileInfo();
        }
        throw new CargoServiceException("DeviceProfileInfo could not be retrieved", deviceProfileGetResponse);
    }

    public BandServiceMessage.Response upgradeFirmware(CargoClientSession clientSession, CargoFirmwareUpdateInfo firmwwareUpdateInfo) {
        BandServiceMessage.Response response;
        BandServiceMessage.Response response2 = BandServiceMessage.Response.SUCCESS;
        CargoProtocolConnection deviceConnection = getProtocolConnection();
        if (firmwwareUpdateInfo == null) {
            BandServiceMessage.Response response3 = BandServiceMessage.Response.INVALID_ARG_ERROR;
            return response3;
        } else if (!firmwwareUpdateInfo.isFirmwareUpdateAvailable()) {
            BandServiceMessage.Response response4 = BandServiceMessage.Response.SERVICE_CLOUD_DATA_NOT_AVAILABLE_ERROR;
            return response4;
        } else {
            File firmwareFile = getDownloadedFirmwareFile(firmwwareUpdateInfo);
            if (firmwareFile == null) {
                BandServiceMessage.Response response5 = BandServiceMessage.Response.SERVICE_CLOUD_DOWNLOAD_REQUIRED_ERROR;
                return response5;
            } else if (DeviceConstants.AppRunning.APP_RUNNING_APP == deviceConnection.getRunningApplication() && !deviceConnection.isUpdatingFirmware()) {
                Bundle bundle = clientSession.getToken().toBundle();
                bundle.putParcelable(InternalBandConstants.EXTRA_DEVICE_INFO, clientSession.getDeviceInfo());
                try {
                    clientSession.sendServiceMessage(BandServiceMessage.FIRMWARE_UPGRADE_PROGRESS, BandServiceMessage.Response.UPGRADE_FIRMWARE_LOADING_FIRMWARE, 8, bundle);
                    byte[] firmwareData = FileHelper.readDataFromFile(firmwareFile);
                    clientSession.sendServiceMessage(BandServiceMessage.FIRMWARE_UPGRADE_PROGRESS, BandServiceMessage.Response.UPGRADE_FIRMWARE_ENTERING_UPGRADE_MODE, 10, bundle);
                    BandServiceMessage.Response response6 = deviceConnection.bootIntoFirmwareUpdateMode();
                    if (!response6.isError()) {
                        if (DeviceConstants.AppRunning.APP_RUNNING_UPAPP == deviceConnection.getRunningApplication() && deviceConnection.isConnected()) {
                            KDKLog.w(TAG, "!!! ENTERED FIRMWARE UPDATE MODE !!!");
                            KDKLog.i(TAG, "Writing firmware version %s (%d bytes) to device", firmwwareUpdateInfo.getUniqueVersion(), Integer.valueOf(firmwareData.length));
                            clientSession.sendServiceMessage(BandServiceMessage.FIRMWARE_UPGRADE_PROGRESS, BandServiceMessage.Response.UPGRADE_FIRMWARE_INSTALLING_FIRMWARE, 15, bundle);
                            DeviceCommand cmdUpdate = new DeviceCommand(BandDeviceConstants.Command.CargoSRAMFWUpdateLoadData, null, firmwareData);
                            response = deviceConnection.performIO(cmdUpdate);
                            if (!response.isError()) {
                                clientSession.sendServiceMessage(BandServiceMessage.FIRMWARE_UPGRADE_PROGRESS, BandServiceMessage.Response.UPGRADE_FIRMWARE_FINALIZING_UPGRADE, 25, bundle);
                                response = deviceConnection.waitForFirmwareUpdateToComplete();
                            }
                        } else {
                            KDKLog.e(TAG, "Firmware upgrade aborted, device is not in the proper state.");
                            response = BandServiceMessage.Response.DEVICE_STATE_ERROR;
                        }
                        if (DeviceConstants.AppRunning.APP_RUNNING_APP == deviceConnection.getRunningApplication()) {
                            if (!isFirmwareUpgraded(firmwwareUpdateInfo)) {
                                KDKLog.e(TAG, "Firmware Failed to upgrade to %s", firmwwareUpdateInfo.getFirmwareVersion());
                                BandServiceMessage.Response response7 = BandServiceMessage.Response.DEVICE_FIRMWARE_UPGRADE_VERSION_FAILED_ERROR;
                                return response7;
                            }
                            return response;
                        }
                        deviceConnection.resetDevice();
                        return response;
                    }
                    KDKLog.e(TAG, "Failed to put device into firmware update mode, response code: %s.", response6);
                    return response6;
                } catch (CargoServiceException e) {
                    KDKLog.e(TAG, e, "firmware upgrade error: %s", e.getMessage());
                    BandServiceMessage.Response response8 = BandServiceMessage.Response.SERVICE_FILE_IO_ERROR;
                    return response8;
                }
            } else {
                BandServiceMessage.Response response9 = BandServiceMessage.Response.INVALID_OPERATION_ERROR;
                return response9;
            }
        }
    }

    private boolean isFirmwareUpgraded(CargoFirmwareUpdateInfo firmwareUpgradeInfo) {
        GetVersionValidation cmd = new GetVersionValidation();
        BandServiceMessage.Response response = processCommand(cmd);
        if (response != BandServiceMessage.Response.SUCCESS) {
            KDKLog.e(TAG, "Device firmware version cannot be retrieved from device: %s.", response.toString());
            return false;
        } else if (!cmd.isResourcesValid()) {
            KDKLog.e(TAG, "Device firmware version AssetValid is false.");
            return false;
        } else {
            FWVersion[] fwVersions = cmd.getFWVersion();
            int appIndex = DeviceConstants.AppRunning.APP_RUNNING_APP.getFirmwareVersionIndex();
            if (fwVersions == null || fwVersions.length <= appIndex) {
                KDKLog.e(TAG, "isFirmwareUpgraded Device firmware version does not contain App Version");
                return false;
            } else if (fwVersions[appIndex].isEqual(firmwareUpgradeInfo.getFirmwareVersion())) {
                return true;
            } else {
                KDKLog.e(TAG, "Device firmware version %s failed to be updated %s.", fwVersions[appIndex].getCurrentVersion(), firmwareUpgradeInfo.getFirmwareVersion());
                return false;
            }
        }
    }

    private File getDownloadedFirmwareFile(CargoFirmwareUpdateInfo firmwareUpgradeInfo) {
        File firmwareFile = null;
        if (firmwareUpgradeInfo == null || StringUtil.isNullOrEmpty(firmwareUpgradeInfo.getUniqueVersion())) {
            return null;
        }
        try {
            firmwareFile = FileHelper.makeFirmwareFile(CargoClientSession.getDirectoryPath(), firmwareUpgradeInfo.getUniqueVersion(), false);
        } catch (CargoServiceException e) {
            KDKLog.e(TAG, e, "download firmware error: %s", e.getMessage());
        }
        if (firmwareFile == null || !firmwareFile.exists()) {
            KDKLog.e(TAG, "Firmware version %s has not been downloaded.", firmwareUpgradeInfo.getUniqueVersion());
            return null;
        }
        return firmwareFile;
    }

    public BandServiceMessage.Response upgradeEphemerisWithLogic(@NonNull CargoClientSession clientSession, EphemerisUpdateInfo ephemerisUpdateInfo) {
        BandServiceMessage.Response response = BandServiceMessage.Response.OPERATION_NOT_REQUIRED;
        try {
            long lastUpgradeTime = clientSession.getSharedPreferences().getLong(CargoConstants.last_ephemeris_upgrade_time, 0L);
            long lastDownloadTime = clientSession.getSharedPreferences().getLong(CargoConstants.last_ephemeris_download_time, 0L);
            if (lastUpgradeTime < lastDownloadTime) {
                if (ephemerisUpdateInfo == null) {
                    ephemerisUpdateInfo = EphemerisUpdateInfo.fromSharedPreferences(clientSession.getSharedPreferences());
                }
                BandServiceMessage.Response response2 = upgradeEphemerisWithoutLogic(clientSession, ephemerisUpdateInfo);
                return response2;
            }
            return response;
        } catch (CargoException e1) {
            BandServiceMessage.Response response3 = BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR;
            KDKLog.e(TAG, e1, "Ephemeris Upgrade CargoException: %s", e1.getMessage());
            return response3;
        }
    }

    public BandServiceMessage.Response upgradeEphemerisWithoutLogic(@NonNull final CargoClientSession clientSession, final EphemerisUpdateInfo ephemerisUpdateInfo) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.4
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response;
                Bundle bundle = clientSession.getToken().toBundle();
                bundle.putParcelable(InternalBandConstants.EXTRA_DEVICE_INFO, clientSession.getDeviceInfo());
                bundle.putParcelable(CargoConstants.EXTRA_CLOUD_DATA, ephemerisUpdateInfo);
                clientSession.sendServiceMessage(BandServiceMessage.UPGRADE_NOTIFICATION, BandServiceMessage.Response.UPGRADE_EPHEMERIS_STARTED, 0, bundle);
                File ephemerisFile = DeviceServiceProvider.this.getDownloadedEphemerisFile();
                if (ephemerisFile == null) {
                    response = BandServiceMessage.Response.SERVICE_CLOUD_DOWNLOAD_REQUIRED_ERROR;
                } else {
                    try {
                        byte[] data = FileHelper.readDataFromFile(ephemerisFile);
                        KDKLog.i(DeviceServiceProvider.TAG, "Writing ephemeris file (%d bytes) to device", Integer.valueOf(data.length));
                        FileWrite ephemerisFileCommand = new FileWrite(CargoFileName.EPHEMERIS, data);
                        response = DeviceServiceProvider.this.processCommand(ephemerisFileCommand);
                    } catch (CargoServiceException e) {
                        KDKLog.e(DeviceServiceProvider.TAG, e, "upgrade ephemeris exception: %s", e.getMessage());
                        response = BandServiceMessage.Response.SERVICE_FILE_IO_ERROR;
                    }
                }
                if (!response.isError()) {
                    clientSession.getSharedPreferences().edit().putLong(CargoConstants.last_ephemeris_upgrade_time, System.currentTimeMillis()).commit();
                }
                clientSession.sendServiceMessage(BandServiceMessage.UPGRADE_NOTIFICATION, BandServiceMessage.Response.UPGRADE_EPHEMERIS_COMPLETED, response.getCode(), bundle);
                return response;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File getDownloadedEphemerisFile() {
        File ephemerisFile = null;
        try {
            ephemerisFile = FileHelper.makeFile(CargoClientSession.getDirectoryPath(), FileHelper.EPHEMERIS_DIR, FileHelper.ACTUAL_DATA, false);
        } catch (CargoServiceException e) {
            KDKLog.e(TAG, e, "download ephemeris exception: %s", e.getMessage());
        }
        if (ephemerisFile == null || !ephemerisFile.exists()) {
            KDKLog.w(TAG, "Ephemeris file could not be found");
            return null;
        }
        return ephemerisFile;
    }

    public BandServiceMessage.Response upgradeTimeZoneSettingsWithLogic(CargoClientSession clientSession, TimeZoneSettingsUpdateInfo timeZoneSettingUpdateInfo) {
        BandServiceMessage.Response response = BandServiceMessage.Response.OPERATION_NOT_REQUIRED;
        if (clientSession == null || clientSession.isTerminating()) {
            BandServiceMessage.Response response2 = BandServiceMessage.Response.SERVICE_TERMINATED_ERROR;
            return response2;
        }
        try {
            long lastUpgradeTime = clientSession.getSharedPreferences().getLong(CargoConstants.last_timezone_upgrade_time, 0L);
            long lastDownloadTime = clientSession.getSharedPreferences().getLong(CargoConstants.last_timezone_download_time, 0L);
            if (lastUpgradeTime < lastDownloadTime) {
                if (timeZoneSettingUpdateInfo == null) {
                    timeZoneSettingUpdateInfo = TimeZoneSettingsUpdateInfo.fromSharedPreferences(clientSession.getSharedPreferences());
                }
                BandServiceMessage.Response response3 = upgradeTimeZoneSettingsWithoutLogic(clientSession, timeZoneSettingUpdateInfo);
                return response3;
            }
            return response;
        } catch (CargoException e1) {
            BandServiceMessage.Response response4 = BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR;
            KDKLog.e(TAG, e1, "Timezone Upgrade CargoException: %s", e1.getMessage());
            return response4;
        }
    }

    public BandServiceMessage.Response upgradeTimeZoneSettingsWithoutLogic(@NonNull final CargoClientSession clientSession, final TimeZoneSettingsUpdateInfo timeZoneSettingUpdateInfo) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.5
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response;
                Bundle bundle = clientSession.getToken().toBundle();
                bundle.putParcelable(InternalBandConstants.EXTRA_DEVICE_INFO, clientSession.getDeviceInfo());
                bundle.putParcelable(CargoConstants.EXTRA_CLOUD_DATA, timeZoneSettingUpdateInfo);
                clientSession.sendServiceMessage(BandServiceMessage.UPGRADE_NOTIFICATION, BandServiceMessage.Response.UPGRADE_TIMEZONE_SETTINGS_STARTED, 0, bundle);
                File tzFile = DeviceServiceProvider.this.getDownloadedTimezoneFile();
                if (tzFile == null) {
                    response = BandServiceMessage.Response.SERVICE_CLOUD_DOWNLOAD_REQUIRED_ERROR;
                } else {
                    try {
                        byte[] data = FileHelper.readDataFromFile(tzFile);
                        KDKLog.i(DeviceServiceProvider.TAG, "Writing timezone file (%d bytes) to device", Integer.valueOf(data.length));
                        DeviceCommand updateTzFileCmd = new DeviceCommand(BandDeviceConstants.Command.CargoTimeUpdateTimezoneFile, null, data);
                        response = DeviceServiceProvider.this.processCommand(updateTzFileCmd);
                        if (BandServiceMessage.Response.DEVICE_FIRMWARE_VERSION_INCOMPATIBLE_ERROR == response) {
                            KDKLog.d(DeviceServiceProvider.TAG, "Using fallback FileWrite command to update Timezone Settings file on device.");
                            FileWrite tzFileCommand = new FileWrite(CargoFileName.TIME_ZONE, data);
                            response = DeviceServiceProvider.this.processCommand(tzFileCommand);
                        }
                    } catch (CargoServiceException e) {
                        KDKLog.e(DeviceServiceProvider.TAG, e, "upgrade timezone file error: %s", e.getMessage());
                        response = BandServiceMessage.Response.SERVICE_FILE_IO_ERROR;
                    }
                }
                if (!response.isError()) {
                    clientSession.getSharedPreferences().edit().putLong(CargoConstants.last_timezone_upgrade_time, System.currentTimeMillis()).commit();
                }
                clientSession.sendServiceMessage(BandServiceMessage.UPGRADE_NOTIFICATION, BandServiceMessage.Response.UPGRADE_TIMEZONE_SETTINGS_COMPLETED, response.getCode(), bundle);
                return response;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File getDownloadedTimezoneFile() {
        File tzFile = null;
        try {
            tzFile = FileHelper.makeFile(CargoClientSession.getDirectoryPath(), FileHelper.TIMEZONE_DIR, FileHelper.ACTUAL_DATA, false);
        } catch (CargoServiceException e) {
            KDKLog.e(TAG, e.getMessage());
        }
        if (tzFile == null || !tzFile.exists()) {
            KDKLog.w(TAG, "TimeZone file could not be found");
            return null;
        }
        return tzFile;
    }

    public BandServiceMessage.Response getCrashDumpFilesFromDevice() {
        return getFilesFromDevice(CargoConstants.CrashDumps, CargoFileName.CRASHDUMP);
    }

    public BandServiceMessage.Response getTelemetryFilesFromDevice(@NonNull CargoClientSession clientSession, boolean crashdumpFilesRetrieved) {
        BandServiceMessage.Response response = BandServiceMessage.Response.OPERATION_NOT_REQUIRED;
        long lastTelemetryRetrievedTime = clientSession.getSharedPreferences().getLong(CargoConstants.last_telemetry_file_retrieved_attempt_time, 0L);
        if (crashdumpFilesRetrieved || lastTelemetryRetrievedTime < System.currentTimeMillis() - 604800000) {
            response = getFilesFromDevice(CargoConstants.Instrumentation, CargoFileName.INSTRUMENTATION);
            if (!response.isError()) {
                clientSession.getSharedPreferences().edit().putLong(CargoConstants.last_telemetry_file_retrieved_attempt_time, System.currentTimeMillis()).commit();
            }
        }
        return response;
    }

    public BandServiceMessage.Response getFilesFromDevice(final String name, @NonNull final CargoFileName cargoFileName) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.6
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider provider) {
                FileGetSize fileGetSize = new FileGetSize(cargoFileName);
                DeviceCommand devCommand = new DeviceCommand(fileGetSize);
                BandServiceMessage.Response response = provider.processCommand(devCommand);
                if (!response.isError()) {
                    int size = fileGetSize.getFileSize();
                    if (size > 0) {
                        FileRead fileRead = new FileRead(cargoFileName, size);
                        DeviceCommand devCommand2 = new DeviceCommand(fileRead);
                        BandServiceMessage.Response response2 = provider.processCommand(devCommand2);
                        if (!response2.isError()) {
                            byte[] data = fileRead.getFileBuff();
                            SimpleDateFormat sdf = new SimpleDateFormat(CloudDataResource.CLOUD_UPLOADID_FORMAT, Locale.US);
                            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                            String fileName = name + "-" + sdf.format(Long.valueOf(System.currentTimeMillis())) + ".dat";
                            File f = new File(CargoClientSession.getDirectoryPath() + File.separator + fileName);
                            try {
                                FileHelper.writeDataToFile(data, f);
                                KDKLog.d(DeviceServiceProvider.TAG, "Wrote file \"%s\"", fileName);
                                return response2;
                            } catch (CargoServiceException e) {
                                KDKLog.e(DeviceServiceProvider.TAG, e.getMessage(), e);
                                return e.getResponse();
                            }
                        }
                        return response2;
                    }
                    return response;
                } else if (response == BandServiceMessage.Response.DEVICE_COMMAND_RESPONSE_ERROR) {
                    return BandServiceMessage.Response.FILE_NOT_ON_DEVICE;
                } else {
                    return response;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @NonNull
    public BluetoothDeviceConnection.BluetoothDeviceConnectionListener getBluetoothDevicePushConnectionListener() {
        return (BluetoothDeviceConnection.BluetoothDeviceConnectionListener) this.mPushConnection;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @NonNull
    public BluetoothDeviceConnection.BluetoothDeviceConnectionListener getBluetoothDeviceProtocolConnectionListener() {
        return (BluetoothDeviceConnection.BluetoothDeviceConnectionListener) this.mProtocolConnection;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendDeviceStatusNotification(boolean isTimeoutError) {
        if (isDeviceConnected()) {
            DeviceInfo deviceInfo = getDeviceInfo();
            deviceInfo.setDeviceUUID(getDeviceUUID());
            deviceInfo.setFWVersion(getFWVersion());
            deviceInfo.setLogVersion(getLogVersion());
            deviceInfo.setSerialNumber(getSerialNumber());
            deviceInfo.setHardwareVersion(getHardwareVersion());
            CargoDeviceManager.getInstance().dispatchQueuedCommands(getDeviceInfo());
        }
        synchronized (this.mListeners) {
            DeviceConnectionListener[] arr$ = (DeviceConnectionListener[]) this.mListeners.toArray(new DeviceConnectionListener[this.mListeners.size()]);
            for (DeviceConnectionListener listener : arr$) {
                onDeviceConnectionStatusChanged(listener, isTimeoutError);
            }
        }
    }

    private void onDeviceConnectionStatusChanged(DeviceConnectionListener listener, boolean isTimeoutError) {
        if (listener != null) {
            try {
                if (isDeviceConnected()) {
                    listener.onDeviceConnected(this);
                } else if (isTimeoutError) {
                    listener.onDeviceTimeout(this);
                } else {
                    listener.onDeviceDisconnected(this);
                }
            } catch (Exception e) {
                KDKLog.w(TAG, e, "onDeviceConnectionStatusChanged caught exception: %s", e.getMessage());
            }
        }
    }

    public BandServiceMessage.Response turnTelemetryOnOff(boolean f) {
        if (f) {
            try {
                LoggerFactory.initLogger(getContext());
            } catch (CargoServiceException e) {
                KDKLog.w(TAG, e, "turnTelemetryOnOff caught exception: %s", e.getMessage());
                return BandServiceMessage.Response.OPERATION_EXCEPTION_ERROR;
            }
        }
        LoggerFactory.setTelemetryEnabled(f);
        return BandServiceMessage.Response.SUCCESS;
    }

    public BandServiceMessage.Response turnPerformanceLoggingOnOff(boolean f) {
        if (f) {
            try {
                LoggerFactory.initLogger(getContext());
            } catch (CargoServiceException e) {
                KDKLog.w(TAG, e, "turnPerformanceLoggingOnOff caught exception: %s", e.getMessage());
                return BandServiceMessage.Response.OPERATION_EXCEPTION_ERROR;
            }
        }
        LoggerFactory.setPerformanceEnabled(f);
        return BandServiceMessage.Response.SUCCESS;
    }

    public BandServiceMessage.Response turnDiagnosticsLoggingOnOff(boolean f) {
        if (f) {
            try {
                LoggerFactory.initLogger(getContext());
            } catch (CargoServiceException e) {
                KDKLog.w(TAG, e, "turnDiagnosticsLoggingOnOff caught exception: %s", e.getMessage());
                return BandServiceMessage.Response.OPERATION_EXCEPTION_ERROR;
            }
        }
        LoggerFactory.setDiagnosticsEnabled(f);
        return BandServiceMessage.Response.SUCCESS;
    }

    public boolean isOobeCompleted() {
        DeviceCommand cmd = new DeviceCommand(BandDeviceConstants.Command.CargoSystemSettingsOOBEGet);
        BandServiceMessage.Response response = processCommand(cmd);
        if (!response.isError()) {
            ByteBuffer buffer = ByteBuffer.wrap(cmd.getPayload()).order(ByteOrder.LITTLE_ENDIAN);
            return buffer.getInt() != 0;
        }
        KDKLog.e(TAG, "Error in get OOBE Complete flag: %s", response);
        return true;
    }

    public BandServiceMessage.Response getSensorlogChunkRangeMetadata(@NonNull SensorLogMetadata meta, int count) {
        ByteBuffer argBuf = BufferUtil.allocateLittleEndian(4).putInt(count);
        DeviceCommand cmd = new DeviceCommand(BandDeviceConstants.Command.LoggerGetChunkRangeMetadata, argBuf.array(), null);
        BandServiceMessage.Response response = processCommand(cmd);
        if (response == BandServiceMessage.Response.SUCCESS) {
            meta.initializeMetadata(ByteBuffer.wrap(cmd.getPayload()).order(ByteOrder.LITTLE_ENDIAN));
        } else {
            KDKLog.e(TAG, "Error in getSensorlogChunkRangeMetadata: %s", response);
        }
        return response;
    }

    public BandServiceMessage.Response deleteSensorlogChunkRangeData(@NonNull SensorLogMetadata meta) {
        ByteBuffer metaBuf = BufferUtil.allocateLittleEndian(12).put(meta.toBytes());
        DeviceCommand cmd = new DeviceCommand(BandDeviceConstants.Command.LoggerDeleteChunkRange, null, metaBuf.array());
        BandServiceMessage.Response response = processCommand(cmd);
        return response;
    }

    public BandServiceMessage.Response getSensorlogChunkRangeData(@NonNull SensorLogDownload log, int maxRange) {
        SensorLogMetadata meta = new SensorLogMetadata();
        BandServiceMessage.Response response = getSensorlogChunkRangeMetadata(meta, maxRange);
        if (response == BandServiceMessage.Response.SUCCESS) {
            ByteBuffer argBuf = BufferUtil.allocateLittleEndian(12).put(meta.toBytes());
            CommandRead cmd = new CommandRead(BandDeviceConstants.Command.LoggerGetChunkRangeData, argBuf.array(), (int) meta.getByteCount());
            response = processCommand(cmd);
            if (response == BandServiceMessage.Response.SUCCESS) {
                log.initializeSensorLogDownload(cmd.getResultByte(), meta);
            }
        }
        return response;
    }

    public BandServiceMessage.Response flushLogger() {
        FlushLoggerCommand cmd = new FlushLoggerCommand();
        return processCommand(cmd);
    }

    public BandServiceMessage.Response getFirmwareAppVersion(Bundle responseBundle, VersionType type, int clientVersion) {
        GetVersion cmd = new GetVersion();
        BandServiceMessage.Response response = processCommand(cmd);
        int appIndex = DeviceConstants.AppRunning.APP_RUNNING_APP.getFirmwareVersionIndex();
        if (response == BandServiceMessage.Response.SUCCESS) {
            FWVersion[] versions = cmd.getFWVersion();
            if (versions == null || versions.length <= appIndex) {
                KDKLog.e(TAG, "getFirmwareAppVersion Device firmware version does not contain App Version");
                return BandServiceMessage.Response.DEVICE_DATA_ERROR;
            }
            switch (type) {
                case FIRMWARE:
                    responseBundle.putString(InternalBandConstants.EXTRA_FIRMWARE_VERSION_APP, versions[appIndex].getCurrentVersion());
                    return response;
                case HARDWARE:
                    if (clientVersion >= 16842752) {
                        responseBundle.putShort(InternalBandConstants.EXTRA_HARDWARE_VERSION, versions[appIndex].getPcbId());
                        return response;
                    }
                    responseBundle.putString(InternalBandConstants.EXTRA_HARDWARE_VERSION, Byte.toString(versions[appIndex].getPcbId()));
                    return response;
                default:
                    responseBundle.putString(InternalBandConstants.EXTRA_FIRMWARE_VERSION_APP, versions[appIndex].getCurrentVersion());
                    responseBundle.putString(InternalBandConstants.EXTRA_HARDWARE_VERSION, Byte.toString(versions[appIndex].getPcbId()));
                    return response;
            }
        }
        return response;
    }

    public BandServiceMessage.Response getTiles(final Bundle responseBundle, final byte[] appId) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.7
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response = DeviceServiceProvider.this.getTheme(responseBundle);
                if (BandServiceMessage.Response.SUCCESS != response) {
                    return response;
                }
                BandTheme defaultTheme = (BandTheme) responseBundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
                BandServiceMessage.Response response2 = DeviceServiceProvider.this.getInstalledAppListWithImage(responseBundle);
                if (response2 != BandServiceMessage.Response.SUCCESS) {
                    return response2;
                }
                StartStrip strappList = (StartStrip) responseBundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
                List<CargoStrapp> validatedTiles = new ArrayList<>();
                for (CargoStrapp tile : strappList.getAppList()) {
                    BandServiceMessage.Response response3 = DeviceServiceProvider.this.validateTile(tile, appId);
                    switch (response3) {
                        case SUCCESS:
                            validatedTiles.add(tile);
                            break;
                        case TILE_SECURITY_ERROR:
                            break;
                        default:
                            return response3;
                    }
                }
                BandServiceMessage.Response response4 = BandServiceMessage.Response.SUCCESS;
                ArrayList<BandTile> tileList = new ArrayList<>();
                for (CargoStrapp strapp : validatedTiles) {
                    response4 = DeviceServiceProvider.this.getLayouts(strapp.getId(), responseBundle);
                    if (response4 != BandServiceMessage.Response.SUCCESS) {
                        return response4;
                    }
                    List<PageLayout> tilePages = new ArrayList<>();
                    Map<Integer, StrappLayout> layoutMap = (Map) responseBundle.getSerializable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
                    for (int i = 0; i < 5; i++) {
                        LayoutTemplate template = new LayoutTemplate(layoutMap.get(Integer.valueOf(i)).getLayoutBlob());
                        PageLayout layoutPage = template.toPageLayout();
                        if (layoutPage == null) {
                            break;
                        }
                        tilePages.add(layoutPage);
                    }
                    tileList.add(TileUtils.strappToTile(strapp, defaultTheme, tilePages));
                }
                responseBundle.putParcelableArrayList(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, tileList);
                return response4;
            }
        });
    }

    public BandServiceMessage.Response getRemainingTileCapacity(@NonNull final Bundle responseBundle) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.8
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                CommandRead installedAppListCmd = new CommandRead(BandDeviceConstants.Command.CargoInstalledAppListGetNoImages, null, 1324);
                BandServiceMessage.Response response = deviceProvider.processCommand(installedAppListCmd);
                if (BandServiceMessage.Response.SUCCESS != response) {
                    return response;
                }
                ByteBuffer buffer = ByteBuffer.wrap(installedAppListCmd.getResultByte()).order(ByteOrder.LITTLE_ENDIAN);
                int installedAppCount = (int) BitHelper.unsignedIntegerToLong(buffer.getInt());
                BandServiceMessage.Response response2 = DeviceServiceProvider.this.getMaxStrappCount(responseBundle);
                if (BandServiceMessage.Response.SUCCESS != response2) {
                    return response2;
                }
                int maxallowedTiles = responseBundle.getInt(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
                int result = maxallowedTiles - installedAppCount;
                responseBundle.putInt(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, result);
                return response2;
            }
        });
    }

    public BandServiceMessage.Response addTile(final CargoStrapp strapp, final BandTheme theme, final Bundle responseBundle) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.9
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response;
                BandServiceMessage.Response response2;
                BandServiceMessage.Response response3;
                responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, false);
                try {
                    response2 = DeviceServiceProvider.this.getInstalledAppListWithoutImage(responseBundle);
                } catch (CargoException e) {
                    response = e.getResponse();
                    KDKLog.e(DeviceServiceProvider.TAG, e, "Add tile CargoException: %s", e.getMessage());
                }
                if (response2 != BandServiceMessage.Response.SUCCESS) {
                    return response2;
                }
                StartStrip appList = (StartStrip) responseBundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
                appList.add(strapp);
                BandServiceMessage.Response response4 = DeviceServiceProvider.this.startUISync();
                if (BandServiceMessage.Response.SUCCESS != response4) {
                    return response4;
                }
                BandServiceMessage.Response response5 = DeviceServiceProvider.this.registerApp(strapp);
                if (BandServiceMessage.Response.SUCCESS != response5) {
                    return response5;
                }
                BandServiceMessage.Response response6 = DeviceServiceProvider.this.setTileImageIndex(strapp.getId(), strapp.getTileImageIndex());
                if (BandServiceMessage.Response.SUCCESS != response6) {
                    return response6;
                }
                BandServiceMessage.Response response7 = DeviceServiceProvider.this.setTileBadgeIndex(strapp.getId(), strapp.getBadgeImageIndex());
                if (BandServiceMessage.Response.SUCCESS != response7) {
                    return response7;
                }
                BandServiceMessage.Response response8 = DeviceServiceProvider.this.setTileNotificationIndex(strapp.getId(), strapp.getNotificationImageIndex());
                if (BandServiceMessage.Response.SUCCESS != response8) {
                    return response8;
                }
                BandServiceMessage.Response response9 = DeviceServiceProvider.this.setInstalledAppList(appList);
                if (BandServiceMessage.Response.SUCCESS != response9) {
                    return response9;
                }
                if (theme == null || BandServiceMessage.Response.SUCCESS == (response3 = DeviceServiceProvider.this.setCustomTileThemeHelper(strapp.getId(), new StrappColorPalette(theme)))) {
                    BandServiceMessage.Response response10 = DeviceServiceProvider.this.setLayout(strapp);
                    if (response10 != BandServiceMessage.Response.SUCCESS) {
                        return response10;
                    }
                    response = DeviceServiceProvider.this.endUISync();
                    if (response != BandServiceMessage.Response.SUCCESS) {
                        return response;
                    }
                    responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, true);
                    return response;
                }
                return response3;
            }
        });
    }

    public BandServiceMessage.Response removeTile(final UUID tileId, final Bundle responseBundle, final byte[] appId) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.10
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response = BandServiceMessage.Response.SUCCESS;
                BandServiceMessage.Response response2 = DeviceServiceProvider.this.validateTile(tileId, appId);
                if (response2 != BandServiceMessage.Response.SUCCESS) {
                    return response2;
                }
                BandServiceMessage.Response response3 = DeviceServiceProvider.this.getInstalledAppListWithoutImage(responseBundle);
                if (response3 != BandServiceMessage.Response.SUCCESS) {
                    return response3;
                }
                StartStrip appList = (StartStrip) responseBundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
                if (appList.remove(tileId)) {
                    BandServiceMessage.Response response4 = DeviceServiceProvider.this.startUISync();
                    if (BandServiceMessage.Response.SUCCESS != response4) {
                        return response4;
                    }
                    BandServiceMessage.Response response5 = DeviceServiceProvider.this.removeApp(tileId);
                    if (BandServiceMessage.Response.SUCCESS != response5) {
                        return response5;
                    }
                    BandServiceMessage.Response response6 = DeviceServiceProvider.this.setInstalledAppList(appList);
                    if (BandServiceMessage.Response.SUCCESS != response6) {
                        return response6;
                    }
                    BandServiceMessage.Response response7 = DeviceServiceProvider.this.endUISync();
                    responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, true);
                    return response7;
                }
                responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, false);
                return BandServiceMessage.Response.TILE_NOT_FOUND_ERROR;
            }
        });
    }

    public BandServiceMessage.Response removeTiles(final List<UUID> tileIds) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.11
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response;
                BandServiceMessage.Response response2;
                BandServiceMessage.Response response3 = BandServiceMessage.Response.SUCCESS;
                Bundle bundle = new Bundle();
                BandServiceMessage.Response response4 = DeviceServiceProvider.this.getInstalledAppListWithoutImage(bundle);
                if (response4 != BandServiceMessage.Response.SUCCESS) {
                    return response4;
                }
                StartStrip appList = (StartStrip) bundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
                int tileNumber = appList.getCount();
                BandServiceMessage.Response response5 = DeviceServiceProvider.this.startUISync();
                if (BandServiceMessage.Response.SUCCESS != response5) {
                    return response5;
                }
                for (UUID tileId : tileIds) {
                    if (appList.remove(tileId) && BandServiceMessage.Response.SUCCESS != (response2 = DeviceServiceProvider.this.removeApp(tileId))) {
                        return response2;
                    }
                }
                return (tileNumber == appList.getCount() || BandServiceMessage.Response.SUCCESS == (response = DeviceServiceProvider.this.setInstalledAppList(appList))) ? DeviceServiceProvider.this.endUISync() : response;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response validateTile(UUID id, byte[] hashedAppId) {
        BandServiceMessage.Response response = BandServiceMessage.Response.SUCCESS;
        Bundle bundle = new Bundle();
        BandServiceMessage.Response response2 = getStrappWithoutImage(id, bundle);
        return BandServiceMessage.Response.SUCCESS != response2 ? response2 : validateTile((CargoStrapp) bundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD), hashedAppId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response validateTile(CargoStrapp tile, byte[] hashedAppId) {
        BandServiceMessage.Response response = BandServiceMessage.Response.SUCCESS;
        if (!Arrays.equals(hashedAppId, tile.getHashedAppId())) {
            BandServiceMessage.Response response2 = BandServiceMessage.Response.TILE_SECURITY_ERROR;
            return response2;
        }
        return response;
    }

    public BandServiceMessage.Response getMaxStrappCount(Bundle responseBundle) {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoInstalledAppListGetMaxCount, null, 4);
        BandServiceMessage.Response response = processCommand(cmd);
        if (!response.isError()) {
            ByteBuffer buffer = ByteBuffer.wrap(cmd.getResultByte()).order(ByteOrder.LITTLE_ENDIAN);
            responseBundle.putInt(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, (int) BitHelper.unsignedIntegerToLong(buffer.getInt()));
        }
        return response;
    }

    public BandServiceMessage.Response getInstalledAppListWithImage(Bundle responseBundle) {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoInstalledAppListGet, null, StrappListFromDevice.STRAPP_LIST_STRUCTURE_SIZE);
        BandServiceMessage.Response response = processCommand(cmd);
        if (!response.isError()) {
            StrappListFromDevice list = new StrappListFromDevice(cmd.getResultByte(), true);
            try {
                responseBundle.putParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, new StartStrip(list));
                return response;
            } catch (CargoException e) {
                return BandServiceMessage.Response.DEVICE_DATA_ERROR;
            }
        }
        return response;
    }

    public BandServiceMessage.Response getInstalledAppListWithoutImage(Bundle responseBundle) {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoInstalledAppListGetNoImages, null, 1324);
        BandServiceMessage.Response response = processCommand(cmd);
        if (!response.isError()) {
            try {
                responseBundle.putParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, new StartStrip(new StrappListFromDevice(cmd.getResultByte(), false)));
                return response;
            } catch (Exception e) {
                KDKLog.e(TAG, "Get Installed AppList error: ", e);
                return BandServiceMessage.Response.DEVICE_DATA_ERROR;
            }
        }
        return response;
    }

    public BandServiceMessage.Response getDefaultInstalledAppListWithImage(Bundle responseBundle) {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoInstalledAppListGetDefaults, null, StrappListFromDevice.STRAPP_LIST_STRUCTURE_SIZE);
        BandServiceMessage.Response response = processCommand(cmd);
        if (!response.isError()) {
            StrappListFromDevice list = new StrappListFromDevice(cmd.getResultByte(), true);
            try {
                responseBundle.putParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, new StartStrip(list));
                return response;
            } catch (CargoException e) {
                return BandServiceMessage.Response.DEVICE_DATA_ERROR;
            }
        }
        return response;
    }

    public BandServiceMessage.Response getDefaultInstalledAppListWithoutImage(Bundle responseBundle) {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoInstalledAppListGetDefaultsNoImages, null, 1324);
        BandServiceMessage.Response response = processCommand(cmd);
        if (!response.isError()) {
            StrappListFromDevice list = new StrappListFromDevice(cmd.getResultByte(), false);
            try {
                responseBundle.putParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, new StartStrip(list));
                return response;
            } catch (CargoException e) {
                return BandServiceMessage.Response.DEVICE_DATA_ERROR;
            }
        }
        return response;
    }

    public BandServiceMessage.Response getStrappWithImage(UUID tileId, Bundle responseBundle) {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoInstalledAppListGetStrapp, UUIDHelper.toGuidArray(tileId), CargoStrapp.CARGO_STRAPP_STRUCTURE_SIZE);
        BandServiceMessage.Response response = processCommand(cmd);
        if (!response.isError()) {
            responseBundle.putParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, new CargoStrapp(cmd.getResultByte(), true));
        }
        return response;
    }

    public BandServiceMessage.Response getStrappWithoutImage(UUID tileId, Bundle responseBundle) {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoInstalledAppListGetStrappNoImage, UUIDHelper.toGuidArray(tileId), 88);
        BandServiceMessage.Response response = processCommand(cmd);
        if (!response.isError()) {
            responseBundle.putParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, new CargoStrapp(cmd.getResultByte(), false));
        }
        return response;
    }

    public BandServiceMessage.Response setStartStrip(final StartStrip strapps, final Bundle responseBundle) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.12
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, false);
                BandServiceMessage.Response response = DeviceServiceProvider.this.startUISync();
                if (!response.isError()) {
                    response = DeviceServiceProvider.this.setStartStripHelperInsideSync(strapps);
                    BandServiceMessage.Response responseEndSync = DeviceServiceProvider.this.endUISync();
                    if (!response.isError()) {
                        response = responseEndSync;
                    }
                }
                if (response.isError()) {
                    return response;
                }
                BandServiceMessage.Response response2 = DeviceServiceProvider.this.setStartStripHelperOutsideSync(strapps);
                if (!response2.isError()) {
                    responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, true);
                }
                return response2;
            }
        });
    }

    public BandServiceMessage.Response updateStrapp(final CargoStrapp strapp, final Bundle responseBundle) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.13
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, false);
                BandServiceMessage.Response response = DeviceServiceProvider.this.startUISync();
                if (!response.isError()) {
                    response = DeviceServiceProvider.this.updateStrappHelperInsideSync(strapp);
                    BandServiceMessage.Response responseEndSync = DeviceServiceProvider.this.endUISync();
                    if (!response.isError()) {
                        response = responseEndSync;
                    }
                }
                if (response.isError()) {
                    return response;
                }
                BandServiceMessage.Response response2 = DeviceServiceProvider.this.setLayout(strapp);
                if (!response2.isError()) {
                    responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, true);
                }
                return response2;
            }
        });
    }

    public BandServiceMessage.Response getTileSettingsMask(UUID tileId, Bundle responseBundle) {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoInstalledAppGetStrappSettingsMask, UUIDHelper.toGuidArray(tileId), 2);
        BandServiceMessage.Response response = processCommand(cmd);
        if (!response.isError()) {
            responseBundle.putInt(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, TileSettings.get(cmd.getResultByte()));
        }
        return response;
    }

    public BandServiceMessage.Response setTileSettingsMask(UUID tileId, int settingMask) {
        StrappSettingsData strappSettingsData = new StrappSettingsData(tileId, settingMask);
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoInstalledAppSetStrappSettingsMask, null, strappSettingsData.toByte());
        return processCommand(cmd);
    }

    public BandServiceMessage.Response getMeTileImageId(@NonNull Bundle responseBundle) {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoSystemSettingsGetMeTileImageId, null, 4);
        BandServiceMessage.Response response = processCommand(cmd);
        if (!response.isError()) {
            ByteBuffer buffer = ByteBuffer.wrap(cmd.getResultByte()).order(ByteOrder.LITTLE_ENDIAN);
            responseBundle.putLong(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, BitHelper.unsignedIntegerToLong(buffer.getInt()));
        }
        return response;
    }

    public BandServiceMessage.Response getMeTileImage(@NonNull final Bundle responseBundle, final int clientVersion) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.14
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                int size;
                BandServiceMessage.Response response = DeviceServiceProvider.this.getMeTileImageId(responseBundle);
                if (!response.isError()) {
                    if (responseBundle.getLong(InternalBandConstants.EXTRA_COMMAND_PAYLOAD) == 0) {
                        return BandServiceMessage.Response.NO_METILE_IMAGE;
                    }
                    if (VersionCheck.isV2DeviceOrGreater(DeviceServiceProvider.this.getHardwareVersion()) && clientVersion >= 16842752) {
                        size = 79360;
                    } else {
                        size = 63240;
                    }
                    CommandRead cmdGetMeTile = new CommandRead(BandDeviceConstants.Command.CargoFireballUIReadMeTileImage, null, size);
                    BandServiceMessage.Response response2 = deviceProvider.processCommand(cmdGetMeTile);
                    if (!response2.isError()) {
                        responseBundle.putByteArray(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, cmdGetMeTile.getResultByte());
                        return response2;
                    }
                    return response2;
                }
                return response;
            }
        });
    }

    public BandServiceMessage.Response setMeTileImage(final byte[] imageBytes, final long id) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.15
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response = DeviceServiceProvider.this.startUISync();
                if (!response.isError()) {
                    BandServiceMessage.Response response2 = DeviceServiceProvider.this.setMeTileImageHelper(imageBytes, id);
                    BandServiceMessage.Response responseEndSync = DeviceServiceProvider.this.endUISync();
                    if (!response2.isError()) {
                        return responseEndSync;
                    }
                    return response2;
                }
                return response;
            }
        });
    }

    public BandServiceMessage.Response clearMeTileImage() {
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoFireballUIClearMeTileImage, null, null);
        return processCommand(cmd);
    }

    public BandServiceMessage.Response getTheme(@NonNull Bundle responseBundle) {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoThemeColorGetFirstPartyTheme, null, 24);
        BandServiceMessage.Response response = processCommand(cmd);
        if (!response.isError()) {
            StrappColorPalette colorPalette = new StrappColorPalette(cmd.getResultByte());
            responseBundle.putParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, colorPalette.getTheme());
        }
        return response;
    }

    public BandServiceMessage.Response setTheme(@NonNull final StrappColorPalette colorPalette) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.16
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response = DeviceServiceProvider.this.startUISync();
                if (!response.isError()) {
                    BandServiceMessage.Response response2 = DeviceServiceProvider.this.setBandThemeHelper(colorPalette);
                    BandServiceMessage.Response responseEndSync = DeviceServiceProvider.this.endUISync();
                    if (!response2.isError()) {
                        return responseEndSync;
                    }
                    return response2;
                }
                return response;
            }
        });
    }

    public BandServiceMessage.Response resetTheme() {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.17
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response = DeviceServiceProvider.this.startUISync();
                if (!response.isError()) {
                    CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoThemeColorReset, null, null);
                    BandServiceMessage.Response response2 = deviceProvider.processCommand(cmd);
                    BandServiceMessage.Response responseEndSync = DeviceServiceProvider.this.endUISync();
                    if (!response2.isError()) {
                        return responseEndSync;
                    }
                    return response2;
                }
                return response;
            }
        });
    }

    public BandServiceMessage.Response setTileTheme(@NonNull final UUID id, @NonNull final StrappColorPalette colorPalette) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.18
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response = DeviceServiceProvider.this.startUISync();
                if (!response.isError()) {
                    BandServiceMessage.Response response2 = DeviceServiceProvider.this.setCustomTileThemeHelper(id, colorPalette);
                    BandServiceMessage.Response responseEndSync = DeviceServiceProvider.this.endUISync();
                    if (!response2.isError()) {
                        return responseEndSync;
                    }
                    return response2;
                }
                return response;
            }
        });
    }

    public BandServiceMessage.Response setTileThemes(@NonNull final Map<UUID, StrappColorPalette> themeMap) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.19
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response = DeviceServiceProvider.this.startUISync();
                if (!response.isError()) {
                    for (Map.Entry<UUID, StrappColorPalette> pair : themeMap.entrySet()) {
                        response = DeviceServiceProvider.this.setCustomTileThemeHelper(pair.getKey(), pair.getValue());
                        if (response != BandServiceMessage.Response.SUCCESS) {
                            break;
                        }
                    }
                    BandServiceMessage.Response responseEndSync = DeviceServiceProvider.this.endUISync();
                    if (!response.isError()) {
                        return responseEndSync;
                    }
                    return response;
                }
                return response;
            }
        });
    }

    public BandServiceMessage.Response setTileImageIndex(@NonNull UUID appId, int tileImageIndex) {
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(20).put(UUIDHelper.toGuidArray(appId)).putInt(tileImageIndex);
        CommandWrite appTileIconCmd = new CommandWrite(BandDeviceConstants.Command.CargoDynamicAppSetAppTileIndex, null, writeBuf.array());
        return processCommand(appTileIconCmd);
    }

    public BandServiceMessage.Response setTileBadgeIndex(@NonNull UUID appId, int badgeImageIndex) {
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(20).put(UUIDHelper.toGuidArray(appId)).putInt(badgeImageIndex);
        CommandWrite appBadgeIconCmd = new CommandWrite(BandDeviceConstants.Command.CargoDynamicAppSetAppBadgeTileIndex, null, writeBuf.array());
        return processCommand(appBadgeIconCmd);
    }

    public BandServiceMessage.Response setTileNotificationIndex(@NonNull UUID appId, int notificationImageIndex) {
        if (VersionCheck.isV2DeviceOrGreater(getHardwareVersion())) {
            ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(20).put(UUIDHelper.toGuidArray(appId)).putInt(notificationImageIndex);
            CommandWrite appNotificationIconCmd = new CommandWrite(BandDeviceConstants.Command.CargoDynamicAppSetAppNotificationIndex, null, writeBuf.array());
            return processCommand(appNotificationIconCmd);
        }
        return BandServiceMessage.Response.SUCCESS;
    }

    public BandServiceMessage.Response personalizeDevice(@NonNull final StartStrip strapps, final byte[] image, final StrappColorPalette theme, final long imageId, @NonNull final Map<UUID, StrappColorPalette> customThemes) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.20
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response = DeviceServiceProvider.this.startUISync();
                if (!response.isError()) {
                    if (image != null && (response = DeviceServiceProvider.this.setMeTileImageHelper(image, imageId)) != BandServiceMessage.Response.SUCCESS) {
                        return response;
                    }
                    if (theme != null && (response = DeviceServiceProvider.this.setBandThemeHelper(theme)) != BandServiceMessage.Response.SUCCESS) {
                        return response;
                    }
                    if (strapps != null && (response = DeviceServiceProvider.this.setStartStripHelperInsideSync(strapps)) != BandServiceMessage.Response.SUCCESS) {
                        return response;
                    }
                    if (customThemes != null) {
                        for (Map.Entry<UUID, StrappColorPalette> pair : customThemes.entrySet()) {
                            response = DeviceServiceProvider.this.setCustomTileThemeHelper(pair.getKey(), pair.getValue());
                            if (response != BandServiceMessage.Response.SUCCESS) {
                                return response;
                            }
                        }
                    }
                    BandServiceMessage.Response responseEndSync = DeviceServiceProvider.this.endUISync();
                    if (!response.isError()) {
                        response = responseEndSync;
                    }
                    if (response.isError()) {
                        return response;
                    }
                    if (strapps != null) {
                        response = DeviceServiceProvider.this.setStartStripHelperOutsideSync(strapps);
                    }
                }
                return response;
            }
        });
    }

    public BandServiceMessage.Response showDialog(final String tileId, final NotificationGenericDialog dialog, final byte[] appId) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.21
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response = DeviceServiceProvider.this.validateTile(UUID.fromString(tileId), appId);
                return response != BandServiceMessage.Response.SUCCESS ? response : DeviceServiceProvider.this.showDialogWithoutValidation(tileId, dialog);
            }
        });
    }

    public BandServiceMessage.Response showDialogWithoutValidation(String tileId, NotificationGenericDialog dialog) {
        NotificationCommand cmdShowDialog = new NotificationCommand(DeviceConstants.NotificationID.GENERIC_DIALOG, tileId, dialog.toBytes());
        return processCommand(cmdShowDialog);
    }

    public BandServiceMessage.Response sendMessage(final String tileId, final StrappMessage message, final byte[] appId) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.22
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response response = DeviceServiceProvider.this.validateTile(UUID.fromString(tileId), appId);
                if (response == BandServiceMessage.Response.SUCCESS) {
                    return DeviceServiceProvider.this.sendMessageWithoutValidation(tileId, message);
                }
                return response;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response sendMessageWithoutValidation(String tileId, StrappMessage message) {
        NotificationCommand cmdSendMessage = new NotificationCommand(DeviceConstants.NotificationID.MESSAGING, tileId, message.toBytes());
        return processCommand(cmdSendMessage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response startUISync() {
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoInstalledAppUISyncStart, null, null);
        return processCommand(cmd);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response endUISync() {
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoInstalledAppUISyncEnd, null, null);
        return processCommand(cmd);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response setMeTileImageHelper(byte[] imageBytes, long id) {
        BandServiceMessage.Response response = BandServiceMessage.Response.INVALID_ARG_ERROR;
        if (imageBytes != null && id > 0) {
            ByteBuffer argBuf = BufferUtil.allocateLittleEndian(4).putInt(BitHelper.longToUnsignedInt(id));
            CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoFireballUIWriteMeTileImageWithID, argBuf.array(), imageBytes);
            return processCommand(cmd);
        }
        return response;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response setBandThemeHelper(@NonNull StrappColorPalette colorPalette) {
        BandServiceMessage.Response response = BandServiceMessage.Response.INVALID_ARG_ERROR;
        if (colorPalette != null) {
            CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoThemeColorSetFirstPartyTheme, null, colorPalette.toBytes());
            BandServiceMessage.Response response2 = processCommand(cmd);
            return response2;
        }
        return response;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response setCustomTileThemeHelper(@NonNull UUID id, @NonNull StrappColorPalette colorPalette) {
        BandServiceMessage.Response response = BandServiceMessage.Response.INVALID_ARG_ERROR;
        if (colorPalette != null && id != null) {
            ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(40).put(colorPalette.toBytes()).put(UUIDHelper.toGuidArray(id));
            CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoThemeColorSetCustomTheme, null, writeBuf.array());
            return processCommand(cmd);
        }
        return response;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response setStartStripHelperInsideSync(StartStrip strapps) {
        Bundle bundle = new Bundle();
        BandServiceMessage.Response response = getInstalledAppListWithoutImage(bundle);
        if (response != BandServiceMessage.Response.SUCCESS) {
            return response;
        }
        StartStrip strip = (StartStrip) bundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
        ArrayList<CargoStrapp> listOnDevice = (ArrayList) strip.getAppList();
        BandServiceMessage.Response response2 = getDefaultInstalledAppListWithoutImage(bundle);
        if (response2 != BandServiceMessage.Response.SUCCESS) {
            return response2;
        }
        StartStrip strip2 = (StartStrip) bundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
        ArrayList<CargoStrapp> defaults = (ArrayList) strip2.getAppList();
        int i = 0;
        while (i < listOnDevice.size()) {
            UUID id = listOnDevice.get(i).getId();
            if (!strapps.contains(id)) {
                BandServiceMessage.Response response3 = removeApp(id);
                if (response3 != BandServiceMessage.Response.SUCCESS) {
                    return response3;
                }
                listOnDevice.remove(i);
                i--;
            }
            i++;
        }
        for (int i2 = 0; i2 < strapps.getCount(); i2++) {
            CargoStrapp app = strapps.getAppList().get(i2);
            boolean isDefault = isStrappInList(defaults, app);
            boolean alreadyOnDevice = isStrappInList(listOnDevice, app);
            if (isDefault && !alreadyOnDevice) {
                BandServiceMessage.Response response4 = registerDefaultApp(app);
                if (response4 != BandServiceMessage.Response.SUCCESS) {
                    return response4;
                }
            } else if (!isDefault && alreadyOnDevice) {
                if (app.getImages() != null && app.getImages().size() > 0) {
                    BandServiceMessage.Response response5 = registerAppIcon(app);
                    if (response5 != BandServiceMessage.Response.SUCCESS) {
                        return response5;
                    }
                    BandServiceMessage.Response response6 = setTileImageIndex(app.getId(), app.getTileImageIndex());
                    if (response6 != BandServiceMessage.Response.SUCCESS) {
                        return response6;
                    }
                    BandServiceMessage.Response response7 = setTileBadgeIndex(app.getId(), app.getBadgeImageIndex());
                    if (response7 != BandServiceMessage.Response.SUCCESS) {
                        return response7;
                    }
                    BandServiceMessage.Response response8 = setTileNotificationIndex(app.getId(), app.getNotificationImageIndex());
                    if (BandServiceMessage.Response.SUCCESS != response8) {
                        return response8;
                    }
                }
            } else if (isDefault) {
                continue;
            } else if (app.getImages() == null) {
                return BandServiceMessage.Response.TILE_ICON_ERROR;
            } else {
                BandServiceMessage.Response response9 = registerApp(app);
                if (response9 != BandServiceMessage.Response.SUCCESS) {
                    return response9;
                }
                BandServiceMessage.Response response10 = setTileImageIndex(app.getId(), app.getTileImageIndex());
                if (response10 != BandServiceMessage.Response.SUCCESS) {
                    return response10;
                }
                BandServiceMessage.Response response11 = setTileBadgeIndex(app.getId(), app.getBadgeImageIndex());
                if (response11 != BandServiceMessage.Response.SUCCESS) {
                    return response11;
                }
                BandServiceMessage.Response response12 = setTileNotificationIndex(app.getId(), app.getNotificationImageIndex());
                if (BandServiceMessage.Response.SUCCESS != response12) {
                    return response12;
                }
            }
        }
        return setInstalledAppList(strapps);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response updateStrappHelperInsideSync(CargoStrapp app) {
        Bundle bundle = new Bundle();
        UUID id = app.getId();
        BandServiceMessage.Response response = getInstalledAppListWithoutImage(bundle);
        if (response != BandServiceMessage.Response.SUCCESS) {
            return response;
        }
        StartStrip strip = (StartStrip) bundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
        if (!strip.contains(id)) {
            return BandServiceMessage.Response.TILE_NOT_FOUND_ERROR;
        }
        BandServiceMessage.Response response2 = getDefaultInstalledAppListWithoutImage(bundle);
        if (response2 != BandServiceMessage.Response.SUCCESS) {
            return response2;
        }
        StartStrip defaultStrip = (StartStrip) bundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
        if (defaultStrip.contains(id)) {
            return BandServiceMessage.Response.UPDATE_DEFAULT_STRAPP_ERROR;
        }
        if (app.getImages() != null && app.getImages().size() > 0) {
            BandServiceMessage.Response response3 = registerAppIcon(app);
            if (response3 != BandServiceMessage.Response.SUCCESS) {
                return response3;
            }
            BandServiceMessage.Response response4 = setTileImageIndex(app.getId(), app.getTileImageIndex());
            if (response4 != BandServiceMessage.Response.SUCCESS) {
                return response4;
            }
            BandServiceMessage.Response response5 = setTileBadgeIndex(app.getId(), app.getBadgeImageIndex());
            if (response5 != BandServiceMessage.Response.SUCCESS) {
                return response5;
            }
            BandServiceMessage.Response response6 = setTileNotificationIndex(app.getId(), app.getNotificationImageIndex());
            if (BandServiceMessage.Response.SUCCESS != response6) {
                return response6;
            }
        }
        return setInstalledAppListStrapp(app);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response setStartStripHelperOutsideSync(StartStrip strapps) {
        Bundle bundle = new Bundle();
        BandServiceMessage.Response response = getDefaultInstalledAppListWithoutImage(bundle);
        if (response != BandServiceMessage.Response.SUCCESS) {
            return response;
        }
        StartStrip strip = (StartStrip) bundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
        ArrayList<CargoStrapp> defaults = (ArrayList) strip.getAppList();
        for (int i = 0; i < strapps.getCount(); i++) {
            CargoStrapp app = strapps.getAppList().get(i);
            if (!isStrappInList(defaults, app) && (response = setLayout(app)) != BandServiceMessage.Response.SUCCESS) {
                return response;
            }
        }
        return response;
    }

    private boolean isStrappInList(ArrayList<CargoStrapp> listOnDevice, CargoStrapp app) {
        Iterator i$ = listOnDevice.iterator();
        while (i$.hasNext()) {
            CargoStrapp strapp = i$.next();
            if (strapp.getId().equals(app.getId())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response setInstalledAppList(@NonNull StartStrip strappList) {
        byte[] payload = strappList.toBytesToDevice();
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(payload.length).put(payload);
        ByteBuffer argBuf = BufferUtil.allocateLittleEndian(4).putInt(strappList.getCount());
        CommandWrite setStrappListCmd = new CommandWrite(BandDeviceConstants.Command.CargoInstalledAppListSet, argBuf.array(), writeBuf.array());
        return processCommand(setStrappListCmd);
    }

    private BandServiceMessage.Response setInstalledAppListStrapp(@NonNull CargoStrapp strapp) {
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(88).put(strapp.getStrappData().toByte());
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoInstalledAppListSetStrapp, null, writeBuf.array());
        return processCommand(cmd);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response registerApp(@NonNull CargoStrapp strapp) {
        UUID appId = strapp.getId();
        List<Bitmap> images = strapp.getImages();
        int numIcons = images.size();
        int size = (numIcons * 1024) + 20;
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(size).put(UUIDHelper.toGuidArray(appId)).putInt(numIcons);
        for (int i = 0; i < numIcons; i++) {
            StrappIcon icon = new StrappIcon(images.get(i));
            writeBuf.put(icon.toByte());
        }
        CommandWrite registerAppCmd = new CommandWrite(BandDeviceConstants.Command.CargoDynamicAppRegisterApp, null, writeBuf.array());
        return processCommand(registerAppCmd);
    }

    private BandServiceMessage.Response registerDefaultApp(@NonNull CargoStrapp strapp) {
        UUID appId = strapp.getId();
        int size = 20 + 0;
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(size).put(UUIDHelper.toGuidArray(appId)).putInt(0);
        CommandWrite registerAppCmd = new CommandWrite(BandDeviceConstants.Command.CargoDynamicAppRegisterApp, null, writeBuf.array());
        return processCommand(registerAppCmd);
    }

    private BandServiceMessage.Response registerAppIcon(@NonNull CargoStrapp strapp) {
        UUID appId = strapp.getId();
        List<Bitmap> images = strapp.getImages();
        int numIcons = images.size();
        int size = (numIcons * 1024) + 20;
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(size).put(UUIDHelper.toGuidArray(appId)).putInt(numIcons);
        for (int i = 0; i < numIcons; i++) {
            StrappIcon icon = new StrappIcon(images.get(i));
            writeBuf.put(icon.toByte());
        }
        CommandWrite registerAppCmd = new CommandWrite(BandDeviceConstants.Command.CargoDynamicAppRegisterAppIcons, null, writeBuf.array());
        return processCommand(registerAppCmd);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response removeApp(UUID tileId) {
        CommandWrite removeCommand = new CommandWrite(BandDeviceConstants.Command.CargoDynamicAppRemoveApp, null, UUIDHelper.toGuidArray(tileId));
        return processCommand(removeCommand);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response setLayout(CargoStrapp strapp) {
        BandServiceMessage.Response response = BandServiceMessage.Response.SUCCESS;
        for (Integer num : strapp.getLayoutsToRemove()) {
            int indexToRemove = num.intValue();
            response = removePageLayout(strapp.getId(), indexToRemove);
            if (response != BandServiceMessage.Response.SUCCESS) {
                return response;
            }
        }
        for (Map.Entry<Integer, StrappLayout> pair : strapp.getLayouts().entrySet()) {
            response = setPageLayout(strapp.getId(), pair.getKey().intValue(), pair.getValue().getLayoutBlob());
            if (response != BandServiceMessage.Response.SUCCESS) {
                return response;
            }
        }
        return response;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response getLayouts(UUID tileId, Bundle responseBundle) {
        BandServiceMessage.Response response = BandServiceMessage.Response.SUCCESS;
        HashMap<Integer, StrappLayout> layoutMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            response = getPageLayout(tileId, i, responseBundle);
            if (response != BandServiceMessage.Response.SUCCESS) {
                return response;
            }
            layoutMap.put(Integer.valueOf(i), new StrappLayout(responseBundle.getByteArray(InternalBandConstants.EXTRA_COMMAND_PAYLOAD)));
        }
        responseBundle.putSerializable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, layoutMap);
        return response;
    }

    private BandServiceMessage.Response removePageLayout(UUID appId, int layoutIndex) {
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(20).put(UUIDHelper.toGuidArray(appId)).putInt(layoutIndex);
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoDynamicPageLayoutRemove, null, writeBuf.array());
        return processCommand(cmd);
    }

    private BandServiceMessage.Response setPageLayout(UUID appId, int layoutIndex, byte[] layoutBlob) {
        int layoutBlobSize = layoutBlob.length;
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(layoutBlobSize + 24).put(UUIDHelper.toGuidArray(appId)).putInt(layoutIndex).putInt(layoutBlobSize).put(layoutBlob);
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoDynamicPageLayoutSet, null, writeBuf.array());
        return processCommand(cmd);
    }

    public BandServiceMessage.Response updatePages(final UUID tileId, final Parcelable[] data, final Bundle responseBundle, final byte[] appId) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.23
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response securityResponse = DeviceServiceProvider.this.validateTile(tileId, appId);
                if (securityResponse != BandServiceMessage.Response.SUCCESS) {
                    responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, false);
                    return securityResponse;
                }
                for (int i = 0; i < data.length; i++) {
                    try {
                        PageData pageData = (PageData) data[i];
                        NotificationGenericUpdate genericUpdate = new NotificationGenericUpdate(pageData.getPageId(), pageData.getPageLayoutIndex(), DeviceServiceProvider.this.toStrappElements(pageData.getValues()));
                        BandServiceMessage.Response response = DeviceServiceProvider.this.updatePage(tileId, genericUpdate.toBytes());
                        if (response.isError()) {
                            responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, false);
                            return response;
                        }
                    } catch (CargoException e) {
                        KDKLog.e(DeviceServiceProvider.TAG, "update pages", e);
                        return e.getResponse();
                    } catch (IllegalArgumentException e2) {
                        KDKLog.e(DeviceServiceProvider.TAG, "update pages", e2);
                        return BandServiceMessage.Response.TILE_PAGE_DATA_ERROR;
                    }
                }
                responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, true);
                return BandServiceMessage.Response.SUCCESS;
            }
        });
    }

    public BandServiceMessage.Response updatePage(UUID tileId, byte[] pageData) {
        NotificationCommand notificationCommand = new NotificationCommand(DeviceConstants.NotificationID.GENERIC_UPDATE, tileId.toString(), pageData);
        return processCommand(notificationCommand);
    }

    public BandServiceMessage.Response removePages(final UUID tileId, final Bundle responseBundle, final byte[] appId) {
        return processAsBatch(new BatchCommand() { // from class: com.microsoft.band.service.device.DeviceServiceProvider.24
            @Override // com.microsoft.band.service.command.BatchCommand
            public BandServiceMessage.Response execute(@NonNull DeviceServiceProvider deviceProvider) {
                BandServiceMessage.Response securityResponse = DeviceServiceProvider.this.validateTile(tileId, appId);
                if (securityResponse != BandServiceMessage.Response.SUCCESS) {
                    responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, false);
                    return securityResponse;
                }
                BandServiceMessage.Response response = DeviceServiceProvider.this.clearTile(tileId);
                responseBundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, response.isError() ? false : true);
                return response;
            }
        });
    }

    public BandServiceMessage.Response clearTile(UUID tileId) {
        NotificationCommand notificationCommand = new NotificationCommand(DeviceConstants.NotificationID.GENERIC_CLEAR_STRAPP, tileId.toString(), null);
        return processCommand(notificationCommand);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<StrappPageElement> toStrappElements(List<PageElementData> data) throws CargoException {
        List<StrappPageElement> strappElements = new ArrayList<>(data.size());
        for (PageElementData element : data) {
            strappElements.add(StrappPageElement.from(element));
        }
        return strappElements;
    }

    private BandServiceMessage.Response getPageLayout(UUID appId, int layoutIndex, Bundle responseBundle) {
        ByteBuffer argBuf = BufferUtil.allocateLittleEndian(24).put(UUIDHelper.toGuidArray(appId)).putInt(layoutIndex).putInt(768);
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoDynamicPageLayoutGet, argBuf.array(), 768);
        BandServiceMessage.Response response = processCommand(cmd);
        if (!response.isError()) {
            responseBundle.putByteArray(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, cmd.getResultByte());
        }
        return response;
    }

    public BandServiceMessage.Response sendPageToBand(UUID tileId, PageData pageData, boolean sendAsMessage) {
        if (sendAsMessage) {
            String title = null;
            String body = null;
            for (PageElementData element : pageData.getValues()) {
                switch (element.getId()) {
                    case 1:
                        title = ((TextBlockData) element).getText();
                        break;
                    case 2:
                        body = ((TextBlockData) element).getText();
                        break;
                }
            }
            try {
                StrappMessage message = new StrappMessage(title, body, new Date());
                return sendMessageWithoutValidation(tileId.toString(), message);
            } catch (Exception e) {
                KDKLog.e(TAG, e, "Exception caught when trying to construct strapp message: %s", e.getMessage());
                return BandServiceMessage.Response.TILE_PAGE_DATA_ERROR;
            }
        }
        List<StrappPageElement> elements = new ArrayList<>();
        try {
            for (PageElementData element2 : pageData.getValues()) {
                elements.add(StrappPageElement.from(element2));
            }
            NotificationGenericUpdate genericUpdate = new NotificationGenericUpdate(pageData.getPageId(), pageData.getPageLayoutIndex(), elements);
            return updatePage(tileId, genericUpdate.toBytes());
        } catch (Exception e2) {
            KDKLog.e(TAG, e2, "Exception caught when trying to construct DBLOB: %s", e2.getMessage());
            return BandServiceMessage.Response.TILE_PAGE_DATA_ERROR;
        }
    }
}
