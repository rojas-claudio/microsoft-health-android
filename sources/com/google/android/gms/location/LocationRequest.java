package com.google.android.gms.location;

import android.os.Parcel;
import android.os.SystemClock;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dl;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public final class LocationRequest implements SafeParcelable {
    public static final LocationRequestCreator CREATOR = new LocationRequestCreator();
    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
    public static final int PRIORITY_HIGH_ACCURACY = 100;
    public static final int PRIORITY_LOW_POWER = 104;
    public static final int PRIORITY_NO_POWER = 105;
    private final int iM;
    int mPriority;
    long oC;
    long oJ;
    long oK;
    boolean oL;
    int oM;
    float oN;

    public LocationRequest() {
        this.iM = 1;
        this.mPriority = 102;
        this.oJ = 3600000L;
        this.oK = 600000L;
        this.oL = false;
        this.oC = Long.MAX_VALUE;
        this.oM = Integer.MAX_VALUE;
        this.oN = 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocationRequest(int versionCode, int priority, long interval, long fastestInterval, boolean explicitFastestInterval, long expireAt, int numUpdates, float smallestDisplacement) {
        this.iM = versionCode;
        this.mPriority = priority;
        this.oJ = interval;
        this.oK = fastestInterval;
        this.oL = explicitFastestInterval;
        this.oC = expireAt;
        this.oM = numUpdates;
        this.oN = smallestDisplacement;
    }

    private static void X(int i) {
        switch (i) {
            case 100:
            case 102:
            case PRIORITY_LOW_POWER /* 104 */:
            case 105:
                return;
            case 101:
            case Constants.SLEEP_FRAGMENT_EVENT_DETAILS_REQUEST /* 103 */:
            default:
                throw new IllegalArgumentException("invalid quality: " + i);
        }
    }

    public static String Y(int i) {
        switch (i) {
            case 100:
                return "PRIORITY_HIGH_ACCURACY";
            case 101:
            case Constants.SLEEP_FRAGMENT_EVENT_DETAILS_REQUEST /* 103 */:
            default:
                return "???";
            case 102:
                return "PRIORITY_BALANCED_POWER_ACCURACY";
            case PRIORITY_LOW_POWER /* 104 */:
                return "PRIORITY_LOW_POWER";
            case 105:
                return "PRIORITY_NO_POWER";
        }
    }

    private static void a(float f) {
        if (f < 0.0f) {
            throw new IllegalArgumentException("invalid displacement: " + f);
        }
    }

    public static LocationRequest create() {
        return new LocationRequest();
    }

    private static void h(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("invalid interval: " + j);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof LocationRequest) {
            LocationRequest locationRequest = (LocationRequest) object;
            return this.mPriority == locationRequest.mPriority && this.oJ == locationRequest.oJ && this.oK == locationRequest.oK && this.oL == locationRequest.oL && this.oC == locationRequest.oC && this.oM == locationRequest.oM && this.oN == locationRequest.oN;
        }
        return false;
    }

    public long getExpirationTime() {
        return this.oC;
    }

    public long getFastestInterval() {
        return this.oK;
    }

    public long getInterval() {
        return this.oJ;
    }

    public int getNumUpdates() {
        return this.oM;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public float getSmallestDisplacement() {
        return this.oN;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return dl.hashCode(Integer.valueOf(this.mPriority), Long.valueOf(this.oJ), Long.valueOf(this.oK), Boolean.valueOf(this.oL), Long.valueOf(this.oC), Integer.valueOf(this.oM), Float.valueOf(this.oN));
    }

    public LocationRequest setExpirationDuration(long millis) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (millis > Long.MAX_VALUE - elapsedRealtime) {
            this.oC = Long.MAX_VALUE;
        } else {
            this.oC = elapsedRealtime + millis;
        }
        if (this.oC < 0) {
            this.oC = 0L;
        }
        return this;
    }

    public LocationRequest setExpirationTime(long millis) {
        this.oC = millis;
        if (this.oC < 0) {
            this.oC = 0L;
        }
        return this;
    }

    public LocationRequest setFastestInterval(long millis) {
        h(millis);
        this.oL = true;
        this.oK = millis;
        return this;
    }

    public LocationRequest setInterval(long millis) {
        h(millis);
        this.oJ = millis;
        if (!this.oL) {
            this.oK = (long) (this.oJ / 6.0d);
        }
        return this;
    }

    public LocationRequest setNumUpdates(int numUpdates) {
        if (numUpdates <= 0) {
            throw new IllegalArgumentException("invalid numUpdates: " + numUpdates);
        }
        this.oM = numUpdates;
        return this;
    }

    public LocationRequest setPriority(int priority) {
        X(priority);
        this.mPriority = priority;
        return this;
    }

    public LocationRequest setSmallestDisplacement(float smallestDisplacementMeters) {
        a(smallestDisplacementMeters);
        this.oN = smallestDisplacementMeters;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request[").append(Y(this.mPriority));
        if (this.mPriority != 105) {
            sb.append(" requested=");
            sb.append(this.oJ + "ms");
        }
        sb.append(" fastest=");
        sb.append(this.oK + "ms");
        if (this.oC != Long.MAX_VALUE) {
            long elapsedRealtime = this.oC - SystemClock.elapsedRealtime();
            sb.append(" expireIn=");
            sb.append(elapsedRealtime + "ms");
        }
        if (this.oM != Integer.MAX_VALUE) {
            sb.append(" num=").append(this.oM);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        LocationRequestCreator.a(this, parcel, flags);
    }
}
