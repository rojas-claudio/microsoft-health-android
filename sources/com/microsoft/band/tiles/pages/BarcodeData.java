package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.internal.util.VersionCheck;
/* loaded from: classes.dex */
public final class BarcodeData extends PageElementData {
    private static final int MAX_BARCODE_STRING_SIZE = 39;
    private String mBarcodeText;
    private BarcodeType mBarcodeType;
    private static final byte[] VALID_CODE39_CHAR_MAP = {1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private static final byte[] VALID_PDF417_CHAR_MAP_V2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0};
    public static final Parcelable.Creator<BarcodeData> CREATOR = new Parcelable.Creator<BarcodeData>() { // from class: com.microsoft.band.tiles.pages.BarcodeData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BarcodeData createFromParcel(Parcel in) {
            return new BarcodeData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BarcodeData[] newArray(int size) {
            return new BarcodeData[size];
        }
    };

    public BarcodeData(int id, String barcodeText, BarcodeType barcodeType) {
        super(id);
        setBarcodeType(barcodeType);
        Validation.notNull(barcodeText, "Barcode cannot be null");
        Validation.validateStringEmptyOrWhiteSpace(barcodeText, "Barcode");
        Validation.validateInRange("Barcode length", barcodeText.length(), 0, 39);
        this.mBarcodeText = barcodeText;
    }

    private void setBarcodeType(BarcodeType type) {
        Validation.notNull(type, "Barcode type cannot be null");
        this.mBarcodeType = type;
    }

    public BarcodeType getBarcodeType() {
        return this.mBarcodeType;
    }

    private BarcodeData(Parcel source) {
        super(source);
        this.mBarcodeText = source.readString();
        setBarcodeType((BarcodeType) source.readSerializable());
    }

    public String getBarCode() {
        return this.mBarcodeText;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.band.tiles.pages.PageElementData
    public void validate(int hardwareVersion) {
        if (getBarcodeType() == BarcodeType.CODE39) {
            checkCode39(getBarCode());
        } else {
            checkPDF417(getBarCode(), hardwareVersion);
        }
    }

    private void checkCode39(String code39) {
        for (int i = 0; i < code39.length(); i++) {
            int digit = code39.charAt(i) - ' ';
            if (digit < 0 || digit > VALID_CODE39_CHAR_MAP.length || VALID_CODE39_CHAR_MAP[digit] == 0) {
                throw new IllegalArgumentException(String.format("BarcodeData Validation: %s is not a valid character for a Code39 barcode", Character.valueOf(code39.charAt(i))));
            }
        }
    }

    private void checkPDF417(String code, int hardwareVersion) {
        if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            for (int i = 0; i < code.length(); i++) {
                int ch = code.charAt(i);
                if (ch >= VALID_PDF417_CHAR_MAP_V2.length || VALID_PDF417_CHAR_MAP_V2[ch] == 0) {
                    throw new IllegalArgumentException(String.format("BarcodeData Validation: %s is not a valid character for a Pdf417 barcode.", Character.valueOf(code.charAt(i))));
                }
            }
            return;
        }
        for (int i2 = 0; i2 < code.length(); i2++) {
            if (!Character.isDigit(code.charAt(i2))) {
                throw new IllegalArgumentException(String.format("BarcodeData Validation: %s is not a digit. Pdf417 barcodes on V1 Microsoft Bands only accept digits.", Character.valueOf(code.charAt(i2))));
            }
        }
    }

    @Override // com.microsoft.band.tiles.pages.PageElementData, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mBarcodeText);
        dest.writeSerializable(this.mBarcodeType);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
