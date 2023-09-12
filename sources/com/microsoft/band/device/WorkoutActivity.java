package com.microsoft.band.device;

import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.UUIDHelper;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.util.StringHelper;
import java.nio.ByteBuffer;
import java.util.UUID;
/* loaded from: classes.dex */
public final class WorkoutActivity {
    public static final int MAXIMUM_WORKOUT_ACTIVITY_TYPES = 15;
    public static final int MAXIMUM_WORKOUT_ACTIVITY_TYPE_NAME_WIDECHARACTERS = 40;
    public static final int WORKOUT_ACTIVITY_STRUCTURE_SIZE = 1724;
    private static final int WORKOUT_ACTIVITY_TYPE_DATA_RESERVED_BYTES = 9;
    public static final int WORKOUT_ACTIVITY_TYPE_DATA_STRUCTURE_SIZE = 114;
    private int mAlgorithmFlags;
    private int mFlags;
    private UUID mId;
    private String mName;
    private short mTrackingAlgorithmId;

    public WorkoutActivity(String name, UUID id) {
        setName(name);
        setId(id);
    }

    public WorkoutActivity(ByteBuffer buffer) {
        byte[] nameByte = new byte[80];
        buffer.get(nameByte);
        this.mName = StringHelper.valueOf(nameByte).trim();
        byte[] guidArray = new byte[16];
        buffer.get(guidArray);
        this.mId = UUIDHelper.guidByteArrayToUuid(guidArray);
        this.mFlags = buffer.getInt();
        this.mAlgorithmFlags = buffer.getInt();
        this.mTrackingAlgorithmId = BitHelper.unsignedByteToShort(buffer.get());
        byte[] reserved = new byte[9];
        buffer.get(reserved);
    }

    public byte[] toBytes() {
        byte[] nameBytes = StringHelper.getBytes(this.mName);
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(WORKOUT_ACTIVITY_TYPE_DATA_STRUCTURE_SIZE);
        buffer.put(nameBytes);
        buffer.position(80);
        buffer.put(UUIDHelper.toGuidArray(this.mId)).putInt(this.mFlags).putInt(this.mAlgorithmFlags).put(BitHelper.shortToUnsignedByte(this.mTrackingAlgorithmId));
        return buffer.array();
    }

    public String getName() {
        return this.mName;
    }

    public WorkoutActivity setName(String name) {
        Validation.validateNullParameter(name, "Workout activity name");
        Validation.validateStringEmptyOrWhiteSpace(name, "Workout activity name");
        this.mName = StringUtil.truncateString(name, 39);
        return this;
    }

    public UUID getId() {
        return this.mId;
    }

    public WorkoutActivity setId(UUID id) {
        Validation.validateNullParameter(id, "Workout activity id");
        this.mId = id;
        return this;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public WorkoutActivity setFlags(int flags) {
        Validation.validateUINT32("Workout activity flags", flags);
        this.mFlags = flags;
        return this;
    }

    public int getAlgorithmFlags() {
        return this.mAlgorithmFlags;
    }

    public WorkoutActivity setAlgorithmFlags(int algorithmFlags) {
        Validation.validateUINT32("Workout activity algorithm flags", algorithmFlags);
        this.mAlgorithmFlags = algorithmFlags;
        return this;
    }

    public short getTrackingAlgorithmId() {
        return this.mTrackingAlgorithmId;
    }

    public WorkoutActivity setTrackingAlgorithmId(short trackingAlgorithmId) {
        Validation.validateUINT8("Workout activity trackingAlgorithmId", trackingAlgorithmId);
        this.mTrackingAlgorithmId = trackingAlgorithmId;
        return this;
    }
}
