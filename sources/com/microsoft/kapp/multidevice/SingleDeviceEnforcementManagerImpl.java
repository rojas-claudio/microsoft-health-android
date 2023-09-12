package com.microsoft.kapp.multidevice;

import com.microsoft.band.CargoClient;
import com.microsoft.band.DeviceProfileStatus;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.device.DeviceProfileInfo;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.kapp.CargoExtensions;
import com.microsoft.kapp.DeviceErrorState;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import java.util.UUID;
/* loaded from: classes.dex */
public class SingleDeviceEnforcementManagerImpl implements SingleDeviceEnforcementManager {
    private static final String TAG = "CargoConnectionProxy";
    private CredentialsManager mCredentialsManager;
    private SettingsProvider mSettingsProvider;

    public SingleDeviceEnforcementManagerImpl(SettingsProvider settingsProvider, CredentialsManager credentialsManager) {
        this.mSettingsProvider = settingsProvider;
        this.mCredentialsManager = credentialsManager;
    }

    @Override // com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager
    public boolean isUserPairedDevice(CargoClient client) throws CargoException {
        try {
            UUID clientId = client.getDeviceInfo().getDeviceUUID();
            DeviceProfileInfo deviceProfile = client.getDeviceClient().getDeviceProfile();
            UUID profileId = deviceProfile.getProfileID();
            CargoUserProfile profile = this.mSettingsProvider.getUserProfile();
            String msaCredential = this.mCredentialsManager.getCredentials().getKdsCredential().getUserId();
            boolean deviceProfilePaired = clientId.equals(profile.getDeviceId());
            boolean cloudProfilePaired = profileId.toString().equals(msaCredential);
            return deviceProfilePaired && cloudProfilePaired;
        } catch (Exception e) {
            KLog.w(TAG, "Exception thrown checking for user paired device!");
            return false;
        }
    }

    @Override // com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager
    public void performSDECheck(CargoClient client) throws CargoException {
        DeviceProfileStatus status = client.getDeviceAndProfileLinkStatus();
        DeviceProfileStatus.LinkStatus deviceLinkStatus = status.deviceLinkStatus;
        DeviceProfileStatus.LinkStatus userLinkStatus = status.userLinkStatus;
        DeviceErrorState deviceErrorState = getErrorState(deviceLinkStatus, userLinkStatus);
        if (deviceErrorState != DeviceErrorState.NONE) {
            throw new CargoExtensions.SingleDeviceCheckFailedException(deviceErrorState, BandServiceMessage.Response.DEVICE_STATE_ERROR);
        }
    }

    private DeviceErrorState getErrorState(DeviceProfileStatus.LinkStatus deviceLinkStatus, DeviceProfileStatus.LinkStatus userLinkStatus) {
        DeviceErrorState deviceErrorState = DeviceErrorState.DEVICE_NOT_OWNED;
        FreStatus freStatus = this.mSettingsProvider.getFreStatus();
        if (deviceLinkStatus == DeviceProfileStatus.LinkStatus.EMPTY) {
            if (userLinkStatus == DeviceProfileStatus.LinkStatus.EMPTY) {
                if (freStatus == FreStatus.SHOWN) {
                    return DeviceErrorState.USER_HAS_NO_DEVICE;
                }
                return DeviceErrorState.NONE;
            } else if (freStatus == FreStatus.SHOWN) {
                return DeviceErrorState.USER_HAS_NO_DEVICE_AND_DEVICE_IN_USE;
            } else {
                return DeviceErrorState.NONE;
            }
        } else if (deviceLinkStatus == DeviceProfileStatus.LinkStatus.NON_MATCHING && userLinkStatus == DeviceProfileStatus.LinkStatus.NON_MATCHING) {
            return DeviceErrorState.WRONG_DEVICE;
        } else {
            if (deviceLinkStatus == DeviceProfileStatus.LinkStatus.MATCHING && userLinkStatus == DeviceProfileStatus.LinkStatus.MATCHING) {
                return DeviceErrorState.NONE;
            }
            return deviceErrorState;
        }
    }
}
