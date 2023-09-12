package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.ServerProtocol;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
/* loaded from: classes.dex */
public class AppConfiguration implements Parcelable {
    public static final Parcelable.Creator<AppConfiguration> CREATOR = new Parcelable.Creator<AppConfiguration>() { // from class: com.microsoft.krestsdk.models.AppConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppConfiguration createFromParcel(Parcel in) {
            return new AppConfiguration(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppConfiguration[] newArray(int size) {
            return new AppConfiguration[size];
        }
    };
    @SerializedName("configuration")
    private Configuration mConfiguration;
    @SerializedName(WorkoutSummary.NAME)
    private String mName;
    @SerializedName(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION)
    private String mVersion;

    public AppConfiguration() {
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public Configuration getConfiguration() {
        return this.mConfiguration;
    }

    public void setConfiguration(Configuration configuration) {
        this.mConfiguration = configuration;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mVersion);
        dest.writeValue(this.mConfiguration);
    }

    protected AppConfiguration(Parcel in) {
        this.mName = in.readString();
        this.mVersion = in.readString();
        this.mConfiguration = (Configuration) in.readValue(Configuration.class.getClassLoader());
    }
}
