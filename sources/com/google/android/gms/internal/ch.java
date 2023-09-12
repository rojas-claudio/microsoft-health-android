package com.google.android.gms.internal;

import android.os.Process;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public final class ch {
    private static final ThreadFactory hF = new ThreadFactory() { // from class: com.google.android.gms.internal.ch.2
        private final AtomicInteger hI = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "AdWorker #" + this.hI.getAndIncrement());
        }
    };
    private static final ThreadPoolExecutor hG = new ThreadPoolExecutor(0, 10, 65, TimeUnit.SECONDS, new SynchronousQueue(true), hF);

    public static void execute(final Runnable task) {
        try {
            hG.execute(new Runnable() { // from class: com.google.android.gms.internal.ch.1
                @Override // java.lang.Runnable
                public void run() {
                    Process.setThreadPriority(10);
                    task.run();
                }
            });
        } catch (RejectedExecutionException e) {
            cn.b("Too many background threads already running. Aborting task.", e);
        }
    }
}
