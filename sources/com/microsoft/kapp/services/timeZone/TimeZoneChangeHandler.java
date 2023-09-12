package com.microsoft.kapp.services.timeZone;

import android.content.Context;
import android.os.Handler;
import com.microsoft.kapp.diagnostics.Validate;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public class TimeZoneChangeHandler {
    private final Context mContext;
    private final CopyOnWriteArrayList<WeakReference<TimeZoneChangeListner>> mTimeZoneChangeListnersWefRefs = new CopyOnWriteArrayList<>();

    public TimeZoneChangeHandler(Context context) {
        this.mContext = context;
    }

    public void removeTimeZoneChangeListner(TimeZoneChangeListner listener) {
        Validate.notNull(listener, "listener");
        for (int i = this.mTimeZoneChangeListnersWefRefs.size() - 1; i >= 0; i--) {
            TimeZoneChangeListner current = this.mTimeZoneChangeListnersWefRefs.get(i).get();
            if (current == null || current == listener) {
                this.mTimeZoneChangeListnersWefRefs.remove(i);
            }
        }
    }

    public void addTimeZoneChangeListenerWeakRef(TimeZoneChangeListner listener) {
        Validate.notNull(listener, "listener");
        this.mTimeZoneChangeListnersWefRefs.add(new WeakReference<>(listener));
    }

    public void notifyTimeZoneChange() {
        Handler uiHandler = new Handler(this.mContext.getMainLooper());
        uiHandler.post(new Runnable() { // from class: com.microsoft.kapp.services.timeZone.TimeZoneChangeHandler.1
            @Override // java.lang.Runnable
            public void run() {
                Iterator i$ = TimeZoneChangeHandler.this.mTimeZoneChangeListnersWefRefs.iterator();
                while (i$.hasNext()) {
                    WeakReference<TimeZoneChangeListner> listenerWeakRef = (WeakReference) i$.next();
                    TimeZoneChangeListner listener = listenerWeakRef.get();
                    if (listener != null) {
                        listener.onTimeZoneChanged();
                    }
                }
            }
        });
    }
}
