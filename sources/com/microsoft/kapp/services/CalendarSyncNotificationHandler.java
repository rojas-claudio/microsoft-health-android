package com.microsoft.kapp.services;

import android.content.Context;
import android.os.Bundle;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.calendar.CalendarEvent;
import com.microsoft.kapp.calendar.CalendarEventsDataProvider;
import com.microsoft.kapp.models.FreStatus;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class CalendarSyncNotificationHandler extends InjectableNotificationHandler {
    @Inject
    CalendarEventsDataProvider mCalendarEventsDataProvider;
    @Inject
    CargoConnection mCargoConnection;
    @Inject
    SettingsProvider mSettingsProvider;

    public CalendarSyncNotificationHandler(Context context) {
        super(context);
    }

    @Override // com.microsoft.kapp.services.NotificationHandler
    public void handleNotification(Bundle bundle) {
        if (this.mCalendarEventsDataProvider != null && this.mCargoConnection != null && this.mSettingsProvider != null && this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN) {
            List<CalendarEvent> calendarEvents = this.mCalendarEventsDataProvider.getCalendarEvents();
            this.mCargoConnection.sendCalendarEvents(calendarEvents);
        }
    }
}
