package com.microsoft.kapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class FragmentFactory {
    private Class<?>[] mFragmentClasses;
    private String[] mTitles;

    public FragmentFactory(Class<?>[] fragmentClasses, String[] fragmentTitles) {
        if (fragmentClasses.length != fragmentTitles.length) {
            throw new IllegalArgumentException(String.format("The number of fragments must match the number of titles. Fragment count: %d - Title count: %d", Integer.valueOf(fragmentClasses.length), Integer.valueOf(fragmentTitles.length)));
        }
        this.mFragmentClasses = fragmentClasses;
        this.mTitles = fragmentTitles;
    }

    public Fragment getFragment(int position) {
        if (position < 0 || position >= this.mFragmentClasses.length) {
            throw new IllegalArgumentException("The requested position is outside the bounds of the available fragments.");
        }
        try {
            Fragment fragment = (Fragment) this.mFragmentClasses[position].newInstance();
            Bundle fragmentParams = new Bundle();
            fragmentParams.putInt(Constants.KEY_FRAGMENT_INDEX, position);
            fragment.setArguments(fragmentParams);
            return fragment;
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Could not instantiate fragment for provided position");
        } catch (InstantiationException e2) {
            throw new IllegalArgumentException("Could not instantiate fragment for provided position");
        }
    }

    public int getFragmentIndex(Class<?> fragmentClass) {
        for (int i = 0; i < this.mFragmentClasses.length; i++) {
            if (this.mFragmentClasses[i].equals(fragmentClass)) {
                return i;
            }
        }
        return -1;
    }

    public String getFragmentTitle(int position) {
        if (position < 0 || position >= this.mTitles.length) {
            throw new IllegalArgumentException("The requested position is outside the bounds of the available titles.");
        }
        return this.mTitles[position];
    }

    public int getCount() {
        return this.mFragmentClasses.length;
    }
}
