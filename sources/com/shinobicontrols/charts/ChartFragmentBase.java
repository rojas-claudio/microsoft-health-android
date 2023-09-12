package com.shinobicontrols.charts;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
@TargetApi(12)
/* loaded from: classes.dex */
abstract class ChartFragmentBase extends Fragment {
    private v a;

    abstract v a();

    public ShinobiChart getShinobiChart() {
        return this.a;
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        this.a = a();
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.a;
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) this.a.getParent()).removeView(this.a);
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.a != null) {
            this.a.c();
        }
    }

    @Override // android.app.Fragment
    public void onPause() {
        super.onPause();
        if (this.a != null) {
            this.a.d();
        }
    }
}
