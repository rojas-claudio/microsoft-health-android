package com.microsoft.kapp.calendar;

import android.content.Context;
import android.content.Intent;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.NotificationIntentService;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.LogScenarioTags;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class DefaultCalendarManager implements CalendarManager, CalendarListener {
    @Inject
    CalendarObserver mCalendarObserver;
    Context mContext;

    public DefaultCalendarManager(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mContext = context;
        ((KApplication) context.getApplicationContext()).inject(this);
        this.mCalendarObserver.addListener(this);
        KLog.v(LogScenarioTags.Calendar, "Setup 2: Registered Calendar Listener");
    }

    @Override // com.microsoft.kapp.calendar.CalendarListener
    public void calendarChanged() {
        KLog.v(LogScenarioTags.Calendar, "Step 1: CalendarChanged event receivied.");
        Intent intent = new Intent(this.mContext, NotificationIntentService.class);
        intent.setAction(Constants.NOTIFICATION_CALENDAR_SYNC);
        this.mContext.startService(intent);
        KLog.v(LogScenarioTags.Calendar, "Step 2: Calendar Sync service notified.");
    }
}
