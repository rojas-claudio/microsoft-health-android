package com.shinobicontrols.charts;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
@TargetApi(12)
/* loaded from: classes.dex */
public class ChartFragment extends ChartFragmentBase {
    @Override // com.shinobicontrols.charts.ChartFragmentBase
    public final ShinobiChart getShinobiChart() {
        return super.getShinobiChart();
    }

    @Override // com.shinobicontrols.charts.ChartFragmentBase, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // com.shinobicontrols.charts.ChartFragmentBase, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override // com.shinobicontrols.charts.ChartFragmentBase
    v a() {
        return new ce(getActivity());
    }

    @Override // com.shinobicontrols.charts.ChartFragmentBase, android.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.shinobicontrols.charts.ChartFragmentBase, android.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // com.shinobicontrols.charts.ChartFragmentBase, android.app.Fragment
    public final void onDestroyView() {
        super.onDestroyView();
    }
}
