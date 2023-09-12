package com.microsoft.kapp.fragments.guidedworkout;

import android.content.Context;
import android.widget.ListAdapter;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController;
import com.microsoft.kapp.adapters.GuidedWorkoutFitnessSelectionPageBaseAdapter;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class GuidedWorkoutTypesFragment extends GuidedWorkoutFitnessBaseFragment {
    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment
    int getPageHeaderTextResId() {
        return R.string.select_a_type;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment
    ListAdapter getPageListAdapter(Context context, List<String> list) {
        return list == null ? new GuidedWorkoutFitnessSelectionPageBaseAdapter(context, new ArrayList()) : new GuidedWorkoutFitnessSelectionPageBaseAdapter(context, list);
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment
    int getPageId() {
        return 5;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment
    void setFilterValue(WorkoutPlanDiscoveryActivityController controller, String value, int position) {
        if (controller != null) {
            controller.setSelectedTypeName(value);
        }
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment
    List<String> getDataList(WorkoutPlanDiscoveryActivityController controller) {
        if (controller == null) {
            return null;
        }
        return controller.getAllHnFTypes();
    }
}
