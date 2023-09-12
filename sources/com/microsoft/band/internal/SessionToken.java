package com.microsoft.band.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Locale;
/* loaded from: classes.dex */
public final class SessionToken implements Parcelable {
    public static final int CARGO_SERVICE_VERSION = 1;
    public static final Parcelable.Creator<SessionToken> CREATOR = new Parcelable.Creator<SessionToken>() { // from class: com.microsoft.band.internal.SessionToken.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SessionToken createFromParcel(Parcel in) {
            return new SessionToken(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SessionToken[] newArray(int size) {
            return new SessionToken[size];
        }
    };
    private final int mId;
    private final int mVersion;

    public int getId() {
        return this.mId;
    }

    public int getVersion() {
        return this.mVersion;
    }

    public SessionToken(Parcel in) {
        this.mVersion = in.readInt();
        this.mId = in.readInt();
    }

    public SessionToken(int sessionId) {
        this.mVersion = 1;
        this.mId = sessionId;
    }

    public Bundle toBundle() {
        return putInBundle(new Bundle());
    }

    public Bundle putInBundle(Bundle bundle) {
        if (bundle != null) {
            bundle.putParcelable(SessionToken.class.getName(), this);
        }
        return bundle;
    }

    public static SessionToken fromBundle(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        SessionToken sessionToken = (SessionToken) bundle.getParcelable(SessionToken.class.getName());
        return sessionToken;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "%s(%d.%d)", SessionToken.class.getSimpleName(), Integer.valueOf(getVersion()), Integer.valueOf(getId()));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mVersion);
        dest.writeInt(this.mId);
    }
}
