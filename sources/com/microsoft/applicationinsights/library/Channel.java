package com.microsoft.applicationinsights.library;

import com.microsoft.applicationinsights.contracts.Envelope;
import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.library.config.IQueueConfig;
import com.microsoft.applicationinsights.logging.InternalLogging;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class Channel {
    private static final String TAG = "Channel";
    private static Channel instance;
    private static ChannelQueue queue;
    private static volatile boolean isChannelLoaded = false;
    private static final Object LOCK = new Object();

    protected Channel() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void initialize(IQueueConfig config) {
        if (!isChannelLoaded) {
            synchronized (LOCK) {
                if (!isChannelLoaded) {
                    isChannelLoaded = true;
                    instance = new Channel();
                    instance.setQueue(new ChannelQueue(config));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Channel getInstance() {
        if (instance == null) {
            InternalLogging.error(TAG, "getInstance was called before initialization");
        }
        return instance;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void synchronize() {
        getQueue().flush();
    }

    protected ChannelQueue getQueue() {
        return queue;
    }

    protected void setQueue(ChannelQueue queue2) {
        queue = queue2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void enqueue(Envelope envelope) {
        queue.isCrashing = false;
        queue.enqueue(envelope);
        InternalLogging.info(TAG, "enqueued telemetry", envelope.getName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void processUnhandledException(Envelope envelope) {
        queue.isCrashing = true;
        queue.flush();
        IJsonSerializable[] data = {envelope};
        Persistence persistence = Persistence.getInstance();
        if (persistence != null) {
            persistence.persist(data, (Boolean) true);
        } else {
            InternalLogging.info(TAG, "error persisting crash", envelope.toString());
        }
    }
}
