package com.microsoft.kapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.HomeTileStateChangedListener;
import com.microsoft.kapp.adapters.ViewPagerItemAdapter;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.ViewPagerItemCollection;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.PagerTitleStrip;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@SuppressLint({"ValidFragment"})
/* loaded from: classes.dex */
public class MainContentFragment extends BaseFragment implements HomeTileStateChangedListener {
    private static final String TAG = MainContentFragment.class.getSimpleName();
    private List<WeakReference<BaseHomeTileFragment>> mFragmentRefreshList;
    private List<Pair<Integer, Class<? extends BaseFragment>>> mFragmentsData;
    private int mHeaderBackgroundColor;
    private int mInitialItem;
    private volatile boolean mIsHomeScreenAnimationCompleted;
    private volatile boolean mIsL2View;
    private ArrayList<String> mListClassNames;
    private ArrayList<Integer> mListInt;
    private PagerAdapter mPagerAdapter;
    private PagerTitleStrip mPagerTitleStrip;
    private String mUserEventId;
    private ViewPager mViewPager;
    private ViewPagerItemCollection mViewPagerItemCollection;
    private Object synchronizedObj;

    public MainContentFragment() {
        this.mInitialItem = -1;
        this.synchronizedObj = new Object();
        this.mIsHomeScreenAnimationCompleted = false;
    }

    public MainContentFragment(String eventId, List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData, int headerBackgroundColor, boolean isL2View, boolean isHomeScreenAnimationCompleted) {
        this.mInitialItem = -1;
        this.synchronizedObj = new Object();
        this.mIsHomeScreenAnimationCompleted = false;
        this.mUserEventId = eventId;
        this.mIsL2View = isL2View;
        this.mIsHomeScreenAnimationCompleted = isHomeScreenAnimationCompleted;
        init(fragmentsData, headerBackgroundColor);
    }

    public MainContentFragment(String eventId, List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData, int headerBackgroundColor, boolean waitForAnimationThenLoad) {
        this.mInitialItem = -1;
        this.synchronizedObj = new Object();
        this.mIsHomeScreenAnimationCompleted = false;
        this.mUserEventId = eventId;
        this.mIsHomeScreenAnimationCompleted = waitForAnimationThenLoad ? false : true;
        init(fragmentsData, headerBackgroundColor);
    }

    public MainContentFragment(String eventId, List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData, int headerBackgroundColor) {
        this.mInitialItem = -1;
        this.synchronizedObj = new Object();
        this.mIsHomeScreenAnimationCompleted = false;
        this.mUserEventId = eventId;
        init(fragmentsData, headerBackgroundColor);
    }

