package com.google.android.gms.common;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/* loaded from: classes.dex */
public class a implements ServiceConnection {
    boolean iN = false;
    private final BlockingQueue<IBinder> iO = new LinkedBlockingQueue();

    public IBinder aG() throws InterruptedException {
        if (this.iN) {
            throw new IllegalStateException();
        }
        this.iN = true;
        return this.iO.take();
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName name, IBinder service) {
        try {
            this.iO.put(service);
        } catch (InterruptedException e) {
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName name) {
    }
}
