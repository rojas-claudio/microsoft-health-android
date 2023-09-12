package com.microsoft.kapp.services;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.ContactResolver;
import com.microsoft.kapp.telephony.MmsSmsMessageMetadataRetriever;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class MessageNotificationHandler$$InjectAdapter extends Binding<MessageNotificationHandler> implements MembersInjector<MessageNotificationHandler> {
    private Binding<CargoConnection> mCargoConnection;
    private Binding<ContactResolver> mContactResolver;
    private Binding<MmsSmsMessageMetadataRetriever> mMetadataRetriever;
    private Binding<InjectableNotificationHandler> supertype;

    public MessageNotificationHandler$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.services.MessageNotificationHandler", false, MessageNotificationHandler.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", MessageNotificationHandler.class, getClass().getClassLoader());
        this.mContactResolver = linker.requestBinding("com.microsoft.kapp.ContactResolver", MessageNotificationHandler.class, getClass().getClassLoader());
        this.mMetadataRetriever = linker.requestBinding("com.microsoft.kapp.telephony.MmsSmsMessageMetadataRetriever", MessageNotificationHandler.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.services.InjectableNotificationHandler", MessageNotificationHandler.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.mContactResolver);
        injectMembersBindings.add(this.mMetadataRetriever);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(MessageNotificationHandler object) {
        object.mCargoConnection = this.mCargoConnection.get();
        object.mContactResolver = this.mContactResolver.get();
        object.mMetadataRetriever = this.mMetadataRetriever.get();
        this.supertype.injectMembers(object);
    }
}
