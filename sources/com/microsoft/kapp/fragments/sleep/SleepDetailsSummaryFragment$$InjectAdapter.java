package com.microsoft.kapp.fragments.sleep;

import com.microsoft.kapp.fragments.BaseEventSummaryFragment;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SleepDetailsSummaryFragment$$InjectAdapter extends Binding<SleepDetailsSummaryFragment> implements Provider<SleepDetailsSummaryFragment>, MembersInjector<SleepDetailsSummaryFragment> {
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<BaseEventSummaryFragment> supertype;

    public SleepDetailsSummaryFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.sleep.SleepDetailsSummaryFragment", "members/com.microsoft.kapp.fragments.sleep.SleepDetailsSummaryFragment", false, SleepDetailsSummaryFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", SleepDetailsSummaryFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseEventSummaryFragment", SleepDetailsSummaryFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public SleepDetailsSummaryFragment get() {
        SleepDetailsSummaryFragment result = new SleepDetailsSummaryFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SleepDetailsSummaryFragment object) {
        object.mCredentialsManager = this.mCredentialsManager.get();
        this.supertype.injectMembers(object);
    }
}
