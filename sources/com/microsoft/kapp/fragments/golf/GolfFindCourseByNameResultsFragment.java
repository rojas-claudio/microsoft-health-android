package com.microsoft.kapp.fragments.golf;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.golf.GolfSearchResultsModel;
import com.microsoft.kapp.services.LocationService;
import com.microsoft.kapp.services.golf.CourseFilters;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.models.GolfCourseSearchResults;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GolfFindCourseByNameResultsFragment extends GolfSearchResultsFragment {
    private static final String TAG = GolfFindCourseByNameResultsFragment.class.getSimpleName();
    private View mBackButton;
    @Inject
    Context mContext;
    private CourseFilters mCourseFilters;
    private EditText mCourseNameSearchBox;
    private TextView mFindIcon;
    private boolean mHasMoreData = true;
    private TextView mHeaderTileText;
    @Inject
    LocationService mLocationService;
    protected TextView mNoResultsSuggestionLink;
    private int mPage;
    private String mQuery;

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment, com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateChildView(inflater, container, savedInstanceState);
        this.mHeaderTileText = (TextView) ViewUtils.getValidView(view, R.id.search_name, TextView.class);
        this.mBackButton = (View) ViewUtils.getValidView(view, R.id.clear_search, View.class);
        this.mHeaderTileText.setVisibility(0);
        this.mHeaderTileText.setText(String.format(getResources().getString(R.string.golf_search_page_header), this.mQuery));
        this.mCourseNameSearchBox = (EditText) ViewUtils.getValidView(view, R.id.search_box, EditText.class);
        this.mFindIcon = (TextView) ViewUtils.getValidView(view, R.id.search_icon, TextView.class);
        this.mNoResultsSuggestionLink = (TextView) ViewUtils.getValidView(view, R.id.find_a_course_link, TextView.class);
        this.mNoResultsSuggestionLink.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfFindCourseByNameResultsFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GolfFindCourseByNameResultsFragment.this.suggestACourse();
            }
        });
        this.mFindIcon.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfFindCourseByNameResultsFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GolfFindCourseByNameResultsFragment.this.getActivity().onBackPressed();
            }
        });
        this.mCourseNameSearchBox.setVisibility(8);
        this.mBackButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfFindCourseByNameResultsFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GolfFindCourseByNameResultsFragment.this.getActivity().onBackPressed();
            }
        });
        return view;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    protected void getData(final int page, CourseFilters coursefilters, final Callback<GolfSearchResultsModel> callback) {
        Validate.notNull(this.mQuery, "mQuery");
        this.mGolfService.findCoursesByName(this.mQuery, coursefilters, page, 10, new ActivityScopedCallback<>(getActivity(), new Callback<GolfCourseSearchResults>() { // from class: com.microsoft.kapp.fragments.golf.GolfFindCourseByNameResultsFragment.4
            @Override // com.microsoft.kapp.Callback
            public void callback(GolfCourseSearchResults result) {
                if (result != null && result.getFacilities() != null) {
                    GolfFindCourseByNameResultsFragment.this.mHasMoreData = result.getFacilities().size() >= 10;
                    GolfFindCourseByNameResultsFragment.this.mNoResultsSuggestionLink.setVisibility((result.getFacilities().size() == 0 && page == 1) ? 0 : 4);
                }
                GolfSearchResultsModel resultsModel = new GolfSearchResultsModel();
                resultsModel.addSearchResults(result, false);
                callback.callback(resultsModel);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                GolfFindCourseByNameResultsFragment.this.setState(1235);
                callback.onError(ex);
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    protected int getLayoutId() {
        return R.layout.golf_find_by_name_results_fragment;
    }

    public void setSearchTerms(String searchTerms) {
        this.mQuery = searchTerms;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public boolean isFilterEnabled() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public boolean hasMoreData() {
        return this.mHasMoreData;
    }

    public int getPageHeaderTextResId() {
        return R.string.cancel;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public boolean shouldshowResultCount() {
        return false;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public void clearData() {
        this.mHasMoreData = true;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public String getReferralName() {
        return TelemetryConstants.PageViews.Referrers.GOLF_SEARCH_BY_NAME;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void suggestACourse() {
        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(Constants.TMAG_FEEDBACK_URL));
        startActivity(viewIntent);
    }
}
