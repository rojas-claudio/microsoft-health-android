package com.microsoft.kapp.calendar;

import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class DefaultCalendarManager$$InjectAdapter extends Binding<DefaultCalendarManager> implements MembersInjector<DefaultCalendarManager> {
    private Binding<CalendarObserver> mCalendarObserver;

    public DefaultCalendarManager$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.calendar.DefaultCalendarManager", false, DefaultCalendarManager.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCalendarObserver = linker.requestBinding("com.microsoft.kapp.calendar.CalendarObserver", DefaultCalendarManager.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCalendarObserver);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(DefaultCalendarManager object) {
        object.mCalendarObserver = this.mCalendarObserver.get();
    }
}
