package com.microsoft.kapp.fragments.golf;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListAdapter;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.GuidedWorkoutFitnessSelectionPageBaseAdapter;
import com.microsoft.kapp.models.golf.GolfRegion;
import com.microsoft.kapp.models.golf.GolfRegionResponse;
import com.microsoft.kapp.services.golf.GolfFindCourseByRegionListener;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class GolfSelectRegionFragment extends GolfFindCourseBaseFragment {
    private static final String GOLF_REGIONS = "GolfRegions";
    private static final String GOLF_SELECTED_REGION = "GolfSelectedRegions";
    private GolfRegionResponse mGolfRegions;
    private GolfFindCourseByRegionListener mListener;
    private ArrayList<String> mRegionList = new ArrayList<>();
    private GolfRegion mSelectedRegion;

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
        outState.putParcelable(GOLF_REGIONS, this.mGolfRegions);
        outState.putParcelable(GOLF_SELECTED_REGION, this.mSelectedRegion);
    }

    @Override // android.support.v4.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfFindCourseBaseFragment
    public int getPageHeaderTextResId() {
        return R.string.golf_find_by_country_title;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfFindCourseBaseFragment
    ListAdapter getPageListAdapter(Context context) {
        if (this.mRegionList == null || this.mRegionList.size() == 0) {
            setRegionList();
        }
        return new GuidedWorkoutFitnessSelectionPageBaseAdapter(context, this.mRegionList);
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfFindCourseBaseFragment
    void restoreSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.mGolfRegions = (GolfRegionResponse) savedInstanceState.getParcelable(GOLF_REGIONS);
            this.mSelectedRegion = (GolfRegion) savedInstanceState.getParcelable(GOLF_SELECTED_REGION);
        }
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfFindCourseBaseFragment
    void onListItemClicked(int position) {
        if (this.mListener != null) {
            this.mSelectedRegion = this.mGolfRegions.getRegions()[position];
            this.mListener.onRegionSelected(this.mSelectedRegion);
        }
    }

    public void setListener(GolfFindCourseByRegionListener listener) {
        this.mListener = listener;
    }

    public void setAvailableCountries(GolfRegionResponse result) {
        if (result != null && result.getRegions().length != 0) {
            this.mGolfRegions = result;
        }
    }

    private void setRegionList() {
        if (this.mGolfRegions != null && this.mGolfRegions.getRegions().length != 0) {
            GolfRegion[] arr$ = this.mGolfRegions.getRegions();
            for (GolfRegion entry : arr$) {
                if (entry != null) {
                    this.mRegionList.add(entry.getRegionName());
                }
            }
        }
    }
}
