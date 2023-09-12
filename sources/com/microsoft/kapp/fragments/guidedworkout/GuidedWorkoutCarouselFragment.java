package com.microsoft.kapp.fragments.guidedworkout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.PageIndicatorView;
import com.microsoft.krestsdk.models.FeaturedWorkout;
import com.microsoft.krestsdk.models.WorkoutPlanBrowseDetails;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GuidedWorkoutCarouselFragment extends BaseFragment {
    public static final String DEFAULT_IMAGE = "default";
    private CarouselPagerAdapter mCarouselPagerAdapter;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private PageIndicatorView mPageIndicatorView;
    @Inject
    SettingsProvider mSettingsProvider;
    private ViewPager mViewPager;
    private final String TAG = getClass().getSimpleName();
    private UserProfileInfo.Gender mGender = UserProfileInfo.Gender.male;
    private int mAge = 25;
    private List<CarouselItem> mCarouselItems = new ArrayList();

    public static GuidedWorkoutCarouselFragment newInstance() {
        GuidedWorkoutCarouselFragment fragment = new GuidedWorkoutCarouselFragment();
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.guided_workout_carousel_fragment, container, false);
        this.mViewPager = (ViewPager) ViewUtils.getValidView(rootView, R.id.featured_workout_carousel, ViewPager.class);
        this.mCarouselPagerAdapter = new CarouselPagerAdapter(getChildFragmentManager());
        this.mViewPager.setAdapter(this.mCarouselPagerAdapter);
        this.mPageIndicatorView = (PageIndicatorView) ViewUtils.getValidView(rootView, R.id.guided_workout_page_indicator, PageIndicatorView.class);
        this.mPageIndicatorView.setPageCount(0);
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutCarouselFragment.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int position) {
                GuidedWorkoutCarouselFragment.this.mPageIndicatorView.setCurrentPage(position);
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        initializeAgeAndGender();
        loadFeaturedWorkouts();
        return rootView;
    }

    private void initializeAgeAndGender() {
        CargoUserProfile userProfile = this.mSettingsProvider.getUserProfile();
        if (userProfile == null || userProfile.getGender() == null || userProfile.getBirthdate() == null) {
            KLog.w(this.TAG, "Unable to retrieve userProfile locally. retrieving featured workouts will be aborted!");
            return;
        }
        this.mGender = userProfile.getGender();
        this.mAge = userProfile.getAge();
    }

    private void loadFeaturedWorkouts() {
        this.mGuidedWorkoutService.getFeaturedWorkouts(this.mAge, this.mGender.toString(), new ActivityScopedCallback(this, new Callback<List<FeaturedWorkout>>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutCarouselFragment.2
            @Override // com.microsoft.kapp.Callback
            public void callback(List<FeaturedWorkout> result) {
                GuidedWorkoutCarouselFragment.this.loadFeaturedWorkoutResources(result);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GuidedWorkoutCarouselFragment.this.TAG, "Error while fetching Featured Workouts!", ex);
                GuidedWorkoutCarouselFragment.this.loadFeaturedWorkoutResources(null);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadFeaturedWorkoutResources(List<FeaturedWorkout> result) {
        loadDefaultFeaturedWorkout();
        if (result == null) {
            this.mCarouselPagerAdapter.notifyDataSetChanged();
            this.mPageIndicatorView.setPageCount(this.mCarouselPagerAdapter.getCount());
            KLog.i(this.TAG, "No workouts sent, so only default loaded.");
            return;
        }
        for (FeaturedWorkout featuredWorkout : result) {
            WorkoutPlanBrowseDetails featuredWorkoutDetails = featuredWorkout.getWorkoutPlanBrowseDetails();
            if (featuredWorkoutDetails != null) {
                CarouselItem item = new CarouselItem();
                String imagePath = featuredWorkoutDetails.getPath();
                String featuredWorkoutPlanId = featuredWorkout.getWorkoutPlanId();
                String workoutPlanName = featuredWorkoutDetails.getName();
                item.setImageUrl(imagePath);
                item.setId(featuredWorkoutPlanId);
                item.setTitle(getResources().getString(R.string.guided_workout_featured_title));
                item.setSubTitle(workoutPlanName);
                this.mCarouselItems.add(item);
            }
        }
        this.mCarouselPagerAdapter.notifyDataSetChanged();
        this.mPageIndicatorView.setPageCount(this.mCarouselPagerAdapter.getCount());
    }

    private void loadDefaultFeaturedWorkout() {
        CarouselItem item = new CarouselItem();
        item.setImageUrl(DEFAULT_IMAGE);
        item.setTitle(getResources().getString(R.string.guided_workout_default_title));
        item.setSubTitle(getResources().getString(R.string.FeaturedWorkoutsDefaultPhrase_Subtitle));
        this.mCarouselItems.add(item);
    }

    /* loaded from: classes.dex */
    public class CarouselItem {
        private String mId;
        private String mImageUrl;
        private String mSubTitle;
        private String mTitle;

        public CarouselItem() {
        }

        public String getTitle() {
            return this.mTitle;
        }

        public void setTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public String getSubTitle() {
            return this.mSubTitle;
        }

        public void setSubTitle(String mSubTitle) {
            this.mSubTitle = mSubTitle;
        }

        public String getImageUrl() {
            return this.mImageUrl;
        }

        public void setImageUrl(String mImageUrl) {
            this.mImageUrl = mImageUrl;
        }

        public String getId() {
            return this.mId;
        }

        public void setId(String mId) {
            this.mId = mId;
        }
    }

    /* loaded from: classes.dex */
    public class CarouselPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> _mFragments;

        public CarouselPagerAdapter(FragmentManager fm) {
            super(fm);
            this._mFragments = new ArrayList<>();
        }

        @Override // android.support.v4.app.FragmentPagerAdapter
        public Fragment getItem(int index) {
            CarouselItem item = (CarouselItem) GuidedWorkoutCarouselFragment.this.mCarouselItems.get(index);
            Fragment fragment = GuidedWorkoutCarouselItemFragment.newInstance(index, item.getTitle(), item.getSubTitle(), item.getImageUrl(), item.getId());
            this._mFragments.add(fragment);
            return fragment;
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return GuidedWorkoutCarouselFragment.this.mCarouselItems.size();
        }
    }
}
