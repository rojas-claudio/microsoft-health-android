package com.google.android.gms.gcm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class GoogleCloudMessaging {
    public static final String ERROR_MAIN_THREAD = "MAIN_THREAD";
    public static final String ERROR_SERVICE_NOT_AVAILABLE = "SERVICE_NOT_AVAILABLE";
    public static final String MESSAGE_TYPE_DELETED = "deleted_messages";
    public static final String MESSAGE_TYPE_MESSAGE = "gcm";
    public static final String MESSAGE_TYPE_SEND_ERROR = "send_error";
    static GoogleCloudMessaging oo;
    private Context ee;
    private PendingIntent op;
    final BlockingQueue<Intent> oq = new LinkedBlockingQueue();
    private Handler or = new Handler(Looper.getMainLooper()) { // from class: com.google.android.gms.gcm.GoogleCloudMessaging.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            GoogleCloudMessaging.this.oq.add((Intent) msg.obj);
        }
    };
    private Messenger os = new Messenger(this.or);

    private void b(String... strArr) {
        String c = c(strArr);
        Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
        intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE);
        intent.putExtra("google.messenger", this.os);
        d(intent);
        intent.putExtra("sender", c);
        this.ee.startService(intent);
    }

    private void cj() {
        Intent intent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
        intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE);
        this.oq.clear();
        intent.putExtra("google.messenger", this.os);
        d(intent);
        this.ee.startService(intent);
    }

    public static synchronized GoogleCloudMessaging getInstance(Context context) {
        GoogleCloudMessaging googleCloudMessaging;
        synchronized (GoogleCloudMessaging.class) {
            if (oo == null) {
                oo = new GoogleCloudMessaging();
                oo.ee = context;
            }
            googleCloudMessaging = oo;
        }
        return googleCloudMessaging;
    }

    String c(String... strArr) {
        if (strArr == null || strArr.length == 0) {
            throw new IllegalArgumentException("No senderIds");
        }
        StringBuilder sb = new StringBuilder(strArr[0]);
        for (int i = 1; i < strArr.length; i++) {
            sb.append(',').append(strArr[i]);
        }
        return sb.toString();
    }

    synchronized void ck() {
        if (this.op != null) {
            this.op.cancel();
            this.op = null;
        }
    }

    public void close() {
        ck();
    }

    synchronized void d(Intent intent) {
        if (this.op == null) {
            this.op = PendingIntent.getBroadcast(this.ee, 0, new Intent(), 0);
        }
        intent.putExtra("app", this.op);
    }

    public String getMessageType(Intent intent) {
        if ("com.google.android.c2dm.intent.RECEIVE".equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra("message_type");
            return stringExtra == null ? MESSAGE_TYPE_MESSAGE : stringExtra;
        }
        return null;
    }

    public String register(String... senderIds) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException(ERROR_MAIN_THREAD);
        }
        this.oq.clear();
        b(senderIds);
        try {
            Intent poll = this.oq.poll(5000L, TimeUnit.MILLISECONDS);
            if (poll == null) {
                throw new IOException(ERROR_SERVICE_NOT_AVAILABLE);
            }
            String stringExtra = poll.getStringExtra("registration_id");
            if (stringExtra != null) {
                return stringExtra;
            }
            poll.getStringExtra("error");
            String stringExtra2 = poll.getStringExtra("error");
            if (stringExtra2 != null) {
                throw new IOException(stringExtra2);
            }
            throw new IOException(ERROR_SERVICE_NOT_AVAILABLE);
        } catch (InterruptedException e) {
            throw new IOException(e.getMessage());
        }
    }

    public void send(String to, String msgId, long timeToLive, Bundle data) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException(ERROR_MAIN_THREAD);
        }
        if (to == null) {
            throw new IllegalArgumentException("Missing 'to'");
        }
        Intent intent = new Intent("com.google.android.gcm.intent.SEND");
        intent.putExtras(data);
        d(intent);
        intent.putExtra("google.to", to);
        intent.putExtra("google.message_id", msgId);
        intent.putExtra("google.ttl", Long.toString(timeToLive));
        this.ee.sendOrderedBroadcast(intent, null);
    }

    public void send(String to, String msgId, Bundle data) throws IOException {
        send(to, msgId, -1L, data);
    }

    public void unregister() throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException(ERROR_MAIN_THREAD);
        }
        cj();
        try {
            Intent poll = this.oq.poll(5000L, TimeUnit.MILLISECONDS);
            if (poll == null) {
                throw new IOException(ERROR_SERVICE_NOT_AVAILABLE);
            }
            if (poll.getStringExtra("unregistered") != null) {
                return;
            }
            String stringExtra = poll.getStringExtra("error");
            if (stringExtra == null) {
                throw new IOException(ERROR_SERVICE_NOT_AVAILABLE);
            }
            throw new IOException(stringExtra);
        } catch (InterruptedException e) {
            throw new IOException(e.getMessage());
        }
    }
}
