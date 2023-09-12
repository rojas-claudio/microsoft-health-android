package com.microsoft.kapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.FiltersListAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class HistoryFilterActivity extends BaseFragmentActivity {
    private FiltersListAdapter<Integer> mAdapter;
    private CustomGlyphView mCloseButton;
    private ListView mFilterListView;

    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_history_filters_dialog);
        View v = getWindow().getDecorView();
        this.mFilterListView = (ListView) ViewUtils.getValidView(v, R.id.filters_list, ListView.class);
        ArrayList<Pair<String, Integer>> filters = new ArrayList<>();
        filters.add(new Pair<>(getFilterString(R.string.label_history_filter_all), Integer.valueOf((int) Constants.FILTER_TYPE_ALL)));
        filters.add(new Pair<>(getFilterString(R.string.label_history_filter_bests), Integer.valueOf((int) Constants.FILTER_TYPE_BESTS)));
        filters.add(new Pair<>(getFilterString(R.string.label_history_filter_runs), Integer.valueOf((int) Constants.FILTER_TYPE_RUNS)));
        filters.add(new Pair<>(getFilterString(R.string.label_history_filter_biking), 256));
        filters.add(new Pair<>(getFilterString(R.string.label_history_filter_golf), 257));
        filters.add(new Pair<>(getFilterString(R.string.label_history_filter_exercises), Integer.valueOf((int) Constants.FILTER_TYPE_EXERCISES)));
        filters.add(new Pair<>(getFilterString(R.string.label_history_filter_sleep), Integer.valueOf((int) Constants.FILTER_TYPE_SLEEP)));
        filters.add(new Pair<>(getFilterString(R.string.label_history_filter_guided_workout), 255));
        this.mAdapter = new FiltersListAdapter<>(getBaseContext(), filters);
        this.mFilterListView.setAdapter((ListAdapter) this.mAdapter);
        this.mFilterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.activities.HistoryFilterActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int filterType = ((Integer) HistoryFilterActivity.this.mAdapter.getItem(i).second).intValue();
                HistoryFilterActivity.this.returnResultAndExit(-1, filterType);
            }
        });
    }

    private String getFilterString(int stringId) {
        return getString(stringId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void returnResultAndExit(int resultCode, int filterType) {
        Intent i = new Intent();
        i.putExtra(Constants.INTENT_HISTORY_FILTER_TYPE, filterType);
        setResult(resultCode, i);
        finish();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_HISTORY_FILTERS_SUMMARY);
    }
}
