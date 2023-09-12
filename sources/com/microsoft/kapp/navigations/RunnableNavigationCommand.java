package com.microsoft.kapp.navigations;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class RunnableNavigationCommand<T extends Fragment> extends NavigationCommandV1 {
    private Runnable mRunnable;

    public RunnableNavigationCommand(Context context, int titleResourceId, int iconResourceId, Runnable runnable) {
        this(context.getString(titleResourceId), iconResourceId, runnable);
    }

    public RunnableNavigationCommand(String title, int iconResourceId, Runnable runnable) {
        super(title, iconResourceId);
        Validate.notNull(runnable, "runnable");
        this.mRunnable = runnable;
    }

    @Override // com.microsoft.kapp.navigations.NavigationCommandV1
    public void navigate(Activity parentActivity) {
        this.mRunnable.run();
    }
}
