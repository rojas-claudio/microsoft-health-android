package com.microsoft.kapp.fragments.golf;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.GolfSectionedListAdapterWithIndex;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.golf.GolfSearchResultItem;
import com.microsoft.kapp.models.golf.GolfSearchResultsModel;
import com.microsoft.kapp.services.LocationService;
import com.microsoft.kapp.services.golf.CourseFilters;
import com.microsoft.kapp.services.golf.GolfFindCourseByRegionListener;
import com.microsoft.kapp.views.astickyheader.AutoScrollListView;
import com.microsoft.krestsdk.models.GolfCourseSearchResults;
import java.util.ArrayList;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GolfFindByRegionResultsFragment extends GolfSearchResultsFragment {
    private static final String GOLF_REGION_ID = "GOLF_REGION_ID";
    private static final String GOLF_STATE_ID = "GOLF_STATE_ID";
    private static final String GOLF_STAT_COURSE_COUNT = "GOLF_STAT_COURSE_COUNT";
    private static final String GOLF_STAT_STATE_NAME = "GOLF_STAT_STATE_NAME";
    private static final String TAG = GolfFindByRegionResultsFragment.class.getSimpleName();
    private GolfSectionedListAdapterWithIndex mAdapter;
    @Inject
    Context mContext;
    private GolfCourseSearchResults mCourses;
    private GolfFindCourseByRegionListener mListener;
    @Inject
    LocationService mLocationService;
    private String mSelectedStateName;
    private int mRegionId = Integer.MIN_VALUE;
    private int mStateId = Integer.MIN_VALUE;
    private int mStateCourseCount = Integer.MIN_VALUE;

    public static final GolfFindByRegionResultsFragment newInstance() {
        GolfFindByRegionResultsFragment fragment = new GolfFindByRegionResultsFragment();
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment, com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateChildView(inflater, container, savedInstanceState);
        if (savedInstanceState != null) {
            this.mRegionId = savedInstanceState.getInt(GOLF_REGION_ID);
            this.mStateId = savedInstanceState.getInt(GOLF_STATE_ID);
            this.mStateCourseCount = savedInstanceState.getInt(GOLF_STAT_COURSE_COUNT);
            this.mSelectedStateName = savedInstanceState.getString(GOLF_STAT_STATE_NAME);
        }
        return view;
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof GolfFindCourseByRegionListener) {
            this.mListener = (GolfFindCourseByRegionListener) activity;
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mListener != null) {
            this.mListener.setPageTitle(this.mSelectedStateName);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GOLF_REGION_ID, this.mRegionId);
        outState.putInt(GOLF_STATE_ID, this.mStateId);
        outState.putInt(GOLF_STAT_COURSE_COUNT, this.mStateCourseCount);
        outState.putString(GOLF_STAT_STATE_NAME, this.mSelectedStateName);
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    protected void getData(int page, CourseFilters coursefilters, Callback<GolfSearchResultsModel> callback) {
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    protected int getLayoutId() {
        return R.layout.golf_search_by_region_fragment;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public boolean isFilterEnabled() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"DefaultLocale"})
    public void populateCourseList() {
        GolfSearchResultsModel result = new GolfSearchResultsModel();
        result.addAndSortResults(this.mCourses);
        ArrayList<GolfSectionedListAdapterWithIndex.Section> sections = new ArrayList<>();
        String previousKey = null;
        int i = 0;
        for (GolfSearchResultItem course : result.getItems()) {
            if (course != null && course.getName() != null && course.getName().length() != 0) {
                String key = String.valueOf(course.getName().charAt(0)).toUpperCase();
                if (!key.equals(previousKey)) {
                    sections.add(new GolfSectionedListAdapterWithIndex.Section(i, key));
                    previousKey = key;
                }
                i++;
            }
        }
        if (result.getItems() != null && result.getItems().size() > 0) {
            if (this.mAdapter == null) {
                this.mSearchAdapter = new GolfSearchResultsAdapter(getActivity(), this.mSettingsProvider, result.getItems());
                this.mAdapter = new GolfSectionedListAdapterWithIndex(getActivity(), this.mSearchAdapter, R.layout.adapter_golf_find_by_country_list_section_header, R.id.section_header);
                this.mAdapter.setSections(sections);
                this.mListView.setAdapter((ListAdapter) this.mAdapter);
            } else {
                this.mAdapter.setSections(sections);
                this.mSearchAdapter.clear();
                this.mSearchAdapter.addAll(result.getItems());
                this.mAdapter.getAdapter().notifyDataSetChanged();
                this.mAdapter.notifyDataSetChanged();
            }
            this.mResultText.setText(String.format(getActivity().getResources().getString(R.string.guided_workout_fitness_plan_result_header, Integer.valueOf(this.mSearchAdapter.getCount())), new Object[0]));
            this.mFilterIcon.setVisibility(0);
            this.mNoResultsText.setVisibility(4);
            ((AutoScrollListView) this.mListView).setIsScrollBarEnabled(false);
        } else {
            this.mResultText.setText(String.format(getActivity().getResources().getString(R.string.guided_workout_fitness_plan_result_header, 0), new Object[0]));
            this.mNoResultsText.setVisibility(0);
        }
        setState(1234);
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    protected void loadList() {
        setState(1233);
        ActivityScopedCallback<GolfCourseSearchResults> callback = new ActivityScopedCallback<>(this, new Callback<GolfCourseSearchResults>() { // from class: com.microsoft.kapp.fragments.golf.GolfFindByRegionResultsFragment.1
            @Override // com.microsoft.kapp.Callback
            public void callback(GolfCourseSearchResults result) {
                if (result != null) {
                    GolfFindByRegionResultsFragment.this.mCourses = result;
                    GolfFindByRegionResultsFragment.this.populateCourseList();
                    GolfFindByRegionResultsFragment.this.setState(1234);
                    return;
                }
                GolfFindByRegionResultsFragment.this.setState(1236);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception exception) {
                KLog.e(GolfFindByRegionResultsFragment.TAG, "Error Fetching courses for region!", exception);
                GolfFindByRegionResultsFragment.this.setState(1235);
            }
        });
        if (this.mStateId != Integer.MIN_VALUE) {
            this.mGolfService.getAvailableCoursesForState(this.mStateId, this.mStateCourseCount, this.mCourseFilters, callback);
        } else {
            this.mGolfService.getAvailableCoursesForRegion(this.mRegionId, this.mCourseFilters, callback);
        }
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public boolean hasMoreData() {
        return false;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public boolean shouldshowResultCount() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public void clearData() {
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public String getReferralName() {
        return TelemetryConstants.PageViews.Referrers.GOLF_SEARCH_REGION;
    }

    public void setCourses(GolfCourseSearchResults courses) {
        this.mCourses = courses;
    }

    public void setRegions(int selectedRegion, int selectedState, String selectedStateName, int stateCourseCount) {
        this.mRegionId = selectedRegion;
        this.mStateId = selectedState;
        this.mStateCourseCount = stateCourseCount;
        this.mSelectedStateName = selectedStateName;
    }
}
