package com.microsoft.band.client;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.BandServiceMessage;
import java.io.Serializable;
/* loaded from: classes.dex */
public class SyncResult implements Parcelable, Serializable {
    public static final Parcelable.Creator<SyncResult> CREATOR = new Parcelable.Creator<SyncResult>() { // from class: com.microsoft.band.client.SyncResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SyncResult createFromParcel(Parcel in) {
            return new SyncResult(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SyncResult[] newArray(int size) {
            return new SyncResult[size];
        }
    };
    private static final long serialVersionUID = 1;
    private long mBytesSentToCloud;
    private int mChuncksDeletedFromDevice;
    private int mChuncksSentToCloud;
    private int mCloudProfileRetrievalResponseCode;
    private int mCrashdumpFileRetrievalResponseCode;
    private int mCrashdumpFileSendingResponseCode;
    private int mDeviceProfileRetrievalResponseCode;
    private long mDownloadDurationMillisTotal;
    private int mDownloadEphemerisResponseCode;
    private int mDownloadTimeZoneFileResponseCode;
    private int mLogSyncResponseCode;
    private int mPrivateByteArrayRetrievalResponseCode;
    private int mPrivateByteArraySavingResponseCode;
    private int mSyncDeviceTimeResponseCode;
    private int mSyncDeviceTimeZoneResponseCode;
    private int mTelemetryFileRetrievalResponseCode;
    private int mTelemetryFileSendingResponseCode;
    private int mUpgradeEphemerisResponseCode;
    private int mUpgradeTimeZoneFileResponseCode;
    private long mUploadDurationMillisTotal;
    private byte mUserProfileSyncValue;

    public SyncResult() {
        this.mBytesSentToCloud = 0L;
        this.mDownloadDurationMillisTotal = 0L;
        this.mUploadDurationMillisTotal = 0L;
        this.mChuncksSentToCloud = 0;
        this.mChuncksDeletedFromDevice = 0;
        this.mUserProfileSyncValue = (byte) 0;
        this.mSyncDeviceTimeResponseCode = 0;
        this.mSyncDeviceTimeZoneResponseCode = 0;
        this.mPrivateByteArrayRetrievalResponseCode = 0;
        this.mDeviceProfileRetrievalResponseCode = 0;
        this.mCloudProfileRetrievalResponseCode = 0;
        this.mPrivateByteArraySavingResponseCode = 0;
        this.mDownloadEphemerisResponseCode = 0;
        this.mUpgradeEphemerisResponseCode = 0;
        this.mDownloadTimeZoneFileResponseCode = 0;
        this.mUpgradeTimeZoneFileResponseCode = 0;
        this.mCrashdumpFileRetrievalResponseCode = 0;
        this.mCrashdumpFileSendingResponseCode = 0;
        this.mTelemetryFileRetrievalResponseCode = 0;
        this.mTelemetryFileSendingResponseCode = 0;
        this.mLogSyncResponseCode = 0;
    }

    public long getBytesSentToCloud() {
        return this.mBytesSentToCloud;
    }

    public void setBytesSentToCloud(long bytesSentToCloud) {
        this.mBytesSentToCloud = bytesSentToCloud;
    }

    public long getUploadDurationMillisTotal() {
        return this.mUploadDurationMillisTotal;
    }

    public void setUploadDurationMillisTotal(long uploadDurationMillisTotal) {
        this.mUploadDurationMillisTotal = uploadDurationMillisTotal;
    }

    public long getDownloadDurationMillisTotal() {
        return this.mDownloadDurationMillisTotal;
    }

    public void setDownloadDurationMillisTotal(long downloadDurationMillisTotal) {
        this.mDownloadDurationMillisTotal = downloadDurationMillisTotal;
    }

    public float getDownloadFromDeviceRate() {
        if (this.mDownloadDurationMillisTotal != 0) {
            return ((float) this.mBytesSentToCloud) / ((float) this.mDownloadDurationMillisTotal);
        }
        return 0.0f;
    }

    public float getUploadToCloudRate() {
        if (this.mUploadDurationMillisTotal != 0) {
            return ((float) this.mBytesSentToCloud) / ((float) this.mUploadDurationMillisTotal);
        }
        return 0.0f;
    }

    public int getChuncksSentToCloud() {
        return this.mChuncksSentToCloud;
    }

    public void setChuncksSentToCloud(int chuncksSentToCloud) {
        this.mChuncksSentToCloud = chuncksSentToCloud;
    }

    public int getChuncksDeletedFromDevice() {
        return this.mChuncksDeletedFromDevice;
    }

    public void setChuncksDeletedFromDevice(int chuncksDeletedFromDevice) {
        this.mChuncksDeletedFromDevice = chuncksDeletedFromDevice;
    }

    public byte getUserProfileSyncValue() {
        return this.mUserProfileSyncValue;
    }

    public void setUserProfileSyncValue(byte userProfileSyncValue) {
        this.mUserProfileSyncValue = userProfileSyncValue;
    }

    public int getSyncDeviceTimeResponseCode() {
        return this.mSyncDeviceTimeResponseCode;
    }

    public void setSyncDeviceTimeResponseCode(int syncDeviceTimeResponseCode) {
        this.mSyncDeviceTimeResponseCode = syncDeviceTimeResponseCode;
    }

    public int getSyncDeviceTimeZoneResponseCode() {
        return this.mSyncDeviceTimeZoneResponseCode;
    }

    public void setSyncDeviceTimeZoneResponseCode(int syncDeviceTimeZoneResponseCode) {
        this.mSyncDeviceTimeZoneResponseCode = syncDeviceTimeZoneResponseCode;
    }

    public int getPrivateByteArrayRetrievalResponseCode() {
        return this.mPrivateByteArrayRetrievalResponseCode;
    }

    public void setPrivateByteArrayRetrievalResponseCode(int privateByteArrayRetrievalResponseCode) {
        this.mPrivateByteArrayRetrievalResponseCode = privateByteArrayRetrievalResponseCode;
    }

    public int getDeviceProfileRetrievalResponseCode() {
        return this.mDeviceProfileRetrievalResponseCode;
    }

    public void setDeviceProfileRetrievalResponseCode(int deviceProfileRetrievalResponseCode) {
        this.mDeviceProfileRetrievalResponseCode = deviceProfileRetrievalResponseCode;
    }

    public int getCloudProfileRetrievalResponseCode() {
        return this.mCloudProfileRetrievalResponseCode;
    }

    public void setCloudProfileRetrievalResponseCode(int cloudProfileRetrievalResponseCode) {
        this.mCloudProfileRetrievalResponseCode = cloudProfileRetrievalResponseCode;
    }

    public int getPrivateByteArraySavingResponseCode() {
        return this.mPrivateByteArraySavingResponseCode;
    }

    public void setPrivateByteArraySavingResponseCode(int privateByteArraySavingResponseCode) {
        this.mPrivateByteArraySavingResponseCode = privateByteArraySavingResponseCode;
    }

    public int getDownloadEphemerisResponseCode() {
        return this.mDownloadEphemerisResponseCode;
    }

    public void setDownloadEphemerisResponseCode(int downloadEphemerisResponseCode) {
        this.mDownloadEphemerisResponseCode = downloadEphemerisResponseCode;
    }

    public int getUpgradeEphemerisResponseCode() {
        return this.mUpgradeEphemerisResponseCode;
    }

    public void setUpgradeEphemerisResponseCode(int upgradeEphemerisResponseCode) {
        this.mUpgradeEphemerisResponseCode = upgradeEphemerisResponseCode;
    }

    public int getDownloadTimeZoneFileResponseCode() {
        return this.mDownloadTimeZoneFileResponseCode;
    }

    public void setDownloadTimeZoneFileResponseCode(int downloadTimeZoneFileResponseCode) {
        this.mDownloadTimeZoneFileResponseCode = downloadTimeZoneFileResponseCode;
    }

    public int getUpgradeTimeZoneFileResponseCode() {
        return this.mUpgradeTimeZoneFileResponseCode;
    }

    public void setUpgradeTimeZoneFileResponseCode(int upgradeTimeZoneFileResponseCode) {
        this.mUpgradeTimeZoneFileResponseCode = upgradeTimeZoneFileResponseCode;
    }

    public int getCrashdumpFileRetrievalResponseCode() {
        return this.mCrashdumpFileRetrievalResponseCode;
    }

    public void setCrashdumpFileRetrievalResponseCode(int crashdumpFileRetrievalResponseCode) {
        this.mCrashdumpFileRetrievalResponseCode = crashdumpFileRetrievalResponseCode;
    }

    public int getCrashdumpFileSendingResponseCode() {
        return this.mCrashdumpFileSendingResponseCode;
    }

    public void setCrashdumpFileSendingResponseCode(int crashdumpFileSendingResponseCode) {
        this.mCrashdumpFileSendingResponseCode = crashdumpFileSendingResponseCode;
    }

    public int getTelemetryFileRetrievalResponseCode() {
        return this.mTelemetryFileRetrievalResponseCode;
    }

    public void setTelemetryFileRetrievalResponseCode(int telemetryFileRetrievalResponseCode) {
        this.mTelemetryFileRetrievalResponseCode = telemetryFileRetrievalResponseCode;
    }

    public int getTelemetryFileSendingResponseCode() {
        return this.mTelemetryFileSendingResponseCode;
    }

    public void setTelemetryFileSendingResponseCode(int telemetryFileSendingResponseCode) {
        this.mTelemetryFileSendingResponseCode = telemetryFileSendingResponseCode;
    }

    public int getLogSyncResponseCode() {
        return this.mLogSyncResponseCode;
    }

    public void setLogSyncResponseCode(int logSyncResponseCode) {
        this.mLogSyncResponseCode = logSyncResponseCode;
    }

    public boolean wasSyncSuccess() {
        return !BandServiceMessage.Response.isErrorCode(this.mLogSyncResponseCode);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Sync SensorLog Successful:\t%s\n", Boolean.valueOf(wasSyncSuccess())));
        if (wasSyncSuccess()) {
            sb.append(String.format("Bytes Transferred:\t%d\n", Long.valueOf(this.mBytesSentToCloud)));
            sb.append(String.format("Download Time (ms):\t%d\n", Long.valueOf(this.mDownloadDurationMillisTotal)));
            sb.append(String.format("Upload Time (ms):\t%d\n", Long.valueOf(this.mUploadDurationMillisTotal)));
            sb.append(String.format("Chunks Sent To Cloud:\t%d\n", Integer.valueOf(this.mChuncksSentToCloud)));
            sb.append(String.format("Chunks Deleted From Device:\t%d\n", Integer.valueOf(this.mChuncksDeletedFromDevice)));
            sb.append(String.format("Download From Device Rate:\t%f (kb/second)\n", Float.valueOf(getDownloadFromDeviceRate())));
            sb.append(String.format("Upload To Cloud Rate:\t%f (kb/second)\n", Float.valueOf(getUploadToCloudRate())));
        }
        if (this.mSyncDeviceTimeResponseCode != 0) {
            Object[] objArr = new Object[1];
            objArr[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mSyncDeviceTimeResponseCode));
            sb.append(String.format("UTC Sync Time Completed Successfully: \t%s\n", objArr));
        }
        if (this.mSyncDeviceTimeZoneResponseCode != 0) {
            Object[] objArr2 = new Object[1];
            objArr2[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mSyncDeviceTimeZoneResponseCode));
            sb.append(String.format("Timezone Sync Completed Successfully: \t%s\n", objArr2));
        }
        if (this.mPrivateByteArrayRetrievalResponseCode != 0) {
            Object[] objArr3 = new Object[1];
            objArr3[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mPrivateByteArrayRetrievalResponseCode));
            sb.append(String.format("Private Byte Array Retrieved From Device Successfully: \t%s\n", objArr3));
        }
        if (this.mDeviceProfileRetrievalResponseCode != 0) {
            Object[] objArr4 = new Object[1];
            objArr4[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mDeviceProfileRetrievalResponseCode));
            sb.append(String.format("Device Profile Retrieved Successfully: \t%s\n", objArr4));
        }
        if (this.mCloudProfileRetrievalResponseCode != 0) {
            Object[] objArr5 = new Object[1];
            objArr5[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mCloudProfileRetrievalResponseCode));
            sb.append(String.format("Cloud Profile Retrieved Successfully: \t%s\n", objArr5));
        }
        switch (this.mUserProfileSyncValue) {
            case 1:
                sb.append("Cloud Profile Updated As Part of Firmware Byte Array Upload\n");
                break;
            case 2:
                sb.append("Profile Sync Was Not Needed\n");
                break;
            case 3:
                sb.append("Device Profile Updated\n");
                break;
            case 4:
                sb.append("Device Profile Update Failed\n");
                break;
        }
        if (this.mPrivateByteArraySavingResponseCode != 0) {
            Object[] objArr6 = new Object[1];
            objArr6[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mPrivateByteArraySavingResponseCode));
            sb.append(String.format("Private Byte Array Saved To Cloud Successfully: \t%s\n", objArr6));
        }
        if (this.mDownloadEphemerisResponseCode != 0 && this.mDownloadEphemerisResponseCode != BandServiceMessage.Response.OPERATION_NOT_REQUIRED.getCode()) {
            Object[] objArr7 = new Object[1];
            objArr7[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mDownloadEphemerisResponseCode));
            sb.append(String.format("Ephemeris File Downloaded Successfully: \t%s\n", objArr7));
        }
        if (this.mUpgradeEphemerisResponseCode != 0 && this.mUpgradeEphemerisResponseCode != BandServiceMessage.Response.OPERATION_NOT_REQUIRED.getCode()) {
            Object[] objArr8 = new Object[1];
            objArr8[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mUpgradeEphemerisResponseCode));
            sb.append(String.format("Ephemeris File Upgraded On Device Successfully: \t%s\n", objArr8));
        }
        if (this.mDownloadTimeZoneFileResponseCode != 0 && this.mDownloadTimeZoneFileResponseCode != BandServiceMessage.Response.OPERATION_NOT_REQUIRED.getCode()) {
            Object[] objArr9 = new Object[1];
            objArr9[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mDownloadTimeZoneFileResponseCode));
            sb.append(String.format("Timezone Settings File Downloaded Successfully: \t%s\n", objArr9));
        }
        if (this.mUpgradeTimeZoneFileResponseCode != 0 && this.mUpgradeTimeZoneFileResponseCode != BandServiceMessage.Response.OPERATION_NOT_REQUIRED.getCode()) {
            Object[] objArr10 = new Object[1];
            objArr10[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mUpgradeTimeZoneFileResponseCode));
            sb.append(String.format("Timezone Settings File Upgraded On Device Successfully: \t%s\n", objArr10));
        }
        if (this.mCrashdumpFileRetrievalResponseCode != 0) {
            Object[] objArr11 = new Object[1];
            objArr11[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mCrashdumpFileRetrievalResponseCode));
            sb.append(String.format("Crashdump Files Retrieved From Device Successfully: \t%s\n", objArr11));
        }
        if (this.mCrashdumpFileSendingResponseCode != 0 && this.mCrashdumpFileSendingResponseCode != BandServiceMessage.Response.OPERATION_NOT_REQUIRED.getCode()) {
            Object[] objArr12 = new Object[1];
            objArr12[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mCrashdumpFileSendingResponseCode));
            sb.append(String.format("Crashdump Files Sent to the Cloud Successfully: \t%s\n", objArr12));
        }
        if (this.mTelemetryFileRetrievalResponseCode != 0 && this.mTelemetryFileRetrievalResponseCode != BandServiceMessage.Response.OPERATION_NOT_REQUIRED.getCode()) {
            Object[] objArr13 = new Object[1];
            objArr13[0] = Boolean.valueOf(!BandServiceMessage.Response.isErrorCode(this.mTelemetryFileRetrievalResponseCode));
            sb.append(String.format("Telemetry Files Retrieved From Device Successfully: \t%s\n", objArr13));
        }
        if (this.mTelemetryFileSendingResponseCode != 0 && this.mTelemetryFileSendingResponseCode != BandServiceMessage.Response.OPERATION_NOT_REQUIRED.getCode()) {
            Object[] objArr14 = new Object[1];
            objArr14[0] = Boolean.valueOf(BandServiceMessage.Response.isErrorCode(this.mTelemetryFileSendingResponseCode) ? false : true);
            sb.append(String.format("Telemetry Files Sent to the Cloud Successfully: \t%s\n", objArr14));
        }
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mBytesSentToCloud);
        dest.writeLong(this.mDownloadDurationMillisTotal);
        dest.writeLong(this.mUploadDurationMillisTotal);
        dest.writeInt(this.mChuncksSentToCloud);
        dest.writeInt(this.mChuncksDeletedFromDevice);
        dest.writeByte(this.mUserProfileSyncValue);
        dest.writeInt(this.mSyncDeviceTimeResponseCode);
        dest.writeInt(this.mSyncDeviceTimeZoneResponseCode);
        dest.writeInt(this.mPrivateByteArrayRetrievalResponseCode);
        dest.writeInt(this.mDeviceProfileRetrievalResponseCode);
        dest.writeInt(this.mCloudProfileRetrievalResponseCode);
        dest.writeInt(this.mPrivateByteArraySavingResponseCode);
        dest.writeInt(this.mDownloadEphemerisResponseCode);
        dest.writeInt(this.mUpgradeEphemerisResponseCode);
        dest.writeInt(this.mDownloadTimeZoneFileResponseCode);
        dest.writeInt(this.mUpgradeTimeZoneFileResponseCode);
        dest.writeInt(this.mCrashdumpFileRetrievalResponseCode);
        dest.writeInt(this.mCrashdumpFileSendingResponseCode);
        dest.writeInt(this.mTelemetryFileRetrievalResponseCode);
        dest.writeInt(this.mTelemetryFileSendingResponseCode);
        dest.writeInt(this.mLogSyncResponseCode);
    }

    public SyncResult(Parcel in) {
        this.mBytesSentToCloud = 0L;
        this.mDownloadDurationMillisTotal = 0L;
        this.mUploadDurationMillisTotal = 0L;
        this.mChuncksSentToCloud = 0;
        this.mChuncksDeletedFromDevice = 0;
        this.mUserProfileSyncValue = (byte) 0;
        this.mSyncDeviceTimeResponseCode = 0;
        this.mSyncDeviceTimeZoneResponseCode = 0;
        this.mPrivateByteArrayRetrievalResponseCode = 0;
        this.mDeviceProfileRetrievalResponseCode = 0;
        this.mCloudProfileRetrievalResponseCode = 0;
        this.mPrivateByteArraySavingResponseCode = 0;
        this.mDownloadEphemerisResponseCode = 0;
        this.mUpgradeEphemerisResponseCode = 0;
        this.mDownloadTimeZoneFileResponseCode = 0;
        this.mUpgradeTimeZoneFileResponseCode = 0;
        this.mCrashdumpFileRetrievalResponseCode = 0;
        this.mCrashdumpFileSendingResponseCode = 0;
        this.mTelemetryFileRetrievalResponseCode = 0;
        this.mTelemetryFileSendingResponseCode = 0;
        this.mLogSyncResponseCode = 0;
        this.mBytesSentToCloud = in.readLong();
        this.mDownloadDurationMillisTotal = in.readLong();
        this.mUploadDurationMillisTotal = in.readLong();
        this.mChuncksSentToCloud = in.readInt();
        this.mChuncksDeletedFromDevice = in.readInt();
        this.mUserProfileSyncValue = in.readByte();
        this.mSyncDeviceTimeResponseCode = in.readInt();
        this.mSyncDeviceTimeZoneResponseCode = in.readInt();
        this.mPrivateByteArrayRetrievalResponseCode = in.readInt();
        this.mDeviceProfileRetrievalResponseCode = in.readInt();
        this.mCloudProfileRetrievalResponseCode = in.readInt();
        this.mPrivateByteArraySavingResponseCode = in.readInt();
        this.mDownloadEphemerisResponseCode = in.readInt();
        this.mUpgradeEphemerisResponseCode = in.readInt();
        this.mDownloadTimeZoneFileResponseCode = in.readInt();
        this.mUpgradeTimeZoneFileResponseCode = in.readInt();
        this.mCrashdumpFileRetrievalResponseCode = in.readInt();
        this.mCrashdumpFileSendingResponseCode = in.readInt();
        this.mTelemetryFileRetrievalResponseCode = in.readInt();
        this.mTelemetryFileSendingResponseCode = in.readInt();
        this.mLogSyncResponseCode = in.readInt();
    }
}
