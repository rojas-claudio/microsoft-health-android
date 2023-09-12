package com.microsoft.band.service.device.bluetooth;

import com.microsoft.band.internal.util.KDKLog;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
class BluetoothSocketReaderThread extends InputStreamReaderThread {
    protected static final String TAG = BluetoothSocketReaderThread.class.getSimpleName();
    private final BluetoothDeviceConnection mConnection;

    public BluetoothSocketReaderThread(BluetoothDeviceConnection connection, InputStream inStream) {
        super(inStream);
        if (connection == null) {
            throw new NullPointerException("connection argument is null");
        }
        this.mConnection = connection;
    }

    @Override // com.microsoft.band.service.device.bluetooth.InputStreamReaderThread
    public boolean isValid() {
        return this.mConnection.isConnected();
    }

    @Override // com.microsoft.band.service.device.bluetooth.InputStreamReaderThread
    protected void onDataReceived(byte[] buffer, int bytesRead) {
        this.mConnection.onDataReceived(buffer, bytesRead);
    }

    @Override // com.microsoft.band.service.device.bluetooth.InputStreamReaderThread
    protected void onReadError(IOException e) {
        try {
            this.mConnection.onConnectionError(e);
        } finally {
            this.mConnection.disconnect();
        }
    }

    @Override // com.microsoft.band.service.device.bluetooth.InputStreamReaderThread, java.lang.Thread, java.lang.Runnable
    public void run() {
        KDKLog.i(TAG, this.mConnection + ", Reader thread started.");
        try {
            super.run();
        } finally {
            KDKLog.i(TAG, this.mConnection + ", Reader thread stopped.");
        }
    }
}
