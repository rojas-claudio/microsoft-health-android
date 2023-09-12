package com.microsoft.kapp.logging.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.logging.LogConfiguration;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
/* loaded from: classes.dex */
public class FeedbackContextMicrosoftHealthCloud implements Parcelable {
    public static final Parcelable.Creator<FeedbackContextMicrosoftHealthCloud> CREATOR = new Parcelable.Creator<FeedbackContextMicrosoftHealthCloud>() { // from class: com.microsoft.kapp.logging.models.FeedbackContextMicrosoftHealthCloud.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextMicrosoftHealthCloud createFromParcel(Parcel in) {
            return new FeedbackContextMicrosoftHealthCloud(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextMicrosoftHealthCloud[] newArray(int size) {
            return new FeedbackContextMicrosoftHealthCloud[size];
        }
    };
    @SerializedName("kAccessToken")
    String mKAccessToken;
    @SerializedName("kdsAddress")
    String mKdsAddress;
    @SerializedName("msaToken")
    String mMsaToken;
    @SerializedName("odsUserId")
    String mOdsUserId;
    @SerializedName("podAddress")
    String mPodAddress;

    public FeedbackContextMicrosoftHealthCloud(CredentialsManager credentialManager, LogConfiguration logConfiguration) {
        this.mOdsUserId = credentialManager.getCredentials().getKdsCredential().getUserId();
        this.mKdsAddress = credentialManager.getCredentials().getKdsCredential().getKdsUrl();
        this.mPodAddress = credentialManager.getCredentials().getKdsCredential().getEndPoint();
        if (logConfiguration.getLogMode().equals(LogMode.CAN_LOG_PRIVATE_DATA)) {
            this.mMsaToken = credentialManager.getCredentials().getMsaCredential().getAccessToken();
            this.mKAccessToken = credentialManager.getCredentials().getKdsCredential().getAccessToken();
        }
    }

    public FeedbackContextMicrosoftHealthCloud(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mKdsAddress);
        dest.writeString(this.mOdsUserId);
        dest.writeString(this.mPodAddress);
    }

    public void readFromParcel(Parcel in) {
        this.mKdsAddress = in.readString();
        this.mOdsUserId = in.readString();
        this.mPodAddress = in.readString();
    }
}
