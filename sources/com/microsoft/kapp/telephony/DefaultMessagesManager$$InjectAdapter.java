package com.microsoft.kapp.telephony;

import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class DefaultMessagesManager$$InjectAdapter extends Binding<DefaultMessagesManager> implements MembersInjector<DefaultMessagesManager> {
    private Binding<MessagesObserver> mMessagesObserver;

    public DefaultMessagesManager$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.telephony.DefaultMessagesManager", false, DefaultMessagesManager.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mMessagesObserver = linker.requestBinding("com.microsoft.kapp.telephony.MessagesObserver", DefaultMessagesManager.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mMessagesObserver);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(DefaultMessagesManager object) {
        object.mMessagesObserver = this.mMessagesObserver.get();
    }
}
