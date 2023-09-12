package com.microsoft.band.device;

import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;
/* loaded from: classes.dex */
public class ProfileHeader implements Serializable {
    public static final int PROFILEHEADER_STRUCTURE_LENGTH = 26;
    private static final long serialVersionUID = 7175579055350834965L;
    private UUID mProfileID;
    private FileTime mTimeStampUTC;
    private int mVersion;

    public ProfileHeader(ByteBuffer buffer) {
        this.mVersion = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mTimeStampUTC = FileTime.valueOf(buffer);
        this.mProfileID = BufferUtil.getUUID(buffer);
    }

    public ProfileHeader(int version, UUID id) {
        this.mVersion = version;
        this.mTimeStampUTC = new FileTime(System.currentTimeMillis());
        this.mProfileID = id;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(26);
        buffer.putShort(BitHelper.intToUnsignedShort(this.mVersion));
        buffer.put(this.mTimeStampUTC.toBytes());
        BufferUtil.putUUID(buffer, this.mProfileID);
        return buffer.array();
    }

    public UUID getProfileID() {
        return this.mProfileID;
    }

    public void setProfileID(UUID profileID) {
        this.mProfileID = profileID;
    }

    public int getVersion() {
        return this.mVersion;
    }

    public void setVersion(int version) {
        this.mVersion = version;
    }

    public Date getTimeStampUTC() {
        return this.mTimeStampUTC.toDate();
    }

    public void setTimeStampUTC(long tsUTC) {
        this.mTimeStampUTC = new FileTime(tsUTC);
    }

    public String toString() {
        return String.format("Profile Header Portion:%s", System.getProperty("line.separator")) + String.format("     |--Version = %d %s", Integer.valueOf(this.mVersion), System.getProperty("line.separator")) + String.format("     |--timeStampUTC = %s %s", this.mTimeStampUTC.toString(), System.getProperty("line.separator")) + String.format("     |--profileID = %s %s", this.mProfileID.toString(), System.getProperty("line.separator"));
    }
}
