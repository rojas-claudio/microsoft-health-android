package com.microsoft.kapp.fragments.golf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class GolfCourseBroadcastSyncReceiver extends BroadcastReceiver {
    private WeakReference<GolfCourseSyncBroadcastListener> mListenerWeakRef;

    public GolfCourseBroadcastSyncReceiver(GolfCourseSyncBroadcastListener listener) {
        this.mListenerWeakRef = new WeakReference<>(listener);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        GolfCourseSyncBroadcastListener listener;
        if (this.mListenerWeakRef != null && (listener = this.mListenerWeakRef.get()) != null) {
            listener.onGolfCourseSyncBroadcastReceived(context, intent);
        }
    }
}
