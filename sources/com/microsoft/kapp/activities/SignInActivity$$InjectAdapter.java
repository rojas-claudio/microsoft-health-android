package com.microsoft.kapp.activities;

import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SignInActivity$$InjectAdapter extends Binding<SignInActivity> implements Provider<SignInActivity>, MembersInjector<SignInActivity> {
    private Binding<AppConfigurationManager> mAppConfigurationManager;
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<SettingsProvider> mSettingProvider;
    private Binding<OobeBaseActivity> supertype;

    public SignInActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.SignInActivity", "members/com.microsoft.kapp.activities.SignInActivity", false, SignInActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", SignInActivity.class, getClass().getClassLoader());
        this.mSettingProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", SignInActivity.class, getClass().getClassLoader());
        this.mAppConfigurationManager = linker.requestBinding("com.microsoft.kapp.utils.AppConfigurationManager", SignInActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.OobeBaseActivity", SignInActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.mSettingProvider);
        injectMembersBindings.add(this.mAppConfigurationManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public SignInActivity get() {
        SignInActivity result = new SignInActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SignInActivity object) {
        object.mCredentialsManager = this.mCredentialsManager.get();
        object.mSettingProvider = this.mSettingProvider.get();
        object.mAppConfigurationManager = this.mAppConfigurationManager.get();
        this.supertype.injectMembers(object);
    }
}
