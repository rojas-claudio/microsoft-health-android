package com.microsoft.band;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import com.microsoft.band.BandServiceConnection;
import com.microsoft.band.cloud.CargoServiceInfo;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.KDKLog;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class KDKServiceConnection {
    private KDKServiceConnection() {
        throw new UnsupportedOperationException();
    }

    public static BandServiceConnection create(Context context, CargoServiceInfo serviceInfo, DeviceInfo deviceInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CargoServiceInfo.EXTRA_SERVICE_INFO, serviceInfo);
        bundle.putParcelable(InternalBandConstants.EXTRA_DEVICE_INFO, deviceInfo);
        return new BandServiceConnection(context, bundle).setDeviceStatusListener(new DeviceStatusListenerImpl(context)).setSyncNotificationListener(new SyncNotificationListenerImpl(context)).setSyncProgressNotificationListener(new SyncProgressNotificationListenerImpl(context)).setFirmwareUpgradeProgressNotificationListener(new FirmwareUpgradeProgressNotificationListenerImpl(context)).setDownloadNotificationListener(new DownloadNotificationListenerImpl(context)).setUpgradeNotificationListener(new UpgradeNotificationListenerImpl(context));
    }

    /* loaded from: classes.dex */
    private static abstract class DeviceListener {
        private static final String TAG = DeviceListener.class.getSimpleName();
        protected final LocalBroadcastManager mLocalBroadcastManager;

        public DeviceListener(Context context) {
            this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
        }

        protected boolean sendLocalBroadcast(String intentAction, String extraKey, Parcelable extraData, int resultCode) {
            return sendLocalBroadcast(intentAction, extraKey, extraData, resultCode, null, null);
        }

        protected boolean sendLocalBroadcast(String intentAction, String extraKey, Parcelable extraData, int resultCode, String cloudKey, Object cloudData) {
            if (intentAction == null) {
                return false;
            }
            Intent intent = new Intent(intentAction);
            if (extraKey != null && extraData != null) {
                intent.putExtra(extraKey, extraData);
            }
            if (cloudKey != null && cloudData != null) {
                if (cloudData instanceof Parcelable) {
                    intent.putExtra(cloudKey, (Parcelable) cloudData);
                } else if (cloudData instanceof ArrayList) {
                    intent.putExtra(cloudKey, (ArrayList) cloudData);
                }
            }
            intent.putExtra(CargoConstants.EXTRA_RESULT_CODE, resultCode);
            boolean sent = this.mLocalBroadcastManager.sendBroadcast(intent);
            KDKLog.i(TAG, "Sent Local Broadcast %s with extra %s'", intentAction, extraKey);
            return sent;
        }
    }

    /* loaded from: classes.dex */
    private static class DeviceStatusListenerImpl extends DeviceListener implements BandServiceConnection.DeviceStatusListener {
        public DeviceStatusListenerImpl(Context context) {
            super(context);
        }

        @Override // com.microsoft.band.BandServiceConnection.DeviceStatusListener
        public void onDeviceStatusNotification(Message msg) {
            String intentAction = null;
            Bundle data = msg.getData();
            DeviceInfo deviceInfo = (DeviceInfo) data.getParcelable(InternalBandConstants.EXTRA_DEVICE_INFO);
            if (BandServiceMessage.Response.DEVICE_CONNECTED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_DEVICE_CONNECTED;
            } else if (BandServiceMessage.Response.DEVICE_DISCONNECTED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_DEVICE_DISCONNECTED;
            }
            sendLocalBroadcast(intentAction, InternalBandConstants.EXTRA_DEVICE_INFO, deviceInfo, msg.arg2);
        }
    }

    /* loaded from: classes.dex */
    private static class SyncNotificationListenerImpl extends DeviceListener implements BandServiceConnection.SyncNotificationListener {
        public SyncNotificationListenerImpl(Context context) {
            super(context);
        }

        @Override // com.microsoft.band.BandServiceConnection.SyncNotificationListener
        public void onSyncNotification(Message msg) {
            Bundle data = msg.getData();
            Parcelable syncResult = null;
            Object cloudData = null;
            String intentAction = null;
            if (BandServiceMessage.Response.SYNC_DEVICE_TO_CLOUD_STARTED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_SYNC_DEVICE_TO_CLOUD_STARTED;
            } else if (BandServiceMessage.Response.SYNC_DEVICE_TO_CLOUD_COMPLETED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_SYNC_DEVICE_TO_CLOUD_COMPLETED;
                syncResult = data.getParcelable(CargoConstants.EXTRA_DOWNLOAD_SYNC_RESULT);
                cloudData = data.getParcelableArrayList(CargoConstants.EXTRA_CLOUD_DATA);
            } else if (BandServiceMessage.Response.SYNC_WEB_TILE_COMPLETED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_SYNC_WEB_TILE_COMPLETED;
                syncResult = data.getParcelable(CargoConstants.EXTRA_DOWNLOAD_SYNC_RESULT);
            }
            sendLocalBroadcast(intentAction, CargoConstants.EXTRA_SYNC_RESULT, syncResult, msg.arg1, CargoConstants.EXTRA_CLOUD_DATA, cloudData);
        }
    }

    /* loaded from: classes.dex */
    private static class SyncProgressNotificationListenerImpl extends DeviceListener implements BandServiceConnection.SyncProgressNotificationListener {
        public SyncProgressNotificationListenerImpl(Context context) {
            super(context);
        }

        @Override // com.microsoft.band.BandServiceConnection.SyncProgressNotificationListener
        public void onSyncProgressNotification(Message msg) {
            Intent intent = new Intent(CargoConstants.ACTION_SYNC_PROGRESS);
            intent.putExtra(CargoConstants.PROGRESS_VALUE, msg.arg1);
            this.mLocalBroadcastManager.sendBroadcast(intent);
        }
    }

    /* loaded from: classes.dex */
    private static class FirmwareUpgradeProgressNotificationListenerImpl extends DeviceListener implements BandServiceConnection.FirmwareUpgradeProgressNotificationListener {
        public FirmwareUpgradeProgressNotificationListenerImpl(Context context) {
            super(context);
        }

        @Override // com.microsoft.band.BandServiceConnection.FirmwareUpgradeProgressNotificationListener
        public void onFirmwareUpgradeProgressNotification(Message msg) {
            Intent intent = new Intent(CargoConstants.ACTION_FW_UPGRADE_PROGRESS);
            intent.putExtra(CargoConstants.PROGRESS_VALUE, msg.arg1);
            intent.putExtra(CargoConstants.PROGRESS_CODE, msg.arg2);
            this.mLocalBroadcastManager.sendBroadcast(intent);
        }
    }

    /* loaded from: classes.dex */
    private static class DownloadNotificationListenerImpl extends DeviceListener implements BandServiceConnection.DownloadNotificationListener {
        public DownloadNotificationListenerImpl(Context context) {
            super(context);
        }

        @Override // com.microsoft.band.BandServiceConnection.DownloadNotificationListener
        public void onDownloadNotification(Message msg) {
            Bundle data = msg.getData();
            Parcelable cloudData = data.getParcelable(CargoConstants.EXTRA_CLOUD_DATA);
            String intentAction = null;
            if (BandServiceMessage.Response.DOWNLOAD_FIRMWARE_UPDATE_STARTED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_DOWNLOAD_FIRMWARE_UPDATE_STARTED;
            } else if (BandServiceMessage.Response.DOWNLOAD_FIRMWARE_UPDATE_COMPLETED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_DOWNLOAD_FIRMWARE_UPDATE_COMPLETED;
            } else if (BandServiceMessage.Response.DOWNLOAD_EPHEMERIS_UPDATE_STARTED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_DOWNLOAD_EPHEMERIS_UPDATE_STARTED;
            } else if (BandServiceMessage.Response.DOWNLOAD_EPHEMERIS_UPDATE_COMPLETED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_DOWNLOAD_EPHEMERIS_UPDATE_COMPLETED;
            } else if (BandServiceMessage.Response.DOWNLOAD_TIMEZONE_SETTINGS_UPDATE_STARTED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_DOWNLOAD_TIME_ZONE_SETTINGS_UPDATE_STARTED;
            } else if (BandServiceMessage.Response.DOWNLOAD_TIMEZONE_SETTINGS_UPDATE_COMPLETED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_DOWNLOAD_TIME_ZONE_SETTINGS_UPDATE_COMPLETED;
            }
            sendLocalBroadcast(intentAction, CargoConstants.EXTRA_CLOUD_DATA, cloudData, msg.arg1);
        }
    }

    /* loaded from: classes.dex */
    private static class UpgradeNotificationListenerImpl extends DeviceListener implements BandServiceConnection.UpgradeNotificationListener {
        public UpgradeNotificationListenerImpl(Context context) {
            super(context);
        }

        @Override // com.microsoft.band.BandServiceConnection.UpgradeNotificationListener
        public void onUpgradeNotification(Message msg) {
            Bundle data = msg.getData();
            Parcelable deviceInfo = data.getParcelable(InternalBandConstants.EXTRA_DEVICE_INFO);
            Parcelable syncProgress = null;
            String syncProgressKey = null;
            String intentAction = null;
            if (BandServiceMessage.Response.UPGRADE_FIRMWARE_STARTED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_UPGRADE_FIRMWARE_STARTED;
            } else if (BandServiceMessage.Response.UPGRADE_FIRMWARE_COMPLETED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_UPGRADE_FIRMWARE_COMPLETED;
                syncProgress = data.getParcelable(CargoConstants.EXTRA_DOWNLOAD_SYNC_RESULT);
                syncProgressKey = CargoConstants.EXTRA_SYNC_RESULT;
            } else if (BandServiceMessage.Response.UPGRADE_EPHEMERIS_STARTED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_UPGRADE_EPHEMERIS_STARTED;
            } else if (BandServiceMessage.Response.UPGRADE_EPHEMERIS_COMPLETED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_UPGRADE_EPHEMERIS_COMPLETED;
            } else if (BandServiceMessage.Response.UPGRADE_TIMEZONE_SETTINGS_STARTED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_UPGRADE_TIME_ZONE_SETTINGS_STARTED;
            } else if (BandServiceMessage.Response.UPGRADE_TIMEZONE_SETTINGS_COMPLETED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_UPGRADE_TIME_ZONE_SETTINGS_COMPLETED;
            } else if (BandServiceMessage.Response.SYNC_TIMEZONE_COMPLETED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_SYNC_TIMEZONE_COMPLETED;
            } else if (BandServiceMessage.Response.SYNC_TIME_COMPLETED.getCode() == msg.arg2) {
                intentAction = CargoConstants.ACTION_SYNC_TIME_COMPLETED;
            }
            sendLocalBroadcast(intentAction, InternalBandConstants.EXTRA_DEVICE_INFO, deviceInfo, msg.arg1, syncProgressKey, syncProgress);
        }
    }
}
