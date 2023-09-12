package com.microsoft.kapp.telephony;

import com.microsoft.kapp.database.LoggingContentResolver;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MessageThreadsRetriever$$InjectAdapter extends Binding<MessageThreadsRetriever> implements Provider<MessageThreadsRetriever> {
    private Binding<LoggingContentResolver> contentResolver;

    public MessageThreadsRetriever$$InjectAdapter() {
        super("com.microsoft.kapp.telephony.MessageThreadsRetriever", "members/com.microsoft.kapp.telephony.MessageThreadsRetriever", false, MessageThreadsRetriever.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.contentResolver = linker.requestBinding("com.microsoft.kapp.database.LoggingContentResolver", MessageThreadsRetriever.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        getBindings.add(this.contentResolver);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public MessageThreadsRetriever get() {
        MessageThreadsRetriever result = new MessageThreadsRetriever(this.contentResolver.get());
        return result;
    }
}
