package com.microsoft.kapp.activities;

import com.microsoft.kapp.cache.CacheService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TMAGConnectActivity$$InjectAdapter extends Binding<TMAGConnectActivity> implements Provider<TMAGConnectActivity>, MembersInjector<TMAGConnectActivity> {
    private Binding<CacheService> mCacheService;
    private Binding<BaseFragmentActivityWithOfflineSupport> supertype;

    public TMAGConnectActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.TMAGConnectActivity", "members/com.microsoft.kapp.activities.TMAGConnectActivity", false, TMAGConnectActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCacheService = linker.requestBinding("com.microsoft.kapp.cache.CacheService", TMAGConnectActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport", TMAGConnectActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCacheService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public TMAGConnectActivity get() {
        TMAGConnectActivity result = new TMAGConnectActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(TMAGConnectActivity object) {
        object.mCacheService = this.mCacheService.get();
        this.supertype.injectMembers(object);
    }
}
