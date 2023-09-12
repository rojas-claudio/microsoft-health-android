package com.microsoft.kapp.activities;

import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BingMapActivity$$InjectAdapter extends Binding<BingMapActivity> implements Provider<BingMapActivity>, MembersInjector<BingMapActivity> {
    private Binding<RestService> mRestService;
    private Binding<BaseFragmentActivityWithOfflineSupport> supertype;

    public BingMapActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.BingMapActivity", "members/com.microsoft.kapp.activities.BingMapActivity", false, BingMapActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", BingMapActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport", BingMapActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public BingMapActivity get() {
        BingMapActivity result = new BingMapActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(BingMapActivity object) {
        object.mRestService = this.mRestService.get();
        this.supertype.injectMembers(object);
    }
}
