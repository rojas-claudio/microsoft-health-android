package com.microsoft.kapp.calendar;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import com.microsoft.kapp.activities.BingMapActivity;
import com.microsoft.kapp.calendar.CalendarEvent;
import com.microsoft.kapp.database.LoggingContentResolver;
import com.microsoft.kapp.database.Projection;
import com.microsoft.kapp.diagnostics.Validate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class DefaultCalendarEventsDataProvider implements CalendarEventsDataProvider {
    private static final String InstancesWhereStatementFormat = "visible <> 0 AND ((begin >= %1$d AND allDay = 0 )OR(begin >= %2$d AND allDay <> 0 ))";
    private static final int MILLIS_TO_HOURS = 3600000;
    private static final String RemindersOrderStatement = "minutes ASC  LIMIT 1";
    private LoggingContentResolver mContentResolver;
    private static final Projection InstancesProjection = new Projection();
    private static final int EventIdColumnIndex = InstancesProjection.addColumn(BingMapActivity.ARG_IN_EVENT);
    private static final int EventTitleColumnIndex = InstancesProjection.addColumn("title");
    private static final int EventLocationColumnIndex = InstancesProjection.addColumn("eventLocation");
    private static final int InstanceBeginColumnIndex = InstancesProjection.addColumn("begin");
    private static final int InstanceStatusColumnIndex = InstancesProjection.addColumn("eventStatus");
    private static final int InstanceEndColumnIndex = InstancesProjection.addColumn("end");
    private static final int IsAllDayColumnIndex = InstancesProjection.addColumn("allDay");
    private static final int InstanceAttendeeStatus = InstancesProjection.addColumn("selfAttendeeStatus");
    private static final int AvailabilityColumnIndex = InstancesProjection.addColumn("availability");
    private static final int CalendarColorColumnIndex = InstancesProjection.addColumn("calendar_color");
    private static final int HasAlarmColumnIndex = InstancesProjection.addColumn("hasAlarm");
    private static final Projection ReminderProjection = new Projection();
    private static final int ReminderMinutesColumnIndex = ReminderProjection.addColumn("minutes");

    public DefaultCalendarEventsDataProvider(LoggingContentResolver loggingContentResolver) {
        Validate.notNull(loggingContentResolver, "loggingContentResolver");
        this.mContentResolver = loggingContentResolver;
    }

    @Override // com.microsoft.kapp.calendar.CalendarEventsDataProvider
    public List<CalendarEvent> getCalendarEvents() {
        ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();
        Cursor instancesCursor = null;
        DateTime now = DateTime.now();
        int timezoneOffset = Calendar.getInstance().getTimeZone().getOffset(now.getMillis()) / 3600000;
        DateTime start = now.minusHours(24 - timezoneOffset);
        DateTime end = now.plusDays(14);
        long startTimeMilliSec = start.getMillis();
        long endTimeMilliSec = end.getMillis();
        try {
            Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, startTimeMilliSec);
            ContentUris.appendId(builder, endTimeMilliSec);
            String whereStatement = String.format(InstancesWhereStatementFormat, Long.valueOf(now.getMillis()), Long.valueOf(start.getMillis()));
            instancesCursor = this.mContentResolver.query(builder.build(), InstancesProjection, whereStatement, null, null);
            if (instancesCursor != null) {
                while (instancesCursor.moveToNext()) {
                    int attendeeStatus = instancesCursor.getInt(InstanceAttendeeStatus);
                    if (attendeeStatus != 2) {
                        CalendarEvent.Builder calendarEventBuilder = new CalendarEvent.Builder();
                        int eventId = instancesCursor.getInt(EventIdColumnIndex);
                        calendarEventBuilder.setTitle(instancesCursor.getString(EventTitleColumnIndex));
                        calendarEventBuilder.setLocation(instancesCursor.getString(EventLocationColumnIndex));
                        calendarEventBuilder.setColor(instancesCursor.getInt(CalendarColorColumnIndex));
                        boolean isAllDay = instancesCursor.getInt(IsAllDayColumnIndex) > 0;
                        calendarEventBuilder.setIsAllDay(isAllDay);
                        DateTime startTime = new DateTime(instancesCursor.getLong(InstanceBeginColumnIndex));
                        DateTime endTime = new DateTime(instancesCursor.getLong(InstanceEndColumnIndex));
                        if (isAllDay) {
                            int timeZoneOffset = Calendar.getInstance().getTimeZone().getOffset(instancesCursor.getLong(InstanceBeginColumnIndex));
                            startTime = startTime.minusMillis(timeZoneOffset);
                            endTime = endTime.minusMillis(timeZoneOffset);
                        }
                        calendarEventBuilder.setStartDate(startTime.toDate());
                        calendarEventBuilder.setEndDate(endTime.toDate());
                        int eventStatus = instancesCursor.getInt(InstanceStatusColumnIndex);
                        calendarEventBuilder.setIsCanceled(eventStatus == 2);
                        CalendarEvent.Availability availability = CalendarEvent.Availability.valueOf(instancesCursor.getInt(AvailabilityColumnIndex));
                        if (attendeeStatus == 1) {
                            availability = CalendarEvent.Availability.BUSY;
                        }
                        calendarEventBuilder.setAvailability(availability);
                        boolean hasAlarm = instancesCursor.getInt(HasAlarmColumnIndex) > 0;
                        calendarEventBuilder.setHasReminder(hasAlarm);
                        Cursor remindersCursor = this.mContentResolver.query(CalendarContract.Reminders.CONTENT_URI, ReminderProjection, "event_id = " + eventId, null, RemindersOrderStatement);
                        if (remindersCursor != null && remindersCursor.moveToFirst()) {
                            calendarEventBuilder.setReminderMinutes(remindersCursor.getInt(ReminderMinutesColumnIndex));
                        }
                        if (remindersCursor != null) {
                            remindersCursor.close();
                        }
                        calendarEvents.add(calendarEventBuilder.Build());
                    }
                }
            }
            Collections.sort(calendarEvents, new Comparator<CalendarEvent>() { // from class: com.microsoft.kapp.calendar.DefaultCalendarEventsDataProvider.1
                @Override // java.util.Comparator
                public int compare(CalendarEvent lhs, CalendarEvent rhs) {
                    return lhs.getStartDate().compareTo(rhs.getStartDate());
                }
            });
            return calendarEvents.subList(0, Math.min(calendarEvents.size(), 8));
        } finally {
            if (instancesCursor != null) {
                instancesCursor.close();
            }
        }
    }
}
