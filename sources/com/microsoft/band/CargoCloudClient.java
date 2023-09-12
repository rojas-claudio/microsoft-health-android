package com.microsoft.band;

import android.content.Context;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.client.UnitType;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.cloud.CargoServiceInfo;
import com.microsoft.band.cloud.CloudJSONDataModel;
import com.microsoft.band.cloud.CloudProfileLinker;
import com.microsoft.band.cloud.EphemerisUpdateInfo;
import com.microsoft.band.cloud.TimeZoneSettingsUpdateInfo;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.band.device.FirmwareVersions;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.ServiceCommand;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.service.cloud.CloudDataResource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
/* loaded from: classes.dex */
public class CargoCloudClient extends CargoServicesClient {
    public static final long CLOUD_PROCESSING_ERROR_WAIT_TIME = 4000;
    public static final long CLOUD_PROCESSING_SUCCESS_WAIT_TIME = 2000;
    private static final String TAG = CargoCloudClient.class.getSimpleName();

    public static CargoCloudClient create(BandServiceConnection serviceConnection) throws CargoException {
        return new CargoCloudClient(serviceConnection);
    }

    private CargoCloudClient(BandServiceConnection serviceConnection) throws CargoException {
        super(serviceConnection);
    }

    public static CargoCloudClient create(Context context, CargoServiceInfo serviceInfo) throws CargoException {
        BandServiceConnection csc = KDKServiceConnection.create(context, serviceInfo, null);
        CargoCloudClient cargoCloudClient = new CargoCloudClient(csc);
        return cargoCloudClient;
    }

