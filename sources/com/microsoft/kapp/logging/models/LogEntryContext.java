package com.microsoft.kapp.logging.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class LogEntryContext implements Parcelable {
    public static final Parcelable.Creator<LogEntryContext> CREATOR = new Parcelable.Creator<LogEntryContext>() { // from class: com.microsoft.kapp.logging.models.LogEntryContext.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LogEntryContext createFromParcel(Parcel in) {
            return new LogEntryContext(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LogEntryContext[] newArray(int size) {
            return new LogEntryContext[size];
        }
    };
    @SerializedName("file")
    private String file;
    @SerializedName("line")
    private int line;
    @SerializedName("method")
    private String method;

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getLine() {
        return this.line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    protected LogEntryContext(Parcel in) {
        this.method = in.readString();
        this.line = in.readInt();
        this.file = in.readString();
    }

    public LogEntryContext() {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.method);
        dest.writeInt(this.line);
        dest.writeString(this.file);
    }
}
