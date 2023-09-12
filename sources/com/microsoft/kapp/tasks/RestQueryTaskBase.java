package com.microsoft.kapp.tasks;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.tasks.OnTaskStateChangedListener;
import com.microsoft.krestsdk.services.RestService;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public abstract class RestQueryTaskBase<TResult, TListener extends OnTaskStateChangedListener> implements StateListenerTask {
    public static final int ACTIVITY = 0;
    public static final int FRAGMENT = 1;
    protected final String TAG = getClass().getSimpleName();
    private TListener mListener;
    private WeakReference<Activity> mParentActivityWeakReference;
    private WeakReference<Fragment> mParentFragmentWeakReference;
    private RestService mRestService;
    private int mType;

    protected abstract void executeInternal(Callback<TResult> callback);

    protected abstract void onSuccess(TResult tresult);

    /* loaded from: classes.dex */
    public static abstract class Builder<TBuilder extends Builder, TListener extends OnTaskStateChangedListener> {
        private TListener mListener;
        private WeakReference<Activity> mParentActivityWeakReference;
        private WeakReference<Fragment> mParentFragmentWeakReference;
        private RestService mRestService;
        private int mType;

        public abstract RestQueryTaskBase build();

        public TBuilder forParentFragment(Fragment parentFragment) {
            Validate.notNull(parentFragment, "parentFragment");
            this.mParentFragmentWeakReference = new WeakReference<>(parentFragment);
            this.mType = 1;
            return this;
        }

        public TBuilder forParentFragment(WeakReference<Fragment> parentFragmentWeakRef) {
            Validate.notNull(parentFragmentWeakRef, "parentFragmentWeakRef");
            this.mParentFragmentWeakReference = parentFragmentWeakRef;
            this.mType = 1;
            return this;
        }

        public TBuilder forParentActivity(Activity parentActivity) {
            Validate.notNull(parentActivity, "parentActivity");
            this.mParentActivityWeakReference = new WeakReference<>(parentActivity);
            this.mType = 0;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public WeakReference<Fragment> getParentFragment() {
            return this.mParentFragmentWeakReference;
        }

        protected WeakReference<Activity> getParentActivity() {
            return this.mParentActivityWeakReference;
        }

        protected int getParentType() {
            return this.mType;
        }

        public TBuilder usingRestService(RestService restService) {
            Validate.notNull(restService, "restService");
            this.mRestService = restService;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public RestService getRestService() {
            return this.mRestService;
        }

        public TBuilder withListener(TListener listener) {
            Validate.notNull(listener, "listener");
            this.mListener = listener;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void validate() {
            if (this.mRestService == null) {
                throw new IllegalStateException("RestService is not set.");
            }
            if (this.mListener == null) {
                throw new IllegalStateException("Listener is not set.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RestQueryTaskBase(Builder builder) {
        Validate.notNull(builder, "builder");
        this.mParentFragmentWeakReference = builder.mParentFragmentWeakReference;
        this.mRestService = builder.mRestService;
        this.mListener = (TListener) builder.mListener;
        this.mParentActivityWeakReference = builder.mParentActivityWeakReference;
        this.mType = builder.mType;
    }

    @Override // com.microsoft.kapp.tasks.StateListenerTask
    public TListener getListener() {
        return this.mListener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WeakReference<Fragment> getParentFragment() {
        return this.mParentFragmentWeakReference;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WeakReference<Activity> getParentActivity() {
        return this.mParentActivityWeakReference;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getParentType() {
        return this.mType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RestService getRestService() {
        return this.mRestService;
    }

    @Override // com.microsoft.kapp.tasks.StateListenerTask
    public void execute() {
        Callback<TResult> callback = new Callback<TResult>() { // from class: com.microsoft.kapp.tasks.RestQueryTaskBase.1
            @Override // com.microsoft.kapp.Callback
            public void callback(TResult result) {
                RestQueryTaskBase.this.onSuccess(result);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                RestQueryTaskBase.this.onFailed(ex);
            }
        };
        switch (this.mType) {
            case 0:
                if (this.mParentActivityWeakReference != null && this.mParentActivityWeakReference.get() != null) {
                    callback = new ActivityScopedCallback<>(this.mParentActivityWeakReference.get(), callback);
                    break;
                }
                break;
            case 1:
                if (this.mParentFragmentWeakReference != null && this.mParentFragmentWeakReference.get() != null) {
                    callback = new ActivityScopedCallback<>(this.mParentFragmentWeakReference.get(), callback);
                    break;
                }
                break;
            default:
                KLog.e(this.TAG, "mType is incorrect! Task not started!");
                return;
        }
        executeInternal(callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onFailed(Exception ex) {
        this.mListener.onTaskFailed(this, ex);
    }
}
