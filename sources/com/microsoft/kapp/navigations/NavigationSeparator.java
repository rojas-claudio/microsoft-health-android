package com.microsoft.kapp.navigations;

import android.app.Activity;
/* loaded from: classes.dex */
public class NavigationSeparator extends NavigationCommandV1 {
    public NavigationSeparator() {
        super("", 0);
    }

    @Override // com.microsoft.kapp.navigations.NavigationCommandV1
    public String getTitle() {
        return "";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.microsoft.kapp.navigations.NavigationCommandV1
    public void navigate(Activity parentActivity) {
    }
}
