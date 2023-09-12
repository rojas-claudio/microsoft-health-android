package com.microsoft.kapp.models;

import android.support.v4.app.Fragment;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class ViewPagerItem {
    private final Class<? extends Fragment> mFragmentClass;
    private final int mTitleResourceId;

    public ViewPagerItem(int titleResourceId, Class<? extends Fragment> fragmentClass) {
        Validate.notNull(fragmentClass, Constants.KEY_FRAGMENT_CLASS);
        this.mTitleResourceId = titleResourceId;
        this.mFragmentClass = fragmentClass;
    }

    public int getTitleResourceId() {
        return this.mTitleResourceId;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return this.mFragmentClass;
    }
}
