package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.squareup.picasso.Picasso;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
abstract class Action<T> {
    boolean cancelled;
    final Drawable errorDrawable;
    final int errorResId;
    final String key;
    final boolean noFade;
    final Picasso picasso;
    final Request request;
    final boolean skipCache;
    final Object tag;
    final WeakReference<T> target;
    boolean willReplay;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void complete(Bitmap bitmap, Picasso.LoadedFrom loadedFrom);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void error();

    /* loaded from: classes.dex */
    static class RequestWeakReference<M> extends WeakReference<M> {
        final Action action;

        public RequestWeakReference(Action action, M referent, ReferenceQueue<? super M> q) {
            super(referent, q);
            this.action = action;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Action(Picasso picasso, T target, Request request, boolean skipCache, boolean noFade, int errorResId, Drawable errorDrawable, String key, Object tag) {
        this.picasso = picasso;
        this.request = request;
        this.target = target == null ? null : new RequestWeakReference(this, target, picasso.referenceQueue);
        this.skipCache = skipCache;
        this.noFade = noFade;
        this.errorResId = errorResId;
        this.errorDrawable = errorDrawable;
        this.key = key;
        this.tag = tag == null ? this : tag;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancel() {
        this.cancelled = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Request getRequest() {
        return this.request;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public T getTarget() {
        if (this.target == null) {
            return null;
        }
        return this.target.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getKey() {
        return this.key;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean willReplay() {
        return this.willReplay;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Picasso getPicasso() {
        return this.picasso;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Picasso.Priority getPriority() {
        return this.request.priority;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object getTag() {
        return this.tag;
    }
}
