package com.microsoft.kapp.sensor.service;

import com.microsoft.kapp.sensor.SensorDataLogger;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SensorService$$InjectAdapter extends Binding<SensorService> implements Provider<SensorService>, MembersInjector<SensorService> {
    private Binding<SensorDataLogger> mSensorDataLogger;

    public SensorService$$InjectAdapter() {
        super("com.microsoft.kapp.sensor.service.SensorService", "members/com.microsoft.kapp.sensor.service.SensorService", false, SensorService.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSensorDataLogger = linker.requestBinding("com.microsoft.kapp.sensor.SensorDataLogger", SensorService.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSensorDataLogger);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public SensorService get() {
        SensorService result = new SensorService();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SensorService object) {
        object.mSensorDataLogger = this.mSensorDataLogger.get();
    }
}
