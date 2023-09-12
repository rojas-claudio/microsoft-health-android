package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.Interstitial;
/* loaded from: classes.dex */
public abstract class BaseFragmentWithOfflineSupport extends BaseFragment {
    protected static final int ERROR = 1235;
    protected static final int ERROR_NODATA = 1236;
    protected static final int LOADED = 1234;
    protected static final int LOADING = 1233;
    private int mCurrentState;
    private View mErrorMessage;
    private ViewGroup mFragmentContainer;
    private View mNoDataErrorMessage;
    private Interstitial mProgressBar;

    public abstract View onCreateChildView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle);

    @Override // android.support.v4.app.Fragment
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_support_offline, container, false);
        this.mFragmentContainer = (ViewGroup) ViewUtils.getValidView(rootView, R.id.fragment_content, ViewGroup.class);
        this.mErrorMessage = rootView.findViewById(R.id.offline_error_message);
        this.mNoDataErrorMessage = rootView.findViewById(R.id.no_data_error_message);
        this.mProgressBar = (Interstitial) ViewUtils.getValidView(rootView, R.id.fragment_progress_bar, Interstitial.class);
        View childView = onCreateChildView(inflater, this.mFragmentContainer, savedInstanceState);
        if (childView != null) {
            this.mFragmentContainer.addView(childView);
        }
        this.mProgressBar.setOnClickListener(null);
        return rootView;
    }

    public final void setState(int status) {
        Activity activity = getActivity();
        if (isAdded()) {
            switch (status) {
                case LOADING /* 1233 */:
                    this.mFragmentContainer.setVisibility(8);
                    this.mProgressBar.setSlide(5000);
                    this.mCurrentState = LOADING;
                    return;
                case LOADED /* 1234 */:
                    if (this.mCurrentState != ERROR) {
                        this.mFragmentContainer.setVisibility(0);
                        this.mProgressBar.setSlide(Interstitial.SLIDE_GONE);
                        this.mCurrentState = LOADED;
                        return;
                    }
                    return;
                case ERROR /* 1235 */:
                    this.mProgressBar.setSlide(Interstitial.SLIDE_GONE);
                    if (CommonUtils.isNetworkAvailable(activity)) {
                        getDialogManager().showNetworkErrorDialog(activity);
                    } else {
                        showOfflineMessage();
                    }
                    this.mCurrentState = ERROR;
                    return;
                case ERROR_NODATA /* 1236 */:
                    this.mProgressBar.setSlide(Interstitial.SLIDE_GONE);
                    showNoDataMessage();
                    this.mCurrentState = ERROR;
                    return;
                default:
                    return;
            }
        }
    }

    protected final int getState() {
        return this.mCurrentState;
    }

    private void showOfflineMessage() {
        this.mFragmentContainer.setVisibility(8);
        this.mProgressBar.setVisibility(8);
        this.mNoDataErrorMessage.setVisibility(8);
        this.mErrorMessage.setVisibility(0);
    }

    private void showNoDataMessage() {
        this.mFragmentContainer.setVisibility(8);
        this.mProgressBar.setVisibility(8);
        this.mErrorMessage.setVisibility(8);
        this.mNoDataErrorMessage.setVisibility(0);
    }

    public void setBackground(int resId) {
        this.mProgressBar.setBackgroundResource(resId);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment
    protected int getTopMenuDividerVisibility() {
        return 8;
    }
}
