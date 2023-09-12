package com.microsoft.band.cloud;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import com.microsoft.band.client.BaseCargoException;
import com.microsoft.band.client.CargoDateFormat;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.client.CargoLanguage;
import com.microsoft.band.client.CargoLocation;
import com.microsoft.band.client.CargoTimeFormat;
import com.microsoft.band.client.UnitType;
import com.microsoft.band.device.DeviceProfileInfo;
import com.microsoft.band.device.DeviceSettings;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class UserProfileInfo extends CloudJSONDataModel {
    private static final String JSON_KEY_ADDITIONAL_SETTINGS = "AdditionalSettings";
    private static final String JSON_KEY_ALL_PAIRED_DEVICES = "AllDeviceSettings";
    private static final String JSON_KEY_APPLICATION_SETTINGS = "ApplicationSettings";
    private static final String JSON_KEY_CONNECTED_DEVICE_ID = "DeviceId";
    private static final String JSON_KEY_CONNECTED_DEVICE_IS_BAND = "IsBand";
    private static final String JSON_KEY_CONNECTED_DEVICE_NAME = "DeviceMetadataHint";
    private static final String JSON_KEY_CREATED_ON = "CreatedOn";
    private static final String JSON_KEY_DATE_OF_BIRTH = "DateOfBirth";
    private static final String JSON_KEY_DEFAULT_LOCALE = "DefaultLocale";
    private static final String JSON_KEY_DEVICE_ID = "DeviceId";
    private static final String JSON_KEY_DEVICE_SETTINGS = "DeviceSettings";
    private static final String JSON_KEY_EMAIL = "EmailAddress";
    private static final String JSON_KEY_FIRMWARE_BYTE_ARRAY = "FirmwareByteArray";
    private static final String JSON_KEY_FIRMWARE_DEVICE_NAME = "FirmwareDeviceName";
    private static final String JSON_KEY_FIRMWARE_LOCALE = "FirmwareLocale";
    private static final String JSON_KEY_FIRMWARE_PROFILE_VERSION = "FirmwareProfileVersion";
    private static final String JSON_KEY_GENDER = "Gender";
    private static final String JSON_KEY_HAS_COMPLETED_OOBE = "HasCompletedOOBE";
    private static final String JSON_KEY_HEIGHT = "HeightInMM";
    private static final String JSON_KEY_LAST_KDK_SYNC_UPDATE_ON = "LastKDKSyncUpdateOn";
    private static final String JSON_KEY_LAST_MODIFIED_ON = "LastModifiedOn";
    private static final String JSON_KEY_LAST_USER_UPDATE_ON = "LastUserUpdateOn";
    private static final String JSON_KEY_LFS_USER_ID = "LFSUserID";
    private static final String JSON_KEY_LOCALE_DATE_FORMAT = "DateFormat";
    private static final String JSON_KEY_LOCALE_DATE_SEPARATOR = "DateSeparator";
    private static final String JSON_KEY_LOCALE_DECIMAL_SEPERATOR = "DecimalSeparator";
    private static final String JSON_KEY_LOCALE_DISTANCE_UNIT = "DisplayDistanceUnit";
    private static final String JSON_KEY_LOCALE_ENERGY_UNIT = "DisplayCaloriesUnit";
    private static final String JSON_KEY_LOCALE_LANGUAGE = "Language";
    private static final String JSON_KEY_LOCALE_LOCALE = "Locale";
    private static final String JSON_KEY_LOCALE_LOCALE_NAME = "LocaleName";
    private static final String JSON_KEY_LOCALE_NUMBER_SEPERATOR = "NumberSeparator";
    private static final String JSON_KEY_LOCALE_SIZE_UNIT = "DisplaySizeUnit";
    private static final String JSON_KEY_LOCALE_TEMPURATURE_UNIT = "DisplayTemperatureUnit";
    private static final String JSON_KEY_LOCALE_TIME_FORMAT = "TimeFormat";
    private static final String JSON_KEY_LOCALE_VOLUME_UNIT = "DisplayVolumeUnit";
    private static final String JSON_KEY_LOCALE_WEIGHT_UNIT = "DisplayWeightUnit";
    private static final String JSON_KEY_MAX_HR = "MaxHR";
    private static final String JSON_KEY_NAME_FIRST = "FirstName";
    private static final String JSON_KEY_NAME_LAST = "LastName";
    private static final String JSON_KEY_ODS_USER_ID = "ODSUserID";
    private static final String JSON_KEY_PAIRED_DEVICE_ID = "PairedDeviceId";
    private static final String JSON_KEY_PREFERRED_LOCALE = "PreferredLocale";
    private static final String JSON_KEY_PREFERRED_REGION = "PreferredRegion";
    private static final String JSON_KEY_RESTING_HR = "RestingHR";
    private static final String JSON_KEY_RUN_DISPLAY_UNITS = "RunDisplayUnits";
    private static final String JSON_KEY_SERIAL_NUMBER = "SerialNumber";
    private static final String JSON_KEY_SMS_ADDRESS = "SmsAddress";
    private static final String JSON_KEY_TELEMETRY_ENABLED = "IsTelemetryEnabled";
    private static final String JSON_KEY_THIRD_PARTY_PARTNERS_PORTAL_ENDPOINT = "ThirdPartyPartnersPortalEndpoint";
    private static final String JSON_KEY_WEIGHT_IN_GRAMS = "WeightInGrams";
    private static final String JSON_KEY_ZIPCODE = "ZipCode";
    private static final long serialVersionUID = 1;
    private JSONObject mAdditionalSettingsDictionary;
    private UUID mAppPairingDeviceID;
    private String mBirthdate;
    private UnitType mCaloriesUnit;
    private ArrayList<DeviceSettings> mConnectedDevices;
    private String mCreatedOn;
    private CargoDateFormat mDateFormat;
    private char mDateSeparator;
    private char mDecimalSeparator;
    private String mDefaultLocale;
    private UnitType mDistanceUnit;
    private String mEmailAddress;
    private String mFirmwareByteArray;
    private String mFirmwareDeviceName;
    private int mFirmwareProfileVersion;
    private String mFirstName;
    private Gender mGender;
    private Boolean mHasCompletedOOBE;
    private int mHeightInMM;
    private String mLFSuserID;
    private CargoLanguage mLanguage;
    private String mLastKDKSyncUpdateOn;
    private String mLastModifiedOn;
    private String mLastName;
    private String mLastUserUpdateOn;
    private String mLocaleName;
    private CargoLocation mLocation;
    private int mMaxHR;
    private char mNumberSeparator;
    private UUID mODSUserID;
    private String mPreferredLocale;
    private String mPreferredRegion;
    private int mRestingHR;
    private RunDisplayUnits mRunDisplayUnits;
    private String mSerialNumber;
    private UnitType mSizeUnit;
    private String mSmsAddress;
    private Boolean mTelemetryEnabled;
    private UnitType mTemperatureUnit;
    private String mThirdPartyPartnersPortalEndpoint;
    private CargoTimeFormat mTimeFormat;
    private UnitType mVolumeUnit;
    private int mWeightInGrams;
    private UnitType mWeightUnit;
    private String mZipCode;
    private static final String TAG = UserProfileInfo.class.getSimpleName();
    public static UUID EMPTY_DEVICE_ID = new UUID(0, 0);
    public static final Parcelable.Creator<UserProfileInfo> CREATOR = new Parcelable.Creator<UserProfileInfo>() { // from class: com.microsoft.band.cloud.UserProfileInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserProfileInfo createFromParcel(Parcel in) {
            return new UserProfileInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserProfileInfo[] newArray(int size) {
            return new UserProfileInfo[size];
        }
    };

    public UserProfileInfo() {
        this.mFirmwareProfileVersion = 1;
    }

    public UserProfileInfo(DeviceProfileInfo dProfile) {
        this.mFirmwareProfileVersion = 1;
        updateUsingDeviceUserProfile(dProfile);
    }

    public void updateUsingDeviceUserProfile(DeviceProfileInfo dProfile) {
        setLastKDKSyncUpdateOn(dProfile.getTimeStampUTC().getTime());
        this.mFirmwareProfileVersion = dProfile.getVersion();
        this.mBirthdate = getCloudTimeStringFromDate(dProfile.getBirthday());
        this.mWeightInGrams = dProfile.getWeightInGrams();
        this.mHeightInMM = dProfile.getHeightInMM();
        this.mGender = dProfile.getGender();
        this.mRunDisplayUnits = dProfile.getRunDisplayUnits();
        this.mTelemetryEnabled = Boolean.valueOf(dProfile.isTelemetryEnabled());
        this.mLocaleName = dProfile.getLocaleName();
        this.mLocation = dProfile.getLocation();
        this.mLanguage = dProfile.getLanguage();
        this.mDateSeparator = dProfile.getDateSeparator();
        this.mNumberSeparator = dProfile.getNumberSeparator();
        this.mDecimalSeparator = dProfile.getDecimalSeparator();
        this.mTimeFormat = dProfile.getTimeFormat();
        this.mDateFormat = dProfile.getDateFormat();
        this.mSizeUnit = dProfile.getDisplaySizeUnit();
        this.mDistanceUnit = dProfile.getDisplayDistanceUnit();
        this.mWeightUnit = dProfile.getDisplayWeightUnit();
        this.mVolumeUnit = dProfile.getDisplayVolumeUnit();
        this.mCaloriesUnit = dProfile.getDisplayCaloriesUnit();
        this.mTemperatureUnit = dProfile.getDisplayTemperatureUnit();
        this.mFirmwareDeviceName = dProfile.getDeviceName();
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    public String toJSONString() throws JSONException {
        JSONObject profile = new JSONObject();
        JSONObject aSettings = new JSONObject();
        aSettings.put(JSON_KEY_PAIRED_DEVICE_ID, this.mAppPairingDeviceID != null ? this.mAppPairingDeviceID.toString() : null);
        aSettings.put(JSON_KEY_ADDITIONAL_SETTINGS, checkIfEmpty(this.mAdditionalSettingsDictionary));
        aSettings.put(JSON_KEY_THIRD_PARTY_PARTNERS_PORTAL_ENDPOINT, this.mThirdPartyPartnersPortalEndpoint);
        aSettings.put(JSON_KEY_PREFERRED_LOCALE, this.mPreferredLocale);
        aSettings.put(JSON_KEY_PREFERRED_REGION, this.mPreferredRegion);
        profile.put(JSON_KEY_APPLICATION_SETTINGS, checkIfEmpty(aSettings));
        profile.put(JSON_KEY_DATE_OF_BIRTH, this.mBirthdate);
        profile.put(JSON_KEY_CREATED_ON, this.mCreatedOn);
        profile.put(JSON_KEY_LAST_KDK_SYNC_UPDATE_ON, this.mLastKDKSyncUpdateOn);
        profile.put(JSON_KEY_DEFAULT_LOCALE, this.mDefaultLocale);
        if (this.mConnectedDevices != null && !this.mConnectedDevices.isEmpty()) {
            JSONObject connectedDevices = new JSONObject();
            Iterator i$ = this.mConnectedDevices.iterator();
            while (i$.hasNext()) {
                DeviceSettings device = i$.next();
                JSONObject deviceJson = new JSONObject();
                if (device.getDeviceId() != null) {
                    deviceJson.put("DeviceId", device.getDeviceId());
                    if (device.getName() != null && device.getName().length() != 0) {
                        deviceJson.put(JSON_KEY_CONNECTED_DEVICE_NAME, device.getName());
                    }
                    deviceJson.put(JSON_KEY_CONNECTED_DEVICE_IS_BAND, device.getIsBand());
                    connectedDevices.put(device.getDeviceId(), deviceJson);
                }
            }
            profile.put(JSON_KEY_ALL_PAIRED_DEVICES, connectedDevices);
        }
        JSONObject dSettings = new JSONObject();
        dSettings.put("DeviceId", EMPTY_DEVICE_ID.toString());
        dSettings.put(JSON_KEY_SERIAL_NUMBER, this.mSerialNumber);
        dSettings.put(JSON_KEY_FIRMWARE_BYTE_ARRAY, getFirmwareByteArrayAsBase64String());
        dSettings.put(JSON_KEY_FIRMWARE_DEVICE_NAME, this.mFirmwareDeviceName);
        JSONObject localeSettings = new JSONObject();
        localeSettings.put(JSON_KEY_LOCALE_DATE_FORMAT, this.mDateFormat != null ? Integer.valueOf(this.mDateFormat.getValue()) : null);
        localeSettings.put(JSON_KEY_LOCALE_DATE_SEPARATOR, accountForCharacterNotInitialized(this.mDateSeparator));
        localeSettings.put(JSON_KEY_LOCALE_DECIMAL_SEPERATOR, accountForCharacterNotInitialized(this.mDecimalSeparator));
        localeSettings.put(JSON_KEY_LOCALE_LANGUAGE, this.mLanguage != null ? Integer.valueOf(this.mLanguage.getValue()) : null);
        localeSettings.put(JSON_KEY_LOCALE_LOCALE, this.mLocation != null ? Integer.valueOf(this.mLocation.getValue()) : null);
        localeSettings.put(JSON_KEY_LOCALE_LOCALE_NAME, this.mLocaleName);
        localeSettings.put(JSON_KEY_LOCALE_NUMBER_SEPERATOR, accountForCharacterNotInitialized(this.mNumberSeparator));
        localeSettings.put(JSON_KEY_LOCALE_TIME_FORMAT, this.mTimeFormat != null ? Integer.valueOf(this.mTimeFormat.getValue()) : null);
        localeSettings.put(JSON_KEY_LOCALE_SIZE_UNIT, this.mSizeUnit != null ? Integer.valueOf(this.mSizeUnit.getValue()) : null);
        localeSettings.put(JSON_KEY_LOCALE_DISTANCE_UNIT, this.mDistanceUnit != null ? Integer.valueOf(this.mDistanceUnit.getValue()) : null);
        localeSettings.put(JSON_KEY_LOCALE_WEIGHT_UNIT, this.mWeightUnit != null ? Integer.valueOf(this.mWeightUnit.getValue()) : null);
        localeSettings.put(JSON_KEY_LOCALE_VOLUME_UNIT, this.mVolumeUnit != null ? Integer.valueOf(this.mVolumeUnit.getValue()) : null);
        localeSettings.put(JSON_KEY_LOCALE_ENERGY_UNIT, this.mCaloriesUnit != null ? Integer.valueOf(this.mCaloriesUnit.getValue()) : null);
        localeSettings.put(JSON_KEY_LOCALE_TEMPURATURE_UNIT, this.mTemperatureUnit != null ? Integer.valueOf(this.mTemperatureUnit.getValue()) : null);
        dSettings.put(JSON_KEY_FIRMWARE_LOCALE, checkIfEmpty(localeSettings));
        dSettings.put(JSON_KEY_FIRMWARE_PROFILE_VERSION, checkNotDefault(this.mFirmwareProfileVersion));
        dSettings.put(JSON_KEY_RUN_DISPLAY_UNITS, this.mRunDisplayUnits != null ? Integer.valueOf(this.mRunDisplayUnits.getIndex()) : null);
        dSettings.put(JSON_KEY_TELEMETRY_ENABLED, this.mTelemetryEnabled != null ? Boolean.valueOf(this.mTelemetryEnabled.booleanValue()) : null);
        profile.put(JSON_KEY_DEVICE_SETTINGS, checkIfEmpty(dSettings));
        profile.put(JSON_KEY_EMAIL, this.mEmailAddress);
        profile.put(JSON_KEY_NAME_FIRST, this.mFirstName);
        profile.put("Gender", this.mGender != null ? Integer.valueOf(this.mGender.getIndex()) : null);
        profile.put(JSON_KEY_HEIGHT, checkNotDefault(this.mHeightInMM));
        profile.put(JSON_KEY_LAST_MODIFIED_ON, this.mLastModifiedOn);
        profile.put(JSON_KEY_NAME_LAST, this.mLastName);
        profile.put(JSON_KEY_LAST_USER_UPDATE_ON, this.mLastUserUpdateOn);
        profile.put(JSON_KEY_LFS_USER_ID, this.mLFSuserID);
        profile.put(JSON_KEY_MAX_HR, checkNotDefault(this.mMaxHR));
        profile.put(JSON_KEY_ODS_USER_ID, this.mODSUserID);
        profile.put(JSON_KEY_RESTING_HR, checkNotDefault(this.mRestingHR));
        profile.put(JSON_KEY_SMS_ADDRESS, this.mSmsAddress);
        profile.put(JSON_KEY_WEIGHT_IN_GRAMS, checkNotDefault(this.mWeightInGrams));
        profile.put(JSON_KEY_ZIPCODE, this.mZipCode);
        profile.put(JSON_KEY_HAS_COMPLETED_OOBE, this.mHasCompletedOOBE != null ? Boolean.valueOf(this.mHasCompletedOOBE.booleanValue()) : null);
        String json = profile.toString();
        return json;
    }

    private Integer checkNotDefault(int i) {
        if (i == 0) {
            return null;
        }
        return Integer.valueOf(i);
    }

    private String accountForCharacterNotInitialized(char c) {
        if (c == 0) {
            return null;
        }
        return Character.toString(c);
    }

    private JSONObject checkIfEmpty(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.length() == 0) {
            return null;
        }
        return jsonObject;
    }

    public static UserProfileInfo getCloudProfileFromJson(String data) throws CargoException {
        UserProfileInfo profile = new UserProfileInfo();
        profile.initWithJSONString(data);
        return profile;
    }

    UserProfileInfo(Parcel in) {
        super(in);
        this.mFirmwareProfileVersion = 1;
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    protected void initWithJSONObject(JSONObject json) throws CargoException, IllegalArgumentException {
        IllegalArgumentException illegalError = null;
        try {
            if (json.has(JSON_KEY_APPLICATION_SETTINGS)) {
                JSONObject asJSON = new JSONObject(json.getString(JSON_KEY_APPLICATION_SETTINGS));
                if (asJSON.has(JSON_KEY_PAIRED_DEVICE_ID)) {
                    setAppPairingDeviceID(UUID.fromString(asJSON.getString(JSON_KEY_PAIRED_DEVICE_ID)));
                }
                if (asJSON.has(JSON_KEY_ADDITIONAL_SETTINGS)) {
                    this.mAdditionalSettingsDictionary = asJSON.getJSONObject(JSON_KEY_ADDITIONAL_SETTINGS);
                }
                if (asJSON.has(JSON_KEY_THIRD_PARTY_PARTNERS_PORTAL_ENDPOINT)) {
                    this.mThirdPartyPartnersPortalEndpoint = asJSON.getString(JSON_KEY_THIRD_PARTY_PARTNERS_PORTAL_ENDPOINT);
                }
                if (asJSON.has(JSON_KEY_PREFERRED_LOCALE)) {
                    setPreferredLocale(asJSON.getString(JSON_KEY_PREFERRED_LOCALE));
                }
                if (asJSON.has(JSON_KEY_PREFERRED_REGION)) {
                    setPreferredRegion(asJSON.getString(JSON_KEY_PREFERRED_REGION));
                }
            }
            if (json.has(JSON_KEY_ALL_PAIRED_DEVICES)) {
                JSONObject devices = json.getJSONObject(JSON_KEY_ALL_PAIRED_DEVICES);
                Iterator<String> keys = devices.keys();
                this.mConnectedDevices = new ArrayList<>();
                while (keys.hasNext()) {
                    JSONObject device = (JSONObject) devices.get(keys.next());
                    String deviceName = "";
                    if (device.has("DeviceId")) {
                        String deviceId = device.getString("DeviceId");
                        if (device.has(JSON_KEY_CONNECTED_DEVICE_NAME)) {
                            deviceName = device.getString(JSON_KEY_CONNECTED_DEVICE_NAME);
                        }
                        boolean isBand = false;
                        if (device.has(JSON_KEY_CONNECTED_DEVICE_IS_BAND)) {
                            isBand = device.getBoolean(JSON_KEY_CONNECTED_DEVICE_IS_BAND);
                        }
                        this.mConnectedDevices.add(new DeviceSettings(deviceName, deviceId.toUpperCase(Locale.US), isBand));
                    }
                }
            }
            if (json.has(JSON_KEY_DATE_OF_BIRTH)) {
                setBirthdate(json.getString(JSON_KEY_DATE_OF_BIRTH));
            }
            if (json.has(JSON_KEY_CREATED_ON)) {
                setCreatedOn(json.getString(JSON_KEY_CREATED_ON));
            }
            if (json.has(JSON_KEY_LAST_KDK_SYNC_UPDATE_ON)) {
                setLastKDKSyncUpdateOn(json.getString(JSON_KEY_LAST_KDK_SYNC_UPDATE_ON));
            }
            if (json.has(JSON_KEY_DEFAULT_LOCALE)) {
                setDefaultLocale(json.getString(JSON_KEY_DEFAULT_LOCALE));
            }
            if (json.has(JSON_KEY_DEVICE_SETTINGS)) {
                JSONObject dsJSON = new JSONObject(json.getString(JSON_KEY_DEVICE_SETTINGS));
                if (dsJSON.has(JSON_KEY_SERIAL_NUMBER)) {
                    setSerialNumber(dsJSON.getString(JSON_KEY_SERIAL_NUMBER));
                }
                if (dsJSON.has(JSON_KEY_FIRMWARE_BYTE_ARRAY)) {
                    setFirmwareByteArrayFromBase64String(dsJSON.getString(JSON_KEY_FIRMWARE_BYTE_ARRAY));
                }
                if (dsJSON.has(JSON_KEY_FIRMWARE_DEVICE_NAME)) {
                    setFirmwareDeviceName(dsJSON.getString(JSON_KEY_FIRMWARE_DEVICE_NAME));
                }
                if (dsJSON.has(JSON_KEY_FIRMWARE_LOCALE)) {
                    JSONObject localeJSON = new JSONObject(dsJSON.getString(JSON_KEY_FIRMWARE_LOCALE));
                    if (localeJSON.has(JSON_KEY_LOCALE_DATE_FORMAT)) {
                        setDateFormat(CargoDateFormat.lookup(localeJSON.getInt(JSON_KEY_LOCALE_DATE_FORMAT)));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_DATE_SEPARATOR)) {
                        setDateSeparator(localeJSON.getString(JSON_KEY_LOCALE_DATE_SEPARATOR).charAt(0));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_DECIMAL_SEPERATOR)) {
                        setDecimalSeparator(localeJSON.getString(JSON_KEY_LOCALE_DECIMAL_SEPERATOR).charAt(0));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_LANGUAGE)) {
                        setLanguage(CargoLanguage.lookup(localeJSON.getInt(JSON_KEY_LOCALE_LANGUAGE)));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_LOCALE)) {
                        setLocation(CargoLocation.lookup(localeJSON.getInt(JSON_KEY_LOCALE_LOCALE)));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_LOCALE_NAME)) {
                        setLocaleName(localeJSON.getString(JSON_KEY_LOCALE_LOCALE_NAME));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_NUMBER_SEPERATOR)) {
                        setNumberSeparator(localeJSON.getString(JSON_KEY_LOCALE_NUMBER_SEPERATOR).charAt(0));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_TIME_FORMAT)) {
                        setTimeFormat(CargoTimeFormat.lookup(localeJSON.getInt(JSON_KEY_LOCALE_TIME_FORMAT)));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_SIZE_UNIT)) {
                        setDisplaySizeUnit(UnitType.lookup(localeJSON.getInt(JSON_KEY_LOCALE_SIZE_UNIT)));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_DISTANCE_UNIT)) {
                        setDisplayDistanceUnit(UnitType.lookup(localeJSON.getInt(JSON_KEY_LOCALE_DISTANCE_UNIT)));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_WEIGHT_UNIT)) {
                        setDisplayWeightUnit(UnitType.lookup(localeJSON.getInt(JSON_KEY_LOCALE_WEIGHT_UNIT)));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_VOLUME_UNIT)) {
                        setDisplayVolumeUnit(UnitType.lookup(localeJSON.getInt(JSON_KEY_LOCALE_VOLUME_UNIT)));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_ENERGY_UNIT)) {
                        setDisplayCaloriesUnit(UnitType.lookup(localeJSON.getInt(JSON_KEY_LOCALE_ENERGY_UNIT)));
                    }
                    if (localeJSON.has(JSON_KEY_LOCALE_TEMPURATURE_UNIT)) {
                        setDisplayTemperatureUnit(UnitType.lookup(localeJSON.getInt(JSON_KEY_LOCALE_TEMPURATURE_UNIT)));
                    }
                }
                if (dsJSON.has(JSON_KEY_FIRMWARE_PROFILE_VERSION)) {
                    setFirmwareProfileVersion(dsJSON.getInt(JSON_KEY_FIRMWARE_PROFILE_VERSION));
                }
                if (dsJSON.has(JSON_KEY_RUN_DISPLAY_UNITS)) {
                    setRunDisplayUnits(RunDisplayUnits.valueOf(dsJSON.getInt(JSON_KEY_RUN_DISPLAY_UNITS)));
                }
                if (dsJSON.has(JSON_KEY_TELEMETRY_ENABLED)) {
                    setTelemetryEnabled(dsJSON.getBoolean(JSON_KEY_TELEMETRY_ENABLED));
                }
            }
            if (json.has(JSON_KEY_EMAIL)) {
                setEmailAddress(json.getString(JSON_KEY_EMAIL));
            }
            if (json.has(JSON_KEY_NAME_FIRST)) {
                setFirstName(json.getString(JSON_KEY_NAME_FIRST));
            }
            if (json.has("Gender")) {
                setGender(Gender.valueOf(json.getInt("Gender")));
            }
            if (json.has(JSON_KEY_HEIGHT)) {
                try {
                    setHeightInMM(json.getInt(JSON_KEY_HEIGHT));
                } catch (IllegalArgumentException e) {
                    illegalError = e;
                }
            }
            if (json.has(JSON_KEY_LAST_MODIFIED_ON)) {
                setLastModifiedOn(json.getString(JSON_KEY_LAST_MODIFIED_ON));
            }
            if (json.has(JSON_KEY_NAME_LAST)) {
                setLastName(json.getString(JSON_KEY_NAME_LAST));
            }
            if (json.has(JSON_KEY_LAST_USER_UPDATE_ON)) {
                setLastUserUpdateOn(json.getString(JSON_KEY_LAST_USER_UPDATE_ON));
            }
            if (json.has(JSON_KEY_LFS_USER_ID)) {
                setLFSuserID(json.getString(JSON_KEY_LFS_USER_ID));
            }
            if (json.has(JSON_KEY_MAX_HR)) {
                setMaxHR(json.getInt(JSON_KEY_MAX_HR));
            }
            if (json.has(JSON_KEY_ODS_USER_ID)) {
                setODSUserID(UUID.fromString(json.getString(JSON_KEY_ODS_USER_ID)));
            }
            if (json.has(JSON_KEY_RESTING_HR)) {
                setRestingHR(json.getInt(JSON_KEY_RESTING_HR));
            }
            if (json.has(JSON_KEY_SMS_ADDRESS)) {
                setSmsAddress(json.getString(JSON_KEY_SMS_ADDRESS));
            }
            if (json.has(JSON_KEY_WEIGHT_IN_GRAMS)) {
                try {
                    setWeightInGrams(json.getInt(JSON_KEY_WEIGHT_IN_GRAMS));
                } catch (IllegalArgumentException e2) {
                    illegalError = e2;
                }
            }
            if (json.has(JSON_KEY_ZIPCODE)) {
                setZipCode(json.getString(JSON_KEY_ZIPCODE));
            }
            if (json.has(JSON_KEY_HAS_COMPLETED_OOBE)) {
                setHasCompletedOOBE(json.getBoolean(JSON_KEY_HAS_COMPLETED_OOBE));
            }
            if (illegalError != null) {
                throw illegalError;
            }
        } catch (JSONException e3) {
            KDKLog.e(TAG, e3.getMessage(), e3);
            throw new CargoException(String.format(BaseCargoException.EXCEPTION, e3.getMessage()), e3, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("CloudProfileInfo:%s", System.getProperty("line.separator")));
        if (this.mCreatedOn != null) {
            result.append(String.format("     |--CreatedOn= %s", this.mCreatedOn)).append(System.getProperty("line.separator"));
        }
        if (this.mLastModifiedOn != null) {
            result.append(String.format("     |--LastModifiedOn= %s", this.mLastModifiedOn)).append(System.getProperty("line.separator"));
        }
        if (this.mLastUserUpdateOn != null) {
            result.append(String.format("     |--LastUserUpdateOn= %s", this.mLastUserUpdateOn)).append(System.getProperty("line.separator"));
        }
        if (this.mFirstName != null) {
            result.append(String.format("     |--First Name= %s %s", this.mFirstName, System.getProperty("line.separator")));
        }
        if (this.mLastName != null) {
            result.append(String.format("     |--Last Name= %s %s", this.mLastName, System.getProperty("line.separator")));
        }
        if (this.mEmailAddress != null) {
            result.append(String.format("     |--Email= %s %s", this.mEmailAddress, System.getProperty("line.separator")));
        }
        if (this.mSmsAddress != null) {
            result.append(String.format("     |--SMS Address= %s %s", this.mSmsAddress, System.getProperty("line.separator")));
        }
        if (this.mZipCode != null) {
            result.append(String.format("     |--ZIP Code= %s %s", this.mZipCode, System.getProperty("line.separator")));
        }
        if (this.mPreferredLocale != null) {
            result.append(String.format("     |--PreferredLocale= %s %s", this.mPreferredLocale, System.getProperty("line.separator")));
        }
        if (this.mPreferredRegion != null) {
            result.append(String.format("     |--PreferredRegion= %s %s", this.mPreferredRegion, System.getProperty("line.separator")));
        }
        if (this.mHeightInMM != 0) {
            result.append(String.format("     |--HeightInMM= %d ", Integer.valueOf(this.mHeightInMM))).append(System.getProperty("line.separator"));
        }
        if (this.mWeightInGrams != 0) {
            result.append(String.format("     |--WeightInGram= %d", Integer.valueOf(this.mWeightInGrams))).append(System.getProperty("line.separator"));
        }
        if (this.mRestingHR != 0) {
            result.append(String.format("     |--RestingHeartRate= %d", Integer.valueOf(this.mRestingHR))).append(System.getProperty("line.separator"));
        }
        if (this.mMaxHR != 0) {
            result.append(String.format("     |--MaxHeartRate= %d", Integer.valueOf(this.mMaxHR))).append(System.getProperty("line.separator"));
        }
        if (this.mGender != null) {
            result.append(String.format("     |--Gender= %s", this.mGender)).append(System.getProperty("line.separator"));
        }
        if (this.mBirthdate != null) {
            result.append(String.format("     |--Birthdate= %s", this.mBirthdate)).append(System.getProperty("line.separator"));
        }
        if (this.mRunDisplayUnits != null) {
            result.append(String.format("     |--RunDisplayUnits= %s", this.mRunDisplayUnits)).append(System.getProperty("line.separator"));
        }
        if (this.mTelemetryEnabled != null) {
            result.append(String.format("     |--Is Telemetry Enabled= %s", Boolean.valueOf(this.mTelemetryEnabled.booleanValue()))).append(System.getProperty("line.separator"));
        }
        if (this.mHasCompletedOOBE != null) {
            result.append(String.format("     |--Has Completed OOBE= %s", Boolean.valueOf(this.mHasCompletedOOBE.booleanValue()))).append(System.getProperty("line.separator"));
        }
        if (this.mFirmwareDeviceName != null) {
            result.append(String.format("     |--Device Name= %s %s", this.mFirmwareDeviceName, System.getProperty("line.separator")));
        }
        if (this.mFirmwareByteArray != null) {
            result.append(String.format("     |--Device byte array= %s %s", this.mFirmwareByteArray, System.getProperty("line.separator")));
        }
        if (this.mODSUserID != null) {
            result.append(String.format("     |--Profile Id= %s %s", this.mODSUserID, System.getProperty("line.separator")));
        }
        if (this.mAppPairingDeviceID != null) {
            result.append(String.format("     |--App Paired Device Id= %s %s", this.mAppPairingDeviceID, System.getProperty("line.separator")));
        }
        if (this.mFirmwareProfileVersion != 0) {
            result.append(String.format("     |--Firmware Profile Version= %d", Integer.valueOf(this.mFirmwareProfileVersion))).append(System.getProperty("line.separator"));
        }
        if (this.mDateSeparator != 0) {
            result.append(String.format("     |--Date Separator= %c %s", Character.valueOf(this.mDateSeparator), System.getProperty("line.separator")));
        }
        if (this.mDecimalSeparator != 0) {
            result.append(String.format("     |--Decimal Separator= %c %s", Character.valueOf(this.mDecimalSeparator), System.getProperty("line.separator")));
        }
        if (this.mNumberSeparator != 0) {
            result.append(String.format("     |--Number Separator= %c %s", Character.valueOf(this.mNumberSeparator), System.getProperty("line.separator")));
        }
        if (this.mDefaultLocale != null) {
            result.append(String.format("     |--Default Locale= %s %s", this.mDefaultLocale, System.getProperty("line.separator")));
        }
        if (this.mLocaleName != null) {
            result.append(String.format("     |--Locale Name= %s %s", this.mLocaleName, System.getProperty("line.separator")));
        }
        if (this.mLanguage != null) {
            result.append(String.format("     |--Language= %s %s", this.mLanguage, System.getProperty("line.separator")));
        }
        if (this.mLocation != null) {
            result.append(String.format("     |--Location= %s %s", this.mLocation, System.getProperty("line.separator")));
        }
        if (this.mTimeFormat != null) {
            result.append(String.format("     |--Time Format= %s %s", this.mTimeFormat, System.getProperty("line.separator")));
        }
        if (this.mDateFormat != null) {
            result.append(String.format("     |--Date Format= %s %s", this.mDateFormat, System.getProperty("line.separator")));
        }
        if (this.mSizeUnit != null) {
            result.append(String.format("     |--Size Unit= %s %s", this.mSizeUnit, System.getProperty("line.separator")));
        }
        if (this.mDistanceUnit != null) {
            result.append(String.format("     |--Distance Unit= %s %s", this.mDistanceUnit, System.getProperty("line.separator")));
        }
        if (this.mWeightUnit != null) {
            result.append(String.format("     |--Weight Unit= %s %s", this.mWeightUnit, System.getProperty("line.separator")));
        }
        if (this.mVolumeUnit != null) {
            result.append(String.format("     |--Volume Unit= %s %s", this.mVolumeUnit, System.getProperty("line.separator")));
        }
        if (this.mCaloriesUnit != null) {
            result.append(String.format("     |--Energy Unit= %s %s", this.mCaloriesUnit, System.getProperty("line.separator")));
        }
        if (this.mTemperatureUnit != null) {
            result.append(String.format("     |--Tempurature Unit= %s %s", this.mTemperatureUnit, System.getProperty("line.separator")));
        }
        return result.toString();
    }

    public String getThirdPartyPartnersPortalEndpoint() {
        return this.mThirdPartyPartnersPortalEndpoint;
    }

    public Date getBirthdate() {
        if (this.mBirthdate == null) {
            return null;
        }
        try {
            return getDateFromCloudTime(this.mBirthdate);
        } catch (ParseException e) {
            KDKLog.e(TAG, e.getLocalizedMessage(), e);
            return null;
        }
    }

    public UserProfileInfo setBirthdate(Date birthdate) {
        this.mBirthdate = getCloudTimeStringFromDate(birthdate);
        return this;
    }

    protected void setBirthdate(String birthdate) {
        this.mBirthdate = birthdate;
    }

    public Date getCreatedOn() {
        if (this.mCreatedOn == null) {
            return null;
        }
        try {
            return getDateFromCloudTime(this.mCreatedOn);
        } catch (ParseException e) {
            KDKLog.e(TAG, e.getLocalizedMessage(), e);
            return null;
        }
    }

    protected void setCreatedOn(String createdOn) {
        this.mCreatedOn = createdOn;
    }

    public Date getLastKDKSyncUpdateOn() {
        if (this.mLastKDKSyncUpdateOn == null) {
            return null;
        }
        try {
            return getDateFromCloudTime(this.mLastKDKSyncUpdateOn);
        } catch (ParseException e) {
            KDKLog.e(TAG, e.getLocalizedMessage(), e);
            return null;
        }
    }

    public void setLastKDKSyncUpdateOn(long lastKDKSyncUpdateOn) {
        this.mLastKDKSyncUpdateOn = getCloudTimeStringFromDate(new Date(lastKDKSyncUpdateOn));
    }

    protected void setLastKDKSyncUpdateOn(String lastKDKSyncUpdateOn) {
        this.mLastKDKSyncUpdateOn = lastKDKSyncUpdateOn;
    }

    public String getDefaultLocale() {
        return this.mDefaultLocale;
    }

    public UserProfileInfo setDefaultLocale(String defaultLocale) {
        this.mDefaultLocale = defaultLocale;
        return this;
    }

    public String getEmailAddress() {
        return this.mEmailAddress;
    }

    public UserProfileInfo setEmailAddress(String emailAddress) {
        this.mEmailAddress = emailAddress;
        return this;
    }

    public String getFirstName() {
        return this.mFirstName;
    }

    public UserProfileInfo setFirstName(String firstName) {
        this.mFirstName = firstName;
        return this;
    }

    public Gender getGender() {
        return this.mGender;
    }

    public UserProfileInfo setGender(Gender gender) {
        this.mGender = gender;
        return this;
    }

    public int getHeightInMM() {
        return this.mHeightInMM;
    }

    public UserProfileInfo setHeightInMM(int heightInMM) {
        if (heightInMM >= 1000 && heightInMM <= 2500) {
            this.mHeightInMM = heightInMM;
            return this;
        }
        throw new IllegalArgumentException("Height must be between 1000 and 2500");
    }

    public Date getLastModifiedOn() {
        if (this.mLastModifiedOn == null) {
            return null;
        }
        try {
            return getDateFromCloudTime(this.mLastModifiedOn);
        } catch (ParseException e) {
            KDKLog.e(TAG, e.getLocalizedMessage(), e);
            return null;
        }
    }

    protected void setLastModifiedOn(String lastModifiedOn) {
        this.mLastModifiedOn = lastModifiedOn;
    }

    public String getLastName() {
        return this.mLastName;
    }

    public UserProfileInfo setLastName(String lastName) {
        this.mLastName = lastName;
        return this;
    }

    public Date getLastUserUpdateOn() {
        if (this.mLastUserUpdateOn == null) {
            return null;
        }
        try {
            return getDateFromCloudTime(this.mLastUserUpdateOn);
        } catch (ParseException e) {
            KDKLog.e(TAG, e.getLocalizedMessage(), e);
            return null;
        }
    }

    protected void setLastUserUpdateOn(String lastUserUpdateOn) {
        this.mLastUserUpdateOn = lastUserUpdateOn;
    }

    public String getLFSuserID() {
        return this.mLFSuserID;
    }

    protected void setLFSuserID(String lFSuserID) {
        this.mLFSuserID = lFSuserID;
    }

    public int getMaxHR() {
        return this.mMaxHR;
    }

    public UserProfileInfo setMaxHR(int maxHR) {
        this.mMaxHR = maxHR;
        return this;
    }

    public UUID getODSUserID() {
        return this.mODSUserID;
    }

    private void setODSUserID(UUID oDSUserID) {
        this.mODSUserID = oDSUserID;
    }

    public int getRestingHR() {
        return this.mRestingHR;
    }

    public UserProfileInfo setRestingHR(int restingHR) {
        this.mRestingHR = restingHR;
        return this;
    }

    public String getSmsAddress() {
        return this.mSmsAddress;
    }

    public UserProfileInfo setSmsAddress(String smsAddress) {
        this.mSmsAddress = smsAddress;
        return this;
    }

    public int getWeightInGrams() {
        return this.mWeightInGrams;
    }

    public UserProfileInfo setWeightInGrams(int weightInGrams) {
        if (weightInGrams >= 35000 && weightInGrams <= 250000) {
            this.mWeightInGrams = weightInGrams;
            return this;
        }
        throw new IllegalArgumentException("Weight must be between 35000 and 250000");
    }

    public String getZipCode() {
        return this.mZipCode;
    }

    public UserProfileInfo setZipCode(String zipCode) {
        this.mZipCode = zipCode;
        return this;
    }

    public String getPreferredLocale() {
        return this.mPreferredLocale;
    }

    public UserProfileInfo setPreferredLocale(String preferredLocale) {
        this.mPreferredLocale = preferredLocale;
        return this;
    }

    public String getPreferredRegion() {
        return this.mPreferredRegion;
    }

    public UserProfileInfo setPreferredRegion(String preferredRegion) {
        this.mPreferredRegion = preferredRegion;
        return this;
    }

    public byte[] getFirmwareByteArray() {
        if (this.mFirmwareByteArray == null) {
            return null;
        }
        return Base64.decode(this.mFirmwareByteArray, 2);
    }

    protected String getFirmwareByteArrayAsBase64String() {
        return this.mFirmwareByteArray;
    }

    public UserProfileInfo setFirmwareByteArray(byte[] firmwareByteArray) {
        this.mFirmwareByteArray = Base64.encodeToString(firmwareByteArray, 2);
        return this;
    }

    protected void setFirmwareByteArrayFromBase64String(String firmwareByteArray) {
        this.mFirmwareByteArray = firmwareByteArray;
    }

    public String getFirmwareDeviceName() {
        return this.mFirmwareDeviceName;
    }

    public UserProfileInfo setFirmwareDeviceName(String firmwareDeviceName) {
        this.mFirmwareDeviceName = firmwareDeviceName;
        return this;
    }

    public String getSerialNumber() {
        return this.mSerialNumber;
    }

    public UserProfileInfo setSerialNumber(String serialNumber) {
        this.mSerialNumber = serialNumber;
        return this;
    }

    public RunDisplayUnits getRunDisplayUnits() {
        return this.mRunDisplayUnits;
    }

    public UserProfileInfo setRunDisplayUnits(RunDisplayUnits runDisplayUnits) {
        this.mRunDisplayUnits = runDisplayUnits;
        return this;
    }

    public Boolean isTelemetryEnabled() {
        return this.mTelemetryEnabled;
    }

    public UserProfileInfo setTelemetryEnabled(boolean telemetryEnabled) {
        this.mTelemetryEnabled = Boolean.valueOf(telemetryEnabled);
        return this;
    }

    public char getDateSeparator() {
        return this.mDateSeparator;
    }

    public UserProfileInfo setDateSeparator(char dateSeparator) {
        this.mDateSeparator = dateSeparator;
        return this;
    }

    public char getDecimalSeparator() {
        return this.mDecimalSeparator;
    }

    public UserProfileInfo setDecimalSeparator(char decimalSeparator) {
        this.mDecimalSeparator = decimalSeparator;
        return this;
    }

    public char getNumberSeparator() {
        return this.mNumberSeparator;
    }

    public UserProfileInfo setNumberSeparator(char numberSeparator) {
        this.mNumberSeparator = numberSeparator;
        return this;
    }

    public CargoLanguage getLanguage() {
        return this.mLanguage;
    }

    public UserProfileInfo setLanguage(CargoLanguage language) {
        this.mLanguage = language;
        return this;
    }

    public CargoLocation getLocation() {
        return this.mLocation;
    }

    public UserProfileInfo setLocation(CargoLocation location) {
        this.mLocation = location;
        return this;
    }

    public CargoTimeFormat getTimeFormat() {
        return this.mTimeFormat;
    }

    public UserProfileInfo setTimeFormat(CargoTimeFormat timeFormat) {
        this.mTimeFormat = timeFormat;
        return this;
    }

    public CargoDateFormat getDateFormat() {
        return this.mDateFormat;
    }

    public UserProfileInfo setDateFormat(CargoDateFormat dateFormat) {
        this.mDateFormat = dateFormat;
        return this;
    }

    public UnitType getDisplaySizeUnit() {
        return this.mSizeUnit;
    }

    public UserProfileInfo setDisplaySizeUnit(UnitType sizeUnit) {
        this.mSizeUnit = sizeUnit;
        return this;
    }

    public UnitType getDisplayDistanceUnit() {
        return this.mDistanceUnit;
    }

    public UserProfileInfo setDisplayDistanceUnit(UnitType distanceUnit) {
        this.mDistanceUnit = distanceUnit;
        return this;
    }

    public UnitType getDisplayWeightUnit() {
        return this.mWeightUnit;
    }

    public UserProfileInfo setDisplayWeightUnit(UnitType weightUnit) {
        this.mWeightUnit = weightUnit;
        return this;
    }

    public UnitType getDisplayVolumeUnit() {
        return this.mVolumeUnit;
    }

    public UserProfileInfo setDisplayVolumeUnit(UnitType volumeUnit) {
        this.mVolumeUnit = volumeUnit;
        return this;
    }

    public UnitType getDisplayCaloriesUnit() {
        return this.mCaloriesUnit;
    }

    public UserProfileInfo setDisplayCaloriesUnit(UnitType caloriesUnit) {
        this.mCaloriesUnit = caloriesUnit;
        return this;
    }

    public UnitType getDisplayTemperatureUnit() {
        return this.mTemperatureUnit;
    }

    public UserProfileInfo setDisplayTemperatureUnit(UnitType temperatureUnit) {
        this.mTemperatureUnit = temperatureUnit;
        return this;
    }

    public String getLocaleName() {
        return this.mLocaleName;
    }

    public UserProfileInfo setLocaleName(String localeName) {
        this.mLocaleName = localeName;
        return this;
    }

    public int getFirmwareProfileVersion() {
        return this.mFirmwareProfileVersion;
    }

    protected UserProfileInfo setFirmwareProfileVersion(int firmwareProfileVersion) {
        this.mFirmwareProfileVersion = firmwareProfileVersion;
        return this;
    }

    public Boolean isHasCompletedOOBE() {
        return this.mHasCompletedOOBE;
    }

    public UserProfileInfo setHasCompletedOOBE(boolean hasCompletedOOBE) {
        this.mHasCompletedOOBE = Boolean.valueOf(hasCompletedOOBE);
        return this;
    }

    public UUID getAppPairingDeviceID() {
        return this.mAppPairingDeviceID;
    }

    public UserProfileInfo setAppPairingDeviceID(UUID appPairingDeviceID) {
        this.mAppPairingDeviceID = appPairingDeviceID;
        return this;
    }

    public boolean isBandPaired() {
        return (this.mAppPairingDeviceID == null || EMPTY_DEVICE_ID.equals(this.mAppPairingDeviceID)) ? false : true;
    }

    public String getValueFromDictionary(String key) {
        Validation.validateNullParameter(key, "Key");
        if (this.mAdditionalSettingsDictionary != null) {
            try {
                return this.mAdditionalSettingsDictionary.getString(key);
            } catch (JSONException e) {
                KDKLog.e(TAG, e.getLocalizedMessage(), e);
            }
        }
        return null;
    }

    public ArrayList<DeviceSettings> getConnectedDevices() {
        return this.mConnectedDevices;
    }

    public void setConnectedDevices(ArrayList<DeviceSettings> connectedDevices) {
        this.mConnectedDevices = connectedDevices;
    }

    public void addConnectedDevice(DeviceSettings device) {
        this.mConnectedDevices.add(device);
    }

    public void addValueToDictionary(String key, String value) {
        Validation.validateNullParameter(key, "Key");
        Validation.validateNullParameter(value, TelemetryConstants.Events.ShakeDialogPreferences.Dimensions.VALUE);
        if (this.mAdditionalSettingsDictionary == null) {
            this.mAdditionalSettingsDictionary = new JSONObject();
        }
        try {
            this.mAdditionalSettingsDictionary.put(key, value);
        } catch (JSONException e) {
            KDKLog.e(TAG, e.getLocalizedMessage(), e);
        }
    }

    /* loaded from: classes.dex */
    public enum Gender {
        male(0),
        female(1);
        
        private int mIndex;

        Gender(int id) {
            this.mIndex = id;
        }

        public int getIndex() {
            return this.mIndex;
        }

        public static Gender valueOf(int id) {
            switch (id) {
                case 0:
                    return male;
                case 1:
                    return female;
                default:
                    return male;
            }
        }
    }

    /* loaded from: classes.dex */
    public enum RunDisplayUnits {
        Local(0),
        SI(1);
        
        private int mIndex;

        RunDisplayUnits(int id) {
            this.mIndex = id;
        }

        public int getIndex() {
            return this.mIndex;
        }

        public static RunDisplayUnits valueOf(int id) {
            switch (id) {
                case 0:
                    return Local;
                case 1:
                    return SI;
                default:
                    return Local;
            }
        }
    }
}
