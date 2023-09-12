package com.microsoft.kapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.LevelTwoBaseActivity;
import com.microsoft.kapp.activities.LevelTwoPagesActivity;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
/* loaded from: classes.dex */
public class ActivityUtils {
    public static final String TAG = ActivityUtils.class.getSimpleName();

    public static <T> T getAndValidateView(Activity activity, int id, Class<T> viewClass) {
        Validate.notNull(activity, "activity");
        Validate.notNull(viewClass, "viewClass");
        View targetView = activity.findViewById(id);
        if (targetView == null) {
            String message = String.format("The activity does not contain a child view with the id %d", Integer.valueOf(id));
            throw new IllegalStateException(message);
        } else if (!viewClass.isInstance(targetView)) {
            String message2 = String.format("The child view is not an instance of specified type '%s'", viewClass.getSimpleName());
            throw new IllegalStateException(message2);
        } else {
            return viewClass.cast(targetView);
        }
    }

    public static <T extends Fragment> void launchDetailFragment(Context context, Class<T> fragmentClass, Bundle fragmentParameters, boolean isNewTask) {
        Intent intent = new Intent(context, LevelTwoPagesActivity.class);
        intent.putExtra(Constants.KEY_FRAGMENT_CLASS, fragmentClass.getName());
        intent.putExtra(Constants.KEY_FRAGMENT_PARAMS, fragmentParameters);
        if (isNewTask) {
            intent.addFlags(268435456);
        }
        context.startActivity(intent);
    }

    public static <T extends Fragment> void launchDetailFragment(Context context, Class<T> fragmentClass, Bundle fragmentParameters) {
        Intent intent = new Intent(context, LevelTwoPagesActivity.class);
        intent.putExtra(Constants.KEY_FRAGMENT_CLASS, fragmentClass.getName());
        intent.putExtra(Constants.KEY_FRAGMENT_PARAMS, fragmentParameters);
        context.startActivity(intent);
    }

    public static <T extends Fragment> void launchLevelTwoActivity(Context context, Class<T> fragmentClass, Bundle fragmentParameters) {
        Intent intent = new Intent(context, LevelTwoBaseActivity.class);
        intent.putExtra(Constants.KEY_FRAGMENT_CLASS, fragmentClass.getName());
        intent.putExtra(Constants.KEY_FRAGMENT_PARAMS, fragmentParameters);
        context.startActivity(intent);
    }

    public static <T extends Fragment> void launchLevelTwoActivityForResult(Context context, Class<T> fragmentClass, Bundle fragmentParameters, Fragment fragmentRequestor, int request) {
        if (context != null && fragmentClass != null && fragmentRequestor != null) {
            Intent intent = new Intent(context, LevelTwoBaseActivity.class);
            intent.putExtra(Constants.KEY_FRAGMENT_CLASS, fragmentClass.getName());
            intent.putExtra(Constants.KEY_FRAGMENT_PARAMS, fragmentParameters);
            intent.putExtra(Constants.KEY_FRAGMENT_CLASS, fragmentClass.getName());
            try {
                fragmentRequestor.startActivityForResult(intent, request);
            } catch (Exception ex) {
                KLog.e(TAG, "Starting a new Activity was aborted: %s", ex);
            }
        }
    }

    public static <T extends Fragment> void launchDetailFragmentForResult(Context context, Fragment fragmentRequestor, Class<T> fragmentClass, Bundle fragmentParameters) {
        Intent intent = new Intent(context, LevelTwoPagesActivity.class);
        intent.putExtra(Constants.KEY_FRAGMENT_CLASS, fragmentClass.getName());
        intent.putExtra(Constants.KEY_FRAGMENT_PARAMS, fragmentParameters);
        Fragment mainFragment = fragmentRequestor.getParentFragment();
        if (mainFragment != null) {
            mainFragment.startActivityForResult(intent, 100);
        }
    }

    public static <T extends Fragment> void launchFragment(Context context, Class<T> fragmentClass, Bundle fragmentParameters) {
        Intent intent = new Intent(context, LevelTwoPagesActivity.class);
        intent.putExtra(Constants.KEY_FRAGMENT_CLASS, fragmentClass.getName());
        context.startActivity(intent);
    }

    public static FragmentTransaction getFragmentTransaction(FragmentActivity fActivity, boolean isAnimated) {
        FragmentTransaction fTransaction = fActivity.getSupportFragmentManager().beginTransaction();
        if (isAnimated) {
            fTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return fTransaction;
    }

    public static void performBackButton(Activity activity) {
        if (Validate.isActivityAlive(activity)) {
            activity.onBackPressed();
        }
    }
}
