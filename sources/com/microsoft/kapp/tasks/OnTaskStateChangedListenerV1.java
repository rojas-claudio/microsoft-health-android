package com.microsoft.kapp.tasks;
/* loaded from: classes.dex */
public interface OnTaskStateChangedListenerV1 {
    public static final int DELETE_TASK = 2;
    public static final int RENAME_TASK = 1;
    public static final int SHARE_TASK = 0;

    void onTaskFailed(int i, Exception exc);
}
