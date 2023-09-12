package com.microsoft.kapp.fragments.guidedworkout;

import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class GuidedWorkoutsBrowsePlanBaseFragment$$InjectAdapter extends Binding<GuidedWorkoutsBrowsePlanBaseFragment> implements MembersInjector<GuidedWorkoutsBrowsePlanBaseFragment> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<MultiDeviceManager> mMultiDeviceManager;
    private Binding<SettingsProvider> mSettings;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public GuidedWorkoutsBrowsePlanBaseFragment$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanBaseFragment", false, GuidedWorkoutsBrowsePlanBaseFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettings = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", GuidedWorkoutsBrowsePlanBaseFragment.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", GuidedWorkoutsBrowsePlanBaseFragment.class, getClass().getClassLoader());
        this.mMultiDeviceManager = linker.requestBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", GuidedWorkoutsBrowsePlanBaseFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", GuidedWorkoutsBrowsePlanBaseFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettings);
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.mMultiDeviceManager);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GuidedWorkoutsBrowsePlanBaseFragment object) {
        object.mSettings = this.mSettings.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        object.mMultiDeviceManager = this.mMultiDeviceManager.get();
        this.supertype.injectMembers(object);
    }
}
