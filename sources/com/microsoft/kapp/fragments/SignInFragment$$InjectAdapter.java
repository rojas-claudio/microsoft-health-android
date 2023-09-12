package com.microsoft.kapp.fragments;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.krestsdk.auth.KdsFetcher;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SignInFragment$$InjectAdapter extends Binding<SignInFragment> implements Provider<SignInFragment>, MembersInjector<SignInFragment> {
    private Binding<CargoConnection> mCargoConnection;
    private Binding<KdsFetcher> mKdsFetcher;

    public SignInFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.SignInFragment", "members/com.microsoft.kapp.fragments.SignInFragment", false, SignInFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mKdsFetcher = linker.requestBinding("com.microsoft.krestsdk.auth.KdsFetcher", SignInFragment.class, getClass().getClassLoader());
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", SignInFragment.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mKdsFetcher);
        injectMembersBindings.add(this.mCargoConnection);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public SignInFragment get() {
        SignInFragment result = new SignInFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SignInFragment object) {
        object.mKdsFetcher = this.mKdsFetcher.get();
        object.mCargoConnection = this.mCargoConnection.get();
    }
}
