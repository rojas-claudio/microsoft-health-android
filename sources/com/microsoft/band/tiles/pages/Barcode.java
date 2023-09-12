package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
/* loaded from: classes.dex */
public final class Barcode extends PageElement<Barcode> {
    public static final Parcelable.Creator<Barcode> CREATOR = new Parcelable.Creator<Barcode>() { // from class: com.microsoft.band.tiles.pages.Barcode.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Barcode createFromParcel(Parcel in) {
            return new Barcode(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Barcode[] newArray(int size) {
            return new Barcode[size];
        }
    };
    private BarcodeType mBarcodeType;

    public Barcode(int originX, int originY, int width, int height, BarcodeType barcodeType) {
        super(originX, originY, width, height);
        setBarcodeType(barcodeType);
    }

    public Barcode(PageRect bound, BarcodeType barcodeType) {
        super(bound);
        setBarcodeType(barcodeType);
    }

    public BarcodeType getBarcodeType() {
        return this.mBarcodeType;
    }

    public Barcode setBarcodeType(BarcodeType type) {
        Validation.notNull(type, "Barcode type cannot be null");
        this.mBarcodeType = type;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Barcode(Parcel in) {
        super(in);
        setBarcodeType((BarcodeType) in.readSerializable());
    }

    @Override // com.microsoft.band.tiles.pages.PageElement, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(this.mBarcodeType);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.band.tiles.pages.PageElement
    public ElementType getType() {
        switch (this.mBarcodeType) {
            case PDF417:
                return ElementType.BARCODE_PDF147;
            default:
                return ElementType.BARCODE_CODE39;
        }
    }
}
