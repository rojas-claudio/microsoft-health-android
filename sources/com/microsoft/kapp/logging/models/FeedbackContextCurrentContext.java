package com.microsoft.kapp.logging.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class FeedbackContextCurrentContext implements Parcelable {
    public static final Parcelable.Creator<FeedbackContextCurrentContext> CREATOR = new Parcelable.Creator<FeedbackContextCurrentContext>() { // from class: com.microsoft.kapp.logging.models.FeedbackContextCurrentContext.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextCurrentContext createFromParcel(Parcel in) {
            return new FeedbackContextCurrentContext(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextCurrentContext[] newArray(int size) {
            return new FeedbackContextCurrentContext[size];
        }
    };
    @SerializedName("page")
    String mSendingPage;

    public FeedbackContextCurrentContext(Parcel source) {
        readFromParcel(source);
    }

    public FeedbackContextCurrentContext(String sender) {
        this.mSendingPage = sender;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSendingPage);
    }

    public void readFromParcel(Parcel in) {
        this.mSendingPage = in.readString();
    }
}
