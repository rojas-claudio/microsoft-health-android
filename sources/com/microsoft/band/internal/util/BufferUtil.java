package com.microsoft.band.internal.util;

import android.os.Parcel;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import java.util.UUID;
/* loaded from: classes.dex */
public final class BufferUtil {
    public static final byte[] EMPTY = new byte[0];

    public static byte[] copy(byte[] source, int offset, int length) {
        validate(source, offset, length);
        if (length == 0) {
            return EMPTY;
        }
        byte[] copyOfSource = new byte[length];
        System.arraycopy(source, offset, copyOfSource, 0, length);
        return copyOfSource;
    }

    public static byte calculateLRC(byte seed, byte[] data, int offset, int length) {
        validate(data, offset, length);
        byte lrc = seed;
        for (int charIndex = offset; charIndex < offset + length; charIndex++) {
            lrc = (byte) (data[charIndex] ^ lrc);
        }
        return lrc;
    }

    public static byte calculateLRC(byte[] data, int offset, int length) {
        return calculateLRC((byte) 0, data, offset, length);
    }

    public static void validate(byte[] data, int offset, int length) throws IllegalArgumentException {
        if (data == null || data.length == 0) {
            if (offset != 0) {
                throw new IllegalArgumentException("Specified offset must be zero when data.length is zero");
            }
            if (length != 0) {
                throw new IllegalArgumentException("Specified length must be zero when data.length is zero");
            }
        } else if (offset < 0 || offset >= data.length) {
            throw new IllegalArgumentException("Specified offset out of bounds: " + offset);
        } else {
            if (offset + length > data.length) {
                throw new IllegalArgumentException("Specified length exceeds data buffer length at specified offset: " + length);
            }
        }
    }

    public static String toHexOctetString(byte[] data, int offset, int length) {
        validate(data, offset, length);
        StringBuffer sb = new StringBuffer();
        appendHexOctetString(sb, data, offset, length);
        return sb.toString();
    }

    public static String toHexString(byte[] data, int offset, int length) {
        validate(data, offset, length);
        StringBuffer sb = new StringBuffer();
        for (int byteIndex = offset; byteIndex < offset + length; byteIndex++) {
            sb.append(StringUtil.toHexOctet(data[byteIndex]));
        }
        return sb.toString();
    }

    public static StringBuffer appendHexOctetString(StringBuffer stringBuffer, byte[] data, int offset, int length) {
        if (stringBuffer == null) {
            throw new NullPointerException("StringBuffer cannot be null");
        }
        validate(data, offset, length);
        for (int byteIndex = offset; byteIndex < offset + length; byteIndex++) {
            stringBuffer.append(' ').append(StringUtil.toHexOctet(data[byteIndex]));
        }
        return stringBuffer;
    }

    public static ByteBuffer allocateLittleEndian(int capacity) {
        return ByteBuffer.allocate(capacity).order(ByteOrder.LITTLE_ENDIAN);
    }

    public static byte toByteBCD(int value) {
        byte digitLow = (byte) (value % 10);
        byte digitHigh = (byte) ((value / 10) % 10);
        return (byte) ((digitHigh << 4) | digitLow);
    }

    public static int write(byte[] data, Parcel parcel) {
        int length = data == null ? -1 : data.length;
        parcel.writeInt(length);
        if (data != null && length > 0) {
            for (byte byteValue : data) {
                parcel.writeByte(byteValue);
            }
        }
        return length;
    }

    public static byte[] obtain(Parcel parcel) {
        if (parcel == null) {
            throw new NullPointerException("Parcel cannot be null");
        }
        int length = parcel.readInt();
        if (length == 0) {
            return EMPTY;
        }
        if (length <= 0) {
            return null;
        }
        byte[] data = new byte[length];
        for (int byteIndex = 0; byteIndex < data.length; byteIndex++) {
            data[byteIndex] = parcel.readByte();
        }
        return data;
    }

    public static int length(byte[] buffer) {
        if (buffer == null) {
            return 0;
        }
        return buffer.length;
    }

    public static String getString(ByteBuffer buffer) throws BufferUnderflowException {
        int strLen = buffer.getInt();
        if (strLen == 0) {
            return "";
        }
        if (strLen <= 0) {
            return null;
        }
        char[] charArray = new char[strLen];
        for (int i = 0; i < strLen; i++) {
            charArray[i] = buffer.getChar();
        }
        String strValue = String.valueOf(charArray);
        return strValue;
    }

    public static void putString(ByteBuffer buffer, String strValue) throws BufferOverflowException, ReadOnlyBufferException {
        if (strValue == null) {
            buffer.putInt(-1);
            return;
        }
        buffer.putInt(strValue.length());
        for (int i = 0; i < strValue.length(); i++) {
            buffer.putChar(strValue.charAt(i));
        }
    }

    public static String getString(ByteBuffer buffer, int length) {
        String strValue = null;
        int strLen = 0;
        if (length > 0) {
            int startPosition = buffer.position();
            char[] charArray = new char[length];
            while (strLen < charArray.length) {
                charArray[strLen] = buffer.getChar();
                if (charArray[strLen] == 0) {
                    break;
                }
                strLen++;
            }
            strValue = String.valueOf(charArray, 0, strLen);
            int leftOver = ((length * 2) + startPosition) - buffer.position();
            if (leftOver > 0) {
                buffer.position(buffer.position() + leftOver);
            }
        }
        return strValue;
    }

    public static UUID getUUID(ByteBuffer buffer) {
        long uuidLeastSigBits = 0;
        byte[] mostSigBytes = new byte[8];
        buffer.get(mostSigBytes);
        long uuidMostSigBits = mostSigBytes[3] & 255;
        long uuidMostSigBits2 = (((((((((((((uuidMostSigBits << 8) | (mostSigBytes[2] & 255)) << 8) | (mostSigBytes[1] & 255)) << 8) | (mostSigBytes[0] & 255)) << 8) | (mostSigBytes[5] & 255)) << 8) | (mostSigBytes[4] & 255)) << 8) | (mostSigBytes[7] & 255)) << 8) | (mostSigBytes[6] & 255);
        for (int i = 0; i < 8; i++) {
            uuidLeastSigBits = (uuidLeastSigBits << 8) | (buffer.get() & 255);
        }
        UUID uuid = new UUID(uuidMostSigBits2, uuidLeastSigBits);
        return uuid;
    }

    public static void putUUID(ByteBuffer buffer, UUID uuid) {
        long uuidMostSigBits = uuid == null ? 0L : uuid.getMostSignificantBits();
        long uuidLeastSigBits = uuid != null ? uuid.getLeastSignificantBits() : 0L;
        buffer.put((byte) ((1095216660480L & uuidMostSigBits) >> 32));
        buffer.put((byte) ((280375465082880L & uuidMostSigBits) >> 40));
        buffer.put((byte) ((71776119061217280L & uuidMostSigBits) >> 48));
        buffer.put((byte) ((uuidMostSigBits & (-72057594037927936L)) >> 56));
        buffer.put((byte) ((16711680 & uuidMostSigBits) >> 16));
        buffer.put((byte) ((4278190080L & uuidMostSigBits) >> 24));
        buffer.put((byte) (255 & uuidMostSigBits));
        buffer.put((byte) ((65280 & uuidMostSigBits) >> 8));
        for (int i = 0; i < 8; i++) {
            buffer.put((byte) ((uuidLeastSigBits & (-72057594037927936L)) >> 56));
            uuidLeastSigBits <<= 8;
        }
    }

    private BufferUtil() {
    }
}
