package com.microsoft.kapp.telephony;

import android.content.Context;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MmsSmsMessagesObserver$$InjectAdapter extends Binding<MmsSmsMessagesObserver> implements Provider<MmsSmsMessagesObserver>, MembersInjector<MmsSmsMessagesObserver> {
    private Binding<MmsSmsMessageMetadataRetriever> field_mMetadataRetriever;
    private Binding<Context> parameter_context;

    public MmsSmsMessagesObserver$$InjectAdapter() {
        super("com.microsoft.kapp.telephony.MmsSmsMessagesObserver", "members/com.microsoft.kapp.telephony.MmsSmsMessagesObserver", true, MmsSmsMessagesObserver.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.parameter_context = linker.requestBinding("android.content.Context", MmsSmsMessagesObserver.class, getClass().getClassLoader());
        this.field_mMetadataRetriever = linker.requestBinding("com.microsoft.kapp.telephony.MmsSmsMessageMetadataRetriever", MmsSmsMessagesObserver.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        getBindings.add(this.parameter_context);
        injectMembersBindings.add(this.field_mMetadataRetriever);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public MmsSmsMessagesObserver get() {
        MmsSmsMessagesObserver result = new MmsSmsMessagesObserver(this.parameter_context.get());
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(MmsSmsMessagesObserver object) {
        object.mMetadataRetriever = this.field_mMetadataRetriever.get();
    }
}
