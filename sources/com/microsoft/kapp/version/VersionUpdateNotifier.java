package com.microsoft.kapp.version;

import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.version.VersionRetriever;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public abstract class VersionUpdateNotifier<T extends VersionRetriever> implements Runnable {
    private final CopyOnWriteArrayList<VersionUpdateListener> mListeners;
    private final T mRetriever;
    private T mRetrieverOverride;

    @Override // java.lang.Runnable
    public abstract void run();

    /* JADX INFO: Access modifiers changed from: protected */
    public VersionUpdateNotifier(T retriever) {
        Validate.notNull(retriever, "retriever");
        this.mRetriever = retriever;
        this.mListeners = new CopyOnWriteArrayList<>();
    }

    public void registerListener(VersionUpdateListener listener) {
        Validate.notNull(listener, "listener");
        this.mListeners.add(listener);
    }

    public void unregisterListener(VersionUpdateListener listener) {
        Validate.notNull(listener, "listener");
        this.mListeners.remove(listener);
    }

    public void setVersionRetrieverOverride(T retrieverOverride) {
        this.mRetrieverOverride = retrieverOverride;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public T getRetriever() {
        return this.mRetrieverOverride != null ? this.mRetrieverOverride : this.mRetriever;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void notifyVersionUpdateDetected(VersionUpdate versionUpdate) {
        Validate.notNull(versionUpdate, "versionUpdate");
        Iterator i$ = this.mListeners.iterator();
        while (i$.hasNext()) {
            VersionUpdateListener listener = i$.next();
            listener.versionUpdateDetected(this, versionUpdate);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void notifyVersionUpdateCheckFailed(Exception ex) {
        Validate.notNull(ex, "ex");
        Iterator i$ = this.mListeners.iterator();
        while (i$.hasNext()) {
            VersionUpdateListener listener = i$.next();
            listener.versionUpdateCheckFailed(this, ex);
        }
    }
}
