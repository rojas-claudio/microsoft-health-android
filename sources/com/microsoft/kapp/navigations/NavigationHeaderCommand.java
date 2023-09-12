package com.microsoft.kapp.navigations;

import android.app.Activity;
import android.content.Context;
/* loaded from: classes.dex */
public class NavigationHeaderCommand extends NavigationCommandV1 {
    public NavigationHeaderCommand(Context context, int titleResourceId) {
        super(context.getString(titleResourceId), -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.kapp.navigations.NavigationCommandV1
    public void navigate(Activity parentActivity) {
    }
}
