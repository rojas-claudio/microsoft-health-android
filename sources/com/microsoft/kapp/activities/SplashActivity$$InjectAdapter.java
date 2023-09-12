package com.microsoft.kapp.activities;

import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SplashActivity$$InjectAdapter extends Binding<SplashActivity> implements Provider<SplashActivity>, MembersInjector<SplashActivity> {
    private Binding<FiddlerLogger> mFiddlerLogger;
    private Binding<SettingsProvider> mSettingsProvider;

    public SplashActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.SplashActivity", "members/com.microsoft.kapp.activities.SplashActivity", false, SplashActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", SplashActivity.class, getClass().getClassLoader());
        this.mFiddlerLogger = linker.requestBinding("com.microsoft.kapp.logging.http.FiddlerLogger", SplashActivity.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mFiddlerLogger);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public SplashActivity get() {
        SplashActivity result = new SplashActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SplashActivity object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mFiddlerLogger = this.mFiddlerLogger.get();
    }
}
