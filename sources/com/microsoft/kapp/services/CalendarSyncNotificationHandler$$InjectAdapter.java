package com.microsoft.kapp.services;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.calendar.CalendarEventsDataProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class CalendarSyncNotificationHandler$$InjectAdapter extends Binding<CalendarSyncNotificationHandler> implements MembersInjector<CalendarSyncNotificationHandler> {
    private Binding<CalendarEventsDataProvider> mCalendarEventsDataProvider;
    private Binding<CargoConnection> mCargoConnection;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<InjectableNotificationHandler> supertype;

    public CalendarSyncNotificationHandler$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.services.CalendarSyncNotificationHandler", false, CalendarSyncNotificationHandler.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCalendarEventsDataProvider = linker.requestBinding("com.microsoft.kapp.calendar.CalendarEventsDataProvider", CalendarSyncNotificationHandler.class, getClass().getClassLoader());
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", CalendarSyncNotificationHandler.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", CalendarSyncNotificationHandler.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.services.InjectableNotificationHandler", CalendarSyncNotificationHandler.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCalendarEventsDataProvider);
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CalendarSyncNotificationHandler object) {
        object.mCalendarEventsDataProvider = this.mCalendarEventsDataProvider.get();
        object.mCargoConnection = this.mCargoConnection.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}
