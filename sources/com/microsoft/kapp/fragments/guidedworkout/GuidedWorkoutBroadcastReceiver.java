package com.microsoft.kapp.fragments.guidedworkout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class GuidedWorkoutBroadcastReceiver extends BroadcastReceiver {
    private WeakReference<GuidedWorkoutBroadcastListener> mListenerWeakRef;

    public GuidedWorkoutBroadcastReceiver(GuidedWorkoutBroadcastListener listener) {
        this.mListenerWeakRef = new WeakReference<>(listener);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        GuidedWorkoutBroadcastListener listener;
        if (this.mListenerWeakRef != null && (listener = this.mListenerWeakRef.get()) != null) {
            listener.onGWBroadcastReceived(context, intent);
        }
    }
}
