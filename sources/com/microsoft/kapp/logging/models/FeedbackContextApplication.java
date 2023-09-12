package com.microsoft.kapp.logging.models;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.ServerProtocol;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.R;
import com.microsoft.kapp.debug.KappConfig;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.logging.LogConstants;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
/* loaded from: classes.dex */
public class FeedbackContextApplication implements Parcelable {
    public static final Parcelable.Creator<FeedbackContextApplication> CREATOR = new Parcelable.Creator<FeedbackContextApplication>() { // from class: com.microsoft.kapp.logging.models.FeedbackContextApplication.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextApplication createFromParcel(Parcel in) {
            return new FeedbackContextApplication(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextApplication[] newArray(int size) {
            return new FeedbackContextApplication[size];
        }
    };
    @SerializedName("buildFlavor")
    String mBuildFlavor;
    @SerializedName("id")
    String mId;
    @SerializedName(WorkoutSummary.NAME)
    String mName;
    @SerializedName(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION)
    String mVersion;

    public FeedbackContextApplication(Context context) {
        this.mId = LogConstants.METADATA_PLATFORM_ID;
        this.mName = context.getString(R.string.app_name);
        try {
            this.mVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException exception) {
            KLog.e(FeedbackContextApplication.class.getSimpleName(), "Exception in getVersionText", exception);
        }
        this.mBuildFlavor = KappConfig.isDebbuging ? TelemetryConstants.Events.ShakeDialog.Dimensions.DEBUG : "Release";
    }

    public FeedbackContextApplication(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mVersion);
        dest.writeString(this.mBuildFlavor);
    }

    public void readFromParcel(Parcel in) {
        this.mId = in.readString();
        this.mName = in.readString();
        this.mVersion = in.readString();
        this.mBuildFlavor = in.readString();
    }
}
