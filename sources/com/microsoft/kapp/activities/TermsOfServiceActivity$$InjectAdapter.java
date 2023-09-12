package com.microsoft.kapp.activities;

import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TermsOfServiceActivity$$InjectAdapter extends Binding<TermsOfServiceActivity> implements Provider<TermsOfServiceActivity>, MembersInjector<TermsOfServiceActivity> {
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<FiddlerLogger> mFiddlerLogger;
    private Binding<OobeBaseActivity> supertype;

    public TermsOfServiceActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.TermsOfServiceActivity", "members/com.microsoft.kapp.activities.TermsOfServiceActivity", false, TermsOfServiceActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", TermsOfServiceActivity.class, getClass().getClassLoader());
        this.mFiddlerLogger = linker.requestBinding("com.microsoft.kapp.logging.http.FiddlerLogger", TermsOfServiceActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.OobeBaseActivity", TermsOfServiceActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.mFiddlerLogger);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public TermsOfServiceActivity get() {
        TermsOfServiceActivity result = new TermsOfServiceActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(TermsOfServiceActivity object) {
        object.mCredentialsManager = this.mCredentialsManager.get();
        object.mFiddlerLogger = this.mFiddlerLogger.get();
        this.supertype.injectMembers(object);
    }
}
