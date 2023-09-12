package com.microsoft.band.device;

import com.microsoft.band.client.CargoDateFormat;
import com.microsoft.band.client.CargoLanguage;
import com.microsoft.band.client.CargoLocation;
import com.microsoft.band.client.CargoTimeFormat;
import com.microsoft.band.client.UnitType;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.Validation;
import java.io.Serializable;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class LocaleSettings implements Serializable {
    public static final int LOCALESETTINGS_STRUCTURE_LENGTH = 30;
    public static final int MAX_LOCALE_NAME_LENGTH = 6;
    private static final long serialVersionUID = -817005294477974961L;
    private CargoDateFormat mDateFormat;
    private char mDateSeparator;
    private char mDecimalSeparator;
    private UnitType mDistanceUnit;
    private UnitType mEnergyUnit;
    private CargoLanguage mLanguage;
    private char[] mLocaleName = new char[6];
    private CargoLocation mLocation;
    private char mNumberSeparator;
    private UnitType mSizeUnit;
    private UnitType mTempUnit;
    private CargoTimeFormat mTimeFormat;
    private UnitType mVolumeUnit;
    private UnitType mWeightUnit;

    public LocaleSettings(ByteBuffer buffer) {
        for (int i = 0; i < 6; i++) {
            this.mLocaleName[i] = buffer.getChar();
        }
        this.mLocation = CargoLocation.lookup(BitHelper.unsignedShortToInteger(buffer.getShort()));
        this.mLanguage = CargoLanguage.lookup(BitHelper.unsignedShortToInteger(buffer.getShort()));
        this.mDateSeparator = buffer.getChar();
        this.mNumberSeparator = buffer.getChar();
        this.mDecimalSeparator = buffer.getChar();
        this.mTimeFormat = CargoTimeFormat.lookup(BitHelper.unsignedByteToInteger(buffer.get()));
        this.mDateFormat = CargoDateFormat.lookup(BitHelper.unsignedByteToInteger(buffer.get()));
        this.mSizeUnit = UnitType.lookup(BitHelper.unsignedByteToInteger(buffer.get()));
        this.mDistanceUnit = UnitType.lookup(BitHelper.unsignedByteToInteger(buffer.get()));
        this.mWeightUnit = UnitType.lookup(BitHelper.unsignedByteToInteger(buffer.get()));
        this.mVolumeUnit = UnitType.lookup(BitHelper.unsignedByteToInteger(buffer.get()));
        this.mEnergyUnit = UnitType.lookup(BitHelper.unsignedByteToInteger(buffer.get()));
        this.mTempUnit = UnitType.lookup(BitHelper.unsignedByteToInteger(buffer.get()));
    }

    public byte[] toBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(30);
        for (int i = 0; i < 6; i++) {
            buffer.putChar(this.mLocaleName[i]);
        }
        buffer.putShort(BitHelper.intToUnsignedShort(this.mLocation.getValue()));
        buffer.putShort(BitHelper.intToUnsignedShort(this.mLanguage.getValue()));
        buffer.putChar(this.mDateSeparator);
        buffer.putChar(this.mNumberSeparator);
        buffer.putChar(this.mDecimalSeparator);
        buffer.put(BitHelper.intToUnsignedByte(this.mTimeFormat.getValue()));
        buffer.put(BitHelper.intToUnsignedByte(this.mDateFormat.getValue()));
        buffer.put(BitHelper.intToUnsignedByte(this.mSizeUnit.getValue()));
        buffer.put(BitHelper.intToUnsignedByte(this.mDistanceUnit.getValue()));
        buffer.put(BitHelper.intToUnsignedByte(this.mWeightUnit.getValue()));
        buffer.put(BitHelper.intToUnsignedByte(this.mVolumeUnit.getValue()));
        buffer.put(BitHelper.intToUnsignedByte(this.mEnergyUnit.getValue()));
        buffer.put(BitHelper.intToUnsignedByte(this.mTempUnit.getValue()));
        return buffer.array();
    }

    public String getLocaleName() {
        return new String(this.mLocaleName).trim();
    }

    public void setLocaleName(String localeNameAsString) {
        Validation.validStringNullAndLength(localeNameAsString, 6, "Locale Name");
        this.mLocaleName = new char[6];
        for (int i = 0; i < localeNameAsString.length(); i++) {
            this.mLocaleName[i] = localeNameAsString.charAt(i);
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("LocaleSettings:%s", System.getProperty("line.separator")));
        result.append(String.format("     |--localeName= %s", getLocaleName())).append(System.getProperty("line.separator"));
        result.append(String.format("     |--Location= %d", Integer.valueOf(this.mLocation.getValue()))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--Language= %d", Integer.valueOf(this.mLanguage.getValue()))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--TimeFormat= %d", Integer.valueOf(this.mTimeFormat.getValue()))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--DateFormat= %d", Integer.valueOf(this.mDateFormat.getValue()))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--dateSeparator= %c", Character.valueOf(this.mDateSeparator))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--numberSeparator= %c", Character.valueOf(this.mNumberSeparator))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--decimalSeparator= %c", Character.valueOf(this.mDecimalSeparator))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--shortDistanceUnit= %d", Integer.valueOf(this.mSizeUnit.getValue()))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--longDistanceUnit= %d", Integer.valueOf(this.mDistanceUnit.getValue()))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--massUnit= %d", Integer.valueOf(this.mWeightUnit.getValue()))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--volumeUnit= %d", Integer.valueOf(this.mVolumeUnit.getValue()))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--energyUnit= %d", Integer.valueOf(this.mEnergyUnit.getValue()))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--tempUnit= %d", Integer.valueOf(this.mTempUnit.getValue()))).append(System.getProperty("line.separator"));
        return result.toString();
    }

    public CargoLocation getLocation() {
        return this.mLocation;
    }

    public void setLocation(CargoLocation location) {
        this.mLocation = location;
    }

    public CargoLanguage getLanguage() {
        return this.mLanguage;
    }

    public void setLanguage(CargoLanguage language) {
        this.mLanguage = language;
    }

    public CargoTimeFormat getTimeFormat() {
        return this.mTimeFormat;
    }

    public void setTimeFormat(CargoTimeFormat timeFormat) {
        this.mTimeFormat = timeFormat;
    }

    public CargoDateFormat getDateFormat() {
        return this.mDateFormat;
    }

    public void setDateFormat(CargoDateFormat dateFormat) {
        this.mDateFormat = dateFormat;
    }

    public char getDateSeparator() {
        return this.mDateSeparator;
    }

    public void setDateSeparator(char dateSeparator) {
        this.mDateSeparator = dateSeparator;
    }

    public char getNumberSeparator() {
        return this.mNumberSeparator;
    }

    public void setNumberSeparator(char numberSeparator) {
        this.mNumberSeparator = numberSeparator;
    }

    public char getDecimalSeparator() {
        return this.mDecimalSeparator;
    }

    public void setDecimalSeparator(char decimalSeparator) {
        this.mDecimalSeparator = decimalSeparator;
    }

    public UnitType getSizeUnit() {
        return this.mSizeUnit;
    }

    public void setSizeUnit(UnitType sizeUnit) {
        this.mSizeUnit = sizeUnit;
    }

    public UnitType getDistanceUnit() {
        return this.mDistanceUnit;
    }

    public void setDistanceUnit(UnitType distanceUnit) {
        this.mDistanceUnit = distanceUnit;
    }

    public UnitType getWeightUnit() {
        return this.mWeightUnit;
    }

    public void setWeightUnit(UnitType weightUnit) {
        this.mWeightUnit = weightUnit;
    }

    public UnitType getVolumeUnit() {
        return this.mVolumeUnit;
    }

    public void setVolumeUnit(UnitType volumeUnit) {
        this.mVolumeUnit = volumeUnit;
    }

    public UnitType getEnergyUnit() {
        return this.mEnergyUnit;
    }

    public void setEnergyUnit(UnitType energyUnit) {
        this.mEnergyUnit = energyUnit;
    }

    public UnitType getTempUnit() {
        return this.mTempUnit;
    }

    public void setTempUnit(UnitType tempUnit) {
        this.mTempUnit = tempUnit;
    }
}
