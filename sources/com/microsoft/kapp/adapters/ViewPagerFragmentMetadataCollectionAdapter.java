package com.microsoft.kapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.ViewPagerFragmentMetadataCollection;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class ViewPagerFragmentMetadataCollectionAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    private static final String TAG = ViewPagerFragmentMetadataCollectionAdapter.class.getSimpleName();
    private final ViewPagerFragmentMetadataCollection mCollection;
    private final Context mContext;

    public ViewPagerFragmentMetadataCollectionAdapter(Context context, FragmentManager fm, ViewPagerFragmentMetadataCollection collection) {
        super(fm);
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(collection, "collection");
        this.mContext = context;
        this.mCollection = collection;
    }

    @Override // android.support.v4.app.FragmentPagerAdapter
    public Fragment getItem(int position) {
        Validate.inRange(position, 0, this.mCollection.size(), "The requested position is outside the bounds of the available fragments.");
        Class<? extends Fragment> fragmentClass = this.mCollection.get(position).getFragmentClass();
        try {
            return fragmentClass.newInstance();
        } catch (IllegalAccessException ex) {
            KLog.e(TAG, "Could not instantiate fragment for provided position.", ex);
            throw new IllegalStateException("Could not instantiate fragment for provided position.", ex);
        } catch (InstantiationException ex2) {
            KLog.e(TAG, "Could not instantiate fragment for provided position.", ex2);
            throw new IllegalStateException("Could not instantiate fragment for provided position.", ex2);
        }
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        return this.mCollection.size();
    }

    @Override // com.microsoft.kapp.adapters.IconPagerAdapter
    public String getContentDescription(int position) {
        return this.mContext.getResources().getString(this.mCollection.get(position).getTitleResourceId());
    }

    @Override // android.support.v4.view.PagerAdapter
    public String getPageTitle(int position) {
        return this.mContext.getResources().getString(this.mCollection.get(position).getTitleResourceId());
    }

    @Override // com.microsoft.kapp.adapters.IconPagerAdapter
    public int getIconResourceId(int position) {
        return this.mCollection.get(position).getIconResourceId();
    }
}