    private void init(List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData, int headerBackgroundColor) {
        ViewPagerItemCollection collection = new ViewPagerItemCollection();
        this.mHeaderBackgroundColor = headerBackgroundColor;
        this.mFragmentsData = fragmentsData;
        if (fragmentsData != null) {
            for (Pair<Integer, Class<? extends BaseFragment>> fragmentData : fragmentsData) {
                collection.add(((Integer) fragmentData.first).intValue(), (Class) fragmentData.second);
            }
            this.mViewPagerItemCollection = collection;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mIsHomeScreenAnimationCompleted = true;
            this.mHeaderBackgroundColor = savedInstanceState.getInt("mHeaderBackgroundColor");
            this.mListInt = savedInstanceState.getIntegerArrayList("mListInt");
            this.mListClassNames = savedInstanceState.getStringArrayList("mListClassNames");
            if (this.mListInt != null && this.mListClassNames != null) {
                int size = this.mListClassNames.size();
                this.mViewPagerItemCollection = new ViewPagerItemCollection();
                this.mFragmentsData = new ArrayList();
                for (int i = 0; i < size; i++) {
                    try {
                        Class<?> cls = Class.forName(this.mListClassNames.get(i));
                        this.mFragmentsData.add(new Pair<>(this.mListInt.get(i), cls));
                        this.mViewPagerItemCollection.add(this.mListInt.get(i).intValue(), cls);
                    } catch (ClassNotFoundException e) {
                        KLog.e(TAG, "Class Not Found:" + this.mListClassNames.get(i), e);
                    }
                }
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_content, container, false);
        this.mViewPager = (ViewPager) ViewUtils.getValidView(view, R.id.pager, ViewPager.class);
        this.mPagerTitleStrip = (PagerTitleStrip) ViewUtils.getValidView(view, R.id.pager_title_strip, PagerTitleStrip.class);
        HomeData homeData = HomeData.getInstance();
        this.mPagerAdapter = new ViewPagerItemAdapter(getActivity(), getChildFragmentManager(), this.mViewPagerItemCollection, homeData, this.mUserEventId, this.mIsL2View);
        int initialPosition = getViewPagerInitialFragment();
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setCurrentItem(initialPosition);
        this.mPagerTitleStrip.setViewPager(this.mViewPager, initialPosition);
        this.mPagerTitleStrip.setBackgroundColor(this.mHeaderBackgroundColor);
        return view;
    }

    public ViewPagerItemCollection getViewPagerItemCollection() {
        return this.mViewPagerItemCollection;
    }

    public int getCurrentSubFragmentIndex() {
        if (this.mViewPager == null) {
            return -1;
        }
        return this.mViewPager.getCurrentItem();
    }

    public Class<? extends Fragment> getCurrentSubFragmentClass() {
        int currentIndex = getCurrentSubFragmentIndex();
        if (currentIndex < 0) {
            return null;
        }
        return this.mViewPagerItemCollection.get(getCurrentSubFragmentIndex()).getFragmentClass();
    }

    public int getCount() {
        return getViewPagerItemCollection().size();
    }

    public void setViewPagerInitialFragment(int index) {
        Validate.isTrue(index >= 0 && index < getViewPagerItemCollection().size(), "The requested position is outside the bounds of the available fragments.");
        this.mInitialItem = index;
    }

    public int getViewPagerInitialFragment() {
        if (this.mPagerAdapter != null && this.mInitialItem == -1) {
            return ((ViewPagerItemAdapter) ViewPagerItemAdapter.class.cast(this.mPagerAdapter)).getInitialPosition();
        }
        if (this.mInitialItem == -1) {
            this.mInitialItem = 0;
        }
        return this.mInitialItem;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> nestedFragmentsList = getChildFragmentManager().getFragments();
        if (nestedFragmentsList != null) {
            for (Fragment nestedFragment : nestedFragmentsList) {
                if (nestedFragment != null) {
                    nestedFragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mHeaderBackgroundColor", this.mHeaderBackgroundColor);
        if (this.mFragmentsData != null) {
            int size = this.mFragmentsData.size();
            this.mListInt = new ArrayList<>(size);
            this.mListClassNames = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                this.mListInt.add(this.mFragmentsData.get(i).first);
                this.mListClassNames.add(((Class) this.mFragmentsData.get(i).second).getName());
            }
        }
        outState.putIntegerArrayList("mListInt", this.mListInt);
        outState.putStringArrayList("mListClassNames", this.mListClassNames);
    }

    @Override // com.microsoft.kapp.activities.HomeTileStateChangedListener
    public void onPagerTitleStripColorUpdated(int newColor) {
        if (this.mPagerTitleStrip != null) {
            this.mPagerTitleStrip.setBackgroundColor(newColor);
            this.mHeaderBackgroundColor = newColor;
        }
    }

    public void onHomeTileAnimationComplete() {
        synchronized (this.synchronizedObj) {
            this.mIsHomeScreenAnimationCompleted = true;
            notifyRefreshFragments();
            this.mFragmentRefreshList = null;
        }
    }

    public boolean shouldRefresh(BaseHomeTileFragment fragment) {
        synchronized (this.synchronizedObj) {
            if (this.mIsHomeScreenAnimationCompleted) {
                return true;
            }
            removeFromFragmentsToRefresh(fragment);
            addFragmentsToRefresh(fragment);
            return false;
        }
    }

    public void notifyRefreshFragments() {
        BaseHomeTileFragment listener;
        if (this.mFragmentRefreshList != null) {
            for (WeakReference<BaseHomeTileFragment> listenerWeakRef : this.mFragmentRefreshList) {
                if (listenerWeakRef != null && (listener = listenerWeakRef.get()) != null) {
                    listener.fetchData();
                }
            }
        }
    }

    private void addFragmentsToRefresh(BaseHomeTileFragment listener) {
        if (this.mFragmentRefreshList == null) {
            this.mFragmentRefreshList = new ArrayList();
        }
        this.mFragmentRefreshList.add(new WeakReference<>(listener));
    }

    private void removeFromFragmentsToRefresh(BaseHomeTileFragment listener) {
        if (this.mFragmentRefreshList != null) {
            Iterator<WeakReference<BaseHomeTileFragment>> it = this.mFragmentRefreshList.iterator();
            while (it.hasNext()) {
                WeakReference<BaseHomeTileFragment> currentHomeTileListenerWeakReference = it.next();
                if (currentHomeTileListenerWeakReference != null && currentHomeTileListenerWeakReference.get() == listener) {
                    it.remove();
                }
            }
        }
    }
}
