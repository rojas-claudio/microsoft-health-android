package com.microsoft.kapp.fragments.guidedworkout;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.BaseHomeTileFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.guidedworkout.GuidedWorkoutPostModel;
import com.microsoft.kapp.models.guidedworkout.GuidedWorkoutUnitType;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.WorkoutExerciseSequence;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GuidedWorkoutDetailsFragment extends BaseHomeTileFragment {
    public static final String GUIDEDWORKOUT_EVENT_ID = "guidedworkout_event_id";
    private boolean isUnitSpinnerCalledOnSelected = false;
    private String mEventId;
    private GuidedWorkoutEvent mGuidedWorkoutEvent;
    private GuidedWorkoutPostModel mGuidedWorkoutPostModel;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private boolean mIsSmartCountingWorkout;
    @Inject
    SettingsProvider mSettingsProvider;
    private FrameLayout mTreeContainer;
    private Spinner mUnitSpinner;

    public static BaseFragment newInstance(String eventID, Boolean isL2View) {
        GuidedWorkoutDetailsFragment fragment = new GuidedWorkoutDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GUIDEDWORKOUT_EVENT_ID, eventID);
        bundle.putBoolean(Constants.EVENT_L2_VIEW, isL2View.booleanValue());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle savedBundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        this.mEventId = savedBundle.getString(GUIDEDWORKOUT_EVENT_ID);
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GUIDED_WORKOUTS_DETAILS);
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GUIDEDWORKOUT_EVENT_ID, this.mEventId);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_completed_guidedworkout_details, container, false);
        this.mTreeContainer = (FrameLayout) ViewUtils.getValidView(rootView, R.id.tree_container, FrameLayout.class);
        this.mUnitSpinner = (Spinner) ViewUtils.getValidView(rootView, R.id.guided_workout_post_detail_unit_spinner, Spinner.class);
        ArrayAdapter<CharSequence> weightSpinnerAdapter = createSpinnerAdapterFromStringArray(getActivity(), R.array.post_gw_unit);
        this.mUnitSpinner.setAdapter((SpinnerAdapter) weightSpinnerAdapter);
        this.mUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutDetailsFragment.1
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (GuidedWorkoutDetailsFragment.this.mGuidedWorkoutPostModel != null && GuidedWorkoutDetailsFragment.this.isUnitSpinnerCalledOnSelected) {
                    GuidedWorkoutUnitType guidedWorkoutUnitType = GuidedWorkoutUnitType.valueOf(pos + 1);
                    GuidedWorkoutDetailsFragment.this.mGuidedWorkoutPostModel.updateTreeWithUnitType(guidedWorkoutUnitType);
                }
                GuidedWorkoutDetailsFragment.this.isUnitSpinnerCalledOnSelected = true;
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        return rootView;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment
    public void fetchData() {
        setState(1233);
        this.mGuidedWorkoutService.getGuidedWorkoutEventById(this.mEventId, true, new ActivityScopedCallback(this, new Callback<GuidedWorkoutEvent>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutDetailsFragment.2
            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GuidedWorkoutDetailsFragment.this.TAG, "getting guidedworkout event failed.", ex);
                GuidedWorkoutDetailsFragment.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void callback(GuidedWorkoutEvent result) {
                GuidedWorkoutDetailsFragment.this.mGuidedWorkoutEvent = result;
                if (GuidedWorkoutDetailsFragment.this.mGuidedWorkoutEvent != null) {
                    try {
                        GuidedWorkoutDetailsFragment.this.setState(1234);
                        GuidedWorkoutDetailsFragment.this.loadDataOrShowError();
                    } catch (Exception ex) {
                        KLog.e(GuidedWorkoutDetailsFragment.this.TAG, "exception loading guidedworkout event.", ex);
                        GuidedWorkoutDetailsFragment.this.setState(1235);
                    }
                }
            }
        }));
    }

    protected void loadDataOrShowError() {
        try {
            showPostWorkoutDetailsList();
        } catch (IndexOutOfBoundsException e) {
            showLoadingDataError(R.string.exercise_detail_error);
            setState(1235);
        }
    }

    private void showPostWorkoutDetailsList() {
        Validate.notNull(this.mGuidedWorkoutEvent, "GuidedWorkoutEvent");
        this.mIsSmartCountingWorkout = this.mGuidedWorkoutEvent.getKIsSupportedCounting();
        List<WorkoutExerciseSequence> exerciseList = this.mGuidedWorkoutEvent.getWorkoutExerciseSequences();
        if (exerciseList == null) {
            KLog.e(this.TAG, "WorkoutExerciseSequence list cannot be null in post guidedWorkout!");
            return;
        }
        this.mTreeContainer.setVisibility(8);
        LoadTreeTask loadTreeTask = new LoadTreeTask();
        loadTreeTask.execute(exerciseList);
    }

    private void showLoadingDataError(int errorMessage) {
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.home_error_loading_data_title), Integer.valueOf(errorMessage), DialogPriority.LOW);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addPostWorkoutDetailsToTree(TreeNode root) {
        AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
        this.mTreeContainer.removeAllViews();
        this.mTreeContainer.addView(tView.getView());
        tView.expandAll();
        tView.setDefaultAnimation(true);
    }

    private static ArrayAdapter<CharSequence> createSpinnerAdapterFromStringArray(Context context, int stringArrayResourceId) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context, stringArrayResourceId, R.drawable.guided_workout_post_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.drawable.guided_workout_post_spinner_dropdown_item);
        return spinnerAdapter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class LoadTreeTask extends AsyncTask<List<WorkoutExerciseSequence>, Integer, TreeNode> {
        private LoadTreeTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public TreeNode doInBackground(List<WorkoutExerciseSequence>... params) {
            if (params.length > 0) {
                try {
                    List<WorkoutExerciseSequence> exercies = params[0];
                    GuidedWorkoutDetailsFragment.this.mGuidedWorkoutPostModel = new GuidedWorkoutPostModel(GuidedWorkoutDetailsFragment.this.getActivity(), exercies, GuidedWorkoutDetailsFragment.this.mIsSmartCountingWorkout, GuidedWorkoutDetailsFragment.this.mSettingsProvider.isDistanceHeightMetric());
                    return GuidedWorkoutDetailsFragment.this.mGuidedWorkoutPostModel.generateTree();
                } catch (Exception e) {
                    KLog.e(GuidedWorkoutDetailsFragment.this.TAG, "Load Tree Error: " + e.toString());
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... progress) {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(TreeNode root) {
            if (root != null) {
                try {
                    GuidedWorkoutDetailsFragment.this.mTreeContainer.setVisibility(0);
                    GuidedWorkoutDetailsFragment.this.addPostWorkoutDetailsToTree(root);
                } catch (Exception e) {
                    KLog.e(GuidedWorkoutDetailsFragment.this.TAG, "Load Tree Error: " + e.toString());
                }
            }
        }
    }
}
