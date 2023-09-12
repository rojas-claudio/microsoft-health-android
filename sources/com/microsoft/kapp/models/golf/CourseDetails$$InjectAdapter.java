package com.microsoft.kapp.models.golf;

import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CourseDetails$$InjectAdapter extends Binding<CourseDetails> implements Provider<CourseDetails>, MembersInjector<CourseDetails> {
    private Binding<SettingsProvider> mSettingsProvider;

    public CourseDetails$$InjectAdapter() {
        super("com.microsoft.kapp.models.golf.CourseDetails", "members/com.microsoft.kapp.models.golf.CourseDetails", false, CourseDetails.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", CourseDetails.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public CourseDetails get() {
        CourseDetails result = new CourseDetails();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CourseDetails object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
    }
}
