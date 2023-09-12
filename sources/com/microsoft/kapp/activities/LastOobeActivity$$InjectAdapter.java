package com.microsoft.kapp.activities;

import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.models.strapp.DefaultStrappManager;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class LastOobeActivity$$InjectAdapter extends Binding<LastOobeActivity> implements MembersInjector<LastOobeActivity> {
    private Binding<MsaAuth> mAuthService;
    private Binding<DefaultStrappManager> mDefaultStrappManager;
    private Binding<MultiDeviceManager> mMultiDeviceManager;
    private Binding<PersonalizationManagerFactory> mPersonalizationManagerFactory;
    private Binding<RestService> mRestService;
    private Binding<UserProfileFetcher> mUserProfileFetcher;
    private Binding<OobeBaseActivity> supertype;

    public LastOobeActivity$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.activities.LastOobeActivity", false, LastOobeActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", LastOobeActivity.class, getClass().getClassLoader());
        this.mPersonalizationManagerFactory = linker.requestBinding("com.microsoft.kapp.personalization.PersonalizationManagerFactory", LastOobeActivity.class, getClass().getClassLoader());
        this.mMultiDeviceManager = linker.requestBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", LastOobeActivity.class, getClass().getClassLoader());
        this.mUserProfileFetcher = linker.requestBinding("com.microsoft.kapp.UserProfileFetcher", LastOobeActivity.class, getClass().getClassLoader());
        this.mDefaultStrappManager = linker.requestBinding("com.microsoft.kapp.models.strapp.DefaultStrappManager", LastOobeActivity.class, getClass().getClassLoader());
        this.mAuthService = linker.requestBinding("com.microsoft.krestsdk.auth.MsaAuth", LastOobeActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.OobeBaseActivity", LastOobeActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mPersonalizationManagerFactory);
        injectMembersBindings.add(this.mMultiDeviceManager);
        injectMembersBindings.add(this.mUserProfileFetcher);
        injectMembersBindings.add(this.mDefaultStrappManager);
        injectMembersBindings.add(this.mAuthService);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(LastOobeActivity object) {
        object.mRestService = this.mRestService.get();
        object.mPersonalizationManagerFactory = this.mPersonalizationManagerFactory.get();
        object.mMultiDeviceManager = this.mMultiDeviceManager.get();
        object.mUserProfileFetcher = this.mUserProfileFetcher.get();
        object.mDefaultStrappManager = this.mDefaultStrappManager.get();
        object.mAuthService = this.mAuthService.get();
        this.supertype.injectMembers(object);
    }
}
