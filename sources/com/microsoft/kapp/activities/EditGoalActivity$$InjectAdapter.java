package com.microsoft.kapp.activities;

import com.microsoft.kapp.models.goal.GoalProcessorManager;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class EditGoalActivity$$InjectAdapter extends Binding<EditGoalActivity> implements MembersInjector<EditGoalActivity> {
    private Binding<GoalProcessorManager> mGoalProcessorManager;
    private Binding<MultiDeviceManager> mMultiDeviceManager;
    private Binding<RestService> mRestService;
    private Binding<BaseFragmentActivity> supertype;

    public EditGoalActivity$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.activities.EditGoalActivity", false, EditGoalActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", EditGoalActivity.class, getClass().getClassLoader());
        this.mMultiDeviceManager = linker.requestBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", EditGoalActivity.class, getClass().getClassLoader());
        this.mGoalProcessorManager = linker.requestBinding("com.microsoft.kapp.models.goal.GoalProcessorManager", EditGoalActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseFragmentActivity", EditGoalActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mMultiDeviceManager);
        injectMembersBindings.add(this.mGoalProcessorManager);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(EditGoalActivity object) {
        object.mRestService = this.mRestService.get();
        object.mMultiDeviceManager = this.mMultiDeviceManager.get();
        object.mGoalProcessorManager = this.mGoalProcessorManager.get();
        this.supertype.injectMembers(object);
    }
}
