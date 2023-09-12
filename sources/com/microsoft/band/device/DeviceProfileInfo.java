package com.microsoft.band.device;

import com.microsoft.band.client.CargoDateFormat;
import com.microsoft.band.client.CargoLanguage;
import com.microsoft.band.client.CargoLocation;
import com.microsoft.band.client.CargoTimeFormat;
import com.microsoft.band.client.UnitType;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.Validation;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;
/* loaded from: classes.dex */
public class DeviceProfileInfo implements Serializable {
    public static final int DEVICEPROFILE_ENVOY_LENGTH = 397;
    public static final int DEVICEPROFILE_STRUCTURE_LENGTH = 128;
    public static final int MAX_AGE = 110;
    public static final int MAX_HEIGHT = 2500;
    public static final int MAX_WEIGHT = 250000;
    public static final int MIN_AGE = 8;
    public static final int MIN_HEIGHT = 1000;
    public static final int MIN_WEIGHT = 35000;
    private static final long serialVersionUID = -7486325682755897991L;
    private FileTime mBirthday;
    private char[] mDeviceName = new char[16];
    private UserProfileInfo.Gender mGender;
    int mHardwareVersion;
    private int mHeightInMM;
    private LocaleSettings mLocaleSettings;
    private ProfileHeader mProfileHeader;
    private UserProfileInfo.RunDisplayUnits mRunDisplayUnits;
    private boolean mTelemetryEnabled;
    private int mWeightInGrams;

    /* JADX INFO: Access modifiers changed from: protected */
    public DeviceProfileInfo(ByteBuffer buffer, int hardwareVersion) {
        this.mHardwareVersion = 0;
        this.mHardwareVersion = hardwareVersion;
        this.mProfileHeader = new ProfileHeader(buffer);
        this.mBirthday = FileTime.valueOf(buffer);
        this.mWeightInGrams = (int) BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mHeightInMM = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mGender = UserProfileInfo.Gender.valueOf(BitHelper.unsignedByteToInteger(buffer.get()));
        for (int i = 0; i < 16; i++) {
            this.mDeviceName[i] = buffer.getChar();
        }
        this.mLocaleSettings = new LocaleSettings(buffer);
        this.mRunDisplayUnits = UserProfileInfo.RunDisplayUnits.valueOf(BitHelper.unsignedByteToInteger(buffer.get()));
        this.mTelemetryEnabled = BitHelper.unsignedByteToInteger(buffer.get()) == 1;
    }

    public void updateUsingCloudUserProfile(UserProfileInfo cProfile) {
        Validation.validateNullParameter(cProfile, "Cloud Profile needs to not be null");
        Date lastCloudUpdateTime = cProfile.getLastKDKSyncUpdateOn();
        if (lastCloudUpdateTime != null) {
            setTimeStampUTC(lastCloudUpdateTime.getTime());
        }
        if (cProfile.getODSUserID() != null) {
            setProfileID(cProfile.getODSUserID());
        }
        if (cProfile.getFirmwareProfileVersion() != 1) {
            setVersion(cProfile.getFirmwareProfileVersion());
        }
        if (cProfile.getBirthdate() != null) {
            setBirthday(cProfile.getBirthdate());
        }
        if (cProfile.getWeightInGrams() != 0) {
            setWeightInGrams(cProfile.getWeightInGrams());
        }
        if (cProfile.getHeightInMM() != 0) {
            setHeightInMM(cProfile.getHeightInMM());
        }
        if (cProfile.getGender() != null) {
            setGender(cProfile.getGender());
        }
        if (cProfile.getFirmwareDeviceName() != null) {
            setDeviceName(cProfile.getFirmwareDeviceName());
        }
        if (cProfile.getDateSeparator() != 0) {
            setDateSeparator(cProfile.getDateSeparator());
        }
        if (cProfile.getNumberSeparator() != 0) {
            setNumberSeparator(cProfile.getNumberSeparator());
        }
        if (cProfile.getDecimalSeparator() != 0) {
            setDecimalSeparator(cProfile.getDecimalSeparator());
        }
        if (cProfile.getDisplaySizeUnit() != null) {
            setDisplaySizeUnit(cProfile.getDisplaySizeUnit());
        }
        if (cProfile.getDisplayDistanceUnit() != null) {
            setDisplayDistanceUnit(cProfile.getDisplayDistanceUnit());
        }
        if (cProfile.getDisplayWeightUnit() != null) {
            setDisplayWeightUnit(cProfile.getDisplayWeightUnit());
        }
        if (cProfile.getDisplayVolumeUnit() != null) {
            setDisplayVolumeUnit(cProfile.getDisplayVolumeUnit());
        }
        if (cProfile.getDisplayCaloriesUnit() != null) {
            setDisplayCaloriesUnit(cProfile.getDisplayCaloriesUnit());
        }
        if (cProfile.getDisplayTemperatureUnit() != null) {
            setDisplayTemperatureUnit(cProfile.getDisplayTemperatureUnit());
        }
        if (cProfile.getRunDisplayUnits() != null) {
            setRunDisplayUnits(cProfile.getRunDisplayUnits());
        }
        if (cProfile.isTelemetryEnabled() != null) {
            setTelemetryEnabled(cProfile.isTelemetryEnabled().booleanValue());
        }
    }

