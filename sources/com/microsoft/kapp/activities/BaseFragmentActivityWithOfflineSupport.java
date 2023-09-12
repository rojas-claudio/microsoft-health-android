package com.microsoft.kapp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.widgets.Interstitial;
/* loaded from: classes.dex */
public abstract class BaseFragmentActivityWithOfflineSupport extends BaseFragmentActivity {
    protected static final int ERROR = 1235;
    protected static final int ERROR_BCK = 1236;
    protected static final int LOADED = 1234;
    protected static final int LOADING = 1233;
    private View mErrorMessage;
    private ViewGroup mFragmentContainer;
    private Interstitial mProgressBar;

    protected abstract void onCreate(ViewGroup viewGroup, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.fragment_base_support_offline);
        this.mFragmentContainer = (ViewGroup) ActivityUtils.getAndValidateView(this, R.id.fragment_content, ViewGroup.class);
        this.mErrorMessage = findViewById(R.id.offline_error_message);
        this.mProgressBar = (Interstitial) ActivityUtils.getAndValidateView(this, R.id.fragment_progress_bar, Interstitial.class);
        this.mProgressBar.setOnClickListener(null);
        onCreate(this.mFragmentContainer, savedInstanceState);
    }

    @Override // android.app.Activity
    public void setContentView(int layoutResID) {
        View childView = LayoutInflater.from(this).inflate(layoutResID, this.mFragmentContainer, false);
        setContentView(childView);
    }

    @Override // android.app.Activity
    public void setContentView(View childView) {
        setContentView(childView, null);
    }

    @Override // android.app.Activity
    public void setContentView(View childView, ViewGroup.LayoutParams params) {
        if (childView != null) {
            if (params == null) {
                this.mFragmentContainer.addView(childView);
            } else {
                this.mFragmentContainer.addView(childView, params);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setState(int status) {
        switch (status) {
            case LOADING /* 1233 */:
                this.mFragmentContainer.setVisibility(8);
                this.mProgressBar.setSlide(5000);
                return;
            case LOADED /* 1234 */:
                this.mFragmentContainer.setVisibility(0);
                this.mProgressBar.setSlide(Interstitial.SLIDE_GONE);
                return;
            case ERROR /* 1235 */:
                this.mProgressBar.setSlide(Interstitial.SLIDE_GONE);
                if (CommonUtils.isNetworkAvailable(this)) {
                    getDialogManager().showNetworkErrorDialog(this);
                    return;
                } else {
                    showOfflineMessage();
                    return;
                }
            case ERROR_BCK /* 1236 */:
                this.mProgressBar.setSlide(Interstitial.SLIDE_GONE);
                if (CommonUtils.isNetworkAvailable(this)) {
                    getDialogManager().showNetworkErrorDialogWithCallback(this, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            BaseFragmentActivityWithOfflineSupport.this.onBackPressed();
                        }
                    });
                    return;
                } else {
                    showOfflineMessage();
                    return;
                }
            default:
                return;
        }
    }

    private void showOfflineMessage() {
        this.mFragmentContainer.setVisibility(8);
        this.mProgressBar.setVisibility(8);
        this.mErrorMessage.setVisibility(0);
    }
}
