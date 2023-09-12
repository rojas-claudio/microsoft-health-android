package com.microsoft.kapp.fragments;

import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CredentialsFragment$$InjectAdapter extends Binding<CredentialsFragment> implements Provider<CredentialsFragment>, MembersInjector<CredentialsFragment> {
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<BaseFragment> supertype;

    public CredentialsFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.CredentialsFragment", "members/com.microsoft.kapp.fragments.CredentialsFragment", false, CredentialsFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", CredentialsFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", CredentialsFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public CredentialsFragment get() {
        CredentialsFragment result = new CredentialsFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CredentialsFragment object) {
        object.mCredentialsManager = this.mCredentialsManager.get();
        this.supertype.injectMembers(object);
    }
}