    public void updateUsingCloudUserProfileRegionUpdates(UserProfileInfo cProfile) {
        updateUsingCloudUserProfile(cProfile);
        if (cProfile.getTimeFormat() != null) {
            setTimeFormat(cProfile.getTimeFormat());
        }
        if (cProfile.getDateFormat() != null) {
            setDateFormat(cProfile.getDateFormat());
        }
    }

    public byte[] toBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(128);
        buffer.put(this.mProfileHeader.toBytes());
        buffer.put(this.mBirthday.toBytes());
        buffer.putInt(BitHelper.longToUnsignedInt(this.mWeightInGrams));
        buffer.putShort(BitHelper.intToUnsignedShort(this.mHeightInMM));
        buffer.put(BitHelper.intToUnsignedByte(this.mGender.getIndex()));
        for (int i = 0; i < 16; i++) {
            buffer.putChar(this.mDeviceName[i]);
        }
        buffer.put(this.mLocaleSettings.toBytes());
        buffer.put(BitHelper.intToUnsignedByte(this.mRunDisplayUnits.getIndex()));
        if (this.mTelemetryEnabled) {
            buffer.put(BitHelper.intToUnsignedByte(1));
        } else {
            buffer.put(BitHelper.intToUnsignedByte(0));
        }
        buffer.put(new byte[23]);
        return buffer.array();
    }

    public UUID getProfileID() {
        return this.mProfileHeader.getProfileID();
    }

    public DeviceProfileInfo setProfileID(UUID profileID) {
        this.mProfileHeader.setProfileID(profileID);
        return this;
    }

    public int getVersion() {
        return this.mProfileHeader.getVersion();
    }

    public DeviceProfileInfo setVersion(int version) {
        this.mProfileHeader.setVersion(version);
        return this;
    }

    public Date getTimeStampUTC() {
        return this.mProfileHeader.getTimeStampUTC();
    }

    public void setTimeStampUTC(long tsUTC) {
        this.mProfileHeader.setTimeStampUTC(tsUTC);
    }

    public Date getBirthday() {
        return this.mBirthday.toDate();
    }

    public DeviceProfileInfo setBirthday(Date bday) {
        this.mBirthday = new FileTime(bday);
        return this;
    }

    public int getWeightInGrams() {
        return this.mWeightInGrams;
    }

    public DeviceProfileInfo setWeightInGrams(int weightInGrams) {
        if (weightInGrams >= 35000 && weightInGrams <= 250000) {
            this.mWeightInGrams = weightInGrams;
            return this;
        }
        throw new IllegalArgumentException("Weight must be between 35000 and 250000");
    }

    public int getHeightInMM() {
        return this.mHeightInMM;
    }

    public DeviceProfileInfo setHeightInMM(int heightInMM) {
        if (heightInMM >= 1000 && heightInMM <= 2500) {
            this.mHeightInMM = heightInMM;
            return this;
        }
        throw new IllegalArgumentException("Height must be between 1000 and 2500");
    }

    public UserProfileInfo.Gender getGender() {
        return this.mGender;
    }

    public DeviceProfileInfo setGender(UserProfileInfo.Gender gender) {
        this.mGender = gender;
        return this;
    }

    public String getDeviceName() {
        return new String(this.mDeviceName).trim();
    }

    public DeviceProfileInfo setDeviceName(String dName) {
        if (dName == null) {
            dName = "";
        }
        Validation.validStringNullAndLength(dName, 15, "DeviceProfileInfo: Device Name");
        this.mDeviceName = new char[16];
        for (int i = 0; i < dName.length(); i++) {
            this.mDeviceName[i] = dName.charAt(i);
        }
        return this;
    }

    public String getLocaleName() {
        return this.mLocaleSettings.getLocaleName();
    }

    public CargoLocation getLocation() {
        return this.mLocaleSettings.getLocation();
    }

    public CargoLanguage getLanguage() {
        return this.mLocaleSettings.getLanguage();
    }

    public CargoTimeFormat getTimeFormat() {
        return this.mLocaleSettings.getTimeFormat();
    }

    private void setTimeFormat(CargoTimeFormat timeFormat) {
        this.mLocaleSettings.setTimeFormat(timeFormat);
    }

    public CargoDateFormat getDateFormat() {
        return this.mLocaleSettings.getDateFormat();
    }

    private void setDateFormat(CargoDateFormat dateFormat) {
        this.mLocaleSettings.setDateFormat(dateFormat);
    }

    public char getDateSeparator() {
        return this.mLocaleSettings.getDateSeparator();
    }

    public DeviceProfileInfo setDateSeparator(char dateSeparator) {
        this.mLocaleSettings.setDateSeparator(dateSeparator);
        return this;
    }

    public char getNumberSeparator() {
        return this.mLocaleSettings.getNumberSeparator();
    }

    public DeviceProfileInfo setNumberSeparator(char numberSeparator) {
        this.mLocaleSettings.setNumberSeparator(numberSeparator);
        return this;
    }

    public char getDecimalSeparator() {
        return this.mLocaleSettings.getDecimalSeparator();
    }

    public DeviceProfileInfo setDecimalSeparator(char decimalSeparator) {
        this.mLocaleSettings.setDecimalSeparator(decimalSeparator);
        return this;
    }

    public UnitType getDisplaySizeUnit() {
        return this.mLocaleSettings.getSizeUnit();
    }

    public DeviceProfileInfo setDisplaySizeUnit(UnitType sizeUnit) {
        this.mLocaleSettings.setSizeUnit(sizeUnit);
        return this;
    }

    public UnitType getDisplayDistanceUnit() {
        return this.mLocaleSettings.getDistanceUnit();
    }

    public DeviceProfileInfo setDisplayDistanceUnit(UnitType distanceUnit) {
        this.mLocaleSettings.setDistanceUnit(distanceUnit);
        return this;
    }

    public UnitType getDisplayWeightUnit() {
        return this.mLocaleSettings.getWeightUnit();
    }

    public DeviceProfileInfo setDisplayWeightUnit(UnitType weightUnit) {
        this.mLocaleSettings.setWeightUnit(weightUnit);
        return this;
    }

    public UnitType getDisplayVolumeUnit() {
        return this.mLocaleSettings.getVolumeUnit();
    }

    public DeviceProfileInfo setDisplayVolumeUnit(UnitType volumeUnit) {
        this.mLocaleSettings.setVolumeUnit(volumeUnit);
        return this;
    }

    public UnitType getDisplayCaloriesUnit() {
        return this.mLocaleSettings.getEnergyUnit();
    }

    public DeviceProfileInfo setDisplayCaloriesUnit(UnitType caloriesUnit) {
        this.mLocaleSettings.setEnergyUnit(caloriesUnit);
        return this;
    }

    public UnitType getDisplayTemperatureUnit() {
        return this.mLocaleSettings.getTempUnit();
    }

    public DeviceProfileInfo setDisplayTemperatureUnit(UnitType tempUnit) {
        this.mLocaleSettings.setTempUnit(tempUnit);
        return this;
    }

    public UserProfileInfo.RunDisplayUnits getRunDisplayUnits() {
        return this.mRunDisplayUnits;
    }

    public DeviceProfileInfo setRunDisplayUnits(UserProfileInfo.RunDisplayUnits runDisplayUnits) {
        this.mRunDisplayUnits = runDisplayUnits;
        return this;
    }

    public boolean isTelemetryEnabled() {
        return this.mTelemetryEnabled;
    }

    public DeviceProfileInfo setTelemetryEnabled(boolean telemetryEnabled) {
        this.mTelemetryEnabled = telemetryEnabled;
        return this;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Device Profile Information:%s", System.getProperty("line.separator")));
        result.append(this.mProfileHeader.toString());
        result.append(String.format("     |--Birthdate= %s", getBirthday().toString())).append(System.getProperty("line.separator"));
        result.append(String.format("     |--WeightInGram= %d", Integer.valueOf(this.mWeightInGrams))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--HeightInMM= %d ", Integer.valueOf(this.mHeightInMM))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--Gender= %s", this.mGender.toString())).append(System.getProperty("line.separator"));
        result.append(String.format("     |--Device Name= %s", getDeviceName())).append(System.getProperty("line.separator"));
        result.append(this.mLocaleSettings.toString());
        result.append(String.format("     |--RunDisplayUnits= %s", this.mRunDisplayUnits.toString())).append(System.getProperty("line.separator"));
        result.append(String.format("     |--TelemetryEnabled= %s", Boolean.valueOf(this.mTelemetryEnabled))).append(System.getProperty("line.separator"));
        return result.toString();
    }
}
