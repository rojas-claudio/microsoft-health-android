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
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class FilterSelectionFragment extends Fragment {
    private static final String CURRENT_FILTER_SELECTION = "mCurrentFilterSelection";
    private static final String SELECTED_CRITERIA_LIST = "mSelectedCriteriaList";
    private FilterActivityInterface mActivityController;
    private ConfirmationBar mConfirmationBar;
    private Context mContext;
    private String mCurrentFilterSelection;
    private TextView mFilterTitle;
    private List<String> mSelectedCriteriaList;
    private LinearLayout mSelectionList;

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreSavedData(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
        this.mContext = getHostActivity();
        this.mActivityController = (FilterActivityInterface) FilterActivityInterface.class.cast(getActivity());
        List<String> initiallySelectedCriteriaList = null;
        if (savedInstanceState == null) {
            this.mCurrentFilterSelection = this.mActivityController.getCurrentFilterSelection();
            initiallySelectedCriteriaList = this.mActivityController.getFilterCriteriaList(this.mActivityController.getFilterName(this.mCurrentFilterSelection));
        }
        if (initiallySelectedCriteriaList == null) {
            this.mSelectedCriteriaList = new ArrayList();
        } else {
            this.mSelectedCriteriaList = new ArrayList(initiallySelectedCriteriaList);
        }
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.filter_selection_fragment, container, false);
        this.mFilterTitle = (TextView) ViewUtils.getValidView(view, R.id.workout_filter_title, TextView.class);
        this.mFilterTitle.setText(getFilterTitle());
        this.mSelectionList = (LinearLayout) ViewUtils.getValidView(view, R.id.selection_list, LinearLayout.class);
        this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(view, R.id.confirmation_bar, ConfirmationBar.class);
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.FilterSelectionFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FilterSelectionFragment.this.saveSelectionAndExit();
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.FilterSelectionFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FilterSelectionFragment.this.exit();
            }
        });
        populateViewList(this.mSelectionList);
        return view;
    }

    private void populateViewList(ViewGroup parentView) {
        List<String> filterValuesList = this.mActivityController.getCurrentFilterValuesList();
        if (filterValuesList != null) {
            LayoutInflater inflater = LayoutInflater.from(this.mContext);
            for (String value : filterValuesList) {
                addFilterValueView(inflater, parentView, value);
            }
        }
    }

    private void addFilterValueView(LayoutInflater inflater, ViewGroup parentView, final String valueName) {
        final View singleOptionLine = inflater.inflate(R.layout.workout_filter_selection_single_option, (ViewGroup) null);
        TextView title = (TextView) singleOptionLine.findViewById(R.id.workout_filter_selection_title);
        title.setText(valueName);
        if (this.mSelectedCriteriaList.contains(valueName)) {
            TextView checkboxGlyph = (TextView) singleOptionLine.findViewById(R.id.workout_filter_selection_checkbox);
            checkboxGlyph.setVisibility(0);
        }
        singleOptionLine.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.FilterSelectionFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TextView checkboxGlyph2 = (TextView) singleOptionLine.findViewById(R.id.workout_filter_selection_checkbox);
                if (FilterSelectionFragment.this.mSelectedCriteriaList.contains(valueName)) {
                    FilterSelectionFragment.this.mSelectedCriteriaList.remove(valueName);
                    checkboxGlyph2.setVisibility(8);
                    return;
                }
                FilterSelectionFragment.this.mSelectedCriteriaList.add(valueName);
                checkboxGlyph2.setVisibility(0);
            }
        });
        parentView.addView(singleOptionLine);
    }

    private String getFilterTitle() {
        return this.mActivityController.getCurrentFilterSelection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveSelectionAndExit() {
        this.mActivityController.setSelectedCriteria(this.mSelectedCriteriaList);
        exit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exit() {
        this.mActivityController.goToMainFilterPage();
    }

    protected Activity getHostActivity() {
        return getActivity();
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_FILTER_SELECTION, this.mCurrentFilterSelection);
        outState.putStringArrayList(SELECTED_CRITERIA_LIST, (ArrayList) this.mSelectedCriteriaList);
    }

    private void restoreSavedData(Bundle savedInstanceState) {
        this.mCurrentFilterSelection = savedInstanceState.getString(CURRENT_FILTER_SELECTION);
        this.mSelectedCriteriaList = savedInstanceState.getStringArrayList(SELECTED_CRITERIA_LIST);
    }
}
