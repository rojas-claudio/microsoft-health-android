package com.microsoft.band.webtiles;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
/* loaded from: classes.dex */
public class WebTileContentMapping implements Parcelable {
    public static final Parcelable.Creator<WebTileContentMapping> CREATOR = new Parcelable.Creator<WebTileContentMapping>() { // from class: com.microsoft.band.webtiles.WebTileContentMapping.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileContentMapping createFromParcel(Parcel in) {
            return new WebTileContentMapping(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileContentMapping[] newArray(int size) {
            return new WebTileContentMapping[size];
        }
    };
    private String mContentPath;
    private String mTemplatePattern;

    public WebTileContentMapping(String templatePattern, String contentPath) {
        setTemplatePattern(templatePattern);
        setContentPath(contentPath);
    }

    public String getTemplatePattern() {
        return this.mTemplatePattern;
    }

    public void setTemplatePattern(String templatePattern) {
        Validation.validateNullParameter(templatePattern, "TemplatePattern");
        Validation.validateStringEmptyOrWhiteSpace(templatePattern, "TemplatePattern");
        this.mTemplatePattern = templatePattern;
    }

    public String getContentPath() {
        return this.mContentPath;
    }

    public void setContentPath(String contentPath) {
        Validation.validateNullParameter(contentPath, "ContentPath");
        Validation.validateStringEmptyOrWhiteSpace(contentPath, "ContentPath");
        this.mContentPath = contentPath;
    }

    WebTileContentMapping(Parcel in) {
        this.mTemplatePattern = in.readString();
        this.mContentPath = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTemplatePattern);
        dest.writeString(this.mContentPath);
    }
}
