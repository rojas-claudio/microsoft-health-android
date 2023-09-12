package com.microsoft.kapp.models;

import android.support.v4.app.Fragment;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class ViewPagerFragmentMetadata {
    private final Class<? extends Fragment> mFragmentClass;
    private final int mIconResourceId;
    private final int mTitleResourceId;

    public ViewPagerFragmentMetadata(int titleResourceId, int iconResourceId, Class<? extends Fragment> fragmentClass) {
        Validate.notNull(fragmentClass, Constants.KEY_FRAGMENT_CLASS);
        this.mTitleResourceId = titleResourceId;
        this.mIconResourceId = iconResourceId;
        this.mFragmentClass = fragmentClass;
    }

    public int getTitleResourceId() {
        return this.mTitleResourceId;
    }

    public int getIconResourceId() {
        return this.mIconResourceId;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return this.mFragmentClass;
    }
}
