package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.InternalBandConstants;
/* loaded from: classes.dex */
public final class IconData extends PageElementData {
    public static final Parcelable.Creator<IconData> CREATOR = new Parcelable.Creator<IconData>() { // from class: com.microsoft.band.tiles.pages.IconData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IconData createFromParcel(Parcel source) {
            return new IconData(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IconData[] newArray(int size) {
            return new IconData[size];
        }
    };
    private int mIconIndex;

    public IconData(int id, int iconIndex) {
        super(id);
        this.mIconIndex = iconIndex;
    }

    public int getIconIndex() {
        return this.mIconIndex;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.band.tiles.pages.PageElementData
    public void validate(int hardwareVersion) {
        if (this.mIconIndex < 0 || this.mIconIndex > InternalBandConstants.getMaxIconsPerTile(hardwareVersion) - 1) {
            throw new IllegalArgumentException(String.format("IconData Validation: Icon index must be between 0 and %d", Integer.valueOf(InternalBandConstants.getMaxIconsPerTile(hardwareVersion) - 1)));
        }
    }

    private IconData(Parcel source) {
        super(source);
        this.mIconIndex = source.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.microsoft.band.tiles.pages.PageElementData, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mIconIndex);
    }
}
