package com.microsoft.kapp.fragments.guidedworkout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.WorkoutPlanFilterActivity;
import com.microsoft.kapp.adapters.GuidedWorkoutPlanBrowseDetailsListAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.SearchBaseFragment;
import com.microsoft.kapp.guidedworkout.WorkoutPlanSummaryDetails;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.WorkoutProviderType;
import com.microsoft.kapp.services.healthandfitness.models.HistogramEntry;
import com.microsoft.kapp.services.healthandfitness.models.HistogramValue;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponseEnvelope;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GuidedWorkoutsSearchFragment extends SearchBaseFragment implements GuidedWorkoutBroadcastListener {
    private final String TAG = getClass().getSimpleName();
    private GuidedWorkoutPlanBrowseDetailsListAdapter mAdapter;
    private Map<String, String> mAllFilterEntriesNamesIdsMapping;
    private String mCategoryName;
    private int mCategoryType;
    private HashSet<String> mFavoritePlanIdSet;
    private HashMap<String, List<String>> mFilterCriteria;
    private CustomGlyphView mFilterIcon;
    private ViewGroup mFilterIconFrameLayout;
    private String mFilterString;
    private GuidedWorkoutBroadcastReceiver mGuidedWorkoutBroadcastReceiver;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private ListView mListView;
    private ViewGroup mListViewHeader;
    private TextView mNoSearchResult;
    private TextView mResultTextView;
    private TextView mSearchName;
    @Inject
    SettingsProvider mSettings;
    private List<WorkoutPlanSummaryDetails> mWorkoutPlanSummaryDetailsList;

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreSavedData(savedInstanceState);
        this.mFilterString = this.mSettings.getLastGuidedWorkoutFilterString();
        if (this.mFilterString == null) {
            this.mFilterString = "";
        }
        Bundle argumentsData = getArguments();
        this.mCategoryName = argumentsData.getString(Constants.KEY_GUIDED_WORKOUT_CATEGORY_NAME);
        this.mCategoryType = argumentsData.getInt(Constants.KEY_GUIDED_WORKOUT_CATEGORY_TYPE, -1);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GUIDED_WORKOUTS_SEARCH);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mGuidedWorkoutBroadcastReceiver);
    }

    @Override // com.microsoft.kapp.fragments.SearchBaseFragment, com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateChildView(inflater, container, savedInstanceState);
        this.mListView = (ListView) ViewUtils.getValidView(view, R.id.listview, ListView.class);
        this.mFindIcon = (TextView) ViewUtils.getValidView(view, R.id.search_icon, TextView.class);
        this.mSearchName = (TextView) ViewUtils.getValidView(view, R.id.search_name, TextView.class);
        this.mNoSearchResult = (TextView) ViewUtils.getValidView(view, 16908292, TextView.class);
        this.mNoSearchResult.setVisibility(4);
        this.mListViewHeader = (ViewGroup) ViewUtils.getValidView(view, R.id.list_view_header_plus_arrow, ViewGroup.class);
        this.mResultTextView = (TextView) ViewUtils.getValidView(this.mListViewHeader, R.id.result, TextView.class);
        this.mFilterIcon = (CustomGlyphView) ViewUtils.getValidView(view, R.id.filter_icon, CustomGlyphView.class);
        this.mFilterIconFrameLayout = (ViewGroup) ViewUtils.getValidView(view, R.id.filter_icon_frameLayout, ViewGroup.class);
        this.mFilterIconFrameLayout.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsSearchFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GuidedWorkoutsSearchFragment.this.startWorkoutPlanFilterActivity();
            }
        });
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsSearchFragment.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                WorkoutPlanSummaryDetails workoutPlanSummaryDetails = (WorkoutPlanSummaryDetails) GuidedWorkoutsSearchFragment.this.mWorkoutPlanSummaryDetailsList.get(position);
                GuidedWorkoutsSearchFragment.this.startBrowseWorkoutPlanActivity(workoutPlanSummaryDetails.getWorkoutPlanId());
            }
        });
        this.mSearchName.setVisibility(4);
        this.mClearSearch.setVisibility(0);
        this.mFavoritePlanIdSet = new HashSet<>();
        this.mWorkoutPlanSummaryDetailsList = new ArrayList();
        this.mAdapter = new GuidedWorkoutPlanBrowseDetailsListAdapter(getActivity(), this.mWorkoutPlanSummaryDetailsList);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        fetchFavoriteWorkoutPlans();
        this.mGuidedWorkoutBroadcastReceiver = new GuidedWorkoutBroadcastReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_FAVORITE);
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_UNFAVORITE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mGuidedWorkoutBroadcastReceiver, filter);
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startWorkoutPlanFilterActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, WorkoutPlanFilterActivity.class);
            intent.putExtra(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION, this.mFilterCriteria);
            intent.putExtra(Constants.KEY_GUIDED_WORKOUT_PROVIDER_TYPE, WorkoutProviderType.Provider);
            startActivityForResult(intent, 10001);
        }
    }

    @Override // com.microsoft.kapp.fragments.SearchBaseFragment
    protected void executeSearchClick() {
        if (this.mIsSearchInProgress.compareAndSet(false, true)) {
            if (this.mIsSearchOn) {
                this.mSearchBox.setVisibility(0);
                this.mSearchName.setVisibility(4);
                this.mSavedSearchString = this.mSearchBox.getText().toString();
                if (this.mSavedSearchString.length() > 0) {
                    searchWorkouts(this.mSavedSearchString);
                    this.mIsSearchOn = false;
                    return;
                }
                return;
            }
            this.mSearchBox.setVisibility(0);
            this.mSearchName.setVisibility(4);
            this.mSearchBox.setText("");
            this.mSearchBox.requestFocus();
            ViewUtils.openSoftKeyboard(this.mSearchBox);
            this.mIsSearchOn = true;
            this.mSearchBox.setEnabled(true);
            this.mIsSearchInProgress.set(false);
        }
    }

    protected void startBrowseWorkoutPlanActivity(String workoutPlanId) {
        Bundle bundle = new Bundle();
        bundle.putString("workoutPlanId", workoutPlanId);
        ActivityUtils.launchLevelTwoActivityForResult(getActivity(), GuidedWorkoutsBrowsePlanFragment.class, bundle, this, 10002);
    }

    protected void searchWorkouts(final String name) {
        this.mSearchBox.setEnabled(false);
        this.mGuidedWorkoutService.getHnFStrengthWorkoutPlans(name, this.mFilterString, WorkoutProviderType.Provider, new ActivityScopedCallback(this, new Callback<WorkoutsResponseEnvelope>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsSearchFragment.3
            @Override // com.microsoft.kapp.Callback
            public void callback(WorkoutsResponseEnvelope result) {
                try {
                    GuidedWorkoutsSearchFragment.this.populateFilterList(result);
                    GuidedWorkoutsSearchFragment.this.filterWorkoutPlansSummaries(result);
                    GuidedWorkoutsSearchFragment.this.mSearchBox.setVisibility(4);
                    GuidedWorkoutsSearchFragment.this.mSearchName.setVisibility(0);
                    GuidedWorkoutsSearchFragment.this.mSearchName.setText(String.format("\"%s\"", name));
                    GuidedWorkoutsSearchFragment.this.setResultHeaderText(GuidedWorkoutsSearchFragment.this.mWorkoutPlanSummaryDetailsList.size());
                    if (GuidedWorkoutsSearchFragment.this.mWorkoutPlanSummaryDetailsList.size() > 0) {
                        GuidedWorkoutsSearchFragment.this.mNoSearchResult.setVisibility(4);
                    } else {
                        GuidedWorkoutsSearchFragment.this.mNoSearchResult.setVisibility(0);
                    }
                    GuidedWorkoutsSearchFragment.this.mAdapter.notifyDataSetChanged();
                    GuidedWorkoutsSearchFragment.this.mListViewHeader.setVisibility(0);
                    GuidedWorkoutsSearchFragment.this.mListView.setBackgroundColor(GuidedWorkoutsSearchFragment.this.getResources().getColor(R.color.white));
                } catch (Exception exception) {
                    KLog.e(GuidedWorkoutsSearchFragment.this.TAG, "Exception reading workout response", exception);
                } finally {
                    GuidedWorkoutsSearchFragment.this.mIsSearchInProgress.set(false);
                }
                GuidedWorkoutsSearchFragment.this.setState(1234);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception exception) {
                KLog.e(GuidedWorkoutsSearchFragment.this.TAG, "Exception During fetching workoutPlans: %s", exception);
                GuidedWorkoutsSearchFragment.this.setState(1235);
                GuidedWorkoutsSearchFragment.this.mIsSearchOn = true;
                GuidedWorkoutsSearchFragment.this.mIsSearchInProgress.set(false);
            }
        }));
    }

    private void populateStrengthWorkoutPlansSummary(WorkoutsResponseEnvelope responseEnvelope) {
        if (responseEnvelope == null || responseEnvelope.getResponse() == null || responseEnvelope.getResponse().getWorkoutSummaries() == null) {
            KLog.d(this.TAG, "Corrupt WorkoutsResponseEnvelope for guidedWorkout!");
            return;
        }
        this.mWorkoutPlanSummaryDetailsList.clear();
        WorkoutSummary[] arr$ = responseEnvelope.getResponse().getWorkoutSummaries();
        for (WorkoutSummary summary : arr$) {
            if (summary != null) {
                WorkoutPlanSummaryDetails workoutPlanSummaryDetails = new WorkoutPlanSummaryDetails(summary);
                if (this.mFavoritePlanIdSet != null && this.mFavoritePlanIdSet.contains(summary.getId())) {
                    workoutPlanSummaryDetails.setIsFavorite(true);
                }
                this.mWorkoutPlanSummaryDetailsList.add(workoutPlanSummaryDetails);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void filterWorkoutPlansSummaries(WorkoutsResponseEnvelope result) {
        if (this.mCategoryType == -1 || this.mCategoryName == null) {
            populateStrengthWorkoutPlansSummary(result);
        } else if (this.mCategoryType == 1) {
            populateFavoriteWorkoutPlansSummary(result);
        } else if (this.mCategoryType == 0) {
            populateStrengthWorkoutPlansSummary(result);
        } else if (this.mCategoryType != 2) {
            populateBrandWorkoutPlansSummary(result);
        }
    }

    private void populateFavoriteWorkoutPlansSummary(WorkoutsResponseEnvelope responseEnvelope) {
        if (responseEnvelope == null || responseEnvelope.getResponse() == null || responseEnvelope.getResponse().getWorkoutSummaries() == null) {
            KLog.d(this.TAG, "Corrupt WorkoutsResponseEnvelope for guidedWorkout!");
            return;
        }
        this.mWorkoutPlanSummaryDetailsList.clear();
        if (this.mFavoritePlanIdSet != null) {
            WorkoutSummary[] arr$ = responseEnvelope.getResponse().getWorkoutSummaries();
            for (WorkoutSummary summary : arr$) {
                if (summary != null) {
                    WorkoutPlanSummaryDetails workoutPlanSummaryDetails = new WorkoutPlanSummaryDetails(summary);
                    if (this.mFavoritePlanIdSet.contains(summary.getId())) {
                        workoutPlanSummaryDetails.setIsFavorite(true);
                        this.mWorkoutPlanSummaryDetailsList.add(workoutPlanSummaryDetails);
                    }
                }
            }
        }
    }

    private void populateBrandWorkoutPlansSummary(WorkoutsResponseEnvelope responseEnvelope) {
        if (responseEnvelope == null || responseEnvelope.getResponse() == null || responseEnvelope.getResponse().getWorkoutSummaries() == null) {
            KLog.d(this.TAG, "Corrupt WorkoutsResponseEnvelope for guidedWorkout!");
            return;
        }
        this.mWorkoutPlanSummaryDetailsList.clear();
        WorkoutSummary[] arr$ = responseEnvelope.getResponse().getWorkoutSummaries();
        for (WorkoutSummary summary : arr$) {
            if (summary != null && this.mCategoryName.equals(summary.getBrandName())) {
                WorkoutPlanSummaryDetails workoutPlanSummaryDetails = new WorkoutPlanSummaryDetails(summary);
                if (this.mFavoritePlanIdSet != null && this.mFavoritePlanIdSet.contains(summary.getId())) {
                    workoutPlanSummaryDetails.setIsFavorite(true);
                }
                this.mWorkoutPlanSummaryDetailsList.add(workoutPlanSummaryDetails);
            }
        }
    }

    protected Activity getHostActivity() {
        return getActivity();
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10002 && resultCode == 4) {
            setState(1235);
        } else if (resultCode == 6) {
            CommonUtils.setResultAndExit(getActivity(), 6);
        } else if (requestCode == 10001 && resultCode == -1 && data != null) {
            this.mFilterCriteria = (HashMap) data.getSerializableExtra(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION);
            this.mFilterCriteria.remove(Constants.FITNESS_PROS);
            this.mFilterString = CommonUtils.calculateHnFFilterString(this.mAllFilterEntriesNamesIdsMapping, this.mFilterCriteria);
            searchWorkouts(this.mSavedSearchString);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populateFilterList(WorkoutsResponseEnvelope responseEnvelope) {
        if (responseEnvelope != null) {
            this.mAllFilterEntriesNamesIdsMapping = new HashMap();
            HistogramEntry[] histogramEntries = responseEnvelope.getHistogramEntries();
            if (histogramEntries != null) {
                for (HistogramEntry entry : histogramEntries) {
                    if (entry == null) {
                        KLog.d(this.TAG, "HistogramEntry cannot be null!");
                    } else {
                        String filter = entry.getFilter();
                        if (GuidedWorkoutUtils.showFilter(filter)) {
                            List<String> filterValues = new ArrayList<>();
                            HistogramValue[] arr$ = entry.getValues();
                            for (HistogramValue value : arr$) {
                                if (value == null) {
                                    KLog.d(this.TAG, "HistogramValue cannot be null!");
                                } else {
                                    filterValues.add(value.getName());
                                    this.mAllFilterEntriesNamesIdsMapping.put(value.getName(), value.getId());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override // com.microsoft.kapp.fragments.SearchBaseFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("NameSearch", this.mSavedSearchString);
        outState.putBoolean("IconEnabled", this.mFindIcon.isEnabled());
        outState.putBundle(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION, CommonUtils.convertListMapToBundle(this.mFilterCriteria));
    }

    private void restoreSavedData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bundle filterCriteriaBundle = savedInstanceState.getBundle(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION);
            this.mFilterCriteria = CommonUtils.extractStringListMapFromBundle(filterCriteriaBundle);
        }
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutBroadcastListener
    public void onGWBroadcastReceived(Context context, Intent data) {
        if (isAdded() && data != null && this.mWorkoutPlanSummaryDetailsList != null) {
            String operation = data.getAction();
            int operationStatus = data.getIntExtra(GuidedWorkoutNotificationHandler.KEY_ACTION_STATUS, 2);
            if ((GuidedWorkoutNotificationHandler.OPERATION_FAVORITE.equals(operation) || GuidedWorkoutNotificationHandler.OPERATION_UNFAVORITE.equals(operation)) && operationStatus == 1) {
                fetchFavoriteWorkoutPlans();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setResultHeaderText(int size) {
        this.mResultTextView.setText(String.format(getResources().getString(R.string.guided_workout_fitness_plan_result_header), Integer.valueOf(size)));
    }

    private void fetchFavoriteWorkoutPlans() {
        this.mGuidedWorkoutService.getFavoriteWorkoutPlans(new ActivityScopedCallback(this, new Callback<List<FavoriteWorkoutPlan>>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsSearchFragment.4
            @Override // com.microsoft.kapp.Callback
            public void callback(List<FavoriteWorkoutPlan> favoriteWorkoutPlans) {
                GuidedWorkoutsSearchFragment.this.mFavoritePlanIdSet.clear();
                GuidedWorkoutsSearchFragment.this.mFavoritePlanIdSet.addAll(GuidedWorkoutUtils.getFavoritedWorkoutPlanIdSet(favoriteWorkoutPlans));
                GuidedWorkoutUtils.flagFavoritedWorkoutPlans(GuidedWorkoutsSearchFragment.this.mWorkoutPlanSummaryDetailsList, GuidedWorkoutsSearchFragment.this.mFavoritePlanIdSet);
                GuidedWorkoutsSearchFragment.this.mAdapter.notifyDataSetChanged();
                GuidedWorkoutsSearchFragment.this.setState(1234);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GuidedWorkoutsSearchFragment.this.TAG, "unable to fetch favorite workoutPlans", ex);
                GuidedWorkoutsSearchFragment.this.setState(1234);
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.SearchBaseFragment
    protected int getLayoutid() {
        return R.layout.guided_workout_search_fragment;
    }
}
