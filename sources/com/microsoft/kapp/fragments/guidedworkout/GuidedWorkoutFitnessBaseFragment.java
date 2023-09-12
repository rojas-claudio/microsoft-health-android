package com.microsoft.kapp.fragments.guidedworkout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public abstract class GuidedWorkoutFitnessBaseFragment extends BaseFragment {
    private static final String HnF_DATA_LIST = "mHnFDataList";
    private WorkoutPlanDiscoveryActivityController mActivityDataController;
    protected List<String> mHnFDataList;
    private ListView mListView;

    abstract List<String> getDataList(WorkoutPlanDiscoveryActivityController workoutPlanDiscoveryActivityController);

    abstract int getPageHeaderTextResId();

    abstract int getPageId();

    abstract ListAdapter getPageListAdapter(Context context, List<String> list);

    abstract void setFilterValue(WorkoutPlanDiscoveryActivityController workoutPlanDiscoveryActivityController, String str, int i);

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.guided_workout_fitness_selection_fragment, container, false);
        this.mListView = (ListView) ViewUtils.getValidView(view, R.id.listview, ListView.class);
        this.mActivityDataController = (WorkoutPlanDiscoveryActivityController) WorkoutPlanDiscoveryActivityController.class.cast(getActivity());
        this.mHnFDataList = getDataList(this.mActivityDataController);
        if (this.mHnFDataList == null) {
            this.mHnFDataList = new ArrayList();
        }
        this.mActivityDataController.setHeaderText(getPageHeaderTextResId());
        this.mListView.setAdapter(getPageListAdapter(getActivity(), this.mHnFDataList));
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                GuidedWorkoutFitnessBaseFragment.this.setFilterValue(GuidedWorkoutFitnessBaseFragment.this.mActivityDataController, GuidedWorkoutFitnessBaseFragment.this.mHnFDataList.get(position), position);
                GuidedWorkoutFitnessBaseFragment.this.mActivityDataController.nextPage(GuidedWorkoutFitnessBaseFragment.this.getPageId());
            }
        });
        return view;
    }
}
