package com.microsoft.kapp.telephony;

import com.microsoft.kapp.services.CallDismissManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class PhoneStateListener$$InjectAdapter extends Binding<PhoneStateListener> implements MembersInjector<PhoneStateListener> {
    private Binding<CallDismissManager> mCallDismissManager;

    public PhoneStateListener$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.telephony.PhoneStateListener", false, PhoneStateListener.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCallDismissManager = linker.requestBinding("com.microsoft.kapp.services.CallDismissManager", PhoneStateListener.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCallDismissManager);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(PhoneStateListener object) {
        object.mCallDismissManager = this.mCallDismissManager.get();
    }
}
