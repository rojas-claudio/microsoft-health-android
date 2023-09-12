package com.microsoft.band.service.device.bluetooth;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public abstract class InputStreamReaderThread extends Thread {
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    private final InputStream mInStream;
    private volatile boolean mIsRunning;
    private volatile boolean mIsStopping;
    private volatile byte[] mReadBuffer;

    protected abstract boolean isValid();

    protected abstract void onDataReceived(byte[] bArr, int i);

    protected abstract void onReadError(IOException iOException);

    /* JADX INFO: Access modifiers changed from: protected */
    public InputStreamReaderThread(InputStream inStream) {
        if (inStream == null) {
            throw new IllegalStateException("inStream not available.");
        }
        this.mInStream = inStream;
    }

    public byte[] setReadBufferSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        }
        byte[] buffer = this.mReadBuffer;
        if (buffer == null || buffer.length != size) {
            this.mReadBuffer = new byte[size];
        }
        return this.mReadBuffer;
    }

    public byte[] getReadBuffer() {
        if (this.mReadBuffer == null) {
            this.mReadBuffer = new byte[1024];
        }
        return this.mReadBuffer;
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    public boolean isStopping() {
        return this.mIsStopping;
    }

    public void stopRunning() {
        this.mIsStopping = true;
    }

    public InputStream getInputStream() {
        return this.mInStream;
    }

    @Override // java.lang.Thread
    public synchronized void start() {
        super.start();
        this.mIsRunning = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            InputStream inputStream = getInputStream();
            while (isValid() && !isStopping()) {
                byte[] buffer = getReadBuffer();
                int bytesRead = inputStream.read(buffer);
                if (bytesRead > 0 && isValid()) {
                    onDataReceived(buffer, bytesRead);
                }
            }
        } catch (IOException e) {
            if (isValid() && !isStopping()) {
                onReadError(e);
            }
        } finally {
            this.mIsRunning = false;
            this.mIsStopping = false;
        }
    }
}
