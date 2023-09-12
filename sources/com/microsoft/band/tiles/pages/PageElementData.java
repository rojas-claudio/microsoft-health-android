package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
/* loaded from: classes.dex */
public abstract class PageElementData implements Parcelable {
    int mId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PageElementData(int id) {
        Validation.validateNotNegative(id, "ID cannot be negative");
        this.mId = id;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PageElementData(Parcel source) {
        this.mId = source.readInt();
    }

    public int getId() {
        return this.mId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void validate(int hardwareVersion) {
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
    }
}
