package com.microsoft.kapp.widgets;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class HeaderBarWrapper {
    private final Activity mActivity;
    private ViewGroup mContentView;
    private ViewGroup mDecorView;
    private HeaderBar mHeaderBar;

    public HeaderBarWrapper(Activity activity) {
        Validate.notNull(activity, "activity");
        this.mActivity = activity;
        this.mActivity.getWindow().requestFeature(1);
    }

    public HeaderBar getHeaderBar() {
        if (this.mHeaderBar == null) {
            initializeDecorView();
        }
        return this.mHeaderBar;
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        if (this.mContentView == null) {
            initializeDecorView();
        }
        this.mContentView.addView(view, params);
    }

    public void setContentView(int layoutResId) {
        onPreSetContentView();
        this.mActivity.getLayoutInflater().inflate(layoutResId, this.mContentView);
        onPostSetContentView();
    }

    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(-1, -1));
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        onPreSetContentView();
        this.mContentView.addView(view, params);
        onPostSetContentView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public View getDecorView() {
        return this.mDecorView;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initializeDecorView() {
        if (this.mDecorView == null) {
            this.mDecorView = (ViewGroup) this.mActivity.getWindow().getDecorView().findViewById(16908290);
        }
        if (this.mContentView == null) {
            this.mContentView = generateLayout();
            this.mHeaderBar = (HeaderBar) ViewUtils.getValidView(this.mDecorView, R.id.window_header_bar, HeaderBar.class);
        }
    }

    protected int getLayoutResourceId() {
        return R.layout.screen_header_bar;
    }

    private ViewGroup generateLayout() {
        View windowView = this.mActivity.getLayoutInflater().inflate(getLayoutResourceId(), (ViewGroup) null);
        this.mDecorView.addView(windowView, new ViewGroup.LayoutParams(-1, -1));
        ViewGroup contentView = (ViewGroup) ViewUtils.getValidView(this.mDecorView, R.id.window_content, ViewGroup.class);
        this.mDecorView.setId(-1);
        contentView.setId(16908290);
        return contentView;
    }

    private void onPreSetContentView() {
        if (this.mContentView == null) {
            initializeDecorView();
        } else {
            this.mContentView.removeAllViews();
        }
    }

    private void onPostSetContentView() {
        Window.Callback callback = this.mActivity.getWindow().getCallback();
        if (callback != null) {
            callback.onContentChanged();
        }
    }
}
