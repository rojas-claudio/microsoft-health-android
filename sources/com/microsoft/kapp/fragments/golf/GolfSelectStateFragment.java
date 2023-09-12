package com.microsoft.kapp.fragments.golf;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListAdapter;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.GuidedWorkoutFitnessSelectionPageBaseAdapter;
import com.microsoft.kapp.models.golf.GolfRegion;
import com.microsoft.kapp.models.golf.GolfStateResponse;
import com.microsoft.kapp.services.golf.GolfFindCourseByRegionListener;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class GolfSelectStateFragment extends GolfFindCourseBaseFragment {
    private static final String GOLF_SELECTED_REGION = "GolfSelectedRegion";
    private static final String GOLF_STATES = "GolfStates";
    private GolfStateResponse mGolfStates;
    private GolfFindCourseByRegionListener mListener;
    private ArrayList<String> mRegionList = new ArrayList<>();
    private GolfRegion mSlectedRegion;

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof GolfFindCourseByRegionListener) {
            setListener((GolfFindCourseByRegionListener) activity);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mListener != null) {
            this.mListener.setPageTitle(getResources().getString(getPageHeaderTextResId()));
        }
        if (this.mRegionList != null && this.mRegionList.size() != 0) {
            this.mNoResults.setVisibility(8);
        } else {
            this.mNoResults.setVisibility(0);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(GOLF_STATES, this.mGolfStates);
        outState.putParcelable(GOLF_SELECTED_REGION, this.mSlectedRegion);
    }

    @Override // android.support.v4.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfFindCourseBaseFragment
    void restoreSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.mGolfStates = (GolfStateResponse) savedInstanceState.getParcelable(GOLF_STATES);
            this.mSlectedRegion = (GolfRegion) savedInstanceState.getParcelable(GOLF_SELECTED_REGION);
        }
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfFindCourseBaseFragment
    public int getPageHeaderTextResId() {
        return R.string.golf_find_by_state_title;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfFindCourseBaseFragment
    ListAdapter getPageListAdapter(Context context) {
        if (this.mRegionList == null || this.mRegionList.size() == 0) {
            setGolfRegionList();
        }
        return new GuidedWorkoutFitnessSelectionPageBaseAdapter(context, this.mRegionList);
    }

    public void setListener(GolfFindCourseByRegionListener listener) {
        this.mListener = listener;
    }

    public void setAvailableStates(GolfStateResponse result) {
        if (result != null && result.getStates().length != 0) {
            this.mGolfStates = result;
        }
    }

    private void setGolfRegionList() {
        if (this.mGolfStates != null && this.mGolfStates.getStates().length > 0) {
            GolfRegion[] arr$ = this.mGolfStates.getStates();
            for (GolfRegion entry : arr$) {
                if (entry != null) {
                    this.mRegionList.add(entry.getRegionName());
                }
            }
        }
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfFindCourseBaseFragment
    void onListItemClicked(int position) {
        if (this.mListener != null) {
            this.mSlectedRegion = this.mGolfStates.getStates()[position];
            this.mListener.onStateSelected(this.mSlectedRegion);
        }
    }
}
