package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.FilterActivityInterface;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.ConfirmationBar;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class FilterFragment extends Fragment {
    private static final String FITLER_TITLE = "Filter_Title";
    private FilterActivityInterface mActivityController;
    private TextView mClearAll;
    private ConfirmationBar mConfirmationBar;
    private Context mContext;
    private LinearLayout mFiltersListView;
    private boolean mSomeFiltersAreSelected;

    public static FilterFragment newInstance(String title) {
        FilterFragment filterFrag = new FilterFragment();
        Bundle arguments = new Bundle();
        arguments.putString(FITLER_TITLE, title);
        filterFrag.setArguments(arguments);
        return filterFrag;
    }

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getHostActivity();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.filter_fragment, container, false);
        this.mFiltersListView = (LinearLayout) ViewUtils.getValidView(view, R.id.filters_list, LinearLayout.class);
        this.mClearAll = (TextView) ViewUtils.getValidView(view, R.id.clear_all, TextView.class);
        Bundle args = getArguments();
        String activityTitle = args.getString(FITLER_TITLE, "");
        TextView headerTitle = (TextView) view.findViewById(R.id.filter_title);
        headerTitle.setText(activityTitle);
        this.mSomeFiltersAreSelected = false;
        this.mActivityController = (FilterActivityInterface) FilterActivityInterface.class.cast(getActivity());
        Set<String> allFiltersDisplayNames = this.mActivityController.getAllFiltersDisplayNames();
        for (final String filterDisplayName : allFiltersDisplayNames) {
            String filterName = this.mActivityController.getFilterName(filterDisplayName);
            List<String> filterValuesList = this.mActivityController.getSingleFilterValuesList(filterName);
            if (filterValuesList != null && filterValuesList.size() > 1) {
                View filterParentView = inflater.inflate(R.layout.workout_filter_single_item, (ViewGroup) this.mFiltersListView, false);
                TextView filterDisplayNameText = (TextView) ViewUtils.getValidView(filterParentView, R.id.workout_filter_display_name, TextView.class);
                filterDisplayNameText.setText(filterDisplayName);
                LinearLayout filterValuesListlayout = (LinearLayout) ViewUtils.getValidView(filterParentView, R.id.workout_filter_values_list, LinearLayout.class);
                populateViewList(filterValuesListlayout, filterName);
                filterParentView.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.FilterFragment.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        FilterFragment.this.mActivityController.startSelectionFragment(filterDisplayName);
                    }
                });
                this.mFiltersListView.addView(filterParentView);
            }
        }
        if (!this.mSomeFiltersAreSelected) {
            this.mClearAll.setVisibility(4);
        }
        this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(view, R.id.confirmation_bar, ConfirmationBar.class);
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.FilterFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FilterFragment.this.returnResultAndExit(-1);
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.FilterFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FilterFragment.this.mActivityController.returnResultAndExit(0);
            }
        });
        this.mClearAll.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.FilterFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FilterFragment.this.mActivityController.clearAllFiltersSelection();
                FilterFragment.this.mSomeFiltersAreSelected = false;
                FilterFragment.this.clearAllFilterValuesView();
                FilterFragment.this.mClearAll.setVisibility(4);
            }
        });
        return view;
    }

    private void populateViewList(ViewGroup parentView, String filterName) {
        List<String> filterValuesList = this.mActivityController.getFilterCriteriaList(filterName);
        if (filterValuesList != null) {
            LayoutInflater inflater = LayoutInflater.from(this.mContext);
            for (String value : filterValuesList) {
                addFilterValueView(inflater, parentView, value);
            }
            if (filterValuesList.size() > 0) {
                this.mSomeFiltersAreSelected = true;
            }
        }
    }

    private void addFilterValueView(LayoutInflater inflater, ViewGroup parentView, String valueName) {
        View singleOptionLine = inflater.inflate(R.layout.workout_filter_selected_single_option, (ViewGroup) null);
        TextView title = (TextView) singleOptionLine.findViewById(R.id.workout_filter_selection_title);
        title.setText(valueName);
        parentView.addView(singleOptionLine);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void returnResultAndExit(int status) {
        this.mActivityController.returnResultAndExit(status);
    }

    protected Activity getHostActivity() {
        return getActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearAllFilterValuesView() {
        int childsCount = this.mFiltersListView.getChildCount();
        for (int i = 0; i < childsCount; i++) {
            ((ViewGroup) ((ViewGroup) this.mFiltersListView.getChildAt(i)).getChildAt(1)).removeAllViews();
        }
    }
}
