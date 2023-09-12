package com.microsoft.kapp.fragments.golf;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public abstract class GolfFindCourseBaseFragment extends BaseFragment {
    private ListView mListView;
    TextView mNoResults;

    public abstract int getPageHeaderTextResId();

    abstract ListAdapter getPageListAdapter(Context context);

    abstract void onListItemClicked(int i);

    abstract void restoreSaveInstanceState(Bundle bundle);

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        restoreSaveInstanceState(savedInstanceState);
        View view = inflater.inflate(R.layout.golf_find_course_base, container, false);
        this.mListView = (ListView) ViewUtils.getValidView(view, R.id.course_listview, ListView.class);
        this.mNoResults = (TextView) ViewUtils.getValidView(view, R.id.course_no_results, TextView.class);
        this.mListView.setAdapter(getPageListAdapter(getActivity()));
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfFindCourseBaseFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                GolfFindCourseBaseFragment.this.onListItemClicked(position);
            }
        });
        return view;
    }
}
