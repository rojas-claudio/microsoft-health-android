package com.microsoft.kapp.fragments.golf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.GolfCourseFilterActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.exceptions.LocationDisabledException;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.golf.GolfSearchResultItem;
import com.microsoft.kapp.models.golf.GolfSearchResultsModel;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.golf.CourseFilters;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class GolfSearchResultsFragment extends BaseFragmentWithOfflineSupport {
    private static final String TAG = GolfSearchResultsFragment.class.getSimpleName();
    protected CourseFilters mCourseFilters = new CourseFilters((Context) null);
    protected CustomGlyphView mFilterIcon;
    private FrameLayout mFilterIconFrameLayout;
    @Inject
    GolfService mGolfService;
    protected ListView mListView;
    protected TextView mNoResultsText;
    protected TextView mResultText;
    private InfiniteScrollListener mScrollListener;
    protected GolfSearchResultsAdapter mSearchAdapter;
    @Inject
    SettingsProvider mSettingsProvider;
    protected String mTelemetryTag;

    public abstract void clearData();

    protected abstract void getData(int i, CourseFilters courseFilters, Callback<GolfSearchResultsModel> callback);

    public abstract String getReferralName();

    public abstract boolean hasMoreData();

    public abstract boolean isFilterEnabled();

    public abstract boolean shouldshowResultCount();

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        this.mListView = (ListView) ViewUtils.getValidView(view, R.id.golf_search_results_listview, ListView.class);
        this.mResultText = (TextView) ViewUtils.getValidView(view, R.id.result, TextView.class);
        this.mNoResultsText = (TextView) ViewUtils.getValidView(view, R.id.empty, TextView.class);
        this.mFilterIconFrameLayout = (FrameLayout) ViewUtils.getValidView(view, R.id.filter_icon_frameLayout, FrameLayout.class);
        this.mFilterIcon = (CustomGlyphView) ViewUtils.getValidView(view, R.id.filter_icon, CustomGlyphView.class);
        this.mFilterIcon.setVisibility(8);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {
                if (GolfSearchResultsFragment.this.mListView.getItemAtPosition(position) instanceof GolfSearchResultItem) {
                    GolfSearchResultItem item = (GolfSearchResultItem) GolfSearchResultsFragment.this.mListView.getItemAtPosition(position);
                    if (item.getCourseId() != null && Validate.isActivityAlive(GolfSearchResultsFragment.this.getActivity())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(GolfCourseDetailsFragment.COURSE_ID, item.getCourseId());
                        bundle.putString(BaseFragment.ARG_REFERRER, GolfSearchResultsFragment.this.getReferralName());
                        ActivityUtils.launchLevelTwoActivity(GolfSearchResultsFragment.this.getActivity(), GolfCourseDetailsFragment.class, bundle);
                    }
                }
            }
        });
        this.mFilterIconFrameLayout.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Activity activity = GolfSearchResultsFragment.this.getActivity();
                if (activity != null) {
                    Intent intent = new Intent(activity, GolfCourseFilterActivity.class);
                    intent.putExtra(Constants.KEY_GOLF_COURSE_FILTER_SELECTION, GolfSearchResultsFragment.this.mCourseFilters);
                    GolfSearchResultsFragment.this.startActivityForResult(intent, Constants.GOLF_COURSE_FILTER);
                }
            }
        });
        return view;
    }

    protected int getLayoutId() {
        return R.layout.golf_search_fragment;
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage("Fitness/Golf/FindCourse");
        loadList();
    }

    protected void loadList() {
        if (this.mSearchAdapter != null) {
            clearData();
            this.mSearchAdapter.clear();
            this.mSearchAdapter.notifyDataSetChanged();
        }
        setupListViewScroll();
    }

    protected void setupListViewScroll() {
        if (isFilterEnabled()) {
            this.mFilterIcon.setVisibility(0);
        }
        if (shouldshowResultCount()) {
            this.mResultText.setText(String.format(getActivity().getResources().getString(R.string.guided_workout_fitness_plan_result_header, 0), new Object[0]));
        } else {
            this.mResultText.setText(getActivity().getResources().getString(R.string.result_header_no_count));
        }
        this.mScrollListener = new InfiniteScrollListener(5) { // from class: com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment.3
            @Override // com.microsoft.kapp.fragments.golf.InfiniteScrollListener
            public void loadMore(final int page, int totalItemsCount) {
                if (page == 1) {
                    GolfSearchResultsFragment.this.setState(1233);
                }
                GolfSearchResultsFragment.this.getData(page, GolfSearchResultsFragment.this.mCourseFilters, new ActivityScopedCallback(GolfSearchResultsFragment.this.getActivity(), new Callback<GolfSearchResultsModel>() { // from class: com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment.3.1
                    @Override // com.microsoft.kapp.Callback
                    public void callback(GolfSearchResultsModel result) {
                        if (result != null && result.getItems() != null && result.getItems().size() > 0) {
                            if (GolfSearchResultsFragment.this.mSearchAdapter == null) {
                                GolfSearchResultsFragment.this.mSearchAdapter = new GolfSearchResultsAdapter(GolfSearchResultsFragment.this.getActivity(), GolfSearchResultsFragment.this.mSettingsProvider, result.getItems());
                                GolfSearchResultsFragment.this.mListView.setAdapter((ListAdapter) GolfSearchResultsFragment.this.mSearchAdapter);
                            } else {
                                GolfSearchResultsFragment.this.mSearchAdapter.addAll(result.getItems());
                                GolfSearchResultsFragment.this.mSearchAdapter.notifyDataSetChanged();
                            }
                            if (GolfSearchResultsFragment.this.shouldshowResultCount()) {
                                GolfSearchResultsFragment.this.mResultText.setText(String.format(GolfSearchResultsFragment.this.getActivity().getResources().getString(R.string.guided_workout_fitness_plan_result_header, Integer.valueOf(GolfSearchResultsFragment.this.mSearchAdapter.getCount())), new Object[0]));
                            }
                            GolfSearchResultsFragment.this.mNoResultsText.setVisibility(4);
                        } else if (page == 1) {
                            GolfSearchResultsFragment.this.mNoResultsText.setVisibility(0);
                        }
                        GolfSearchResultsFragment.this.setState(1234);
                    }

                    @Override // com.microsoft.kapp.Callback
                    public void onError(Exception ex) {
                        KLog.e(GolfSearchResultsFragment.TAG, "unable to load data", ex);
                        if (ex != null && (ex instanceof LocationDisabledException)) {
                            GolfSearchResultsFragment.this.setState(1236);
                        } else {
                            GolfSearchResultsFragment.this.setState(1235);
                        }
                    }
                }));
            }

            @Override // com.microsoft.kapp.fragments.golf.InfiniteScrollListener
            public boolean hasData() {
                return GolfSearchResultsFragment.this.hasMoreData();
            }
        };
        this.mListView.setOnScrollListener(this.mScrollListener);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10006 && resultCode == -1 && data != null) {
            this.mCourseFilters = (CourseFilters) data.getExtras().getParcelable(Constants.KEY_GOLF_COURSE_FILTER_SELECTION);
            if (this.mSearchAdapter != null) {
                clearData();
                this.mSearchAdapter.clear();
                this.mSearchAdapter.notifyDataSetChanged();
            }
        }
    }

    public InfiniteScrollListener getScrollListener() {
        return this.mScrollListener;
    }
}
