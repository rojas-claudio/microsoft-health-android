package com.microsoft.kapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBrandsFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutTypesFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.LoadStatus;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.WorkoutProviderType;
import com.microsoft.kapp.services.healthandfitness.models.HistogramEntry;
import com.microsoft.kapp.services.healthandfitness.models.HistogramValue;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponse;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponseEnvelope;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.models.DisplaySubType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class WorkoutPlanDiscoveryActivity extends BaseFragmentActivityWithOfflineSupport implements WorkoutPlanDiscoveryActivityController {
    private static final String TAG = WorkoutPlanDiscoveryActivity.class.getSimpleName();
    private static List<DisplaySubType> mDisplayTypes = Arrays.asList(DisplaySubType.RUNNING, DisplaySubType.RUNNING_ONEOFF, DisplaySubType.RUNNING_MULTIWEEK, DisplaySubType.BIKING, DisplaySubType.BIKING_ONEOFF, DisplaySubType.BIKING_MULTIWEEK, DisplaySubType.BODYWEIGHT, DisplaySubType.BODYWEIGHT_ONEOFF, DisplaySubType.BODYWEIGHT_MULTIWEEK, DisplaySubType.STRENGTH, DisplaySubType.STRENGTH_ONEOFF, DisplaySubType.STRENGTH_MULTIWEEK, DisplaySubType.HIKING, DisplaySubType.HIKING_ONEOFF, DisplaySubType.HIKING_MULTIWEEK, DisplaySubType.SWIMMING, DisplaySubType.SWIMMING_ONEOFF, DisplaySubType.SWIMMING_MULTIWEEK, DisplaySubType.GOLFING, DisplaySubType.GOLFING_ONEOFF, DisplaySubType.GOLFING_MULTIWEEK, DisplaySubType.SKIING, DisplaySubType.SKIING_ONEOFF, DisplaySubType.SKIING_MULTIWEEK, DisplaySubType.SLEEPING, DisplaySubType.SLEEPING_ONEOFF, DisplaySubType.SLEEPING_MULTIWEEK, DisplaySubType.YOGA, DisplaySubType.YOGA_ONEOFF, DisplaySubType.YOGA_MULTIWEEK, DisplaySubType.PILATES, DisplaySubType.PILATES_ONEOFF, DisplaySubType.PILATES_MULTIWEEK, DisplaySubType.TENNIS, DisplaySubType.TENNIS_ONEOFF, DisplaySubType.TENNIS_MULTIWEEK, DisplaySubType.TRIATHLON, DisplaySubType.TRIATHLON_ONEOFF, DisplaySubType.TRIATHLON_MULTIWEEK, DisplaySubType.SOCCER, DisplaySubType.SOCCER_ONEOFF, DisplaySubType.SOCCER_MULTIWEEK, DisplaySubType.FOOTBALL, DisplaySubType.FOOTBALL_ONEOFF, DisplaySubType.FOOTBALL_MULTIWEEK, DisplaySubType.BASEBALL, DisplaySubType.BASEBALL_ONEOFF, DisplaySubType.BASEBALL_MULTIWEEK, DisplaySubType.BASKETBALL, DisplaySubType.BASKETBALL_ONEOFF, DisplaySubType.BASKETBALL_MULTIWEEK);
    private HashMap<String, List<String>> mAllFilterEntries;
    private HashMap<String, String> mAllFilterEntriesDisplayNamesMapping;
    private HashMap<String, String> mAllFilterEntriesNamesIdsMapping;
    private TextView mBackBotton;
    private Map<String, String> mBrandsListMap;
    private HashMap<String, String> mFilterCriteria;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private TextView mHeaderTileText;
    private ProgressBar mLoadingProgress;
    private FrameLayout mMainContent;
    private int mPlanDiscoveryType;
    private String mSelectedBrandName;
    private String mSelectedTypeName;
    private ArrayList<String> mWorkoutTypesList;

    @Override // com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport
    protected void onCreate(ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreSavedData(savedInstanceState);
        }
        setContentView(R.layout.plan_discovery_activity);
        View view = getWindow().getDecorView();
        this.mMainContent = (FrameLayout) ViewUtils.getValidView(view, R.id.main_content, FrameLayout.class);
        this.mHeaderTileText = (TextView) ViewUtils.getValidView(view, R.id.header_text, TextView.class);
        this.mLoadingProgress = (ProgressBar) ViewUtils.getValidView(view, R.id.loading_progress, ProgressBar.class);
        this.mBackBotton = (TextView) ViewUtils.getValidView(view, R.id.back, TextView.class);
        this.mBackBotton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FragmentManager frgamentManager = WorkoutPlanDiscoveryActivity.this.getSupportFragmentManager();
                if (frgamentManager != null && !frgamentManager.popBackStackImmediate()) {
                    WorkoutPlanDiscoveryActivity.this.finish();
                }
            }
        });
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            boolean isLoadedFromWhatsNew = intent.getBooleanExtra(Constants.LOAD_FROM_WHATSNEW, false);
            if (isLoadedFromWhatsNew) {
                navigateToType((DisplaySubType) intent.getSerializableExtra(Constants.LOAD_FROM_WHATSNEW_TYPE));
                return;
            }
            Bundle bundle = intent.getExtras();
            this.mPlanDiscoveryType = bundle == null ? -1 : bundle.getInt(Constants.GUIDED_WORKOUT_DISCOVERY_PLAN_TYPE, -1);
            fetchAllHnFWorkoutsFiltersEntries();
            return;
        }
        setState(1234);
    }

    private void fetchAllHnFWorkoutsFiltersEntries() {
        updatePanelVisibility(LoadStatus.LOADING);
        this.mGuidedWorkoutService.getHnFStrengthWorkoutPlans(WorkoutProviderType.Provider, new ActivityScopedCallback(this, new Callback<WorkoutsResponseEnvelope>() { // from class: com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivity.2
            @Override // com.microsoft.kapp.Callback
            public void callback(WorkoutsResponseEnvelope result) {
                if (result != null) {
                    WorkoutPlanDiscoveryActivity.this.populateFilterList(result);
                    WorkoutPlanDiscoveryActivity.this.populateWorkoutBrandsAndTypesList(result);
                    WorkoutPlanDiscoveryActivity.this.nextPage(0);
                    WorkoutPlanDiscoveryActivity.this.updatePanelVisibility(LoadStatus.LOADED);
                    return;
                }
                WorkoutPlanDiscoveryActivity.this.updatePanelVisibility(LoadStatus.NO_DATA);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception exception) {
                KLog.w(WorkoutPlanDiscoveryActivity.TAG, "Error Fetching HnF workourPlans!", exception);
                WorkoutPlanDiscoveryActivity.this.updatePanelVisibility(LoadStatus.ERROR);
            }
        }));
    }

    private void navigateToFragment(Fragment fragment, boolean addtoBackSTack, boolean isAnimated) {
        updatePanelVisibility(LoadStatus.LOADED);
        FragmentTransaction ft = ActivityUtils.getFragmentTransaction(this, isAnimated);
        ft.replace(R.id.main_content, fragment);
        if (addtoBackSTack && ft.isAddToBackStackAllowed()) {
            ft.addToBackStack(null);
        }
        try {
            ft.commit();
        } catch (IllegalStateException ex) {
            KLog.w(TAG, "Error during fragment transaction: %s", ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.KEY_GUIDED_WORKOUT_PLAN_DISCOVERY_TYPE, this.mPlanDiscoveryType);
        outState.putString(Constants.KEY_GUIDED_WORKOUT_SELECTED_BRAND, this.mSelectedBrandName);
        outState.putString(Constants.KEY_GUIDED_WORKOUT_SELECTED_TYPE, this.mSelectedTypeName);
        outState.putBundle(Constants.KEY_GUIDED_WORKOUT_ALL_FILTERS_KEYS_LIST, CommonUtils.convertListMapToBundle(this.mAllFilterEntries));
        outState.putBundle(Constants.KEY_GUIDED_WORKOUT_ALL_FILTERS_ID_LIST, CommonUtils.convertStringMapToBundle(this.mAllFilterEntriesNamesIdsMapping));
        outState.putBundle(Constants.KEY_GUIDED_WORKOUT_FILTERS_NAMES_LIST, CommonUtils.convertStringMapToBundle(this.mAllFilterEntriesDisplayNamesMapping));
        if (this.mAllFilterEntriesDisplayNamesMapping != null) {
            outState.putStringArrayList(Constants.KEY_LINKED_HASH_MAP_KEYS_LIST, new ArrayList<>(this.mAllFilterEntriesDisplayNamesMapping.keySet()));
        }
        outState.putBundle(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION, CommonUtils.convertStringMapToBundle(this.mFilterCriteria));
        outState.putBundle(Constants.KEY_GUIDED_WORKOUT_BRANDS_LIST, CommonUtils.convertStringMapToBundle(this.mBrandsListMap));
        outState.putStringArrayList(Constants.KEY_GUIDED_WORKOUT_SUB_TYPES_LIST, this.mWorkoutTypesList);
    }

    private void restoreSavedData(Bundle savedData) {
        if (savedData != null) {
            this.mPlanDiscoveryType = savedData.getInt(Constants.KEY_GUIDED_WORKOUT_PLAN_DISCOVERY_TYPE);
            this.mSelectedBrandName = savedData.getString(Constants.KEY_GUIDED_WORKOUT_SELECTED_BRAND);
            this.mSelectedTypeName = savedData.getString(Constants.KEY_GUIDED_WORKOUT_SELECTED_TYPE);
            Bundle allFiltersBundle = savedData.getBundle(Constants.KEY_GUIDED_WORKOUT_ALL_FILTERS_KEYS_LIST);
            this.mAllFilterEntries = CommonUtils.extractStringListMapFromBundle(allFiltersBundle);
            Bundle allFilterEntriesNamesIds = savedData.getBundle(Constants.KEY_GUIDED_WORKOUT_ALL_FILTERS_ID_LIST);
            this.mAllFilterEntriesNamesIdsMapping = CommonUtils.extractStringMapFromBundle(allFilterEntriesNamesIds);
            Bundle allFilterDisplayNamesBundle = savedData.getBundle(Constants.KEY_GUIDED_WORKOUT_FILTERS_NAMES_LIST);
            HashMap<String, String> allFilterEntriesDisplayNamesMapping = CommonUtils.extractStringMapFromBundle(allFilterDisplayNamesBundle);
            ArrayList<String> namesMapping = savedData.getStringArrayList(Constants.KEY_LINKED_HASH_MAP_KEYS_LIST);
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
            Bundle filterCriteriaBundle = savedData.getBundle(Constants.KEY_GUIDED_WORKOUT_FILTERS_SELECTION);
            this.mFilterCriteria = CommonUtils.extractStringMapFromBundle(filterCriteriaBundle);
            Bundle brandsListBundle = savedData.getBundle(Constants.KEY_GUIDED_WORKOUT_BRANDS_LIST);
            this.mBrandsListMap = CommonUtils.extractStringMapFromBundle(brandsListBundle);
            this.mWorkoutTypesList = savedData.getStringArrayList(Constants.KEY_GUIDED_WORKOUT_SUB_TYPES_LIST);
        }
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
                        KLog.d(TAG, "HistogramEntry cannot be null!");
                    } else {
                        List<String> filterValues = new ArrayList<>();
                        HistogramValue[] arr$ = entry.getValues();
                        for (HistogramValue value : arr$) {
                            if (value == null) {
                                KLog.d(TAG, "HistogramValue cannot be null!");
                            } else {
                                String valueName = value.getName();
                                if (valueName != null) {
                                    filterValues.add(valueName);
                                    this.mAllFilterEntriesNamesIdsMapping.put(valueName, value.getId());
                                }
                            }
                        }
                        this.mAllFilterEntries.put(entry.getFilter(), filterValues);
                        this.mAllFilterEntriesDisplayNamesMapping.put(entry.getDisplay(), entry.getFilter());
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePanelVisibility(LoadStatus status) {
        this.mLoadingProgress.setVisibility(8);
        this.mMainContent.setVisibility(8);
        switch (status) {
            case LOADING:
                this.mLoadingProgress.setVisibility(0);
                return;
            case LOADED:
                setState(1234);
                this.mMainContent.setVisibility(0);
                return;
            case ERROR:
            case NO_DATA:
                setState(1235);
                return;
            default:
                KLog.e(TAG, "Illegal value of LoadStatus has been used!");
                return;
        }
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public void setHeaderText(int textResId) {
        this.mHeaderTileText.setText(textResId);
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public void setHeaderText(String text) {
        this.mHeaderTileText.setText(text);
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public void nextPage(int pageId) {
        switch (pageId) {
            case 0:
                if (this.mPlanDiscoveryType == 0) {
                    navigateToFragment(new GuidedWorkoutTypesFragment(), false, false);
                    return;
                } else if (this.mPlanDiscoveryType == 1) {
                    navigateToFragment(new GuidedWorkoutFitnessBrandsFragment(), false, false);
                    return;
                } else {
                    return;
                }
            case 1:
            case 2:
            case 3:
            default:
                KLog.e(TAG, "Unknown command for next page!");
                return;
            case 4:
            case 5:
                Fragment resultFragment = new GuidedWorkoutFitnessSelectionResultPageFragment();
                Bundle data = CommonUtils.convertStringMapToBundle(this.mFilterCriteria);
                resultFragment.setArguments(data);
                navigateToFragment(resultFragment, true, true);
                return;
        }
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public Map<String, String> getFitnessPlanFilterSelection() {
        return this.mFilterCriteria;
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public Map<String, String> getAllFiltersNamesIdsMapping() {
        return this.mAllFilterEntriesNamesIdsMapping;
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public String getSelectedBrandName() {
        return this.mSelectedBrandName;
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public void setSelectedBrandName(String brand) {
        this.mSelectedBrandName = brand;
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public Map<String, String> getAllHnFBrands() {
        return this.mBrandsListMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populateWorkoutBrandsAndTypesList(WorkoutsResponseEnvelope responseEnvelope) {
        String brandLogo;
        if (responseEnvelope == null) {
            KLog.d(TAG, "WorkoutsResponseEnvelope for HnF workouts should not be null!");
            return;
        }
        WorkoutsResponse response = responseEnvelope.getResponse();
        if (response == null) {
            KLog.d(TAG, "WorkoutsResponse for HnF workouts should not be null!");
            return;
        }
        this.mBrandsListMap = new HashMap();
        this.mWorkoutTypesList = new ArrayList<>();
        WorkoutSummary[] arr$ = response.getWorkoutSummaries();
        for (WorkoutSummary workoutSummary : arr$) {
            if (workoutSummary == null) {
                KLog.d(TAG, "WorkoutSummary for HnF workouts should not be null!");
            } else {
                DisplaySubType workoutPlanSubType = workoutSummary.getDisplaySubType();
                if (mDisplayTypes.contains(workoutPlanSubType)) {
                    String mappedTypeName = getMappingTypeName(workoutPlanSubType);
                    if (!this.mWorkoutTypesList.contains(mappedTypeName)) {
                        this.mWorkoutTypesList.add(mappedTypeName);
                    }
                }
                String brandName = workoutSummary.getBrandName();
                if (brandName != null && (brandLogo = workoutSummary.getBrandLogo()) != null) {
                    this.mBrandsListMap.put(brandName, brandLogo);
                }
            }
        }
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public int getPlanType() {
        return this.mPlanDiscoveryType;
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public String getSelectedType() {
        return this.mSelectedTypeName;
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public void setSelectedTypeName(String type) {
        this.mSelectedTypeName = type;
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public List<String> getAllHnFTypes() {
        return this.mWorkoutTypesList;
    }

    @Override // com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController
    public String getMappingTypeName(DisplaySubType subType) {
        int typeId;
        if (subType == null) {
            return null;
        }
        switch (subType) {
            case RUNNING:
            case RUNNING_ONEOFF:
            case RUNNING_MULTIWEEK:
                typeId = R.string.guided_workout_type_running_name;
                break;
            case BIKING:
            case BIKING_ONEOFF:
            case BIKING_MULTIWEEK:
                typeId = R.string.guided_workout_type_biking_name;
                break;
            case BODYWEIGHT:
            case BODYWEIGHT_ONEOFF:
            case BODYWEIGHT_MULTIWEEK:
                typeId = R.string.guided_workout_type_body_weight_name;
                break;
            case STRENGTH:
            case STRENGTH_ONEOFF:
            case STRENGTH_MULTIWEEK:
                typeId = R.string.guided_workout_type_strength_name;
                break;
            case HIKING:
            case HIKING_ONEOFF:
            case HIKING_MULTIWEEK:
                typeId = R.string.guided_workout_type_hiking_name;
                break;
            case SWIMMING:
            case SWIMMING_ONEOFF:
            case SWIMMING_MULTIWEEK:
                typeId = R.string.guided_workout_type_swimming_name;
                break;
            case GOLFING:
            case GOLFING_ONEOFF:
            case GOLFING_MULTIWEEK:
                typeId = R.string.guided_workout_type_golfing_name;
                break;
            case SKIING:
            case SKIING_ONEOFF:
            case SKIING_MULTIWEEK:
                typeId = R.string.guided_workout_type_skiing_name;
                break;
            case SLEEPING:
            case SLEEPING_ONEOFF:
            case SLEEPING_MULTIWEEK:
                typeId = R.string.guided_workout_type_sleeping_name;
                break;
            case YOGA:
            case YOGA_ONEOFF:
            case YOGA_MULTIWEEK:
                typeId = R.string.guided_workout_type_yoga_name;
                break;
            case PILATES:
            case PILATES_ONEOFF:
            case PILATES_MULTIWEEK:
                typeId = R.string.guided_workout_type_pilates_name;
                break;
            case TENNIS:
            case TENNIS_ONEOFF:
            case TENNIS_MULTIWEEK:
                typeId = R.string.guided_workout_type_tennis_name;
                break;
            case TRIATHLON:
            case TRIATHLON_ONEOFF:
            case TRIATHLON_MULTIWEEK:
                typeId = R.string.guided_workout_type_triathlon_name;
                break;
            case SOCCER:
            case SOCCER_ONEOFF:
            case SOCCER_MULTIWEEK:
                typeId = R.string.guided_workout_type_soccer_name;
                break;
            case FOOTBALL:
            case FOOTBALL_ONEOFF:
            case FOOTBALL_MULTIWEEK:
                typeId = R.string.guided_workout_type_football_name;
                break;
            case BASEBALL:
            case BASEBALL_ONEOFF:
            case BASEBALL_MULTIWEEK:
                typeId = R.string.guided_workout_type_baseball_name;
                break;
            case BASKETBALL:
            case BASKETBALL_ONEOFF:
            case BASKETBALL_MULTIWEEK:
                typeId = R.string.guided_workout_type_basketball_name;
                break;
            default:
                return "";
        }
        switch (subType) {
            case RUNNING_ONEOFF:
            case BIKING_ONEOFF:
            case BODYWEIGHT_ONEOFF:
            case STRENGTH_ONEOFF:
            case HIKING_ONEOFF:
            case SWIMMING_ONEOFF:
            case GOLFING_ONEOFF:
            case SKIING_ONEOFF:
            case SLEEPING_ONEOFF:
            case YOGA_ONEOFF:
            case PILATES_ONEOFF:
            case TENNIS_ONEOFF:
            case TRIATHLON_ONEOFF:
            case SOCCER_ONEOFF:
            case FOOTBALL_ONEOFF:
            case BASEBALL_ONEOFF:
            case BASKETBALL_ONEOFF:
                return Formatter.formatGuidedWorkoutDisplayType(this, typeId, R.string.guided_workout_type_one_off);
            case RUNNING_MULTIWEEK:
            case BIKING_MULTIWEEK:
            case BODYWEIGHT_MULTIWEEK:
            case STRENGTH_MULTIWEEK:
            case HIKING_MULTIWEEK:
            case SWIMMING_MULTIWEEK:
            case GOLFING_MULTIWEEK:
            case SKIING_MULTIWEEK:
            case SLEEPING_MULTIWEEK:
            case YOGA_MULTIWEEK:
            case PILATES_MULTIWEEK:
            case TENNIS_MULTIWEEK:
            case TRIATHLON_MULTIWEEK:
            case SOCCER_MULTIWEEK:
            case FOOTBALL_MULTIWEEK:
            case BASEBALL_MULTIWEEK:
            case BASKETBALL_MULTIWEEK:
                return Formatter.formatGuidedWorkoutDisplayType(this, typeId, R.string.guided_workout_type_multi_week);
            case BIKING:
            case BODYWEIGHT:
            case STRENGTH:
            case HIKING:
            case SWIMMING:
            case GOLFING:
            case SKIING:
            case SLEEPING:
            case YOGA:
            case PILATES:
            case TENNIS:
            case TRIATHLON:
            case SOCCER:
            case FOOTBALL:
            case BASEBALL:
            case BASKETBALL:
            default:
                return getString(typeId);
        }
    }

    private void navigateToType(DisplaySubType type) {
        this.mPlanDiscoveryType = 0;
        setSelectedTypeName(getMappingTypeName(type));
        Fragment resultFragment = new GuidedWorkoutFitnessSelectionResultPageFragment();
        Bundle data = CommonUtils.convertStringMapToBundle(this.mFilterCriteria);
        resultFragment.setArguments(data);
        navigateToFragment(resultFragment, false, true);
        setState(1234);
    }
}
