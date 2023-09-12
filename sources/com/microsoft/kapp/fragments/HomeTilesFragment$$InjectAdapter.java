package com.microsoft.kapp.fragments;

import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HomeTilesFragment$$InjectAdapter extends Binding<HomeTilesFragment> implements Provider<HomeTilesFragment>, MembersInjector<HomeTilesFragment> {
    private Binding<MsaAuth> mAuthService;
    private Binding<GolfService> mGolfService;
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<RestService> mRestService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<UserProfileFetcher> mUserProfileFetcher;
    private Binding<BaseFragment> supertype;

    public HomeTilesFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.HomeTilesFragment", "members/com.microsoft.kapp.fragments.HomeTilesFragment", false, HomeTilesFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", HomeTilesFragment.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", HomeTilesFragment.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", HomeTilesFragment.class, getClass().getClassLoader());
        this.mGolfService = linker.requestBinding("com.microsoft.kapp.services.golf.GolfService", HomeTilesFragment.class, getClass().getClassLoader());
        this.mAuthService = linker.requestBinding("com.microsoft.krestsdk.auth.MsaAuth", HomeTilesFragment.class, getClass().getClassLoader());
        this.mUserProfileFetcher = linker.requestBinding("com.microsoft.kapp.UserProfileFetcher", HomeTilesFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", HomeTilesFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.mGolfService);
        injectMembersBindings.add(this.mAuthService);
        injectMembersBindings.add(this.mUserProfileFetcher);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public HomeTilesFragment get() {
        HomeTilesFragment result = new HomeTilesFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(HomeTilesFragment object) {
        object.mRestService = this.mRestService.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        object.mGolfService = this.mGolfService.get();
        object.mAuthService = this.mAuthService.get();
        object.mUserProfileFetcher = this.mUserProfileFetcher.get();
        this.supertype.injectMembers(object);
    }
}
