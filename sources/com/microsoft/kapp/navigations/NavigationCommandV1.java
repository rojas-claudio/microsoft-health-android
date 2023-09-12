package com.microsoft.kapp.navigations;

import android.app.Activity;
/* loaded from: classes.dex */
public abstract class NavigationCommandV1 {
    private final int mIconId;
    private final boolean mIndent;
    private boolean mNotification;
    private final String mTitle;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void navigate(Activity activity);

    public NavigationCommandV1(String title, int iconResourceId) {
        this(title, iconResourceId, false);
    }

    public NavigationCommandV1(String title, int iconResourceId, boolean indent) {
        this(title, indent, false, iconResourceId);
    }

    public NavigationCommandV1(String title, boolean indent, boolean notification, int iconId) {
        this.mTitle = title;
        this.mIndent = indent;
        this.mNotification = notification;
        this.mIconId = iconId;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public boolean getIndent() {
        return this.mIndent;
    }

    public boolean getNotification() {
        return this.mNotification;
    }

    public int getIconResourceId() {
        return this.mIconId;
    }

    public void setNotification(boolean mNotification) {
        this.mNotification = mNotification;
    }
}
