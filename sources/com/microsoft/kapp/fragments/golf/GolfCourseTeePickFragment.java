package com.microsoft.kapp.fragments.golf;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.LevelTwoBaseActivity;
import com.microsoft.kapp.adapters.FiltersListAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.models.golf.GolfTeePair;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.FilterHeaderView;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class GolfCourseTeePickFragment extends BaseFragmentWithOfflineSupport {
    public static String GOLF_TEE_PAIR = "Golf_Tee_Pair";
    public static String TEE_ID = "Tee_Id";
    private FiltersListAdapter<String> mAdapter;
    private View mChildView;
    private String mCourseId;
    private FilterHeaderView mFilterHeader;
    private ListView mFilterListView;
    private List<GolfTeePair> mGolfTeePairList;

    public static BaseFragment newInstance(List<GolfTeePair> golfTeePair, String courseId) {
        GolfCourseTeePickFragment fragment = new GolfCourseTeePickFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(GOLF_TEE_PAIR, (ArrayList) golfTeePair);
        bundle.putString(GolfCourseDetailsFragment.COURSE_ID, courseId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mChildView = inflater.inflate(R.layout.golf_course_tee_pick_fragment, container, false);
        Bundle bundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        this.mGolfTeePairList = bundle.getParcelableArrayList(GOLF_TEE_PAIR);
        this.mCourseId = bundle.getString(GolfCourseDetailsFragment.COURSE_ID);
        setViewControls();
        setTeeList();
        wireEventHandlers();
        setState(1234);
        return this.mChildView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GOLF_COURSE_TEE_PICK);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(GOLF_TEE_PAIR, (ArrayList) this.mGolfTeePairList);
        outState.putString(GolfCourseDetailsFragment.COURSE_ID, this.mCourseId);
    }

    private void setViewControls() {
        this.mFilterListView = (ListView) ViewUtils.getValidView(this.mChildView, R.id.golf_tee_list, ListView.class);
        this.mFilterHeader = (FilterHeaderView) ViewUtils.getValidView(this.mChildView, R.id.golf_tee_filter_header, FilterHeaderView.class);
    }

    private void setTeeList() {
        ArrayList<Pair<String, String>> filters = (ArrayList) this.mGolfTeePairList;
        this.mAdapter = new FiltersListAdapter<>(getActivity(), filters);
        this.mFilterListView.setAdapter((ListAdapter) this.mAdapter);
    }

    private void wireEventHandlers() {
        this.mFilterHeader.setCloseOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfCourseTeePickFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GolfCourseTeePickFragment.this.getActivity().finish();
            }
        });
        this.mFilterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfCourseTeePickFragment.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str = (String) GolfCourseTeePickFragment.this.mAdapter.getItem(i).first;
                String teeId = (String) GolfCourseTeePickFragment.this.mAdapter.getItem(i).second;
                LevelTwoBaseActivity activity = (LevelTwoBaseActivity) GolfCourseTeePickFragment.this.getActivity();
                GolfCourseProTipFragment fragment = GolfCourseProTipFragment.newInstance(GolfCourseTeePickFragment.this.mCourseId, teeId);
                activity.navigateToFragment(fragment);
            }
        });
    }
}
