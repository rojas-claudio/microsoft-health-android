package com.microsoft.kapp.calendar;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.CalendarContract;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.database.LoggingContentResolver;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.LogScenarioTags;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class DefaultCalendarObserver extends ContentObserver implements CalendarObserver {
    private static final String TAG = DefaultCalendarObserver.class.getSimpleName();
    @Inject
    CalendarEventsDataProvider mCalendarEventsDataProvider;
    @Inject
    LoggingContentResolver mContentResolver;
    Context mContext;
    private final CopyOnWriteArrayList<CalendarListener> mListeners;

    public DefaultCalendarObserver(Context context) {
        super(null);
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mContext = context;
        ((KApplication) context.getApplicationContext()).inject(this);
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.registerContentObserver(CalendarContract.Instances.CONTENT_URI, true, this);
        contentResolver.registerContentObserver(CalendarContract.Reminders.CONTENT_URI, true, this);
        this.mListeners = new CopyOnWriteArrayList<>();
        KLog.v(LogScenarioTags.Calendar, "Setup 1: Observer created.");
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean selfChange, Uri uri) {
        Iterator i$ = this.mListeners.iterator();
        while (i$.hasNext()) {
            CalendarListener listener = i$.next();
            try {
                listener.calendarChanged();
            } catch (Exception ex) {
                KLog.e(TAG, "Exception in onChange", ex);
            }
        }
    }

    @Override // com.microsoft.kapp.calendar.CalendarObserver
    public void addListener(CalendarListener listener) {
        Validate.notNull(listener, "listener");
        this.mListeners.add(listener);
    }

    @Override // com.microsoft.kapp.calendar.CalendarObserver
    public void removeListener(CalendarListener listener) {
        Validate.notNull(listener, "listener");
        this.mListeners.remove(listener);
    }
}
