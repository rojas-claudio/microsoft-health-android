package com.microsoft.band.device;

import com.microsoft.band.util.StringHelper;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Locale;
/* loaded from: classes.dex */
public class FWVersion implements Serializable {
    private static final int APP_NAME_LENGTH = 5;
    public static final int FWVersion_STRUCTURE_LENGTH = 19;
    private static final long serialVersionUID = 0;
    private String mAppName;
    private int mBuildNumber;
    private byte mDebugBuild;
    private byte mPcbId;
    private int mRevision;
    private short mVersionMajor;
    private short mVersionMinor;

    public FWVersion(ByteBuffer buffer) {
        this.mAppName = StringHelper.valueOfNullTerminated(buffer, 0, 5);
        this.mPcbId = buffer.get();
        this.mVersionMajor = buffer.getShort();
        this.mVersionMinor = buffer.getShort();
        this.mRevision = buffer.getInt();
        this.mBuildNumber = buffer.getInt();
        this.mDebugBuild = buffer.get();
    }

    public String getAppName() {
        return this.mAppName;
    }

    public byte getPcbId() {
        return this.mPcbId;
    }

    public short getVersionMajor() {
        return this.mVersionMajor;
    }

    public short getVersionMinor() {
        return this.mVersionMinor;
    }

    public int getRevision() {
        return this.mRevision;
    }

    public int getBuildNumber() {
        return this.mBuildNumber;
    }

    public byte getDebugBuild() {
        return this.mDebugBuild;
    }

    public String getCurrentVersion() {
        return String.format(Locale.getDefault(), "%d.%d.%d.%d", Short.valueOf(this.mVersionMajor), Short.valueOf(this.mVersionMinor), Integer.valueOf(this.mBuildNumber), Integer.valueOf(this.mRevision));
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Device Firmware Version Info:%s", System.getProperty("line.separator")));
        result.append(String.format("     |--AppName= %s %s", this.mAppName, System.getProperty("line.separator")));
        result.append(String.format("     |--Build= %d %s", Integer.valueOf(this.mBuildNumber), System.getProperty("line.separator")));
        result.append(String.format("     |--PCB= %d %s", Byte.valueOf(this.mPcbId), System.getProperty("line.separator")));
        Object[] objArr = new Object[2];
        objArr[0] = Integer.valueOf(this.mDebugBuild == 1 ? 1 : 0);
        objArr[1] = System.getProperty("line.separator");
        result.append(String.format("     |--Debug= %d %s", objArr));
        result.append(String.format("     |--Version= %d . %d  %s", Short.valueOf(this.mVersionMajor), Short.valueOf(this.mVersionMinor), System.getProperty("line.separator")));
        result.append(String.format("     |--Revision= %d %s", Integer.valueOf(this.mRevision), System.getProperty("line.separator")));
        return result.toString();
    }

    public boolean isEqual(String version) {
        if (version != null) {
            String[] versionComponents = version.split("[.]");
            if (versionComponents.length == 4) {
                int[] myVersion = {this.mVersionMajor, this.mVersionMinor, this.mBuildNumber, this.mRevision};
                for (int i = 0; i < 4; i++) {
                    try {
                        if (Integer.parseInt(versionComponents[i]) != myVersion[i]) {
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }
}
