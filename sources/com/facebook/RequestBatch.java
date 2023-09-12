package com.facebook;

import android.os.Handler;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class RequestBatch extends AbstractList<Request> {
    private static AtomicInteger idGenerator = new AtomicInteger();
    private String batchApplicationId;
    private Handler callbackHandler;
    private List<Callback> callbacks;
    private final String id;
    private List<Request> requests;
    private int timeoutInMilliseconds;

    /* loaded from: classes.dex */
    public interface Callback {
        void onBatchCompleted(RequestBatch requestBatch);
    }

    /* loaded from: classes.dex */
    public interface OnProgressCallback extends Callback {
        void onBatchProgress(RequestBatch requestBatch, long j, long j2);
    }

    public RequestBatch() {
        this.requests = new ArrayList();
        this.timeoutInMilliseconds = 0;
        this.id = Integer.valueOf(idGenerator.incrementAndGet()).toString();
        this.callbacks = new ArrayList();
        this.requests = new ArrayList();
    }

    public RequestBatch(Collection<Request> requests) {
        this.requests = new ArrayList();
        this.timeoutInMilliseconds = 0;
        this.id = Integer.valueOf(idGenerator.incrementAndGet()).toString();
        this.callbacks = new ArrayList();
        this.requests = new ArrayList(requests);
    }

    public RequestBatch(Request... requests) {
        this.requests = new ArrayList();
        this.timeoutInMilliseconds = 0;
        this.id = Integer.valueOf(idGenerator.incrementAndGet()).toString();
        this.callbacks = new ArrayList();
        this.requests = Arrays.asList(requests);
    }

    public RequestBatch(RequestBatch requests) {
        this.requests = new ArrayList();
        this.timeoutInMilliseconds = 0;
        this.id = Integer.valueOf(idGenerator.incrementAndGet()).toString();
        this.callbacks = new ArrayList();
        this.requests = new ArrayList(requests);
        this.callbackHandler = requests.callbackHandler;
        this.timeoutInMilliseconds = requests.timeoutInMilliseconds;
        this.callbacks = new ArrayList(requests.callbacks);
    }

    public int getTimeout() {
        return this.timeoutInMilliseconds;
    }

    public void setTimeout(int timeoutInMilliseconds) {
        if (timeoutInMilliseconds < 0) {
            throw new IllegalArgumentException("Argument timeoutInMilliseconds must be >= 0.");
        }
        this.timeoutInMilliseconds = timeoutInMilliseconds;
    }

    public void addCallback(Callback callback) {
        if (!this.callbacks.contains(callback)) {
            this.callbacks.add(callback);
        }
    }

    public void removeCallback(Callback callback) {
        this.callbacks.remove(callback);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean add(Request request) {
        return this.requests.add(request);
    }

    @Override // java.util.AbstractList, java.util.List
    public final void add(int location, Request request) {
        this.requests.add(location, request);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final void clear() {
        this.requests.clear();
    }

    @Override // java.util.AbstractList, java.util.List
    public final Request get(int i) {
        return this.requests.get(i);
    }

    @Override // java.util.AbstractList, java.util.List
    public final Request remove(int location) {
        return this.requests.remove(location);
    }

    @Override // java.util.AbstractList, java.util.List
    public final Request set(int location, Request request) {
        return this.requests.set(location, request);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.requests.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String getId() {
        return this.id;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Handler getCallbackHandler() {
        return this.callbackHandler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void setCallbackHandler(Handler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<Request> getRequests() {
        return this.requests;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<Callback> getCallbacks() {
        return this.callbacks;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String getBatchApplicationId() {
        return this.batchApplicationId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void setBatchApplicationId(String batchApplicationId) {
        this.batchApplicationId = batchApplicationId;
    }

    public final List<Response> executeAndWait() {
        return executeAndWaitImpl();
    }

    public final RequestAsyncTask executeAsync() {
        return executeAsyncImpl();
    }

    List<Response> executeAndWaitImpl() {
        return Request.executeBatchAndWait(this);
    }

    RequestAsyncTask executeAsyncImpl() {
        return Request.executeBatchAsync(this);
    }
}
