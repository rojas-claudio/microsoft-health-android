package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class TextBlockData extends TextData {
    public static final Parcelable.Creator<TextBlockData> CREATOR = new Parcelable.Creator<TextBlockData>() { // from class: com.microsoft.band.tiles.pages.TextBlockData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextBlockData createFromParcel(Parcel source) {
            return new TextBlockData(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextBlockData[] newArray(int size) {
            return new TextBlockData[0];
        }
    };

    public TextBlockData(int id, String text) {
        super(id, text);
    }

    private TextBlockData(Parcel source) {
        super(source);
    }
}
