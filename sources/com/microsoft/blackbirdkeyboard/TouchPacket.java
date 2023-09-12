package com.microsoft.blackbirdkeyboard;
/* loaded from: classes.dex */
public class TouchPacket {
    static final int CONTACTSTATE_DOWN = 3;
    static final int CONTACTSTATE_INAIRMOVE = 0;
    static final int CONTACTSTATE_MOVE = 1;
    static final int CONTACTSTATE_UP = 2;
    static final int SCALE_ACCEL_ACCEL = 1000000;
    static final int SCALE_ACCEL_TIME = 1000000;
    static final int SCALE_ORIENTATION = 1000;
    static final int SCALE_PRESSURE = 255;
    public long accelTime;
    public float accelX;
    public float accelY;
    public float accelZ;
    public float axisLengthMajor;
    public float axisLengthMinor;
    public boolean contact;
    public boolean contactChanged;
    public int hid;
    public float orientation;
    public int pid;
    public float pressure;
    public long time;
    public float x;
    public float y;

    public boolean IsGenuinePacket() {
        return this.contactChanged || this.hid > 0;
    }

    public static boolean isInContact(int actionMasked) {
        switch (actionMasked) {
            case 0:
            case 2:
            case 5:
                return true;
            case 1:
            case 3:
            case 6:
            case 7:
            case 9:
            case 10:
                return false;
            case 4:
            case 8:
            default:
                throw new RuntimeException("isInContact: Unknown actionMasked " + actionMasked);
        }
    }

    public static boolean isInContactChanged(int actionMasked) {
        switch (actionMasked) {
            case 0:
            case 1:
            case 3:
            case 5:
            case 6:
                return true;
            case 2:
            case 7:
            case 9:
            case 10:
                return false;
            case 4:
            case 8:
            default:
                throw new RuntimeException("isInContact: Unknown actionMasked " + actionMasked);
        }
    }

    int contactState() {
        return (this.contact ? 1 : 0) + (this.contactChanged ? 2 : 0);
    }

    int[] toIntArray() {
        int[] packetInt = {Math.round(this.x), Math.round(this.y), (int) this.time, contactState(), this.pid, this.hid, Math.round(this.axisLengthMajor), Math.round(this.axisLengthMinor), (int) Math.round(((1000.0f * this.orientation) * 180.0f) / 3.141592653589793d), Math.round(this.pressure * 255.0f), Math.round(this.accelX * 1000000.0f), Math.round(this.accelY * 1000000.0f), Math.round(this.accelZ * 1000000.0f), (int) (this.accelTime / 1000000)};
        return packetInt;
    }
}
