package com.microsoft.kapp.tasks;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.krestsdk.models.GoalType;
import java.util.Map;
/* loaded from: classes.dex */
public abstract class GoalsPushDeviceTask extends ScopedAsyncTask<Void, Void, Boolean> {
    private Map<GoalType, Long> mAllGoalsMap;
    private CargoConnection mCargoConnection;

    /* JADX INFO: Access modifiers changed from: protected */
    public GoalsPushDeviceTask(CargoConnection cargoConnection, OnTaskListener onTaskListener) {
        super(onTaskListener);
        Validate.notNull(cargoConnection, "cargoConnection");
        this.mCargoConnection = cargoConnection;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public final Boolean doInBackground(Void... params) {
        if (this.mAllGoalsMap == null || this.mAllGoalsMap.size() == 0) {
            setException(new Exception("The Goals type/value list is null or empty! pushing Goals to device aborted!"));
            return false;
        }
        try {
            this.mCargoConnection.setGoals(this.mAllGoalsMap);
        } catch (Exception ex) {
            setException(ex);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public final void onPostExecute(Boolean result) {
        Exception executeException = getException();
        if (executeException != null) {
            onExecuteFailed(executeException);
        } else {
            onExecuteSucceeded(result);
        }
    }

    protected CargoConnection getCargoConnection() {
        return this.mCargoConnection;
    }

    protected void onExecuteSucceeded(Boolean result) {
    }

    protected void onExecuteFailed(Exception exception) {
    }

    public GoalsPushDeviceTask setAllGoalsValue(Map<GoalType, Long> goalsMap) {
        this.mAllGoalsMap = goalsMap;
        return this;
    }
}
