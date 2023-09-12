package com.microsoft.kapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.ViewPagerItemCollection;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.utils.Constants;
import java.lang.reflect.Method;
/* loaded from: classes.dex */
public class ViewPagerItemAdapter extends FragmentPagerAdapter implements TitlePagerAdapter {
    private static final String TAG = ViewPagerItemAdapter.class.getSimpleName();
    private final ViewPagerItemCollection mCollection;
    private final Context mContext;
    private final HomeData mHomeData;
    private int mInitialPosition;
    private boolean mIsL2View;
    private final String mUserEventId;

    public ViewPagerItemAdapter(Context context, FragmentManager fm, ViewPagerItemCollection collection, HomeData homeData, String userEventId, boolean isL2View) {
        super(fm);
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(collection, "collection");
        this.mUserEventId = userEventId;
        this.mContext = context;
        this.mCollection = collection;
        this.mHomeData = homeData;
        this.mIsL2View = isL2View;
        setInitialPosition(collection);
    }

    private void setInitialPosition(ViewPagerItemCollection collection) {
        try {
            this.mInitialPosition = collection.findIndexOfFragment(GuidedWorkoutNextFragment.class);
        } catch (IllegalStateException e) {
            this.mInitialPosition = 0;
        }
    }

    public int getInitialPosition() {
        return this.mInitialPosition;
    }

    @Override // android.support.v4.app.FragmentPagerAdapter
    public Fragment getItem(int position) {
        Validate.inRange(position, 0, this.mCollection.size(), "The requested position is outside the bounds of the available fragments.");
        Class<? extends Fragment> fragmentClass = this.mCollection.get(position).getFragmentClass();
        try {
            if (fragmentClass == GuidedWorkoutNextFragment.class) {
                HomeData homeData = HomeData.getInstance();
                return GuidedWorkoutNextFragment.newInstance(homeData.getNextGuidedWorkoutStepSchedule(), false, true, true);
            }
            try {
                Method method = fragmentClass.getDeclaredMethod("newInstance", String.class, Boolean.class);
                return (Fragment) BaseFragment.class.cast(method.invoke(null, this.mUserEventId, Boolean.valueOf(this.mIsL2View)));
            } catch (NoSuchMethodException e) {
                KLog.e(TAG, "Trouble invoking newInstance in ViewPagerItemAdapter");
                try {
                    return fragmentClass.getDeclaredConstructor(HomeData.class).newInstance(this.mHomeData);
                } catch (NoSuchMethodException e2) {
                    KLog.e(TAG, "Trouble invoking fragment in ViewPagerItemAdapter");
                    return fragmentClass.newInstance();
                }
            }
        } catch (Exception ex) {
            KLog.w(TAG, "Could not instantiate fragment for provided position.", ex);
            throw new RuntimeException("Could not instantiate fragment for provided position.", ex);
        }
        KLog.w(TAG, "Could not instantiate fragment for provided position.", ex);
        throw new RuntimeException("Could not instantiate fragment for provided position.", ex);
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        return this.mCollection.size();
    }

    @Override // android.support.v4.view.PagerAdapter
    public String getPageTitle(int position) {
        return this.mContext.getResources().getString(this.mCollection.get(position).getTitleResourceId());
    }
}
