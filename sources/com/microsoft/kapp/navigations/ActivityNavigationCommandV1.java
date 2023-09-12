package com.microsoft.kapp.navigations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class ActivityNavigationCommandV1<T extends Activity> extends NavigationCommandV1 {
    private final Class<T> mActivityClass;

    public ActivityNavigationCommandV1(Context context, int titleResourceId, int iconResourceId, Class<T> activityClass) {
        this(context.getString(titleResourceId), iconResourceId, activityClass);
    }

    public ActivityNavigationCommandV1(String title, int iconResourceId, Class<T> activityClass) {
        super(title, iconResourceId);
        Validate.notNull(activityClass, "activityClass");
        this.mActivityClass = activityClass;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.kapp.navigations.NavigationCommandV1
    public void navigate(Activity parentActivity) {
        Validate.notNull(parentActivity, "parentActivity");
        Intent intent = new Intent((Context) parentActivity, (Class<?>) this.mActivityClass);
        parentActivity.startActivity(intent);
    }
}
