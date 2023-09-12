package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.performance.PerfEvent;
/* loaded from: classes.dex */
public class AppConfigInfo implements Parcelable, Comparable<AppConfigInfo> {
    public static final Parcelable.Creator<AppConfigInfo> CREATOR = new Parcelable.Creator<AppConfigInfo>() { // from class: com.microsoft.krestsdk.models.AppConfigInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppConfigInfo createFromParcel(Parcel in) {
            return new AppConfigInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppConfigInfo[] newArray(int size) {
            return new AppConfigInfo[size];
        }
    };
    @SerializedName(PerfEvent.APPLICATION)
    private String mApplication;
    @SerializedName("HashMd5")
    private String mHashMd5;
    @SerializedName("MirrorURL")
    private String mMirrorURL;
    @SerializedName("PrimaryURL")
    private String mPrimaryURL;
    @SerializedName("Region")
    private String mRegion;
    @SerializedName("SizeInBytes")
    private int mSizeInBytes;
    @SerializedName("Version")
    private String mVersion;

    public String getApplication() {
        return this.mApplication;
    }

    public void setApplication(String application) {
        this.mApplication = application;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public void setmVersion(String version) {
        this.mVersion = version;
    }

    public String getRegion() {
        return this.mRegion;
    }

    public void setRegion(String region) {
        this.mRegion = region;
    }

    public String getPrimaryURL() {
        return this.mPrimaryURL;
    }

    public void setPrimaryURL(String primaryURL) {
        this.mPrimaryURL = primaryURL;
    }

    public String getMirrorURL() {
        return this.mMirrorURL;
    }

    public void setMirrorURL(String mirrorURL) {
        this.mMirrorURL = mirrorURL;
    }

    public int getSizeInBytes() {
        return this.mSizeInBytes;
    }

    public void setSizeInBytes(int sizeInBytes) {
        this.mSizeInBytes = sizeInBytes;
    }

    public String getHashMd5() {
        return this.mHashMd5;
    }

    public void setHashMd5(String hashMd5) {
        this.mHashMd5 = hashMd5;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mApplication);
        dest.writeString(this.mVersion);
        dest.writeString(this.mRegion);
        dest.writeString(this.mPrimaryURL);
        dest.writeString(this.mMirrorURL);
        dest.writeInt(this.mSizeInBytes);
        dest.writeString(this.mHashMd5);
    }

    protected AppConfigInfo(Parcel in) {
        this.mApplication = in.readString();
        this.mVersion = in.readString();
        this.mRegion = in.readString();
        this.mPrimaryURL = in.readString();
        this.mMirrorURL = in.readString();
        this.mSizeInBytes = in.readInt();
        this.mHashMd5 = in.readString();
    }

    public AppConfigInfo() {
    }

    @Override // java.lang.Comparable
    public int compareTo(AppConfigInfo another) {
        if (another.getRegion() == null || another.getVersion() == null) {
            return 1;
        }
        if (TextUtils.equals(getRegion().toLowerCase(), another.getRegion().toLowerCase())) {
            String[] anotherVersionSplits = another.getVersion().split("\\.");
            String[] thisVersionSplits = getVersion().split("\\.");
            int anotherVersionSplitLength = anotherVersionSplits.length;
            int thisVersionSplitLength = thisVersionSplits.length;
            int length = Math.max(thisVersionSplitLength, anotherVersionSplitLength);
            for (int i = 0; i < length; i++) {
                int thisVersionSplit = 0;
                int anotherVersionSplit = 0;
                if (i < thisVersionSplitLength) {
                    thisVersionSplit = Integer.parseInt(thisVersionSplits[i]);
                }
                if (i < anotherVersionSplitLength) {
                    anotherVersionSplit = Integer.parseInt(anotherVersionSplits[i]);
                }
                int result = thisVersionSplit - anotherVersionSplit;
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
        return -1;
    }
}
