package com.microsoft.kapp.models.home;

import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class BaseDataFetcher$$InjectAdapter extends Binding<BaseDataFetcher> implements MembersInjector<BaseDataFetcher> {
    private Binding<RestService> mService;
    private Binding<SettingsProvider> mSettingsProvider;

    public BaseDataFetcher$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.models.home.BaseDataFetcher", false, BaseDataFetcher.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", BaseDataFetcher.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", BaseDataFetcher.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mService);
        injectMembersBindings.add(this.mSettingsProvider);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(BaseDataFetcher object) {
        object.mService = this.mService.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
    }
}
