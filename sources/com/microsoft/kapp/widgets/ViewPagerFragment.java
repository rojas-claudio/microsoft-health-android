package com.microsoft.kapp.widgets;

import android.support.v4.app.Fragment;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.models.ViewPagerFragmentMetadataCollection;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public abstract class ViewPagerFragment extends BaseFragment {
    private int mInitialItem;

    public abstract Class<? extends Fragment> getCurrentSubFragmentClass();

    public abstract int getCurrentSubFragmentIndex();

    public abstract ViewPagerFragmentMetadataCollection getViewPagerFragmentMetadataCollection();

    public int getCount() {
        return getViewPagerFragmentMetadataCollection().size();
    }

    public void setViewPagerInitialFragment(int index) {
        Validate.isTrue(index >= 0 && index < getViewPagerFragmentMetadataCollection().size(), "The requested position is outside the bounds of the available fragments.", new Object[0]);
        this.mInitialItem = index;
    }

    public void setViewPagerInitialFragment(Class<? extends Fragment> fragmentClass) {
        setViewPagerInitialFragment(getViewPagerFragmentMetadataCollection().findIndexOfFragment(fragmentClass));
    }

    public int getViewPagerInitialFragment() {
        return this.mInitialItem;
    }
}
