package com.microsoft.kapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.services.SettingsProvider;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class PermissionsFragment extends BaseFragment {
    @Inject
    SettingsProvider mSettingsProvider;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment_permissions, container, false);
        return rootView;
    }
}
