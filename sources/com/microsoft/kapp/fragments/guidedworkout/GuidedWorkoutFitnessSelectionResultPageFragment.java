package com.microsoft.kapp.fragments.guidedworkout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController;
import com.microsoft.kapp.activities.WorkoutPlanFilterActivity;
import com.microsoft.kapp.adapters.GuidedWorkoutPlanBrowseDetailsListAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.guidedworkout.WorkoutPlanSummaryDetails;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.LoadStatus;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.WorkoutProviderType;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponseEnvelope;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.krestsdk.models.DisplaySubType;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GuidedWorkoutFitnessSelectionResultPageFragment extends BaseFragment implements GuidedWorkoutBroadcastListener {
    private final String TAG = getClass().getSimpleName();
    private WorkoutPlanDiscoveryActivityController mActivityDataController;
    private GuidedWorkoutPlanBrowseDetailsListAdapter mAdapter;
    private Map<String, String> mAllFilterEntriesNamesIdsMapping;
    private String mBrandName;
    private TextView mEmptyListText;
    private HashSet<String> mFavoritePlanIdSet;
    private HashMap<String, List<String>> mFilterCriteria;
    private CustomGlyphView mFilterIcon;
    private FrameLayout mFilterIconFrameLayout;
    private String mFilterString;
    private Map<String, String> mFitnessPlanFilterSelection;
    private GuidedWorkoutBroadcastReceiver mGuidedWorkoutBroadcastReceiver;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private ListView mListView;
    private TextView mLoadError;
    private MultipleRequestManager mMultipleRequestManager;
    private ProgressBar mPageProgressBar;
    private int mPlanDiscoveryType;
    private TextView mResultText;
    private List<String> mSelectedBrandsFilters;
    private String mSelectedWorkoutTypeName;
    private List<WorkoutPlanSummaryDetails> mWorkoutPlanSummaryDetailsList;

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivityDataController = (WorkoutPlanDiscoveryActivityController) WorkoutPlanDiscoveryActivityController.class.cast(getActivity());
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.guided_workout_fitness_plan_selection_result_page, container, false);
        this.mListView = (ListView) ViewUtils.getValidView(view, R.id.listview, ListView.class);
        this.mResultText = (TextView) ViewUtils.getValidView(view, R.id.result, TextView.class);
        this.mFilterIconFrameLayout = (FrameLayout) ViewUtils.getValidView(view, R.id.filter_icon_frameLayout, FrameLayout.class);
        this.mFilterIcon = (CustomGlyphView) ViewUtils.getValidView(view, R.id.filter_icon, CustomGlyphView.class);
        this.mPageProgressBar = (ProgressBar) ViewUtils.getValidView(view, R.id.guided_workout_load_progress, ProgressBar.class);
        this.mLoadError = (TextView) ViewUtils.getValidView(view, R.id.guided_workout_load_error, TextView.class);
        this.mEmptyListText = (TextView) ViewUtils.getValidView(view, 16908292, TextView.class);
        this.mPlanDiscoveryType = this.mActivityDataController.getPlanType();
        this.mFitnessPlanFilterSelection = this.mActivityDataController.getFitnessPlanFilterSelection();
        this.mBrandName = this.mActivityDataController.getSelectedBrandName();
        this.mSelectedWorkoutTypeName = this.mActivityDataController.getSelectedType();
        if (this.mBrandName != null) {
            this.mSelectedBrandsFilters = new ArrayList();
            this.mSelectedBrandsFilters.add(this.mBrandName);
        }
        this.mAllFilterEntriesNamesIdsMapping = this.mActivityDataController.getAllFiltersNamesIdsMapping();
        this.mFilterString = CommonUtils.calculateHnFFitnessPlanSelectionFilterString(this.mAllFilterEntriesNamesIdsMapping, this.mFitnessPlanFilterSelection);
        if (this.mFilterString == null) {
            this.mFilterString = "";
        }
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                WorkoutPlanSummaryDetails workoutPlanSummaryDetails = (WorkoutPlanSummaryDetails) GuidedWorkoutFitnessSelectionResultPageFragment.this.mWorkoutPlanSummaryDetailsList.get(position);
                GuidedWorkoutFitnessSelectionResultPageFragment.this.BrowseWorkoutPlan(workoutPlanSummaryDetails.getWorkoutPlanId());
            }
        });
        this.mFilterIconFrameLayout.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GuidedWorkoutFitnessSelectionResultPageFragment.this.startWorkoutPlanFilterActivity();
            }
        });
        setHeaderTitle();
        this.mMultipleRequestManager = new MultipleRequestManager(2, new MultipleRequestManager.OnRequestCompleteListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment.3
            @Override // com.microsoft.kapp.utils.MultipleRequestManager.OnRequestCompleteListener
            public void requestComplete(LoadStatus status) {
                GuidedWorkoutFitnessSelectionResultPageFragment.this.updatePanelVisibility(status);
                if (status == LoadStatus.LOADED) {
                    GuidedWorkoutUtils.flagFavoritedWorkoutPlans(GuidedWorkoutFitnessSelectionResultPageFragment.this.mWorkoutPlanSummaryDetailsList, GuidedWorkoutFitnessSelectionResultPageFragment.this.mFavoritePlanIdSet);
                    GuidedWorkoutFitnessSelectionResultPageFragment.this.setListViewAdapter(GuidedWorkoutFitnessSelectionResultPageFragment.this.getActivity(), GuidedWorkoutFitnessSelectionResultPageFragment.this.mWorkoutPlanSummaryDetailsList);
                }
            }
        });
        fetchFavoriteWorkoutPlans(this.mMultipleRequestManager);
        fetchStrengthWorkouts();
        this.mGuidedWorkoutBroadcastReceiver = new GuidedWorkoutBroadcastReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_FAVORITE);
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_UNFAVORITE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mGuidedWorkoutBroadcastReceiver, filter);
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setListViewAdapter(Context context, List<WorkoutPlanSummaryDetails> list) {
        if (context != null && list != null) {
            setResultHeaderText(list.size());
            this.mListView.setVisibility(0);
            this.mAdapter = (GuidedWorkoutPlanBrowseDetailsListAdapter) this.mListView.getAdapter();
            if (this.mAdapter == null) {
                this.mAdapter = new GuidedWorkoutPlanBrowseDetailsListAdapter(context, list);
                this.mListView.setAdapter((ListAdapter) this.mAdapter);
            } else {
                this.mAdapter.clear();
                this.mAdapter.addAll(list);
            }
            if (list.size() == 0) {
                this.mEmptyListText.setVisibility(0);
            } else {
                this.mEmptyListText.setVisibility(4);
            }
            HashMap<String, String> properties = new HashMap<>();
            if (this.mBrandName != null) {
                properties.put(TelemetryConstants.Events.GuidedWorkoutFilterResults.Dimensions.BRAND_CATEGORY_NAME, this.mBrandName);
            }
            if (this.mFilterCriteria != null && this.mFilterCriteria.containsKey("level")) {
                List<String> criteria = this.mFilterCriteria.get("level");
                for (String criterion : criteria) {
                    properties.put("level", criterion);
                }
            }
            properties.put("level", this.mSelectedWorkoutTypeName);
            properties.put(TelemetryConstants.Events.GuidedWorkoutFilterResults.Dimensions.COUNT, String.valueOf(list.size()));
            Telemetry.logEvent(TelemetryConstants.Events.GuidedWorkoutFilterResults.EVENT_NAME, properties, null);
        }
    }

    private void setResultHeaderText(int size) {
        this.mResultText.setText(String.format(getResources().getString(R.string.guided_workout_fitness_plan_result_header), Integer.valueOf(size)));
    }

    protected void BrowseWorkoutPlan(String workoutPlanId) {
        if (workoutPlanId != null) {
            Bundle bundle = new Bundle();
            bundle.putString("workoutPlanId", workoutPlanId);
            ActivityUtils.launchLevelTwoActivityForResult(getActivity(), GuidedWorkoutsBrowsePlanFragment.class, bundle, this, 10002);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startWorkoutPlanFilterActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            setFilterData();
            Intent intent = new Intent(activity, WorkoutPlanFilterActivity.class);
            intent.putExtra(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION, this.mFilterCriteria);
            intent.putExtra(Constants.KEY_GUIDED_WORKOUT_PROVIDER_TYPE, WorkoutProviderType.Provider);
            startActivityForResult(intent, 10001);
        }
    }

    private void setFilterData() {
        if (this.mFilterCriteria == null) {
            this.mFilterCriteria = new HashMap<>();
            Map<String, String> filterCriterias = this.mActivityDataController.getFitnessPlanFilterSelection();
            if (filterCriterias != null) {
                for (Map.Entry<String, String> entry : filterCriterias.entrySet()) {
                    List<String> list = new ArrayList<>();
                    list.add(entry.getValue());
                    this.mFilterCriteria.put(entry.getKey(), list);
                }
            }
        }
    }

    protected void fetchStrengthWorkouts() {
        updatePanelVisibility(LoadStatus.LOADING);
        this.mGuidedWorkoutService.getHnFStrengthWorkoutPlans(this.mFilterString, WorkoutProviderType.Provider, new ActivityScopedCallback(this, new Callback<WorkoutsResponseEnvelope>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment.4
            @Override // com.microsoft.kapp.Callback
            public void callback(WorkoutsResponseEnvelope result) {
                if (result != null) {
                    try {
                        GuidedWorkoutFitnessSelectionResultPageFragment.this.filterWorkoutPlansSummaries(result);
                        GuidedWorkoutFitnessSelectionResultPageFragment.this.updatePanelVisibility(LoadStatus.LOADED);
                    } catch (Exception exception) {
                        KLog.e(GuidedWorkoutFitnessSelectionResultPageFragment.this.TAG, "Exception reading workout response", exception);
                        GuidedWorkoutFitnessSelectionResultPageFragment.this.updatePanelVisibility(LoadStatus.ERROR);
                    }
                }
                GuidedWorkoutFitnessSelectionResultPageFragment.this.mMultipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception exception) {
                GuidedWorkoutFitnessSelectionResultPageFragment.this.updatePanelVisibility(LoadStatus.ERROR);
                GuidedWorkoutFitnessSelectionResultPageFragment.this.mMultipleRequestManager.notifyRequestFailed();
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void filterWorkoutPlansSummaries(WorkoutsResponseEnvelope result) {
        if (this.mPlanDiscoveryType == 0) {
            populateStrengthWorkoutPlansSummaryByType(result);
        } else if (this.mPlanDiscoveryType == 1) {
            populateBrandWorkoutPlansSummary(result);
        }
    }

    private void populateStrengthWorkoutPlansSummaryByType(WorkoutsResponseEnvelope responseEnvelope) {
        if (responseEnvelope == null || responseEnvelope.getResponse() == null || responseEnvelope.getResponse().getWorkoutSummaries() == null) {
            KLog.d(this.TAG, "Corrupt WorkoutsResponseEnvelope for guidedWorkout!");
            return;
        }
        this.mWorkoutPlanSummaryDetailsList = new ArrayList();
        WorkoutSummary[] arr$ = responseEnvelope.getResponse().getWorkoutSummaries();
        for (WorkoutSummary summary : arr$) {
            if (summary != null && isTypeSelected(summary.getDisplaySubType())) {
                WorkoutPlanSummaryDetails workoutPlanSummaryDetails = new WorkoutPlanSummaryDetails(summary);
                if (this.mFavoritePlanIdSet != null && this.mFavoritePlanIdSet.contains(summary.getId())) {
                    workoutPlanSummaryDetails.setIsFavorite(true);
                } else {
                    workoutPlanSummaryDetails.setIsFavorite(false);
                }
                this.mWorkoutPlanSummaryDetailsList.add(workoutPlanSummaryDetails);
            }
        }
    }

    private void populateBrandWorkoutPlansSummary(WorkoutsResponseEnvelope responseEnvelope) {
        if (responseEnvelope == null || responseEnvelope.getResponse() == null || responseEnvelope.getResponse().getWorkoutSummaries() == null) {
            KLog.d(this.TAG, "Corrupt WorkoutsResponseEnvelope for guidedWorkout!");
            return;
        }
        this.mWorkoutPlanSummaryDetailsList = new ArrayList();
        WorkoutSummary[] arr$ = responseEnvelope.getResponse().getWorkoutSummaries();
        for (WorkoutSummary summary : arr$) {
            if (summary != null && isBrandSelected(summary.getBrandName())) {
                WorkoutPlanSummaryDetails workoutPlanSummaryDetails = new WorkoutPlanSummaryDetails(summary);
                if (this.mFavoritePlanIdSet != null && this.mFavoritePlanIdSet.contains(summary.getId())) {
                    workoutPlanSummaryDetails.setIsFavorite(true);
                }
                this.mWorkoutPlanSummaryDetailsList.add(workoutPlanSummaryDetails);
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001 && resultCode == -1 && data != null) {
            this.mFilterCriteria = (HashMap) data.getSerializableExtra(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION);
            this.mFilterCriteria.remove(Constants.FITNESS_PROS);
            this.mFilterString = CommonUtils.calculateHnFFilterString(this.mAllFilterEntriesNamesIdsMapping, this.mFilterCriteria);
            updatePanelVisibility(LoadStatus.LOADING);
            this.mMultipleRequestManager = new MultipleRequestManager(1, new MultipleRequestManager.OnRequestCompleteListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment.5
                @Override // com.microsoft.kapp.utils.MultipleRequestManager.OnRequestCompleteListener
                public void requestComplete(LoadStatus status) {
                    GuidedWorkoutFitnessSelectionResultPageFragment.this.updatePanelVisibility(LoadStatus.LOADED);
                    if (status == LoadStatus.LOADED) {
                        GuidedWorkoutFitnessSelectionResultPageFragment.this.setListViewAdapter(GuidedWorkoutFitnessSelectionResultPageFragment.this.getActivity(), GuidedWorkoutFitnessSelectionResultPageFragment.this.mWorkoutPlanSummaryDetailsList);
                    }
                }
            });
            fetchStrengthWorkouts();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exit() {
        getActivity().finish();
    }

    private void popUpNetworkErrorAndExit() {
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.network_error_loading_data_title), Integer.valueOf((int) R.string.network_error_loading_data), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                GuidedWorkoutFitnessSelectionResultPageFragment.this.exit();
            }
        }, DialogPriority.HIGH);
    }

    private void fetchFavoriteWorkoutPlans(final MultipleRequestManager multipleRequestManager) {
        if (multipleRequestManager == null) {
            KLog.w(this.TAG, "multipleRequestManager is null!");
        }
        this.mGuidedWorkoutService.getFavoriteWorkoutPlans(new ActivityScopedCallback(this, new Callback<List<FavoriteWorkoutPlan>>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment.7
            @Override // com.microsoft.kapp.Callback
            public void callback(List<FavoriteWorkoutPlan> favoriteWorkoutPlans) {
                if (favoriteWorkoutPlans != null) {
                    GuidedWorkoutFitnessSelectionResultPageFragment.this.mFavoritePlanIdSet = GuidedWorkoutUtils.getFavoritedWorkoutPlanIdSet(favoriteWorkoutPlans);
                }
                if (multipleRequestManager != null) {
                    multipleRequestManager.notifyRequestSucceeded();
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GuidedWorkoutFitnessSelectionResultPageFragment.this.TAG, "unable to fetch favorite workoutPlans", ex);
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
        this.mLoadError.setVisibility(8);
        switch (status) {
            case LOADING:
                this.mPageProgressBar.setVisibility(0);
                return;
            case LOADED:
                this.mListView.setVisibility(0);
                return;
            case ERROR:
            case NO_DATA:
                this.mLoadError.setVisibility(0);
                popUpNetworkErrorAndExit();
                return;
            default:
                KLog.e(this.TAG, "Illegal value of LoadStatus has been used!");
                return;
        }
    }

    private boolean isBrandSelected(String brandName) {
        if (Validate.isNotNullNotEmpty(this.mSelectedBrandsFilters)) {
            return this.mSelectedBrandsFilters.contains(brandName);
        }
        return true;
    }

    private boolean isTypeSelected(DisplaySubType workoutSubType) {
        return this.mSelectedWorkoutTypeName != null && this.mSelectedWorkoutTypeName.equals(this.mActivityDataController.getMappingTypeName(workoutSubType));
    }

    private void setHeaderTitle() {
        String firstFilter = "";
        if (this.mPlanDiscoveryType == 0) {
            String selectedWorkoutSubType = this.mActivityDataController.getSelectedType();
            if (selectedWorkoutSubType != null) {
                firstFilter = selectedWorkoutSubType;
            }
            this.mActivityDataController.setHeaderText(firstFilter);
        } else if (this.mPlanDiscoveryType == 1) {
            String firstFilter2 = this.mActivityDataController.getSelectedBrandName();
            this.mActivityDataController.setHeaderText(firstFilter2);
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

    private void fetchFavoriteWorkoutPlans() {
        MultipleRequestManager multipleRequestManager = new MultipleRequestManager(1, new MultipleRequestManager.OnRequestCompleteListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment.8
            @Override // com.microsoft.kapp.utils.MultipleRequestManager.OnRequestCompleteListener
            public void requestComplete(LoadStatus status) {
                GuidedWorkoutUtils.flagFavoritedWorkoutPlans(GuidedWorkoutFitnessSelectionResultPageFragment.this.mWorkoutPlanSummaryDetailsList, GuidedWorkoutFitnessSelectionResultPageFragment.this.mFavoritePlanIdSet);
                GuidedWorkoutFitnessSelectionResultPageFragment.this.mAdapter.notifyDataSetChanged();
            }
        });
        fetchFavoriteWorkoutPlans(multipleRequestManager);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mGuidedWorkoutBroadcastReceiver);
    }
}
