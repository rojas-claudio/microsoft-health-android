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
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.WorkoutPlanFilterActivity;
import com.microsoft.kapp.adapters.GuidedWorkoutPlanBrowseDetailsListAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.guidedworkout.WorkoutPlanSummaryDetails;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.LoadStatus;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.WorkoutProviderType;
import com.microsoft.kapp.services.healthandfitness.models.HistogramEntry;
import com.microsoft.kapp.services.healthandfitness.models.HistogramValue;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponseEnvelope;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import org.joda.time.ReadableInstant;
/* loaded from: classes.dex */
public abstract class GuidedWorkoutsByCategoryFragment extends BaseFragmentWithOfflineSupport implements GuidedWorkoutBroadcastListener {
    static final String FILTER_TYPE = "type";
    static final String KEY_GUIDED_WORKOUT_CATEGORY_SELECT_INDEX = "selectedIndex";
    static final String KEY_GUIDED_WORKOUT_CATEGORY_SELECT_INDEX_TOP = "topSelection";
    static final String PIPE_CHAR = "%7C";
    static final String TILD_CHAR = "%7E";
    protected final String TAG = getClass().getSimpleName();
    private HashMap<String, List<String>> mAllFilterEntries;
    private HashMap<String, String> mAllFilterEntriesDisplayNamesMapping;
    private HashMap<String, String> mAllFilterEntriesNamesIdsMapping;
    private TextView mBackBotton;
    private TextView mCategoryNameTxtView;
    private TextView mEmptyListText;
    private volatile HashSet<String> mFavoritePlanIdSet;
    private HashMap<String, List<String>> mFilterCriteria;
    private CustomGlyphView mFilterIcon;
    private FrameLayout mFilterIconFrameLayout;
    private CustomGlyphView mFindWorkoutIcon;
    private GuidedWorkoutBroadcastReceiver mGuidedWorkoutBroadcastReceiver;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private boolean mInitializeStaticDataOnResume;
    private ListView mListView;
    private GuidedWorkoutPlanBrowseDetailsListAdapter mListViewAdapter;
    private int mListViewItemSelectedIndex;
    private int mListViewItemSelectedOffsetFromTop;
    private ProgressBar mPageProgressBar;
    private boolean mRefreshDynamicDataOnResume;
    private TextView mResultTextView;
    private List<WorkoutPlanSummaryDetails> mWorkoutPlanSummaryDetailsList;

    protected abstract List<WorkoutPlanSummaryDetails> filterWorkoutPlansSummaries(WorkoutsResponseEnvelope workoutsResponseEnvelope);

    protected abstract String getCategoryName();

    protected abstract int getCategoryType();

    protected abstract boolean isFavoritePage();

