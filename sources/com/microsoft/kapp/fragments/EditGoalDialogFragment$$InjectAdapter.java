package com.microsoft.kapp.fragments;

import com.microsoft.kapp.models.goal.GoalProcessorManager;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class EditGoalDialogFragment$$InjectAdapter extends Binding<EditGoalDialogFragment> implements MembersInjector<EditGoalDialogFragment> {
    private Binding<GoalProcessorManager> mGoalProcessorManager;
    private Binding<RestService> mRestService;
    private Binding<HeaderBarDialogFragment> supertype;

    public EditGoalDialogFragment$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.fragments.EditGoalDialogFragment", false, EditGoalDialogFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", EditGoalDialogFragment.class, getClass().getClassLoader());
        this.mGoalProcessorManager = linker.requestBinding("com.microsoft.kapp.models.goal.GoalProcessorManager", EditGoalDialogFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.HeaderBarDialogFragment", EditGoalDialogFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mGoalProcessorManager);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(EditGoalDialogFragment object) {
        object.mRestService = this.mRestService.get();
        object.mGoalProcessorManager = this.mGoalProcessorManager.get();
        this.supertype.injectMembers(object);
    }
}
