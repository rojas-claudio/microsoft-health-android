package com.microsoft.band.tiles.pages;

import android.os.Parcel;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
/* loaded from: classes.dex */
public abstract class TextData extends PageElementData {
    private String mText;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TextData(int id, String text) {
        super(id);
        Validation.notNull(text, "Text cannot be null");
        this.mText = StringUtil.truncateString(text, 160);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TextData(Parcel source) {
        super(source);
        this.mText = source.readString();
    }

    @Override // com.microsoft.band.tiles.pages.PageElementData, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mText);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getText() {
        return this.mText;
    }
}
