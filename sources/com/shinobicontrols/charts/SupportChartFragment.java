package com.shinobicontrols.charts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: classes.dex */
public class SupportChartFragment extends SupportChartFragmentBase {
    @Override // com.shinobicontrols.charts.SupportChartFragmentBase
    public final ShinobiChart getShinobiChart() {
        return super.getShinobiChart();
    }

    @Override // com.shinobicontrols.charts.SupportChartFragmentBase, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // com.shinobicontrols.charts.SupportChartFragmentBase, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override // com.shinobicontrols.charts.SupportChartFragmentBase
    v a() {
        return new ce(getActivity());
    }

    @Override // com.shinobicontrols.charts.SupportChartFragmentBase, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.shinobicontrols.charts.SupportChartFragmentBase, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // com.shinobicontrols.charts.SupportChartFragmentBase, android.support.v4.app.Fragment
    public final void onDestroyView() {
        super.onDestroyView();
    }
}
