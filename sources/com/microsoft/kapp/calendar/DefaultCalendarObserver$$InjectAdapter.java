package com.microsoft.kapp.calendar;

import com.microsoft.kapp.database.LoggingContentResolver;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class DefaultCalendarObserver$$InjectAdapter extends Binding<DefaultCalendarObserver> implements MembersInjector<DefaultCalendarObserver> {
    private Binding<CalendarEventsDataProvider> mCalendarEventsDataProvider;
    private Binding<LoggingContentResolver> mContentResolver;

    public DefaultCalendarObserver$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.calendar.DefaultCalendarObserver", false, DefaultCalendarObserver.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mContentResolver = linker.requestBinding("com.microsoft.kapp.database.LoggingContentResolver", DefaultCalendarObserver.class, getClass().getClassLoader());
        this.mCalendarEventsDataProvider = linker.requestBinding("com.microsoft.kapp.calendar.CalendarEventsDataProvider", DefaultCalendarObserver.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mContentResolver);
        injectMembersBindings.add(this.mCalendarEventsDataProvider);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(DefaultCalendarObserver object) {
        object.mContentResolver = this.mContentResolver.get();
        object.mCalendarEventsDataProvider = this.mCalendarEventsDataProvider.get();
    }
}
