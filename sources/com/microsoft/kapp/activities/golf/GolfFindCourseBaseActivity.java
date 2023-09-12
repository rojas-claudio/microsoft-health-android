package com.microsoft.kapp.activities.golf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.golf.CourseFilters;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.utils.ActivityUtils;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class GolfFindCourseBaseActivity extends BaseFragmentActivityWithOfflineSupport {
    private static final String TAG = GolfFindCourseBaseActivity.class.getSimpleName();
    protected CourseFilters mFilters;
    @Inject
    GolfService mGolfService;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport
    public void onCreate(ViewGroup container, Bundle savedInstanceState) {
        setContentView(getLayoutId());
    }

    protected int getLayoutId() {
        return R.layout.plan_discovery_activity;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void navigateToFragment(Fragment fragment, boolean addtoBackSTack, boolean isAnimated) {
        setState(1234);
        FragmentTransaction ft = ActivityUtils.getFragmentTransaction(this, isAnimated);
        ft.replace(R.id.main_content, fragment);
        if (addtoBackSTack && ft.isAddToBackStackAllowed()) {
            ft.addToBackStack(null);
        }
        try {
            ft.commit();
        } catch (IllegalStateException ex) {
            KLog.w(TAG, "Error during fragment transaction", ex);
        }
    }
}
