package com.microsoft.kapp.activities;

import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UserEventDetailsActivity$$InjectAdapter extends Binding<UserEventDetailsActivity> implements Provider<UserEventDetailsActivity>, MembersInjector<UserEventDetailsActivity> {
    private Binding<GolfService> mGolfService;
    private Binding<RestService> mService;
    private Binding<BaseFragmentActivityWithOfflineSupport> supertype;

    public UserEventDetailsActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.UserEventDetailsActivity", "members/com.microsoft.kapp.activities.UserEventDetailsActivity", false, UserEventDetailsActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", UserEventDetailsActivity.class, getClass().getClassLoader());
        this.mGolfService = linker.requestBinding("com.microsoft.kapp.services.golf.GolfService", UserEventDetailsActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport", UserEventDetailsActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mService);
        injectMembersBindings.add(this.mGolfService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public UserEventDetailsActivity get() {
        UserEventDetailsActivity result = new UserEventDetailsActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(UserEventDetailsActivity object) {
        object.mService = this.mService.get();
        object.mGolfService = this.mGolfService.get();
        this.supertype.injectMembers(object);
    }
}
