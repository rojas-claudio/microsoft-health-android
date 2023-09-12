package com.microsoft.applicationinsights.library;

import android.os.AsyncTask;
import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.library.config.IQueueConfig;
import com.microsoft.applicationinsights.logging.InternalLogging;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ChannelQueue {
    private static final String TAG = "TelemetryQueue";
    protected final IQueueConfig config;
    private TimerTask scheduledPersistenceTask;
    private final Object LOCK = new Object();
    protected final List<IJsonSerializable> list = new LinkedList();
    protected final Timer timer = new Timer("Application Insights Sender Queue", true);
    protected volatile boolean isCrashing = false;

    /* JADX INFO: Access modifiers changed from: protected */
    public ChannelQueue(IQueueConfig config) {
        this.config = config;
    }

    protected void setIsCrashing(Boolean isCrashing) {
        this.isCrashing = isCrashing.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean enqueue(IJsonSerializable item) {
        boolean success;
        if (item == null) {
            return false;
        }
        synchronized (this.LOCK) {
            success = this.list.add(item);
            if (success) {
                if (this.list.size() >= this.config.getMaxBatchCount() || this.isCrashing) {
                    flush();
                } else if (this.list.size() == 1) {
                    this.scheduledPersistenceTask = new TriggerPersistTask();
                    this.timer.schedule(this.scheduledPersistenceTask, this.config.getMaxBatchIntervalMs());
                }
            } else {
                InternalLogging.warn(TAG, "Unable to add item to queue");
            }
        }
        return success;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void flush() {
        if (this.scheduledPersistenceTask != null) {
            this.scheduledPersistenceTask.cancel();
        }
        synchronized (this.LOCK) {
            if (!this.list.isEmpty()) {
                IJsonSerializable[] data = new IJsonSerializable[this.list.size()];
                this.list.toArray(data);
                this.list.clear();
                PersistenceTask persistTask = new PersistenceTask(data);
                persistTask.execute(new Void[0]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class TriggerPersistTask extends TimerTask {
        public TriggerPersistTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            ChannelQueue.this.flush();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class PersistenceTask extends AsyncTask<Void, Void, Void> {
        private IJsonSerializable[] data;

        public PersistenceTask(IJsonSerializable[] data) {
            this.data = data;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            Persistence persistence;
            if (this.data != null && (persistence = Persistence.getInstance()) != null) {
                persistence.persist(this.data, (Boolean) false);
                return null;
            }
            return null;
        }
    }

    protected IQueueConfig getQueueConfig() {
        return this.config;
    }
}
