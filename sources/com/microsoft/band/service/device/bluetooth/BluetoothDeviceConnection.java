package com.microsoft.band.service.device.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import com.microsoft.band.internal.util.BluetoothAdapterHelper;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/* loaded from: classes.dex */
public class BluetoothDeviceConnection {
    private static final int MAX_CONNECT_RETRIES = 2;
    public static final int MSG_CONNECTION_TO_ANOTHER_DEVICE = 1001;
    public static final int MSG_DEVICE_CONNECTION_FAILED = 1000;
    static final String TAG = BluetoothDeviceConnection.class.getSimpleName();
    protected static final ExecutorService mExecutorService = Executors.newCachedThreadPool();
    private final BluetoothDevice mDevice;
    protected final WeakReference<Handler> mEventHandlerRef;
    private volatile InputStream mInStream;
    private final BluetoothDeviceConnectionListener mListener;
    private volatile OutputStream mOutStream;
    protected volatile InputStreamReaderThread mReaderThread;
    private volatile BluetoothSocket mSocket;
    private final SocketConnectionTask mSocketConnectionTask;
    private final Object mSyncRoot = new Object();
    private volatile UUID mUuid;

    /* loaded from: classes.dex */
    public interface BluetoothDeviceConnectionListener {
        boolean onBluetoothDeviceBound(BluetoothDeviceConnection bluetoothDeviceConnection);

        boolean onBluetoothDeviceConnected(BluetoothDeviceConnection bluetoothDeviceConnection);

        void onBluetoothDeviceDataReceived(BluetoothDeviceConnection bluetoothDeviceConnection, byte[] bArr, int i);

        void onBluetoothDeviceDisconnected(BluetoothDeviceConnection bluetoothDeviceConnection);

        void onBluetoothDeviceError(BluetoothDeviceConnection bluetoothDeviceConnection, Exception exc);
    }

