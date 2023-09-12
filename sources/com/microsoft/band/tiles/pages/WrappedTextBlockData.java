package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class WrappedTextBlockData extends TextData {
    public static final Parcelable.Creator<WrappedTextBlockData> CREATOR = new Parcelable.Creator<WrappedTextBlockData>() { // from class: com.microsoft.band.tiles.pages.WrappedTextBlockData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WrappedTextBlockData createFromParcel(Parcel source) {
            return new WrappedTextBlockData(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WrappedTextBlockData[] newArray(int size) {
            return new WrappedTextBlockData[0];
        }
    };

    public WrappedTextBlockData(int id, String text) {
        super(id, text);
    }

    private WrappedTextBlockData(Parcel source) {
        super(source);
    }
}
