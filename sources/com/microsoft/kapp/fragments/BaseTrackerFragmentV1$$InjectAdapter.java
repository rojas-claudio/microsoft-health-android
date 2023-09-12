package com.microsoft.kapp.fragments;

import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class BaseTrackerFragmentV1$$InjectAdapter extends Binding<BaseTrackerFragmentV1> implements MembersInjector<BaseTrackerFragmentV1> {
    private Binding<CredentialsManager> mCredentials;
    private Binding<RestService> mService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseHomeTileFragment> supertype;

    public BaseTrackerFragmentV1$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.fragments.BaseTrackerFragmentV1", false, BaseTrackerFragmentV1.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", BaseTrackerFragmentV1.class, getClass().getClassLoader());
        this.mCredentials = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", BaseTrackerFragmentV1.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", BaseTrackerFragmentV1.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseHomeTileFragment", BaseTrackerFragmentV1.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mService);
        injectMembersBindings.add(this.mCredentials);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(BaseTrackerFragmentV1 object) {
        object.mService = this.mService.get();
        object.mCredentials = this.mCredentials.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}
