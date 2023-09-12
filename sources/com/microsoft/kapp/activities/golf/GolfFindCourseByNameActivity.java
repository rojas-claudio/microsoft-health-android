package com.microsoft.kapp.activities.golf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BaseFragmentActivity;
import com.microsoft.kapp.fragments.golf.GolfFindCourseByNameFragment;
import com.microsoft.kapp.fragments.golf.GolfFindCourseByNameResultsFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.ActivityUtils;
/* loaded from: classes.dex */
public class GolfFindCourseByNameActivity extends BaseFragmentActivity implements GolfFindCourseByNameFragment.GolfFindCourseByNameListener {
    private static final String GOLF_SEARCH_TERMS = "GolfSearchTerms";
    private static final String TAG = GolfFindCourseByNameActivity.class.getSimpleName();
    private String mSearchTerms;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_two_base_activity);
        if (savedInstanceState != null) {
            this.mSearchTerms = savedInstanceState.getString(GOLF_SEARCH_TERMS, null);
        }
        if (TextUtils.isEmpty(this.mSearchTerms)) {
            loadSearchFragment();
        } else {
            loadResultsFragment();
        }
    }

    private void loadSearchFragment() {
        GolfFindCourseByNameFragment fragment = new GolfFindCourseByNameFragment();
        fragment.setListener(this);
        navigateToFragment(fragment, false, false);
    }

    private void loadResultsFragment() {
        GolfFindCourseByNameResultsFragment fragment = new GolfFindCourseByNameResultsFragment();
        fragment.setSearchTerms(this.mSearchTerms);
        navigateToFragment(fragment, true, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GOLF_SEARCH_TERMS, this.mSearchTerms);
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfFindCourseByNameFragment.GolfFindCourseByNameListener
    public void setSearchTerms(String searchTerms) {
        this.mSearchTerms = searchTerms;
        loadResultsFragment();
    }

    protected void navigateToFragment(Fragment fragment, boolean addtoBackStack, boolean isAnimated) {
        FragmentTransaction ft = ActivityUtils.getFragmentTransaction(this, isAnimated);
        ft.replace(R.id.main_content, fragment, fragment.getClass().getSimpleName());
        if (addtoBackStack && ft.isAddToBackStackAllowed()) {
            ft.addToBackStack(null);
        }
        try {
            ft.commit();
        } catch (IllegalStateException ex) {
            KLog.w(TAG, "Error during fragment transaction", ex);
        }
    }
}
