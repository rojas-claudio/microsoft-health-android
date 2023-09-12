package com.microsoft.kapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.microsoft.kapp.fragments.FragmentFactory;
/* loaded from: classes.dex */
public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    private FragmentFactory mFragmentFactory;

    public FragmentViewPagerAdapter(FragmentManager fm, FragmentFactory fragmentFactory) {
        super(fm);
        this.mFragmentFactory = fragmentFactory;
    }

    @Override // android.support.v4.app.FragmentPagerAdapter
    public Fragment getItem(int position) {
        return this.mFragmentFactory.getFragment(position);
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        return this.mFragmentFactory.getCount();
    }

    @Override // android.support.v4.view.PagerAdapter
    public CharSequence getPageTitle(int position) {
        return this.mFragmentFactory.getFragmentTitle(position);
    }
}
