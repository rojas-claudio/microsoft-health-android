package com.microsoft.band.device;

import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import java.io.Serializable;
/* loaded from: classes.dex */
public class FirmwareVersions implements Serializable {
    public static final int FIRMWARE_VERSIONS_STRUCTURE_LENGTH = 57;
    private static final long serialVersionUID = 0;
    private FWVersion mApplicationVersion;
    private FWVersion mBootloaderVersion;
    private FWVersion mUpdaterVersion;

    public FirmwareVersions(FWVersion[] versions) throws CargoException {
        if (versions == null || versions.length != 3) {
            throw new CargoException("Invalid amount of apps found on device", BandServiceMessage.Response.DEVICE_FIRMWARE_VERSION_INCOMPATIBLE_ERROR);
        }
        for (FWVersion fwVersion : versions) {
            String appName = fwVersion.getAppName();
            if ("1BL".equalsIgnoreCase(appName)) {
                setBootloaderVersion(fwVersion);
            } else if ("2UP".equalsIgnoreCase(appName)) {
                setUpdaterVersion(fwVersion);
            } else if ("APP".equalsIgnoreCase(appName)) {
                setApplicationVersion(fwVersion);
            } else {
                throw new CargoException("A firmware version name read from the device could not be recognized", BandServiceMessage.Response.DEVICE_FIRMWARE_VERSION_INCOMPATIBLE_ERROR);
            }
        }
    }

    public void setBootloaderVersion(FWVersion bootloaderVersion) {
        this.mBootloaderVersion = bootloaderVersion;
    }

    public void setUpdaterVersion(FWVersion updaterVersion) {
        this.mUpdaterVersion = updaterVersion;
    }

    public void setApplicationVersion(FWVersion applicationVersion) {
        this.mApplicationVersion = applicationVersion;
    }

    public FWVersion getBootloaderVersion() {
        return this.mBootloaderVersion;
    }

    public FWVersion getUpdaterVersion() {
        return this.mUpdaterVersion;
    }

    public FWVersion getApplicationVersion() {
        return this.mApplicationVersion;
    }

    public String toString() {
        String result = this.mBootloaderVersion != null ? "" + this.mBootloaderVersion.toString() : "";
        if (this.mUpdaterVersion != null) {
            result = result + this.mUpdaterVersion.toString();
        }
        if (this.mApplicationVersion != null) {
            return result + this.mApplicationVersion.toString();
        }
        return result;
    }
}
