package com.microsoft.kapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import com.microsoft.kapp.fragments.FragmentFactory;
/* loaded from: classes.dex */
public class LoopingPagerAdapter extends FragmentPagerAdapter {
    private FragmentFactory mFragmentFactory;

    public LoopingPagerAdapter(FragmentManager fm, FragmentFactory fragmentFactory) {
        super(fm);
        this.mFragmentFactory = fragmentFactory;
    }

    @Override // android.support.v4.app.FragmentPagerAdapter
    public Fragment getItem(int position) {
        Fragment fragment = this.mFragmentFactory.getFragment(position);
        return fragment;
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public int getItemCount() {
        return this.mFragmentFactory.getCount();
    }

    private int getFragmentPosition(int position) {
        return position % getItemCount();
    }

    @Override // android.support.v4.app.FragmentPagerAdapter, android.support.v4.view.PagerAdapter
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, getFragmentPosition(position));
    }

    @Override // android.support.v4.app.FragmentPagerAdapter, android.support.v4.view.PagerAdapter
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, getFragmentPosition(position), object);
    }

    @Override // android.support.v4.view.PagerAdapter
    public CharSequence getPageTitle(int position) {
        return this.mFragmentFactory.getFragmentTitle(getFragmentPosition(position));
    }

    @Override // android.support.v4.app.FragmentPagerAdapter, android.support.v4.view.PagerAdapter
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, getFragmentPosition(position), object);
    }
}
