package com.microsoft.kapp.tasks;

import android.os.AsyncTask;
import com.microsoft.band.device.command.RunDisplayMetricType;
import com.microsoft.kapp.CargoConnection;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public class RunMetricsSaveTask extends AsyncTask<Void, Void, Boolean> {
    RunMetricsSaveCallback mCallback;
    CargoConnection mCargoConnection;
    RunDisplayMetricType[] mMetrics;

    /* loaded from: classes.dex */
    public interface RunMetricsSaveCallback {
        void onRunMetricsSaveResult(boolean z, RunDisplayMetricType... runDisplayMetricTypeArr);
    }

    public RunMetricsSaveTask(RunMetricsSaveCallback callback, CargoConnection cargoConnection, RunDisplayMetricType... metrics) {
        Validate.notNull(callback, "callback", new Object[0]);
        Validate.notNull(cargoConnection, "cargoConnection", new Object[0]);
        Validate.notNull(metrics, "metrics", new Object[0]);
        this.mCallback = callback;
        this.mCargoConnection = cargoConnection;
        this.mMetrics = metrics;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Boolean doInBackground(Void... params) {
        return Boolean.valueOf(this.mCargoConnection.setRunMetricsOrder(this.mMetrics));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Boolean result) {
        this.mCallback.onRunMetricsSaveResult(result.booleanValue(), this.mMetrics);
    }
}
