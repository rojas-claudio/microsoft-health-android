package com.microsoft.kapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.FilterFragment;
import com.microsoft.kapp.fragments.FilterSelectionFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.LoadStatus;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.kapp.services.healthandfitness.WorkoutProviderType;
import com.microsoft.kapp.services.healthandfitness.models.HistogramEntry;
import com.microsoft.kapp.services.healthandfitness.models.HistogramValue;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponseEnvelope;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.ViewUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class WorkoutPlanFilterActivity extends BaseFragmentActivity implements FilterActivityInterface {
    private static final String CURRENT_FILTER_SELECTION = "mCurrentFilterSelection";
    private static final String FILTER_STRING = "mFilterString";
    private static final String TAG = WorkoutPlanFilterActivity.class.getSimpleName();
    private HashMap<String, List<String>> mAllFilterEntries;
    private HashMap<String, String> mAllFilterEntriesDisplayNamesMapping;
    private String mCurrentFilterSelection;
    private HashMap<String, List<String>> mFilterCriteria;
    private String mFilterString;
    @Inject
    HealthAndFitnessService mFitnessService;
    private TextView mLoadError;
    private FrameLayout mMainContent;
    private ProgressBar mPageProgressBar;
    private WorkoutProviderType mWorkoutProviderType;

    /* JADX WARN: Multi-variable type inference failed */
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
            this.mFilterCriteria = (HashMap) getIntent().getSerializableExtra(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION);
            this.mAllFilterEntries = (HashMap) getIntent().getSerializableExtra(Constants.KEY_GUIDED_WORKOUT_ALL_FILTERS_KEYS_LIST);
            this.mWorkoutProviderType = (WorkoutProviderType) getIntent().getSerializableExtra(Constants.KEY_GUIDED_WORKOUT_PROVIDER_TYPE);
            HashMap<String, String> allFilterEntriesDisplayNamesMapping = (HashMap) getIntent().getSerializableExtra(Constants.KEY_GUIDED_WORKOUT_FILTERS_NAMES_LIST);
            ArrayList<String> namesMappingOrderedKeys = (ArrayList) getIntent().getSerializableExtra(Constants.KEY_LINKED_HASH_MAP_KEYS_LIST);
            if (namesMappingOrderedKeys != null && allFilterEntriesDisplayNamesMapping != null) {
                this.mAllFilterEntriesDisplayNamesMapping = new LinkedHashMap();
                Iterator i$ = namesMappingOrderedKeys.iterator();
                while (i$.hasNext()) {
                    String filterEntry = i$.next();
                    if (filterEntry != null) {
                        this.mAllFilterEntriesDisplayNamesMapping.put(filterEntry, allFilterEntriesDisplayNamesMapping.get(filterEntry));
                    }
                }
            }
            if (this.mAllFilterEntries != null) {
                updatePanelVisibility(LoadStatus.LOADED);
                startFragment(FilterFragment.newInstance(getString(R.string.workout_filter_title_workout)), R.id.main_content, false);
                return;
            }
            updatePanelVisibility(LoadStatus.LOADING);
            fetchAllHnFWorkoutsFiltersEntries();
        } else if (this.mAllFilterEntries == null) {
            updatePanelVisibility(LoadStatus.LOADING);
            fetchAllHnFWorkoutsFiltersEntries();
        }
    }

    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GUIDED_WORKOUTS_FILTERS_SUMMARY);
    }

    private void fetchAllHnFWorkoutsFiltersEntries() {
        this.mFitnessService.getHnFStrengthWorkoutPlans(this.mWorkoutProviderType, new ActivityScopedCallback(this, new Callback<WorkoutsResponseEnvelope>() { // from class: com.microsoft.kapp.activities.WorkoutPlanFilterActivity.1
            @Override // com.microsoft.kapp.Callback
            public void callback(WorkoutsResponseEnvelope result) {
                WorkoutPlanFilterActivity.this.populateFilterList(result);
                WorkoutPlanFilterActivity.this.startFragment(FilterFragment.newInstance(WorkoutPlanFilterActivity.this.getString(R.string.workout_filter_title_workout)), R.id.main_content, false);
                WorkoutPlanFilterActivity.this.updatePanelVisibility(LoadStatus.LOADED);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception exception) {
                KLog.w(WorkoutPlanFilterActivity.TAG, "Error Fetching HnF workourPlans!", exception);
                WorkoutPlanFilterActivity.this.updatePanelVisibility(LoadStatus.ERROR);
            }
        }));
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public Set<String> getAllFiltersDisplayNames() {
        if (this.mAllFilterEntriesDisplayNamesMapping == null) {
            return null;
        }
        return this.mAllFilterEntriesDisplayNamesMapping.keySet();
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public String getFilterName(String filterDisplayName) {
        if (this.mAllFilterEntriesDisplayNamesMapping == null) {
            return null;
        }
        return this.mAllFilterEntriesDisplayNamesMapping.get(filterDisplayName);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void startFragment(Fragment fragment, int container, boolean addtoBackSTack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(container, fragment);
        if (addtoBackSTack) {
            ft.addToBackStack(null);
        }
        try {
            ft.commit();
        } catch (IllegalStateException ex) {
            KLog.w(TAG, "Error during fragment transaction", ex);
        }
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public String getCurrentFilterSelection() {
        return this.mCurrentFilterSelection;
    }

    @Override // com.microsoft.kapp.activities.FilterActivityInterface
    public List<String> getCurrentFilterValuesList() {
        if (this.mAllFilterEntries == null || this.mAllFilterEntriesDisplayNamesMapping == null) {
            return null;
        }
        return this.mAllFilterEntries.get(this.mAllFilterEntriesDisplayNamesMapping.get(this.mCurrentFilterSelection));
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
        if (this.mAllFilterEntriesDisplayNamesMapping != null) {
            if (this.mFilterCriteria == null) {
                this.mFilterCriteria = new HashMap<>();
            }
            this.mFilterCriteria.put(this.mAllFilterEntriesDisplayNamesMapping.get(this.mCurrentFilterSelection), selectedCriteria);
        }
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
            data.putExtra(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION, this.mFilterCriteria);
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
        outState.putBundle(Constants.KEY_GUIDED_WORKOUT_ALL_FILTERS_KEYS_LIST, CommonUtils.convertListMapToBundle(this.mAllFilterEntries));
        outState.putBundle(Constants.KEY_GUIDED_WORKOUT_FILTERS_NAMES_LIST, CommonUtils.convertStringMapToBundle(this.mAllFilterEntriesDisplayNamesMapping));
        if (this.mAllFilterEntriesDisplayNamesMapping != null) {
            outState.putStringArrayList(Constants.KEY_LINKED_HASH_MAP_KEYS_LIST, new ArrayList<>(this.mAllFilterEntriesDisplayNamesMapping.keySet()));
        }
        outState.putString(CURRENT_FILTER_SELECTION, this.mCurrentFilterSelection);
        outState.putBundle(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION, CommonUtils.convertListMapToBundle(this.mFilterCriteria));
        outState.putString(FILTER_STRING, this.mFilterString);
        outState.putSerializable(Constants.KEY_GUIDED_WORKOUT_PROVIDER_TYPE, this.mWorkoutProviderType);
    }

    private void restoreSavedData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bundle allFiltersBundle = savedInstanceState.getBundle(Constants.KEY_GUIDED_WORKOUT_ALL_FILTERS_KEYS_LIST);
            this.mAllFilterEntries = CommonUtils.extractStringListMapFromBundle(allFiltersBundle);
            Bundle allFilterDisplayNamesBundle = savedInstanceState.getBundle(Constants.KEY_GUIDED_WORKOUT_FILTERS_NAMES_LIST);
            HashMap<String, String> allFilterEntriesDisplayNamesMapping = CommonUtils.extractStringMapFromBundle(allFilterDisplayNamesBundle);
            ArrayList<String> namesMapping = savedInstanceState.getStringArrayList(Constants.KEY_LINKED_HASH_MAP_KEYS_LIST);
            if (allFilterEntriesDisplayNamesMapping != null && namesMapping != null) {
                this.mAllFilterEntriesDisplayNamesMapping = new LinkedHashMap();
                Iterator i$ = namesMapping.iterator();
                while (i$.hasNext()) {
                    String filterEntry = i$.next();
                    if (filterEntry != null) {
                        this.mAllFilterEntriesDisplayNamesMapping.put(filterEntry, allFilterEntriesDisplayNamesMapping.get(filterEntry));
                    }
                }
            }
            this.mCurrentFilterSelection = savedInstanceState.getString(CURRENT_FILTER_SELECTION);
            Bundle filterCriteriaBundle = savedInstanceState.getBundle(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION);
            this.mFilterCriteria = CommonUtils.extractStringListMapFromBundle(filterCriteriaBundle);
            this.mFilterString = savedInstanceState.getString(FILTER_STRING);
            this.mWorkoutProviderType = (WorkoutProviderType) savedInstanceState.getSerializable(Constants.KEY_GUIDED_WORKOUT_PROVIDER_TYPE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populateFilterList(WorkoutsResponseEnvelope responseEnvelope) {
        if (responseEnvelope == null) {
            popUpNetworkErrorAndExit();
            return;
        }
        this.mAllFilterEntries = new HashMap<>();
        this.mAllFilterEntriesDisplayNamesMapping = new LinkedHashMap();
        HistogramEntry[] histogramEntries = responseEnvelope.getHistogramEntries();
        if (histogramEntries == null) {
            popUpNetworkErrorAndExit();
            return;
        }
        for (HistogramEntry entry : histogramEntries) {
            if (entry == null) {
                KLog.d(TAG, "HistogramEntry cannot be null!");
            } else {
                String filter = entry.getFilter();
                if (GuidedWorkoutUtils.showFilter(filter)) {
                    List<String> filterValues = new ArrayList<>();
                    HistogramValue[] arr$ = entry.getValues();
                    for (HistogramValue value : arr$) {
                        if (value == null) {
                            KLog.d(TAG, "HistogramValue cannot be null!");
                        } else {
                            filterValues.add(value.getName());
                        }
                    }
                    this.mAllFilterEntries.put(filter, filterValues);
                    this.mAllFilterEntriesDisplayNamesMapping.put(entry.getDisplay(), filter);
                }
            }
        }
    }

    private void popUpNetworkErrorAndExit() {
        getDialogManager().showDialog(this, Integer.valueOf((int) R.string.network_error_loading_data_title_locked), Integer.valueOf((int) R.string.network_error_loading_data_locked), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.activities.WorkoutPlanFilterActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                WorkoutPlanFilterActivity.this.exit();
            }
        }, DialogPriority.HIGH);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exit() {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePanelVisibility(LoadStatus status) {
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
                popUpNetworkErrorAndExit();
                return;
            default:
                KLog.e(TAG, "Illegal value of LoadStatus has been used!");
                return;
        }
    }
}
