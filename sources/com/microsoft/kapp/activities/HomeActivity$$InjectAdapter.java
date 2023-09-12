package com.microsoft.kapp.activities;

import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.kapp.utils.GooglePlayUtils;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HomeActivity$$InjectAdapter extends Binding<HomeActivity> implements Provider<HomeActivity>, MembersInjector<HomeActivity> {
    private Binding<AppConfigurationManager> mAppConfigurationManager;
    private Binding<GooglePlayUtils> mGooglePlayUtils;
    private Binding<UserProfileFetcher> mUserProfileFetcher;
    private Binding<BaseFragmentActivity> supertype;

    public HomeActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.HomeActivity", "members/com.microsoft.kapp.activities.HomeActivity", false, HomeActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGooglePlayUtils = linker.requestBinding("com.microsoft.kapp.utils.GooglePlayUtils", HomeActivity.class, getClass().getClassLoader());
        this.mUserProfileFetcher = linker.requestBinding("com.microsoft.kapp.UserProfileFetcher", HomeActivity.class, getClass().getClassLoader());
        this.mAppConfigurationManager = linker.requestBinding("com.microsoft.kapp.utils.AppConfigurationManager", HomeActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseFragmentActivity", HomeActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGooglePlayUtils);
        injectMembersBindings.add(this.mUserProfileFetcher);
        injectMembersBindings.add(this.mAppConfigurationManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public HomeActivity get() {
        HomeActivity result = new HomeActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(HomeActivity object) {
        object.mGooglePlayUtils = this.mGooglePlayUtils.get();
        object.mUserProfileFetcher = this.mUserProfileFetcher.get();
        object.mAppConfigurationManager = this.mAppConfigurationManager.get();
        this.supertype.injectMembers(object);
    }
}
