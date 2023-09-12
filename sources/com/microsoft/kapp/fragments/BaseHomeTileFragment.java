package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.ChartUtils;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
/* loaded from: classes.dex */
public abstract class BaseHomeTileFragment extends BaseFragmentWithOfflineSupport {
    protected final String TAG = BaseHomeTileFragment.class.getSimpleName();

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override // android.support.v4.app.Fragment
    public void onDetach() {
        super.onDetach();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        MainContentFragment mParentFragment = (MainContentFragment) getParentFragment();
        if (mParentFragment == null || mParentFragment.shouldRefresh(this)) {
            fetchData();
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.setBackground(R.color.app_white);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment
    public final void load() {
        KLog.v(this.TAG, "On Load BaseHomeTileFragment");
    }

    public void fetchData() {
        KLog.v(this.TAG, "BaseHomeTileFragment fetchData");
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void addChartFragment(int resourceID, BaseChartFragment baseChartFragment, String tag) {
        if (baseChartFragment != null && tag != null) {
            try {
                baseChartFragment.pauseChartView();
                FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                if (getFragmentManager().findFragmentByTag(tag) != null) {
                    fragTransaction.replace(resourceID, baseChartFragment, tag);
                } else {
                    fragTransaction.add(resourceID, baseChartFragment, tag);
                }
                fragTransaction.commit();
                baseChartFragment.resumeChartView();
            } catch (Exception ex) {
                KLog.d(this.TAG, "unable to add chart", ex);
            }
        }
    }

    public void requestSnapShotFromChartFragment(final BaseChartFragment baseChartFragment, final ShinobiChart.OnSnapshotDoneListener listener) {
        if (baseChartFragment != null) {
            ChartUtils.prepareChartForScreenshot(baseChartFragment.getChart(), getActivity());
            baseChartFragment.takeSnapShot(new ShinobiChart.OnSnapshotDoneListener() { // from class: com.microsoft.kapp.fragments.BaseHomeTileFragment.1
                @Override // com.shinobicontrols.charts.ShinobiChart.OnSnapshotDoneListener
                public void onSnapshotDone(Bitmap bitmap) {
                    ChartUtils.recoverChartAfterTakingScreenshot(baseChartFragment.getChart());
                    listener.onSnapshotDone(bitmap);
                }
            });
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport, com.microsoft.kapp.fragments.BaseFragment
    protected int getTopMenuDividerVisibility() {
        return 0;
    }
}
