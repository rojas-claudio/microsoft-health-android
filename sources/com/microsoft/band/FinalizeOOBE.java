package com.microsoft.band;

import com.microsoft.band.internal.BandDeviceConstants;
/* loaded from: classes.dex */
public final class FinalizeOOBE {
    public static final int NOT_IN_OOBE = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 0, true);
    public static final int INVALID_STAGE = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 1, true);
    public static final int UNABLE_TO_ADVANCE_STAGE = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 2, true);
    public static final int SYSTEM_SETTINGS_SAVE_FAILED = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 3, true);
    public static final int FILE_VERSION_MISMATCH = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 4, true);
    public static final int TIME_NOT_SET = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 5, true);
    public static final int TIME_ZONE_INVALID = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 6, true);
    public static final int TIME_ZONE_FILE_MISSING = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 7, true);
    public static final int PROFILE_FILE_MISSING = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 8, true);
    public static final int EPHEMERIS_FILE_MISSING = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 9, true);
    public static final int SMS_STRINGS_FILE_MISSING = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 10, true);
    public static final int APP_LIST_MISSING = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 11, true);
    public static final int PROFILE_LOCALE_INVALID = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 12, true);
    public static final int PROFILE_LANGUAGE_INVALID = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 13, true);
    public static final int PROFILE_UNIT_INVALID = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 14, true);
    public static final int RESET_FAILED = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 15, true);
    public static final int STARTPAIRING_IPHONE_FAILED = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 16, true);
    public static final int STARTPAIRING_WINPHONE_FAILED = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 17, true);
    public static final int STARTPAIRING_ANDROID_FAILED = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 18, true);
    public static final int RETRYPAIRING_FAILED = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 19, true);
    public static final int CANCELPAIRING_FAILED = BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.MODULE_OOBE, 20, true);

    private FinalizeOOBE() {
    }

    public static boolean isError(int result) {
        return BandDeviceConstants.isResultSevere(result);
    }
}
