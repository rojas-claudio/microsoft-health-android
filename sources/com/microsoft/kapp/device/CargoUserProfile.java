package com.microsoft.kapp.device;

import com.facebook.internal.ServerProtocol;
import com.google.gson.annotations.SerializedName;
import com.microsoft.band.client.UnitType;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.band.device.DeviceSettings;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.Length;
import com.microsoft.kapp.models.Weight;
import com.microsoft.kapp.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.Years;
/* loaded from: classes.dex */
public class CargoUserProfile implements Cloneable, Serializable {
    private static final long serialVersionUID = 0;
    @SerializedName(Constants.KEY_ALLOW_MARKETING_EMAIL)
    private boolean mAllowMarketingEmail;
    @SerializedName("Birthdate")
    private DateTime mBirthdate;
    @SerializedName("AllDeviceSettings")
    ArrayList<DeviceSettings> mConnectedDevices;
    @SerializedName("DeviceId")
    private UUID mDeviceId;
    @SerializedName("FirstName")
    private String mFirstName;
    @SerializedName(TelemetryConstants.Events.OobeComplete.Dimensions.GENDER)
    private UserProfileInfo.Gender mGender;
    @SerializedName("Height")
    private int mHeightInMM;
    @SerializedName("IsBandPaired")
    private boolean mIsBandPaired;
    @SerializedName("IsDisplayDistanceHeightMetric")
    private boolean mIsDisplayDistanceHeightMetric;
    @SerializedName("IsDisplayTemperatureMetric")
    private boolean mIsDisplayTemperatureMetric;
    @SerializedName("IsDisplayWeightMetric")
    private boolean mIsDisplayWeightMetric;
    @SerializedName("OobeComplete")
    private boolean mIsOobeComplete;
    @SerializedName("AllowTelemetry")
    private boolean mIsTelemetryEnabled;
    private ProfileRegionSettings mLocaleSettings;
    @SerializedName("PreferredLocale")
    private String mPreferredLocale;
    @SerializedName("PreferredRegion")
    private String mPreferredRegion;
    private String mThirdPartyPartnersPortalEndpoint;
    @SerializedName("Weight")
    private int mWeightInGrams;
    @SerializedName("ZipCode")
    private String mZipCode;

    public CargoUserProfile() {
    }

    public CargoUserProfile(UserProfileInfo profile) {
        Validate.notNull(profile, "profile");
        this.mFirstName = profile.getFirstName();
        this.mGender = profile.getGender();
        this.mHeightInMM = profile.getHeightInMM();
        this.mWeightInGrams = profile.getWeightInGrams();
        this.mBirthdate = new DateTime(profile.getBirthdate());
        this.mIsOobeComplete = profile.isHasCompletedOOBE() == Boolean.TRUE;
        this.mDeviceId = profile.getAppPairingDeviceID();
        this.mZipCode = profile.getZipCode();
        this.mPreferredLocale = profile.getPreferredLocale();
        this.mPreferredRegion = profile.getPreferredRegion();
        this.mIsDisplayDistanceHeightMetric = profile.getDisplayDistanceUnit() == UnitType.METRIC;
        this.mIsDisplayTemperatureMetric = profile.getDisplayTemperatureUnit() == UnitType.METRIC;
        this.mIsDisplayWeightMetric = profile.getDisplayWeightUnit() == UnitType.METRIC;
        this.mIsTelemetryEnabled = true;
        this.mThirdPartyPartnersPortalEndpoint = profile.getThirdPartyPartnersPortalEndpoint();
        String allowMarketing = profile.getValueFromDictionary(Constants.KEY_ALLOW_MARKETING_EMAIL);
        this.mAllowMarketingEmail = allowMarketing != null ? allowMarketing.equalsIgnoreCase(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE) : true;
        this.mIsBandPaired = profile.isBandPaired();
        this.mConnectedDevices = profile.getConnectedDevices();
    }

    public boolean isDisplayTemperatureMetric() {
        return this.mIsDisplayTemperatureMetric;
    }

    public boolean isBandPaired() {
        return this.mIsBandPaired;
    }

    public void setDisplayTemperatureMetric(boolean isMetric) {
        this.mIsDisplayTemperatureMetric = isMetric;
    }

    public boolean isDisplayWeightMetric() {
        return this.mIsDisplayWeightMetric;
    }

    public void setDisplayWeightMetric(boolean isMetric) {
        this.mIsDisplayWeightMetric = isMetric;
    }

    public boolean isDisplayDistanceHeightMetric() {
        return this.mIsDisplayDistanceHeightMetric;
    }

    public void setDisplayDistanceHeightMetric(boolean isMetric) {
        this.mIsDisplayDistanceHeightMetric = isMetric;
    }