    protected abstract boolean isSearchFunctionEnabled();

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mInitializeStaticDataOnResume = true;
        if (savedInstanceState != null) {
            restoreSavedData(savedInstanceState);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guided_workout_by_category, container, false);
        this.mListView = (ListView) ViewUtils.getValidView(view, R.id.listview, ListView.class);
        this.mCategoryNameTxtView = (TextView) ViewUtils.getValidView(view, R.id.category_name, TextView.class);
        this.mFilterIconFrameLayout = (FrameLayout) ViewUtils.getValidView(view, R.id.filter_icon_frameLayout, FrameLayout.class);
        this.mFilterIcon = (CustomGlyphView) ViewUtils.getValidView(view, R.id.filter_icon, CustomGlyphView.class);
        this.mFindWorkoutIcon = (CustomGlyphView) ViewUtils.getValidView(view, R.id.find_a_workout_icon, CustomGlyphView.class);
        this.mPageProgressBar = (ProgressBar) ViewUtils.getValidView(view, R.id.guided_workout_load_progress, ProgressBar.class);
        this.mEmptyListText = (TextView) ViewUtils.getValidView(view, 16908292, TextView.class);
        this.mResultTextView = (TextView) ViewUtils.getValidView(view, R.id.result, TextView.class);
        if (isSearchFunctionEnabled()) {
            this.mFindWorkoutIcon.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    GuidedWorkoutsByCategoryFragment.this.startWorkoutPlanSearchActivity();
                }
            });
        } else {
            this.mFindWorkoutIcon.setVisibility(8);
        }
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                WorkoutPlanSummaryDetails workoutPlanSummaryDetails = (WorkoutPlanSummaryDetails) GuidedWorkoutsByCategoryFragment.this.mWorkoutPlanSummaryDetailsList.get(position);
                GuidedWorkoutsByCategoryFragment.this.startBrowseWorkoutPlanActivity(workoutPlanSummaryDetails.getWorkoutPlanId());
            }
        });
        this.mBackBotton = (TextView) ViewUtils.getValidView(view, R.id.back, TextView.class);
        this.mBackBotton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GuidedWorkoutsByCategoryFragment.this.exit();
            }
        });
        this.mFilterIconFrameLayout.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GuidedWorkoutsByCategoryFragment.this.startWorkoutPlanFilterActivity();
            }
        });
        this.mCategoryNameTxtView.setText(getCategoryName());
        this.mGuidedWorkoutBroadcastReceiver = new GuidedWorkoutBroadcastReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_FAVORITE);
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_UNFAVORITE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mGuidedWorkoutBroadcastReceiver, filter);
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchDynamicData() {
        MultipleRequestManager multipleRequestManager = new MultipleRequestManager(1, new MultipleRequestManager.OnRequestCompleteListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment.5
            @Override // com.microsoft.kapp.utils.MultipleRequestManager.OnRequestCompleteListener
            public void requestComplete(LoadStatus status) {
                GuidedWorkoutsByCategoryFragment.this.setState(1234);
                if (status == LoadStatus.ERROR) {
                    GuidedWorkoutsByCategoryFragment.this.updatePanelVisibility(LoadStatus.ERROR);
                    return;
                }
                GuidedWorkoutsByCategoryFragment.this.updatePanelVisibility(LoadStatus.LOADED);
                GuidedWorkoutsByCategoryFragment.this.updateListViewAdapter(GuidedWorkoutsByCategoryFragment.this.getActivity(), GuidedWorkoutsByCategoryFragment.this.getBrowseDetailsList());
            }
        });
        fetchStrengthWorkouts(multipleRequestManager);
    }

    private void fetchStaticData() {
        MultipleRequestManager multipleRequestManager = new MultipleRequestManager(2, new MultipleRequestManager.OnRequestCompleteListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment.6
            @Override // com.microsoft.kapp.utils.MultipleRequestManager.OnRequestCompleteListener
            public void requestComplete(LoadStatus status) {
                GuidedWorkoutsByCategoryFragment.this.fetchDynamicData();
            }
        });
        updatePanelVisibility(LoadStatus.LOADING);
        fetchFavoriteWorkoutPlans(multipleRequestManager);
        fetchAllHnFWorkoutsFiltersEntries(multipleRequestManager);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GUIDED_WORKOUTS_RESULTS);
        if (this.mInitializeStaticDataOnResume) {
            fetchStaticData();
            this.mInitializeStaticDataOnResume = false;
        } else if (this.mRefreshDynamicDataOnResume) {
            fetchDynamicData();
            this.mRefreshDynamicDataOnResume = false;
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mGuidedWorkoutBroadcastReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateListViewAdapter(Context context, List<WorkoutPlanSummaryDetails> list) {
        if (context != null && list != null) {
            this.mListView.setVisibility(0);
            this.mListViewAdapter = (GuidedWorkoutPlanBrowseDetailsListAdapter) this.mListView.getAdapter();
            if (this.mListViewAdapter == null) {
                this.mListViewAdapter = new GuidedWorkoutPlanBrowseDetailsListAdapter(context, list);
                this.mListView.setAdapter((ListAdapter) this.mListViewAdapter);
            } else {
                this.mListViewAdapter.clear();
                this.mListViewAdapter.addAll(list);
            }
            setResultHeaderText(list.size());
            if (list.size() == 0) {
                updateEmptyListTextView(this.mEmptyListText);
                this.mEmptyListText.setVisibility(0);
                return;
            }
            this.mEmptyListText.setVisibility(4);
            this.mListView.setSelectionFromTop(this.mListViewItemSelectedIndex, this.mListViewItemSelectedOffsetFromTop);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startBrowseWorkoutPlanActivity(String workoutPlanId) {
        Bundle bundle = new Bundle();
        bundle.putString("workoutPlanId", workoutPlanId);
        saveListViewSelectionIndex();
        ActivityUtils.launchLevelTwoActivityForResult(getActivity(), GuidedWorkoutsBrowsePlanFragment.class, bundle, this, 10002);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startWorkoutPlanFilterActivity() {
        if (this.mAllFilterEntries == null || this.mAllFilterEntries.size() == 0 || this.mAllFilterEntriesDisplayNamesMapping == null || this.mAllFilterEntriesDisplayNamesMapping.size() == 0) {
            KLog.e(this.TAG, "Imcomplete or Corrupt HnF Filters data!");
            return;
        }
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, WorkoutPlanFilterActivity.class);
            intent.putExtra(Constants.KEY_GUIDED_WORKOUT_ALL_FILTERS_KEYS_LIST, this.mAllFilterEntries);
            intent.putExtra(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION, this.mFilterCriteria);
            intent.putExtra(Constants.KEY_GUIDED_WORKOUT_FILTERS_NAMES_LIST, this.mAllFilterEntriesDisplayNamesMapping);
            intent.putExtra(Constants.KEY_GUIDED_WORKOUT_PROVIDER_TYPE, getWorkoutProviderType());
            if (this.mAllFilterEntriesDisplayNamesMapping != null) {
                intent.putStringArrayListExtra(Constants.KEY_LINKED_HASH_MAP_KEYS_LIST, new ArrayList<>(this.mAllFilterEntriesDisplayNamesMapping.keySet()));
            }
            saveListViewSelectionIndex();
            startActivityForResult(intent, 10001);
        }
    }

    protected void fetchStrengthWorkouts(final MultipleRequestManager multipleRequestManager) {
        if (multipleRequestManager == null) {
            KLog.w(this.TAG, "multipleRequestManager is null!");
        }
        updatePanelVisibility(LoadStatus.LOADING);
        String filterString = CommonUtils.calculateHnFFilterString(this.mAllFilterEntriesNamesIdsMapping, this.mFilterCriteria);
        this.mGuidedWorkoutService.getHnFStrengthWorkoutPlans(filterString, getWorkoutProviderType(), new ActivityScopedCallback(this, new Callback<WorkoutsResponseEnvelope>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment.7
            @Override // com.microsoft.kapp.Callback
            public void callback(WorkoutsResponseEnvelope workoutsResponseEnvelope) {
                try {
                    List<WorkoutPlanSummaryDetails> browseDetailsList = GuidedWorkoutsByCategoryFragment.this.filterWorkoutPlansSummaries(workoutsResponseEnvelope);
                    GuidedWorkoutUtils.flagFavoritedWorkoutPlans(browseDetailsList, GuidedWorkoutsByCategoryFragment.this.getFavoritesPlanIdSet());
                    if (GuidedWorkoutsByCategoryFragment.this.getWorkoutProviderType() == WorkoutProviderType.Me) {
                        Collections.sort(browseDetailsList, new WorkoutPlanSummaryDetailsComparator());
                    }
                    GuidedWorkoutsByCategoryFragment.this.setBrowseDetailsList(browseDetailsList);
                    if (multipleRequestManager != null) {
                        multipleRequestManager.notifyRequestSucceeded();
                    }
                    if (GuidedWorkoutsByCategoryFragment.this.mFilterCriteria != null && GuidedWorkoutsByCategoryFragment.this.mFilterCriteria.containsKey("level")) {
                        HashMap<String, String> properties = new HashMap<>();
                        List<String> criteria = (List) GuidedWorkoutsByCategoryFragment.this.mFilterCriteria.get("level");
                        for (String criterion : criteria) {
                            properties.put("level", criterion);
                        }
                        properties.put(TelemetryConstants.Events.GuidedWorkoutFilterResults.Dimensions.COUNT, String.valueOf(browseDetailsList.size()));
                        Telemetry.logEvent(TelemetryConstants.Events.GuidedWorkoutFilterResults.EVENT_NAME, properties, null);
                    }
                } catch (Exception exception) {
                    KLog.e(GuidedWorkoutsByCategoryFragment.this.TAG, "Exception reading workout response", exception);
                    if (multipleRequestManager != null) {
                        multipleRequestManager.notifyRequestFailed();
                    }
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception exception) {
                GuidedWorkoutsByCategoryFragment.this.updatePanelVisibility(LoadStatus.ERROR);
                if (multipleRequestManager != null) {
                    multipleRequestManager.notifyRequestFailed();
                }
            }
        }));
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001 && resultCode == -1) {
            this.mFilterCriteria = (HashMap) data.getSerializableExtra(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION);
            this.mRefreshDynamicDataOnResume = true;
        } else if (requestCode == 10002 && resultCode == 4) {
            getDialogManager().showNetworkErrorDialog(getActivity());
        } else if (resultCode == 6) {
            CommonUtils.setResultAndExit(getActivity(), 6);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startWorkoutPlanSearchActivity() {
        Activity activity = getActivity();
        if (Validate.isActivityAlive(activity)) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_GUIDED_WORKOUT_CATEGORY_NAME, getCategoryName());
            bundle.putInt(Constants.KEY_GUIDED_WORKOUT_CATEGORY_TYPE, getCategoryType());
            saveListViewSelectionIndex();
            ActivityUtils.launchLevelTwoActivityForResult(activity, GuidedWorkoutsSearchFragment.class, bundle, this, 10003);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION, CommonUtils.convertListMapToBundle(this.mFilterCriteria));
        saveListViewSelectionIndex();
        outState.putInt(KEY_GUIDED_WORKOUT_CATEGORY_SELECT_INDEX, this.mListViewItemSelectedIndex);
        outState.putInt(KEY_GUIDED_WORKOUT_CATEGORY_SELECT_INDEX_TOP, this.mListViewItemSelectedOffsetFromTop);
    }

    private void restoreSavedData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bundle filterCriteriaBundle = savedInstanceState.getBundle(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION);
            this.mFilterCriteria = CommonUtils.extractStringListMapFromBundle(filterCriteriaBundle);
            this.mListViewItemSelectedOffsetFromTop = savedInstanceState.getInt(KEY_GUIDED_WORKOUT_CATEGORY_SELECT_INDEX_TOP);
            this.mListViewItemSelectedIndex = savedInstanceState.getInt(KEY_GUIDED_WORKOUT_CATEGORY_SELECT_INDEX);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exit() {
        getActivity().finish();
    }

    private void fetchFavoriteWorkoutPlans(final MultipleRequestManager multipleRequestManager) {
        if (multipleRequestManager == null) {
            KLog.w(this.TAG, "multipleRequestManager is null!");
        }
        this.mGuidedWorkoutService.getFavoriteWorkoutPlans(new ActivityScopedCallback(this, new Callback<List<FavoriteWorkoutPlan>>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment.8
            @Override // com.microsoft.kapp.Callback
            public void callback(List<FavoriteWorkoutPlan> favoriteWorkoutPlans) {
                if (favoriteWorkoutPlans != null) {
                    Set<String> favoritesPlanIdsSet = GuidedWorkoutsByCategoryFragment.this.getFavoritesPlanIdSet();
                    favoritesPlanIdsSet.clear();
                    favoritesPlanIdsSet.addAll(GuidedWorkoutUtils.getFavoritedWorkoutPlanIdSet(favoriteWorkoutPlans));
                }
                if (multipleRequestManager != null) {
                    multipleRequestManager.notifyRequestSucceeded();
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GuidedWorkoutsByCategoryFragment.this.TAG, "unable to fetch favorite workoutPlans", ex);
                if (multipleRequestManager != null) {
                    multipleRequestManager.notifyRequestFailed();
                }
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePanelVisibility(LoadStatus status) {
        this.mPageProgressBar.setVisibility(8);
        this.mListView.setVisibility(8);
        switch (status) {
            case LOADING:
                setState(1233);
                this.mPageProgressBar.setVisibility(0);
                return;
            case LOADED:
                setState(1234);
                this.mListView.setVisibility(0);
                return;
            case ERROR:
            case NO_DATA:
                setState(1235);
                return;
            default:
                KLog.e(this.TAG, "Illegal value of LoadStatus has been used!");
                return;
        }
    }

    private void fetchAllHnFWorkoutsFiltersEntries(final MultipleRequestManager multipleRequestManager) {
        if (multipleRequestManager == null) {
            KLog.w(this.TAG, "multipleRequestManager is null!");
        }
        this.mGuidedWorkoutService.getHnFStrengthWorkoutPlans(getWorkoutProviderType(), new ActivityScopedCallback(this, new Callback<WorkoutsResponseEnvelope>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment.9
            @Override // com.microsoft.kapp.Callback
            public void callback(WorkoutsResponseEnvelope result) {
                if (result != null) {
                    try {
                        GuidedWorkoutsByCategoryFragment.this.populateFilterList(result);
                    } catch (Exception ex) {
                        KLog.e(GuidedWorkoutsByCategoryFragment.this.TAG, "Unexpected Error!", ex);
                    }
                }
                if (multipleRequestManager != null) {
                    multipleRequestManager.notifyRequestSucceeded();
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception exception) {
                KLog.e(GuidedWorkoutsByCategoryFragment.this.TAG, "Error Fetching HnF workourPlans!", exception);
                if (multipleRequestManager != null) {
                    multipleRequestManager.notifyRequestFailed();
                }
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populateFilterList(WorkoutsResponseEnvelope responseEnvelope) {
        if (responseEnvelope != null) {
            this.mAllFilterEntries = new HashMap<>();
            this.mAllFilterEntriesNamesIdsMapping = new HashMap<>();
            this.mAllFilterEntriesDisplayNamesMapping = new LinkedHashMap();
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
                            this.mAllFilterEntries.put(filter, filterValues);
                            this.mAllFilterEntriesDisplayNamesMapping.put(entry.getDisplay(), filter);
                        }
                    }
                }
            }
        }
    }

    protected void setBrowseDetailsList(List<WorkoutPlanSummaryDetails> workoutPlanSummaryDetailsList) {
        this.mWorkoutPlanSummaryDetailsList = workoutPlanSummaryDetailsList;
    }

    protected List<WorkoutPlanSummaryDetails> getBrowseDetailsList() {
        if (this.mWorkoutPlanSummaryDetailsList == null) {
            this.mWorkoutPlanSummaryDetailsList = new ArrayList();
        }
        return this.mWorkoutPlanSummaryDetailsList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HashSet<String> getFavoritesPlanIdSet() {
        if (this.mFavoritePlanIdSet == null) {
            this.mFavoritePlanIdSet = new HashSet<>();
        }
        return this.mFavoritePlanIdSet;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutBroadcastListener
    public void onGWBroadcastReceived(Context context, Intent data) {
        MultipleRequestManager multipleRequestManager;
        if (isAdded() && data != null && this.mWorkoutPlanSummaryDetailsList != null) {
            String operation = data.getAction();
            int operationStatus = data.getIntExtra(GuidedWorkoutNotificationHandler.KEY_ACTION_STATUS, 2);
            if ((GuidedWorkoutNotificationHandler.OPERATION_FAVORITE.equals(operation) || GuidedWorkoutNotificationHandler.OPERATION_UNFAVORITE.equals(operation)) && operationStatus == 1) {
                if (isFavoritePage()) {
                    multipleRequestManager = new MultipleRequestManager(1, new MultipleRequestManager.OnRequestCompleteListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment.10
                        @Override // com.microsoft.kapp.utils.MultipleRequestManager.OnRequestCompleteListener
                        public void requestComplete(LoadStatus status) {
                            GuidedWorkoutsByCategoryFragment.this.fetchDynamicData();
                        }
                    });
                } else {
                    multipleRequestManager = new MultipleRequestManager(1, new MultipleRequestManager.OnRequestCompleteListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment.11
                        @Override // com.microsoft.kapp.utils.MultipleRequestManager.OnRequestCompleteListener
                        public void requestComplete(LoadStatus status) {
                            GuidedWorkoutUtils.flagFavoritedWorkoutPlans(GuidedWorkoutsByCategoryFragment.this.getBrowseDetailsList(), GuidedWorkoutsByCategoryFragment.this.getFavoritesPlanIdSet());
                            GuidedWorkoutsByCategoryFragment.this.mListViewAdapter.notifyDataSetChanged();
                        }
                    });
                }
                fetchFavoriteWorkoutPlans(multipleRequestManager);
            }
        }
    }

    protected WorkoutProviderType getWorkoutProviderType() {
        return WorkoutProviderType.Provider;
    }

    private void saveListViewSelectionIndex() {
        View vw = this.mListView.getChildAt(0);
        this.mListViewItemSelectedIndex = this.mListView.getFirstVisiblePosition();
        this.mListViewItemSelectedOffsetFromTop = vw != null ? vw.getTop() : 0;
    }

    private void setResultHeaderText(int size) {
        this.mResultTextView.setText(String.format(getResources().getString(R.string.guided_workout_fitness_plan_result_header), Integer.valueOf(size)));
    }

    public void updateEmptyListTextView(TextView textView) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class WorkoutPlanSummaryDetailsComparator implements Comparator<WorkoutPlanSummaryDetails> {
        WorkoutPlanSummaryDetailsComparator() {
        }

        @Override // java.util.Comparator
        public int compare(WorkoutPlanSummaryDetails d1, WorkoutPlanSummaryDetails d2) {
            return d2.getPublishDate().compareTo((ReadableInstant) d1.getPublishDate());
        }
    }
}
