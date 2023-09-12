package com.microsoft.kapp.activities;

import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.tasks.FirmwareUpdateTask;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class OobeFirmwareUpdateActivity$$InjectAdapter extends Binding<OobeFirmwareUpdateActivity> implements Provider<OobeFirmwareUpdateActivity>, MembersInjector<OobeFirmwareUpdateActivity> {
    private Binding<PersonalizationManagerFactory> mPersonalizationManagerFactory;
    private Binding<FirmwareUpdateTask> mTask;
    private Binding<OobeBaseActivity> supertype;

    public OobeFirmwareUpdateActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.OobeFirmwareUpdateActivity", "members/com.microsoft.kapp.activities.OobeFirmwareUpdateActivity", false, OobeFirmwareUpdateActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mTask = linker.requestBinding("com.microsoft.kapp.tasks.FirmwareUpdateTask", OobeFirmwareUpdateActivity.class, getClass().getClassLoader());
        this.mPersonalizationManagerFactory = linker.requestBinding("com.microsoft.kapp.personalization.PersonalizationManagerFactory", OobeFirmwareUpdateActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.OobeBaseActivity", OobeFirmwareUpdateActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mTask);
        injectMembersBindings.add(this.mPersonalizationManagerFactory);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public OobeFirmwareUpdateActivity get() {
        OobeFirmwareUpdateActivity result = new OobeFirmwareUpdateActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(OobeFirmwareUpdateActivity object) {
        object.mTask = this.mTask.get();
        object.mPersonalizationManagerFactory = this.mPersonalizationManagerFactory.get();
        this.supertype.injectMembers(object);
    }
}
