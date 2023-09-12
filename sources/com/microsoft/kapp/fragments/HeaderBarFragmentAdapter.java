package com.microsoft.kapp.fragments;

import android.app.Activity;
import com.microsoft.kapp.activities.HeaderBarFragmentActivity;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class HeaderBarFragmentAdapter {
    private HeaderBarFragmentActivity mActivity;

    public HeaderBarFragmentActivity getHeaderBarActivity() {
        return this.mActivity;
    }

    public void onAttach(Activity activity) {
        Validate.notNull(activity, "activity");
        String fragmentName = getClass().getSimpleName();
        Validate.isTrue(activity instanceof HeaderBarFragmentActivity, String.format("%s must be attached to a %s", fragmentName, HeaderBarFragmentActivity.class.getSimpleName()));
        this.mActivity = (HeaderBarFragmentActivity) activity;
    }

    public void onDetach() {
        this.mActivity = null;
    }

    public void onResume() {
        getHeaderBarActivity().getHeaderBar().onResume();
    }

    public void onPause() {
        getHeaderBarActivity().getHeaderBar().onPause();
    }
}
