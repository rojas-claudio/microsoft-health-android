package com.microsoft.kapp.telephony;

import android.content.Context;
import com.microsoft.kapp.database.LoggingContentResolver;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MmsSmsMessageMetadataRetriever$$InjectAdapter extends Binding<MmsSmsMessageMetadataRetriever> implements Provider<MmsSmsMessageMetadataRetriever> {
    private Binding<LoggingContentResolver> contentResolver;
    private Binding<Context> context;
    private Binding<MessageMetadataRetriever> retriever;

    public MmsSmsMessageMetadataRetriever$$InjectAdapter() {
        super("com.microsoft.kapp.telephony.MmsSmsMessageMetadataRetriever", "members/com.microsoft.kapp.telephony.MmsSmsMessageMetadataRetriever", false, MmsSmsMessageMetadataRetriever.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.contentResolver = linker.requestBinding("com.microsoft.kapp.database.LoggingContentResolver", MmsSmsMessageMetadataRetriever.class, getClass().getClassLoader());
        this.retriever = linker.requestBinding("com.microsoft.kapp.telephony.MessageMetadataRetriever", MmsSmsMessageMetadataRetriever.class, getClass().getClassLoader());
        this.context = linker.requestBinding("android.content.Context", MmsSmsMessageMetadataRetriever.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        getBindings.add(this.contentResolver);
        getBindings.add(this.retriever);
        getBindings.add(this.context);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public MmsSmsMessageMetadataRetriever get() {
        MmsSmsMessageMetadataRetriever result = new MmsSmsMessageMetadataRetriever(this.contentResolver.get(), this.retriever.get(), this.context.get());
        return result;
    }
}
