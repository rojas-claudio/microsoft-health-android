package com.microsoft.kapp.fragments.guidedworkout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivityController;
import com.microsoft.kapp.adapters.GuidedWorkoutFitnessBrandsPageAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
/* loaded from: classes.dex */
public class GuidedWorkoutFitnessBrandsFragment extends GuidedWorkoutFitnessBaseFragment {
    List<String> mBrandsNamesList = new ArrayList();
    List<String> mBrandsLogoList = new ArrayList();

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.top_menu_color));
        return view;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment
    int getPageHeaderTextResId() {
        return R.string.select_a_fitness_pro;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment
    ListAdapter getPageListAdapter(Context context, List<String> list) {
        return list == null ? new GuidedWorkoutFitnessBrandsPageAdapter(context, new ArrayList(), new ArrayList()) : new GuidedWorkoutFitnessBrandsPageAdapter(context, list, this.mBrandsNamesList);
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment
    int getPageId() {
        return 4;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment
    void setFilterValue(WorkoutPlanDiscoveryActivityController controller, String value, int position) {
        if (controller != null) {
            controller.setSelectedBrandName(this.mBrandsNamesList.get(position));
        }
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment
    List<String> getDataList(WorkoutPlanDiscoveryActivityController controller) {
        if (controller == null) {
            return null;
        }
        Map<String, String> brandsMap = controller.getAllHnFBrands();
        this.mBrandsNamesList.clear();
        this.mBrandsLogoList.clear();
        if (brandsMap != null) {
            SortedSet<String> sortedBrandsNames = new TreeSet<>(brandsMap.keySet());
            String[] PREDEFINED_BRANDS_SORTED_LIST = getResources().getStringArray(R.array.guided_workout_brands_predefined_ordered_list);
            for (String brandName : PREDEFINED_BRANDS_SORTED_LIST) {
                if (sortedBrandsNames.contains(brandName)) {
                    this.mBrandsNamesList.add(brandName);
                    this.mBrandsLogoList.add(brandsMap.get(brandName));
                    sortedBrandsNames.remove(brandName);
                }
            }
            for (String brandName2 : sortedBrandsNames) {
                this.mBrandsNamesList.add(brandName2);
                this.mBrandsLogoList.add(brandsMap.get(brandName2));
            }
        }
        return this.mBrandsLogoList;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GUIDED_WORKOUTS_BROWSE_FITNESS_PROS);
    }
}
