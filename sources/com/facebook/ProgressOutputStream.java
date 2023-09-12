package com.facebook;

import android.os.Handler;
import com.facebook.RequestBatch;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ProgressOutputStream extends FilterOutputStream implements RequestOutputStream {
    private long batchProgress;
    private RequestProgress currentRequestProgress;
    private long lastReportedProgress;
    private long maxProgress;
    private final Map<Request, RequestProgress> progressMap;
    private final RequestBatch requests;
    private final long threshold;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProgressOutputStream(OutputStream out, RequestBatch requests, Map<Request, RequestProgress> progressMap, long maxProgress) {
        super(out);
        this.requests = requests;
        this.progressMap = progressMap;
        this.maxProgress = maxProgress;
        this.threshold = Settings.getOnProgressThreshold();
    }

    private void addProgress(long size) {
        if (this.currentRequestProgress != null) {
            this.currentRequestProgress.addProgress(size);
        }
        this.batchProgress += size;
        if (this.batchProgress >= this.lastReportedProgress + this.threshold || this.batchProgress >= this.maxProgress) {
            reportBatchProgress();
        }
    }

    private void reportBatchProgress() {
        if (this.batchProgress > this.lastReportedProgress) {
            for (RequestBatch.Callback callback : this.requests.getCallbacks()) {
                if (callback instanceof RequestBatch.OnProgressCallback) {
                    Handler callbackHandler = this.requests.getCallbackHandler();
                    final RequestBatch.OnProgressCallback progressCallback = (RequestBatch.OnProgressCallback) callback;
                    if (callbackHandler == null) {
                        progressCallback.onBatchProgress(this.requests, this.batchProgress, this.maxProgress);
                    } else {
                        callbackHandler.post(new Runnable() { // from class: com.facebook.ProgressOutputStream.1
                            @Override // java.lang.Runnable
                            public void run() {
                                progressCallback.onBatchProgress(ProgressOutputStream.this.requests, ProgressOutputStream.this.batchProgress, ProgressOutputStream.this.maxProgress);
                            }
                        });
                    }
                }
            }
            this.lastReportedProgress = this.batchProgress;
        }
    }

    @Override // com.facebook.RequestOutputStream
    public void setCurrentRequest(Request request) {
        this.currentRequestProgress = request != null ? this.progressMap.get(request) : null;
    }

    long getBatchProgress() {
        return this.batchProgress;
    }

    long getMaxProgress() {
        return this.maxProgress;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] buffer) throws IOException {
        this.out.write(buffer);
        addProgress(buffer.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] buffer, int offset, int length) throws IOException {
        this.out.write(buffer, offset, length);
        addProgress(length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int oneByte) throws IOException {
        this.out.write(oneByte);
        addProgress(1L);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        for (RequestProgress p : this.progressMap.values()) {
            p.reportProgress();
        }
        reportBatchProgress();
    }
}
