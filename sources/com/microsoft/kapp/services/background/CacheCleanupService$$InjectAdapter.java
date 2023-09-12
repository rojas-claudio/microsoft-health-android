package com.microsoft.kapp.services.background;

import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.services.InjectableIntentService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CacheCleanupService$$InjectAdapter extends Binding<CacheCleanupService> implements Provider<CacheCleanupService>, MembersInjector<CacheCleanupService> {
    private Binding<CacheService> mCacheService;
    private Binding<InjectableIntentService> supertype;

    public CacheCleanupService$$InjectAdapter() {
        super("com.microsoft.kapp.services.background.CacheCleanupService", "members/com.microsoft.kapp.services.background.CacheCleanupService", false, CacheCleanupService.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCacheService = linker.requestBinding("com.microsoft.kapp.cache.CacheService", CacheCleanupService.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.services.InjectableIntentService", CacheCleanupService.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCacheService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public CacheCleanupService get() {
        CacheCleanupService result = new CacheCleanupService();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CacheCleanupService object) {
        object.mCacheService = this.mCacheService.get();
        this.supertype.injectMembers(object);
    }
}
