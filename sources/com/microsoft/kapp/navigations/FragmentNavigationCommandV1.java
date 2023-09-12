package com.microsoft.kapp.navigations;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class FragmentNavigationCommandV1<T extends Fragment> extends NavigationCommandV1 {
    private final boolean mAddToBackStack;
    private final Bundle mArguments;
    private final Class<T> mFragmentClass;

    /* loaded from: classes.dex */
    public interface FragmentNavigationListener {
        boolean isSafeToCommitFragmentTransactions();

        void navigateToFragment(Class cls, Bundle bundle, boolean z);

        void navigateToFragment(Class cls, Bundle bundle, boolean z, boolean z2);
    }

    public FragmentNavigationCommandV1(Context context, int titleResourceId, int iconResourceId, boolean addToBackStack, Class<T> fragmentClass) {
        this(context.getString(titleResourceId), iconResourceId, addToBackStack, fragmentClass);
    }

    public FragmentNavigationCommandV1(Context context, int titleResourceId, int iconResourceId, boolean addToBackStack, Class<T> fragmentClass, Bundle args) {
        this(context.getString(titleResourceId), iconResourceId, addToBackStack, (Class) fragmentClass, false, args);
    }

    public FragmentNavigationCommandV1(Context context, int titleResourceId, int iconResourceId, boolean addToBackStack, Class<T> fragmentClass, boolean indent) {
        this(context.getString(titleResourceId), iconResourceId, addToBackStack, fragmentClass, indent, (Bundle) null);
    }

    public FragmentNavigationCommandV1(Context context, int titleResourceId, int iconResourceId, boolean addToBackStack, Class<T> fragmentClass, boolean indent, boolean notification) {
        this(context.getString(titleResourceId), iconResourceId, addToBackStack, fragmentClass, indent, (Bundle) null, notification);
    }

    public FragmentNavigationCommandV1(String title, int iconResourceId, boolean addToBackStack, Class<T> fragmentClass) {
        this(title, iconResourceId, addToBackStack, (Class) fragmentClass, false, (Bundle) null);
    }

    public FragmentNavigationCommandV1(String title, int iconResourceId, boolean addToBackStack, Class<T> fragmentClass, boolean indent, Bundle args) {
        this(title, iconResourceId, addToBackStack, (Class) fragmentClass, indent, args, false);
    }

    public FragmentNavigationCommandV1(String title, int iconResourceId, boolean addToBackStack, Class<T> fragmentClass, boolean indent, Bundle args, boolean notification) {
        super(title, indent, notification, iconResourceId);
        Validate.notNull(fragmentClass, Constants.KEY_FRAGMENT_CLASS);
        this.mAddToBackStack = addToBackStack;
        this.mFragmentClass = fragmentClass;
        this.mArguments = args;
    }

    @Override // com.microsoft.kapp.navigations.NavigationCommandV1
    public void navigate(Activity parentActivity) {
        navigate(parentActivity, true);
    }

    public void navigate(Activity parentActivity, boolean shouldToggleSlidingMenu) {
        Validate.notNull(parentActivity, "parentActivity");
        FragmentNavigationListener listener = (FragmentNavigationListener) FragmentNavigationListener.class.cast(parentActivity);
        listener.navigateToFragment(this.mFragmentClass, this.mArguments, this.mAddToBackStack, shouldToggleSlidingMenu);
    }
}
