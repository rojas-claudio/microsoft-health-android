package com.microsoft.kapp.models.whatsnew;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class WhatsNewSecondaryCardDataModel implements Parcelable {
    public static final Parcelable.Creator<WhatsNewSecondaryCardDataModel> CREATOR = new Parcelable.Creator<WhatsNewSecondaryCardDataModel>() { // from class: com.microsoft.kapp.models.whatsnew.WhatsNewSecondaryCardDataModel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WhatsNewSecondaryCardDataModel createFromParcel(Parcel in) {
            return new WhatsNewSecondaryCardDataModel(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WhatsNewSecondaryCardDataModel[] newArray(int size) {
            return new WhatsNewSecondaryCardDataModel[size];
        }
    };
    private String subtitle;
    private String title;

    public WhatsNewSecondaryCardDataModel() {
    }

    public WhatsNewSecondaryCardDataModel(String mTitle, String mSubtitle) {
        this.title = mTitle;
        this.subtitle = mSubtitle;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.title);
        out.writeString(this.subtitle);
    }

    private WhatsNewSecondaryCardDataModel(Parcel in) {
        this.title = in.readString();
        this.subtitle = in.readString();
    }
}