    public BluetoothDeviceConnection(Handler eventHandler, BluetoothDevice device, final UUID serviceUUID, BluetoothDeviceConnectionListener listener) {
        if (device == null) {
            throw new NullPointerException("device argument is null");
        }
        if (listener == null) {
            throw new NullPointerException("listener argument is null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("eventHandler argument is null");
        }
        this.mEventHandlerRef = new WeakReference<>(eventHandler);
        this.mSocketConnectionTask = new SocketConnectionTask();
        this.mDevice = device;
        this.mListener = listener;
        this.mUuid = serviceUUID;
        postToEventHandler(new Runnable() { // from class: com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.1
            @Override // java.lang.Runnable
            public void run() {
                if (BluetoothDeviceConnection.this.getListener().onBluetoothDeviceBound(BluetoothDeviceConnection.this) && serviceUUID != null) {
                    BluetoothDeviceConnection.this.connect(serviceUUID);
                }
            }
        });
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    public UUID getUUID() {
        return this.mUuid;
    }

    public BluetoothSocket getSocket() {
        return this.mSocket;
    }

    public InputStream getInputStream() {
        return this.mInStream;
    }

    public OutputStream getOutputStream() {
        return this.mOutStream;
    }

    public Handler getEventHandler() {
        return this.mEventHandlerRef.get();
    }

    public String getDeviceName() {
        BluetoothDevice bluetoothDevice = getDevice();
        return bluetoothDevice == null ? "" : bluetoothDevice.getName();
    }

    public String getDeviceAddress() {
        BluetoothDevice bluetoothDevice = getDevice();
        return bluetoothDevice == null ? "" : bluetoothDevice.getAddress();
    }

    public boolean isConnected() {
        BluetoothSocket socket = this.mSocket;
        return socket != null && socket.isConnected();
    }

    public boolean isConnecting() {
        return this.mSocketConnectionTask.isConnecting();
    }

    public boolean isIdle() {
        return (isConnected() || isConnecting()) ? false : true;
    }

    public boolean connect() {
        return connect(getUUID());
    }

    public boolean connect(UUID uuid) {
        if (isIdle()) {
            synchronized (this.mSyncRoot) {
                if (isIdle()) {
                    this.mSocketConnectionTask.connectToSocket(uuid);
                }
            }
        } else {
            String str = TAG;
            Object[] objArr = new Object[2];
            objArr[0] = this;
            objArr[1] = isConnecting() ? "connecting..." : "connected.";
            KDKLog.i(str, "%sconnect() called while %s", objArr);
        }
        return isConnecting() || isConnected();
    }

    public void disconnect() {
        disconnect(false);
    }

    public void disconnect(boolean reconnect) {
        synchronized (this.mSyncRoot) {
            this.mSocketConnectionTask.cancel();
            closeSocket();
        }
        if (reconnect) {
            reconnect();
        }
    }

    public void reconnect() {
        if (!isConnected()) {
            sendMessageToEventHandler(1001, this);
        }
    }

    public String toString() {
        BluetoothDevice device = getDevice();
        StringBuilder sb = new StringBuilder();
        sb.append("Bluetooth Device: ").append(device.getName()).append(", addr: ").append(device.getAddress());
        if (isConnected()) {
            sb.append(", connected: ").append(getUUID());
        }
        return sb.toString();
    }

    public boolean startReceivingData() {
        if (isConnected() && !isReceivingData()) {
            synchronized (this.mSyncRoot) {
                if (isConnected() && !isReceivingData()) {
                    try {
                        this.mReaderThread = newInputStreamReaderThread();
                        this.mReaderThread.start();
                    } catch (Exception e) {
                        disconnect();
                        onConnectionError(e);
                    }
                }
            }
        }
        return isReceivingData();
    }

    public boolean isReceivingData() {
        InputStreamReaderThread readerThread = this.mReaderThread;
        return readerThread != null && readerThread.isRunning();
    }

    public void stopReceivingData() {
        if (isReceivingData()) {
            synchronized (this.mSyncRoot) {
                if (isReceivingData()) {
                    this.mReaderThread.stopRunning();
                    this.mReaderThread = null;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BluetoothDeviceConnectionListener getListener() {
        return this.mListener;
    }

    protected void attachSocket(BluetoothSocket socket, UUID uuid) {
        synchronized (this.mSyncRoot) {
            closeSocket();
            if (isConnecting() && socket != null) {
                openSocket(socket, uuid);
            }
        }
    }

    private void openSocket(BluetoothSocket socket, UUID uuid) {
        this.mUuid = uuid;
        if (socket != null) {
            try {
                this.mInStream = socket.getInputStream();
                this.mOutStream = socket.getOutputStream();
                this.mSocket = socket;
            } catch (IOException e) {
                KDKLog.e(TAG, "Failed to attach to socket: %s", e.getMessage());
                StreamUtils.closeQuietly(this.mInStream);
                this.mInStream = null;
                StreamUtils.closeQuietly(this.mOutStream);
                this.mOutStream = null;
                StreamUtils.closeQuietly(socket);
            }
            if (this.mSocket != null) {
                KDKLog.i(TAG, "%s, attached socket.", this);
                postToEventHandler(new Runnable() { // from class: com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (BluetoothDeviceConnection.this.getListener().onBluetoothDeviceConnected(BluetoothDeviceConnection.this)) {
                            BluetoothDeviceConnection.this.startReceivingData();
                        }
                    }
                });
            }
        }
    }

    private void closeSocket() {
        BluetoothSocket oldSocket = this.mSocket;
        this.mSocket = null;
        if (oldSocket != null) {
            stopReceivingData();
            StreamUtils.closeQuietly(this.mInStream);
            this.mInStream = null;
            StreamUtils.closeQuietly(this.mOutStream);
            this.mOutStream = null;
            StreamUtils.closeQuietly(oldSocket);
            KDKLog.i(TAG, "%s, closing socket for device %s", this, getDevice());
            postToEventHandler(new Runnable() { // from class: com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.3
                @Override // java.lang.Runnable
                public void run() {
                    BluetoothDeviceConnection.this.getListener().onBluetoothDeviceDisconnected(BluetoothDeviceConnection.this);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onConnectionError(final Exception socketException) {
        KDKLog.e(TAG, "%s, exception: %s", this, socketException.getMessage());
        sendMessageToEventHandler(1000, this);
        postToEventHandler(new Runnable() { // from class: com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.4
            @Override // java.lang.Runnable
            public void run() {
                BluetoothDeviceConnection.this.getListener().onBluetoothDeviceError(BluetoothDeviceConnection.this, socketException);
            }
        });
    }

    protected void sendMessageToEventHandler(int messageId, Object data) {
        Handler handler = getEventHandler();
        if (handler != null) {
            try {
                handler.obtainMessage(messageId, data).sendToTarget();
            } catch (RuntimeException e) {
            }
        }
    }

    protected void postToEventHandler(Runnable runnable) {
        Handler handler = getEventHandler();
        if (handler != null) {
            try {
                handler.post(runnable);
            } catch (RuntimeException e) {
            }
        }
    }

    protected InputStreamReaderThread newInputStreamReaderThread() {
        return new BluetoothSocketReaderThread(this, getInputStream());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onDataReceived(byte[] buffer, int bytesRead) {
        try {
            getListener().onBluetoothDeviceDataReceived(this, buffer, bytesRead);
        } catch (Exception e) {
            KDKLog.e(TAG, e.getMessage(), e);
            disconnect(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SocketConnectionTask implements Callable<BluetoothSocket> {
        private volatile Future<BluetoothSocket> mFuture;
        private volatile UUID mServiceUUID;

        protected SocketConnectionTask() {
        }

        public void connectToSocket(UUID serviceUUID) {
            if (isConnecting()) {
                throw new IllegalStateException(String.format("Already connecting to %s", this));
            }
            this.mServiceUUID = serviceUUID;
            this.mFuture = BluetoothDeviceConnection.mExecutorService.submit(this);
        }

        public boolean isConnecting() {
            Future<BluetoothSocket> future = this.mFuture;
            return (future == null || future.isCancelled() || future.isDone()) ? false : true;
        }

        public void cancel() {
            Future<BluetoothSocket> future = this.mFuture;
            if (future != null && !future.isDone()) {
                KDKLog.w(BluetoothDeviceConnection.TAG, "%s Canceling connection task..", this);
                future.cancel(false);
                try {
                    future.get();
                } catch (InterruptedException e) {
                    KDKLog.w(BluetoothDeviceConnection.TAG, e, "%s, connection interrupted.", this);
                } catch (CancellationException e2) {
                } catch (ExecutionException e3) {
                    KDKLog.e(BluetoothDeviceConnection.TAG, e3, "%s, connection exception.", this);
                }
            }
        }

        public boolean isCancelled() {
            Future<?> future = this.mFuture;
            return future != null && future.isCancelled();
        }

        public String toString() {
            return String.format(Locale.getDefault(), "%s@%d, UUID: %s", BluetoothDeviceConnection.this, Integer.valueOf(BluetoothDeviceConnection.this.hashCode()), this.mServiceUUID);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        public BluetoothSocket call() {
            BluetoothAdapterHelper.cancelDiscovery();
            BluetoothSocket socket = null;
            int retries = 0;
            while (true) {
                if (isCancelled()) {
                    break;
                }
                try {
                    KDKLog.i(BluetoothDeviceConnection.TAG, "%s, connecting to socket for device %s", this, BluetoothDeviceConnection.this.getDevice());
                    socket = BluetoothAdapterHelper.createSocket(BluetoothDeviceConnection.this.getDevice(), this.mServiceUUID);
                    if (isCancelled()) {
                        KDKLog.i(BluetoothDeviceConnection.TAG, "%s, connection was cancelled", this);
                        StreamUtils.closeQuietly(socket);
                        socket = null;
                    } else {
                        socket.connect();
                        BluetoothDeviceConnection.this.attachSocket(socket, this.mServiceUUID);
                    }
                } catch (Exception e) {
                    KDKLog.w(BluetoothDeviceConnection.TAG, "%s, attempt to connect to socket failed: %s", this, e.getMessage());
                    StreamUtils.closeQuietly(socket);
                    socket = null;
                    int retries2 = retries + 1;
                    if (retries >= 2) {
                        BluetoothDeviceConnection.this.onConnectionError(e);
                        break;
                    }
                    KDKLog.w(BluetoothDeviceConnection.TAG, "Retrying to connect %d of %d times...", Integer.valueOf(retries2), 2);
                    retries = retries2;
                }
            }
            return socket;
        }
    }
}
