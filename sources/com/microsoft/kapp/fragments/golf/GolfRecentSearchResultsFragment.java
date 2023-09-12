package com.microsoft.kapp.fragments.golf;

import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.models.golf.GolfSearchResultsModel;
import com.microsoft.kapp.services.golf.CourseFilters;
/* loaded from: classes.dex */
public class GolfRecentSearchResultsFragment extends GolfSearchResultsFragment {
    private boolean mHasMoreData = true;

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    protected void getData(int page, CourseFilters coursefilters, final Callback<GolfSearchResultsModel> callback) {
        this.mGolfService.getRecentCourses(new ActivityScopedCallback(getActivity(), new Callback<GolfSearchResultsModel>() { // from class: com.microsoft.kapp.fragments.golf.GolfRecentSearchResultsFragment.1
            @Override // com.microsoft.kapp.Callback
            public void callback(GolfSearchResultsModel result) {
                callback.callback(result);
                GolfRecentSearchResultsFragment.this.mHasMoreData = false;
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment, android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage("Fitness/Golf/FindCourse");
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public boolean isFilterEnabled() {
        return false;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public boolean hasMoreData() {
        return this.mHasMoreData;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public void clearData() {
        this.mHasMoreData = true;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public boolean shouldshowResultCount() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public String getReferralName() {
        return TelemetryConstants.PageViews.Referrers.GOLF_SEARCH_RECENT;
    }
}
