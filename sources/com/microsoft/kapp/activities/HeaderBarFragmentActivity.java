package com.microsoft.kapp.activities;

import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.widgets.HeaderBar;
import com.microsoft.kapp.widgets.HeaderBarWrapper;
/* loaded from: classes.dex */
public abstract class HeaderBarFragmentActivity extends BaseFragmentActivity {
    private HeaderBarWrapper mHeaderBarWrapper;

    public HeaderBar getHeaderBar() {
        return getHeaderBarWrapper().getHeaderBar();
    }

    @Override // android.app.Activity
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getHeaderBarWrapper().addContentView(view, params);
    }

    @Override // android.app.Activity
    public void setContentView(int layoutResId) {
        getHeaderBarWrapper().setContentView(layoutResId);
    }

    @Override // android.app.Activity
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getHeaderBarWrapper().setContentView(view, params);
    }

    @Override // android.app.Activity
    public void setContentView(View view) {
        getHeaderBarWrapper().setContentView(view);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        getHeaderBarWrapper().getHeaderBar().onResume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        getHeaderBarWrapper().getHeaderBar().onPause();
    }

    protected HeaderBarWrapper getHeaderBarWrapper() {
        if (this.mHeaderBarWrapper == null) {
            this.mHeaderBarWrapper = createHeaderBarWrapper();
        }
        return this.mHeaderBarWrapper;
    }

    protected HeaderBarWrapper createHeaderBarWrapper() {
        return new HeaderBarWrapper(this);
    }
}
