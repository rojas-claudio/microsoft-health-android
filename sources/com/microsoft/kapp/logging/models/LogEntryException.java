package com.microsoft.kapp.logging.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class LogEntryException implements Parcelable {
    public static final Parcelable.Creator<LogEntryException> CREATOR = new Parcelable.Creator<LogEntryException>() { // from class: com.microsoft.kapp.logging.models.LogEntryException.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LogEntryException createFromParcel(Parcel in) {
            return new LogEntryException(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LogEntryException[] newArray(int size) {
            return new LogEntryException[size];
        }
    };
    @SerializedName("info")
    private String info;
    @SerializedName("message")
    private String message;
    @SerializedName("type")
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    protected LogEntryException(Parcel in) {
        this.type = in.readString();
        this.message = in.readString();
        this.info = in.readString();
    }

    public LogEntryException() {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.message);
        dest.writeString(this.info);
    }
}
