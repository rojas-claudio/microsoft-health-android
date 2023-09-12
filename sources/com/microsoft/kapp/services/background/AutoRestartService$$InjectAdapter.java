package com.microsoft.kapp.services.background;

import com.microsoft.kapp.calendar.CalendarManager;
import com.microsoft.kapp.telephony.MessagesManager;
import com.microsoft.kapp.telephony.SmsRequestManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AutoRestartService$$InjectAdapter extends Binding<AutoRestartService> implements Provider<AutoRestartService>, MembersInjector<AutoRestartService> {
    private Binding<CalendarManager> mCalendarManager;
    private Binding<MessagesManager> mMessagesManager;
    private Binding<SmsRequestManager> mSmsRequestManager;

    public AutoRestartService$$InjectAdapter() {
        super("com.microsoft.kapp.services.background.AutoRestartService", "members/com.microsoft.kapp.services.background.AutoRestartService", false, AutoRestartService.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mMessagesManager = linker.requestBinding("com.microsoft.kapp.telephony.MessagesManager", AutoRestartService.class, getClass().getClassLoader());
        this.mCalendarManager = linker.requestBinding("com.microsoft.kapp.calendar.CalendarManager", AutoRestartService.class, getClass().getClassLoader());
        this.mSmsRequestManager = linker.requestBinding("com.microsoft.kapp.telephony.SmsRequestManager", AutoRestartService.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mMessagesManager);
        injectMembersBindings.add(this.mCalendarManager);
        injectMembersBindings.add(this.mSmsRequestManager);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public AutoRestartService get() {
        AutoRestartService result = new AutoRestartService();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(AutoRestartService object) {
        object.mMessagesManager = this.mMessagesManager.get();
        object.mCalendarManager = this.mCalendarManager.get();
        object.mSmsRequestManager = this.mSmsRequestManager.get();
    }
}
