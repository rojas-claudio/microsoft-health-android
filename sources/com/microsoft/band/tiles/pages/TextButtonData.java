package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class TextButtonData extends TextData {
    public static final Parcelable.Creator<TextButtonData> CREATOR = new Parcelable.Creator<TextButtonData>() { // from class: com.microsoft.band.tiles.pages.TextButtonData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextButtonData createFromParcel(Parcel source) {
            return new TextButtonData(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextButtonData[] newArray(int size) {
            return new TextButtonData[0];
        }
    };

    public TextButtonData(int id, String text) {
        super(id, text);
    }

    private TextButtonData(Parcel source) {
        super(source);
    }
}
