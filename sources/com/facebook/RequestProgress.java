package com.facebook;

import android.os.Handler;
import com.facebook.Request;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class RequestProgress {
    private final Handler callbackHandler;
    private long lastReportedProgress;
    private long maxProgress;
    private long progress;
    private final Request request;
    private final long threshold = Settings.getOnProgressThreshold();

    /* JADX INFO: Access modifiers changed from: package-private */
    public RequestProgress(Handler callbackHandler, Request request) {
        this.request = request;
        this.callbackHandler = callbackHandler;
    }

    long getProgress() {
        return this.progress;
    }

    long getMaxProgress() {
        return this.maxProgress;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addProgress(long size) {
        this.progress += size;
        if (this.progress >= this.lastReportedProgress + this.threshold || this.progress >= this.maxProgress) {
            reportProgress();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addToMax(long size) {
        this.maxProgress += size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportProgress() {
        if (this.progress > this.lastReportedProgress) {
            Request.Callback callback = this.request.getCallback();
            if (this.maxProgress > 0 && (callback instanceof Request.OnProgressCallback)) {
                final long currentCopy = this.progress;
                final long maxProgressCopy = this.maxProgress;
                final Request.OnProgressCallback callbackCopy = (Request.OnProgressCallback) callback;
                if (this.callbackHandler == null) {
                    callbackCopy.onProgress(currentCopy, maxProgressCopy);
                } else {
                    this.callbackHandler.post(new Runnable() { // from class: com.facebook.RequestProgress.1
                        @Override // java.lang.Runnable
                        public void run() {
                            callbackCopy.onProgress(currentCopy, maxProgressCopy);
                        }
                    });
                }
                this.lastReportedProgress = this.progress;
            }
        }
    }
}
