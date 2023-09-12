package com.microsoft.kapp;

import android.os.AsyncTask;
import com.microsoft.kapp.utils.SerialAsyncOperation;
/* loaded from: classes.dex */
public abstract class AsyncTaskOperation extends AsyncTask<Void, Void, Void> {
    private SerialAsyncOperation mNextOperation;
    protected int mOperationStatus = 0;
    protected int mOperationResultCodeId = -1;

    protected abstract void onOperationError();

    protected abstract void onOperationStarted();

    protected abstract void onOperationSuccess();

    public AsyncTaskOperation(SerialAsyncOperation nextOperation) {
        this.mNextOperation = nextOperation;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
        onOperationStarted();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Void result) {
        if (this.mOperationStatus == 1) {
            onOperationSuccess();
        } else {
            onOperationError();
        }
        if (this.mNextOperation != null) {
            this.mNextOperation.execute();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setOperationResultStatus(int status, int operationResultCodeId) {
        this.mOperationStatus = status;
        this.mOperationResultCodeId = operationResultCodeId;
    }

    protected int getOperationResultStatus() {
        return this.mOperationStatus;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getOperationResultCodeId() {
        return this.mOperationResultCodeId;
    }
}