    public UserProfileInfo getCloudProfile() throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoGetCloudProfile);
        getServiceConnection().sendCommand(cmd);
        return (UserProfileInfo) cmd.getBundle().getParcelable(CargoConstants.EXTRA_CLOUD_DATA);
    }

    public void setUserProfileUnitPrefs(UnitType weightUnitType, UnitType distanceUnitType, UnitType temperatureUnitType) throws CargoException {
        UserProfileInfo upi = new UserProfileInfo();
        upi.setDisplayWeightUnit(weightUnitType);
        upi.setDisplayDistanceUnit(distanceUnitType);
        upi.setDisplayTemperatureUnit(temperatureUnitType);
        upi.setRunDisplayUnits(UserProfileInfo.RunDisplayUnits.Local);
        saveCloudProfile(upi, System.currentTimeMillis());
    }

    public void saveCloudProfile(UserProfileInfo cloudProfile, long currentTime) throws CargoException {
        Validation.validateNullParameter(cloudProfile, "Cloud profile object must not be null");
        cloudProfile.setLastKDKSyncUpdateOn(currentTime);
        saveOrLinkCloudProfile(cloudProfile);
    }

    public void linkCloudProfile(CloudProfileLinker cloudLinker, long currentTime) throws CargoException {
        Validation.validateNullParameter(cloudLinker, "Cloud profile linker object must not be null");
        cloudLinker.setLastKDKSyncUpdateOn(currentTime);
        saveOrLinkCloudProfile(cloudLinker);
    }

    private void saveOrLinkCloudProfile(CloudJSONDataModel cloudProfile) throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoSaveUserProfile);
        cmd.getBundle().putParcelable(CargoConstants.EXTRA_CLOUD_DATA, cloudProfile);
        getServiceConnection().sendCommand(cmd);
    }

    public CargoFirmwareUpdateInfo getLatestAvailableFirmwareVersion(String deviceFamily, FirmwareVersions deviceVersion, boolean firmwareOnDeviceValid, Map<String, String> queryParams) throws CargoException {
        Validation.validateNullParameter(deviceFamily, "deviceFamily not specified");
        Validation.validateNullParameter(deviceVersion, "deviceVersion not specified");
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoGetFirmwareUpdateInfo);
        cmd.getBundle().putString(CargoConstants.EXTRA_FIRMWARE_DEVICE_FAMILY, deviceFamily);
        cmd.getBundle().putString(InternalBandConstants.EXTRA_FIRMWARE_VERSION_APP, deviceVersion.getApplicationVersion().getCurrentVersion());
        cmd.getBundle().putString(CargoConstants.EXTRA_FIRMWARE_VERSION_BL, deviceVersion.getBootloaderVersion().getCurrentVersion());
        cmd.getBundle().putString(CargoConstants.EXTRA_FIRMWARE_VERSION_UP, deviceVersion.getUpdaterVersion().getCurrentVersion());
        cmd.getBundle().putBoolean(CargoConstants.EXTRA_FIRMWARE_FW_VALIDATION, firmwareOnDeviceValid);
        if (queryParams != null) {
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, String> pair : queryParams.entrySet()) {
                queryString.append(String.format("&%s=%s", pair.getKey(), pair.getValue()));
            }
            cmd.getBundle().putString(CargoConstants.EXTRA_FIRMWARE_QUERY_PARAM, queryString.toString());
        }
        getServiceConnection().sendCommand(cmd);
        return (CargoFirmwareUpdateInfo) cmd.getBundle().getParcelable(CargoConstants.EXTRA_CLOUD_DATA);
    }

    public boolean downloadFirmwareUpdate(CargoFirmwareUpdateInfo firmwareUpdate) throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoDownloadFirmwareUpdate);
        cmd.getBundle().putParcelable(CargoConstants.EXTRA_CLOUD_DATA, firmwareUpdate);
        return getServiceConnection().sendCommandAsync(cmd);
    }

    public EphemerisUpdateInfo getLatestAvailableEphemerisVersion() throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoGetEphemerisUpdateInfo);
        getServiceConnection().sendCommand(cmd);
        return (EphemerisUpdateInfo) cmd.getBundle().getParcelable(CargoConstants.EXTRA_CLOUD_DATA);
    }

    public boolean downloadEphemerisUpdate(EphemerisUpdateInfo ephemerisUpdate) throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoDownloadEphemerisUpdate);
        cmd.getBundle().putParcelable(CargoConstants.EXTRA_CLOUD_DATA, ephemerisUpdate);
        return getServiceConnection().sendCommandAsync(cmd);
    }

    public boolean downloadTimeZoneSettingsUpdate(TimeZoneSettingsUpdateInfo timezoneSettingsInfo) throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoDownloadTimeZoneSettingsUpdate);
        cmd.getBundle().putParcelable(CargoConstants.EXTRA_CLOUD_DATA, timezoneSettingsInfo);
        return getServiceConnection().sendCommandAsync(cmd);
    }

    public boolean waitForCloudProcessingToComplete(final List<CloudDataResource> cdrList, long timeoutInMillis) throws CargoException {
        Boolean processed = null;
        if (cdrList != null && !cdrList.isEmpty()) {
            Callable<Boolean> callable = new Callable<Boolean>() { // from class: com.microsoft.band.CargoCloudClient.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public synchronized Boolean call() throws Exception {
                    if (cdrList.size() <= 2) {
                        wait(CargoCloudClient.CLOUD_PROCESSING_SUCCESS_WAIT_TIME);
                    }
                    int errorCount = 0;
                    while (!cdrList.isEmpty()) {
                        if (errorCount <= 4) {
                            try {
                                BandServiceMessage.Response response = CargoCloudClient.this.updateCloudDataResourceStatus(cdrList);
                                if (response.isError()) {
                                    errorCount++;
                                    wait(CargoCloudClient.CLOUD_PROCESSING_ERROR_WAIT_TIME);
                                } else if (!cdrList.isEmpty()) {
                                    if (errorCount > 0) {
                                        errorCount--;
                                    }
                                    wait(CargoCloudClient.CLOUD_PROCESSING_SUCCESS_WAIT_TIME);
                                }
                            } catch (CargoException e) {
                                if (e.getResponse() == BandServiceMessage.Response.OPERATION_TIMEOUT_ERROR) {
                                    errorCount++;
                                    wait(CargoCloudClient.CLOUD_PROCESSING_ERROR_WAIT_TIME);
                                } else {
                                    throw e;
                                }
                            }
                        } else {
                            throw new CargoException("Too many errors attempting update", BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
                        }
                    }
                    return Boolean.valueOf(cdrList.isEmpty());
                }
            };
            if (timeoutInMillis == 0) {
                timeoutInMillis = -1;
            }
            Future<Boolean> future = execute(callable, timeoutInMillis);
            try {
                processed = future.get();
            } catch (InterruptedException e) {
                KDKLog.e(TAG, e.getMessage());
                throw new CargoException("Operation was interrupted", e, BandServiceMessage.Response.OPERATION_INTERRUPTED_ERROR);
            } catch (ExecutionException e2) {
                KDKLog.e(TAG, e2.getMessage());
                throw new CargoException("Operation raised an exception", e2, BandServiceMessage.Response.OPERATION_EXCEPTION_ERROR);
            }
        }
        if (processed == null) {
            return false;
        }
        return processed.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BandServiceMessage.Response updateCloudDataResourceStatus(List<CloudDataResource> cdrList) throws CargoException {
        BandServiceMessage.Response response = BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR;
        if (cdrList == null) {
            throw new IllegalArgumentException("CloudDataResource list not specified");
        }
        if (!cdrList.isEmpty()) {
            ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoUpdateCloudDataResourceStatus);
            List<CloudDataResource> subList = new ArrayList<>(25);
            subList.addAll(cdrList.subList(0, Math.min(25, cdrList.size())));
            cmd.getBundle().putParcelableArrayList(CargoConstants.EXTRA_CLOUD_DATA, new ArrayList<>(subList));
            if (getServiceConnection().sendCommand(cmd)) {
                List<String> ids = cmd.getBundle().getStringArrayList(CargoConstants.EXTRA_CLOUD_DATA);
                response = cmd.getResponseCode();
                if (ids != null) {
                    Iterator<CloudDataResource> cdrListIter = cdrList.iterator();
                    while (cdrListIter.hasNext()) {
                        CloudDataResource cdr = cdrListIter.next();
                        if (ids.contains(cdr.getUploadId())) {
                            cdrListIter.remove();
                        }
                    }
                }
            }
        }
        return response;
    }

    public void uploadLogBytesToCloud(byte[] data, CloudDataResource.LogFileTypes type) throws CargoException {
        Validation.validateNullParameter(data, "Log data bytes shouldn't be null");
        Validation.validateNullParameter(type, "LogFileType shouldn't be null");
        if (type == CloudDataResource.LogFileTypes.PERFLOGS) {
            ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoUploadLogToCloud);
            cmd.getBundle().putByteArray(CargoConstants.KEY_LOG_BYTES, data);
            cmd.getBundle().putSerializable(CargoConstants.KEY_LOG_TYPE, type);
            getServiceConnection().sendCommand(cmd);
            return;
        }
        throw new IllegalArgumentException(type.name() + " is invalid logFileType for uploadLogBytesToCloud.");
    }

    public void sendFileToCloud(String fileName, CloudDataResource.LogFileTypes type) throws CargoException {
        Validation.validateNullParameter(fileName, "fileName cannot be null");
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoSendFileToCloud);
        cmd.getBundle().putString(CargoConstants.EXTRA_CLOUD_DATA, fileName);
        cmd.getBundle().putSerializable(CargoConstants.KEY_LOG_TYPE, type);
        getServiceConnection().sendCommand(cmd);
    }
}
