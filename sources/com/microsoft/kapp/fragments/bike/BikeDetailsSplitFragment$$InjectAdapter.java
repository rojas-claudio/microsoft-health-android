package com.microsoft.kapp.fragments.bike;

import com.microsoft.kapp.fragments.BaseHomeTileFragment;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BikeDetailsSplitFragment$$InjectAdapter extends Binding<BikeDetailsSplitFragment> implements Provider<BikeDetailsSplitFragment>, MembersInjector<BikeDetailsSplitFragment> {
    private Binding<RestService> mRestService;
    private Binding<SettingsProvider> mSettings;
    private Binding<BaseHomeTileFragment> supertype;

    public BikeDetailsSplitFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.bike.BikeDetailsSplitFragment", "members/com.microsoft.kapp.fragments.bike.BikeDetailsSplitFragment", false, BikeDetailsSplitFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettings = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", BikeDetailsSplitFragment.class, getClass().getClassLoader());
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", BikeDetailsSplitFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseHomeTileFragment", BikeDetailsSplitFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettings);
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public BikeDetailsSplitFragment get() {
        BikeDetailsSplitFragment result = new BikeDetailsSplitFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(BikeDetailsSplitFragment object) {
        object.mSettings = this.mSettings.get();
        object.mRestService = this.mRestService.get();
        this.supertype.injectMembers(object);
    }
}
