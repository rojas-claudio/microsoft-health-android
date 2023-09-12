package com.microsoft.kapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.fragments.FilterFragment;
import com.microsoft.kapp.fragments.FilterSelectionFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.LoadStatus;
import com.microsoft.kapp.services.golf.CourseFilters;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class GolfCourseFilterActivity extends BaseFragmentActivity implements FilterActivityInterface {
    private static final String TAG = GolfCourseFilterActivity.class.getSimpleName();
    private HashMap<String, List<String>> mAllFilterEntries;
    private CourseFilters mCourseFilters;
    private String mCurrentFilterSelection;
    private HashMap<String, List<String>> mFilterCriteria;
    private TextView mLoadError;
    private FrameLayout mMainContent;
    private ProgressBar mPageProgressBar;

    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreSavedData(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_filter_page);
        View view = getWindow().getDecorView();
        this.mPageProgressBar = (ProgressBar) ViewUtils.getValidView(view, R.id.load_progress, ProgressBar.class);
        this.mLoadError = (TextView) ViewUtils.getValidView(view, R.id.load_error, TextView.class);
        this.mMainContent = (FrameLayout) ViewUtils.getValidView(view, R.id.main_content, FrameLayout.class);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            this.mCourseFilters = (CourseFilters) extras.getParcelable(Constants.KEY_GOLF_COURSE_FILTER_SELECTION);
            this.mFilterCriteria = this.mCourseFilters.getSelectedCriteriaMap();
            this.mAllFilterEntries = this.mCourseFilters.getFullCriteriaMap();
            if (this.mAllFilterEntries == null) {
                fetchGolfCourseFilterEntries();
            }
        } else if (this.mAllFilterEntries == null) {
            fetchGolfCourseFilterEntries();
        }
        startFragment(FilterFragment.newInstance(getString(R.string.golf_filter_title_workout)), R.id.main_content, false);
        updatePanelVisibility(LoadStatus.LOADED);
    }

    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }

    private void fetchGolfCourseFilterEntries() {
        updatePanelVisibility(LoadStatus.LOADING);
        populateFilterList();
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public Set<String> getAllFiltersDisplayNames() {
        if (this.mAllFilterEntries == null) {
            return null;
        }
        return this.mAllFilterEntries.keySet();
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public String getFilterName(String filterDisplayName) {
        return filterDisplayName;
    }

    public HashMap<String, List<String>> getFilterEntries() {
        return this.mAllFilterEntries;
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public List<String> getSingleFilterValuesList(String filterName) {
        if (this.mAllFilterEntries == null) {
            return null;
        }
        return this.mAllFilterEntries.get(filterName);
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public void startSelectionFragment(String filterDisplayName) {
        this.mCurrentFilterSelection = filterDisplayName;
        startFragment(new FilterSelectionFragment(), R.id.main_content, true);
    }

    private void startFragment(Fragment fragment, int container, boolean addtoBackSTack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(container, fragment);
        if (addtoBackSTack) {
            ft.addToBackStack(null);
        }
        try {
            ft.commit();
        } catch (IllegalStateException ex) {
            KLog.w(TAG, "Error during fragment transaction: %s", ex);
        }
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public String getCurrentFilterSelection() {
        return this.mCurrentFilterSelection;
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public List<String> getCurrentFilterValuesList() {
        if (this.mAllFilterEntries == null) {
            return null;
        }
        return this.mAllFilterEntries.get(this.mCurrentFilterSelection);
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public List<String> getFilterCriteriaList(String filterName) {
        if (this.mFilterCriteria == null) {
            return null;
        }
        return this.mFilterCriteria.get(filterName);
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public void setSelectedCriteria(List<String> selectedCriteria) {
        if (this.mFilterCriteria == null) {
            this.mFilterCriteria = new HashMap<>();
        }
        this.mFilterCriteria.put(this.mCurrentFilterSelection, selectedCriteria);
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public void clearAllFiltersSelection() {
        this.mFilterCriteria = new HashMap<>();
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public void goToMainFilterPage() {
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public void returnResultAndExit(int status) {
        if (status == -1) {
            Intent data = new Intent();
            this.mCourseFilters.setFilters(this.mFilterCriteria);
            data.putExtra(Constants.KEY_GOLF_COURSE_FILTER_SELECTION, this.mCourseFilters);
            setResult(-1, data);
        } else {
            setResult(status);
        }
        finish();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.KEY_GOLF_COURSE_FILTER_SELECTION, this.mCourseFilters);
    }

    private void restoreSavedData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.mCourseFilters = (CourseFilters) savedInstanceState.getParcelable(Constants.KEY_GOLF_COURSE_FILTER_SELECTION);
        }
    }

    private void populateFilterList() {
        if (this.mCourseFilters == null) {
            KLog.e(TAG, "The course filter passes was null");
            return;
        }
        this.mAllFilterEntries = new HashMap<>();
        String filter = CourseFilters.CourseTypeFilterString;
        List<String> filterValues = new ArrayList<>();
        filterValues.add(this.mCourseFilters.getCourseTypeString(CourseFilters.AvailableCourseType.PRIVATE));
        filterValues.add(this.mCourseFilters.getCourseTypeString(CourseFilters.AvailableCourseType.PUBLIC));
        this.mAllFilterEntries.put(filter, filterValues);
        String filter2 = CourseFilters.HoleFilterString;
        List<String> filterValues2 = new ArrayList<>();
        filterValues2.add(this.mCourseFilters.getHoleTypeString(CourseFilters.AvailableHoleType.HOLE_9));
        filterValues2.add(this.mCourseFilters.getHoleTypeString(CourseFilters.AvailableHoleType.HOLE_18));
        this.mAllFilterEntries.put(filter2, filterValues2);
    }

    private void updatePanelVisibility(LoadStatus status) {
        this.mPageProgressBar.setVisibility(8);
        this.mMainContent.setVisibility(8);
        this.mLoadError.setVisibility(8);
        switch (status) {
            case LOADING:
                this.mPageProgressBar.setVisibility(0);
                return;
            case LOADED:
                this.mMainContent.setVisibility(0);
                return;
            case ERROR:
            case NO_DATA:
                this.mLoadError.setVisibility(0);
                return;
            default:
                return;
        }
    }
}
