package com.microsoft.kapp.utils;

import com.microsoft.kapp.models.LoadStatus;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class MultipleRequestManager {
    private boolean mIsNoData;
    private Exception mLastException;
    private int mNumOfFailedRequests;
    private int mNumOfRequestsIssued;
    private int mNumOfSuccessfulRequests;
    private final OnRequestCompleteListener mRequestCompleteListener;
    private final Object mLock = new Object();
    private AtomicBoolean mResponseSentAlready = new AtomicBoolean(false);

    /* loaded from: classes.dex */
    public interface OnRequestCompleteListener {
        void requestComplete(LoadStatus loadStatus);
    }

    public MultipleRequestManager(int numOfRequestsIssued, OnRequestCompleteListener requestCompleteListener) {
        this.mRequestCompleteListener = requestCompleteListener;
        this.mNumOfRequestsIssued = numOfRequestsIssued;
    }

    public void notifyRequestSucceeded() {
        notifyRequestSucceeded(false);
    }

    public void notifyRequestSucceededNoData() {
        notifyRequestSucceeded(true);
    }

    private void notifyRequestSucceeded(boolean isNoData) {
        synchronized (this.mLock) {
            this.mIsNoData |= isNoData;
            this.mNumOfSuccessfulRequests++;
            onRequestCompleted();
        }
    }

    public void notifyRequestFailed() {
        synchronized (this.mLock) {
            this.mNumOfFailedRequests++;
            onRequestCompleted();
        }
    }

    public void notifyRequestFailed(Exception exception) {
        synchronized (this.mLock) {
            this.mNumOfFailedRequests++;
            this.mLastException = exception;
            onRequestCompleted();
        }
    }

    public Exception getException() {
        return this.mLastException;
    }

    private void onRequestCompleted() {
        if (this.mNumOfSuccessfulRequests + this.mNumOfFailedRequests == this.mNumOfRequestsIssued) {
            if (this.mNumOfFailedRequests != 0) {
                fetchingFinished(LoadStatus.ERROR);
            } else if (this.mIsNoData) {
                fetchingFinished(LoadStatus.NO_DATA);
            } else {
                fetchingFinished(LoadStatus.LOADED);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fetchingFinished(LoadStatus loadStatus) {
        if (this.mResponseSentAlready.compareAndSet(false, true) && this.mRequestCompleteListener != null && loadStatus != null) {
            this.mRequestCompleteListener.requestComplete(loadStatus);
        }
    }
}
