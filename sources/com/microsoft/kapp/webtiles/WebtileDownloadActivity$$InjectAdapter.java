package com.microsoft.kapp.webtiles;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WebtileDownloadActivity$$InjectAdapter extends Binding<WebtileDownloadActivity> implements Provider<WebtileDownloadActivity>, MembersInjector<WebtileDownloadActivity> {
    private Binding<CargoConnection> mCargoConnection;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseActivity> supertype;

    public WebtileDownloadActivity$$InjectAdapter() {
        super("com.microsoft.kapp.webtiles.WebtileDownloadActivity", "members/com.microsoft.kapp.webtiles.WebtileDownloadActivity", false, WebtileDownloadActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", WebtileDownloadActivity.class, getClass().getClassLoader());
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", WebtileDownloadActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseActivity", WebtileDownloadActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public WebtileDownloadActivity get() {
        WebtileDownloadActivity result = new WebtileDownloadActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(WebtileDownloadActivity object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mCargoConnection = this.mCargoConnection.get();
        this.supertype.injectMembers(object);
    }
}
