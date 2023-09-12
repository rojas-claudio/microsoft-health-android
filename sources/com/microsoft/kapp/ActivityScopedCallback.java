package com.microsoft.kapp;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.microsoft.kapp.diagnostics.Validate;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class ActivityScopedCallback<T> implements Callback<T> {
    private static final int ACTIVITY = 0;
    private static final int FRAGMENT = 1;
    private Callback<T> mCallback;
    private int mScopeType = 0;
    private WeakReference<Activity> mWeakActivity;
    private WeakReference<Fragment> mWeakFragment;

    public ActivityScopedCallback(Fragment fragment, Callback<T> callback) {
        this.mWeakFragment = new WeakReference<>(fragment);
        this.mCallback = callback;
    }

    public ActivityScopedCallback(Activity activity, Callback<T> callback) {
        this.mWeakActivity = new WeakReference<>(activity);
        this.mCallback = callback;
    }

    @Override // com.microsoft.kapp.Callback
    public void callback(T result) {
        if (isValid()) {
            this.mCallback.callback(result);
        }
    }

    @Override // com.microsoft.kapp.Callback
    public void onError(Exception ex) {
        if (isValid()) {
            this.mCallback.onError(ex);
        }
    }

    private boolean isValid() {
        switch (this.mScopeType) {
            case 0:
                Activity activity = this.mWeakActivity.get();
                return Validate.isActivityAlive(activity);
            case 1:
                Fragment fragment = this.mWeakFragment.get();
                return fragment != null && fragment.isAdded() && Validate.isActivityAlive(fragment.getActivity());
            default:
                return false;
        }
    }
}