    public void ApplyToProfile(UserProfileInfo profile) {
        Validate.notNull(profile, "profile");
        profile.setGender(this.mGender);
        profile.setHeightInMM(this.mHeightInMM);
        profile.setWeightInGrams(this.mWeightInGrams);
        profile.setBirthdate(this.mBirthdate.toDateTime(DateTimeZone.UTC).toDate());
        profile.setFirstName(this.mFirstName);
        profile.setHasCompletedOOBE(this.mIsOobeComplete);
        profile.setZipCode(this.mZipCode);
        profile.setPreferredLocale(this.mPreferredLocale);
        profile.setPreferredRegion(this.mPreferredRegion);
        profile.addValueToDictionary(Constants.KEY_ALLOW_MARKETING_EMAIL, String.valueOf(this.mAllowMarketingEmail));
        profile.setDisplayTemperatureUnit(this.mIsDisplayTemperatureMetric ? UnitType.METRIC : UnitType.IMPERIAL);
        profile.setDisplayDistanceUnit(this.mIsDisplayDistanceHeightMetric ? UnitType.METRIC : UnitType.IMPERIAL);
        profile.setDisplayWeightUnit(this.mIsDisplayWeightMetric ? UnitType.METRIC : UnitType.IMPERIAL);
        profile.setConnectedDevices(this.mConnectedDevices);
        if (this.mLocaleSettings != null) {
            profile.setDateSeparator(this.mLocaleSettings.getDateSeparator());
            profile.setNumberSeparator(this.mLocaleSettings.getNumberSeparator());
            profile.setDecimalSeparator(this.mLocaleSettings.getDecimalSeparator());
            profile.setTimeFormat(this.mLocaleSettings.getDisplayTimeFormat());
            profile.setDateFormat(this.mLocaleSettings.getDisplayDateFormat());
            profile.setLocation(this.mLocaleSettings.getCargoLocation());
            profile.setDisplayCaloriesUnit(this.mLocaleSettings.getDisplayCaloriesUnit());
            profile.setDisplaySizeUnit(this.mLocaleSettings.getDisplaySizeUnit());
            profile.setDisplayVolumeUnit(this.mLocaleSettings.getDisplayVolumeUnit());
        }
    }

    public String getThirdPartyPartnersPortalEndpoint() {
        return this.mThirdPartyPartnersPortalEndpoint;
    }

    public int getAge() {
        DateTime birthdate = getBirthdate();
        LocalDate birthDate = birthdate.toLocalDate();
        LocalDate now = new LocalDate();
        return Years.yearsBetween(birthDate, now).getYears();
    }

    public String getFirstName() {
        return this.mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public UserProfileInfo.Gender getGender() {
        return this.mGender;
    }

    public void setGender(UserProfileInfo.Gender gender) {
        this.mGender = gender;
    }

    public int getHeightInMM() {
        return this.mHeightInMM;
    }

    public void setHeight(Length height) {
        if (height != null) {
            this.mHeightInMM = (int) Math.round(height.getMillimeters());
        }
    }

    public void setHeight(int heightInMM) {
        this.mHeightInMM = heightInMM;
    }

    public int getWeightInGrams() {
        return this.mWeightInGrams;
    }

    public void setWeight(Weight weight) {
        if (weight != null) {
            this.mWeightInGrams = (int) Math.round(weight.getGrams());
        }
    }

    public void setWeight(int weightInGrams) {
        this.mWeightInGrams = weightInGrams;
    }

    public String getZipCode() {
        return this.mZipCode;
    }

    public void setZipCode(String zipCode) {
        this.mZipCode = zipCode;
    }

    public String getPreferredLocale() {
        return this.mPreferredLocale;
    }

    public void setPreferredLocale(String preferredLocale) {
        this.mPreferredLocale = preferredLocale;
    }

    public String getPreferredRegion() {
        return this.mPreferredRegion;
    }

    public void setPreferredRegion(String preferredRegion) {
        this.mPreferredRegion = preferredRegion;
    }

    public DateTime getBirthdate() {
        return this.mBirthdate;
    }

    public void setBirthdate(DateTime birthdate) {
        this.mBirthdate = birthdate;
    }

    public void setBirthdate(Date birthdate) {
        if (birthdate != null) {
            this.mBirthdate = new DateTime(birthdate);
        }
    }

    public UUID getDeviceId() {
        return this.mDeviceId;
    }

    public void setOobeIsComplete(boolean isComplete) {
        this.mIsOobeComplete = isComplete;
    }

    public boolean isOobeComplete() {
        return this.mIsOobeComplete;
    }

    public boolean isTelemetryEnabled() {
        return true;
    }

    public boolean isAllowMarketingEmail() {
        return this.mAllowMarketingEmail;
    }

    public void setAllowMarketingEmail(boolean allowMarketingEmail) {
        this.mAllowMarketingEmail = allowMarketingEmail;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Unable to clone CargoUserProfile");
        }
    }

    public ArrayList<DeviceSettings> getDevices() {
        return this.mConnectedDevices;
    }

    public void setLocaleSettings(ProfileRegionSettings localeSettings) {
        this.mLocaleSettings = localeSettings;
    }
}
