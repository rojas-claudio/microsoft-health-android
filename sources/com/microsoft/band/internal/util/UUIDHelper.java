package com.microsoft.band.internal.util;

import java.nio.ByteBuffer;
import java.util.UUID;
/* loaded from: classes.dex */
public final class UUIDHelper {
    private static final String TAG = UUIDHelper.class.getSimpleName();
    public static final int UUID_SIZE = 16;

    private UUIDHelper() {
        throw new UnsupportedOperationException();
    }

    public static byte[] toGuidArray(UUID id) {
        byte[] uuidArray = toUuidByteArray(id);
        return uuidToGuidByteArray(uuidArray);
    }

    public static byte[] toGuidArray(String uuidString) {
        byte[] uuidArray = toUuidByteArray(uuidString);
        return uuidToGuidByteArray(uuidArray);
    }

    private static byte[] uuidToGuidByteArray(byte[] uuidArray) {
        byte[] guidArray = new byte[16];
        for (int i = 8; i < 16; i++) {
            guidArray[i] = uuidArray[i];
        }
        guidArray[0] = uuidArray[3];
        guidArray[1] = uuidArray[2];
        guidArray[2] = uuidArray[1];
        guidArray[3] = uuidArray[0];
        guidArray[4] = uuidArray[5];
        guidArray[5] = uuidArray[4];
        guidArray[6] = uuidArray[7];
        guidArray[7] = uuidArray[6];
        return guidArray;
    }

    public static UUID guidByteArrayToUuid(byte[] guidArray) {
        byte[] uuidArray = new byte[16];
        for (int i = 8; i < 16; i++) {
            uuidArray[i] = guidArray[i];
        }
        uuidArray[0] = guidArray[3];
        uuidArray[1] = guidArray[2];
        uuidArray[2] = guidArray[1];
        uuidArray[3] = guidArray[0];
        uuidArray[4] = guidArray[5];
        uuidArray[5] = guidArray[4];
        uuidArray[6] = guidArray[7];
        uuidArray[7] = guidArray[6];
        return toUUIDFromBytes(uuidArray);
    }

    private static byte[] toUuidByteArray(UUID id) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(id.getMostSignificantBits());
        bb.putLong(id.getLeastSignificantBits());
        return bb.array();
    }

    private static byte[] toUuidByteArray(String uuidString) {
        String str = uuidString.replaceAll("-", "");
        int len = str.length();
        byte[] data = new byte[len];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return data;
    }

    private static UUID toUUIDFromBytes(byte[] b) throws IllegalArgumentException {
        if (b.length != 16) {
            throw new IllegalArgumentException("A UUID can only be constructed from a 16 byte array.");
        }
        long msb = (b[7] & 255) + ((b[6] & 255) << 8) + ((b[5] & 255) << 16) + ((b[4] & 255) << 24) + ((b[3] & 255) << 32) + ((b[2] & 255) << 40) + ((b[1] & 255) << 48) + ((b[0] & 255) << 56);
        long lsb = (b[15] & 255) + ((b[14] & 255) << 8) + ((b[13] & 255) << 16) + ((b[12] & 255) << 24) + ((b[11] & 255) << 32) + ((b[10] & 255) << 40) + ((b[9] & 255) << 48) + ((b[8] & 255) << 56);
        return new UUID(msb, lsb);
    }

    public static UUID getUUIDFromSerialNumber(String serialNumber) {
        UUID uuid = null;
        try {
            if (serialNumber.length() != 12) {
                KDKLog.e(TAG, "The length of serialNumber must be 12: %s", serialNumber);
            } else {
                uuid = UUID.fromString("FFFFFFFF-FFFF-FFFF-FFFF-" + serialNumber);
            }
        } catch (Exception e) {
            KDKLog.e(TAG, "Unable to format serialNumber %s", serialNumber);
        }
        return uuid;
    }
}
