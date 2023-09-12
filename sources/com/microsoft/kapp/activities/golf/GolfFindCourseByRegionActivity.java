package com.microsoft.kapp.activities.golf;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.fragments.golf.GolfFindByRegionResultsFragment;
import com.microsoft.kapp.fragments.golf.GolfSelectRegionFragment;
import com.microsoft.kapp.fragments.golf.GolfSelectStateFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.golf.GolfRegion;
import com.microsoft.kapp.models.golf.GolfRegionResponse;
import com.microsoft.kapp.models.golf.GolfStateResponse;
import com.microsoft.kapp.services.golf.GolfFindCourseByRegionListener;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class GolfFindCourseByRegionActivity extends GolfFindCourseBaseActivity implements GolfFindCourseByRegionListener {
    private static final int EMPTY_VALUE = Integer.MIN_VALUE;
    private static final String GOLF_DISCOVERY_REGION = "GolfDiscoveryRegion";
    private static final String GOLF_DISCOVERY_STATE = "GolfDiscoveryCountry";
    private static final String GOLF_DISCOVERY_STATE_NAME = "GolfDiscoveryState";
    private static final String GOLF_DISCOVERY_STATE_NUMBER_OF_COURSES = "RegionNumberOfCourses";
    private static final String TAG = GolfFindCourseByRegionActivity.class.getSimpleName();
    private View mBackButton;
    protected TextView mHeaderTileText;
    private int mSelectedRegion;
    private int mSelectedState;
    private String mSelectedStateName;
    private int mSelectedStateNumberOfCourses;

    @Override // com.microsoft.kapp.services.golf.GolfFindCourseByRegionListener
    public void onStateSelected(GolfRegion state) {
        this.mSelectedState = state.getRegionID();
        this.mSelectedStateNumberOfCourses = state.getNumberOfCourses();
        this.mSelectedStateName = state.getRegionName();
        setState(1233);
        populateCourseListFragment();
    }

    @Override // com.microsoft.kapp.services.golf.GolfFindCourseByRegionListener
    public void onRegionSelected(GolfRegion region) {
        this.mSelectedRegion = region.getRegionID();
        this.mGolfService.getAvailableStates(this.mSelectedRegion, new ActivityScopedCallback<>(this, new Callback<GolfStateResponse>() { // from class: com.microsoft.kapp.activities.golf.GolfFindCourseByRegionActivity.1
            @Override // com.microsoft.kapp.Callback
            public void callback(GolfStateResponse result) {
                GolfFindCourseByRegionActivity.this.populateStateListFragment(result);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception exception) {
                KLog.e(GolfFindCourseByRegionActivity.TAG, "Error Fetching states within a region!", exception);
                GolfFindCourseByRegionActivity.this.setState(1235);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.golf.GolfFindCourseBaseActivity, com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport
    public void onCreate(ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreSavedData(savedInstanceState);
            setState(1234);
        }
        super.onCreate(container, savedInstanceState);
        this.mHeaderTileText = (TextView) ViewUtils.getValidView(getWindow().getDecorView(), R.id.header_text, TextView.class);
        this.mBackButton = (View) ViewUtils.getValidView(getWindow().getDecorView(), R.id.back, View.class);
        this.mBackButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.golf.GolfFindCourseByRegionActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FragmentManager frgamentManager = GolfFindCourseByRegionActivity.this.getSupportFragmentManager();
                if (frgamentManager != null && !frgamentManager.popBackStackImmediate()) {
                    GolfFindCourseByRegionActivity.this.finish();
                }
            }
        });
        if (savedInstanceState == null) {
            fetchGolfCountries();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GOLF_DISCOVERY_STATE, this.mSelectedState);
        outState.putInt(GOLF_DISCOVERY_REGION, this.mSelectedRegion);
        outState.putInt(GOLF_DISCOVERY_STATE_NUMBER_OF_COURSES, this.mSelectedStateNumberOfCourses);
        outState.putString(GOLF_DISCOVERY_STATE_NAME, this.mSelectedStateName);
    }

    private void fetchGolfCountries() {
        setState(1233);
        this.mGolfService.getAvailableRegions(new ActivityScopedCallback<>(this, new Callback<GolfRegionResponse>() { // from class: com.microsoft.kapp.activities.golf.GolfFindCourseByRegionActivity.3
            @Override // com.microsoft.kapp.Callback
            public void callback(GolfRegionResponse result) {
                if (result != null) {
                    GolfFindCourseByRegionActivity.this.populateCountryListFragment(result);
                    GolfFindCourseByRegionActivity.this.setState(1234);
                    return;
                }
                GolfFindCourseByRegionActivity.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception exception) {
                KLog.e(GolfFindCourseByRegionActivity.TAG, "Error Fetching golf countries!", exception);
                GolfFindCourseByRegionActivity.this.setState(1235);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populateCountryListFragment(GolfRegionResponse result) {
        GolfSelectRegionFragment fragment = new GolfSelectRegionFragment();
        fragment.setAvailableCountries(result);
        navigateToFragment(fragment, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populateStateListFragment(GolfStateResponse result) {
        GolfSelectStateFragment fragment = new GolfSelectStateFragment();
        fragment.setAvailableStates(result);
        navigateToFragment(fragment, true, true);
    }

    private void populateCourseListFragment() {
        GolfFindByRegionResultsFragment fragment = GolfFindByRegionResultsFragment.newInstance();
        fragment.setRegions(this.mSelectedRegion, this.mSelectedState, this.mSelectedStateName, this.mSelectedStateNumberOfCourses);
        navigateToFragment(fragment, true, true);
    }

    private void restoreSavedData(Bundle savedData) {
        if (savedData != null) {
            this.mSelectedState = savedData.getInt(GOLF_DISCOVERY_STATE);
            this.mSelectedRegion = savedData.getInt(GOLF_DISCOVERY_REGION);
            this.mSelectedStateNumberOfCourses = savedData.getInt(GOLF_DISCOVERY_STATE_NUMBER_OF_COURSES);
            this.mSelectedStateName = savedData.getString(GOLF_DISCOVERY_STATE_NAME);
        }
    }

    @Override // com.microsoft.kapp.services.golf.GolfFindCourseByRegionListener
    public void setPageTitle(String title) {
        this.mHeaderTileText.setText(title);
    }
}
