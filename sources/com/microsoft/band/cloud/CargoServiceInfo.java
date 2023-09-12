package com.microsoft.band.cloud;

import android.os.Parcel;
import android.os.Parcelable;
import java.net.MalformedURLException;
import java.net.URL;
/* loaded from: classes.dex */
public class CargoServiceInfo extends CloudDataModel {
    private static final long serialVersionUID = 1;
    private String mAccessToken;
    private String mFileUpdateServiceAddress;
    private URL mUrl;
    private String mUserAgent;
    public static final String EXTRA_SERVICE_INFO = CargoServiceInfo.class.getName();
    public static final CargoServiceInfo EMPTY_SERVICE_INFO = new CargoServiceInfo("http://empty.url", "http://empty.url", "null-token");
    public static final Parcelable.Creator<CargoServiceInfo> CREATOR = new Parcelable.Creator<CargoServiceInfo>() { // from class: com.microsoft.band.cloud.CargoServiceInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CargoServiceInfo createFromParcel(Parcel in) {
            return new CargoServiceInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CargoServiceInfo[] newArray(int size) {
            return new CargoServiceInfo[size];
        }
    };

    public CargoServiceInfo(String phsURL, String fileUpdateURL, String accessToken, String userAgent) {
        try {
            this.mUrl = new URL(phsURL);
            this.mAccessToken = accessToken;
            this.mFileUpdateServiceAddress = fileUpdateURL;
            this.mUserAgent = userAgent;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("PHS address is not a correct URL", e);
        }
    }

    public CargoServiceInfo(String phsURL, String fileUpdateURL, String accessToken) {
        this(phsURL, fileUpdateURL, accessToken, null);
    }

    CargoServiceInfo(Parcel in) {
        super(in);
        String urlString = in.readString();
        if (urlString != null) {
            try {
                this.mUrl = new URL(urlString);
            } catch (MalformedURLException e) {
            }
        }
        this.mAccessToken = in.readString();
        this.mFileUpdateServiceAddress = in.readString();
        this.mUserAgent = in.readString();
    }

    public String getHostName() {
        return this.mUrl.getHost();
    }

    public URL getUrl() {
        return this.mUrl;
    }

    public String getAccessToken() {
        return this.mAccessToken;
    }

    public String getFileUpdateServiceAddress() {
        return this.mFileUpdateServiceAddress;
    }

    public String getUserAgent() {
        return this.mUserAgent;
    }

    public void setUserAgent(String _userAgent) {
        this.mUserAgent = _userAgent;
    }

    @Override // com.microsoft.band.cloud.CloudDataModel, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mUrl.toString());
        dest.writeString(this.mAccessToken);
        dest.writeString(this.mFileUpdateServiceAddress);
        dest.writeString(this.mUserAgent);
    }
}
