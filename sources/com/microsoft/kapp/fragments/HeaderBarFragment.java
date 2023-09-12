package com.microsoft.kapp.fragments;

import android.app.Activity;
import com.microsoft.kapp.activities.HeaderBarFragmentActivity;
/* loaded from: classes.dex */
public abstract class HeaderBarFragment extends BaseFragment {
    private HeaderBarFragmentAdapter mAdapter = new HeaderBarFragmentAdapter();

    protected HeaderBarFragment() {
    }

    public HeaderBarFragmentActivity getHeaderBarActivity() {
        return this.mAdapter.getHeaderBarActivity();
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mAdapter.onAttach(activity);
    }

    @Override // android.support.v4.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mAdapter.onDetach();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mAdapter.onResume();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mAdapter.onPause();
    }
}
