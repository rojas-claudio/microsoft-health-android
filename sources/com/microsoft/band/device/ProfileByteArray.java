package com.microsoft.band.device;

import com.microsoft.band.internal.util.BufferUtil;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;
/* loaded from: classes.dex */
public class ProfileByteArray implements Serializable {
    public static final int OPAQUE_BYTE_ARRAY_LENGTH = 256;
    public static final int PROFILE_BYTE_ARRAY_STRUCTURE_LENGTH = 282;
    private static final long serialVersionUID = 3085094703646381204L;
    private byte[] mOpaqueByteArray;
    private ProfileHeader mProfileHeader;

    public ProfileByteArray(ByteBuffer buffer) {
        this.mOpaqueByteArray = new byte[256];
        this.mProfileHeader = new ProfileHeader(buffer);
        buffer.get(this.mOpaqueByteArray);
    }

    public ProfileByteArray(DeviceProfileInfo dpi, byte[] opaqueArray) {
        this.mOpaqueByteArray = new byte[256];
        if (opaqueArray == null || opaqueArray.length != 256) {
            throw new IllegalArgumentException("Byte array must be 256 bytes long.");
        }
        if (dpi == null) {
            throw new IllegalArgumentException("DeviceProfileInfo object must not be null");
        }
        this.mProfileHeader = new ProfileHeader(dpi.getVersion(), dpi.getProfileID());
        this.mOpaqueByteArray = opaqueArray;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(PROFILE_BYTE_ARRAY_STRUCTURE_LENGTH);
        buffer.put(this.mProfileHeader.toBytes());
        buffer.put(this.mOpaqueByteArray);
        return buffer.array();
    }

    public UUID getProfileID() {
        return this.mProfileHeader.getProfileID();
    }

    public void setProfileID(UUID profileID) {
        this.mProfileHeader.setProfileID(profileID);
    }

    public int getVersion() {
        return this.mProfileHeader.getVersion();
    }

    public void setVersion(int version) {
        this.mProfileHeader.setVersion(version);
    }

    public Date getTimeStampUTC() {
        return this.mProfileHeader.getTimeStampUTC();
    }

    public void setTimeStampUTC(long tsUTC) {
        this.mProfileHeader.setTimeStampUTC(tsUTC);
    }

    public byte[] getOpaqueByteArray() {
        return this.mOpaqueByteArray;
    }

    public void setOpaqueByteArray(byte[] opaqueByteArray) {
        this.mOpaqueByteArray = opaqueByteArray;
    }
}
