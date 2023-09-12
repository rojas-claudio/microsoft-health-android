package com.microsoft.band.tiles;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class BandIcon implements Parcelable {
    public static final Parcelable.Creator<BandIcon> CREATOR = new Parcelable.Creator<BandIcon>() { // from class: com.microsoft.band.tiles.BandIcon.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BandIcon createFromParcel(Parcel in) {
            return new BandIcon(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BandIcon[] newArray(int size) {
            return new BandIcon[size];
        }
    };
    private Bitmap mIcon;

    public BandIcon(Bitmap icon) {
        this.mIcon = icon;
    }

    public static BandIcon toBandIcon(Bitmap bitmap) {
        return new BandIcon(bitmap);
    }

    public Bitmap getIcon() {
        return this.mIcon;
    }

    BandIcon(Parcel in) {
        this.mIcon = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mIcon);
    }
}
