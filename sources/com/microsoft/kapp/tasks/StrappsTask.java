package com.microsoft.kapp.tasks;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public abstract class StrappsTask<TResult> extends ScopedAsyncTask<Void, Void, TResult> {
    private CargoConnection mCargoConnection;

    /* JADX INFO: Access modifiers changed from: protected */
    public StrappsTask(CargoConnection cargoConnection, OnTaskListener onTaskListener) {
        super(onTaskListener);
        Validate.notNull(cargoConnection, "cargoConnection");
        this.mCargoConnection = cargoConnection;
    }

    @Override // com.microsoft.kapp.ScopedAsyncTask
    protected final void onPostExecute(TResult result) {
        onPostExecuteCalldown(result);
        if (getException() == null) {
            onExecuteSucceeded(result);
        } else {
            onExecuteFailed(getException());
        }
    }

    protected void onPostExecuteCalldown(TResult result) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CargoConnection getCargoConnection() {
        return this.mCargoConnection;
    }

    protected void onExecuteSucceeded(TResult result) {
    }

    protected void onExecuteFailed(Exception exception) {
    }
}
