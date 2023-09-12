package com.microsoft.kapp.version;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.ServerProtocol;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
/* loaded from: classes.dex */
public final class Version implements Parcelable {
    public static final Parcelable.Creator<Version> CREATOR = new Parcelable.Creator<Version>() { // from class: com.microsoft.kapp.version.Version.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Version createFromParcel(Parcel in) {
            return new Version(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Version[] newArray(int size) {
            return new Version[size];
        }
    };
    private int mBuild;
    private byte mDebugBuild;
    private int mMajor;
    private int mMinor;
    private byte mPcbId;
    private int mRevision;

    public Version(int major, int minor) {
        this(major, minor, 0, 0);
    }

    public Version(int major, int minor, int build) {
        this(major, minor, build, 0);
    }

    public Version(int major, int minor, int build, int revision) {
        this.mMajor = major;
        this.mMinor = minor;
        this.mBuild = build;
        this.mRevision = revision;
    }

    public Version(int major, int minor, int build, int revision, byte pcbId, byte debugBuild) {
        this.mMajor = major;
        this.mMinor = minor;
        this.mBuild = build;
        this.mRevision = revision;
        this.mPcbId = pcbId;
        this.mDebugBuild = debugBuild;
    }

    protected Version(Parcel in) {
        Validate.notNull(in, "in");
        this.mMajor = in.readInt();
        this.mMinor = in.readInt();
        this.mBuild = in.readInt();
        this.mRevision = in.readInt();
        this.mPcbId = in.readByte();
        this.mDebugBuild = in.readByte();
    }

    public static Version parse(String version) {
        Validate.notNullOrEmpty(version, ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION);
        String[] versionParts = version.split("\\.");
        if (versionParts.length < 2 || versionParts.length > 4) {
            throw new NumberFormatException("version has less than two components or more than four components");
        }
        int major = parseComponent(versionParts[0], "major");
        int minor = parseComponent(versionParts[1], "minor");
        int build = 0;
        int revision = 0;
        if (versionParts.length >= 3) {
            build = parseComponent(versionParts[2], "build");
        }
        if (versionParts.length == 4) {
            revision = parseComponent(versionParts[3], "revision");
        }
        byte pcbId = (byte) parseComponent(versionParts[4], "pcbId");
        byte debugBuild = (byte) parseComponent(versionParts[5], "debugBuild");
        return new Version(major, minor, build, revision, pcbId, debugBuild);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Validate.notNull(dest, "dest");
        dest.writeInt(this.mMajor);
        dest.writeInt(this.mMinor);
        dest.writeInt(this.mBuild);
        dest.writeInt(this.mRevision);
        dest.writeByte(this.mPcbId);
        dest.writeByte(this.mDebugBuild);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getMajor() {
        return this.mMajor;
    }

    public int getMinor() {
        return this.mMinor;
    }

    public int getBuild() {
        return this.mBuild;
    }

    public int getRevision() {
        return this.mRevision;
    }

    public byte getPcbId() {
        return this.mPcbId;
    }

    public byte getDebugBuild() {
        return this.mDebugBuild;
    }

    public int compareTo(Version value) {
        if (value == null) {
            return 1;
        }
        if (getMajor() != value.getMajor()) {
            return getMajor() <= value.getMajor() ? -1 : 1;
        } else if (getMinor() != value.getMinor()) {
            return getMinor() <= value.getMinor() ? -1 : 1;
        } else if (getBuild() != value.getBuild()) {
            return getBuild() <= value.getBuild() ? -1 : 1;
        } else if (getRevision() != value.getRevision()) {
            return getRevision() <= value.getRevision() ? -1 : 1;
        } else {
            return 0;
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof Version) {
            Version rhs = (Version) obj;
            return new EqualsBuilder().append(this.mMajor, rhs.mMajor).append(this.mMinor, rhs.mMinor).append(this.mBuild, rhs.mBuild).append(this.mRevision, rhs.mRevision).append(this.mPcbId, rhs.mPcbId).append(this.mDebugBuild, rhs.mDebugBuild).isEquals();
        }
        return false;
    }

    public int hashCode() {
        return new HashCodeBuilder().append(this.mMajor).append(this.mMinor).append(this.mBuild).append(this.mRevision).append(this.mPcbId).append(this.mDebugBuild).toHashCode();
    }

    public String toString() {
        Object[] objArr = new Object[6];
        objArr[0] = Integer.valueOf(getMajor());
        objArr[1] = Integer.valueOf(getMinor());
        objArr[2] = Integer.valueOf(getBuild());
        objArr[3] = Integer.valueOf(getRevision());
        objArr[4] = Byte.valueOf(getPcbId());
        objArr[5] = getDebugBuild() == 0 ? "R" : "D";
        return String.format("%d.%d.%d.%d %02d %s", objArr);
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMajor()).append(".").append(getMinor());
        int build = getBuild();
        int revision = getRevision();
        byte pcbId = getPcbId();
        if (build != 0 || revision != 0) {
            sb.append(".").append(build);
            if (revision != 0) {
                sb.append(".").append(build);
            }
            sb.append(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE).append((int) pcbId);
        }
        return sb.toString();
    }

    private static int parseComponent(String componentValue, String componentName) {
        try {
            int version = Integer.parseInt(componentValue);
            return version;
        } catch (NumberFormatException e) {
            String message = String.format("The component '%s' passed in is not a valid integer value.", componentName);
            throw new NumberFormatException(message);
        }
    }
}
